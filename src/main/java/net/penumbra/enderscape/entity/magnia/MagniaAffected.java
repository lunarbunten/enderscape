package net.penumbra.enderscape.entity.magnia;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.particle.MagniaParticleOptions;

import static net.penumbra.enderscape.registry.entity.EnderscapeAttachments.MAGNIA_COOLDOWN_TIME;

public class MagniaAffected {

    public static int cooldown(Entity entity) {
        return entity.getAttachedOrElse(MAGNIA_COOLDOWN_TIME, -1);
    }

    public static void setCooldown(Entity entity, int value) {
        entity.setAttached(MAGNIA_COOLDOWN_TIME, value);

        if (cooldown(entity) == 0) {
            MagniaInteractionBehavior.get(entity).stopMoving().apply(entity);
        }
    }

    public static boolean wasMoved(Entity entity) {
        return cooldown(entity) > 0;
    }

    public static void setMoved(Entity entity, boolean value) {
        if (value) {
            entity.setAttached(MAGNIA_COOLDOWN_TIME, 5);
        } else {
            entity.removeAttached(MAGNIA_COOLDOWN_TIME);
        }
    }

    public static void tickCooldown(Entity entity) {
        int cooldown = cooldown(entity);

        if (cooldown > 0) {
            setCooldown(entity, cooldown - 1);
        }
    }

    public static void sendEntityEffectParticles(ServerLevel level, Entity entity, MagniaParticleOptions options, float chance) {
        if (level != null && entity != null && entity.isAlive() && (entity.getDeltaMovement().lengthSqr() > 0.02 || entity.getRandom().nextInt(12) == 0)) {
            AABB box = entity.getBoundingBox();
            Vec3 pos = entity.position().add(0, box.getYsize() / (entity instanceof ItemEntity ? 0.5F : 2), 0);

            if (level.getRandom().nextFloat() <= chance) level.sendParticles(options, pos.x, pos.y, pos.z, 1, box.getXsize() * 0.6, box.getYsize() * 0.6, box.getZsize() * 0.6, 1);
        }
    }
}