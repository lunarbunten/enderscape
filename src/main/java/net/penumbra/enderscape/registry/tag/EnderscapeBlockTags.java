package net.penumbra.enderscape.registry.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.penumbra.enderscape.Enderscape;

public class EnderscapeBlockTags {
    public static final TagKey<Block> NEBULITE_STORAGE_BLOCKS = common("storage_blocks/nebulite");
    public static final TagKey<Block> SHADOLINE_STORAGE_BLOCKS = common("storage_blocks/shadoline");
    public static final TagKey<Block> RAW_SHADOLINE_STORAGE_BLOCKS = common("storage_blocks/raw_shadoline");
    public static final TagKey<Block> NEBULITE_ORES = common("ores/nebulite");
    public static final TagKey<Block> SHADOLINE_ORES = common("ores/shadoline");

    public static final TagKey<Block> ALL_ETCHED_MAGNIA_BLOCKS = register("all_etched_magnia_blocks");
    public static final TagKey<Block> BASE_STONE_END = register("base_stone_end");
    public static final TagKey<Block> CELESTIAL_BRICK_BLOCKS = register("celestial_brick_blocks");
    public static final TagKey<Block> CELESTIAL_CHANTERELLE_GROWS_ON = register("celestial_chanterelle_grows_on");
    public static final TagKey<Block> CELESTIAL_CORRUPTS_ON = register("celestial_corrupts_on");
    public static final TagKey<Block> CELESTIAL_STEMS = register("celestial_stems");
    public static final TagKey<Block> CELESTIAL_WOOD_BLOCKS = register("celestial_wood_blocks");
    public static final TagKey<Block> CHANTERELLE_BRICK_BLOCKS = register("chanterelle_brick_blocks");
    public static final TagKey<Block> CHANTERELLE_CAP_BLOCKS = register("chanterelle_cap_blocks");
    public static final TagKey<Block> CORRUPTION_PURIFIES_ON = register("corruption_purifies_on");
    public static final TagKey<Block> DUSK_PURPUR_BLOCKS = register("dusk_purpur_blocks");
    public static final TagKey<Block> EDIBLE_FOR_RUSTLE = register("edible_for_rustle");
    public static final TagKey<Block> ENDERMITE_SAFE_WHEN_NEARBY = register("endermite_safe_when_nearby");
    public static final TagKey<Block> END_ORE_BLOCKS = register("end_ore_blocks");
    public static final TagKey<Block> ETCHED_ALLURING_MAGNIA_BLOCKS = register("etched_alluring_magnia_blocks");
    public static final TagKey<Block> ETCHED_REPULSIVE_MAGNIA_BLOCKS = register("etched_repulsive_magnia_blocks");
    public static final TagKey<Block> KURODITE_BLOCKS = register("kurodite_blocks");
    public static final TagKey<Block> MAGNIA_ARCH_REPLACEABLE = register("magnia_arch_replaceable");
    public static final TagKey<Block> MAGNIA_BLOCKS = register("magnia_blocks");
    public static final TagKey<Block> MAGNIA_SPROUTS = register("magnia_sprouts");
    public static final TagKey<Block> MAGNIA_TOWER_REPLACEABLE = register("magnia_tower_replaceable");
    public static final TagKey<Block> MIRESTONE_BLOCKS = register("mirestone_blocks");
    public static final TagKey<Block> MURUBLIGHT_BRICK_BLOCKS = register("murublight_brick_blocks");
    public static final TagKey<Block> MURUBLIGHT_CHANTERELLE_GROWS_ON = register("murublight_chanterelle_grows_on");
    public static final TagKey<Block> MURUBLIGHT_STEMS = register("murublight_stems");
    public static final TagKey<Block> MURUBLIGHT_WOOD_BLOCKS = register("murublight_wood_blocks");
    public static final TagKey<Block> NEW_END_STONE_BLOCKS = register("new_end_stone_blocks");
    public static final TagKey<Block> NEW_PURPUR_BLOCKS = register("new_purpur_blocks");
    public static final TagKey<Block> ORE_REPLACEABLE = register("ore_replaceable");
    public static final TagKey<Block> OVERGROWTH_BLOCKS = register("overgrowth_blocks");
    public static final TagKey<Block> OVERHEATS_MAGNIA_SPROUTS = register("overheats_magnia_sprouts");
    public static final TagKey<Block> POWERS_BULB_FLOWER = register("powers_bulb_flower");
    public static final TagKey<Block> PURPUR_TILE_BLOCKS = register("purpur_tile_blocks");
    public static final TagKey<Block> PURUBERRY_VINE_SUPPORTS = register("puruberry_vine_supports");
    public static final TagKey<Block> RUBBLEMITES_SPAWNABLE_ON = register("rubblemites_spawnable_on");
    public static final TagKey<Block> RUSTLES_SPAWNABLE_ON = register("rustles_spawnable_on");
    public static final TagKey<Block> RUSTLE_PREFER_WALK_ON = register("rustle_prefer_walk_on");
    public static final TagKey<Block> SHADOLINE_BLOCKS = register("shadoline_blocks");
    public static final TagKey<Block> SUPPORTS_BULB_FLOWER = register("supports_bulb_flower");
    public static final TagKey<Block> SUPPORTS_CELESTIAL_CHANTERELLE = register("supports_celestial_chanterelle");
    public static final TagKey<Block> SUPPORTS_CELESTIAL_GROWTH = register("supports_celestial_growth");
    public static final TagKey<Block> SUPPORTS_CELESTIAL_VEGETATION = register("supports_celestial_vegetation");
    public static final TagKey<Block> SUPPORTS_CHORUS_SPROUTS = register("supports_chorus_sprouts");
    public static final TagKey<Block> SUPPORTS_CORRUPT_GROWTH = register("supports_corrupt_growth");
    public static final TagKey<Block> SUPPORTS_CORRUPT_VEGETATION = register("supports_corrupt_vegetation");
    public static final TagKey<Block> SUPPORTS_DRY_END_GROWTH = register("supports_dry_end_growth");
    public static final TagKey<Block> SUPPORTS_MURUBLIGHT_CHANTERELLE = register("supports_murublight_chanterelle");
    public static final TagKey<Block> SUPPORTS_PURUBERRY_VINE = register("supports_puruberry_vine");
    public static final TagKey<Block> SUPPORTS_VEILED_SAPLING = register("supports_veiled_sapling");
    public static final TagKey<Block> SUPPORTS_VEILED_VEGETATION = register("supports_veiled_vegetation");
    public static final TagKey<Block> SUPPORTS_WISP_FLOWER = register("supports_wisp_flower");
    public static final TagKey<Block> SUPPORTS_WISP_GROWTH = register("supports_wisp_growth");
    public static final TagKey<Block> SUPPORTS_WISP_SPROUTS = register("supports_wisp_sprouts");
    public static final TagKey<Block> VEILED_LOGS = register("veiled_logs");
    public static final TagKey<Block> VEILED_LOG_REPLACEABLE = register("veiled_log_replaceable");
    public static final TagKey<Block> VEILED_SAPLING_GROWS_ON = register("veiled_sapling_grows_on");
    public static final TagKey<Block> VEILED_WOOD_BLOCKS = register("veiled_wood_blocks");
    public static final TagKey<Block> VERADITE_BLOCKS = register("veradite_blocks");
    public static final TagKey<Block> VOID_FIRE_BASE_BLOCKS = register("void_fire_base_blocks");
    public static final TagKey<Block> VOID_LACHRYMA_TURNS_TO_VOID_SHALE = register("void_lachryma_turns_to_void_shale");

    public static final TagKey<Block> RUBBLEMITE_MIRESTONE_VARIANTS_SPAWN_ON = register("rubblemite/mirestone_variants_spawn_on");
    public static final TagKey<Block> RUBBLEMITE_VERADITE_VARIANTS_SPAWN_ON = register("rubblemite/veradite_variants_spawn_on");
    public static final TagKey<Block> RUBBLEMITE_KURODITE_VARIANTS_SPAWN_ON = register("rubblemite/kurodite_variants_spawn_on");

    private static TagKey<Block> register(String name) {
        return TagKey.create(Registries.BLOCK, Enderscape.id(name));
    }

    private static TagKey<Block> common(String name) {
        return TagKey.create(Registries.BLOCK, Identifier.tryBuild("c", name));
    }
}
