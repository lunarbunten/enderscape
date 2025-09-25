package net.bunten.enderscape.block;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.registry.EnderscapeBlockEntities;
import net.bunten.enderscape.registry.EnderscapeCriteria;
import net.bunten.enderscape.registry.EnderscapeRegistries;
import net.bunten.enderscape.sound.MagniaRadioSong;
import net.bunten.enderscape.sound.MagniaRadioSongPlayer;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static net.bunten.enderscape.block.state.StateProperties.IS_PLAYING;

public class MagniaRadioBlockEntity extends BlockEntity {
    public static final String MAGNIA_RADIO_SONG_TAG_ID = "MagniaRadioSong";
    public static final String LAST_MAGNIA_RADIO_SONG_TAG_ID = "LastMagniaRadioSong";
    public static final String TICKS_SINCE_SONG_STARTED_TAG_ID = "ticks_since_song_started";

    private Optional<Holder<MagniaRadioSong>> lastSong = Optional.empty();
    private Optional<Holder<MagniaRadioSong>> currentSong = Optional.empty();

    private final MagniaRadioSongPlayer player = new MagniaRadioSongPlayer(this::onSongChanged, getBlockPos());

    public MagniaRadioBlockEntity(BlockPos pos, BlockState state) {
        super(EnderscapeBlockEntities.MAGNIA_RADIO, pos, state);
    }

    public Optional<Holder<MagniaRadioSong>> getLastSong() {
        return lastSong;
    }

    public MagniaRadioSongPlayer getSongPlayer() {
        return player;
    }

    public void onSongChanged() {
        level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());
        setChanged();
    }

    public static void playAmbientSound(Level level, BlockPos pos) {
        level.levelEvent(null, -624641, pos, 0);
    }

    public static boolean tryPlaySong(MagniaRadioBlockEntity entity, Level level, BlockPos pos) {
        return playSong(entity, level, pos, () -> {
            Registry<MagniaRadioSong> songs = level.registryAccess().registryOrThrow(EnderscapeRegistries.MAGNIA_RADIO_SONG);
            Predicate<MagniaRadioSong> biomeFilter = song -> level.getBiome(pos).is(song.permittedBiomes());
            Predicate<MagniaRadioSong> powerFilter = MagniaRadioBlock.isPowered(entity.getBlockState()) ? getExclusiveFilter(level, pos) : getRandomFilter(entity);
            List<MagniaRadioSong> candidates = songs.stream().filter(biomeFilter.and(powerFilter)).toList();

            return pickSong(candidates, songs, level.getRandom());
        });
    }

    private static Predicate<MagniaRadioSong> getExclusiveFilter(Level level, BlockPos pos) {
        return song -> song.exclusiveSignal() == level.getBestNeighborSignal(pos);
    }

    private static Predicate<MagniaRadioSong> getRandomFilter(MagniaRadioBlockEntity entity) {
        return song -> !song.equals(entity.getLastSong().map(Holder::value).orElse(null));
    }

    private static boolean playSong(MagniaRadioBlockEntity entity, Level level, BlockPos pos, Supplier<Optional<Holder<MagniaRadioSong>>> songSupplier) {
        BlockState state = level.getBlockState(pos);

        if (state.hasProperty(IS_PLAYING) && !state.getValue(IS_PLAYING)) {
            if (!level.isClientSide()) {
                Optional<Holder<MagniaRadioSong>> song = songSupplier.get();

                entity.setSong(song);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(null, state));

                song.ifPresent(holder -> {
                    Optional<ResourceKey<MagniaRadioSong>> optional = holder.unwrapKey();
                    optional.ifPresent(key -> {
                        level.players().stream()
                                .filter(player -> player.level().dimensionType() == level.dimensionType() && player.distanceToSqr(pos.getCenter()) < 32)
                                .filter(ServerPlayer.class::isInstance)
                                .map(ServerPlayer.class::cast)
                                .forEach(player -> EnderscapeCriteria.HEAR_MAGNIA_RADIO_SONG.trigger(
                                        player,
                                        new GlobalPos(player.level().dimension(), player.getOnPos()),
                                        optional.get()
                                ));
                    });
                });
            }
            return true;
        }

        return false;
    }

    private static Optional<Holder<MagniaRadioSong>> pickSong(List<MagniaRadioSong> candidates, Registry<MagniaRadioSong> songs, RandomSource random) {
        return candidates.isEmpty() ? Optional.empty() : Optional.of(songs.wrapAsHolder(candidates.get(random.nextInt(candidates.size()))));
    }

    public static InteractionResult tryStopPlaying(Level level, BlockPos pos, @Nullable Player player) {
        BlockState state = level.getBlockState(pos);
        if (state.hasProperty(IS_PLAYING) && state.getValue(IS_PLAYING)) {
            if (!level.isClientSide()) {
                if (level.getBlockEntity(pos) instanceof MagniaRadioBlockEntity entity) {
                    entity.setSong(Optional.empty());
                    level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
                }
            }

            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    public void setSong(Optional<Holder<MagniaRadioSong>> song) {
        notifySongChanged(song.isPresent());

        if (song.isPresent()) {
            player.play(level, song.get());
            lastSong = song;
        } else {
            player.stop(level, getBlockState(), true);
        }
    }

    private void notifySongChanged(boolean value) {
        if (level != null && level.getBlockState(getBlockPos()) == getBlockState()) {
            level.setBlock(getBlockPos(), getBlockState().setValue(IS_PLAYING, value), 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, getBlockPos(), GameEvent.Context.of(getBlockState()));
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, MagniaRadioBlockEntity entity) {
        entity.player.tick(level, state);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);

        if (tag.contains(MAGNIA_RADIO_SONG_TAG_ID, 10)) {
            Optional<Holder<MagniaRadioSong>> holder = MagniaRadioSong.CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag.getCompound(MAGNIA_RADIO_SONG_TAG_ID)).resultOrPartial(string -> Enderscape.LOGGER.error("Tried to load invalid Magnia Radio Song: '{}'", string));
            if (currentSong.isPresent() && currentSong.get() == holder.get()) player.stop(level, getBlockState(), true);
            currentSong = Optional.of(holder.get());
        }

        if (tag.contains(LAST_MAGNIA_RADIO_SONG_TAG_ID, 10)) {
            Optional<Holder<MagniaRadioSong>> holder = MagniaRadioSong.CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag.getCompound(LAST_MAGNIA_RADIO_SONG_TAG_ID)).resultOrPartial(string -> Enderscape.LOGGER.error("Tried to load invalid Magnia Radio Song: '{}'", string));
            lastSong = Optional.of(holder.get());
        }

        if (tag.contains(TICKS_SINCE_SONG_STARTED_TAG_ID)) {
            currentSong.ifPresent(song -> player.setSongWithoutPlaying(song, tag.getLong(TICKS_SINCE_SONG_STARTED_TAG_ID)));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);

        currentSong.ifPresent(holder -> tag.put(MAGNIA_RADIO_SONG_TAG_ID, MagniaRadioSong.CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), holder).getOrThrow()));
        lastSong.ifPresent(holder -> tag.put(LAST_MAGNIA_RADIO_SONG_TAG_ID, MagniaRadioSong.CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), holder).getOrThrow()));

        if (player.getSong() != null) {
            tag.putLong(TICKS_SINCE_SONG_STARTED_TAG_ID, player.getTicksSinceSongStarted());
        }
    }
}