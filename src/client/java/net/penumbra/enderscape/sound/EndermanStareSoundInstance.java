package net.penumbra.enderscape.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.EnderMan;
import net.penumbra.enderscape.registry.sound.EnderscapeEntitySounds;

@Environment(EnvType.CLIENT)
public class EndermanStareSoundInstance extends AbstractTickableSoundInstance {
    private final Minecraft client;
    private final int entityId;

    public EndermanStareSoundInstance(Minecraft client, int entityId) {
        super(EnderscapeEntitySounds.ENDERMAN_STARE_STEREO, SoundSource.HOSTILE, SoundInstance.createUnseededRandom());

        this.client = client;
        this.entityId = entityId;

        delay = 5;
        volume = 1.0F;
        pitch = 1.0F;
    }

    @Override
    public void tick() {
       if (client.level == null || Minecraft.getInstance().level == null || volume < 0.01F) {
           stop();
       } else {
           if (!canPlay()) {
               volume = Mth.lerp(0.1F, volume, 0.0F);
               pitch = Mth.lerp(volume, 0.8F, 1.0F);
           }
       }
    }

    private boolean canPlay() {
        return client.level != null && client.level.getEntity(entityId) instanceof EnderMan enderman && enderman.isAlive() && enderman.isCreepy();
    }
}