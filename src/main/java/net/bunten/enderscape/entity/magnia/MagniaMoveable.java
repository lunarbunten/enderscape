package net.bunten.enderscape.entity.magnia;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.registry.tag.EnderscapeEntityTags;
import net.bunten.enderscape.registry.tag.EnderscapeItemTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public interface MagniaMoveable {

    Predicate<Entity> DEFAULT_MAGNIA_PREDICATE = (entity) -> (entity.getType().is(EnderscapeEntityTags.AFFECTED_BY_MAGNIA) || getMagnetismFactor(entity) > 0) && EntitySelector.NO_SPECTATORS.test(entity);
    AttributeModifier MAGNIA_GRAVITY_MODIFIER = new AttributeModifier(Enderscape.id("magnia_gravity"), 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);

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

            for (EquipmentSlot slot : EquipmentSlot.VALUES) {
                ItemStack stack = mob.getItemBySlot(slot);
                if (stack.has(DataComponents.EQUIPPABLE)) {
                    if (stack.is(EnderscapeItemTags.WEAK_MAGNETISM_WHEN_WORN)) weak++;
                    else if (stack.is(EnderscapeItemTags.AVERAGE_MAGNETISM_WHEN_WORN)) average++;
                    else if (stack.is(EnderscapeItemTags.STRONG_MAGNETISM_WHEN_WORN)) strong++;
                }
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
            entity.getEntityData().set(moveable.Enderscape$magniaCooldownData(), value ? 20 : 0);
        }
    }

    static void tickMagniaCooldown(Entity entity) {
        int cooldown = getMagniaCooldown(entity);
        if (cooldown > 0) setMagniaCooldown(entity, cooldown - 1);
    }
}