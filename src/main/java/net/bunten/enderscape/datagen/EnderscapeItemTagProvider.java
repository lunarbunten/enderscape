package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;
import static net.bunten.enderscape.registry.EnderscapeItems.*;
import static net.bunten.enderscape.registry.tag.EnderscapeItemTags.*;
import static net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.*;
import static net.minecraft.tags.ItemTags.*;
import static net.minecraft.world.item.Items.*;

public class EnderscapeItemTagProvider extends FabricTagProvider<Item> {

    public EnderscapeItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Registries.ITEM, future);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        getOrCreateTagBuilder(WEAK_MAGNETISM_WHEN_WORN).add(CHAINMAIL_HELMET, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_BOOTS, NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS);
        getOrCreateTagBuilder(AVERAGE_MAGNETISM_WHEN_WORN).add(IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS);
        getOrCreateTagBuilder(STRONG_MAGNETISM_WHEN_WORN);
        getOrCreateTagBuilder(POWERS_MAGNIA_WHEN_MINED_WITH);

        getOrCreateTagBuilder(DRIFTER_FOOD).add(CHORUS_FRUIT, FLANGER_BERRY);
        getOrCreateTagBuilder(ELYTRA_ENCHANTABLE).add(ELYTRA);
        getOrCreateTagBuilder(MAGNIA_ATTRACTOR_ENCHANTABLE).add(MAGNIA_ATTRACTOR);
        getOrCreateTagBuilder(MIRROR_ENCHANTABLE).add(MIRROR);
        getOrCreateTagBuilder(NEBULITE_TOOLS).add(MIRROR, MAGNIA_ATTRACTOR);
        getOrCreateTagBuilder(REPAIRS_DRIFT_LEGGINGS).add(DRIFT_JELLY_BOTTLE);
        getOrCreateTagBuilder(REPAIRS_RUBBLE_SHIELDS).add(RUBBLE_CHITIN);
        getOrCreateTagBuilder(RUBBLE_SHIELDS).add(END_STONE_RUBBLE_SHIELD, MIRESTONE_RUBBLE_SHIELD, VERADITE_RUBBLE_SHIELD, KURODITE_RUBBLE_SHIELD);
        getOrCreateTagBuilder(RUSTLE_FOOD).add(MURUBLIGHT_SHELF_ITEM);

        getOrCreateTagBuilder(CELESTIAL_BRICK_BLOCKS).add(CELESTIAL_BRICKS.asItem(), CELESTIAL_BRICK_SLAB.asItem(), CELESTIAL_BRICK_STAIRS.asItem(), CELESTIAL_BRICK_WALL.asItem());
        getOrCreateTagBuilder(CELESTIAL_STEMS).add(CELESTIAL_STEM.asItem().asItem(), STRIPPED_CELESTIAL_STEM.asItem(), CELESTIAL_HYPHAE.asItem(), STRIPPED_CELESTIAL_HYPHAE.asItem());
        getOrCreateTagBuilder(CELESTIAL_WOOD).forceAddTag(CELESTIAL_STEMS).add(CELESTIAL_PLANKS.asItem(), CELESTIAL_SLAB.asItem(), CELESTIAL_STAIRS.asItem(), CELESTIAL_FENCE.asItem(), CELESTIAL_FENCE_GATE.asItem(), CELESTIAL_BUTTON.asItem(), CELESTIAL_SIGN.asItem(), CELESTIAL_HANGING_SIGN.asItem(), CELESTIAL_WALL_SIGN.asItem(), CELESTIAL_WALL_HANGING_SIGN.asItem());
        getOrCreateTagBuilder(ETCHED_MAGNIA_BLOCKS).add(ETCHED_ALLURING_MAGNIA.asItem(), ETCHED_REPULSIVE_MAGNIA.asItem());
        getOrCreateTagBuilder(MAGNIA_BLOCKS).add(ALLURING_MAGNIA.asItem(), REPULSIVE_MAGNIA.asItem());
        getOrCreateTagBuilder(MAGNIA_SPROUTS).add(ALLURING_MAGNIA_SPROUT.asItem(), REPULSIVE_MAGNIA_SPROUT.asItem());
        getOrCreateTagBuilder(MURUBLIGHT_BRICK_BLOCKS).add(MURUBLIGHT_BRICKS.asItem(), MURUBLIGHT_BRICK_SLAB.asItem(), MURUBLIGHT_BRICK_STAIRS.asItem(), MURUBLIGHT_BRICK_WALL.asItem());
        getOrCreateTagBuilder(MURUBLIGHT_STEMS).add(MURUBLIGHT_STEM.asItem(), STRIPPED_MURUBLIGHT_STEM.asItem(), MURUBLIGHT_HYPHAE.asItem(), STRIPPED_MURUBLIGHT_HYPHAE.asItem());
        getOrCreateTagBuilder(MURUBLIGHT_WOOD).forceAddTag(MURUBLIGHT_STEMS).add(MURUBLIGHT_PLANKS.asItem(), MURUBLIGHT_SLAB.asItem(), MURUBLIGHT_STAIRS.asItem(), MURUBLIGHT_FENCE.asItem(), MURUBLIGHT_FENCE_GATE.asItem(), MURUBLIGHT_BUTTON.asItem(), MURUBLIGHT_SIGN.asItem(), MURUBLIGHT_HANGING_SIGN.asItem(), MURUBLIGHT_WALL_SIGN.asItem(), MURUBLIGHT_WALL_HANGING_SIGN.asItem());
        getOrCreateTagBuilder(CHANTERELLE_BRICK_BLOCKS).forceAddTag(CELESTIAL_BRICK_BLOCKS).forceAddTag(MURUBLIGHT_BRICK_BLOCKS);
        getOrCreateTagBuilder(CHANTERELLE_CAP_BLOCKS).add(CELESTIAL_CAP.asItem(), MURUBLIGHT_CAP.asItem());
        getOrCreateTagBuilder(SHADOLINE_BLOCKS).add(CHISELED_SHADOLINE.asItem(), SHADOLINE_PILLAR.asItem());
        getOrCreateTagBuilder(SHADOLINE_BLOCKS).add(SHADOLINE_BLOCK.asItem(), SHADOLINE_BLOCK_SLAB.asItem(), SHADOLINE_BLOCK_STAIRS.asItem(), SHADOLINE_BLOCK_WALL.asItem());
        getOrCreateTagBuilder(SHADOLINE_BLOCKS).add(CUT_SHADOLINE.asItem(), CUT_SHADOLINE_SLAB.asItem(), CUT_SHADOLINE_STAIRS.asItem(), CUT_SHADOLINE_WALL.asItem());
        getOrCreateTagBuilder(VEILED_LOGS).add(VEILED_LOG.asItem().asItem(), STRIPPED_VEILED_LOG.asItem(), VEILED_WOOD.asItem(), STRIPPED_VEILED_WOOD.asItem());
        getOrCreateTagBuilder(VEILED_WOOD_TAG).forceAddTag(VEILED_LOGS).add(VEILED_PLANKS.asItem(), VEILED_SLAB.asItem(), VEILED_STAIRS.asItem(), VEILED_FENCE.asItem(), VEILED_FENCE_GATE.asItem(), VEILED_BUTTON.asItem(), VEILED_SIGN.asItem(), VEILED_HANGING_SIGN.asItem(), VEILED_WALL_SIGN.asItem(), VEILED_WALL_HANGING_SIGN.asItem());
        getOrCreateTagBuilder(VERADITE_BLOCKS).add(CHISELED_VERADITE.asItem(), POLISHED_VERADITE_BUTTON.asItem(), POLISHED_VERADITE_PRESSURE_PLATE.asItem());
        getOrCreateTagBuilder(VERADITE_BLOCKS).add(POLISHED_VERADITE.asItem(), POLISHED_VERADITE_SLAB.asItem(), POLISHED_VERADITE_STAIRS.asItem(), POLISHED_VERADITE_WALL.asItem());
        getOrCreateTagBuilder(VERADITE_BLOCKS).add(VERADITE.asItem(), VERADITE_SLAB.asItem(), VERADITE_STAIRS.asItem(), VERADITE_WALL.asItem());
        getOrCreateTagBuilder(VERADITE_BLOCKS).add(VERADITE_BRICKS.asItem(), VERADITE_BRICK_SLAB.asItem(), VERADITE_BRICK_STAIRS.asItem(), VERADITE_BRICK_WALL.asItem());
        getOrCreateTagBuilder(KURODITE_BLOCKS).add(CHISELED_KURODITE.asItem(), POLISHED_KURODITE_BUTTON.asItem(), POLISHED_KURODITE_PRESSURE_PLATE.asItem());
        getOrCreateTagBuilder(KURODITE_BLOCKS).add(POLISHED_KURODITE.asItem(), POLISHED_KURODITE_SLAB.asItem(), POLISHED_KURODITE_STAIRS.asItem(), POLISHED_KURODITE_WALL.asItem());
        getOrCreateTagBuilder(KURODITE_BLOCKS).add(KURODITE.asItem(), KURODITE_SLAB.asItem(), KURODITE_STAIRS.asItem(), KURODITE_WALL.asItem());
        getOrCreateTagBuilder(KURODITE_BLOCKS).add(KURODITE_BRICKS.asItem(), KURODITE_BRICK_SLAB.asItem(), KURODITE_BRICK_STAIRS.asItem(), KURODITE_BRICK_WALL.asItem());

        getOrCreateTagBuilder(BEACON_PAYMENT_ITEMS).add(NEBULITE);
        getOrCreateTagBuilder(DURABILITY_ENCHANTABLE).add(END_STONE_RUBBLE_SHIELD, MIRESTONE_RUBBLE_SHIELD, VERADITE_RUBBLE_SHIELD, KURODITE_RUBBLE_SHIELD);
        getOrCreateTagBuilder(EQUIPPABLE_ENCHANTABLE).add(SHULKER_SHELL);
        getOrCreateTagBuilder(ItemTags.FENCE_GATES).add(CELESTIAL_FENCE_GATE.asItem(), MURUBLIGHT_FENCE_GATE.asItem());
        getOrCreateTagBuilder(HANGING_SIGNS).add(VEILED_HANGING_SIGN_ITEM.asItem(), CELESTIAL_HANGING_SIGN_ITEM, MURUBLIGHT_HANGING_SIGN_ITEM);
        getOrCreateTagBuilder(LEG_ARMOR).add(DRIFT_LEGGINGS);
        getOrCreateTagBuilder(SAPLINGS).add(VEILED_SAPLING.asItem());
        getOrCreateTagBuilder(LEAVES).add(VEILED_LEAVES.asItem());
        getOrCreateTagBuilder(LOGS_THAT_BURN).forceAddTag(VEILED_LOGS).forceAddTag(CELESTIAL_STEMS).forceAddTag(MURUBLIGHT_STEMS);
        getOrCreateTagBuilder(PLANKS).add(VEILED_PLANKS.asItem(), CELESTIAL_PLANKS.asItem(), MURUBLIGHT_PLANKS.asItem());
        getOrCreateTagBuilder(SIGNS).add(VEILED_SIGN_ITEM.asItem(), CELESTIAL_SIGN_ITEM, MURUBLIGHT_SIGN_ITEM);
        getOrCreateTagBuilder(SLABS).add(VEILED_SLAB.asItem(), CELESTIAL_SLAB.asItem(), CELESTIAL_BRICK_SLAB.asItem(), MURUBLIGHT_SLAB.asItem(), MURUBLIGHT_BRICK_SLAB.asItem(), VERADITE_SLAB.asItem(), POLISHED_VERADITE_SLAB.asItem(), VERADITE_BRICK_SLAB.asItem(), KURODITE_SLAB.asItem(), POLISHED_KURODITE_SLAB.asItem(), KURODITE_BRICK_SLAB.asItem(), SHADOLINE_BLOCK_SLAB.asItem(), CUT_SHADOLINE_SLAB.asItem());
        getOrCreateTagBuilder(SMALL_FLOWERS).add(BULB_FLOWER.asItem());
        getOrCreateTagBuilder(TALL_FLOWERS).add(WISP_FLOWER.asItem());
        getOrCreateTagBuilder(FLOWERS).add(FLANGER_BERRY_FLOWER.asItem(), WISP_FLOWER.asItem());
        getOrCreateTagBuilder(STAIRS).add(VEILED_STAIRS.asItem(), CELESTIAL_STAIRS.asItem(), CELESTIAL_BRICK_STAIRS.asItem(), MURUBLIGHT_STAIRS.asItem(), MURUBLIGHT_BRICK_STAIRS.asItem(), VERADITE_STAIRS.asItem(), POLISHED_VERADITE_STAIRS.asItem(), VERADITE_BRICK_STAIRS.asItem(), KURODITE_STAIRS.asItem(), POLISHED_KURODITE_STAIRS.asItem(), KURODITE_BRICK_STAIRS.asItem(), SHADOLINE_BLOCK_STAIRS.asItem(), CUT_SHADOLINE_STAIRS.asItem());
        getOrCreateTagBuilder(STONE_BUTTONS).add(POLISHED_VERADITE_BUTTON.asItem(), POLISHED_KURODITE_BUTTON.asItem());
        getOrCreateTagBuilder(STONE_CRAFTING_MATERIALS).add(END_STONE.asItem(), VERADITE.asItem(), MIRESTONE.asItem(), KURODITE.asItem());
        getOrCreateTagBuilder(STONE_TOOL_MATERIALS).add(END_STONE.asItem(), VERADITE.asItem(), MIRESTONE.asItem(), KURODITE.asItem());
        getOrCreateTagBuilder(TRIMMABLE_ARMOR).add(DRIFT_LEGGINGS);
        getOrCreateTagBuilder(TRIM_MATERIALS).add(NEBULITE, SHADOLINE_INGOT);
        getOrCreateTagBuilder(VANISHING_ENCHANTABLE).add(MIRROR, SHULKER_SHELL);
        getOrCreateTagBuilder(WALLS).add(VERADITE_WALL.asItem(), POLISHED_VERADITE_WALL.asItem(), VERADITE_BRICK_WALL.asItem(), SHADOLINE_BLOCK_WALL.asItem(), KURODITE_WALL.asItem(), POLISHED_KURODITE_WALL.asItem(), KURODITE_BRICK_WALL.asItem(), CUT_SHADOLINE_WALL.asItem(), CELESTIAL_BRICK_WALL.asItem(), MURUBLIGHT_BRICK_WALL.asItem());
        getOrCreateTagBuilder(WOODEN_BUTTONS).add(VEILED_BUTTON.asItem(), CELESTIAL_BUTTON.asItem(), MURUBLIGHT_BUTTON.asItem());
        getOrCreateTagBuilder(WOODEN_DOORS).add(VEILED_DOOR.asItem(), CELESTIAL_DOOR.asItem(), MURUBLIGHT_DOOR.asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_FENCES).add(VEILED_FENCE.asItem(), CELESTIAL_FENCE.asItem(), MURUBLIGHT_FENCE.asItem());
        getOrCreateTagBuilder(WOODEN_PRESSURE_PLATES).add(VEILED_PRESSURE_PLATE.asItem(), CELESTIAL_PRESSURE_PLATE.asItem(), MURUBLIGHT_PRESSURE_PLATE.asItem());
        getOrCreateTagBuilder(WOODEN_SLABS).add(VEILED_SLAB.asItem(), CELESTIAL_SLAB.asItem(), MURUBLIGHT_SLAB.asItem());
        getOrCreateTagBuilder(WOODEN_STAIRS).add(VEILED_STAIRS.asItem(), CELESTIAL_STAIRS.asItem(), MURUBLIGHT_STAIRS.asItem());
        getOrCreateTagBuilder(WOODEN_TRAPDOORS).add(VEILED_TRAPDOOR.asItem(), CELESTIAL_TRAPDOOR.asItem(), MURUBLIGHT_TRAPDOOR.asItem());

        getOrCreateTagBuilder(BUCKETS).add(RUSTLE_BUCKET);
        getOrCreateTagBuilder(EDIBLE_WHEN_PLACED_FOODS).add(CHORUS_CAKE_ROLL_ITEM);
        getOrCreateTagBuilder(FOODS).add(DRIFT_JELLY_BOTTLE);
        getOrCreateTagBuilder(DRINKS).add(DRIFT_JELLY_BOTTLE);
        getOrCreateTagBuilder(DRINK_CONTAINING_BOTTLE).add(DRIFT_JELLY_BOTTLE);
        getOrCreateTagBuilder(MAGIC_DRINKS).add(DRIFT_JELLY_BOTTLE);
        getOrCreateTagBuilder(DRIFT_JELLY_DRINKS).add(DRIFT_JELLY_BOTTLE);
        getOrCreateTagBuilder(BERRY_FOODS).add(FLANGER_BERRY);
        getOrCreateTagBuilder(GEMS).add(NEBULITE);
        getOrCreateTagBuilder(INGOTS).add(SHADOLINE_INGOT);
        getOrCreateTagBuilder(RAW_MATERIALS).add(RAW_SHADOLINE);
        getOrCreateTagBuilder(MUSIC_DISCS).add(MUSIC_DISC_GLARE, MUSIC_DISC_DECAY, MUSIC_DISC_BLISS);
        getOrCreateTagBuilder(SHIELD_TOOLS).add(END_STONE_RUBBLE_SHIELD, VERADITE_RUBBLE_SHIELD, MIRESTONE_RUBBLE_SHIELD, KURODITE_RUBBLE_SHIELD);
        getOrCreateTagBuilder(STORAGE_BLOCKS).add(SHADOLINE_BLOCK.asItem(), NEBULITE_BLOCK.asItem(), DRIFT_JELLY_BLOCK.asItem());
        getOrCreateTagBuilder(TOOLS).add(MIRROR, MAGNIA_ATTRACTOR);

        getOrCreateTagBuilder(STORAGE_BLOCKS_NEBULITE).add(NEBULITE_BLOCK.asItem());
        getOrCreateTagBuilder(STORAGE_BLOCKS_SHADOLINE).add(SHADOLINE_BLOCK.asItem());
        getOrCreateTagBuilder(STORAGE_BLOCKS_RAW_SHADOLINE).add(RAW_SHADOLINE_BLOCK.asItem());
        getOrCreateTagBuilder(NEBULITE_ORES).add(NEBULITE_ORE.asItem(), MIRESTONE_NEBULITE_ORE.asItem());
        getOrCreateTagBuilder(SHADOLINE_ORES).add(SHADOLINE_ORE.asItem(), MIRESTONE_SHADOLINE_ORE.asItem());
        getOrCreateTagBuilder(GEMS_NEBULITE).add(NEBULITE);
        getOrCreateTagBuilder(INGOTS_SHADOLINE).add(SHADOLINE_INGOT);
        getOrCreateTagBuilder(RAW_ORE_SHADOLINE).add(RAW_SHADOLINE);

        getOrCreateTagBuilder(externalKey("create", "upright_on_belt")).add(DRIFT_JELLY_BOTTLE);
        getOrCreateTagBuilder(externalKey("create", "deployable_drink")).add(DRIFT_JELLY_BOTTLE);
    }

    private TagKey<Item> externalKey(String namespace, String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
}
