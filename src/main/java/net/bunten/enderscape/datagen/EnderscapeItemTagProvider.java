package net.bunten.enderscape.datagen;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;
import static net.bunten.enderscape.registry.EnderscapeItems.*;
import static net.bunten.enderscape.registry.tag.EnderscapeItemTags.*;
import static net.minecraft.tags.ItemTags.*;
import static net.minecraft.world.item.Items.*;
import static net.neoforged.neoforge.common.Tags.Items.*;

public class EnderscapeItemTagProvider extends ItemTagsProvider {

    public EnderscapeItemTagProvider(GatherDataEvent event, BlockTagsProvider blockTagProvider) {
        super(event.getGenerator().getPackOutput(), event.getLookupProvider(), blockTagProvider.contentsGetter(), Enderscape.MOD_ID, event.getExistingFileHelper());
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

        tag(DAGGER_ENCHANTABLE).add(DAGGER.get());
        tag(DRIFTER_FOOD).add(CHORUS_FRUIT, FLANGER_BERRY.get());
        tag(ELYTRA_ENCHANTABLE).add(ELYTRA);
        tag(MAGNIA_ATTRACTOR_ENCHANTABLE).add(MAGNIA_ATTRACTOR.get());
        tag(MIRROR_ENCHANTABLE).add(MIRROR.get());
        tag(NEBULITE_TOOLS).add(DAGGER.get(), MIRROR.get(), MAGNIA_ATTRACTOR.get());
        tag(NEBULITE_TOOL_ENCHANTABLE).addTag(NEBULITE_TOOLS);
        tag(NEBULITE_TOOL_FUELS).add(NEBULITE.get());
        tag(REPAIRS_DRIFT_LEGGINGS).add(DRIFT_JELLY_BOTTLE.get());
        tag(REPAIRS_RUBBLE_SHIELDS).add(RUBBLE_CHITIN.get());
        tag(REPAIRS_SHADOLINE_ARMOR).add(SHADOLINE_INGOT.get());
        tag(RUBBLE_SHIELDS).add(END_STONE_RUBBLE_SHIELD.get(), MIRESTONE_RUBBLE_SHIELD.get(), VERADITE_RUBBLE_SHIELD.get(), KURODITE_RUBBLE_SHIELD.get());
        tag(RUSTLE_FOOD).add(MURUBLIGHT_BRACKET.get().asItem());

        tag(DUSK_PURPUR_BLOCKS).add(DUSK_PURPUR_BLOCK.get().asItem(), DUSK_PURPUR_PILLAR.get().asItem(), DUSK_PURPUR_STAIRS.get().asItem(), DUSK_PURPUR_SLAB.get().asItem(), DUSK_PURPUR_WALL.get().asItem(), CHISELED_DUSK_PURPUR.get().asItem());
        tag(END_ORE_BLOCKS).add(NEBULITE_ORE.get().asItem(), SHADOLINE_ORE.get().asItem(), MIRESTONE_NEBULITE_ORE.get().asItem(), MIRESTONE_SHADOLINE_ORE.get().asItem());
        tag(NEW_END_STONE_BLOCKS).add(
                CHISELED_END_STONE.get().asItem(),
                END_STONE_SLAB.get().asItem(),
                END_STONE_STAIRS.get().asItem(),
                END_STONE_WALL.get().asItem(),
                POLISHED_END_STONE.get().asItem(),
                POLISHED_END_STONE_BUTTON.get().asItem(),
                POLISHED_END_STONE_PRESSURE_PLATE.get().asItem(),
                POLISHED_END_STONE_SLAB.get().asItem(),
                POLISHED_END_STONE_STAIRS.get().asItem(),
                POLISHED_END_STONE_WALL.get().asItem()
        );

        tag(NEW_PURPUR_BLOCKS).add(
                CHISELED_PURPUR.get().asItem(),
                PURPUR_WALL.get().asItem()
        );

        tag(OVERGROWTH_BLOCKS).add(
                CELESTIAL_OVERGROWTH.get().asItem(),
                CORRUPT_OVERGROWTH.get().asItem()
        );

        tag(PURPUR_TILE_BLOCKS).add(
                PURPUR_TILES.get().asItem(),
                PURPUR_TILE_STAIRS.get().asItem(),
                PURPUR_TILE_SLAB.get().asItem()
        );

        tag(CHANTERELLE_BRICK_BLOCKS).addTag(CELESTIAL_BRICK_BLOCKS).addTag(MURUBLIGHT_BRICK_BLOCKS);
        tag(CHANTERELLE_CAP_BLOCKS).add(CELESTIAL_CAP.get().asItem(), MURUBLIGHT_CAP.get().asItem());

        tag(CELESTIAL_BRICK_BLOCKS).add(CELESTIAL_BRICKS.get().asItem(), CELESTIAL_BRICK_SLAB.get().asItem(), CELESTIAL_BRICK_STAIRS.get().asItem(), CELESTIAL_BRICK_WALL.get().asItem());
        tag(CELESTIAL_STEMS).add(CELESTIAL_STEM.get().asItem(), STRIPPED_CELESTIAL_STEM.get().asItem(), CELESTIAL_HYPHAE.get().asItem(), STRIPPED_CELESTIAL_HYPHAE.get().asItem());
        tag(CELESTIAL_WOOD_ITEMS).addTag(CELESTIAL_STEMS).add(
                CELESTIAL_BUTTON.get().asItem(),
                CELESTIAL_DOOR.get().asItem(),
                CELESTIAL_FENCE.get().asItem(),
                CELESTIAL_FENCE_GATE.get().asItem(),
                CELESTIAL_HANGING_SIGN.get().asItem(),
                CELESTIAL_PLANKS.get().asItem(),
                CELESTIAL_SIGN.get().asItem(),
                CELESTIAL_SLAB.get().asItem(),
                CELESTIAL_STAIRS.get().asItem(),
                CELESTIAL_TRAPDOOR.get().asItem(),
                CELESTIAL_WALL_HANGING_SIGN.get().asItem(),
                CELESTIAL_WALL_SIGN.get().asItem()
        );

        tag(VEILED_LOGS).add(VEILED_LOG.get().asItem(), STRIPPED_VEILED_LOG.get().asItem(), VEILED_WOOD.get().asItem(), STRIPPED_VEILED_WOOD.get().asItem());
        tag(VEILED_WOOD_ITEMS).addTag(VEILED_LOGS).add(
                VEILED_BUTTON.get().asItem(),
                VEILED_DOOR.get().asItem(),
                VEILED_FENCE.get().asItem(),
                VEILED_FENCE_GATE.get().asItem(),
                VEILED_HANGING_SIGN.get().asItem(),
                VEILED_PLANKS.get().asItem(),
                VEILED_SIGN.get().asItem(),
                VEILED_SLAB.get().asItem(),
                VEILED_STAIRS.get().asItem(),
                VEILED_TRAPDOOR.get().asItem(),
                VEILED_WALL_HANGING_SIGN.get().asItem(),
                VEILED_WALL_SIGN.get().asItem()
        );

        tag(MURUBLIGHT_BRICK_BLOCKS).add(MURUBLIGHT_BRICKS.get().asItem(), MURUBLIGHT_BRICK_SLAB.get().asItem(), MURUBLIGHT_BRICK_STAIRS.get().asItem(), MURUBLIGHT_BRICK_WALL.get().asItem());
        tag(MURUBLIGHT_STEMS).add(MURUBLIGHT_STEM.get().asItem(), STRIPPED_MURUBLIGHT_STEM.get().asItem(), MURUBLIGHT_HYPHAE.get().asItem(), STRIPPED_MURUBLIGHT_HYPHAE.get().asItem());

        tag(MURUBLIGHT_WOOD_ITEMS).addTag(MURUBLIGHT_STEMS).add(
                MURUBLIGHT_BUTTON.get().asItem(),
                MURUBLIGHT_DOOR.get().asItem(),
                MURUBLIGHT_FENCE.get().asItem(),
                MURUBLIGHT_FENCE_GATE.get().asItem(),
                MURUBLIGHT_HANGING_SIGN.get().asItem(),
                MURUBLIGHT_PLANKS.get().asItem(),
                MURUBLIGHT_SIGN.get().asItem(),
                MURUBLIGHT_SLAB.get().asItem(),
                MURUBLIGHT_STAIRS.get().asItem(),
                MURUBLIGHT_TRAPDOOR.get().asItem(),
                MURUBLIGHT_WALL_HANGING_SIGN.get().asItem(),
                MURUBLIGHT_WALL_SIGN.get().asItem()
        );

        tag(MIRESTONE_BLOCKS).add(
                CHISELED_MIRESTONE.get().asItem(),
                MIRESTONE.get().asItem(),
                MIRESTONE_BRICKS.get().asItem(),
                MIRESTONE_BRICK_SLAB.get().asItem(),
                MIRESTONE_BRICK_STAIRS.get().asItem(),
                MIRESTONE_BRICK_WALL.get().asItem(),
                MIRESTONE_SLAB.get().asItem(),
                MIRESTONE_STAIRS.get().asItem(),
                MIRESTONE_WALL.get().asItem(),
                POLISHED_MIRESTONE.get().asItem(),
                POLISHED_MIRESTONE_BUTTON.get().asItem(),
                POLISHED_MIRESTONE_PRESSURE_PLATE.get().asItem(),
                POLISHED_MIRESTONE_SLAB.get().asItem(),
                POLISHED_MIRESTONE_STAIRS.get().asItem(),
                POLISHED_MIRESTONE_WALL.get().asItem()
        );

        tag(VERADITE_BLOCKS).add(
                CHISELED_VERADITE.get().asItem(),
                VERADITE.get().asItem(),
                VERADITE_BRICKS.get().asItem(),
                VERADITE_BRICK_SLAB.get().asItem(),
                VERADITE_BRICK_STAIRS.get().asItem(),
                VERADITE_BRICK_WALL.get().asItem(),
                VERADITE_SLAB.get().asItem(),
                VERADITE_STAIRS.get().asItem(),
                VERADITE_WALL.get().asItem(),
                POLISHED_VERADITE.get().asItem(),
                POLISHED_VERADITE_BUTTON.get().asItem(),
                POLISHED_VERADITE_PRESSURE_PLATE.get().asItem(),
                POLISHED_VERADITE_SLAB.get().asItem(),
                POLISHED_VERADITE_STAIRS.get().asItem(),
                POLISHED_VERADITE_WALL.get().asItem()
        );

        tag(KURODITE_BLOCKS).add(
                CHISELED_KURODITE.get().asItem(),
                KURODITE.get().asItem(),
                KURODITE_BRICKS.get().asItem(),
                KURODITE_BRICK_SLAB.get().asItem(),
                KURODITE_BRICK_STAIRS.get().asItem(),
                KURODITE_BRICK_WALL.get().asItem(),
                KURODITE_SLAB.get().asItem(),
                KURODITE_STAIRS.get().asItem(),
                KURODITE_WALL.get().asItem(),
                POLISHED_KURODITE.get().asItem(),
                POLISHED_KURODITE_BUTTON.get().asItem(),
                POLISHED_KURODITE_PRESSURE_PLATE.get().asItem(),
                POLISHED_KURODITE_SLAB.get().asItem(),
                POLISHED_KURODITE_STAIRS.get().asItem(),
                POLISHED_KURODITE_WALL.get().asItem()
        );

        tag(SHADOLINE_BLOCKS).add(
                CHISELED_SHADOLINE.get().asItem(),
                CUT_SHADOLINE.get().asItem(),
                CUT_SHADOLINE_SLAB.get().asItem(),
                CUT_SHADOLINE_STAIRS.get().asItem(),
                CUT_SHADOLINE_WALL.get().asItem(),
                SHADOLINE_BLOCK.get().asItem(),
                SHADOLINE_BLOCK_SLAB.get().asItem(),
                SHADOLINE_BLOCK_STAIRS.get().asItem(),
                SHADOLINE_BLOCK_WALL.get().asItem(),
                SHADOLINE_PILLAR.get().asItem(),
                SHADOLINE_BARS.get().asItem(),
                SHADOLINE_CHAIN.get().asItem()
        );

        tag(ALL_ETCHED_MAGNIA_BLOCKS).addTag(ETCHED_ALLURING_MAGNIA_BLOCKS).addTag(ETCHED_REPULSIVE_MAGNIA_BLOCKS);

        tag(ETCHED_ALLURING_MAGNIA_BLOCKS).add(
                ETCHED_ALLURING_MAGNIA.get().asItem(),
                ETCHED_ALLURING_MAGNIA_STAIRS.get().asItem(),
                ETCHED_ALLURING_MAGNIA_SLAB.get().asItem(),
                ETCHED_ALLURING_MAGNIA_WALL.get().asItem()
        );

        tag(ETCHED_REPULSIVE_MAGNIA_BLOCKS).add(
                ETCHED_REPULSIVE_MAGNIA.get().asItem(),
                ETCHED_REPULSIVE_MAGNIA_STAIRS.get().asItem(),
                ETCHED_REPULSIVE_MAGNIA_SLAB.get().asItem(),
                ETCHED_REPULSIVE_MAGNIA_WALL.get().asItem()
        );

        tag(MAGNIA_BLOCKS).add(ALLURING_MAGNIA.get().asItem(), REPULSIVE_MAGNIA.get().asItem());
        tag(MAGNIA_SPROUTS).add(ALLURING_MAGNIA_SPROUT.get().asItem(), REPULSIVE_MAGNIA_SPROUT.get().asItem());

        tag(BEACON_PAYMENT_ITEMS).add(NEBULITE.get());
        tag(CHEST_ARMOR).add(SHADOLINE_CHESTPLATE.get());
        tag(DURABILITY_ENCHANTABLE).add(END_STONE_RUBBLE_SHIELD.get(), MIRESTONE_RUBBLE_SHIELD.get(), VERADITE_RUBBLE_SHIELD.get(), KURODITE_RUBBLE_SHIELD.get());
        tag(EQUIPPABLE_ENCHANTABLE).add(SHULKER_SHELL);
        tag(FIRE_ASPECT_ENCHANTABLE).add(DAGGER.get());
        tag(FOOT_ARMOR).add(SHADOLINE_BOOTS.get());
        tag(HANGING_SIGNS).add(VEILED_HANGING_SIGN.get().asItem(), CELESTIAL_HANGING_SIGN.get().asItem(), MURUBLIGHT_HANGING_SIGN.get().asItem());
        tag(HEAD_ARMOR).add(SHADOLINE_HELMET.get());
        tag(ItemTags.FENCE_GATES).add(CELESTIAL_FENCE_GATE.get().asItem(), MURUBLIGHT_FENCE_GATE.get().asItem());
        tag(ItemTags.TALL_FLOWERS).add(WISP_FLOWER.get().asItem());
        tag(ItemTags.FLOWERS).add(FLANGER_BERRY_FLOWER.get().asItem());
        tag(ItemTags.SMALL_FLOWERS).add(BULB_FLOWER.get().asItem());
        tag(ItemTags.WOODEN_FENCES).add(VEILED_FENCE.get().asItem(), CELESTIAL_FENCE.get().asItem(), MURUBLIGHT_FENCE.get().asItem());
        tag(LEAVES).add(VEILED_LEAVES.get().asItem());
        tag(LEG_ARMOR).add(SHADOLINE_LEGGINGS.get(), DRIFT_LEGGINGS.get());
        tag(LOGS_THAT_BURN).addTag(VEILED_LOGS).addTag(CELESTIAL_STEMS).addTag(MURUBLIGHT_STEMS);
        tag(PLANKS).add(VEILED_PLANKS.get().asItem(), CELESTIAL_PLANKS.get().asItem(), MURUBLIGHT_PLANKS.get().asItem());
        tag(SAPLINGS).add(VEILED_SAPLING.get().asItem());
        tag(SHARP_WEAPON_ENCHANTABLE).add(DAGGER.get());
        tag(SIGNS).add(VEILED_SIGN.get().asItem(), CELESTIAL_SIGN.get().asItem(), MURUBLIGHT_SIGN.get().asItem());
        tag(SLABS).add(ETCHED_ALLURING_MAGNIA_SLAB.get().asItem(), ETCHED_REPULSIVE_MAGNIA_SLAB.get().asItem(), VEILED_SLAB.get().asItem(), CELESTIAL_SLAB.get().asItem(), CELESTIAL_BRICK_SLAB.get().asItem(), MURUBLIGHT_SLAB.get().asItem(), MURUBLIGHT_BRICK_SLAB.get().asItem(), VERADITE_SLAB.get().asItem(), POLISHED_VERADITE_SLAB.get().asItem(), VERADITE_BRICK_SLAB.get().asItem(), KURODITE_SLAB.get().asItem(), POLISHED_KURODITE_SLAB.get().asItem(), KURODITE_BRICK_SLAB.get().asItem(), SHADOLINE_BLOCK_SLAB.get().asItem(), CUT_SHADOLINE_SLAB.get().asItem());
        tag(STAIRS).add(ETCHED_ALLURING_MAGNIA_SLAB.get().asItem(), ETCHED_REPULSIVE_MAGNIA_STAIRS.get().asItem(), VEILED_STAIRS.get().asItem(), CELESTIAL_STAIRS.get().asItem(), CELESTIAL_BRICK_STAIRS.get().asItem(), MURUBLIGHT_STAIRS.get().asItem(), MURUBLIGHT_BRICK_STAIRS.get().asItem(), VERADITE_STAIRS.get().asItem(), POLISHED_VERADITE_STAIRS.get().asItem(), VERADITE_BRICK_STAIRS.get().asItem(), KURODITE_STAIRS.get().asItem(), POLISHED_KURODITE_STAIRS.get().asItem(), KURODITE_BRICK_STAIRS.get().asItem(), SHADOLINE_BLOCK_STAIRS.get().asItem(), CUT_SHADOLINE_STAIRS.get().asItem());
        tag(STONE_BUTTONS).add(POLISHED_VERADITE_BUTTON.get().asItem(), POLISHED_KURODITE_BUTTON.get().asItem());
        tag(STONE_CRAFTING_MATERIALS).add(END_STONE.asItem(), VERADITE.get().asItem(), MIRESTONE.get().asItem(), KURODITE.get().asItem());
        tag(STONE_TOOL_MATERIALS).add(END_STONE.asItem(), VERADITE.get().asItem(), MIRESTONE.get().asItem(), KURODITE.get().asItem());
        tag(SWEEPING_EDGE_INCOMPATIBLE).add(DAGGER.get());
        tag(SWORD_ENCHANTABLE).add(DAGGER.get());
        tag(TRIMMABLE_ARMOR).add(SHADOLINE_HELMET.get(), SHADOLINE_CHESTPLATE.get(), SHADOLINE_LEGGINGS.get(), SHADOLINE_BOOTS.get(), DRIFT_LEGGINGS.get());
        tag(TRIM_MATERIALS).add(NEBULITE.get(), SHADOLINE_INGOT.get());
        tag(VANISHING_ENCHANTABLE).add(MIRROR.get(), SHULKER_SHELL);
        tag(WALLS).add(ETCHED_ALLURING_MAGNIA_WALL.get().asItem(), ETCHED_REPULSIVE_MAGNIA_WALL.get().asItem(), VERADITE_WALL.get().asItem(), POLISHED_VERADITE_WALL.get().asItem(), VERADITE_BRICK_WALL.get().asItem(), SHADOLINE_BLOCK_WALL.get().asItem(), KURODITE_WALL.get().asItem(), POLISHED_KURODITE_WALL.get().asItem(), KURODITE_BRICK_WALL.get().asItem(), CUT_SHADOLINE_WALL.get().asItem(), CELESTIAL_BRICK_WALL.get().asItem(), MURUBLIGHT_BRICK_WALL.get().asItem());
        tag(WOODEN_BUTTONS).add(VEILED_BUTTON.get().asItem(), CELESTIAL_BUTTON.get().asItem(), MURUBLIGHT_BUTTON.get().asItem());
        tag(WOODEN_DOORS).add(VEILED_DOOR.get().asItem(), CELESTIAL_DOOR.get().asItem(), MURUBLIGHT_DOOR.get().asItem());
        tag(WOODEN_PRESSURE_PLATES).add(VEILED_PRESSURE_PLATE.get().asItem(), CELESTIAL_PRESSURE_PLATE.get().asItem(), MURUBLIGHT_PRESSURE_PLATE.get().asItem());
        tag(WOODEN_SLABS).add(VEILED_SLAB.get().asItem(), CELESTIAL_SLAB.get().asItem(), MURUBLIGHT_SLAB.get().asItem());
        tag(WOODEN_STAIRS).add(VEILED_STAIRS.get().asItem(), CELESTIAL_STAIRS.get().asItem(), MURUBLIGHT_STAIRS.get().asItem());
        tag(WOODEN_TRAPDOORS).add(VEILED_TRAPDOOR.get().asItem(), CELESTIAL_TRAPDOOR.get().asItem(), MURUBLIGHT_TRAPDOOR.get().asItem());

        // Item Descriptions

        tag(externalKey("item_tooltips", "has_description")).add(MAGNIA_ATTRACTOR.get(), DRIFT_LEGGINGS.get(), DRIFT_JELLY_BOTTLE.get(), DRIFT_JELLY_BLOCK.get().asItem(), NEBULITE.get()).addTag(RUBBLE_SHIELDS);

        // Common Tags

        tag(FOODS_BERRY).add(FLANGER_BERRY.get());
        tag(BUCKETS).add(RUSTLE_BUCKET.get());
        tag(CHAINS).add(SHADOLINE_CHAIN.get().asItem());
        tag(DRIFT_JELLY_DRINKS).add(DRIFT_JELLY_BOTTLE.get());
        tag(DRINKS).add(DRIFT_JELLY_BOTTLE.get());
        tag(DRINK_CONTAINING_BOTTLE).add(DRIFT_JELLY_BOTTLE.get());
        tag(FOODS_EDIBLE_WHEN_PLACED).add(CHORUS_CAKE_ROLL.get().asItem());
        tag(FOODS).add(DRIFT_JELLY_BOTTLE.get());
        tag(GEMS).add(NEBULITE.get());
        tag(INGOTS).add(SHADOLINE_INGOT.get());
        tag(NUGGETS).add(SHADOLINE_NUGGET.get());
        tag(DRINKS_MAGIC).add(DRIFT_JELLY_BOTTLE.get());
        tag(MUSIC_DISCS).add(MUSIC_DISC_GLARE.get(), MUSIC_DISC_DECAY.get(), MUSIC_DISC_BLISS.get());
        tag(NEBULITE_GEMS).add(NEBULITE.get());
        tag(NEBULITE_ORES).add(NEBULITE_ORE.get().asItem(), MIRESTONE_NEBULITE_ORE.get().asItem());
        tag(NEBULITE_STORAGE_BLOCKS).add(NEBULITE_BLOCK.get().asItem());
        tag(RAW_MATERIALS).add(RAW_SHADOLINE.get());
        tag(RAW_SHADOLINE_STORAGE_BLOCKS).add(RAW_SHADOLINE_BLOCK.get().asItem());
        tag(SHADOLINE_INGOTS).add(SHADOLINE_INGOT.get());
        tag(SHADOLINE_NUGGETS).add(SHADOLINE_NUGGET.get());
        tag(SHADOLINE_ORES).add(SHADOLINE_ORE.get().asItem(), MIRESTONE_SHADOLINE_ORE.get().asItem());
        tag(SHADOLINE_RAW_MATERIALS).add(RAW_SHADOLINE.get());
        tag(SHADOLINE_STORAGE_BLOCKS).add(SHADOLINE_BLOCK.get().asItem());
        tag(TOOLS_SHIELD).add(END_STONE_RUBBLE_SHIELD.get(), VERADITE_RUBBLE_SHIELD.get(), MIRESTONE_RUBBLE_SHIELD.get(), KURODITE_RUBBLE_SHIELD.get());
        tag(STORAGE_BLOCKS).add(SHADOLINE_BLOCK.get().asItem(), NEBULITE_BLOCK.get().asItem(), DRIFT_JELLY_BLOCK.get().asItem());
        tag(TOOLS).add(MIRROR.get(), MAGNIA_ATTRACTOR.get());

        tag(externalKey("create", "upright_on_belt")).add(DRIFT_JELLY_BOTTLE.get());
        tag(externalKey("create", "deployable_drink")).add(DRIFT_JELLY_BOTTLE.get());
    }

    private TagKey<Item> externalKey(String namespace, String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
}
