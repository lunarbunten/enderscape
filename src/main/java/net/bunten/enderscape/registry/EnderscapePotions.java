package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

import java.util.function.Supplier;

import static net.bunten.enderscape.registry.EnderscapeItems.DRIFT_JELLY_BOTTLE;

@EventBusSubscriber(modid = Enderscape.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class EnderscapePotions {

    public static final Holder<Potion> LOW_GRAVITY = register("low_gravity", () -> new Potion("low_gravity", new MobEffectInstance(EnderscapeMobEffects.LOW_GRAVITY, 1200)));
    public static final Holder<Potion> LONG_LOW_GRAVITY = register("long_low_gravity", () -> new Potion("low_gravity", new MobEffectInstance(EnderscapeMobEffects.LOW_GRAVITY, 2400)));

    private static Holder<Potion> register(String name, Supplier<Potion> potion) {
        return RegistryHelper.registerForHolder(BuiltInRegistries.POTION, Enderscape.id(name), potion);
    }

    @SubscribeEvent
    public static void registerRecipes(RegisterBrewingRecipesEvent event) {
        var builder = event.getBuilder();
        builder.addMix(
                Potions.AWKWARD,
                DRIFT_JELLY_BOTTLE.get(),
                LOW_GRAVITY
        );

        builder.addMix(
                LOW_GRAVITY,
                Items.REDSTONE,
                LONG_LOW_GRAVITY
        );
    }
}