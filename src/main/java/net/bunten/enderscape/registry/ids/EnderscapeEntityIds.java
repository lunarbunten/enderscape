package net.bunten.enderscape.registry.ids;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

public class EnderscapeEntityIds {

    public static final ResourceKey<EntityType<?>> DRIFTER = key("drifter");

    public static final ResourceKey<EntityType<?>> RUBBLEMITE = key("rubblemite");

    public static final ResourceKey<EntityType<?>> RUSTLE = key("rustle");


    private static ResourceKey<EntityType<?>> key(String path) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Enderscape.id(path));
    }
}