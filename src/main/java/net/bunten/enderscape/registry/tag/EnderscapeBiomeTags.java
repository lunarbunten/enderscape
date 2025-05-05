package net.bunten.enderscape.registry.tag;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class EnderscapeBiomeTags {
    public static final TagKey<Biome> ENDERSCAPE_BIOMES = register("enderscape_biomes");
    public static final TagKey<Biome> EXCLUDED_FROM_GLOBAL_FEATURE_ADDITIONS = register("excluded_from_global_feature_additions");
    public static final TagKey<Biome> HAS_GATEWAYS = register("has_structure/gateways");
    public static final TagKey<Biome> HAS_MIRESTONE_RUINS = register("has_structure/mirestone_ruins");
    public static final TagKey<Biome> HAS_MODIFIED_END_AMBIENCE = register("has_modified_end_ambience");
    public static final TagKey<Biome> INCLUDES_NEW_BARRENS_CONTENT = register("includes_new_barrens_content");
    public static final TagKey<Biome> DOES_NOT_SPAWN_END_STONE_RUBBLEMITES = register("does_not_spawn_end_stone_rubblemites");
    public static final TagKey<Biome> DOES_NOT_SPAWN_KURODITE_RUBBLEMITES = register("does_not_spawn_kurodite_rubblemites");
    public static final TagKey<Biome> DOES_NOT_SPAWN_MIRESTONE_RUBBLEMITES = register("does_not_spawn_mirestone_rubblemites");
    public static final TagKey<Biome> DOES_NOT_SPAWN_VERADITE_RUBBLEMITES = register("does_not_spawn_veradite_rubblemites");

    private static TagKey<Biome> register(String name) {
        return TagKey.create(Registries.BIOME, Enderscape.id(name));
    }
}
