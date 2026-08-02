package net.penumbra.enderscape.registry.item;

import net.fabricmc.fabric.api.registry.FabricPotionBrewingBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.references.EnderscapePotionIds;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;

import java.util.ArrayList;
import java.util.List;

import static net.penumbra.enderscape.registry.item.EnderscapeItems.DRIFT_JELLY_BOTTLE;
import static net.penumbra.enderscape.registry.item.EnderscapeItems.PURUBERRY;

public class EnderscapePotions {

    public static final List<Holder<Potion>> POTIONS = new ArrayList<>();

    public static final Holder<Potion> LOW_GRAVITY = register(EnderscapePotionIds.LOW_GRAVITY, "enderscape_low_gravity", new MobEffectInstance(EnderscapeMobEffects.LOW_GRAVITY, 90 * 20));
    public static final Holder<Potion> LONG_LOW_GRAVITY = register(EnderscapePotionIds.LONG_LOW_GRAVITY, "enderscape_low_gravity", new MobEffectInstance(EnderscapeMobEffects.LOW_GRAVITY, 240 * 20));
    public static final Holder<Potion> VOID_PURIFICATION = register(EnderscapePotionIds.VOID_PURIFICATION, "enderscape_void_purification", new MobEffectInstance(EnderscapeMobEffects.VOID_PURIFICATION, 90 * 20));
    public static final Holder<Potion> LONG_VOID_PURIFICATION = register(EnderscapePotionIds.LONG_VOID_PURIFICATION, "enderscape_void_purification", new MobEffectInstance(EnderscapeMobEffects.VOID_PURIFICATION, 240 * 20));
    public static final Holder<Potion> STRONG_VOID_PURIFICATION = register(EnderscapePotionIds.STRONG_VOID_PURIFICATION, "enderscape_void_purification", new MobEffectInstance(EnderscapeMobEffects.VOID_PURIFICATION, 30 * 20, 1));
    public static final Holder<Potion> VOID_RESISTANCE = register(EnderscapePotionIds.VOID_RESISTANCE, "enderscape_void_resistance", new MobEffectInstance(EnderscapeMobEffects.VOID_RESISTANCE, 60 * 20));
    public static final Holder<Potion> LONG_VOID_RESISTANCE = register(EnderscapePotionIds.LONG_VOID_RESISTANCE, "enderscape_void_resistance", new MobEffectInstance(EnderscapeMobEffects.VOID_RESISTANCE, 180 * 20));
    public static final Holder<Potion> VOID_CORRUPTION = register(EnderscapePotionIds.VOID_CORRUPTION, "enderscape_void_corruption", new MobEffectInstance(EnderscapeMobEffects.VOID_CORRUPTION, 40 * 20));
    public static final Holder<Potion> LONG_VOID_CORRUPTION = register(EnderscapePotionIds.LONG_VOID_CORRUPTION, "enderscape_void_corruption", new MobEffectInstance(EnderscapeMobEffects.VOID_CORRUPTION, 80 * 20));
    public static final Holder<Potion> STRONG_VOID_CORRUPTION = register(EnderscapePotionIds.STRONG_VOID_CORRUPTION, "enderscape_void_corruption", new MobEffectInstance(EnderscapeMobEffects.VOID_CORRUPTION, 20 * 20, 1));

    static {
        FabricPotionBrewingBuilder.BUILD.register(builder -> {
            normal(builder, DRIFT_JELLY_BOTTLE, LOW_GRAVITY);
            extended(builder, LOW_GRAVITY, LONG_LOW_GRAVITY);

            normal(builder, PURUBERRY, VOID_PURIFICATION);
            extended(builder, VOID_PURIFICATION, LONG_VOID_PURIFICATION);
            amplified(builder, VOID_PURIFICATION, STRONG_VOID_PURIFICATION);

            extended(builder, VOID_RESISTANCE, LONG_VOID_RESISTANCE);

            extended(builder, VOID_CORRUPTION, LONG_VOID_CORRUPTION);
            amplified(builder, VOID_CORRUPTION, STRONG_VOID_CORRUPTION);
        });
    }

    private static void normal(PotionBrewing.Builder builder, Item ingredient, Holder<Potion> potion) {
        builder.registerPotionRecipe(
                Potions.AWKWARD,
                Ingredient.of(ingredient),
                potion
        );
    }

    private static void extended(PotionBrewing.Builder builder, Holder<Potion> normal, Holder<Potion> extended) {
        builder.registerPotionRecipe(
                normal,
                Ingredient.of(Items.REDSTONE),
                extended
        );
    }

    private static void amplified(PotionBrewing.Builder builder, Holder<Potion> normal, Holder<Potion> amplified) {
        builder.registerPotionRecipe(
                normal,
                Ingredient.of(Items.GLOWSTONE_DUST),
                amplified
        );
    }

    private static Holder.Reference<Potion> register(ResourceKey<Potion> key, String potionName, MobEffectInstance instance) {
        Holder.Reference<Potion> holder = Registry.registerForHolder(BuiltInRegistries.POTION, key, new Potion(potionName, instance));
        POTIONS.add(holder);
        return holder;
    }
}