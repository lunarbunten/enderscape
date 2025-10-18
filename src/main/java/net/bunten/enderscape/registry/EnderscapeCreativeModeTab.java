package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;
import static net.bunten.enderscape.registry.EnderscapeItems.*;
import static net.minecraft.world.item.Items.*;

public class EnderscapeCreativeModeTab {

    public static final CreativeModeTab ENDERSCAPE = FabricItemGroup.builder().title(Component.translatable("itemGroup.enderscape")).icon(NEBULITE::getDefaultInstance).displayItems((parameters, output) -> {
        output.accept(ENDERMAN_SPAWN_EGG);
        output.accept(ENDERMITE_SPAWN_EGG);
        output.accept(RUBBLEMITE_SPAWN_EGG);
        output.accept(SHULKER_SPAWN_EGG);
        output.accept(RUSTLE_SPAWN_EGG);
        output.accept(DRIFTER_SPAWN_EGG);
        output.accept(DRIFTLET_SPAWN_EGG);

        output.accept(DRAGON_BREATH);
        output.accept(RUBBLE_CHITIN);
        output.accept(SHULKER_SHELL);
        output.accept(CRESCENT_BANNER_PATTERN);
        output.accept(EYE_ARMOR_TRIM_SMITHING_TEMPLATE);
        output.accept(SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE);
        output.accept(STASIS_ARMOR_TRIM_SMITHING_TEMPLATE);

        output.accept(getEnchantedBook(parameters, EnderscapeEnchantments.BUNDLING, 1));
        output.accept(getEnchantedBook(parameters, EnderscapeEnchantments.LIGHTSPEED, 3));
        output.accept(getEnchantedBook(parameters, EnderscapeEnchantments.TRANSDIMENSIONAL, 1));
        output.accept(getEnchantedBook(parameters, EnderscapeEnchantments.REBOUND, 1));

        output.accept(END_PORTAL_FRAME);
        output.accept(ENDER_EYE);
        output.accept(END_CRYSTAL);
        output.accept(DRAGON_EGG);
        output.accept(DRAGON_HEAD);
        output.accept(ENDER_CHEST);
        output.accept(SHULKER_BOX);
        output.accept(getEndCityBannerInstance(parameters.holders().lookupOrThrow(Registries.BANNER_PATTERN)));
        output.accept(getPainting(parameters, EnderscapePaintingVariants.GRAPE_STATIC));
        output.accept(END_TRIAL_SPAWNER);
        output.accept(getEndVaultInstance());
        output.accept(END_CITY_KEY);

        output.accept(ENDER_PEARL);
        output.accept(END_STONE_RUBBLE_SHIELD);
        output.accept(VERADITE_RUBBLE_SHIELD);
        output.accept(MIRESTONE_RUBBLE_SHIELD);
        output.accept(KURODITE_RUBBLE_SHIELD);
        output.accept(RUSTLE_BUCKET);
        output.accept(DRIFT_JELLY_BOTTLE);
        output.accept(DRIFT_JELLY_BLOCK);
        output.accept(DRIFT_LEGGINGS);

        parameters.holders().lookup(Registries.POTION).ifPresent(potion -> {
            output.accept(PotionContents.createItemStack(POTION, EnderscapePotions.LOW_GRAVITY));
            output.accept(PotionContents.createItemStack(SPLASH_POTION, EnderscapePotions.LOW_GRAVITY));
            output.accept(PotionContents.createItemStack(LINGERING_POTION, EnderscapePotions.LOW_GRAVITY));
            output.accept(PotionContents.createItemStack(TIPPED_ARROW, EnderscapePotions.LOW_GRAVITY));
        });

        output.accept(MAGNIA_ATTRACTOR);
        output.accept(MIRROR);
        output.accept(ELYTRA);

        output.accept(MUSIC_DISC_GLARE);
        output.accept(MUSIC_DISC_DECAY);
        output.accept(MUSIC_DISC_BLISS);

        output.accept(END_STONE);
        output.accept(END_STONE_STAIRS);
        output.accept(END_STONE_SLAB);
        output.accept(END_STONE_WALL);
        output.accept(POLISHED_END_STONE);
        output.accept(POLISHED_END_STONE_STAIRS);
        output.accept(POLISHED_END_STONE_SLAB);
        output.accept(POLISHED_END_STONE_WALL);
        output.accept(POLISHED_END_STONE_PRESSURE_PLATE);
        output.accept(POLISHED_END_STONE_BUTTON);
        output.accept(END_STONE_BRICKS);
        output.accept(END_STONE_BRICK_STAIRS);
        output.accept(END_STONE_BRICK_SLAB);
        output.accept(END_STONE_BRICK_WALL);
        output.accept(CHISELED_END_STONE);

        output.accept(VERADITE);
        output.accept(VERADITE_STAIRS);
        output.accept(VERADITE_SLAB);
        output.accept(VERADITE_WALL);
        output.accept(POLISHED_VERADITE);
        output.accept(POLISHED_VERADITE_STAIRS);
        output.accept(POLISHED_VERADITE_SLAB);
        output.accept(POLISHED_VERADITE_WALL);
        output.accept(POLISHED_VERADITE_PRESSURE_PLATE);
        output.accept(POLISHED_VERADITE_BUTTON);
        output.accept(VERADITE_BRICKS);
        output.accept(VERADITE_BRICK_STAIRS);
        output.accept(VERADITE_BRICK_SLAB);
        output.accept(VERADITE_BRICK_WALL);
        output.accept(CHISELED_VERADITE);

        output.accept(MIRESTONE);
        output.accept(MIRESTONE_STAIRS);
        output.accept(MIRESTONE_SLAB);
        output.accept(MIRESTONE_WALL);
        output.accept(POLISHED_MIRESTONE);
        output.accept(POLISHED_MIRESTONE_STAIRS);
        output.accept(POLISHED_MIRESTONE_SLAB);
        output.accept(POLISHED_MIRESTONE_WALL);
        output.accept(POLISHED_MIRESTONE_PRESSURE_PLATE);
        output.accept(POLISHED_MIRESTONE_BUTTON);
        output.accept(MIRESTONE_BRICKS);
        output.accept(MIRESTONE_BRICK_STAIRS);
        output.accept(MIRESTONE_BRICK_SLAB);
        output.accept(MIRESTONE_BRICK_WALL);
        output.accept(CHISELED_MIRESTONE);

        output.accept(KURODITE);
        output.accept(KURODITE_STAIRS);
        output.accept(KURODITE_SLAB);
        output.accept(KURODITE_WALL);
        output.accept(POLISHED_KURODITE);
        output.accept(POLISHED_KURODITE_STAIRS);
        output.accept(POLISHED_KURODITE_SLAB);
        output.accept(POLISHED_KURODITE_WALL);
        output.accept(POLISHED_KURODITE_PRESSURE_PLATE);
        output.accept(POLISHED_KURODITE_BUTTON);
        output.accept(KURODITE_BRICKS);
        output.accept(KURODITE_BRICK_STAIRS);
        output.accept(KURODITE_BRICK_SLAB);
        output.accept(KURODITE_BRICK_WALL);
        output.accept(CHISELED_KURODITE);

        output.accept(ALLURING_MAGNIA);
        output.accept(ETCHED_ALLURING_MAGNIA);
        output.accept(ETCHED_ALLURING_MAGNIA_STAIRS);
        output.accept(ETCHED_ALLURING_MAGNIA_SLAB);
        output.accept(ETCHED_ALLURING_MAGNIA_WALL);
        output.accept(ALLURING_MAGNIA_SPROUT);
        output.accept(REPULSIVE_MAGNIA);
        output.accept(ETCHED_REPULSIVE_MAGNIA);
        output.accept(ETCHED_REPULSIVE_MAGNIA_STAIRS);
        output.accept(ETCHED_REPULSIVE_MAGNIA_SLAB);
        output.accept(ETCHED_REPULSIVE_MAGNIA_WALL);
        output.accept(REPULSIVE_MAGNIA_SPROUT);
        output.accept(BLISTERED_MAGNIA);
        output.accept(POLARIZED_MAGNIA);

        output.accept(VOID_SHALE);

        output.accept(SHADOLINE_ORE);
        output.accept(MIRESTONE_SHADOLINE_ORE);
        output.accept(RAW_SHADOLINE);
        output.accept(RAW_SHADOLINE_BLOCK);
        output.accept(SHADOLINE_INGOT);
        output.accept(SHADOLINE_NUGGET);
        output.accept(SHADOLINE_BLOCK);
        output.accept(SHADOLINE_BLOCK_STAIRS);
        output.accept(SHADOLINE_BLOCK_SLAB);
        output.accept(SHADOLINE_BLOCK_WALL);
        output.accept(CUT_SHADOLINE);
        output.accept(CUT_SHADOLINE_STAIRS);
        output.accept(CUT_SHADOLINE_SLAB);
        output.accept(CUT_SHADOLINE_WALL);
        output.accept(CHISELED_SHADOLINE);
        output.accept(SHADOLINE_PILLAR);

        output.accept(NEBULITE_ORE);
        output.accept(MIRESTONE_NEBULITE_ORE);
        output.accept(NEBULITE_SHARDS);
        output.accept(NEBULITE);
        output.accept(NEBULITE_BLOCK);

        output.accept(DRY_END_GROWTH);
        output.accept(CHORUS_SPROUTS);
        output.accept(CHORUS_PLANT);
        output.accept(CHORUS_FLOWER);
        output.accept(CHORUS_FRUIT);
        output.accept(POPPED_CHORUS_FRUIT);
        output.accept(CHORUS_CAKE_ROLL_ITEM);
        output.accept(PURPUR_BLOCK);
        output.accept(PURPUR_STAIRS);
        output.accept(PURPUR_SLAB);
        output.accept(PURPUR_WALL);
        output.accept(CHISELED_PURPUR);
        output.accept(PURPUR_PILLAR);
        output.accept(DUSK_PURPUR_BLOCK);
        output.accept(DUSK_PURPUR_STAIRS);
        output.accept(DUSK_PURPUR_SLAB);
        output.accept(DUSK_PURPUR_WALL);
        output.accept(CHISELED_DUSK_PURPUR);
        output.accept(DUSK_PURPUR_PILLAR);
        output.accept(PURPUR_TILES);
        output.accept(PURPUR_TILE_STAIRS);
        output.accept(PURPUR_TILE_SLAB);
        output.accept(END_ROD);
        output.accept(END_LAMP);

        output.accept(VEILED_END_STONE);
        output.accept(WISP_SPROUTS);
        output.accept(WISP_GROWTH);
        output.accept(WISP_FLOWER);
        output.accept(VEILED_SAPLING);
        output.accept(VEILED_LEAVES);
        output.accept(VEILED_LEAF_PILE);
        output.accept(VEILED_VINES);

        output.accept(VEILED_LOG);
        output.accept(STRIPPED_VEILED_LOG);
        output.accept(VEILED_WOOD);
        output.accept(STRIPPED_VEILED_WOOD);
        output.accept(VEILED_PLANKS);
        output.accept(VEILED_STAIRS);
        output.accept(VEILED_SLAB);
        output.accept(VEILED_FENCE);
        output.accept(VEILED_FENCE_GATE);
        output.accept(VEILED_DOOR);
        output.accept(VEILED_TRAPDOOR);
        output.accept(VEILED_PRESSURE_PLATE);
        output.accept(VEILED_BUTTON);
        output.accept(VEILED_SHELF_ITEM);
        output.accept(VEILED_SIGN_ITEM);
        output.accept(VEILED_HANGING_SIGN_ITEM);

        output.accept(CELESTIAL_OVERGROWTH);
        output.accept(CELESTIAL_PATH);
        output.accept(CELESTIAL_GROWTH);
        output.accept(BULB_FLOWER);
        output.accept(BULB_LANTERN);
        output.accept(FLANGER_BERRY);
        output.accept(FLANGER_BERRY_FLOWER);
        output.accept(UNRIPE_FLANGER_BERRY_BLOCK);
        output.accept(RIPE_FLANGER_BERRY_BLOCK);

        output.accept(CELESTIAL_CHANTERELLE);
        output.accept(CELESTIAL_CAP);
        output.accept(CELESTIAL_BRICKS);
        output.accept(CELESTIAL_BRICK_STAIRS);
        output.accept(CELESTIAL_BRICK_SLAB);
        output.accept(CELESTIAL_BRICK_WALL);

        output.accept(CELESTIAL_STEM);
        output.accept(STRIPPED_CELESTIAL_STEM);
        output.accept(CELESTIAL_HYPHAE);
        output.accept(STRIPPED_CELESTIAL_HYPHAE);
        output.accept(CELESTIAL_PLANKS);
        output.accept(CELESTIAL_STAIRS);
        output.accept(CELESTIAL_SLAB);
        output.accept(CELESTIAL_FENCE);
        output.accept(CELESTIAL_FENCE_GATE);
        output.accept(CELESTIAL_DOOR);
        output.accept(CELESTIAL_TRAPDOOR);
        output.accept(CELESTIAL_PRESSURE_PLATE);
        output.accept(CELESTIAL_BUTTON);
        output.accept(CELESTIAL_SHELF_ITEM);
        output.accept(CELESTIAL_SIGN_ITEM);
        output.accept(CELESTIAL_HANGING_SIGN_ITEM);

        output.accept(CORRUPT_OVERGROWTH);
        output.accept(CORRUPT_PATH);
        output.accept(CORRUPT_GROWTH);
        output.accept(BLINKLIGHT);
        output.accept(BLINKLAMP);

        output.accept(MURUBLIGHT_BRACKET_ITEM);
        output.accept(MURUBLIGHT_CHANTERELLE);

        output.accept(MURUBLIGHT_CAP);
        output.accept(MURUBLIGHT_BRICKS);
        output.accept(MURUBLIGHT_BRICK_STAIRS);
        output.accept(MURUBLIGHT_BRICK_SLAB);
        output.accept(MURUBLIGHT_BRICK_WALL);

        output.accept(MURUBLIGHT_STEM);
        output.accept(STRIPPED_MURUBLIGHT_STEM);
        output.accept(MURUBLIGHT_HYPHAE);
        output.accept(STRIPPED_MURUBLIGHT_HYPHAE);
        output.accept(MURUBLIGHT_PLANKS);
        output.accept(MURUBLIGHT_STAIRS);
        output.accept(MURUBLIGHT_SLAB);
        output.accept(MURUBLIGHT_FENCE);
        output.accept(MURUBLIGHT_FENCE_GATE);
        output.accept(MURUBLIGHT_DOOR);
        output.accept(MURUBLIGHT_TRAPDOOR);
        output.accept(MURUBLIGHT_PRESSURE_PLATE);
        output.accept(MURUBLIGHT_BUTTON);
        output.accept(MURUBLIGHT_SHELF_ITEM);
        output.accept(MURUBLIGHT_SIGN_ITEM);
        output.accept(MURUBLIGHT_HANGING_SIGN_ITEM);

    }).build();
    
    static {

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
            entries.addAfter(END_STONE,
                    END_STONE_STAIRS,
                    END_STONE_SLAB,
                    END_STONE_WALL,
                    CHISELED_END_STONE,
                    POLISHED_END_STONE,
                    POLISHED_END_STONE_STAIRS,
                    POLISHED_END_STONE_SLAB,
                    POLISHED_END_STONE_WALL,
                    POLISHED_END_STONE_PRESSURE_PLATE,
                    POLISHED_END_STONE_BUTTON
            );

            entries.addAfter(END_STONE_BRICK_WALL,
                    VERADITE,
                    VERADITE_STAIRS,
                    VERADITE_SLAB,
                    VERADITE_WALL,
                    CHISELED_VERADITE,
                    POLISHED_VERADITE,
                    POLISHED_VERADITE_STAIRS,
                    POLISHED_VERADITE_SLAB,
                    POLISHED_VERADITE_WALL,
                    POLISHED_VERADITE_PRESSURE_PLATE,
                    POLISHED_VERADITE_BUTTON,
                    VERADITE_BRICKS,
                    VERADITE_BRICK_STAIRS,
                    VERADITE_BRICK_SLAB,
                    VERADITE_BRICK_WALL,

                    MIRESTONE,
                    MIRESTONE_STAIRS,
                    MIRESTONE_SLAB,
                    MIRESTONE_WALL,
                    CHISELED_MIRESTONE,
                    POLISHED_MIRESTONE,
                    POLISHED_MIRESTONE_STAIRS,
                    POLISHED_MIRESTONE_SLAB,
                    POLISHED_MIRESTONE_WALL,
                    POLISHED_MIRESTONE_PRESSURE_PLATE,
                    POLISHED_MIRESTONE_BUTTON,
                    MIRESTONE_BRICKS,
                    MIRESTONE_BRICK_STAIRS,
                    MIRESTONE_BRICK_SLAB,
                    MIRESTONE_BRICK_WALL,

                    KURODITE,
                    KURODITE_STAIRS,
                    KURODITE_SLAB,
                    KURODITE_WALL,
                    CHISELED_KURODITE,
                    POLISHED_KURODITE,
                    POLISHED_KURODITE_STAIRS,
                    POLISHED_KURODITE_SLAB,
                    POLISHED_KURODITE_WALL,
                    POLISHED_KURODITE_PRESSURE_PLATE,
                    POLISHED_KURODITE_BUTTON,
                    KURODITE_BRICKS,
                    KURODITE_BRICK_STAIRS,
                    KURODITE_BRICK_SLAB,
                    KURODITE_BRICK_WALL,

                    ALLURING_MAGNIA,
                    ETCHED_ALLURING_MAGNIA,
                    ETCHED_ALLURING_MAGNIA_STAIRS,
                    ETCHED_ALLURING_MAGNIA_SLAB,
                    ETCHED_ALLURING_MAGNIA_WALL,
                    REPULSIVE_MAGNIA,
                    ETCHED_REPULSIVE_MAGNIA,
                    ETCHED_REPULSIVE_MAGNIA_STAIRS,
                    ETCHED_REPULSIVE_MAGNIA_SLAB,
                    ETCHED_REPULSIVE_MAGNIA_WALL
            );

            entries.addBefore(PURPUR_PILLAR, CHISELED_PURPUR);
            entries.addAfter(PURPUR_SLAB,
                    PURPUR_WALL,

                    DUSK_PURPUR_BLOCK,
                    CHISELED_DUSK_PURPUR,
                    DUSK_PURPUR_PILLAR,

                    DUSK_PURPUR_STAIRS,
                    DUSK_PURPUR_SLAB,
                    DUSK_PURPUR_WALL,

                    PURPUR_TILES,
                    PURPUR_TILE_STAIRS,
                    PURPUR_TILE_SLAB,

                    CELESTIAL_BRICKS,
                    CELESTIAL_BRICK_STAIRS,
                    CELESTIAL_BRICK_SLAB,
                    CELESTIAL_BRICK_WALL,

                    MURUBLIGHT_BRICKS,
                    MURUBLIGHT_BRICK_STAIRS,
                    MURUBLIGHT_BRICK_SLAB,
                    MURUBLIGHT_BRICK_WALL
            );

            entries.addAfter(WARPED_BUTTON,
                    VEILED_LOG,
                    STRIPPED_VEILED_LOG,
                    VEILED_WOOD,
                    STRIPPED_VEILED_WOOD,
                    VEILED_PLANKS,
                    VEILED_STAIRS,
                    VEILED_SLAB,
                    VEILED_FENCE,
                    VEILED_FENCE_GATE,
                    VEILED_DOOR,
                    VEILED_TRAPDOOR,
                    VEILED_PRESSURE_PLATE,
                    VEILED_BUTTON,

                    CELESTIAL_STEM,
                    STRIPPED_CELESTIAL_STEM,
                    CELESTIAL_HYPHAE,
                    STRIPPED_CELESTIAL_HYPHAE,
                    CELESTIAL_PLANKS,
                    CELESTIAL_STAIRS,
                    CELESTIAL_SLAB,
                    CELESTIAL_FENCE,
                    CELESTIAL_FENCE_GATE,
                    CELESTIAL_DOOR,
                    CELESTIAL_TRAPDOOR,
                    CELESTIAL_PRESSURE_PLATE,
                    CELESTIAL_BUTTON,

                    MURUBLIGHT_STEM,
                    STRIPPED_MURUBLIGHT_STEM,
                    MURUBLIGHT_HYPHAE,
                    STRIPPED_MURUBLIGHT_HYPHAE,
                    MURUBLIGHT_PLANKS,
                    MURUBLIGHT_STAIRS,
                    MURUBLIGHT_SLAB,
                    MURUBLIGHT_FENCE,
                    MURUBLIGHT_FENCE_GATE,
                    MURUBLIGHT_DOOR,
                    MURUBLIGHT_TRAPDOOR,
                    MURUBLIGHT_PRESSURE_PLATE,
                    MURUBLIGHT_BUTTON
            );

            entries.addAfter(NETHERITE_BLOCK, NEBULITE_BLOCK);

            entries.addAfter(WAXED_OXIDIZED_COPPER_BULB,
                    SHADOLINE_BLOCK,
                    SHADOLINE_BLOCK_STAIRS,
                    SHADOLINE_BLOCK_SLAB,
                    SHADOLINE_BLOCK_WALL,
                    CUT_SHADOLINE,
                    CUT_SHADOLINE_STAIRS,
                    CUT_SHADOLINE_SLAB,
                    CUT_SHADOLINE_WALL,
                    CHISELED_SHADOLINE,
                    SHADOLINE_PILLAR
            );
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> {

            entries.addAfter(END_STONE,
                    VEILED_END_STONE,
                    CELESTIAL_OVERGROWTH,
                    CELESTIAL_PATH,
                    MIRESTONE,
                    CORRUPT_OVERGROWTH,
                    CORRUPT_PATH,
                    VERADITE,
                    KURODITE,
                    ALLURING_MAGNIA,
                    ALLURING_MAGNIA_SPROUT,
                    REPULSIVE_MAGNIA,
                    REPULSIVE_MAGNIA_SPROUT,
                    BLISTERED_MAGNIA,
                    VOID_SHALE
            );

            entries.addBefore(RAW_IRON_BLOCK, NEBULITE_ORE, MIRESTONE_NEBULITE_ORE, SHADOLINE_ORE, MIRESTONE_SHADOLINE_ORE);

            entries.addBefore(GLOWSTONE, RAW_SHADOLINE_BLOCK);

            entries.addBefore(MUSHROOM_STEM, VEILED_LOG);
            entries.addBefore(OAK_LEAVES, CELESTIAL_STEM, MURUBLIGHT_STEM);
            entries.addBefore(BROWN_MUSHROOM_BLOCK, VEILED_LEAVES, VEILED_LEAF_PILE, VEILED_VINES);
            entries.addBefore(OAK_SAPLING, CELESTIAL_CAP, MURUBLIGHT_CAP);
            entries.addBefore(SHORT_GRASS, CELESTIAL_CHANTERELLE, MURUBLIGHT_CHANTERELLE, MURUBLIGHT_BRACKET_ITEM);
            entries.addBefore(BROWN_MUSHROOM, VEILED_SAPLING);

            entries.addBefore(VINE,
                    DRY_END_GROWTH,
                    CHORUS_SPROUTS,
                    WISP_SPROUTS,
                    WISP_GROWTH,
                    WISP_FLOWER,
                    CELESTIAL_GROWTH,
                    BULB_FLOWER,
                    CORRUPT_GROWTH,
                    BLINKLIGHT
            );

            entries.addAfter(HONEY_BLOCK, DRIFT_JELLY_BLOCK);
            entries.addAfter(JACK_O_LANTERN,
                    FLANGER_BERRY_FLOWER,
                    UNRIPE_FLANGER_BERRY_BLOCK,
                    RIPE_FLANGER_BERRY_BLOCK
            );
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
            entries.addBefore(IRON_CHAIN, BULB_LANTERN);
            entries.addBefore(END_ROD, END_LAMP);
            entries.addBefore(CRYING_OBSIDIAN, POLARIZED_MAGNIA, BLINKLAMP);
            entries.addBefore(CHEST, VEILED_SIGN_ITEM, VEILED_HANGING_SIGN_ITEM, CELESTIAL_SIGN_ITEM, CELESTIAL_HANGING_SIGN_ITEM, MURUBLIGHT_SIGN_ITEM, MURUBLIGHT_HANGING_SIGN_ITEM);
            entries.addBefore(INFESTED_STONE, getEndVaultInstance());
            entries.addBefore(SKELETON_SKULL, getEndCityBannerInstance(entries.getContext().holders().lookupOrThrow(Registries.BANNER_PATTERN)));
        });
        
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(entries -> {
            entries.addBefore(PISTON, BLINKLIGHT);
            entries.addAfter(CAULDRON, BLISTERED_MAGNIA, POLARIZED_MAGNIA, NEBULITE_BLOCK, ALLURING_MAGNIA, REPULSIVE_MAGNIA, FLANGER_BERRY);
            entries.addAfter(REDSTONE_LAMP, BLINKLAMP);
            entries.addAfter(BIG_DRIPLEAF, ALLURING_MAGNIA_SPROUT, REPULSIVE_MAGNIA_SPROUT);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
            entries.addAfter(LEAD, MAGNIA_ATTRACTOR);
            entries.addAfter(ELYTRA, MIRROR);
            entries.addBefore(LAVA_BUCKET, RUSTLE_BUCKET);

            entries.accept(MUSIC_DISC_GLARE);
            entries.accept(MUSIC_DISC_DECAY);
            entries.accept(MUSIC_DISC_BLISS);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(entries -> {
            entries.addBefore(LEATHER_HELMET, END_STONE_RUBBLE_SHIELD, VERADITE_RUBBLE_SHIELD, MIRESTONE_RUBBLE_SHIELD, KURODITE_RUBBLE_SHIELD);
            entries.addAfter(TURTLE_HELMET, DRIFT_LEGGINGS);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries -> {
            entries.addBefore(CARROT, FLANGER_BERRY, MURUBLIGHT_BRACKET_ITEM);
            entries.addBefore(PUMPKIN_PIE, CHORUS_CAKE_ROLL_ITEM);
            entries.addAfter(HONEY_BOTTLE, DRIFT_JELLY_BOTTLE);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {
            entries.addBefore(SHULKER_SHELL, RUBBLE_CHITIN);
            entries.addBefore(ECHO_SHARD, DRIFT_JELLY_BOTTLE);
            entries.addBefore(EMERALD, RAW_SHADOLINE);
            entries.addBefore(STICK, NEBULITE, SHADOLINE_INGOT);
            entries.addBefore(IRON_INGOT, SHADOLINE_NUGGET);
            entries.addBefore(QUARTZ, NEBULITE_SHARDS);
            entries.addAfter(SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, STASIS_ARMOR_TRIM_SMITHING_TEMPLATE);
            entries.addAfter(OMINOUS_TRIAL_KEY, END_CITY_KEY);
            entries.addBefore(ANGLER_POTTERY_SHERD, CRESCENT_BANNER_PATTERN);
        });
        
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(entries -> {
            entries.addBefore(CREAKING_HEART, END_TRIAL_SPAWNER);
            entries.addAfter(DONKEY_SPAWN_EGG, DRIFTER_SPAWN_EGG, DRIFTLET_SPAWN_EGG);
            entries.addAfter(RAVAGER_SPAWN_EGG, RUBBLEMITE_SPAWN_EGG, RUSTLE_SPAWN_EGG);
        });

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.OP_BLOCKS).register(entries -> {
            entries.accept(HEALING);
        });

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Enderscape.id("enderscape"), ENDERSCAPE);
    }

    public static ItemStack getEnchantedBook(CreativeModeTab.ItemDisplayParameters parameters, ResourceKey<Enchantment> enchantment, int level) {
        return EnchantmentHelper.createBook(new EnchantmentInstance(parameters.holders().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantment), level));
    }

    public static ItemStack getPainting(CreativeModeTab.ItemDisplayParameters parameters, ResourceKey<PaintingVariant> painting) {
        ItemStack stack = PAINTING.getDefaultInstance();
        stack.set(DataComponents.PAINTING_VARIANT, parameters.holders().lookupOrThrow(Registries.PAINTING_VARIANT).getOrThrow(painting));
        return stack;
    }
}