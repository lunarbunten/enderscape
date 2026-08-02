package net.penumbra.enderscape.references;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.alchemy.Potion;
import net.penumbra.enderscape.Enderscape;

public class EnderscapePotionIds {
    public static final ResourceKey<Potion> LOW_GRAVITY = register("low_gravity");
    public static final ResourceKey<Potion> LONG_LOW_GRAVITY = register("long_low_gravity");
    public static final ResourceKey<Potion> VOID_PURIFICATION = register("void_purification");
    public static final ResourceKey<Potion> LONG_VOID_PURIFICATION = register("long_void_purification");
    public static final ResourceKey<Potion> STRONG_VOID_PURIFICATION = register("strong_void_purification");
    public static final ResourceKey<Potion> VOID_RESISTANCE = register("void_resistance");
    public static final ResourceKey<Potion> LONG_VOID_RESISTANCE = register("long_void_resistance");
    public static final ResourceKey<Potion> VOID_CORRUPTION = register("void_corruption");
    public static final ResourceKey<Potion> LONG_VOID_CORRUPTION = register("long_void_corruption");
    public static final ResourceKey<Potion> STRONG_VOID_CORRUPTION = register("strong_void_corruption");

    private static ResourceKey<Potion> register(String name) {
        return ResourceKey.create(Registries.POTION, Enderscape.id(name));
    }
}