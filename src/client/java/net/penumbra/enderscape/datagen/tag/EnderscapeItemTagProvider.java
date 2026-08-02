package net.penumbra.enderscape.datagen.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

import static net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.*;
import static net.minecraft.tags.ItemTags.*;
import static net.minecraft.world.item.Items.*;
import static net.penumbra.enderscape.registry.block.EnderscapeBlocks.*;
import static net.penumbra.enderscape.registry.item.EnderscapeItems.*;
import static net.penumbra.enderscape.registry.tag.EnderscapeItemTags.*;

public class EnderscapeItemTagProvider extends FabricTagsProvider.ItemTagsProvider {

    public EnderscapeItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, null);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        valueLookupBuilder(WEAK_MAGNIA_STRENGTH).add(
                CHAINMAIL_HELMET,
                CHAINMAIL_CHESTPLATE,
                CHAINMAIL_LEGGINGS,
                CHAINMAIL_BOOTS,
                NETHERITE_HELMET,
                NETHERITE_CHESTPLATE,
                NETHERITE_LEGGINGS,
                NETHERITE_BOOTS,
                NETHERITE_HORSE_ARMOR,
                NETHERITE_NAUTILUS_ARMOR
        );

        valueLookupBuilder(AVERAGE_MAGNIA_STRENGTH).add(
                IRON_HELMET,
                IRON_CHESTPLATE,
                IRON_LEGGINGS,
                IRON_BOOTS,
                IRON_HORSE_ARMOR,
                IRON_NAUTILUS_ARMOR
        );

        valueLookupBuilder(STRONG_MAGNIA_STRENGTH);

        valueLookupBuilder(DAGGER_ENCHANTABLE).add(DAGGER);
        valueLookupBuilder(CANNOT_COMBINE_IN_ANVIL).add(MIRROR);
        valueLookupBuilder(DRIFTER_FOOD).add(CHORUS_FRUIT, PURUBERRY);
        valueLookupBuilder(ELYTRA_ENCHANTABLE).add(ELYTRA);
        valueLookupBuilder(END_HAVEN_CORE_FUELS).add(NEBULITE);
        valueLookupBuilder(LANTERNS).add(BULB_LANTERN.asItem(), VOID_LANTERN.asItem());
        valueLookupBuilder(MAGNIA_ATTRACTOR_ENCHANTABLE).add(MAGNIA_ATTRACTOR);
        valueLookupBuilder(MIRROR_ENCHANTABLE).add(MIRROR);
        valueLookupBuilder(NEBULITE_TOOLS).add(DAGGER, MIRROR, MAGNIA_ATTRACTOR);
        valueLookupBuilder(NEBULITE_TOOL_ENCHANTABLE).forceAddTag(NEBULITE_TOOLS);
        valueLookupBuilder(NEBULITE_TOOL_FUELS).add(NEBULITE);
        valueLookupBuilder(REPAIRS_DRIFT_LEGGINGS).add(DRIFT_JELLY_BOTTLE);
        valueLookupBuilder(REPAIRS_RUBBLE_SHIELDS).add(RUBBLE_CHITIN);
        valueLookupBuilder(REPAIRS_SHADOLINE_ARMOR).add(SHADOLINE_INGOT);
        valueLookupBuilder(RUSTLE_FOOD).add(MURUBLIGHT_BRACKET_ITEM);

        valueLookupBuilder(VOID_IMMUNE).add(
                BLINKLAMP.asItem(),
                BLINKLIGHT,
                CORRUPT_GROWTH.asItem(),
                CORRUPT_OVERGROWTH.asItem(),
                CORRUPT_PATH.asItem(),
                DAGGER,
                DRAGON_EGG,
                DRAGON_HEAD,
                ENDER_CHEST,
                ENDER_EYE,
                ENDER_PEARL,
                END_HAVEN_CORE.asItem(),
                ELYTRA,
                END_CRYSTAL,
                MAGNIA_ATTRACTOR,
                MIRROR,
                MURUBLIGHT_BRACKET_ITEM,
                MURUBLIGHT_CAP.asItem(),
                MURUBLIGHT_CHANTERELLE.asItem(),
                MUSIC_DISC_DECAY,
                MUSIC_DISC_GLARE,
                NEBULITE,
                NEBULITE_BLOCK.asItem(),
                NEBULITE_SHARDS,
                RAW_SHADOLINE,
                RAW_SHADOLINE_BLOCK.asItem(),
                RUBBLE_CHITIN,
                RUBBLE_SHIELD,
                RUSTLE_SILK,
                SHADOLINE_BOOTS,
                SHADOLINE_CHESTPLATE,
                SHADOLINE_HELMET,
                SHADOLINE_INGOT,
                SHADOLINE_LEGGINGS,
                SHADOLINE_NUGGET,
                STASIS_ARMOR_TRIM_SMITHING_TEMPLATE,
                VOID_CAMPFIRE.asItem(),
                VOID_LANTERN.asItem(),
                VOID_SHALE.asItem(),
                VOID_TORCH.asItem()
        ).forceAddTag(SHADOLINE_BLOCKS).forceAddTag(DUSK_PURPUR_BLOCKS).forceAddTag(KURODITE_BLOCKS).forceAddTag(MURUBLIGHT_BRICK_BLOCKS).forceAddTag(MURUBLIGHT_STEMS).forceAddTag(MURUBLIGHT_WOOD_ITEMS);

        valueLookupBuilder(DUSK_PURPUR_BLOCKS).add(DUSK_PURPUR_BLOCK.asItem(), DUSK_PURPUR_PILLAR.asItem(), DUSK_PURPUR_STAIRS.asItem(), DUSK_PURPUR_SLAB.asItem(), DUSK_PURPUR_WALL.asItem(), CHISELED_DUSK_PURPUR.asItem());
        valueLookupBuilder(END_ORE_BLOCKS).add(NEBULITE_ORE.asItem(), SHADOLINE_ORE.asItem(), MIRESTONE_NEBULITE_ORE.asItem(), MIRESTONE_SHADOLINE_ORE.asItem());
        valueLookupBuilder(NEW_END_STONE_BLOCKS).add(
                CHISELED_END_STONE.asItem(),
                CRACKED_END_STONE_BRICKS.asItem(),
                END_STONE_SLAB.asItem(),
                END_STONE_STAIRS.asItem(),
                END_STONE_WALL.asItem(),
                OVERGROWN_END_STONE_BRICKS.asItem(),
                OVERGROWN_END_STONE_BRICK_SLAB.asItem(),
                OVERGROWN_END_STONE_BRICK_STAIRS.asItem(),
                OVERGROWN_END_STONE_BRICK_WALL.asItem(),
                POLISHED_END_STONE.asItem(),
                POLISHED_END_STONE_BUTTON.asItem(),
                POLISHED_END_STONE_PRESSURE_PLATE.asItem(),
                POLISHED_END_STONE_SLAB.asItem(),
                POLISHED_END_STONE_STAIRS.asItem(),
                POLISHED_END_STONE_WALL.asItem()
        );

        valueLookupBuilder(NEW_PURPUR_BLOCKS).add(
                CHISELED_PURPUR.asItem(),
                PURPUR_WALL.asItem()
        );

        valueLookupBuilder(OVERGROWTH_BLOCKS).add(
                CELESTIAL_OVERGROWTH.asItem(),
                CORRUPT_OVERGROWTH.asItem()
        );

        valueLookupBuilder(PURPUR_TILE_BLOCKS).add(
                PURPUR_TILES.asItem(),
                PURPUR_TILE_STAIRS.asItem(),
                PURPUR_TILE_SLAB.asItem()
        );

        valueLookupBuilder(CHANTERELLE_BRICK_BLOCKS).forceAddTag(CELESTIAL_BRICK_BLOCKS).forceAddTag(MURUBLIGHT_BRICK_BLOCKS);
        valueLookupBuilder(CHANTERELLE_CAP_BLOCKS).add(CELESTIAL_CAP.asItem(), MURUBLIGHT_CAP.asItem());

        valueLookupBuilder(CELESTIAL_BRICK_BLOCKS).add(CELESTIAL_BRICKS.asItem(), CELESTIAL_BRICK_SLAB.asItem(), CELESTIAL_BRICK_STAIRS.asItem(), CELESTIAL_BRICK_WALL.asItem());
        valueLookupBuilder(CELESTIAL_STEMS).add(CELESTIAL_STEM.asItem().asItem(), STRIPPED_CELESTIAL_STEM.asItem(), CELESTIAL_HYPHAE.asItem(), STRIPPED_CELESTIAL_HYPHAE.asItem());
        valueLookupBuilder(CELESTIAL_WOOD_ITEMS).forceAddTag(CELESTIAL_STEMS).add(
                CELESTIAL_BUTTON.asItem(),
                CELESTIAL_DOOR.asItem(),
                CELESTIAL_FENCE.asItem(),
                CELESTIAL_FENCE_GATE.asItem(),
                CELESTIAL_HANGING_SIGN.asItem(),
                CELESTIAL_PLANKS.asItem(),
                CELESTIAL_PRESSURE_PLATE.asItem(),
                CELESTIAL_SHELF_ITEM,
                CELESTIAL_SIGN.asItem(),
                CELESTIAL_SLAB.asItem(),
                CELESTIAL_STAIRS.asItem(),
                CELESTIAL_TRAPDOOR.asItem()
        );

        valueLookupBuilder(VEILED_LOGS).add(VEILED_LOG.asItem().asItem(), STRIPPED_VEILED_LOG.asItem(), VEILED_WOOD.asItem(), STRIPPED_VEILED_WOOD.asItem());
        valueLookupBuilder(VEILED_WOOD_ITEMS).forceAddTag(VEILED_LOGS).add(
                VEILED_BUTTON.asItem(),
                VEILED_DOOR.asItem(),
                VEILED_FENCE.asItem(),
                VEILED_FENCE_GATE.asItem(),
                VEILED_HANGING_SIGN.asItem(),
                VEILED_PLANKS.asItem(),
                VEILED_PRESSURE_PLATE.asItem(),
                VEILED_SHELF_ITEM,
                VEILED_SIGN.asItem(),
                VEILED_SLAB.asItem(),
                VEILED_STAIRS.asItem(),
                VEILED_TRAPDOOR.asItem()
        );

        valueLookupBuilder(MURUBLIGHT_BRICK_BLOCKS).add(MURUBLIGHT_BRICKS.asItem(), MURUBLIGHT_BRICK_SLAB.asItem(), MURUBLIGHT_BRICK_STAIRS.asItem(), MURUBLIGHT_BRICK_WALL.asItem());
        valueLookupBuilder(MURUBLIGHT_STEMS).add(MURUBLIGHT_STEM.asItem(), STRIPPED_MURUBLIGHT_STEM.asItem(), MURUBLIGHT_HYPHAE.asItem(), STRIPPED_MURUBLIGHT_HYPHAE.asItem());

        valueLookupBuilder(MURUBLIGHT_WOOD_ITEMS).forceAddTag(MURUBLIGHT_STEMS).add(
                MURUBLIGHT_BUTTON.asItem(),
                MURUBLIGHT_DOOR.asItem(),
                MURUBLIGHT_FENCE.asItem(),
                MURUBLIGHT_FENCE_GATE.asItem(),
                MURUBLIGHT_HANGING_SIGN.asItem(),
                MURUBLIGHT_PLANKS.asItem(),
                MURUBLIGHT_PRESSURE_PLATE.asItem(),
                MURUBLIGHT_SHELF_ITEM,
                MURUBLIGHT_SIGN.asItem(),
                MURUBLIGHT_SLAB.asItem(),
                MURUBLIGHT_STAIRS.asItem(),
                MURUBLIGHT_TRAPDOOR.asItem()
        );

        valueLookupBuilder(MIRESTONE_BLOCKS).add(
                CHISELED_MIRESTONE.asItem(),
                CRACKED_MIRESTONE_BRICKS.asItem(),
                MIRESTONE.asItem(),
                MIRESTONE_BRICKS.asItem(),
                MIRESTONE_BRICK_SLAB.asItem(),
                MIRESTONE_BRICK_STAIRS.asItem(),
                MIRESTONE_BRICK_WALL.asItem(),
                OVERGROWN_MIRESTONE_BRICKS.asItem(),
                OVERGROWN_MIRESTONE_BRICK_SLAB.asItem(),
                OVERGROWN_MIRESTONE_BRICK_STAIRS.asItem(),
                OVERGROWN_MIRESTONE_BRICK_WALL.asItem(),
                MIRESTONE_SLAB.asItem(),
                MIRESTONE_STAIRS.asItem(),
                MIRESTONE_WALL.asItem(),
                POLISHED_MIRESTONE.asItem(),
                POLISHED_MIRESTONE_BUTTON.asItem(),
                POLISHED_MIRESTONE_PRESSURE_PLATE.asItem(),
                POLISHED_MIRESTONE_SLAB.asItem(),
                POLISHED_MIRESTONE_STAIRS.asItem(),
                POLISHED_MIRESTONE_WALL.asItem()
        );

        valueLookupBuilder(VERADITE_BLOCKS).add(
                CHISELED_VERADITE.asItem(),
                VERADITE.asItem(),
                VERADITE_BRICKS.asItem(),
                VERADITE_BRICK_SLAB.asItem(),
                VERADITE_BRICK_STAIRS.asItem(),
                VERADITE_BRICK_WALL.asItem(),
                VERADITE_SLAB.asItem(),
                VERADITE_STAIRS.asItem(),
                VERADITE_WALL.asItem(),
                POLISHED_VERADITE.asItem(),
                POLISHED_VERADITE_BUTTON.asItem(),
                POLISHED_VERADITE_PRESSURE_PLATE.asItem(),
                POLISHED_VERADITE_SLAB.asItem(),
                POLISHED_VERADITE_STAIRS.asItem(),
                POLISHED_VERADITE_WALL.asItem()
        );

        valueLookupBuilder(KURODITE_BLOCKS).add(
                CHISELED_KURODITE.asItem(),
                KURODITE.asItem(),
                KURODITE_BRICKS.asItem(),
                KURODITE_BRICK_SLAB.asItem(),
                KURODITE_BRICK_STAIRS.asItem(),
                KURODITE_BRICK_WALL.asItem(),
                KURODITE_SLAB.asItem(),
                KURODITE_STAIRS.asItem(),
                KURODITE_WALL.asItem(),
                POLISHED_KURODITE.asItem(),
                POLISHED_KURODITE_BUTTON.asItem(),
                POLISHED_KURODITE_PRESSURE_PLATE.asItem(),
                POLISHED_KURODITE_SLAB.asItem(),
                POLISHED_KURODITE_STAIRS.asItem(),
                POLISHED_KURODITE_WALL.asItem()
        );

        valueLookupBuilder(SHADOLINE_BLOCKS).add(
                CHISELED_SHADOLINE.asItem(),
                CUT_SHADOLINE.asItem(),
                CUT_SHADOLINE_SLAB.asItem(),
                CUT_SHADOLINE_STAIRS.asItem(),
                CUT_SHADOLINE_WALL.asItem(),
                SHADOLINE_BLOCK.asItem(),
                SHADOLINE_BLOCK_SLAB.asItem(),
                SHADOLINE_BLOCK_STAIRS.asItem(),
                SHADOLINE_BLOCK_WALL.asItem(),
                SHADOLINE_PILLAR.asItem(),
                SHADOLINE_BARS.asItem(),
                SHADOLINE_CHAIN.asItem()
        );

        valueLookupBuilder(ALL_ETCHED_MAGNIA_BLOCKS).addTag(ETCHED_ALLURING_MAGNIA_BLOCKS).addTag(ETCHED_REPULSIVE_MAGNIA_BLOCKS);

        valueLookupBuilder(ETCHED_ALLURING_MAGNIA_BLOCKS).add(
                ETCHED_ALLURING_MAGNIA.asItem(),
                ETCHED_ALLURING_MAGNIA_STAIRS.asItem(),
                ETCHED_ALLURING_MAGNIA_SLAB.asItem(),
                ETCHED_ALLURING_MAGNIA_WALL.asItem()
        );

        valueLookupBuilder(ETCHED_REPULSIVE_MAGNIA_BLOCKS).add(
                ETCHED_REPULSIVE_MAGNIA.asItem(),
                ETCHED_REPULSIVE_MAGNIA_STAIRS.asItem(),
                ETCHED_REPULSIVE_MAGNIA_SLAB.asItem(),
                ETCHED_REPULSIVE_MAGNIA_WALL.asItem()
        );

        valueLookupBuilder(MAGNIA_BLOCKS).add(ALLURING_MAGNIA.asItem(), REPULSIVE_MAGNIA.asItem());
        valueLookupBuilder(MAGNIA_SPROUTS).add(ALLURING_MAGNIA_SPROUT.asItem(), REPULSIVE_MAGNIA_SPROUT.asItem());

        valueLookupBuilder(ItemTags.BARS).add(SHADOLINE_BARS.asItem());
        valueLookupBuilder(BEACON_PAYMENT_ITEMS).add(NEBULITE);
        valueLookupBuilder(ItemTags.CHAINS).add(SHADOLINE_CHAIN.asItem());
        valueLookupBuilder(CHEST_ARMOR).add(SHADOLINE_CHESTPLATE);
        valueLookupBuilder(DURABILITY_ENCHANTABLE).add(RUBBLE_SHIELD);
        valueLookupBuilder(EQUIPPABLE_ENCHANTABLE).add(SHULKER_SHELL);
        valueLookupBuilder(FIRE_ASPECT_ENCHANTABLE).add(DAGGER);
        valueLookupBuilder(FOOT_ARMOR).add(SHADOLINE_BOOTS);
        valueLookupBuilder(HANGING_SIGNS).add(VEILED_HANGING_SIGN_ITEM.asItem(), CELESTIAL_HANGING_SIGN_ITEM, MURUBLIGHT_HANGING_SIGN_ITEM);
        valueLookupBuilder(HEAD_ARMOR).add(SHADOLINE_HELMET);
        valueLookupBuilder(ItemTags.FENCE_GATES).add(VEILED_FENCE_GATE.asItem(), CELESTIAL_FENCE_GATE.asItem(), MURUBLIGHT_FENCE_GATE.asItem());
        valueLookupBuilder(ItemTags.FLOWERS).add(WISP_FLOWER.asItem(), PURUBERRY_FLOWER.asItem());
        valueLookupBuilder(ItemTags.SMALL_FLOWERS).add(BULB_FLOWER.asItem());
        valueLookupBuilder(ItemTags.WOODEN_FENCES).add(VEILED_FENCE.asItem(), CELESTIAL_FENCE.asItem(), MURUBLIGHT_FENCE.asItem());
        valueLookupBuilder(LEAVES).add(VEILED_LEAVES.asItem());
        valueLookupBuilder(LEG_ARMOR).add(SHADOLINE_LEGGINGS, DRIFT_LEGGINGS);
        valueLookupBuilder(LOGS_THAT_BURN).forceAddTag(VEILED_LOGS).forceAddTag(CELESTIAL_STEMS).forceAddTag(MURUBLIGHT_STEMS);
        valueLookupBuilder(LOOM_PATTERNS).add(CRESCENT_BANNER_PATTERN.asItem());
        valueLookupBuilder(PLANKS).add(VEILED_PLANKS.asItem(), CELESTIAL_PLANKS.asItem(), MURUBLIGHT_PLANKS.asItem());
        valueLookupBuilder(SAPLINGS).add(VEILED_SAPLING.asItem());
        valueLookupBuilder(SHARP_WEAPON_ENCHANTABLE).add(DAGGER);
        valueLookupBuilder(SIGNS).add(VEILED_SIGN_ITEM.asItem(), CELESTIAL_SIGN_ITEM, MURUBLIGHT_SIGN_ITEM);
        valueLookupBuilder(SLABS).add(
                CELESTIAL_BRICK_SLAB.asItem(),
                CELESTIAL_SLAB.asItem(),
                CUT_SHADOLINE_SLAB.asItem(),
                DUSK_PURPUR_SLAB.asItem(),
                END_STONE_SLAB.asItem(),
                ETCHED_ALLURING_MAGNIA_SLAB.asItem(),
                ETCHED_REPULSIVE_MAGNIA_SLAB.asItem(),
                KURODITE_BRICK_SLAB.asItem(),
                KURODITE_SLAB.asItem(),
                MIRESTONE_BRICK_SLAB.asItem(),
                MIRESTONE_SLAB.asItem(),
                MURUBLIGHT_BRICK_SLAB.asItem(),
                MURUBLIGHT_SLAB.asItem(),
                OVERGROWN_END_STONE_BRICK_SLAB.asItem(),
                OVERGROWN_MIRESTONE_BRICK_SLAB.asItem(),
                POLISHED_END_STONE_SLAB.asItem(),
                POLISHED_KURODITE_SLAB.asItem(),
                POLISHED_MIRESTONE_SLAB.asItem(),
                POLISHED_VERADITE_SLAB.asItem(),
                PURPUR_TILE_SLAB.asItem(),
                SHADOLINE_BLOCK_SLAB.asItem(),
                VEILED_SLAB.asItem(),
                VERADITE_BRICK_SLAB.asItem(),
                VERADITE_SLAB.asItem()
        );

        valueLookupBuilder(STAIRS).add(
                CELESTIAL_BRICK_STAIRS.asItem(),
                CELESTIAL_STAIRS.asItem(),
                CUT_SHADOLINE_STAIRS.asItem(),
                DUSK_PURPUR_STAIRS.asItem(),
                END_STONE_STAIRS.asItem(),
                ETCHED_ALLURING_MAGNIA_STAIRS.asItem(),
                ETCHED_REPULSIVE_MAGNIA_STAIRS.asItem(),
                KURODITE_BRICK_STAIRS.asItem(),
                KURODITE_STAIRS.asItem(),
                MIRESTONE_BRICK_STAIRS.asItem(),
                MIRESTONE_STAIRS.asItem(),
                MURUBLIGHT_BRICK_STAIRS.asItem(),
                MURUBLIGHT_STAIRS.asItem(),
                OVERGROWN_END_STONE_BRICK_STAIRS.asItem(),
                OVERGROWN_MIRESTONE_BRICK_STAIRS.asItem(),
                POLISHED_END_STONE_STAIRS.asItem(),
                POLISHED_KURODITE_STAIRS.asItem(),
                POLISHED_MIRESTONE_STAIRS.asItem(),
                POLISHED_VERADITE_STAIRS.asItem(),
                PURPUR_TILE_STAIRS.asItem(),
                SHADOLINE_BLOCK_STAIRS.asItem(),
                VEILED_STAIRS.asItem(),
                VERADITE_BRICK_STAIRS.asItem(),
                VERADITE_STAIRS.asItem()
        );

        valueLookupBuilder(STONE_BUTTONS).add(POLISHED_END_STONE_BUTTON.asItem(), POLISHED_MIRESTONE_BUTTON.asItem(), POLISHED_VERADITE_BUTTON.asItem(), POLISHED_KURODITE_BUTTON.asItem());
        valueLookupBuilder(STONE_CRAFTING_MATERIALS).add(END_STONE.asItem(), VERADITE.asItem(), MIRESTONE.asItem(), KURODITE.asItem());
        valueLookupBuilder(STONE_TOOL_MATERIALS).add(END_STONE.asItem(), VERADITE.asItem(), MIRESTONE.asItem(), KURODITE.asItem());
        valueLookupBuilder(MELEE_WEAPON_ENCHANTABLE).add(DAGGER);
        valueLookupBuilder(METAL_NUGGETS).add(SHADOLINE_NUGGET);
        valueLookupBuilder(TRIMMABLE_ARMOR).add(SHADOLINE_HELMET, SHADOLINE_CHESTPLATE, SHADOLINE_LEGGINGS, SHADOLINE_BOOTS, DRIFT_LEGGINGS);
        valueLookupBuilder(TRIM_MATERIALS).add(NEBULITE, SHADOLINE_INGOT);
        valueLookupBuilder(VANISHING_ENCHANTABLE).add(DAGGER, MAGNIA_ATTRACTOR, MIRROR, SHULKER_SHELL);
        valueLookupBuilder(WALLS).add(
                CELESTIAL_BRICK_WALL.asItem(),
                CUT_SHADOLINE_WALL.asItem(),
                DUSK_PURPUR_WALL.asItem(),
                END_STONE_WALL.asItem(),
                ETCHED_ALLURING_MAGNIA_WALL.asItem(),
                ETCHED_REPULSIVE_MAGNIA_WALL.asItem(),
                KURODITE_BRICK_WALL.asItem(),
                KURODITE_WALL.asItem(),
                MIRESTONE_BRICK_WALL.asItem(),
                MIRESTONE_WALL.asItem(),
                MURUBLIGHT_BRICK_WALL.asItem(),
                OVERGROWN_END_STONE_BRICK_WALL.asItem(),
                OVERGROWN_MIRESTONE_BRICK_WALL.asItem(),
                POLISHED_END_STONE_WALL.asItem(),
                POLISHED_KURODITE_WALL.asItem(),
                POLISHED_MIRESTONE_WALL.asItem(),
                POLISHED_VERADITE_WALL.asItem(),
                PURPUR_WALL.asItem(),
                SHADOLINE_BLOCK_WALL.asItem(),
                VERADITE_BRICK_WALL.asItem(),
                VERADITE_WALL.asItem()
        );
        valueLookupBuilder(WOODEN_BUTTONS).add(VEILED_BUTTON.asItem(), CELESTIAL_BUTTON.asItem(), MURUBLIGHT_BUTTON.asItem());
        valueLookupBuilder(WOODEN_DOORS).add(VEILED_DOOR.asItem(), CELESTIAL_DOOR.asItem(), MURUBLIGHT_DOOR.asItem());
        valueLookupBuilder(WOODEN_PRESSURE_PLATES).add(VEILED_PRESSURE_PLATE.asItem(), CELESTIAL_PRESSURE_PLATE.asItem(), MURUBLIGHT_PRESSURE_PLATE.asItem());
        valueLookupBuilder(ItemTags.WOODEN_SHELVES).add(VEILED_SHELF_ITEM, CELESTIAL_SHELF_ITEM, MURUBLIGHT_SHELF_ITEM);
        valueLookupBuilder(WOODEN_SLABS).add(VEILED_SLAB.asItem(), CELESTIAL_SLAB.asItem(), MURUBLIGHT_SLAB.asItem());
        valueLookupBuilder(WOODEN_STAIRS).add(VEILED_STAIRS.asItem(), CELESTIAL_STAIRS.asItem(), MURUBLIGHT_STAIRS.asItem());
        valueLookupBuilder(WOODEN_TRAPDOORS).add(VEILED_TRAPDOOR.asItem(), CELESTIAL_TRAPDOOR.asItem(), MURUBLIGHT_TRAPDOOR.asItem());

        // Item Descriptions

        valueLookupBuilder(externalKey("item_tooltips", "has_description")).add(MAGNIA_ATTRACTOR, DRIFT_LEGGINGS, DRIFT_JELLY_BOTTLE, DRIFT_JELLY_BLOCK.asItem(), NEBULITE, RUBBLE_SHIELD);

        // Conventional Tags

        valueLookupBuilder(ARMORS).add(SHADOLINE_HELMET, SHADOLINE_CHESTPLATE, SHADOLINE_LEGGINGS, SHADOLINE_BOOTS, DRIFT_LEGGINGS);
        valueLookupBuilder(HUMANOID_ARMORS).add(SHADOLINE_HELMET, SHADOLINE_CHESTPLATE, SHADOLINE_LEGGINGS, SHADOLINE_BOOTS, DRIFT_LEGGINGS);
        valueLookupBuilder(BERRY_FOODS).add(PURUBERRY);
        valueLookupBuilder(BUCKETS).add(RUSTLE_BUCKET, VOID_LACHRYMA_BUCKET);
        valueLookupBuilder(ConventionalItemTags.CHAINS).add(SHADOLINE_CHAIN.asItem());
        valueLookupBuilder(DRIFT_JELLY_DRINKS).add(DRIFT_JELLY_BOTTLE);
        valueLookupBuilder(DRINKS).add(DRIFT_JELLY_BOTTLE);
        valueLookupBuilder(DRINK_CONTAINING_BOTTLE).add(DRIFT_JELLY_BOTTLE);
        valueLookupBuilder(EDIBLE_WHEN_PLACED_FOODS).add(CHORUS_CAKE_ROLL_ITEM);
        valueLookupBuilder(FOODS).add(DRIFT_JELLY_BOTTLE);
        valueLookupBuilder(GEMS).add(NEBULITE);
        valueLookupBuilder(INGOTS).add(SHADOLINE_INGOT);
        valueLookupBuilder(NUGGETS).add(SHADOLINE_NUGGET);
        valueLookupBuilder(MAGIC_DRINKS).add(DRIFT_JELLY_BOTTLE);
        valueLookupBuilder(MUSIC_DISCS).add(MUSIC_DISC_GLARE, MUSIC_DISC_DECAY, MUSIC_DISC_BLISS);
        valueLookupBuilder(MELEE_WEAPON_TOOLS).add(DAGGER);
        valueLookupBuilder(NEBULITE_GEMS).add(NEBULITE);
        valueLookupBuilder(ConventionalItemTags.ORES).forceAddTag(END_ORE_BLOCKS);
        valueLookupBuilder(NEBULITE_ORES).add(NEBULITE_ORE.asItem(), MIRESTONE_NEBULITE_ORE.asItem());
        valueLookupBuilder(NEBULITE_STORAGE_BLOCKS).add(NEBULITE_BLOCK.asItem());
        valueLookupBuilder(RAW_MATERIALS).add(RAW_SHADOLINE);
        valueLookupBuilder(RAW_SHADOLINE_STORAGE_BLOCKS).add(RAW_SHADOLINE_BLOCK.asItem());
        valueLookupBuilder(SHADOLINE_INGOTS).add(SHADOLINE_INGOT);
        valueLookupBuilder(SHADOLINE_NUGGETS).add(SHADOLINE_NUGGET);
        valueLookupBuilder(SHADOLINE_ORES).add(SHADOLINE_ORE.asItem(), MIRESTONE_SHADOLINE_ORE.asItem());
        valueLookupBuilder(SHADOLINE_RAW_MATERIALS).add(RAW_SHADOLINE);
        valueLookupBuilder(SHADOLINE_STORAGE_BLOCKS).add(SHADOLINE_BLOCK.asItem());
        valueLookupBuilder(SHIELD_TOOLS).add(RUBBLE_SHIELD);
        valueLookupBuilder(STORAGE_BLOCKS).forceAddTag(NEBULITE_STORAGE_BLOCKS).forceAddTag(RAW_SHADOLINE_STORAGE_BLOCKS).forceAddTag(SHADOLINE_STORAGE_BLOCKS);
        valueLookupBuilder(STRIPPED_LOGS).add(STRIPPED_VEILED_LOG.asItem(), STRIPPED_CELESTIAL_STEM.asItem(), STRIPPED_MURUBLIGHT_STEM.asItem());
        valueLookupBuilder(STRIPPED_WOODS).add(STRIPPED_VEILED_WOOD.asItem(), STRIPPED_CELESTIAL_HYPHAE.asItem(), STRIPPED_MURUBLIGHT_HYPHAE.asItem());
        valueLookupBuilder(TOOLS).add(DAGGER, MIRROR, MAGNIA_ATTRACTOR, RUBBLE_SHIELD);

        valueLookupBuilder(externalKey("create", "upright_on_belt")).add(DRIFT_JELLY_BOTTLE);
        valueLookupBuilder(externalKey("create", "deployable_drink")).add(DRIFT_JELLY_BOTTLE);
    }

    private TagKey<Item> externalKey(String namespace, String path) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(namespace, path));
    }
}
