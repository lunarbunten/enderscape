package net.penumbra.enderscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.penumbra.enderscape.registry.EnderscapeRegistries;
import net.penumbra.enderscape.registry.block.EnderscapeBlockEntities;
import net.penumbra.enderscape.registry.server.EnderscapeCriteria;
import net.penumbra.enderscape.registry.sound.EnderscapeMagniaRadioSongs;
import net.penumbra.enderscape.sound.MagniaRadioSong;
import net.penumbra.enderscape.sound.MagniaRadioSongPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static net.penumbra.enderscape.block.state.StateProperties.RADIO_IS_PLAYING;

public class MagniaRadioBlockEntity extends BlockEntity {

    private static final Predicate<SongPlayingContext> BIOME_FILTER = context -> context.song().permittedBiomes().isEmpty() || context.level().getBiome(context.pos()).is(context.song().permittedBiomes().get());
    private static final Predicate<SongPlayingContext> EXCLUSIVE_FILTER = context -> context.song().exclusiveSignal().isEmpty() || context.song().exclusiveSignal().get() == context.level().getBestNeighborSignal(context.pos());
    private static final Predicate<SongPlayingContext> SKIP_LAST_SONG_FILTER = context -> !context.song().equals(context.entity().getLastSong().map(Holder::value).orElse(null));

    private static final UniformInt INITIAL_TICKS_UNTIL_SONG_RANGE_IN_SECONDS = UniformInt.of(3, 6);
    private static final UniformInt REPEAT_TICKS_UNTIL_SONG_RANGE_IN_SECONDS = UniformInt.of(30, 240);

    public static final String CURRENT_SONG_ID = "current_song";
    public static final String LAST_SONG_ID = "last_song";

    public static final String TICKS_UNTIL_SONG_ID = "ticks_until_song";
    public static final String TICKS_SINCE_SONG_STARTED_ID = "ticks_since_song_started";

    private Optional<Holder<MagniaRadioSong>> lastSong = Optional.empty();
    private Optional<Holder<MagniaRadioSong>> currentSong = Optional.empty();

    private long ticksUntilSong = -1L;

    private final MagniaRadioSongPlayer player = new MagniaRadioSongPlayer(this::onSongChanged, getBlockPos());

    public MagniaRadioBlockEntity(BlockPos pos, BlockState state) {
        super(EnderscapeBlockEntities.MAGNIA_RADIO, pos, state);

        resetTicksUntilSong(false);
    }

    public void resetTicksUntilSong(boolean finishedSong) {
        if (hasLevel()) {
            UniformInt range = finishedSong ? REPEAT_TICKS_UNTIL_SONG_RANGE_IN_SECONDS : INITIAL_TICKS_UNTIL_SONG_RANGE_IN_SECONDS;
            ticksUntilSong = range.sample(getLevel().getRandom()) * 20L;
        }
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

    public void setSong(Optional<Holder<MagniaRadioSong>> song) {
        notifySongChanged(song.isPresent());

        if (level != null) {
            if (song.isPresent()) {
                player.play(level, song.get());

                if (!song.get().is(EnderscapeMagniaRadioSongs.FALLBACK)) {
                    lastSong = song;
                }
            } else {
                player.stop(level, getBlockState(), true);
                ticksUntilSong = 0L;
            }
        }
    }

    private void notifySongChanged(boolean value) {
        if (level != null) {
            level.setBlock(getBlockPos(), getBlockState().setValue(RADIO_IS_PLAYING, value), 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, getBlockPos(), GameEvent.Context.of(getBlockState()));
        }
    }

    private void validateFallback() {
        if (level instanceof ServerLevel server) {
            server.registryAccess().lookupOrThrow(EnderscapeRegistries.MAGNIA_RADIO_SONG).get(EnderscapeMagniaRadioSongs.FALLBACK).orElseThrow();
        }
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);

        validateFallback();
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        Optional<Holder<MagniaRadioSong>> possible = input.read(CURRENT_SONG_ID, MagniaRadioSong.CODEC);

        possible.ifPresent(value -> {
            if (currentSong.isPresent() && !currentSong.get().equals(value)) {
                player.stop(level, getBlockState(), true);
            }

            currentSong = Optional.of(value);
        });

        input.read(LAST_SONG_ID, MagniaRadioSong.CODEC).ifPresent(value -> lastSong = Optional.of(value));
        input.getLong(TICKS_SINCE_SONG_STARTED_ID).ifPresent(value -> currentSong.ifPresent(song -> player.setSongWithoutPlaying(song, value)));
        input.getLong(TICKS_UNTIL_SONG_ID).ifPresent(value -> ticksUntilSong = value);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        currentSong.ifPresent(value -> output.store(CURRENT_SONG_ID, MagniaRadioSong.CODEC, value));
        lastSong.ifPresent(value -> output.store(LAST_SONG_ID, MagniaRadioSong.CODEC, value));

        if (player.getSong() != null) {
            output.putLong(TICKS_SINCE_SONG_STARTED_ID, player.getTicksSinceSongStarted());
        }

        output.putLong(TICKS_UNTIL_SONG_ID, ticksUntilSong);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        player.stop(level, getBlockState(), false);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, MagniaRadioBlockEntity entity) {
        if (entity.ticksUntilSong < 0) entity.resetTicksUntilSong(false);

        if (MagniaRadioBlock.isPlaying(state)) {
            MagniaRadioSongPlayer player = entity.getSongPlayer();
            boolean wasPlaying = player.isPlaying();

            player.tick(level, state);

            if (wasPlaying && !player.isPlaying()) {
                entity.resetTicksUntilSong(true);
            }
        } else {
            if (entity.ticksUntilSong-- <= 0L) {
                tryPlaySong(entity, level, pos);
            } else if (level.getRandom().nextInt(3200) == 0) {
                playAmbientSound(level, pos);
            }
        }
    }

    public static void playAmbientSound(Level level, BlockPos pos) {
        level.levelEvent(null, -624641, pos, 0);
    }

    public static void tryPlaySong(MagniaRadioBlockEntity entity, Level level, BlockPos pos) {
        playSong(entity, level, pos, () -> {
            Registry<MagniaRadioSong> registry = level.registryAccess().lookupOrThrow(EnderscapeRegistries.MAGNIA_RADIO_SONG);
            Predicate<SongPlayingContext> powerFilter = MagniaRadioBlock.isPowered(entity.getBlockState()) ? EXCLUSIVE_FILTER : SKIP_LAST_SONG_FILTER;

            return pickSong(entity, registry, registry.stream().filter(song -> {
                SongPlayingContext context = new SongPlayingContext(entity, level, pos, song);
                return context.passes(BIOME_FILTER, powerFilter);
            }).toList());
        });
    }

    public static void tryStopPlaying(Level level, BlockPos pos, @Nullable Player player) {
        BlockState state = level.getBlockState(pos);
        if (state.hasProperty(RADIO_IS_PLAYING) && state.getValue(RADIO_IS_PLAYING)) {
            if (!level.isClientSide()) {
                if (level.getBlockEntity(pos) instanceof MagniaRadioBlockEntity entity) {
                    entity.setSong(Optional.empty());
                    level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
                }
            }
        }
    }

    private static void playSong(MagniaRadioBlockEntity entity, Level level, BlockPos pos, Supplier<Optional<Holder<MagniaRadioSong>>> songSupplier) {
        BlockState state = level.getBlockState(pos);

        if (state.hasProperty(RADIO_IS_PLAYING) && !state.getValue(RADIO_IS_PLAYING)) {
            if (!level.isClientSide()) {
                Optional<Holder<MagniaRadioSong>> song = songSupplier.get();

                entity.setSong(song);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(null, state));
                triggerHearMagniaRadioCriterion(level, pos, song);
            }
        }
    }

    private static void triggerHearMagniaRadioCriterion(Level level, BlockPos pos, Optional<Holder<MagniaRadioSong>> song) {
        song.flatMap(Holder::unwrapKey).ifPresent(key -> level.players().stream()
                .filter(player -> player.level().dimensionType().equals(level.dimensionType()) && Math.sqrt(player.distanceToSqr(pos.getCenter())) < 32)
                .filter(ServerPlayer.class::isInstance)
                .map(ServerPlayer.class::cast)
                .forEach(player -> EnderscapeCriteria.HEAR_MAGNIA_RADIO_SONG.trigger(
                        player,
                        new GlobalPos(player.level().dimension(), player.getOnPos()),
                        key
                )));
    }

    private static Optional<Holder<MagniaRadioSong>> pickSong(MagniaRadioBlockEntity entity, Registry<MagniaRadioSong> registry, List<MagniaRadioSong> candidates) {
        if (candidates.isEmpty()) {
            return entity.lastSong.or(() -> Optional.of(registry.getOrThrow(EnderscapeMagniaRadioSongs.FALLBACK)));
        } else {
            RandomSource random = entity.getLevel().getRandom();
            MagniaRadioSong picked = candidates.get(random.nextInt(candidates.size()));

            return wrapSong(registry, picked);
        }
    }

    private static Optional<Holder<MagniaRadioSong>> wrapSong(Registry<MagniaRadioSong> registry, MagniaRadioSong song) {
        return Optional.of(registry.wrapAsHolder(song));
    }

    record SongPlayingContext(MagniaRadioBlockEntity entity, Level level, BlockPos pos, MagniaRadioSong song) {

        @SafeVarargs
        public final boolean passes(final Predicate<SongPlayingContext>... predicates) {
            for (Predicate<SongPlayingContext> predicate : predicates) {
                if (!predicate.test(this)) {
                    return false;
                }
            }
            return true;
        }
    }
}