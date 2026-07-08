package net.bunten.enderscape.entity.magnia;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.particle.MagniaParticleOptions;
import net.bunten.enderscape.registry.tag.EnderscapeEntityTags;
import net.bunten.enderscape.registry.tag.EnderscapeItemTags;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public interface MagniaMoveable {

    Predicate<Entity> DEFAULT_MAGNIA_PREDICATE = (entity) -> (entity.getType().is(EnderscapeEntityTags.AFFECTED_BY_MAGNIA) || getMagnetismFactor(entity) > 0) && EntitySelector.NO_SPECTATORS.test(entity);
    AttributeModifier MAGNIA_GRAVITY_MODIFIER = new AttributeModifier(Enderscape.id("magnia_gravity"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

    static void sendEntityEffectParticles(ServerLevel level, Entity entity, MagniaParticleOptions options, float chance) {
        if (level != null && entity != null && entity.isAlive() && (entity.getDeltaMovement().lengthSqr() > 0.02 || entity.getRandom().nextInt(12) == 0)) {
            AABB box = entity.getBoundingBox();
            Vec3 pos = entity.position().add(0, box.getYsize() / (entity instanceof ItemEntity ? 0.5F : 2), 0);

            if (level.random.nextFloat() <= chance) level.sendParticles(options, pos.x, pos.y, pos.z, 1, box.getXsize() * 0.6, box.getYsize() * 0.6, box.getZsize() * 0.6, 1);
        }
    }

    MagniaProperties createMagniaProperties();

    @Nullable
    static MagniaProperties getMagniaProperties(Entity entity) {
        if (entity instanceof MagniaMoveable moveable) {
            return moveable.createMagniaProperties();
        }
        return null;
    }

    static boolean canMagniaAffect(Entity entity) {
        if (!(entity instanceof MagniaMoveable)) return false;

        MagniaProperties properties = getMagniaProperties(entity);
        if (properties != null) {
            Predicate<Entity> predicate = properties.isAllowed();
            return predicate.test(entity);
        }
        return false;
    }

    EntityDataAccessor<Integer> Enderscape$magniaCooldownData();

    static boolean is(Entity entity) {
        return entity instanceof MagniaMoveable;
    }

    static float getMagnetismFactor(Entity entity) {
        float factor = 0;

        if (entity instanceof LivingEntity mob) {
            int weak = 0, average = 0, strong = 0;

            for (ItemStack stack : mob.getArmorSlots()) {
                if (stack.is(EnderscapeItemTags.WEAK_MAGNIA_STRENGTH)) weak++;
                else if (stack.is(EnderscapeItemTags.AVERAGE_MAGNIA_STRENGTH)) average++;
                else if (stack.is(EnderscapeItemTags.STRONG_MAGNIA_STRENGTH)) strong++;
            }

            factor += weak * 0.5F + average + strong * 1.5F;
        }

        return factor;
    }

    default void defineMagniaData(SynchedEntityData.Builder builder) {
        builder.define(Enderscape$magniaCooldownData(), 0);
    }

    static int getMagniaCooldown(Entity entity) {
        if (entity instanceof MagniaMoveable moveable) {
            return entity.getEntityData().get(moveable.Enderscape$magniaCooldownData());
        }
        return -1;
    }

    static void setMagniaCooldown(Entity entity, int value) {
        if (!canMagniaAffect(entity)) return;
        entity.getEntityData().set(((MagniaMoveable) entity).Enderscape$magniaCooldownData(), value);
        if (getMagniaCooldown(entity) == 0) getMagniaProperties(entity).onStopMoving().apply(entity);
    }


    static boolean wasMovedByMagnia(Entity entity) {
        if (entity instanceof MagniaMoveable) {
            return getMagniaCooldown(entity) > 0;
        }
        return false;
    }

    static void setMovedByMagnia(Entity entity, boolean value) {
        if (entity instanceof MagniaMoveable moveable) {
            entity.getEntityData().set(moveable.Enderscape$magniaCooldownData(), value ? 5 : 0);
        }
    }

    static void tickMagniaCooldown(Entity entity) {
        int cooldown = getMagniaCooldown(entity);
        if (cooldown > 0) setMagniaCooldown(entity, cooldown - 1);
    }
}