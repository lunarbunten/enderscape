package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.fabricmc.fabric.api.registry.FabricPotionBrewingBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;

import static net.bunten.enderscape.registry.EnderscapeItems.DRIFT_JELLY_BOTTLE;

public class EnderscapePotions {

    public static final Holder<Potion> LOW_GRAVITY = register("low_gravity", new Potion("low_gravity", new MobEffectInstance(EnderscapeMobEffects.LOW_GRAVITY, 90 * 20)));
    public static final Holder<Potion> LONG_LOW_GRAVITY = register("long_low_gravity", new Potion("low_gravity", new MobEffectInstance(EnderscapeMobEffects.LOW_GRAVITY, 240 * 20)));

    private static Holder.Reference<Potion> register(String name, Potion potion) {
        return Registry.registerForHolder(BuiltInRegistries.POTION, Enderscape.id(name), potion);
    }

    static {
        FabricPotionBrewingBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(
                    Potions.AWKWARD,
                    Ingredient.of(DRIFT_JELLY_BOTTLE),
                    LOW_GRAVITY
            );

            builder.registerPotionRecipe(
                    LOW_GRAVITY,
                    Ingredient.of(Items.REDSTONE),
                    LONG_LOW_GRAVITY
            );
        });
    }
}