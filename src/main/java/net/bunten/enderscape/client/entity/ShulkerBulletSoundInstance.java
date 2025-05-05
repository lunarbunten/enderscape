package net.bunten.enderscape.client.entity;

import net.bunten.enderscape.registry.EnderscapeEntitySounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.world.entity.projectile.ShulkerBullet;

@Environment(EnvType.CLIENT)
public class ShulkerBulletSoundInstance extends AbstractTickableSoundInstance {
    protected final ShulkerBullet bullet;

    public ShulkerBulletSoundInstance(ShulkerBullet bullet) {
        super(EnderscapeEntitySounds.SHULKER_BULLET_LOOP, bullet.getSoundSource(), SoundInstance.createUnseededRandom());
        this.bullet = bullet;
        x = bullet.getX();
        y = bullet.getY();
        z = bullet.getZ();
        looping = true;
        delay = 0;
        volume = 1;
    }

    @Override
    public void tick() {
        if (!bullet.isRemoved()) {
            x = bullet.getX();
            y = bullet.getY();
            z = bullet.getZ();
        } else {
            stop();
        }
    }

    @Override
    public boolean canPlaySound() {
        return !bullet.isSilent();
    }
}