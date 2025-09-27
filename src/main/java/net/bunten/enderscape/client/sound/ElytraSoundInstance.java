package net.bunten.enderscape.client.sound;

import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public class ElytraSoundInstance extends AbstractTickableSoundInstance {
    private final Player player;
    private int time;

    public ElytraSoundInstance(Player player) {
        super(EnderscapeItemSounds.ELYTRA_GLIDING, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.player = player;
        looping = true;
        delay = 0;
        volume = 0.1F;
    }

    @Override
    public void tick() {
        time++;

        if (player.isRemoved() || (time > 20 && !player.isFallFlying())) {
            stop();
            return;
        }

        x = (float) player.getX();
        y = (float) player.getY();
        z = (float) player.getZ();

        float movementSpeed = (float) player.getDeltaMovement().lengthSqr();
        volume = movementSpeed >= 1.0E-7 ? Mth.clamp(movementSpeed / 4.0F, 0.0F, 1.0F) : 0.0F;

        if (time < 20) {
            volume = 0.0F;
        } else if (time < 40) {
            volume *= (float) (time - 20) / 20.0F;
        }

        pitch = volume > 0.8F ? 1.0F + (volume - 0.8F) : 1.0F;
    }
}