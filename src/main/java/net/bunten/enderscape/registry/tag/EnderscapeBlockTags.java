package net.bunten.enderscape.registry.tag;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class EnderscapeBlockTags {
    public static final TagKey<Block> NEBULITE_STORAGE_BLOCKS = common("storage_blocks/nebulite");
    public static final TagKey<Block> SHADOLINE_STORAGE_BLOCKS = common("storage_blocks/shadoline");
    public static final TagKey<Block> RAW_SHADOLINE_STORAGE_BLOCKS = common("storage_blocks/raw_shadoline");
    public static final TagKey<Block> NEBULITE_ORES = common("ores/nebulite");
    public static final TagKey<Block> SHADOLINE_ORES = common("ores/shadoline");

    public static final TagKey<Block> ALL_ETCHED_MAGNIA_BLOCKS = register("all_etched_magnia_blocks");
    public static final TagKey<Block> CELESTIAL_BRICK_BLOCKS = register("celestial_brick_blocks");
    public static final TagKey<Block> CELESTIAL_CHANTERELLE_MATURES_ON = register("celestial_chanterelle_matures_on");
    public static final TagKey<Block> CELESTIAL_CORRUPTS_ON = register("celestial_corrupts_on");
    public static final TagKey<Block> CELESTIAL_GROVE_VEGETATION_PLANTABLE_ON = register("celestial_grove_vegetation_plantable_on");
    public static final TagKey<Block> CELESTIAL_STEMS = register("celestial_stems");
    public static final TagKey<Block> CELESTIAL_WOOD_BLOCKS = register("celestial_wood_blocks");
    public static final TagKey<Block> CHANTERELLE_BRICK_BLOCKS = register("chanterelle_brick_blocks");
    public static final TagKey<Block> CHANTERELLE_CAP_BLOCKS = register("chanterelle_cap_blocks");
    public static final TagKey<Block> CHORUS_VEGETATION_PLANTABLE_ON = register("chorus_vegetation_plantable_on");
    public static final TagKey<Block> CORRUPTION_PURIFIES_ON = register("corruption_purifies_on");
    public static final TagKey<Block> CORRUPT_BARRENS_VEGETATION_PLANTABLE_ON = register("corrupt_barrens_vegetation_plantable_on");
    public static final TagKey<Block> DRY_END_GROWTH_PLANTABLE_ON = register("dry_end_growth_plantable_on");
    public static final TagKey<Block> DUSK_PURPUR_BLOCKS = register("dusk_purpur_blocks");
    public static final TagKey<Block> ENDERMITE_SAFE_WHEN_NEARBY = register("endermite_safe_when_nearby");
    public static final TagKey<Block> END_ORE_BLOCKS = register("end_ore_blocks");
    public static final TagKey<Block> ETCHED_ALLURING_MAGNIA_BLOCKS = register("etched_alluring_magnia_blocks");
    public static final TagKey<Block> ETCHED_REPULSIVE_MAGNIA_BLOCKS = register("etched_repulsive_magnia_blocks");
    public static final TagKey<Block> FLANGER_BERRY_VINE_PLANTABLE_ON = register("flanger_berry_vine_plantable_on");
    public static final TagKey<Block> FLANGER_BERRY_VINE_SUPPORTS = register("flanger_berry_vine_supports");
    public static final TagKey<Block> KURODITE_BLOCKS = register("kurodite_blocks");
    public static final TagKey<Block> MAGNIA_ARCH_REPLACEABLE = register("magnia_arch_replaceable");
    public static final TagKey<Block> MAGNIA_BLOCKS = register("magnia_blocks");
    public static final TagKey<Block> MAGNIA_SPROUTS = register("magnia_sprouts");
    public static final TagKey<Block> MAGNIA_TOWER_REPLACEABLE = register("magnia_tower_replaceable");
    public static final TagKey<Block> MIRESTONE_BLOCKS = register("mirestone_blocks");
    public static final TagKey<Block> MURUBLIGHT_BRICK_BLOCKS = register("murublight_brick_blocks");
    public static final TagKey<Block> MURUBLIGHT_CHANTERELLE_MATURES_ON = register("murublight_chanterelle_matures_on");
    public static final TagKey<Block> MURUBLIGHT_STEMS = register("murublight_stems");
    public static final TagKey<Block> MURUBLIGHT_WOOD_BLOCKS = register("murublight_wood_blocks");
    public static final TagKey<Block> NEW_END_STONE_BLOCKS = register("new_end_stone_blocks");
    public static final TagKey<Block> NEW_PURPUR_BLOCKS = register("new_purpur_blocks");
    public static final TagKey<Block> ORE_REPLACEABLE = register("ore_replaceable");
    public static final TagKey<Block> OVERGROWTH_BLOCKS = register("overgrowth_blocks");
    public static final TagKey<Block> OVERHEATS_MAGNIA_SPROUTS = register("overheats_magnia_sprouts");
    public static final TagKey<Block> PURPUR_TILE_BLOCKS = register("purpur_tile_blocks");
    public static final TagKey<Block> RUBBLEMITE_SPAWNABLE_ON = register("rubblemite_spawnable_on");
    public static final TagKey<Block> RUSTLE_FOOD = register("rustle_food");
    public static final TagKey<Block> RUSTLE_PREFERRED = register("rustle_preferred");
    public static final TagKey<Block> RUSTLE_SPAWNABLE_ON = register("rustle_spawnable_on");
    public static final TagKey<Block> SHADOLINE_BLOCKS = register("shadoline_blocks");
    public static final TagKey<Block> SUPPORTS_END_CRYSTAL = register("supports_end_crystal");
    public static final TagKey<Block> VEILED_LOGS = register("veiled_logs");
    public static final TagKey<Block> VEILED_SAPLING_MATURES_ON = register("veiled_sapling_matures_on");
    public static final TagKey<Block> VEILED_WOODLANDS_VEGETATION_PLANTABLE_ON = register("veiled_woodlands_vegetation_plantable_on");
    public static final TagKey<Block> VEILED_WOOD_BLOCKS = register("veiled_wood_blocks");
    public static final TagKey<Block> VERADITE_BLOCKS = register("veradite_blocks");

    public static final TagKey<Block> RUBBLEMITE_MIRESTONE_VARIANTS_SPAWN_ON = register("rubblemite/mirestone_variants_spawn_on");
    public static final TagKey<Block> RUBBLEMITE_VERADITE_VARIANTS_SPAWN_ON = register("rubblemite/veradite_variants_spawn_on");
    public static final TagKey<Block> RUBBLEMITE_KURODITE_VARIANTS_SPAWN_ON = register("rubblemite/kurodite_variants_spawn_on");

    private static TagKey<Block> register(String name) {
        return TagKey.create(Registries.BLOCK, Enderscape.id(name));
    }

    private static TagKey<Block> common(String name) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.tryBuild("c", name));
    }
}
