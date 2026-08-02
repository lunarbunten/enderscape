package net.penumbra.enderscape.registry.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.penumbra.enderscape.Enderscape;

public class EnderscapeBiomeTags {
    public static final TagKey<Biome> DEEP_VOID_BIOMES = register("deep_void_biomes");
    public static final TagKey<Biome> ENDERSCAPE_BIOMES = register("enderscape_biomes");
    public static final TagKey<Biome> SKY_VOID_BIOMES = register("sky_void_biomes");
    public static final TagKey<Biome> VOID_BIOMES = register("void_biomes");

    public static final TagKey<Biome> EXCLUDED_FROM_GLOBAL_FEATURE_ADDITIONS = register("enderscape/excluded_from_global_feature_additions");
    public static final TagKey<Biome> HAS_BARRENS_ADDITIONS = register("enderscape/has_barrens_additions");
    public static final TagKey<Biome> OVERRIDES_DEFAULT_AMBIENCE = register("enderscape/overrides_default_ambience");

    public static final TagKey<Biome> HAS_GATEWAYS = register("has_structure/gateways");
    public static final TagKey<Biome> HAS_MIRESTONE_RUINS = register("has_structure/mirestone_ruins");

    private static TagKey<Biome> register(String name) {
        return TagKey.create(Registries.BIOME, Enderscape.id(name));
    }
}
