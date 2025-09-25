package net.bunten.enderscape.client.sound;

import net.bunten.enderscape.client.EnderscapeClient;
import net.bunten.enderscape.registry.EnderscapeEntitySounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class EndermanStaticSoundInstance extends AbstractTickableSoundInstance {
    private final Minecraft client;

    public EndermanStaticSoundInstance(Minecraft client) {
        super(EnderscapeEntitySounds.ENDERMAN_STATIC, SoundSource.HOSTILE, SoundInstance.createUnseededRandom());
        this.client = client;
        this.looping = true;
        delay = 0;
        pitch = 0.96F;
        volume = 0.02F;
    }

    @Override
    public void tick() {
        if (client.level == null || volume < 0.01F) remove();

        float target = Mth.clamp((float) EnderscapeClient.stareTicks / ((float) EnderscapeClient.MAX_STARE_STICKS / 3), 0.0F, 1.0F);

        float lerpFactor = volume < target ? 0.04F : 0.2F;
        volume = Mth.lerp(lerpFactor, volume, target);
        pitch = Mth.lerp(volume, 0.96F, 1.0F);
    }

    private void remove() {
        stop();
        EnderscapeClient.staticSoundInstance = null;
    }
}