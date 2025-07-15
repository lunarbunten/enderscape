package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;
import static net.bunten.enderscape.registry.EnderscapeItems.*;
import static net.bunten.enderscape.registry.tag.EnderscapeItemTags.*;
import static net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.*;
import static net.minecraft.tags.ItemTags.FENCE_GATES;
import static net.minecraft.tags.ItemTags.SMALL_FLOWERS;
import static net.minecraft.tags.ItemTags.WOODEN_FENCES;
import static net.minecraft.tags.ItemTags.*;
import static net.minecraft.world.item.Items.*;

public class EnderscapeItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public EnderscapeItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, null);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        valueLookupBuilder(WEAK_MAGNETISM_WHEN_WORN).add(CHAINMAIL_HELMET, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_BOOTS, NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS);
        valueLookupBuilder(AVERAGE_MAGNETISM_WHEN_WORN).add(IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS);
        valueLookupBuilder(STRONG_MAGNETISM_WHEN_WORN);
        valueLookupBuilder(POWERS_MAGNIA_WHEN_MINED_WITH);

        valueLookupBuilder(DRIFTER_FOOD).add(CHORUS_FRUIT, FLANGER_BERRY);
        valueLookupBuilder(ELYTRA_ENCHANTABLE).add(ELYTRA);
        valueLookupBuilder(MAGNIA_ATTRACTOR_ENCHANTABLE).add(MAGNIA_ATTRACTOR);
        valueLookupBuilder(MIRROR_ENCHANTABLE).add(MIRROR);
        valueLookupBuilder(NEBULITE_TOOLS).add(MIRROR, MAGNIA_ATTRACTOR);
        valueLookupBuilder(REPAIRS_DRIFT_LEGGINGS).add(DRIFT_JELLY_BOTTLE);
        valueLookupBuilder(REPAIRS_RUBBLE_SHIELDS).add(RUBBLE_CHITIN);
        valueLookupBuilder(RUBBLE_SHIELDS).add(END_STONE_RUBBLE_SHIELD, MIRESTONE_RUBBLE_SHIELD, VERADITE_RUBBLE_SHIELD, KURODITE_RUBBLE_SHIELD);
        valueLookupBuilder(RUSTLE_FOOD).add(MURUBLIGHT_SHELF_ITEM);

        valueLookupBuilder(CELESTIAL_BRICK_BLOCKS).add(CELESTIAL_BRICKS.asItem(), CELESTIAL_BRICK_SLAB.asItem(), CELESTIAL_BRICK_STAIRS.asItem(), CELESTIAL_BRICK_WALL.asItem());
        valueLookupBuilder(CELESTIAL_STEMS).add(CELESTIAL_STEM.asItem().asItem(), STRIPPED_CELESTIAL_STEM.asItem(), CELESTIAL_HYPHAE.asItem(), STRIPPED_CELESTIAL_HYPHAE.asItem());
        valueLookupBuilder(CELESTIAL_WOOD).forceAddTag(CELESTIAL_STEMS).add(CELESTIAL_PLANKS.asItem(), CELESTIAL_SLAB.asItem(), CELESTIAL_STAIRS.asItem(), CELESTIAL_FENCE.asItem(), CELESTIAL_FENCE_GATE.asItem(), CELESTIAL_BUTTON.asItem(), CELESTIAL_SIGN.asItem(), CELESTIAL_HANGING_SIGN.asItem(), CELESTIAL_WALL_SIGN.asItem(), CELESTIAL_WALL_HANGING_SIGN.asItem());
        valueLookupBuilder(ETCHED_MAGNIA_BLOCKS).add(ETCHED_ALLURING_MAGNIA.asItem(), ETCHED_REPULSIVE_MAGNIA.asItem());
        valueLookupBuilder(MAGNIA_BLOCKS).add(ALLURING_MAGNIA.asItem(), REPULSIVE_MAGNIA.asItem());
        valueLookupBuilder(MAGNIA_SPROUTS).add(ALLURING_MAGNIA_SPROUT.asItem(), REPULSIVE_MAGNIA_SPROUT.asItem());
        valueLookupBuilder(MURUBLIGHT_BRICK_BLOCKS).add(MURUBLIGHT_BRICKS.asItem(), MURUBLIGHT_BRICK_SLAB.asItem(), MURUBLIGHT_BRICK_STAIRS.asItem(), MURUBLIGHT_BRICK_WALL.asItem());
        valueLookupBuilder(MURUBLIGHT_STEMS).add(MURUBLIGHT_STEM.asItem(), STRIPPED_MURUBLIGHT_STEM.asItem(), MURUBLIGHT_HYPHAE.asItem(), STRIPPED_MURUBLIGHT_HYPHAE.asItem());
        valueLookupBuilder(MURUBLIGHT_WOOD).forceAddTag(MURUBLIGHT_STEMS).add(MURUBLIGHT_PLANKS.asItem(), MURUBLIGHT_SLAB.asItem(), MURUBLIGHT_STAIRS.asItem(), MURUBLIGHT_FENCE.asItem(), MURUBLIGHT_FENCE_GATE.asItem(), MURUBLIGHT_BUTTON.asItem(), MURUBLIGHT_SIGN.asItem(), MURUBLIGHT_HANGING_SIGN.asItem(), MURUBLIGHT_WALL_SIGN.asItem(), MURUBLIGHT_WALL_HANGING_SIGN.asItem());
        valueLookupBuilder(CHANTERELLE_BRICK_BLOCKS).forceAddTag(CELESTIAL_BRICK_BLOCKS).forceAddTag(MURUBLIGHT_BRICK_BLOCKS);
        valueLookupBuilder(CHANTERELLE_CAP_BLOCKS).add(CELESTIAL_CAP.asItem(), MURUBLIGHT_CAP.asItem());
        valueLookupBuilder(SHADOLINE_BLOCKS).add(CHISELED_SHADOLINE.asItem(), SHADOLINE_PILLAR.asItem());
        valueLookupBuilder(SHADOLINE_BLOCKS).add(SHADOLINE_BLOCK.asItem(), SHADOLINE_BLOCK_SLAB.asItem(), SHADOLINE_BLOCK_STAIRS.asItem(), SHADOLINE_BLOCK_WALL.asItem());
        valueLookupBuilder(SHADOLINE_BLOCKS).add(CUT_SHADOLINE.asItem(), CUT_SHADOLINE_SLAB.asItem(), CUT_SHADOLINE_STAIRS.asItem(), CUT_SHADOLINE_WALL.asItem());
        valueLookupBuilder(VEILED_LOGS).add(VEILED_LOG.asItem().asItem(), STRIPPED_VEILED_LOG.asItem(), VEILED_WOOD.asItem(), STRIPPED_VEILED_WOOD.asItem());
        valueLookupBuilder(VEILED_WOOD_TAG).forceAddTag(VEILED_LOGS).add(VEILED_PLANKS.asItem(), VEILED_SLAB.asItem(), VEILED_STAIRS.asItem(), VEILED_FENCE.asItem(), VEILED_FENCE_GATE.asItem(), VEILED_BUTTON.asItem(), VEILED_SIGN.asItem(), VEILED_HANGING_SIGN.asItem(), VEILED_WALL_SIGN.asItem(), VEILED_WALL_HANGING_SIGN.asItem());
        valueLookupBuilder(VERADITE_BLOCKS).add(CHISELED_VERADITE.asItem(), POLISHED_VERADITE_BUTTON.asItem(), POLISHED_VERADITE_PRESSURE_PLATE.asItem());
        valueLookupBuilder(VERADITE_BLOCKS).add(POLISHED_VERADITE.asItem(), POLISHED_VERADITE_SLAB.asItem(), POLISHED_VERADITE_STAIRS.asItem(), POLISHED_VERADITE_WALL.asItem());
        valueLookupBuilder(VERADITE_BLOCKS).add(VERADITE.asItem(), VERADITE_SLAB.asItem(), VERADITE_STAIRS.asItem(), VERADITE_WALL.asItem());
        valueLookupBuilder(VERADITE_BLOCKS).add(VERADITE_BRICKS.asItem(), VERADITE_BRICK_SLAB.asItem(), VERADITE_BRICK_STAIRS.asItem(), VERADITE_BRICK_WALL.asItem());
        valueLookupBuilder(KURODITE_BLOCKS).add(CHISELED_KURODITE.asItem(), POLISHED_KURODITE_BUTTON.asItem(), POLISHED_KURODITE_PRESSURE_PLATE.asItem());
        valueLookupBuilder(KURODITE_BLOCKS).add(POLISHED_KURODITE.asItem(), POLISHED_KURODITE_SLAB.asItem(), POLISHED_KURODITE_STAIRS.asItem(), POLISHED_KURODITE_WALL.asItem());
        valueLookupBuilder(KURODITE_BLOCKS).add(KURODITE.asItem(), KURODITE_SLAB.asItem(), KURODITE_STAIRS.asItem(), KURODITE_WALL.asItem());
        valueLookupBuilder(KURODITE_BLOCKS).add(KURODITE_BRICKS.asItem(), KURODITE_BRICK_SLAB.asItem(), KURODITE_BRICK_STAIRS.asItem(), KURODITE_BRICK_WALL.asItem());

        valueLookupBuilder(BEACON_PAYMENT_ITEMS).add(NEBULITE);
        valueLookupBuilder(DURABILITY_ENCHANTABLE).add(END_STONE_RUBBLE_SHIELD, MIRESTONE_RUBBLE_SHIELD, VERADITE_RUBBLE_SHIELD, KURODITE_RUBBLE_SHIELD);
        valueLookupBuilder(EQUIPPABLE_ENCHANTABLE).add(SHULKER_SHELL);
        valueLookupBuilder(FENCE_GATES).add(CELESTIAL_FENCE_GATE.asItem(), MURUBLIGHT_FENCE_GATE.asItem());
        valueLookupBuilder(HANGING_SIGNS).add(VEILED_HANGING_SIGN_ITEM.asItem(), CELESTIAL_HANGING_SIGN_ITEM, MURUBLIGHT_HANGING_SIGN_ITEM);
        valueLookupBuilder(LEG_ARMOR).add(DRIFT_LEGGINGS);
        valueLookupBuilder(LOGS_THAT_BURN).forceAddTag(VEILED_LOGS).forceAddTag(CELESTIAL_STEMS).forceAddTag(MURUBLIGHT_STEMS);
        valueLookupBuilder(PLANKS).add(VEILED_PLANKS.asItem(), CELESTIAL_PLANKS.asItem(), MURUBLIGHT_PLANKS.asItem());
        valueLookupBuilder(SIGNS).add(VEILED_SIGN_ITEM.asItem(), CELESTIAL_SIGN_ITEM, MURUBLIGHT_SIGN_ITEM);
        valueLookupBuilder(SLABS).add(VEILED_SLAB.asItem(), CELESTIAL_SLAB.asItem(), CELESTIAL_BRICK_SLAB.asItem(), MURUBLIGHT_SLAB.asItem(), MURUBLIGHT_BRICK_SLAB.asItem(), VERADITE_SLAB.asItem(), POLISHED_VERADITE_SLAB.asItem(), VERADITE_BRICK_SLAB.asItem(), KURODITE_SLAB.asItem(), POLISHED_KURODITE_SLAB.asItem(), KURODITE_BRICK_SLAB.asItem(), SHADOLINE_BLOCK_SLAB.asItem(), CUT_SHADOLINE_SLAB.asItem());
        valueLookupBuilder(SMALL_FLOWERS).add(BULB_FLOWER.asItem());
        valueLookupBuilder(STAIRS).add(VEILED_STAIRS.asItem(), CELESTIAL_STAIRS.asItem(), CELESTIAL_BRICK_STAIRS.asItem(), MURUBLIGHT_STAIRS.asItem(), MURUBLIGHT_BRICK_STAIRS.asItem(), VERADITE_STAIRS.asItem(), POLISHED_VERADITE_STAIRS.asItem(), VERADITE_BRICK_STAIRS.asItem(), KURODITE_STAIRS.asItem(), POLISHED_KURODITE_STAIRS.asItem(), KURODITE_BRICK_STAIRS.asItem(), SHADOLINE_BLOCK_STAIRS.asItem(), CUT_SHADOLINE_STAIRS.asItem());
        valueLookupBuilder(STONE_BUTTONS).add(POLISHED_VERADITE_BUTTON.asItem(), POLISHED_KURODITE_BUTTON.asItem());
        valueLookupBuilder(STONE_CRAFTING_MATERIALS).add(END_STONE.asItem(), VERADITE.asItem(), MIRESTONE.asItem(), KURODITE.asItem());
        valueLookupBuilder(STONE_TOOL_MATERIALS).add(END_STONE.asItem(), VERADITE.asItem(), MIRESTONE.asItem(), KURODITE.asItem());
        valueLookupBuilder(TRIMMABLE_ARMOR).add(DRIFT_LEGGINGS);
        valueLookupBuilder(TRIM_MATERIALS).add(NEBULITE, SHADOLINE_INGOT);
        valueLookupBuilder(VANISHING_ENCHANTABLE).add(MIRROR, SHULKER_SHELL);
        valueLookupBuilder(WALLS).add(VERADITE_WALL.asItem(), POLISHED_VERADITE_WALL.asItem(), VERADITE_BRICK_WALL.asItem(), SHADOLINE_BLOCK_WALL.asItem(), KURODITE_WALL.asItem(), POLISHED_KURODITE_WALL.asItem(), KURODITE_BRICK_WALL.asItem(), CUT_SHADOLINE_WALL.asItem(), CELESTIAL_BRICK_WALL.asItem(), MURUBLIGHT_BRICK_WALL.asItem());
        valueLookupBuilder(WOODEN_BUTTONS).add(VEILED_BUTTON.asItem(), CELESTIAL_BUTTON.asItem(), MURUBLIGHT_BUTTON.asItem());
        valueLookupBuilder(WOODEN_DOORS).add(VEILED_DOOR.asItem(), CELESTIAL_DOOR.asItem(), MURUBLIGHT_DOOR.asItem());
        valueLookupBuilder(WOODEN_FENCES).add(VEILED_FENCE.asItem(), CELESTIAL_FENCE.asItem(), MURUBLIGHT_FENCE.asItem());
        valueLookupBuilder(WOODEN_PRESSURE_PLATES).add(VEILED_PRESSURE_PLATE.asItem(), CELESTIAL_PRESSURE_PLATE.asItem(), MURUBLIGHT_PRESSURE_PLATE.asItem());
        valueLookupBuilder(WOODEN_SLABS).add(VEILED_SLAB.asItem(), CELESTIAL_SLAB.asItem(), MURUBLIGHT_SLAB.asItem());
        valueLookupBuilder(WOODEN_STAIRS).add(VEILED_STAIRS.asItem(), CELESTIAL_STAIRS.asItem(), MURUBLIGHT_STAIRS.asItem());
        valueLookupBuilder(WOODEN_TRAPDOORS).add(VEILED_TRAPDOOR.asItem(), CELESTIAL_TRAPDOOR.asItem(), MURUBLIGHT_TRAPDOOR.asItem());

        valueLookupBuilder(BUCKETS).add(RUSTLE_BUCKET);
        valueLookupBuilder(EDIBLE_WHEN_PLACED_FOODS).add(CHORUS_CAKE_ROLL_ITEM);
        valueLookupBuilder(FOODS).add(DRIFT_JELLY_BOTTLE);
        valueLookupBuilder(FRUIT_FOODS).add(FLANGER_BERRY);
        valueLookupBuilder(GEMS).add(NEBULITE);
        valueLookupBuilder(INGOTS).add(SHADOLINE_INGOT);
        valueLookupBuilder(MUSIC_DISCS).add(MUSIC_DISC_GLARE, MUSIC_DISC_DECAY, MUSIC_DISC_BLISS);
        valueLookupBuilder(SHIELD_TOOLS).add(END_STONE_RUBBLE_SHIELD, VERADITE_RUBBLE_SHIELD, MIRESTONE_RUBBLE_SHIELD, KURODITE_RUBBLE_SHIELD);
        valueLookupBuilder(STORAGE_BLOCKS).add(SHADOLINE_BLOCK.asItem(), NEBULITE_BLOCK.asItem(), DRIFT_JELLY_BLOCK.asItem());
        valueLookupBuilder(TOOLS).add(MIRROR, MAGNIA_ATTRACTOR);

        valueLookupBuilder(HAS_DESCRIPTION).add(MAGNIA_ATTRACTOR, DRIFT_LEGGINGS, DRIFT_JELLY_BOTTLE, DRIFT_JELLY_BLOCK.asItem(), NEBULITE).addTag(RUBBLE_SHIELDS);

        valueLookupBuilder(externalKey("item_tooltips", "has_description")).addTag(HAS_DESCRIPTION);

        valueLookupBuilder(externalKey("create", "upright_on_belt")).add(DRIFT_JELLY_BOTTLE);
        valueLookupBuilder(externalKey("create", "deployable_drink")).add(DRIFT_JELLY_BOTTLE);
    }

    private TagKey<Item> externalKey(String namespace, String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
}
