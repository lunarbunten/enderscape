package net.penumbra.enderscape.sound;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.block.state.StateProperties;
import net.penumbra.enderscape.registry.EnderscapeRegistries;
import org.jetbrains.annotations.Nullable;

public class MagniaRadioSongPlayer {
    private long ticksSinceSongStarted;

    @Nullable
    private Holder<MagniaRadioSong> song;
    private final BlockPos pos;
    private final MagniaRadioSongPlayer.OnSongChanged onChanged;

    public MagniaRadioSongPlayer(MagniaRadioSongPlayer.OnSongChanged onChanged, BlockPos pos) {
        this.onChanged = onChanged;
        this.pos = pos;
    }

    public boolean isPlaying() {
        return song != null;
    }

    @Nullable
    public MagniaRadioSong getSong() {
        return song == null ? null : song.value();
    }

    public long getTicksSinceSongStarted() {
        return ticksSinceSongStarted;
    }

    public void setSongWithoutPlaying(Holder<MagniaRadioSong> holder, long time) {
        if (!holder.value().hasFinished(time)) {
            song = holder;
            ticksSinceSongStarted = time;
        }
    }

    public void play(LevelAccessor level, Holder<MagniaRadioSong> holder) {
        song = holder;
        ticksSinceSongStarted = 0L;
        int value = level.registryAccess().lookupOrThrow(EnderscapeRegistries.MAGNIA_RADIO_SONG).getId(song.value());
        level.levelEvent(null, -624642, pos, value);
        onChanged.notifyChange();
    }

    public void stop(LevelAccessor level, @Nullable BlockState state, boolean updateBlock) {
        if (song != null) {
            song = null;
            ticksSinceSongStarted = 0L;
            level.gameEvent(GameEvent.JUKEBOX_STOP_PLAY, pos, GameEvent.Context.of(state));
            level.levelEvent(-624643, pos, 0);
            if (state != null && updateBlock) level.setBlock(pos, state.setValue(StateProperties.RADIO_IS_PLAYING, false), 2);
            onChanged.notifyChange();
        }
    }

    public void tick(LevelAccessor level, @Nullable BlockState state) {
        if (song != null) {
            if (song.value().hasFinished(ticksSinceSongStarted)) {
                stop(level, state, true);
            } else {
                if (shouldEmitRadioPlayingEvent()) {
                    level.gameEvent(GameEvent.JUKEBOX_PLAY, pos, GameEvent.Context.of(state));
                    spawnMusicParticles(level, pos);
                }

                ticksSinceSongStarted++;
            }
        }
    }

    private boolean shouldEmitRadioPlayingEvent() {
        return ticksSinceSongStarted % 20L == 0L;
    }

    private static void spawnMusicParticles(LevelAccessor level, BlockPos pos) {
        if (level instanceof ServerLevel server) {
            Vec3 vec3 = Vec3.atBottomCenterOf(pos).add(0.0, 1.2F, 0.0);
            float f = level.getRandom().nextInt(4) / 24.0F;
            server.sendParticles(ParticleTypes.NOTE, vec3.x(), vec3.y(), vec3.z(), 0, f, 0.0, 0.0, 1.0);
        }
    }

    @FunctionalInterface
    public interface OnSongChanged {
        void notifyChange();
    }
}
