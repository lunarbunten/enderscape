package net.penumbra.enderscape.references;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.*;
import net.penumbra.enderscape.Enderscape;

public class EnderscapeEntityIds {

    public static final ResourceKey<EntityType<?>> DRIFTER = of("drifter");
    public static final ResourceKey<EntityType<?>> RUBBLEMITE = of("rubblemite");
    public static final ResourceKey<EntityType<?>> RUSTLE = of("rustle");

    private static ResourceKey<EntityType<?>> of(String name) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Enderscape.id(name));
    }
}