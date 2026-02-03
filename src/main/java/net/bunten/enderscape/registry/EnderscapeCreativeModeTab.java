package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import java.util.Optional;
import java.util.function.Supplier;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;
import static net.bunten.enderscape.registry.EnderscapeItems.*;
import static net.minecraft.world.item.CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;
import static net.minecraft.world.item.Items.*;

@EventBusSubscriber(modid = Enderscape.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class EnderscapeCreativeModeTab {

    public static final Holder<CreativeModeTab> ENDERSCAPE = RegistryHelper.registerForHolder(BuiltInRegistries.CREATIVE_MODE_TAB, Enderscape.id("enderscape"), () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.enderscape")).icon(NEBULITE.get()::getDefaultInstance).displayItems((parameters, output) -> {

        output.accept(ENDERMAN_SPAWN_EGG);
        output.accept(ENDERMITE_SPAWN_EGG);
        output.accept(RUBBLEMITE_SPAWN_EGG.get());
        output.accept(SHULKER_SPAWN_EGG);
        output.accept(RUSTLE_SPAWN_EGG.get());
        output.accept(DRIFTER_SPAWN_EGG.get());

        output.accept(DRAGON_BREATH);
        output.accept(RUBBLE_CHITIN.get());
        output.accept(SHULKER_SHELL);
        output.accept(CRESCENT_BANNER_PATTERN.get());
        output.accept(EYE_ARMOR_TRIM_SMITHING_TEMPLATE);
        output.accept(SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE);
        output.accept(STASIS_ARMOR_TRIM_SMITHING_TEMPLATE.get());

        output.accept(getEnchantedBook(parameters, EnderscapeEnchantments.REBOUND, 1));
        output.accept(getEnchantedBook(parameters, EnderscapeEnchantments.RESONANCE, 3));
        output.accept(getEnchantedBook(parameters, EnderscapeEnchantments.TRANSDIMENSIONAL, 1));

        output.accept(END_PORTAL_FRAME);
        output.accept(ENDER_EYE);
        output.accept(END_CRYSTAL);
        output.accept(DRAGON_EGG);
        output.accept(DRAGON_HEAD);
        output.accept(ENDER_CHEST);
        output.accept(SHULKER_BOX);
        output.accept(getEndCityBannerInstance(parameters.holders().lookupOrThrow(Registries.BANNER_PATTERN)));
        output.accept(getPainting(parameters, EnderscapePaintingVariants.GRAPE_STATIC));
        output.accept(END_TRIAL_SPAWNER.get());
        output.accept(getEndVaultInstance());
        output.accept(END_CITY_KEY.get());

        output.accept(ENDER_PEARL);
        output.accept(SHADOLINE_HELMET.get());
        output.accept(SHADOLINE_CHESTPLATE.get());
        output.accept(SHADOLINE_LEGGINGS.get());
        output.accept(SHADOLINE_BOOTS.get());
        output.accept(END_STONE_RUBBLE_SHIELD.get());
        output.accept(VERADITE_RUBBLE_SHIELD.get());
        output.accept(MIRESTONE_RUBBLE_SHIELD.get());
        output.accept(KURODITE_RUBBLE_SHIELD.get());
        output.accept(RUSTLE_BUCKET.get());
        output.accept(DRIFT_JELLY_BOTTLE.get());
        output.accept(DRIFT_JELLY_BLOCK.get());
        output.accept(DRIFT_LEGGINGS.get());

        parameters.holders().lookup(Registries.POTION).ifPresent(potion -> {
            output.accept(PotionContents.createItemStack(POTION, EnderscapePotions.LOW_GRAVITY));
            output.accept(PotionContents.createItemStack(SPLASH_POTION, EnderscapePotions.LOW_GRAVITY));
            output.accept(PotionContents.createItemStack(LINGERING_POTION, EnderscapePotions.LOW_GRAVITY));
            output.accept(PotionContents.createItemStack(TIPPED_ARROW, EnderscapePotions.LOW_GRAVITY));
        });

        output.accept(DAGGER.get());
        output.accept(MAGNIA_ATTRACTOR.get());
        output.accept(MIRROR.get());
        output.accept(ELYTRA);

        output.accept(MUSIC_DISC_GLARE.get());
        output.accept(MUSIC_DISC_DECAY.get());
        output.accept(MUSIC_DISC_BLISS.get());

        output.accept(END_STONE);
        output.accept(END_STONE_STAIRS.get());
        output.accept(END_STONE_SLAB.get());
        output.accept(END_STONE_WALL.get());
        output.accept(POLISHED_END_STONE.get());
        output.accept(POLISHED_END_STONE_STAIRS.get());
        output.accept(POLISHED_END_STONE_SLAB.get());
        output.accept(POLISHED_END_STONE_WALL.get());
        output.accept(POLISHED_END_STONE_PRESSURE_PLATE.get());
        output.accept(POLISHED_END_STONE_BUTTON.get());
        output.accept(END_STONE_BRICKS);
        output.accept(END_STONE_BRICK_STAIRS);
        output.accept(END_STONE_BRICK_SLAB);
        output.accept(END_STONE_BRICK_WALL);
        output.accept(CHISELED_END_STONE.get());

        output.accept(VERADITE.get());
        output.accept(VERADITE_STAIRS.get());
        output.accept(VERADITE_SLAB.get());
        output.accept(VERADITE_WALL.get());
        output.accept(POLISHED_VERADITE.get());
        output.accept(POLISHED_VERADITE_STAIRS.get());
        output.accept(POLISHED_VERADITE_SLAB.get());
        output.accept(POLISHED_VERADITE_WALL.get());
        output.accept(POLISHED_VERADITE_PRESSURE_PLATE.get());
        output.accept(POLISHED_VERADITE_BUTTON.get());
        output.accept(VERADITE_BRICKS.get());
        output.accept(VERADITE_BRICK_STAIRS.get());
        output.accept(VERADITE_BRICK_SLAB.get());
        output.accept(VERADITE_BRICK_WALL.get());
        output.accept(CHISELED_VERADITE.get());

        output.accept(MIRESTONE.get());
        output.accept(MIRESTONE_STAIRS.get());
        output.accept(MIRESTONE_SLAB.get());
        output.accept(MIRESTONE_WALL.get());
        output.accept(POLISHED_MIRESTONE.get());
        output.accept(POLISHED_MIRESTONE_STAIRS.get());
        output.accept(POLISHED_MIRESTONE_SLAB.get());
        output.accept(POLISHED_MIRESTONE_WALL.get());
        output.accept(POLISHED_MIRESTONE_PRESSURE_PLATE.get());
        output.accept(POLISHED_MIRESTONE_BUTTON.get());
        output.accept(MIRESTONE_BRICKS.get());
        output.accept(MIRESTONE_BRICK_STAIRS.get());
        output.accept(MIRESTONE_BRICK_SLAB.get());
        output.accept(MIRESTONE_BRICK_WALL.get());
        output.accept(CHISELED_MIRESTONE.get());

        output.accept(KURODITE.get());
        output.accept(KURODITE_STAIRS.get());
        output.accept(KURODITE_SLAB.get());
        output.accept(KURODITE_WALL.get());
        output.accept(POLISHED_KURODITE.get());
        output.accept(POLISHED_KURODITE_STAIRS.get());
        output.accept(POLISHED_KURODITE_SLAB.get());
        output.accept(POLISHED_KURODITE_WALL.get());
        output.accept(POLISHED_KURODITE_PRESSURE_PLATE.get());
        output.accept(POLISHED_KURODITE_BUTTON.get());
        output.accept(KURODITE_BRICKS.get());
        output.accept(KURODITE_BRICK_STAIRS.get());
        output.accept(KURODITE_BRICK_SLAB.get());
        output.accept(KURODITE_BRICK_WALL.get());
        output.accept(CHISELED_KURODITE.get());

        output.accept(ALLURING_MAGNIA.get());
        output.accept(ETCHED_ALLURING_MAGNIA.get());
        output.accept(ETCHED_ALLURING_MAGNIA_STAIRS.get());
        output.accept(ETCHED_ALLURING_MAGNIA_SLAB.get());
        output.accept(ETCHED_ALLURING_MAGNIA_WALL.get());
        output.accept(ALLURING_MAGNIA_SPROUT.get());
        output.accept(REPULSIVE_MAGNIA.get());
        output.accept(ETCHED_REPULSIVE_MAGNIA.get());
        output.accept(ETCHED_REPULSIVE_MAGNIA_STAIRS.get());
        output.accept(ETCHED_REPULSIVE_MAGNIA_SLAB.get());
        output.accept(ETCHED_REPULSIVE_MAGNIA_WALL.get());
        output.accept(REPULSIVE_MAGNIA_SPROUT.get());
        output.accept(BLISTERED_MAGNIA.get());
        output.accept(POLARIZED_MAGNIA.get());

        output.accept(VOID_SHALE.get());
        output.accept(VOID_TORCH_ITEM.get());
        output.accept(VOID_LANTERN.get());
        output.accept(VOID_CAMPFIRE.get());

        output.accept(SHADOLINE_ORE.get());
        output.accept(MIRESTONE_SHADOLINE_ORE.get());
        output.accept(RAW_SHADOLINE.get());
        output.accept(RAW_SHADOLINE_BLOCK.get());
        output.accept(SHADOLINE_INGOT.get());
        output.accept(SHADOLINE_NUGGET.get());
        output.accept(SHADOLINE_BLOCK.get());
        output.accept(SHADOLINE_BLOCK_STAIRS.get());
        output.accept(SHADOLINE_BLOCK_SLAB.get());
        output.accept(SHADOLINE_BLOCK_WALL.get());
        output.accept(CUT_SHADOLINE.get());
        output.accept(CUT_SHADOLINE_STAIRS.get());
        output.accept(CUT_SHADOLINE_SLAB.get());
        output.accept(CUT_SHADOLINE_WALL.get());
        output.accept(CHISELED_SHADOLINE.get());
        output.accept(SHADOLINE_PILLAR.get());
        output.accept(SHADOLINE_BARS.get());
        output.accept(SHADOLINE_CHAIN.get());

        output.accept(NEBULITE_ORE.get());
        output.accept(MIRESTONE_NEBULITE_ORE.get());
        output.accept(NEBULITE_SHARDS.get());
        output.accept(NEBULITE.get());
        output.accept(NEBULITE_BLOCK.get());

        output.accept(DRY_END_GROWTH.get());
        output.accept(CHORUS_SPROUTS.get());
        output.accept(CHORUS_PLANT);
        output.accept(CHORUS_FLOWER);
        output.accept(CHORUS_FRUIT);
        output.accept(POPPED_CHORUS_FRUIT);
        output.accept(CHORUS_CAKE_ROLL_ITEM.get());
        output.accept(PURPUR_BLOCK);
        output.accept(PURPUR_STAIRS);
        output.accept(PURPUR_SLAB);
        output.accept(PURPUR_WALL.get());
        output.accept(CHISELED_PURPUR.get());
        output.accept(PURPUR_PILLAR);
        output.accept(DUSK_PURPUR_BLOCK.get());
        output.accept(DUSK_PURPUR_STAIRS.get());
        output.accept(DUSK_PURPUR_SLAB.get());
        output.accept(DUSK_PURPUR_WALL.get());
        output.accept(CHISELED_DUSK_PURPUR.get());
        output.accept(DUSK_PURPUR_PILLAR.get());
        output.accept(PURPUR_TILES.get());
        output.accept(PURPUR_TILE_STAIRS.get());
        output.accept(PURPUR_TILE_SLAB.get());
        output.accept(END_ROD);
        output.accept(END_LAMP.get());

        output.accept(VEILED_END_STONE.get());
        output.accept(WISP_SPROUTS.get());
        output.accept(WISP_GROWTH.get());
        output.accept(WISP_FLOWER.get());
        output.accept(VEILED_SAPLING.get());
        output.accept(VEILED_LEAVES.get());
        output.accept(VEILED_LEAF_PILE.get());
        output.accept(VEILED_VINES.get());

        output.accept(VEILED_LOG.get());
        output.accept(STRIPPED_VEILED_LOG.get());
        output.accept(VEILED_WOOD.get());
        output.accept(STRIPPED_VEILED_WOOD.get());
        output.accept(VEILED_PLANKS.get());
        output.accept(VEILED_STAIRS.get());
        output.accept(VEILED_SLAB.get());
        output.accept(VEILED_FENCE.get());
        output.accept(VEILED_FENCE_GATE.get());
        output.accept(VEILED_DOOR.get());
        output.accept(VEILED_TRAPDOOR.get());
        output.accept(VEILED_PRESSURE_PLATE.get());
        output.accept(VEILED_BUTTON.get());
        output.accept(VEILED_SIGN_ITEM.get());
        output.accept(VEILED_HANGING_SIGN_ITEM.get());

        output.accept(CELESTIAL_OVERGROWTH.get());
        output.accept(CELESTIAL_PATH.get());
        output.accept(CELESTIAL_GROWTH.get());
        output.accept(BULB_FLOWER.get());
        output.accept(BULB_LANTERN.get());
        output.accept(FLANGER_BERRY.get());
        output.accept(FLANGER_BERRY_FLOWER.get());
        output.accept(UNRIPE_FLANGER_BERRY_BLOCK.get());
        output.accept(RIPE_FLANGER_BERRY_BLOCK.get());

        output.accept(CELESTIAL_CHANTERELLE.get());
        output.accept(CELESTIAL_CAP.get());
        output.accept(CELESTIAL_BRICKS.get());
        output.accept(CELESTIAL_BRICK_STAIRS.get());
        output.accept(CELESTIAL_BRICK_SLAB.get());
        output.accept(CELESTIAL_BRICK_WALL.get());

        output.accept(CELESTIAL_STEM.get());
        output.accept(STRIPPED_CELESTIAL_STEM.get());
        output.accept(CELESTIAL_HYPHAE.get());
        output.accept(STRIPPED_CELESTIAL_HYPHAE.get());
        output.accept(CELESTIAL_PLANKS.get());
        output.accept(CELESTIAL_STAIRS.get());
        output.accept(CELESTIAL_SLAB.get());
        output.accept(CELESTIAL_FENCE.get());
        output.accept(CELESTIAL_FENCE_GATE.get());
        output.accept(CELESTIAL_DOOR.get());
        output.accept(CELESTIAL_TRAPDOOR.get());
        output.accept(CELESTIAL_PRESSURE_PLATE.get());
        output.accept(CELESTIAL_BUTTON.get());
        output.accept(CELESTIAL_SIGN_ITEM.get());
        output.accept(CELESTIAL_HANGING_SIGN_ITEM.get());

        output.accept(CORRUPT_OVERGROWTH.get());
        output.accept(CORRUPT_PATH.get());
        output.accept(CORRUPT_GROWTH.get());
        output.accept(BLINKLIGHT.get());
        output.accept(BLINKLAMP.get());

        output.accept(MURUBLIGHT_BRACKET_ITEM.get());
        output.accept(MURUBLIGHT_CHANTERELLE.get());

        output.accept(MURUBLIGHT_CAP.get());
        output.accept(MURUBLIGHT_BRICKS.get());
        output.accept(MURUBLIGHT_BRICK_STAIRS.get());
        output.accept(MURUBLIGHT_BRICK_SLAB.get());
        output.accept(MURUBLIGHT_BRICK_WALL.get());

        output.accept(MURUBLIGHT_STEM.get());
        output.accept(STRIPPED_MURUBLIGHT_STEM.get());
        output.accept(MURUBLIGHT_HYPHAE.get());
        output.accept(STRIPPED_MURUBLIGHT_HYPHAE.get());
        output.accept(MURUBLIGHT_PLANKS.get());
        output.accept(MURUBLIGHT_STAIRS.get());
        output.accept(MURUBLIGHT_SLAB.get());
        output.accept(MURUBLIGHT_FENCE.get());
        output.accept(MURUBLIGHT_FENCE_GATE.get());
        output.accept(MURUBLIGHT_DOOR.get());
        output.accept(MURUBLIGHT_TRAPDOOR.get());
        output.accept(MURUBLIGHT_PRESSURE_PLATE.get());
        output.accept(MURUBLIGHT_BUTTON.get());
        output.accept(MURUBLIGHT_SIGN_ITEM.get());
        output.accept(MURUBLIGHT_HANGING_SIGN_ITEM.get());

    }).build());

    @SubscribeEvent
    public static void modifyCreativeModeTabListener(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {

            addAfter(event, END_STONE,
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

            addAfter(event, END_STONE_BRICK_WALL,
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

            addBefore(event, PURPUR_PILLAR, CHISELED_PURPUR);
            addAfter(event, PURPUR_SLAB,
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

            addAfter(event, WARPED_BUTTON,
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

            addBefore(event, AMETHYST_BLOCK,
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
        } else if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {

            addAfter(event, END_STONE,
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

            addBefore(event, RAW_IRON_BLOCK, NEBULITE_ORE, MIRESTONE_NEBULITE_ORE, SHADOLINE_ORE, MIRESTONE_SHADOLINE_ORE);

            addBefore(event, GLOWSTONE, RAW_SHADOLINE_BLOCK);

            addBefore(event, MUSHROOM_STEM, VEILED_LOG);
            addBefore(event, OAK_LEAVES, CELESTIAL_STEM, MURUBLIGHT_STEM);
            addBefore(event, BROWN_MUSHROOM_BLOCK, VEILED_LEAVES, VEILED_LEAF_PILE, VEILED_VINES);
            addBefore(event, OAK_SAPLING, CELESTIAL_CAP, MURUBLIGHT_CAP);
            addBefore(event, SHORT_GRASS, CELESTIAL_CHANTERELLE, MURUBLIGHT_CHANTERELLE, MURUBLIGHT_BRACKET_ITEM);
            addBefore(event, BROWN_MUSHROOM, VEILED_SAPLING);

            addBefore(event, VINE,
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

            addAfter(event, HONEY_BLOCK, DRIFT_JELLY_BLOCK);
            addAfter(event, JACK_O_LANTERN,
                    FLANGER_BERRY_FLOWER,
                    UNRIPE_FLANGER_BERRY_BLOCK,
                    RIPE_FLANGER_BERRY_BLOCK
            );
        } else if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            addBefore(event, REDSTONE_TORCH, VOID_TORCH_ITEM);
            addBefore(event, CHAIN, VOID_LANTERN, BULB_LANTERN);
            addBefore(event, END_ROD, SHADOLINE_CHAIN, END_LAMP);
            addBefore(event, CRYING_OBSIDIAN, POLARIZED_MAGNIA, BLINKLAMP);
            addBefore(event, ANVIL, VOID_CAMPFIRE);
            addBefore(event, CHEST, VEILED_SIGN_ITEM, VEILED_HANGING_SIGN_ITEM, CELESTIAL_SIGN_ITEM, CELESTIAL_HANGING_SIGN_ITEM, MURUBLIGHT_SIGN_ITEM, MURUBLIGHT_HANGING_SIGN_ITEM);
            event.insertBefore(INFESTED_STONE.getDefaultInstance(), getEndVaultInstance(), PARENT_AND_SEARCH_TABS);
            event.insertBefore(SKELETON_SKULL.getDefaultInstance(), getEndCityBannerInstance(event.getParameters().holders().lookupOrThrow(Registries.BANNER_PATTERN)), PARENT_AND_SEARCH_TABS);
        } else if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            addBefore(event, PISTON, BLINKLIGHT);
            addAfter(event, CAULDRON, BLISTERED_MAGNIA, POLARIZED_MAGNIA, NEBULITE_BLOCK, ALLURING_MAGNIA, REPULSIVE_MAGNIA, FLANGER_BERRY);
            addAfter(event, REDSTONE_LAMP, BLINKLAMP);
            addAfter(event, BIG_DRIPLEAF, ALLURING_MAGNIA_SPROUT, REPULSIVE_MAGNIA_SPROUT);
        } else if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            addAfter(event, LEAD, MAGNIA_ATTRACTOR);
            addAfter(event, ELYTRA, MIRROR);
            addBefore(event, LAVA_BUCKET, RUSTLE_BUCKET);
            addAfter(event, MUSIC_DISC_PIGSTEP, MUSIC_DISC_GLARE, MUSIC_DISC_DECAY, MUSIC_DISC_BLISS);
        } else if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            addBefore(event, SHIELD, DAGGER);
            addBefore(event, LEATHER_HELMET, END_STONE_RUBBLE_SHIELD, VERADITE_RUBBLE_SHIELD, MIRESTONE_RUBBLE_SHIELD, KURODITE_RUBBLE_SHIELD);
            addBefore(event, TURTLE_HELMET, SHADOLINE_HELMET, SHADOLINE_CHESTPLATE, SHADOLINE_LEGGINGS, SHADOLINE_BOOTS);
            addAfter(event, TURTLE_HELMET, DRIFT_LEGGINGS);
        } else if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            addBefore(event, CARROT, FLANGER_BERRY, MURUBLIGHT_BRACKET_ITEM);
            addBefore(event, PUMPKIN_PIE, CHORUS_CAKE_ROLL_ITEM);
            addAfter(event, HONEY_BOTTLE, DRIFT_JELLY_BOTTLE);
        } else if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            addBefore(event, SHULKER_SHELL, RUBBLE_CHITIN);
            addBefore(event, ECHO_SHARD, DRIFT_JELLY_BOTTLE);
            addBefore(event, EMERALD, RAW_SHADOLINE);
            addBefore(event, STICK, NEBULITE, SHADOLINE_INGOT);
            addBefore(event, IRON_INGOT, SHADOLINE_NUGGET);
            addBefore(event, QUARTZ, NEBULITE_SHARDS);
            addAfter(event, SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, STASIS_ARMOR_TRIM_SMITHING_TEMPLATE);
            addAfter(event, OMINOUS_TRIAL_KEY, END_CITY_KEY);
            addBefore(event, ANGLER_POTTERY_SHERD, CRESCENT_BANNER_PATTERN);
        } else if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            addBefore(event, ALLAY_SPAWN_EGG, END_TRIAL_SPAWNER);
            addAfter(event, DONKEY_SPAWN_EGG, DRIFTER_SPAWN_EGG);
            addAfter(event, RAVAGER_SPAWN_EGG, RUBBLEMITE_SPAWN_EGG, RUSTLE_SPAWN_EGG);
        } else if (event.getTabKey() == CreativeModeTabs.OP_BLOCKS) {
            event.accept(HEALING.get().asItem().getDefaultInstance());
        }
    }

    @SafeVarargs
    public static void addBefore(BuildCreativeModeTabContentsEvent event, Item target, Supplier<? extends ItemLike>... toAdd) {
        for (int i = toAdd.length - 1; i >= 0; i--) {
            event.insertBefore(target.getDefaultInstance(), toAdd[i].get().asItem().getDefaultInstance(), PARENT_AND_SEARCH_TABS);
        }
    }

    @SafeVarargs
    public static void addAfter(BuildCreativeModeTabContentsEvent event, Item target, Supplier<? extends ItemLike>... toAdd) {
        for (int i = toAdd.length - 1; i >= 0; i--) {
            event.insertAfter(target.getDefaultInstance(), toAdd[i].get().asItem().getDefaultInstance(), PARENT_AND_SEARCH_TABS);
        }
    }

    public static ItemStack getEnchantedBook(CreativeModeTab.ItemDisplayParameters parameters, ResourceKey<Enchantment> enchantment, int level) {
        Optional<HolderLookup.RegistryLookup<Enchantment>> lookup = parameters.holders().lookup(Registries.ENCHANTMENT);
        if (lookup.isPresent()) {
            HolderLookup.RegistryLookup<Enchantment> lookup1 = lookup.get();
            Optional<Holder.Reference<Enchantment>> enchantmentReference = lookup1.get(enchantment);
            if (enchantmentReference.isPresent()) {
                Holder.Reference<Enchantment> reference = enchantmentReference.get();
                return EnchantedBookItem.createForEnchantment(new EnchantmentInstance(reference, reference.value().getMaxLevel()));
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getPainting(CreativeModeTab.ItemDisplayParameters parameters, ResourceKey<PaintingVariant> painting) {
        ItemStack stack = PAINTING.getDefaultInstance();

        Holder.Reference<PaintingVariant> reference = parameters.holders().lookupOrThrow(Registries.PAINTING_VARIANT).getOrThrow(painting);
        CustomData data = CustomData.EMPTY.update(parameters.holders().createSerializationContext(NbtOps.INSTANCE), Painting.VARIANT_MAP_CODEC, reference).getOrThrow().update(compoundTag -> compoundTag.putString("id", "minecraft:painting"));

        stack.set(DataComponents.ENTITY_DATA, data);

        return stack;
    }
}