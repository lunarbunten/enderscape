package net.bunten.enderscape.client.sound;

import net.bunten.enderscape.sound.MagniaRadioSong;
import net.bunten.enderscape.registry.EnderscapeBlockSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class ClientsideMagniaRadioHandler {

    public final Minecraft client = Minecraft.getInstance();

    public final Map<BlockPos, SoundInstance> activeAmbientInstances = new HashMap();
    public final Map<BlockPos, SoundInstance> activeMusicInstances = new HashMap();

    public void notifyNearbyEntities(Level level, BlockPos pos, boolean value) {
        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(3.0))) {
            entity.setRecordPlayingNearby(pos, value);
        }
    }

    public void stopAllInstances(BlockPos pos) {
        SoundInstance ambient = activeAmbientInstances.remove(pos);
        if (ambient != null) client.getSoundManager().stop(ambient);

        SoundInstance music = activeMusicInstances.remove(pos);
        if (music != null) client.getSoundManager().stop(music);
    }

    public void playAmbientSound(BlockPos pos) {
        stopAllInstances(pos);

        Vec3 center = Vec3.atCenterOf(pos);

        SimpleSoundInstance instance = SimpleSoundInstance.forAmbientMood(
                EnderscapeBlockSounds.MAGNIA_RADIO_AMBIENT,
                client.level.getRandom(),
                center.x(),
                center.y(),
                center.z()
        );

        activeAmbientInstances.put(pos, instance);
        client.getSoundManager().play(instance);

        notifyNearbyEntities(client.level, pos, true);
    }

    public void playSong(Holder<MagniaRadioSong> holder, BlockPos pos) {
        stopAllInstances(pos);

        MagniaRadioSong song = holder.value();
        SoundInstance instance = SimpleSoundInstance.forJukeboxSong(song.soundEvent().value(), Vec3.atCenterOf(pos));
        activeMusicInstances.put(pos, instance);
        client.getSoundManager().play(instance);
        client.gui.hud.setNowPlaying(song.description());

        notifyNearbyEntities(client.level, pos, true);
    }
}