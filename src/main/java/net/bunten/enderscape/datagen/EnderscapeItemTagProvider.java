package net.bunten.enderscape.datagen;

import net.bunten.enderscape.registry.ids.EnderscapeItemIds;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockItemTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.ids.EnderscapeBlockIds.*;
import static net.bunten.enderscape.registry.ids.EnderscapeItemIds.*;
import static net.bunten.enderscape.registry.tag.EnderscapeItemTags.*;
import static net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.*;
import static net.minecraft.references.BlockItemIds.END_STONE;
import static net.minecraft.tags.BlockItemTags.LANTERNS;
import static net.minecraft.tags.ItemTags.*;
import static net.minecraft.references.ItemIds.*;

public class EnderscapeItemTagProvider extends FabricTagsProvider.ItemTagsProvider {

    public EnderscapeItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, null);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(WEAK_MAGNIA_STRENGTH).add(
                CHAINMAIL_HELMET,
                CHAINMAIL_CHESTPLATE,
                CHAINMAIL_LEGGINGS,
                CHAINMAIL_BOOTS,
                NETHERITE_HELMET,
                NETHERITE_CHESTPLATE,
                NETHERITE_LEGGINGS,
                NETHERITE_BOOTS
        );

        tag(AVERAGE_MAGNIA_STRENGTH).add(
                IRON_HELMET,
                IRON_CHESTPLATE,
                IRON_LEGGINGS,
                IRON_BOOTS,
                IRON_HORSE_ARMOR
        );

        tag(STRONG_MAGNIA_STRENGTH);

        tag(DAGGER_ENCHANTABLE).add(DAGGER);
        tag(DRIFTER_FOOD).add(CHORUS_FRUIT, FLANGER_BERRY);
        tag(ELYTRA_ENCHANTABLE).add(ELYTRA);
        tag(LANTERNS.item()).add(keyBlock(BULB_LANTERN), keyBlock(VOID_LANTERN));
        tag(MAGNIA_ATTRACTOR_ENCHANTABLE).add(MAGNIA_ATTRACTOR);
        tag(MIRROR_ENCHANTABLE).add(MIRROR);
        tag(NEBULITE_TOOLS).add(DAGGER, MIRROR, MAGNIA_ATTRACTOR);
        tag(NEBULITE_TOOL_ENCHANTABLE).forceAddTag(NEBULITE_TOOLS);
        tag(NEBULITE_TOOL_FUELS).add(NEBULITE);
        tag(REPAIRS_DRIFT_LEGGINGS).add(DRIFT_JELLY_BOTTLE);
        tag(REPAIRS_RUBBLE_SHIELDS).add(RUBBLE_CHITIN);
        tag(REPAIRS_SHADOLINE_ARMOR).add(SHADOLINE_INGOT);
        tag(RUBBLE_SHIELDS).add(END_STONE_RUBBLE_SHIELD, MIRESTONE_RUBBLE_SHIELD, VERADITE_RUBBLE_SHIELD, KURODITE_RUBBLE_SHIELD);
        tag(RUSTLE_FOOD).add(MURUBLIGHT_BRACKET_ITEM);

        tag(DUSK_PURPUR_BLOCKS).add(keyBlock(DUSK_PURPUR_BLOCK), keyBlock(DUSK_PURPUR_PILLAR), keyBlock(DUSK_PURPUR_STAIRS), keyBlock(DUSK_PURPUR_SLAB), keyBlock(DUSK_PURPUR_WALL), keyBlock(CHISELED_DUSK_PURPUR));
        tag(END_ORE_BLOCKS).add(keyBlock(NEBULITE_ORE), keyBlock(SHADOLINE_ORE), keyBlock(MIRESTONE_NEBULITE_ORE), keyBlock(MIRESTONE_SHADOLINE_ORE));
        tag(NEW_END_STONE_BLOCKS).add(
                keyBlock(CHISELED_END_STONE),
                keyBlock(END_STONE_SLAB),
                keyBlock(END_STONE_STAIRS),
                keyBlock(END_STONE_WALL),
                keyBlock(POLISHED_END_STONE),
                keyBlock(POLISHED_END_STONE_BUTTON),
                keyBlock(POLISHED_END_STONE_PRESSURE_PLATE),
                keyBlock(POLISHED_END_STONE_SLAB),
                keyBlock(POLISHED_END_STONE_STAIRS),
                keyBlock(POLISHED_END_STONE_WALL)
        );

        tag(NEW_PURPUR_BLOCKS).add(
                keyBlock(CHISELED_PURPUR),
                keyBlock(PURPUR_WALL)
        );

        tag(OVERGROWTH_BLOCKS).add(
                keyBlock(CELESTIAL_OVERGROWTH),
                keyBlock(CORRUPT_OVERGROWTH)
        );

        tag(PURPUR_TILE_BLOCKS).add(
                keyBlock(PURPUR_TILES),
                keyBlock(PURPUR_TILE_STAIRS),
                keyBlock(PURPUR_TILE_SLAB)
        );

        tag(CHANTERELLE_BRICK_BLOCKS).forceAddTag(CELESTIAL_BRICK_BLOCKS).forceAddTag(MURUBLIGHT_BRICK_BLOCKS);
        tag(CHANTERELLE_CAP_BLOCKS).add(keyBlock(CELESTIAL_CAP), keyBlock(MURUBLIGHT_CAP));

        tag(CELESTIAL_BRICK_BLOCKS).add(keyBlock(CELESTIAL_BRICKS), keyBlock(CELESTIAL_BRICK_SLAB), keyBlock(CELESTIAL_BRICK_STAIRS), keyBlock(CELESTIAL_BRICK_WALL));
        tag(CELESTIAL_STEMS).add(keyBlock(CELESTIAL_STEM), keyBlock(STRIPPED_CELESTIAL_STEM), keyBlock(CELESTIAL_HYPHAE), keyBlock(STRIPPED_CELESTIAL_HYPHAE));
        tag(CELESTIAL_WOOD_ITEMS).forceAddTag(CELESTIAL_STEMS).add(
                keyBlock(CELESTIAL_BUTTON),
                keyBlock(CELESTIAL_DOOR),
                keyBlock(CELESTIAL_FENCE),
                keyBlock(CELESTIAL_FENCE_GATE),
                keyBlock(CELESTIAL_HANGING_SIGN),
                keyBlock(CELESTIAL_PLANKS),
                keyBlock(CELESTIAL_SIGN),
                keyBlock(CELESTIAL_SLAB),
                keyBlock(CELESTIAL_STAIRS),
                keyBlock(CELESTIAL_TRAPDOOR),
                CELESTIAL_HANGING_SIGN_ITEM
        );

        tag(VEILED_LOGS).add(keyBlock(VEILED_LOG), keyBlock(STRIPPED_VEILED_LOG), keyBlock(VEILED_WOOD), keyBlock(STRIPPED_VEILED_WOOD));
        tag(VEILED_WOOD_ITEMS).forceAddTag(VEILED_LOGS).add(
                keyBlock(VEILED_BUTTON),
                keyBlock(VEILED_DOOR),
                keyBlock(VEILED_FENCE),
                keyBlock(VEILED_FENCE_GATE),
                keyBlock(VEILED_HANGING_SIGN),
                keyBlock(VEILED_PLANKS),
                keyBlock(VEILED_SIGN),
                keyBlock(VEILED_SLAB),
                keyBlock(VEILED_STAIRS),
                keyBlock(VEILED_TRAPDOOR)
        );

        tag(MURUBLIGHT_BRICK_BLOCKS).add(keyBlock(MURUBLIGHT_BRICKS), keyBlock(MURUBLIGHT_BRICK_SLAB), keyBlock(MURUBLIGHT_BRICK_STAIRS), keyBlock(MURUBLIGHT_BRICK_WALL));
        tag(MURUBLIGHT_STEMS).add(keyBlock(MURUBLIGHT_STEM), keyBlock(STRIPPED_MURUBLIGHT_STEM), keyBlock(MURUBLIGHT_HYPHAE), keyBlock(STRIPPED_MURUBLIGHT_HYPHAE));

        tag(MURUBLIGHT_WOOD_ITEMS).forceAddTag(MURUBLIGHT_STEMS).add(
                keyBlock(MURUBLIGHT_BUTTON),
                keyBlock(MURUBLIGHT_DOOR),
                keyBlock(MURUBLIGHT_FENCE),
                keyBlock(MURUBLIGHT_FENCE_GATE),
                keyBlock(MURUBLIGHT_HANGING_SIGN),
                keyBlock(MURUBLIGHT_PLANKS),
                keyBlock(MURUBLIGHT_SIGN),
                keyBlock(MURUBLIGHT_SLAB),
                keyBlock(MURUBLIGHT_STAIRS),
                keyBlock(MURUBLIGHT_TRAPDOOR)
        );

        tag(MIRESTONE_BLOCKS).add(
                keyBlock(CHISELED_MIRESTONE),
                keyBlock(MIRESTONE),
                keyBlock(MIRESTONE_BRICKS),
                keyBlock(MIRESTONE_BRICK_SLAB),
                keyBlock(MIRESTONE_BRICK_STAIRS),
                keyBlock(MIRESTONE_BRICK_WALL),
                keyBlock(MIRESTONE_SLAB),
                keyBlock(MIRESTONE_STAIRS),
                keyBlock(MIRESTONE_WALL),
                keyBlock(POLISHED_MIRESTONE),
                keyBlock(POLISHED_MIRESTONE_BUTTON),
                keyBlock(POLISHED_MIRESTONE_PRESSURE_PLATE),
                keyBlock(POLISHED_MIRESTONE_SLAB),
                keyBlock(POLISHED_MIRESTONE_STAIRS),
                keyBlock(POLISHED_MIRESTONE_WALL)
        );

        tag(VERADITE_BLOCKS).add(
                keyBlock(CHISELED_VERADITE),
                keyBlock(VERADITE),
                keyBlock(VERADITE_BRICKS),
                keyBlock(VERADITE_BRICK_SLAB),
                keyBlock(VERADITE_BRICK_STAIRS),
                keyBlock(VERADITE_BRICK_WALL),
                keyBlock(VERADITE_SLAB),
                keyBlock(VERADITE_STAIRS),
                keyBlock(VERADITE_WALL),
                keyBlock(POLISHED_VERADITE),
                keyBlock(POLISHED_VERADITE_BUTTON),
                keyBlock(POLISHED_VERADITE_PRESSURE_PLATE),
                keyBlock(POLISHED_VERADITE_SLAB),
                keyBlock(POLISHED_VERADITE_STAIRS),
                keyBlock(POLISHED_VERADITE_WALL)
        );

        tag(KURODITE_BLOCKS).add(
                keyBlock(CHISELED_KURODITE),
                keyBlock(KURODITE),
                keyBlock(KURODITE_BRICKS),
                keyBlock(KURODITE_BRICK_SLAB),
                keyBlock(KURODITE_BRICK_STAIRS),
                keyBlock(KURODITE_BRICK_WALL),
                keyBlock(KURODITE_SLAB),
                keyBlock(KURODITE_STAIRS),
                keyBlock(KURODITE_WALL),
                keyBlock(POLISHED_KURODITE),
                keyBlock(POLISHED_KURODITE_BUTTON),
                keyBlock(POLISHED_KURODITE_PRESSURE_PLATE),
                keyBlock(POLISHED_KURODITE_SLAB),
                keyBlock(POLISHED_KURODITE_STAIRS),
                keyBlock(POLISHED_KURODITE_WALL)
        );

        tag(SHADOLINE_BLOCKS).add(
                keyBlock(CHISELED_SHADOLINE),
                keyBlock(CUT_SHADOLINE),
                keyBlock(CUT_SHADOLINE_SLAB),
                keyBlock(CUT_SHADOLINE_STAIRS),
                keyBlock(CUT_SHADOLINE_WALL),
                keyBlock(SHADOLINE_BLOCK),
                keyBlock(SHADOLINE_BLOCK_SLAB),
                keyBlock(SHADOLINE_BLOCK_STAIRS),
                keyBlock(SHADOLINE_BLOCK_WALL),
                keyBlock(SHADOLINE_PILLAR),
                keyBlock(SHADOLINE_BARS),
                keyBlock(SHADOLINE_CHAIN)
        );

        tag(ALL_ETCHED_MAGNIA_BLOCKS).addTag(ETCHED_ALLURING_MAGNIA_BLOCKS).addTag(ETCHED_REPULSIVE_MAGNIA_BLOCKS);

        tag(ETCHED_ALLURING_MAGNIA_BLOCKS).add(
                keyBlock(ETCHED_ALLURING_MAGNIA),
                keyBlock(ETCHED_ALLURING_MAGNIA_STAIRS),
                keyBlock(ETCHED_ALLURING_MAGNIA_SLAB),
                keyBlock(ETCHED_ALLURING_MAGNIA_WALL)
        );

        tag(ETCHED_REPULSIVE_MAGNIA_BLOCKS).add(
                keyBlock(ETCHED_REPULSIVE_MAGNIA),
                keyBlock(ETCHED_REPULSIVE_MAGNIA_STAIRS),
                keyBlock(ETCHED_REPULSIVE_MAGNIA_SLAB),
                keyBlock(ETCHED_REPULSIVE_MAGNIA_WALL)
        );

        tag(MAGNIA_BLOCKS).add(keyBlock(ALLURING_MAGNIA), keyBlock(REPULSIVE_MAGNIA));
        tag(MAGNIA_SPROUTS).add(keyBlock(ALLURING_MAGNIA_SPROUT), keyBlock(REPULSIVE_MAGNIA_SPROUT));

        tag(BlockItemTags.BARS.item()).add(keyBlock(SHADOLINE_BARS));
        tag(BEACON_PAYMENT_ITEMS).add(NEBULITE);
        tag(BlockItemTags.CHAINS.item()).add(keyBlock(SHADOLINE_CHAIN));
        tag(CHEST_ARMOR).add(SHADOLINE_CHESTPLATE);
        tag(DURABILITY_ENCHANTABLE).add(END_STONE_RUBBLE_SHIELD, MIRESTONE_RUBBLE_SHIELD, VERADITE_RUBBLE_SHIELD, KURODITE_RUBBLE_SHIELD);
        tag(EQUIPPABLE_ENCHANTABLE).add(SHULKER_SHELL);
        tag(FIRE_ASPECT_ENCHANTABLE).add(DAGGER);
        tag(FOOT_ARMOR).add(SHADOLINE_BOOTS);
        tag(HANGING_SIGNS).add(VEILED_HANGING_SIGN_ITEM, CELESTIAL_HANGING_SIGN_ITEM, MURUBLIGHT_HANGING_SIGN_ITEM);
        tag(HEAD_ARMOR).add(SHADOLINE_HELMET);
        tag(ItemTags.FENCE_GATES).add(keyBlock(CELESTIAL_FENCE_GATE), keyBlock(MURUBLIGHT_FENCE_GATE));
        tag(BlockItemTags.FLOWERS.item()).add(keyBlock(WISP_FLOWER), keyBlock(FLANGER_BERRY_FLOWER));
        tag(BlockItemTags.SMALL_FLOWERS.item()).add(keyBlock(BULB_FLOWER));
        tag(ItemTags.WOODEN_FENCES).add(keyBlock(VEILED_FENCE), keyBlock(CELESTIAL_FENCE), keyBlock(MURUBLIGHT_FENCE));
        tag(LEAVES).add(keyBlock(VEILED_LEAVES));
        tag(LEG_ARMOR).add(SHADOLINE_LEGGINGS, DRIFT_LEGGINGS);
        tag(LOGS_THAT_BURN).forceAddTag(VEILED_LOGS).forceAddTag(CELESTIAL_STEMS).forceAddTag(MURUBLIGHT_STEMS);
        tag(PLANKS).add(keyBlock(VEILED_PLANKS), keyBlock(CELESTIAL_PLANKS), keyBlock(MURUBLIGHT_PLANKS));
        tag(SAPLINGS).add(keyBlock(VEILED_SAPLING));
        tag(SHARP_WEAPON_ENCHANTABLE).add(DAGGER);
        tag(SIGNS).add(VEILED_SIGN_ITEM, CELESTIAL_SIGN_ITEM, MURUBLIGHT_SIGN_ITEM);
        tag(BlockItemTags.SLABS.item()).add(keyBlock(ETCHED_ALLURING_MAGNIA_SLAB), keyBlock(ETCHED_REPULSIVE_MAGNIA_SLAB), keyBlock(VEILED_SLAB), keyBlock(CELESTIAL_SLAB), keyBlock(CELESTIAL_BRICK_SLAB), keyBlock(MURUBLIGHT_SLAB), keyBlock(MURUBLIGHT_BRICK_SLAB), keyBlock(VERADITE_SLAB), keyBlock(POLISHED_VERADITE_SLAB), keyBlock(VERADITE_BRICK_SLAB), keyBlock(KURODITE_SLAB), keyBlock(POLISHED_KURODITE_SLAB), keyBlock(KURODITE_BRICK_SLAB), keyBlock(SHADOLINE_BLOCK_SLAB), keyBlock(CUT_SHADOLINE_SLAB));
        tag(BlockItemTags.STAIRS.item()).add(keyBlock(ETCHED_ALLURING_MAGNIA_SLAB), keyBlock(ETCHED_REPULSIVE_MAGNIA_STAIRS), keyBlock(VEILED_STAIRS), keyBlock(CELESTIAL_STAIRS), keyBlock(CELESTIAL_BRICK_STAIRS), keyBlock(MURUBLIGHT_STAIRS), keyBlock(MURUBLIGHT_BRICK_STAIRS), keyBlock(VERADITE_STAIRS), keyBlock(POLISHED_VERADITE_STAIRS), keyBlock(VERADITE_BRICK_STAIRS), keyBlock(KURODITE_STAIRS), keyBlock(POLISHED_KURODITE_STAIRS), keyBlock(KURODITE_BRICK_STAIRS), keyBlock(SHADOLINE_BLOCK_STAIRS), keyBlock(CUT_SHADOLINE_STAIRS));
        tag(BlockItemTags.STONE_BUTTONS.item()).add(keyBlock(POLISHED_VERADITE_BUTTON), keyBlock(POLISHED_KURODITE_BUTTON));
        tag(STONE_CRAFTING_MATERIALS).add(END_STONE.item(), keyBlock(VERADITE), keyBlock(MIRESTONE), keyBlock(KURODITE));
        tag(STONE_TOOL_MATERIALS).add(END_STONE.item(), keyBlock(VERADITE), keyBlock(MIRESTONE), keyBlock(KURODITE));
        tag(MELEE_WEAPON_ENCHANTABLE).add(DAGGER);
        tag(TRIMMABLE_ARMOR).add(SHADOLINE_HELMET, SHADOLINE_CHESTPLATE, SHADOLINE_LEGGINGS, SHADOLINE_BOOTS, DRIFT_LEGGINGS);
        tag(TRIM_MATERIALS).add(NEBULITE, SHADOLINE_INGOT);
        tag(VANISHING_ENCHANTABLE).add(MIRROR, SHULKER_SHELL);
        tag(WALLS).add(keyBlock(ETCHED_ALLURING_MAGNIA_WALL), keyBlock(ETCHED_REPULSIVE_MAGNIA_WALL), keyBlock(VERADITE_WALL), keyBlock(POLISHED_VERADITE_WALL), keyBlock(VERADITE_BRICK_WALL), keyBlock(SHADOLINE_BLOCK_WALL), keyBlock(KURODITE_WALL), keyBlock(POLISHED_KURODITE_WALL), keyBlock(KURODITE_BRICK_WALL), keyBlock(CUT_SHADOLINE_WALL), keyBlock(CELESTIAL_BRICK_WALL), keyBlock(MURUBLIGHT_BRICK_WALL));
        tag(WOODEN_BUTTONS).add(keyBlock(VEILED_BUTTON), keyBlock(CELESTIAL_BUTTON), keyBlock(MURUBLIGHT_BUTTON));
        tag(WOODEN_DOORS).add(keyBlock(VEILED_DOOR), keyBlock(CELESTIAL_DOOR), keyBlock(MURUBLIGHT_DOOR));
        tag(WOODEN_PRESSURE_PLATES).add(keyBlock(VEILED_PRESSURE_PLATE), keyBlock(CELESTIAL_PRESSURE_PLATE), keyBlock(MURUBLIGHT_PRESSURE_PLATE));
        tag(ItemTags.WOODEN_SHELVES).add(VEILED_SHELF_ITEM, CELESTIAL_SHELF_ITEM, MURUBLIGHT_SHELF_ITEM);
        tag(WOODEN_SLABS).add(keyBlock(VEILED_SLAB), keyBlock(CELESTIAL_SLAB), keyBlock(MURUBLIGHT_SLAB));
        tag(WOODEN_STAIRS).add(keyBlock(VEILED_STAIRS), keyBlock(CELESTIAL_STAIRS), keyBlock(MURUBLIGHT_STAIRS));
        tag(WOODEN_TRAPDOORS).add(keyBlock(VEILED_TRAPDOOR), keyBlock(CELESTIAL_TRAPDOOR), keyBlock(MURUBLIGHT_TRAPDOOR));

        // Item Descriptions

        tag(externalKey("item_tooltips", "has_description")).add(MAGNIA_ATTRACTOR, DRIFT_LEGGINGS, DRIFT_JELLY_BOTTLE, keyBlock(DRIFT_JELLY_BLOCK), NEBULITE).addTag(RUBBLE_SHIELDS);

        // Common Tags

        tag(BERRY_FOODS).add(FLANGER_BERRY);
        tag(BUCKETS).add(RUSTLE_BUCKET);
        tag(ConventionalItemTags.CHAINS).add(keyBlock(SHADOLINE_CHAIN));
        tag(DRIFT_JELLY_DRINKS).add(DRIFT_JELLY_BOTTLE);
        tag(DRINKS).add(DRIFT_JELLY_BOTTLE);
        tag(DRINK_CONTAINING_BOTTLE).add(DRIFT_JELLY_BOTTLE);
        tag(EDIBLE_WHEN_PLACED_FOODS).add(CHORUS_CAKE_ROLL_ITEM);
        tag(FOODS).add(DRIFT_JELLY_BOTTLE);
        tag(GEMS).add(NEBULITE);
        tag(INGOTS).add(SHADOLINE_INGOT);
        tag(NUGGETS).add(SHADOLINE_NUGGET);
        tag(MAGIC_DRINKS).add(DRIFT_JELLY_BOTTLE);
        tag(MUSIC_DISCS).add(MUSIC_DISC_GLARE, MUSIC_DISC_DECAY, MUSIC_DISC_BLISS);
        tag(NEBULITE_GEMS).add(NEBULITE);
        tag(NEBULITE_ORES).add(keyBlock(NEBULITE_ORE), keyBlock(MIRESTONE_NEBULITE_ORE));
        tag(NEBULITE_STORAGE_BLOCKS).add(keyBlock(NEBULITE_BLOCK));
        tag(RAW_MATERIALS).add(RAW_SHADOLINE);
        tag(RAW_SHADOLINE_STORAGE_BLOCKS).add(keyBlock(RAW_SHADOLINE_BLOCK));
        tag(SHADOLINE_INGOTS).add(SHADOLINE_INGOT);
        tag(SHADOLINE_NUGGETS).add(SHADOLINE_NUGGET);
        tag(SHADOLINE_ORES).add(keyBlock(SHADOLINE_ORE), keyBlock(MIRESTONE_SHADOLINE_ORE));
        tag(SHADOLINE_RAW_MATERIALS).add(RAW_SHADOLINE);
        tag(SHADOLINE_STORAGE_BLOCKS).add(keyBlock(SHADOLINE_BLOCK));
        tag(SHIELD_TOOLS).add(END_STONE_RUBBLE_SHIELD, VERADITE_RUBBLE_SHIELD, MIRESTONE_RUBBLE_SHIELD, KURODITE_RUBBLE_SHIELD);
        tag(STORAGE_BLOCKS).add(keyBlock(SHADOLINE_BLOCK), keyBlock(NEBULITE_BLOCK), keyBlock(DRIFT_JELLY_BLOCK));
        tag(TOOLS).add(MIRROR, MAGNIA_ATTRACTOR);

        tag(externalKey("create", "upright_on_belt")).add(DRIFT_JELLY_BOTTLE);
        tag(externalKey("create", "deployable_drink")).add(DRIFT_JELLY_BOTTLE);
    }

    private TagKey<Item> externalKey(String namespace, String path) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(namespace, path));
    }
}
