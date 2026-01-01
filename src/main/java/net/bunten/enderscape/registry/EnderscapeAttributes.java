package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import static net.bunten.enderscape.registry.EnderscapeDataComponents.BACKSTAB_ANGLE;

public class EnderscapeAttributes {

    public static final ResourceLocation BASE_BACKSTAB_DAMAGE_ID = Enderscape.id("base_backstab_damage");

    public static final Holder<Attribute> BACKSTAB_DAMAGE = register("backstab_damage", new RangedAttribute("attribute.enderscape.backstab_damage", 0, 0, 2048).setSyncable(true));
    public static final Holder<Attribute> STEALTH = register("stealth", new RangedAttribute("attribute.enderscape.stealth", 1, 0, 2).setSyncable(true));

    public static final int DEFAULT_BACKSTAB_ANGLE = 110;

    private static Holder<Attribute> register(String string, Attribute attribute) {
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, Enderscape.id(string), attribute);
    }

    public static double getBackstabDamage(Entity entity) {
        double fallback = 1.0;

        if (entity instanceof LivingEntity living) {
            AttributeInstance instance = living.getAttribute(EnderscapeAttributes.BACKSTAB_DAMAGE);
            return instance != null ? instance.getValue() : fallback;
        }

        return fallback;
    }

    public static double getStealthValue(Entity entity) {
        double fallback = 1.0;

        if (entity instanceof LivingEntity living) {
            AttributeInstance instance = living.getAttribute(EnderscapeAttributes.STEALTH);
            return instance != null ? instance.getValue() : fallback;
        }

        return fallback;
    }

    public static double getStealthMultiplier(Entity entity) {
        return 2 - EnderscapeAttributes.getStealthValue(entity);
    }

    public static boolean isBackstab(ItemStack stack, Vec3 sourcePos, Entity victim) {
        int angle = stack != null && !stack.isEmpty() ? stack.getOrDefault(BACKSTAB_ANGLE, DEFAULT_BACKSTAB_ANGLE) : DEFAULT_BACKSTAB_ANGLE;

        Vec3 direction = sourcePos.subtract(victim.position()).horizontal().normalize();
        Vec3 facing = victim.calculateViewVector(0.0F, victim.getYHeadRot()).normalize();

        return Math.acos(direction.dot(facing)) > Math.toRadians(angle);
    }
}