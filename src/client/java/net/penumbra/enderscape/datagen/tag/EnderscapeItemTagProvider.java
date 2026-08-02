package net.penumbra.enderscape.datagen.tag;

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

import static net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.*;
import static net.minecraft.references.BlockItemIds.*;
import static net.minecraft.references.ItemIds.*;
import static net.minecraft.tags.BlockItemTags.LANTERNS;
import static net.minecraft.tags.BlockItemTags.SLABS;
import static net.minecraft.tags.ItemTags.*;
import static net.penumbra.enderscape.references.EnderscapeBlockItemIds.*;
import static net.penumbra.enderscape.references.EnderscapeItemIds.*;
import static net.penumbra.enderscape.registry.tag.EnderscapeItemTags.*;

public class EnderscapeItemTagProvider extends FabricTagsProvider.ItemTagsProvider {

    public EnderscapeItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, null);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(WEAK_MAGNIA_STRENGTH).add(
                CHAINMAIL_BOOTS,
                CHAINMAIL_CHESTPLATE,
                CHAINMAIL_HELMET,
                CHAINMAIL_LEGGINGS,
                NETHERITE_BOOTS,
                NETHERITE_CHESTPLATE,
                NETHERITE_HELMET,
                NETHERITE_HORSE_ARMOR,
                NETHERITE_LEGGINGS,
                NETHERITE_NAUTILUS_ARMOR,
                ANCIENT_DEBRIS.item()
        );

        tag(AVERAGE_MAGNIA_STRENGTH).add(
                IRON_BOOTS,
                IRON_CHESTPLATE,
                IRON_HELMET,
                IRON_HORSE_ARMOR,
                IRON_LEGGINGS,
                IRON_NAUTILUS_ARMOR,
                RAW_IRON_BLOCK.item(),
                NETHERITE_BLOCK.item()
        );

        tag(STRONG_MAGNIA_STRENGTH).add(
                IRON_BLOCK.item()
        );

        tag(DAGGER_ENCHANTABLE).add(DAGGER);
        tag(CANNOT_COMBINE_IN_ANVIL).add(MIRROR);
        tag(DRIFTER_FOOD).add(CHORUS_FRUIT, PURUBERRY);
        tag(ELYTRA_ENCHANTABLE).add(ELYTRA);
        tag(END_HAVEN_CORE_FUELS).add(NEBULITE);
        tag(LANTERNS.item()).add(BULB_LANTERN.item(), VOID_LANTERN.item());
        tag(MAGNIA_ATTRACTOR_ENCHANTABLE).add(MAGNIA_ATTRACTOR);
        tag(MIRROR_ENCHANTABLE).add(MIRROR);
        tag(NEBULITE_TOOLS).add(DAGGER, MIRROR, MAGNIA_ATTRACTOR);
        tag(NEBULITE_TOOL_ENCHANTABLE).forceAddTag(NEBULITE_TOOLS);
        tag(NEBULITE_TOOL_FUELS).add(NEBULITE);
        tag(REPAIRS_DRIFT_LEGGINGS).add(DRIFT_JELLY_BOTTLE);
        tag(REPAIRS_RUBBLE_SHIELDS).add(RUBBLE_CHITIN);
        tag(REPAIRS_SHADOLINE_ARMOR).add(SHADOLINE_INGOT);
        tag(RUSTLE_FOOD).add(MURUBLIGHT_BRACKET);

        tag(VOID_IMMUNE).add(
                BLINKLAMP.item(),
                BLINKLIGHT,
                CORRUPT_GROWTH.item(),
                CORRUPT_OVERGROWTH.item(),
                CORRUPT_PATH.item(),
                DAGGER,
                DRAGON_EGG.item(),
                DRAGON_HEAD.item(),
                ENDER_CHEST.item(),
                ENDER_EYE,
                ENDER_PEARL,
                END_HAVEN_CORE.item(),
                ELYTRA,
                END_CRYSTAL,
                MAGNIA_ATTRACTOR,
                MIRROR,
                MURUBLIGHT_BRACKET,
                MURUBLIGHT_CAP.item(),
                MURUBLIGHT_CHANTERELLE.item(),
                MUSIC_DISC_DECAY,
                MUSIC_DISC_GLARE,
                NEBULITE,
                NEBULITE_BLOCK.item(),
                NEBULITE_SHARDS,
                RAW_SHADOLINE,
                RAW_SHADOLINE_BLOCK.item(),
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
                VOID_CAMPFIRE.item(),
                VOID_LANTERN.item(),
                VOID_SHALE.item(),
                VOID_TORCH
        ).forceAddTag(SHADOLINE_BLOCKS).forceAddTag(DUSK_PURPUR_BLOCKS).forceAddTag(KURODITE_BLOCKS).forceAddTag(MURUBLIGHT_BRICK_BLOCKS).forceAddTag(MURUBLIGHT_STEMS).forceAddTag(MURUBLIGHT_WOOD_ITEMS);

        tag(DUSK_PURPUR_BLOCKS).add(DUSK_PURPUR_BLOCK.item(), DUSK_PURPUR_PILLAR.item(), DUSK_PURPUR_STAIRS.item(), DUSK_PURPUR_SLAB.item(), DUSK_PURPUR_WALL.item(), CHISELED_DUSK_PURPUR.item());
        tag(END_ORE_BLOCKS).add(NEBULITE_ORE.item(), SHADOLINE_ORE.item(), MIRESTONE_NEBULITE_ORE.item(), MIRESTONE_SHADOLINE_ORE.item());
        tag(NEW_END_STONE_BLOCKS).add(
                CHISELED_END_STONE.item(),
                CRACKED_END_STONE_BRICKS.item(),
                END_STONE_SLAB.item(),
                END_STONE_STAIRS.item(),
                END_STONE_WALL.item(),
                OVERGROWN_END_STONE_BRICKS.item(),
                OVERGROWN_END_STONE_BRICK_SLAB.item(),
                OVERGROWN_END_STONE_BRICK_STAIRS.item(),
                OVERGROWN_END_STONE_BRICK_WALL.item(),
                POLISHED_END_STONE.item(),
                POLISHED_END_STONE_BUTTON.item(),
                POLISHED_END_STONE_PRESSURE_PLATE.item(),
                POLISHED_END_STONE_SLAB.item(),
                POLISHED_END_STONE_STAIRS.item(),
                POLISHED_END_STONE_WALL.item()
        );

        tag(NEW_PURPUR_BLOCKS).add(
                CHISELED_PURPUR.item(),
                PURPUR_WALL.item()
        );

        tag(OVERGROWTH_BLOCKS).add(
                CELESTIAL_OVERGROWTH.item(),
                CORRUPT_OVERGROWTH.item()
        );

        tag(PURPUR_TILE_BLOCKS).add(
                PURPUR_TILES.item(),
                PURPUR_TILE_STAIRS.item(),
                PURPUR_TILE_SLAB.item()
        );

        tag(CHANTERELLE_BRICK_BLOCKS).forceAddTag(CELESTIAL_BRICK_BLOCKS).forceAddTag(MURUBLIGHT_BRICK_BLOCKS);
        tag(CHANTERELLE_CAP_BLOCKS).add(CELESTIAL_CAP.item(), MURUBLIGHT_CAP.item());

        tag(CELESTIAL_BRICK_BLOCKS).add(CELESTIAL_BRICKS.item(), CELESTIAL_BRICK_SLAB.item(), CELESTIAL_BRICK_STAIRS.item(), CELESTIAL_BRICK_WALL.item());
        tag(CELESTIAL_STEMS).add(CELESTIAL_STEM.item(), STRIPPED_CELESTIAL_STEM.item(), CELESTIAL_HYPHAE.item(), STRIPPED_CELESTIAL_HYPHAE.item());
        tag(CELESTIAL_WOOD_ITEMS).forceAddTag(CELESTIAL_STEMS).add(
                CELESTIAL_BUTTON.item(),
                CELESTIAL_DOOR.item(),
                CELESTIAL_FENCE.item(),
                CELESTIAL_FENCE_GATE.item(),
                CELESTIAL_HANGING_SIGN.item(),
                CELESTIAL_PLANKS.item(),
                CELESTIAL_PRESSURE_PLATE.item(),
                CELESTIAL_SHELF.item(),
                CELESTIAL_SIGN.item(),
                CELESTIAL_SLAB.item(),
                CELESTIAL_STAIRS.item(),
                CELESTIAL_TRAPDOOR.item()
        );

        tag(VEILED_LOGS).add(VEILED_LOG.item(), STRIPPED_VEILED_LOG.item(), VEILED_WOOD.item(), STRIPPED_VEILED_WOOD.item());
        tag(VEILED_WOOD_ITEMS).forceAddTag(VEILED_LOGS).add(
                VEILED_BUTTON.item(),
                VEILED_DOOR.item(),
                VEILED_FENCE.item(),
                VEILED_FENCE_GATE.item(),
                VEILED_HANGING_SIGN.item(),
                VEILED_PLANKS.item(),
                VEILED_PRESSURE_PLATE.item(),
                VEILED_SHELF.item(),
                VEILED_SIGN.item(),
                VEILED_SLAB.item(),
                VEILED_STAIRS.item(),
                VEILED_TRAPDOOR.item()
        );

        tag(MURUBLIGHT_BRICK_BLOCKS).add(MURUBLIGHT_BRICKS.item(), MURUBLIGHT_BRICK_SLAB.item(), MURUBLIGHT_BRICK_STAIRS.item(), MURUBLIGHT_BRICK_WALL.item());
        tag(MURUBLIGHT_STEMS).add(MURUBLIGHT_STEM.item(), STRIPPED_MURUBLIGHT_STEM.item(), MURUBLIGHT_HYPHAE.item(), STRIPPED_MURUBLIGHT_HYPHAE.item());

        tag(MURUBLIGHT_WOOD_ITEMS).forceAddTag(MURUBLIGHT_STEMS).add(
                MURUBLIGHT_BUTTON.item(),
                MURUBLIGHT_DOOR.item(),
                MURUBLIGHT_FENCE.item(),
                MURUBLIGHT_FENCE_GATE.item(),
                MURUBLIGHT_HANGING_SIGN.item(),
                MURUBLIGHT_PLANKS.item(),
                MURUBLIGHT_PRESSURE_PLATE.item(),
                MURUBLIGHT_SHELF.item(),
                MURUBLIGHT_SIGN.item(),
                MURUBLIGHT_SLAB.item(),
                MURUBLIGHT_STAIRS.item(),
                MURUBLIGHT_TRAPDOOR.item()
        );

        tag(MIRESTONE_BLOCKS).add(
                CHISELED_MIRESTONE.item(),
                CRACKED_MIRESTONE_BRICKS.item(),
                MIRESTONE.item(),
                MIRESTONE_BRICKS.item(),
                MIRESTONE_BRICK_SLAB.item(),
                MIRESTONE_BRICK_STAIRS.item(),
                MIRESTONE_BRICK_WALL.item(),
                OVERGROWN_MIRESTONE_BRICKS.item(),
                OVERGROWN_MIRESTONE_BRICK_SLAB.item(),
                OVERGROWN_MIRESTONE_BRICK_STAIRS.item(),
                OVERGROWN_MIRESTONE_BRICK_WALL.item(),
                MIRESTONE_SLAB.item(),
                MIRESTONE_STAIRS.item(),
                MIRESTONE_WALL.item(),
                POLISHED_MIRESTONE.item(),
                POLISHED_MIRESTONE_BUTTON.item(),
                POLISHED_MIRESTONE_PRESSURE_PLATE.item(),
                POLISHED_MIRESTONE_SLAB.item(),
                POLISHED_MIRESTONE_STAIRS.item(),
                POLISHED_MIRESTONE_WALL.item()
        );

        tag(VERADITE_BLOCKS).add(
                CHISELED_VERADITE.item(),
                VERADITE.item(),
                VERADITE_BRICKS.item(),
                VERADITE_BRICK_SLAB.item(),
                VERADITE_BRICK_STAIRS.item(),
                VERADITE_BRICK_WALL.item(),
                VERADITE_SLAB.item(),
                VERADITE_STAIRS.item(),
                VERADITE_WALL.item(),
                POLISHED_VERADITE.item(),
                POLISHED_VERADITE_BUTTON.item(),
                POLISHED_VERADITE_PRESSURE_PLATE.item(),
                POLISHED_VERADITE_SLAB.item(),
                POLISHED_VERADITE_STAIRS.item(),
                POLISHED_VERADITE_WALL.item()
        );

        tag(KURODITE_BLOCKS).add(
                CHISELED_KURODITE.item(),
                KURODITE.item(),
                KURODITE_BRICKS.item(),
                KURODITE_BRICK_SLAB.item(),
                KURODITE_BRICK_STAIRS.item(),
                KURODITE_BRICK_WALL.item(),
                KURODITE_SLAB.item(),
                KURODITE_STAIRS.item(),
                KURODITE_WALL.item(),
                POLISHED_KURODITE.item(),
                POLISHED_KURODITE_BUTTON.item(),
                POLISHED_KURODITE_PRESSURE_PLATE.item(),
                POLISHED_KURODITE_SLAB.item(),
                POLISHED_KURODITE_STAIRS.item(),
                POLISHED_KURODITE_WALL.item()
        );

        tag(SHADOLINE_BLOCKS).add(
                CHISELED_SHADOLINE.item(),
                CUT_SHADOLINE.item(),
                CUT_SHADOLINE_SLAB.item(),
                CUT_SHADOLINE_STAIRS.item(),
                CUT_SHADOLINE_WALL.item(),
                SHADOLINE_BLOCK.item(),
                SHADOLINE_BLOCK_SLAB.item(),
                SHADOLINE_BLOCK_STAIRS.item(),
                SHADOLINE_BLOCK_WALL.item(),
                SHADOLINE_PILLAR.item(),
                SHADOLINE_BARS.item(),
                SHADOLINE_CHAIN.item()
        );

        tag(ALL_ETCHED_MAGNIA_BLOCKS).addTag(ETCHED_ALLURING_MAGNIA_BLOCKS).addTag(ETCHED_REPULSIVE_MAGNIA_BLOCKS);

        tag(ETCHED_ALLURING_MAGNIA_BLOCKS).add(
                ETCHED_ALLURING_MAGNIA.item(),
                ETCHED_ALLURING_MAGNIA_STAIRS.item(),
                ETCHED_ALLURING_MAGNIA_SLAB.item(),
                ETCHED_ALLURING_MAGNIA_WALL.item()
        );

        tag(ETCHED_REPULSIVE_MAGNIA_BLOCKS).add(
                ETCHED_REPULSIVE_MAGNIA.item(),
                ETCHED_REPULSIVE_MAGNIA_STAIRS.item(),
                ETCHED_REPULSIVE_MAGNIA_SLAB.item(),
                ETCHED_REPULSIVE_MAGNIA_WALL.item()
        );

        tag(MAGNIA_BLOCKS).add(ALLURING_MAGNIA.item(), REPULSIVE_MAGNIA.item());
        tag(MAGNIA_SPROUTS).add(ALLURING_MAGNIA_SPROUT.item(), REPULSIVE_MAGNIA_SPROUT.item());

        tag(BlockItemTags.BARS.item()).add(SHADOLINE_BARS.item());
        tag(BEACON_PAYMENT_ITEMS).add(NEBULITE);
        tag(BlockItemTags.CHAINS.item()).add(SHADOLINE_CHAIN.item());
        tag(CHEST_ARMOR).add(SHADOLINE_CHESTPLATE);
        tag(DURABILITY_ENCHANTABLE).add(RUBBLE_SHIELD);
        tag(EQUIPPABLE_ENCHANTABLE).add(SHULKER_SHELL);
        tag(FIRE_ASPECT_ENCHANTABLE).add(DAGGER);
        tag(FOOT_ARMOR).add(SHADOLINE_BOOTS);
        tag(HANGING_SIGNS).add(VEILED_HANGING_SIGN.item(), CELESTIAL_HANGING_SIGN.item(), MURUBLIGHT_HANGING_SIGN.item());
        tag(HEAD_ARMOR).add(SHADOLINE_HELMET);
        tag(ItemTags.FENCE_GATES).add(VEILED_FENCE_GATE.item(), CELESTIAL_FENCE_GATE.item(), MURUBLIGHT_FENCE_GATE.item());
        tag(BlockItemTags.FLOWERS.item()).add(WISP_FLOWER.item(), PURUBERRY_FLOWER.item());
        tag(BlockItemTags.SMALL_FLOWERS.item()).add(BULB_FLOWER.item());
        tag(ItemTags.WOODEN_FENCES).add(VEILED_FENCE.item(), CELESTIAL_FENCE.item(), MURUBLIGHT_FENCE.item());
        tag(LEAVES).add(VEILED_LEAVES.item());
        tag(LEG_ARMOR).add(SHADOLINE_LEGGINGS, DRIFT_LEGGINGS);
        tag(LOGS_THAT_BURN).forceAddTag(VEILED_LOGS).forceAddTag(CELESTIAL_STEMS).forceAddTag(MURUBLIGHT_STEMS);
        tag(LOOM_PATTERNS).add(CRESCENT_BANNER_PATTERN);
        tag(PLANKS).add(VEILED_PLANKS.item(), CELESTIAL_PLANKS.item(), MURUBLIGHT_PLANKS.item());
        tag(SAPLINGS).add(VEILED_SAPLING.item());
        tag(SHARP_WEAPON_ENCHANTABLE).add(DAGGER);
        tag(SIGNS).add(VEILED_SIGN.item(), CELESTIAL_SIGN.item(), MURUBLIGHT_SIGN.item());

        tag(SLABS.item()).add(
                CELESTIAL_BRICK_SLAB.item(),
                CELESTIAL_SLAB.item(),
                CUT_SHADOLINE_SLAB.item(),
                DUSK_PURPUR_SLAB.item(),
                END_STONE_SLAB.item(),
                ETCHED_ALLURING_MAGNIA_SLAB.item(),
                ETCHED_REPULSIVE_MAGNIA_SLAB.item(),
                KURODITE_BRICK_SLAB.item(),
                KURODITE_SLAB.item(),
                MIRESTONE_BRICK_SLAB.item(),
                MIRESTONE_SLAB.item(),
                MURUBLIGHT_BRICK_SLAB.item(),
                MURUBLIGHT_SLAB.item(),
                OVERGROWN_END_STONE_BRICK_SLAB.item(),
                OVERGROWN_MIRESTONE_BRICK_SLAB.item(),
                POLISHED_END_STONE_SLAB.item(),
                POLISHED_KURODITE_SLAB.item(),
                POLISHED_MIRESTONE_SLAB.item(),
                POLISHED_VERADITE_SLAB.item(),
                PURPUR_TILE_SLAB.item(),
                SHADOLINE_BLOCK_SLAB.item(),
                VEILED_SLAB.item(),
                VERADITE_BRICK_SLAB.item(),
                VERADITE_SLAB.item()
        );

        tag(BlockItemTags.STAIRS.item()).add(
                CELESTIAL_BRICK_STAIRS.item(),
                CELESTIAL_STAIRS.item(),
                CUT_SHADOLINE_STAIRS.item(),
                DUSK_PURPUR_STAIRS.item(),
                END_STONE_STAIRS.item(),
                ETCHED_ALLURING_MAGNIA_STAIRS.item(),
                ETCHED_REPULSIVE_MAGNIA_STAIRS.item(),
                KURODITE_BRICK_STAIRS.item(),
                KURODITE_STAIRS.item(),
                MIRESTONE_BRICK_STAIRS.item(),
                MIRESTONE_STAIRS.item(),
                MURUBLIGHT_BRICK_STAIRS.item(),
                MURUBLIGHT_STAIRS.item(),
                OVERGROWN_END_STONE_BRICK_STAIRS.item(),
                OVERGROWN_MIRESTONE_BRICK_STAIRS.item(),
                POLISHED_END_STONE_STAIRS.item(),
                POLISHED_KURODITE_STAIRS.item(),
                POLISHED_MIRESTONE_STAIRS.item(),
                POLISHED_VERADITE_STAIRS.item(),
                PURPUR_TILE_STAIRS.item(),
                SHADOLINE_BLOCK_STAIRS.item(),
                VEILED_STAIRS.item(),
                VERADITE_BRICK_STAIRS.item(),
                VERADITE_STAIRS.item()
        );

        tag(WALLS).add(
                CELESTIAL_BRICK_WALL.item(),
                CUT_SHADOLINE_WALL.item(),
                DUSK_PURPUR_WALL.item(),
                END_STONE_WALL.item(),
                ETCHED_ALLURING_MAGNIA_WALL.item(),
                ETCHED_REPULSIVE_MAGNIA_WALL.item(),
                KURODITE_BRICK_WALL.item(),
                KURODITE_WALL.item(),
                MIRESTONE_BRICK_WALL.item(),
                MIRESTONE_WALL.item(),
                MURUBLIGHT_BRICK_WALL.item(),
                OVERGROWN_END_STONE_BRICK_WALL.item(),
                OVERGROWN_MIRESTONE_BRICK_WALL.item(),
                POLISHED_END_STONE_WALL.item(),
                POLISHED_KURODITE_WALL.item(),
                POLISHED_MIRESTONE_WALL.item(),
                POLISHED_VERADITE_WALL.item(),
                PURPUR_WALL.item(),
                SHADOLINE_BLOCK_WALL.item(),
                VERADITE_BRICK_WALL.item(),
                VERADITE_WALL.item()
        );

        tag(BlockItemTags.STONE_BUTTONS.item()).add(POLISHED_END_STONE_BUTTON.item(), POLISHED_MIRESTONE_BUTTON.item(), POLISHED_VERADITE_BUTTON.item(), POLISHED_KURODITE_BUTTON.item());
        tag(STONE_CRAFTING_MATERIALS).add(END_STONE.item(), VERADITE.item(), MIRESTONE.item(), KURODITE.item());
        tag(STONE_TOOL_MATERIALS).add(END_STONE.item(), VERADITE.item(), MIRESTONE.item(), KURODITE.item());

        tag(SULFUR_CUBE_ARCHETYPE_REGULAR).forceAddTag(OVERGROWTH_BLOCKS);

        tag(SULFUR_CUBE_ARCHETYPE_SLOW_BOUNCY).add(
                ALLURING_MAGNIA.item(),
                BLINKLAMP.item(),
                BLISTERED_MAGNIA.item(),
                CELESTIAL_BRICKS.item(),
                CHISELED_DUSK_PURPUR.item(),
                CHISELED_END_STONE.item(),
                CHISELED_KURODITE.item(),
                CHISELED_MIRESTONE.item(),
                CHISELED_PURPUR.item(),
                CHISELED_VERADITE.item(),
                CRACKED_END_STONE_BRICKS.item(),
                CRACKED_MIRESTONE_BRICKS.item(),
                DUSK_PURPUR_BLOCK.item(),
                DUSK_PURPUR_PILLAR.item(),
                END_LAMP.item(),
                ETCHED_ALLURING_MAGNIA.item(),
                ETCHED_REPULSIVE_MAGNIA.item(),
                KURODITE.item(),
                KURODITE_BRICKS.item(),
                MIRESTONE.item(),
                MIRESTONE_BRICKS.item(),
                MIRESTONE_NEBULITE_ORE.item(),
                MURUBLIGHT_BRICKS.item(),
                NEBULITE_BLOCK.item(),
                NEBULITE_ORE.item(),
                OVERGROWN_END_STONE_BRICKS.item(),
                OVERGROWN_MIRESTONE_BRICKS.item(),
                POLARIZED_MAGNIA.item(),
                POLISHED_END_STONE.item(),
                POLISHED_KURODITE.item(),
                POLISHED_MIRESTONE.item(),
                POLISHED_VERADITE.item(),
                PURPUR_TILES.item(),
                REPULSIVE_MAGNIA.item(),
                VEILED_END_STONE.item(),
                VERADITE.item(),
                VERADITE_BRICKS.item(),
                VOID_SHALE.item()
        );

        tag(SULFUR_CUBE_ARCHETYPE_SLOW_FLAT).add(
                CHISELED_SHADOLINE.item(),
                CUT_SHADOLINE.item(),
                MIRESTONE_SHADOLINE_ORE.item(),
                RAW_SHADOLINE_BLOCK.item(),
                SHADOLINE_BLOCK.item(),
                SHADOLINE_ORE.item(),
                SHADOLINE_PILLAR.item()
        );

        tag(SULFUR_CUBE_ARCHETYPE_SLOW_SLIDING).forceAddTag(CHANTERELLE_CAP_BLOCKS);

        tag(MELEE_WEAPON_ENCHANTABLE).add(DAGGER);
        tag(METAL_NUGGETS).add(SHADOLINE_NUGGET);
        tag(TRIMMABLE_ARMOR).add(SHADOLINE_HELMET, SHADOLINE_CHESTPLATE, SHADOLINE_LEGGINGS, SHADOLINE_BOOTS, DRIFT_LEGGINGS);
        tag(TRIM_MATERIALS).add(NEBULITE, SHADOLINE_INGOT);
        tag(VANISHING_ENCHANTABLE).add(DAGGER, MAGNIA_ATTRACTOR, MIRROR, SHULKER_SHELL);
        tag(WOODEN_BUTTONS).add(VEILED_BUTTON.item(), CELESTIAL_BUTTON.item(), MURUBLIGHT_BUTTON.item());
        tag(WOODEN_DOORS).add(VEILED_DOOR.item(), CELESTIAL_DOOR.item(), MURUBLIGHT_DOOR.item());
        tag(WOODEN_PRESSURE_PLATES).add(VEILED_PRESSURE_PLATE.item(), CELESTIAL_PRESSURE_PLATE.item(), MURUBLIGHT_PRESSURE_PLATE.item());
        tag(ItemTags.WOODEN_SHELVES).add(VEILED_SHELF.item(), CELESTIAL_SHELF.item(), MURUBLIGHT_SHELF.item());
        tag(WOODEN_SLABS).add(VEILED_SLAB.item(), CELESTIAL_SLAB.item(), MURUBLIGHT_SLAB.item());
        tag(WOODEN_STAIRS).add(VEILED_STAIRS.item(), CELESTIAL_STAIRS.item(), MURUBLIGHT_STAIRS.item());
        tag(WOODEN_TRAPDOORS).add(VEILED_TRAPDOOR.item(), CELESTIAL_TRAPDOOR.item(), MURUBLIGHT_TRAPDOOR.item());

        // Item Descriptions

        tag(externalKey("item_tooltips", "has_description")).add(MAGNIA_ATTRACTOR, DRIFT_LEGGINGS, DRIFT_JELLY_BOTTLE, DRIFT_JELLY_BLOCK.item(), NEBULITE, RUBBLE_SHIELD);

        // Conventional Tags

        tag(ARMORS).add(SHADOLINE_HELMET, SHADOLINE_CHESTPLATE, SHADOLINE_LEGGINGS, SHADOLINE_BOOTS, DRIFT_LEGGINGS);
        tag(HUMANOID_ARMORS).add(SHADOLINE_HELMET, SHADOLINE_CHESTPLATE, SHADOLINE_LEGGINGS, SHADOLINE_BOOTS, DRIFT_LEGGINGS);
        tag(BERRY_FOODS).add(PURUBERRY);
        tag(BUCKETS).add(RUSTLE_BUCKET, VOID_LACHRYMA_BUCKET);
        tag(ConventionalItemTags.CHAINS).add(SHADOLINE_CHAIN.item());
        tag(DRIFT_JELLY_DRINKS).add(DRIFT_JELLY_BOTTLE);
        tag(DRINKS).add(DRIFT_JELLY_BOTTLE);
        tag(DRINK_CONTAINING_BOTTLE).add(DRIFT_JELLY_BOTTLE);
        tag(EDIBLE_WHEN_PLACED_FOODS).add(CHORUS_CAKE_ROLL);
        tag(FOODS).add(DRIFT_JELLY_BOTTLE);
        tag(GEMS).add(NEBULITE);
        tag(INGOTS).add(SHADOLINE_INGOT);
        tag(NUGGETS).add(SHADOLINE_NUGGET);
        tag(MAGIC_DRINKS).add(DRIFT_JELLY_BOTTLE);
        tag(MUSIC_DISCS).add(MUSIC_DISC_GLARE, MUSIC_DISC_DECAY, MUSIC_DISC_BLISS);
        tag(MELEE_WEAPON_TOOLS).add(DAGGER);
        tag(NEBULITE_GEMS).add(NEBULITE);
        tag(ConventionalItemTags.ORES).forceAddTag(END_ORE_BLOCKS);
        tag(NEBULITE_ORES).add(NEBULITE_ORE.item(), MIRESTONE_NEBULITE_ORE.item());
        tag(NEBULITE_STORAGE_BLOCKS).add(NEBULITE_BLOCK.item());
        tag(RAW_MATERIALS).add(RAW_SHADOLINE);
        tag(RAW_SHADOLINE_STORAGE_BLOCKS).add(RAW_SHADOLINE_BLOCK.item());
        tag(SHADOLINE_INGOTS).add(SHADOLINE_INGOT);
        tag(SHADOLINE_NUGGETS).add(SHADOLINE_NUGGET);
        tag(SHADOLINE_ORES).add(SHADOLINE_ORE.item(), MIRESTONE_SHADOLINE_ORE.item());
        tag(SHADOLINE_RAW_MATERIALS).add(RAW_SHADOLINE);
        tag(SHADOLINE_STORAGE_BLOCKS).add(SHADOLINE_BLOCK.item());
        tag(SHIELD_TOOLS).add(RUBBLE_SHIELD);
        tag(STORAGE_BLOCKS).forceAddTag(NEBULITE_STORAGE_BLOCKS).forceAddTag(RAW_SHADOLINE_STORAGE_BLOCKS).forceAddTag(SHADOLINE_STORAGE_BLOCKS);
        tag(STRIPPED_LOGS).add(STRIPPED_VEILED_LOG.item(), STRIPPED_CELESTIAL_STEM.item(), STRIPPED_MURUBLIGHT_STEM.item());
        tag(STRIPPED_WOODS).add(STRIPPED_VEILED_WOOD.item(), STRIPPED_CELESTIAL_HYPHAE.item(), STRIPPED_MURUBLIGHT_HYPHAE.item());
        tag(TOOLS).add(DAGGER, MIRROR, MAGNIA_ATTRACTOR, RUBBLE_SHIELD);

        tag(externalKey("create", "upright_on_belt")).add(DRIFT_JELLY_BOTTLE);
        tag(externalKey("create", "deployable_drink")).add(DRIFT_JELLY_BOTTLE);
    }

    private TagKey<Item> externalKey(String namespace, String path) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(namespace, path));
    }
}
