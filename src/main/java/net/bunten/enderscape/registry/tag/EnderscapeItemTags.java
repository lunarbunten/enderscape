package net.bunten.enderscape.registry.tag;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class EnderscapeItemTags {

    public static final TagKey<Item> DAGGER_ENCHANTABLE = register("enchantable/dagger");
    public static final TagKey<Item> ELYTRA_ENCHANTABLE = register("enchantable/elytra");
    public static final TagKey<Item> MAGNIA_ATTRACTOR_ENCHANTABLE = register("enchantable/magnia_attractor");
    public static final TagKey<Item> MIRROR_ENCHANTABLE = register("enchantable/mirror");
    public static final TagKey<Item> NEBULITE_TOOL_ENCHANTABLE = register("enchantable/nebulite_tool");

    public static final TagKey<Item> WEAK_MAGNIA_STRENGTH = register("magnia_strength/weak");
    public static final TagKey<Item> AVERAGE_MAGNIA_STRENGTH = register("magnia_strength/medium");
    public static final TagKey<Item> STRONG_MAGNIA_STRENGTH = register("magnia_strength/strong");

    public static final TagKey<Item> ALL_ETCHED_MAGNIA_BLOCKS = register("all_etched_magnia_blocks");
    public static final TagKey<Item> CELESTIAL_BRICK_BLOCKS = register("celestial_brick_blocks");
    public static final TagKey<Item> CELESTIAL_STEMS = register("celestial_stems");
    public static final TagKey<Item> CELESTIAL_WOOD_ITEMS = register("celestial_wood_items");
    public static final TagKey<Item> CHANTERELLE_BRICK_BLOCKS = register("chanterelle_brick_blocks");
    public static final TagKey<Item> CHANTERELLE_CAP_BLOCKS = register("chanterelle_cap_blocks");
    public static final TagKey<Item> DRIFTER_FOOD = register("drifter_food");
    public static final TagKey<Item> DUSK_PURPUR_BLOCKS = register("dusk_purpur_blocks");
    public static final TagKey<Item> END_ORE_BLOCKS = register("end_ore_blocks");
    public static final TagKey<Item> ETCHED_ALLURING_MAGNIA_BLOCKS = register("etched_alluring_magnia_blocks");
    public static final TagKey<Item> ETCHED_REPULSIVE_MAGNIA_BLOCKS = register("etched_repulsive_magnia_blocks");
    public static final TagKey<Item> KURODITE_BLOCKS = register("kurodite_blocks");
    public static final TagKey<Item> MAGNIA_BLOCKS = register("magnia_blocks");
    public static final TagKey<Item> MAGNIA_SPROUTS = register("magnia_sprouts");
    public static final TagKey<Item> MIRESTONE_BLOCKS = register("mirestone_blocks");
    public static final TagKey<Item> MURUBLIGHT_BRICK_BLOCKS = register("murublight_brick_blocks");
    public static final TagKey<Item> MURUBLIGHT_STEMS = register("murublight_stems");
    public static final TagKey<Item> MURUBLIGHT_WOOD_ITEMS = register("murublight_wood_items");
    public static final TagKey<Item> NEBULITE_TOOLS = register("nebulite_tools");
    public static final TagKey<Item> NEBULITE_TOOL_FUELS = register("nebulite_tool_fuels");
    public static final TagKey<Item> NEW_END_STONE_BLOCKS = register("new_end_stone_blocks");
    public static final TagKey<Item> NEW_PURPUR_BLOCKS = register("new_purpur_blocks");
    public static final TagKey<Item> OVERGROWTH_BLOCKS = register("overgrowth_blocks");
    public static final TagKey<Item> PURPUR_TILE_BLOCKS = register("purpur_tile_blocks");
    public static final TagKey<Item> REPAIRS_DRIFT_LEGGINGS = register("repairs_drift_leggings");
    public static final TagKey<Item> REPAIRS_RUBBLE_SHIELDS = register("repairs_rubble_shields");
    public static final TagKey<Item> REPAIRS_SHADOLINE_ARMOR = register("repairs_shadoline_armor");
    public static final TagKey<Item> RUBBLE_SHIELDS = register("rubble_shields");
    public static final TagKey<Item> RUSTLE_FOOD = register("rustle_food");
    public static final TagKey<Item> SHADOLINE_BLOCKS = register("shadoline_blocks");
    public static final TagKey<Item> VEILED_LOGS = register("veiled_logs");
    public static final TagKey<Item> VEILED_WOOD_ITEMS = register("veiled_wood_items");
    public static final TagKey<Item> VERADITE_BLOCKS = register("veradite_blocks");

    // Common Tags

    public static final TagKey<Item> NEBULITE_STORAGE_BLOCKS = common("storage_blocks/nebulite");
    public static final TagKey<Item> SHADOLINE_STORAGE_BLOCKS = common("storage_blocks/shadoline");
    public static final TagKey<Item> RAW_SHADOLINE_STORAGE_BLOCKS = common("storage_blocks/raw_shadoline");
    public static final TagKey<Item> NEBULITE_ORES = common("ores/nebulite");
    public static final TagKey<Item> SHADOLINE_ORES = common("ores/shadoline");

    public static final TagKey<Item> NEBULITE_GEMS = common("gems/nebulite");
    public static final TagKey<Item> SHADOLINE_INGOTS = common("ingots/shadoline");
    public static final TagKey<Item> SHADOLINE_NUGGETS = common("nuggets/shadoline");
    public static final TagKey<Item> SHADOLINE_RAW_MATERIALS = common("raw_materials/shadoline");
    public static final TagKey<Item> DRIFT_JELLY_DRINKS = common("drinks/drift_jelly");

    private static TagKey<Item> register(String name) {
        return TagKey.create(Registries.ITEM, Enderscape.id(name));
    }

    private static TagKey<Item> common(String name) {
        return TagKey.create(Registries.ITEM, Identifier.tryBuild("c", name));
    }
}
