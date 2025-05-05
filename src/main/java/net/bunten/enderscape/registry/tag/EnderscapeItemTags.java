package net.bunten.enderscape.registry.tag;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class EnderscapeItemTags {

    public static final TagKey<Item> WEAK_MAGNETISM_WHEN_WORN = register("weak_magnetism_when_worn");
    public static final TagKey<Item> AVERAGE_MAGNETISM_WHEN_WORN = register("medium_magnetism_when_worn");
    public static final TagKey<Item> STRONG_MAGNETISM_WHEN_WORN = register("strong_magnetism_when_worn");

    public static final TagKey<Item> CELESTIAL_BRICK_BLOCKS = register("celestial_brick_blocks");
    public static final TagKey<Item> CELESTIAL_STEMS = register("celestial_stems");
    public static final TagKey<Item> CELESTIAL_WOOD = register("celestial_wood");
    public static final TagKey<Item> CHANTERELLE_BRICK_BLOCKS = register("chanterelle_brick_blocks");
    public static final TagKey<Item> CHANTERELLE_CAP_BLOCKS = register("chanterelle_cap_blocks");
    public static final TagKey<Item> DRIFTER_FOOD = register("drifter_food");
    public static final TagKey<Item> ELYTRA_ENCHANTABLE = register("enchantable/elytra");
    public static final TagKey<Item> ETCHED_MAGNIA_BLOCKS = register("etched_magnia_blocks");
    public static final TagKey<Item> KURODITE_BLOCKS = register("kurodite_blocks");
    public static final TagKey<Item> MAGNIA_ATTRACTOR_ENCHANTABLE = register("enchantable/magnia_attractor");
    public static final TagKey<Item> MAGNIA_BLOCKS = register("magnia_blocks");
    public static final TagKey<Item> MAGNIA_SPROUTS = register("magnia_sprouts");
    public static final TagKey<Item> MIRROR_ENCHANTABLE = register("enchantable/mirror");
    public static final TagKey<Item> MURUBLIGHT_BRICK_BLOCKS = register("murublight_brick_blocks");
    public static final TagKey<Item> MURUBLIGHT_STEMS = register("murublight_stems");
    public static final TagKey<Item> MURUBLIGHT_WOOD = register("murublight_wood");
    public static final TagKey<Item> NEBULITE_TOOLS = register("nebulite_tools");
    public static final TagKey<Item> POWERS_MAGNIA_WHEN_MINED_WITH = register("powers_magnia_when_mined_with");
    public static final TagKey<Item> REPAIRS_DRIFT_LEGGINGS = register("repairs_drift_leggings");
    public static final TagKey<Item> REPAIRS_RUBBLE_SHIELDS = register("repairs_rubble_shields");
    public static final TagKey<Item> RUBBLE_SHIELDS = register("rubble_shields");
    public static final TagKey<Item> RUSTLE_FOOD = register("rustle_food");
    public static final TagKey<Item> SHADOLINE_BLOCKS = register("shadoline_blocks");
    public static final TagKey<Item> VEILED_LOGS = register("veiled_logs");
    public static final TagKey<Item> VEILED_WOOD_TAG = register("veiled_wood");
    public static final TagKey<Item> VERADITE_BLOCKS = register("veradite_blocks");

    private static TagKey<Item> register(String name) {
        return TagKey.create(Registries.ITEM, Enderscape.id(name));
    }
}
