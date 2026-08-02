package net.penumbra.enderscape.registry.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.penumbra.enderscape.Enderscape;

public class EnderscapeEntityTags {

    public static final TagKey<EntityType<?>> AFFECTED_BY_MAGNIA = register("affected_by_magnia");
    public static final TagKey<EntityType<?>> AMBIENT_VOIDING_IMMUNE = register("ambient_voiding_immune");
    public static final TagKey<EntityType<?>> BLACKLISTED_FROM_LODESTONE_TELEPORTATION = register("blacklisted_from_lodestone_teleportation");
    public static final TagKey<EntityType<?>> CREATES_VOID_PARTICLES_UPON_DEATH = register("creates_void_particles_upon_death");
    public static final TagKey<EntityType<?>> DRIFTERS_INTIMIDATED_BY = register("drifters_intimidated_by");
    public static final TagKey<EntityType<?>> EXEMPT_FROM_MAGNIA_ATTRACTOR_ABUSE_COST = register("exempt_from_magnia_attractor_abuse_cost");
    public static final TagKey<EntityType<?>> HEALED_BY_VOID_CORRUPTION = register("healed_by_void_corruption");
    public static final TagKey<EntityType<?>> HEALED_BY_VOID_PURIFICATION = register("healed_by_void_purification");
    public static final TagKey<EntityType<?>> HURT_BY_VOID_PURIFICATION = register("hurt_by_void_purification");
    public static final TagKey<EntityType<?>> IGNORES_BACKSTAB_DAMAGE = register("ignores_backstab_damage");
    public static final TagKey<EntityType<?>> PULLED_BY_MAGNIA_ATTRACTOR = register("pulled_by_magnia_attractor");
    public static final TagKey<EntityType<?>> RUBBLEMITE_ALWAYS_HOSTILES = register("rubblemite_always_hostiles");
    public static final TagKey<EntityType<?>> VOID = register("void");
    public static final TagKey<EntityType<?>> VOID_IMMUNE = register("void_immune");
    public static final TagKey<EntityType<?>> VOID_LACHRYMA_WALKABLE_MOBS = register("void_lachryma_walkable_mobs");
    public static final TagKey<EntityType<?>> VOID_SHALE_WALKABLE_MOBS = register("void_shale_walkable_mobs");

    private static TagKey<EntityType<?>> register(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, Enderscape.id(name));
    }
}