package net.penumbra.enderscape.registry.item;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.item.component.RubbleShieldVariant;
import net.penumbra.enderscape.registry.component.EnderscapeDataComponents;
import net.penumbra.enderscape.registry.enchantment.EnderscapeEnchantments;
import net.penumbra.enderscape.registry.entity.EnderscapePaintingVariants;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.item.Items.*;
import static net.penumbra.enderscape.registry.block.EnderscapeBlocks.*;
import static net.penumbra.enderscape.registry.item.EnderscapeItems.*;

public class EnderscapeCreativeModeTab {

    public static final CreativeModeTab ENDERSCAPE = FabricCreativeModeTab.builder().title(Component.translatable("itemGroup.enderscape")).icon(NEBULITE::getDefaultInstance).displayItems((parameters, output) -> {
        if (!EnderscapeConfig.getInstance().creativeTabEnabled) return;

        HolderLookup.Provider holders = parameters.holders();

        output.accept(ENDERMAN_SPAWN_EGG);
        output.accept(ENDERMITE_SPAWN_EGG);
        output.accept(RUBBLEMITE_SPAWN_EGG);
        output.accept(SHULKER_SPAWN_EGG);
        output.accept(RUSTLE_SPAWN_EGG);
        output.accept(DRIFTER_SPAWN_EGG);

        output.accept(DRAGON_BREATH);
        output.accept(RUBBLE_CHITIN);
        output.accept(SHULKER_SHELL);
        output.accept(CRESCENT_BANNER_PATTERN);
        output.accept(EYE_ARMOR_TRIM_SMITHING_TEMPLATE);
        output.accept(SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE);
        output.accept(STASIS_ARMOR_TRIM_SMITHING_TEMPLATE);

        EnderscapeEnchantments.ENCHANTMENTS.forEach(key -> {
            Holder.Reference<Enchantment> enchantment = holders.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);
            output.accept(getEnchantedBook(enchantment, enchantment.value().getMaxLevel()));
        });

        output.accept(END_PORTAL_FRAME);
        output.accept(ENDER_EYE);
        output.accept(END_CRYSTAL);
        output.accept(DRAGON_EGG);
        output.accept(DRAGON_HEAD);
        output.accept(ENDER_CHEST);
        output.accept(SHULKER_BOX);
        output.accept(getEndCityBannerInstance(holders.lookupOrThrow(Registries.BANNER_PATTERN)));

        EnderscapePaintingVariants.PAINTING_VARIANTS.forEach(key -> {
            output.accept(getPainting(holders.lookupOrThrow(Registries.PAINTING_VARIANT).getOrThrow(key)));
        });

        output.accept(END_HAVEN_CORE);
        output.accept(END_TRIAL_SPAWNER);
        output.accept(getEndVaultInstance());
        output.accept(END_CITY_KEY);

        output.accept(ENDER_PEARL);
        output.accept(SHADOLINE_HELMET);
        output.accept(SHADOLINE_CHESTPLATE);
        output.accept(SHADOLINE_LEGGINGS);
        output.accept(SHADOLINE_BOOTS);

        RubbleShieldVariant.VARIANTS.forEach(id -> output.accept(EnderscapeItems.rubbleShieldVariant(id)));

        output.accept(RUSTLE_BUCKET);
        output.accept(RUSTLE_SILK);
        output.accept(VOID_LACHRYMA_BUCKET);
        output.accept(DRIFT_JELLY_BOTTLE);
        output.accept(DRIFT_JELLY_BLOCK);
        output.accept(DRIFT_LEGGINGS);

        output.accept(DAGGER);
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
        output.accept(CRACKED_END_STONE_BRICKS);
        output.accept(END_STONE_BRICK_STAIRS);
        output.accept(END_STONE_BRICK_SLAB);
        output.accept(END_STONE_BRICK_WALL);
        output.accept(OVERGROWN_END_STONE_BRICKS);
        output.accept(OVERGROWN_END_STONE_BRICK_STAIRS);
        output.accept(OVERGROWN_END_STONE_BRICK_SLAB);
        output.accept(OVERGROWN_END_STONE_BRICK_WALL);
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
        output.accept(CRACKED_MIRESTONE_BRICKS);
        output.accept(MIRESTONE_BRICK_STAIRS);
        output.accept(MIRESTONE_BRICK_SLAB);
        output.accept(MIRESTONE_BRICK_WALL);
        output.accept(OVERGROWN_MIRESTONE_BRICKS);
        output.accept(OVERGROWN_MIRESTONE_BRICK_STAIRS);
        output.accept(OVERGROWN_MIRESTONE_BRICK_SLAB);
        output.accept(OVERGROWN_MIRESTONE_BRICK_WALL);
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
        output.accept(VOID_TORCH_ITEM);
        output.accept(VOID_LANTERN);
        output.accept(VOID_CAMPFIRE);

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
        output.accept(SHADOLINE_BARS);
        output.accept(SHADOLINE_CHAIN);

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
        output.accept(PURUBERRY);
        output.accept(PURUBERRY_FLOWER);
        output.accept(UNRIPE_PURUBERRY_BLOCK);
        output.accept(RIPE_PURUBERRY_BLOCK);

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

        addEveryPotion(output, POTION);
        addEveryPotion(output, SPLASH_POTION);
        addEveryPotion(output, LINGERING_POTION);
        addEveryPotion(output, TIPPED_ARROW);

    }).build();

    private static void addEveryPotion(CreativeModeTab.Output output, Item item) {
        EnderscapePotions.POTIONS.forEach(potion -> output.accept(PotionContents.createItemStack(item, potion)));
    }

    static {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register(output -> {
            if (!EnderscapeConfig.getInstance().includeItemsInVanillaCreativeTabs) return;

            output.insertAfter(END_STONE,
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

            output.insertAfter(END_STONE_BRICKS, CRACKED_END_STONE_BRICKS);

            output.insertAfter(END_STONE_BRICK_WALL,
                    OVERGROWN_END_STONE_BRICKS,
                    OVERGROWN_END_STONE_BRICK_STAIRS,
                    OVERGROWN_END_STONE_BRICK_SLAB,
                    OVERGROWN_END_STONE_BRICK_WALL,

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
                    CRACKED_MIRESTONE_BRICKS,
                    MIRESTONE_BRICK_STAIRS,
                    MIRESTONE_BRICK_SLAB,
                    MIRESTONE_BRICK_WALL,
                    OVERGROWN_MIRESTONE_BRICKS,
                    OVERGROWN_MIRESTONE_BRICK_STAIRS,
                    OVERGROWN_MIRESTONE_BRICK_SLAB,
                    OVERGROWN_MIRESTONE_BRICK_WALL,

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

            output.insertBefore(PURPUR_PILLAR, CHISELED_PURPUR);
            output.insertAfter(PURPUR_SLAB,
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

            output.insertAfter(WARPED_BUTTON,
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

            output.insertBefore(AMETHYST_BLOCK,
                    NEBULITE_BLOCK,
                    SHADOLINE_BLOCK,
                    SHADOLINE_BLOCK_STAIRS,
                    SHADOLINE_BLOCK_SLAB,
                    SHADOLINE_BLOCK_WALL,
                    CUT_SHADOLINE,
                    CUT_SHADOLINE_STAIRS,
                    CUT_SHADOLINE_SLAB,
                    CUT_SHADOLINE_WALL,
                    CHISELED_SHADOLINE,
                    SHADOLINE_PILLAR,
                    SHADOLINE_BARS,
                    SHADOLINE_CHAIN
            );
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(output -> {
            if (!EnderscapeConfig.getInstance().includeItemsInVanillaCreativeTabs) return;

            output.insertAfter(END_STONE,
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

            output.insertBefore(RAW_IRON_BLOCK, NEBULITE_ORE, MIRESTONE_NEBULITE_ORE, SHADOLINE_ORE, MIRESTONE_SHADOLINE_ORE);

            output.insertBefore(GLOWSTONE, RAW_SHADOLINE_BLOCK);

            output.insertBefore(MUSHROOM_STEM, VEILED_LOG);
            output.insertBefore(OAK_LEAVES, CELESTIAL_STEM, MURUBLIGHT_STEM);
            output.insertBefore(BROWN_MUSHROOM_BLOCK, VEILED_LEAVES, VEILED_LEAF_PILE, VEILED_VINES);
            output.insertBefore(OAK_SAPLING, CELESTIAL_CAP, MURUBLIGHT_CAP);
            output.insertBefore(SHORT_GRASS, CELESTIAL_CHANTERELLE, MURUBLIGHT_CHANTERELLE, MURUBLIGHT_BRACKET_ITEM);
            output.insertBefore(BROWN_MUSHROOM, VEILED_SAPLING);

            output.insertBefore(VINE,
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

            output.insertAfter(HONEY_BLOCK, DRIFT_JELLY_BLOCK);
            output.insertAfter(JACK_O_LANTERN,
                    PURUBERRY_FLOWER,
                    UNRIPE_PURUBERRY_BLOCK,
                    RIPE_PURUBERRY_BLOCK
            );
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(output -> {
            if (!EnderscapeConfig.getInstance().includeItemsInVanillaCreativeTabs) return;

            output.insertBefore(REDSTONE_TORCH, VOID_TORCH_ITEM);
            output.insertBefore(IRON_CHAIN, VOID_LANTERN, BULB_LANTERN);
            output.insertBefore(END_ROD, SHADOLINE_CHAIN, END_LAMP);
            output.insertBefore(CRYING_OBSIDIAN, POLARIZED_MAGNIA, BLINKLAMP);
            output.insertBefore(ANVIL, VOID_CAMPFIRE);
            output.insertBefore(LECTERN, VEILED_SHELF_ITEM, CELESTIAL_SHELF_ITEM, MURUBLIGHT_SHELF_ITEM);
            output.insertBefore(CHEST, VEILED_SIGN_ITEM, VEILED_HANGING_SIGN_ITEM, CELESTIAL_SIGN_ITEM, CELESTIAL_HANGING_SIGN_ITEM, MURUBLIGHT_SIGN_ITEM, MURUBLIGHT_HANGING_SIGN_ITEM);
            output.insertBefore(WHITE_BED, END_HAVEN_CORE);
            output.insertBefore(ENDER_EYE, getEndVaultInstance());
            output.insertBefore(SKELETON_SKULL, getEndCityBannerInstance(output.getContext().holders().lookupOrThrow(Registries.BANNER_PATTERN)));
        });
        
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(output -> {
            if (!EnderscapeConfig.getInstance().includeItemsInVanillaCreativeTabs) return;

            output.insertBefore(PISTON, BLINKLIGHT);
            output.insertAfter(CAULDRON, BLISTERED_MAGNIA, POLARIZED_MAGNIA, NEBULITE_BLOCK, ALLURING_MAGNIA, REPULSIVE_MAGNIA, PURUBERRY);
            output.insertAfter(REDSTONE_LAMP, BLINKLAMP);
            output.insertAfter(BIG_DRIPLEAF, ALLURING_MAGNIA_SPROUT, REPULSIVE_MAGNIA_SPROUT);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(output -> {
            if (!EnderscapeConfig.getInstance().includeItemsInVanillaCreativeTabs) return;

            output.insertAfter(LEAD, MAGNIA_ATTRACTOR);

            List<ItemStack> mirrors = new ArrayList<>();
            mirrors.add(MIRROR.getDefaultInstance());

            if (EnderscapeConfig.getInstance().includeColoredMirrorsInCreativeTabs) {
                mirrors.addAll(coloredMirrors());
            }

            output.insertAfter(ENDER_EYE, mirrors);

            output.insertBefore(LAVA_BUCKET, RUSTLE_BUCKET);
            output.insertBefore(POWDER_SNOW_BUCKET, VOID_LACHRYMA_BUCKET);

            output.accept(MUSIC_DISC_GLARE);
            output.accept(MUSIC_DISC_DECAY);
            output.accept(MUSIC_DISC_BLISS);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(output -> {
            if (!EnderscapeConfig.getInstance().includeItemsInVanillaCreativeTabs) return;

            RubbleShieldVariant.VARIANTS.forEach(id -> output.insertBefore(LEATHER_HELMET, EnderscapeItems.rubbleShieldVariant(id)));

            output.insertBefore(SHIELD, DAGGER);
            output.insertBefore(TURTLE_HELMET, SHADOLINE_HELMET, SHADOLINE_CHESTPLATE, SHADOLINE_LEGGINGS, SHADOLINE_BOOTS);
            output.insertAfter(TURTLE_HELMET, DRIFT_LEGGINGS);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(output -> {
            if (!EnderscapeConfig.getInstance().includeItemsInVanillaCreativeTabs) return;

            output.insertBefore(CARROT, PURUBERRY, MURUBLIGHT_BRACKET_ITEM);
            output.insertBefore(PUMPKIN_PIE, CHORUS_CAKE_ROLL_ITEM);
            output.insertAfter(HONEY_BOTTLE, DRIFT_JELLY_BOTTLE);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output -> {
            if (!EnderscapeConfig.getInstance().includeItemsInVanillaCreativeTabs) return;

            output.insertBefore(SHULKER_SHELL, RUBBLE_CHITIN);
            output.insertBefore(ECHO_SHARD, RUSTLE_SILK, DRIFT_JELLY_BOTTLE);
            output.insertBefore(EMERALD, RAW_SHADOLINE);
            output.insertBefore(STICK, SHADOLINE_INGOT);
            output.insertBefore(COPPER_INGOT, SHADOLINE_NUGGET);
            output.insertBefore(COPPER_NUGGET, NEBULITE_SHARDS, NEBULITE);
            output.insertAfter(SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, STASIS_ARMOR_TRIM_SMITHING_TEMPLATE);
            output.insertAfter(OMINOUS_TRIAL_KEY, END_CITY_KEY);
            output.insertBefore(ANGLER_POTTERY_SHERD, CRESCENT_BANNER_PATTERN);
        });
        
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS).register(output -> {
            if (!EnderscapeConfig.getInstance().includeItemsInVanillaCreativeTabs) return;

            output.insertBefore(CREAKING_HEART, END_TRIAL_SPAWNER);
            output.insertAfter(ENDERMITE_SPAWN_EGG, RUBBLEMITE_SPAWN_EGG);
            output.insertAfter(SHULKER_SPAWN_EGG, RUSTLE_SPAWN_EGG, DRIFTER_SPAWN_EGG);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.OP_BLOCKS).register(output -> {
            if (!EnderscapeConfig.getInstance().includeItemsInVanillaCreativeTabs) return;

            output.accept(HEALING);
        });

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Enderscape.id("enderscape"), ENDERSCAPE);
    }

    private static List<ItemStack> coloredMirrors() {
        List<ItemStack> stacks = new ArrayList<>();

        for (DyeColor color : List.of(
                DyeColor.WHITE,
                DyeColor.LIGHT_GRAY,
                DyeColor.GRAY,
                DyeColor.BLACK,
                DyeColor.BROWN,
                DyeColor.RED,
                DyeColor.ORANGE,
                DyeColor.YELLOW,
                DyeColor.LIME,
                DyeColor.GREEN,
                DyeColor.CYAN,
                DyeColor.LIGHT_BLUE,
                DyeColor.BLUE,
                DyeColor.PURPLE,
                DyeColor.MAGENTA,
                DyeColor.PINK
        )) {
            ItemStack mirror = MIRROR.getDefaultInstance();
            mirror.set(EnderscapeDataComponents.DYE_COLOR, color);
            stacks.add(mirror);
        }

        return stacks;
    }

    public static ItemStack getEnchantedBook(Holder.Reference<Enchantment> enchantment, int level) {
        return EnchantmentHelper.createBook(new EnchantmentInstance(enchantment, level));
    }

    public static ItemStack getPainting(Holder.Reference<PaintingVariant> painting) {
        ItemStack stack = PAINTING.getDefaultInstance();
        stack.set(DataComponents.PAINTING_VARIANT, painting);
        return stack;
    }
}