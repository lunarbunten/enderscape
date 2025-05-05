package net.bunten.enderscape.registry.tag;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class EnderscapeEntityTags {

    public static final TagKey<EntityType<?>> AFFECTED_BY_MAGNIA = register("affected_by_magnia");
    public static final TagKey<EntityType<?>> BLACKLISTED_FROM_MIRROR_IN_DISPENSER_TELEPORTATION = register("blacklisted_from_mirror_in_dispenser_teleportation");
    public static final TagKey<EntityType<?>> CREATES_VOID_PARTICLES_UPON_DEATH = register("creates_void_particles_upon_death");
    public static final TagKey<EntityType<?>> DRIFTERS = register("drifters");
    public static final TagKey<EntityType<?>> DRIFTERS_INTIMIDATED_BY = register("drifters_intimidated_by");
    public static final TagKey<EntityType<?>> RUBBLEMITE_HOSTILE_TOWARDS = register("rubblemite_hostile_towards");

    private static TagKey<EntityType<?>> register(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, Enderscape.id(name));
    }
}