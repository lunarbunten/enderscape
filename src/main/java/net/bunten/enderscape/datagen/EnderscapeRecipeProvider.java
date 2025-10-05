package net.bunten.enderscape.datagen;

import com.google.common.collect.ImmutableList;
import net.bunten.enderscape.registry.tag.EnderscapeItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;
import static net.bunten.enderscape.registry.EnderscapeItems.*;
import static net.minecraft.data.recipes.ShapedRecipeBuilder.shaped;
import static net.minecraft.data.recipes.ShapelessRecipeBuilder.shapeless;
import static net.minecraft.world.item.Items.*;

public class EnderscapeRecipeProvider extends FabricRecipeProvider {

    private static final ImmutableList<ItemLike> NEBULITE_SMELTABLES = ImmutableList.of(NEBULITE_ORE, MIRESTONE_NEBULITE_ORE);
    private static final ImmutableList<ItemLike> SHADOLINE_SMELTABLES = ImmutableList.of(SHADOLINE_ORE, MIRESTONE_SHADOLINE_ORE, RAW_SHADOLINE);

    public EnderscapeRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future);
    }

    @Override
    public void buildRecipes(RecipeOutput output) {
        EnderscapeBlockFamilies.getAllFamilies().forEach((family) -> generateRecipes(output, family, FeatureFlagSet.of(FeatureFlags.VANILLA)));

        VanillaRecipeProvider.TrimTemplate stasisTrim = new VanillaRecipeProvider.TrimTemplate(STASIS_ARMOR_TRIM_SMITHING_TEMPLATE, ResourceLocation.withDefaultNamespace(getItemName(STASIS_ARMOR_TRIM_SMITHING_TEMPLATE) + "_smithing_trim"));
        trimSmithing(output, stasisTrim.template(), stasisTrim.id());

        shaped(RecipeCategory.MISC, MUSIC_DISC_BLISS)
                .define('D', MUSIC_DISC_DECAY)
                .define('N', NEBULITE_SHARDS)
                .pattern(" N ")
                .pattern("NDN")
                .pattern(" N ")
                .unlockedBy("has_music_disc_decay", has(MUSIC_DISC_DECAY))
                .save(output);

        shapeless(RecipeCategory.FOOD, DRIFT_JELLY_BOTTLE, 4)
                .requires(DRIFT_JELLY_BLOCK, 1)
                .requires(GLASS_BOTTLE, 4)
                .unlockedBy("has_drift_jelly_block", has(DRIFT_JELLY_BLOCK))
                .save(output);

        shaped(RecipeCategory.MISC, DRIFT_JELLY_BLOCK)
                .define('#', DRIFT_JELLY_BOTTLE)
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_drift_jelly_bottle", has(DRIFT_JELLY_BOTTLE))
                .save(output);

        shaped(RecipeCategory.COMBAT, DRIFT_LEGGINGS)
                .define('N', NEBULITE)
                .define('D', DRIFT_JELLY_BOTTLE)
                .pattern("NNN")
                .pattern("D D")
                .pattern("D D")
                .unlockedBy("has_drift_jelly_bottle", has(DRIFT_JELLY_BOTTLE))
                .save(output);

        rubbleShield(output, END_STONE, END_STONE_RUBBLE_SHIELD);
        rubbleShield(output, VERADITE, VERADITE_RUBBLE_SHIELD);
        rubbleShield(output, MIRESTONE, MIRESTONE_RUBBLE_SHIELD);
        rubbleShield(output, KURODITE, KURODITE_RUBBLE_SHIELD);

        shaped(RecipeCategory.TOOLS, MAGNIA_ATTRACTOR)
                .define('A', ALLURING_MAGNIA_SPROUT)
                .define('R', REPULSIVE_MAGNIA_SPROUT)
                .define('S', SHADOLINE_INGOT)
                .pattern("A R")
                .pattern("SSS")
                .pattern(" S ")
                .unlockedBy("has_magnia_block", has(EnderscapeItemTags.MAGNIA_SPROUTS))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, END_STONE_SLAB, END_STONE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, END_STONE_STAIRS, END_STONE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, END_STONE_WALL, END_STONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_END_STONE, END_STONE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, POLISHED_END_STONE_WALL, END_STONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_END_STONE_SLAB, END_STONE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_END_STONE_STAIRS, END_STONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CHISELED_END_STONE, END_STONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_END_STONE_SLAB, POLISHED_END_STONE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_END_STONE_STAIRS, POLISHED_END_STONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, END_STONE_BRICKS, POLISHED_END_STONE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, POLISHED_END_STONE_WALL, POLISHED_END_STONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, END_STONE_BRICK_SLAB, POLISHED_END_STONE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, END_STONE_BRICK_STAIRS, POLISHED_END_STONE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, END_STONE_BRICK_WALL, POLISHED_END_STONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CHISELED_END_STONE, POLISHED_END_STONE);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MIRESTONE_SLAB, MIRESTONE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MIRESTONE_STAIRS, MIRESTONE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, MIRESTONE_WALL, MIRESTONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_MIRESTONE, MIRESTONE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, POLISHED_MIRESTONE_WALL, MIRESTONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_MIRESTONE_SLAB, MIRESTONE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_MIRESTONE_STAIRS, MIRESTONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CHISELED_MIRESTONE, MIRESTONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICKS, MIRESTONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICK_SLAB, MIRESTONE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICK_STAIRS, MIRESTONE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, MIRESTONE_BRICK_WALL, MIRESTONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_MIRESTONE_SLAB, POLISHED_MIRESTONE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_MIRESTONE_STAIRS, POLISHED_MIRESTONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICKS, POLISHED_MIRESTONE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, POLISHED_MIRESTONE_WALL, POLISHED_MIRESTONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICK_SLAB, POLISHED_MIRESTONE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICK_STAIRS, POLISHED_MIRESTONE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, MIRESTONE_BRICK_WALL, POLISHED_MIRESTONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CHISELED_MIRESTONE, POLISHED_MIRESTONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICK_SLAB, MIRESTONE_BRICKS, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICK_STAIRS, MIRESTONE_BRICKS);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, MIRESTONE_BRICK_WALL, MIRESTONE_BRICKS);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, VERADITE_SLAB, VERADITE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, VERADITE_STAIRS, VERADITE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, VERADITE_WALL, VERADITE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_VERADITE, VERADITE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, POLISHED_VERADITE_WALL, VERADITE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_VERADITE_SLAB, VERADITE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_VERADITE_STAIRS, VERADITE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CHISELED_VERADITE, VERADITE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICKS, VERADITE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICK_SLAB, VERADITE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICK_STAIRS, VERADITE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, VERADITE_BRICK_WALL, VERADITE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_VERADITE_SLAB, POLISHED_VERADITE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_VERADITE_STAIRS, POLISHED_VERADITE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICKS, POLISHED_VERADITE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, POLISHED_VERADITE_WALL, POLISHED_VERADITE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICK_SLAB, POLISHED_VERADITE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICK_STAIRS, POLISHED_VERADITE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, VERADITE_BRICK_WALL, POLISHED_VERADITE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CHISELED_VERADITE, POLISHED_VERADITE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICK_SLAB, VERADITE_BRICKS, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICK_STAIRS, VERADITE_BRICKS);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, VERADITE_BRICK_WALL, VERADITE_BRICKS);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, KURODITE_SLAB, KURODITE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, KURODITE_STAIRS, KURODITE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, KURODITE_WALL, KURODITE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_KURODITE, KURODITE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, POLISHED_KURODITE_WALL, KURODITE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_KURODITE_SLAB, KURODITE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_KURODITE_STAIRS, KURODITE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CHISELED_KURODITE, KURODITE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICKS, KURODITE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICK_SLAB, KURODITE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICK_STAIRS, KURODITE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, KURODITE_BRICK_WALL, KURODITE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_KURODITE_SLAB, POLISHED_KURODITE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_KURODITE_STAIRS, POLISHED_KURODITE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICKS, POLISHED_KURODITE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, POLISHED_KURODITE_WALL, POLISHED_KURODITE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICK_SLAB, POLISHED_KURODITE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICK_STAIRS, POLISHED_KURODITE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, KURODITE_BRICK_WALL, POLISHED_KURODITE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CHISELED_KURODITE, POLISHED_KURODITE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICK_SLAB, KURODITE_BRICKS, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICK_STAIRS, KURODITE_BRICKS);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, KURODITE_BRICK_WALL, KURODITE_BRICKS);

        shaped(RecipeCategory.BUILDING_BLOCKS, ETCHED_ALLURING_MAGNIA, 4)
                .define('#', ALLURING_MAGNIA)
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_alluring_magnia", has(ALLURING_MAGNIA))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ETCHED_ALLURING_MAGNIA, ALLURING_MAGNIA);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, ETCHED_ALLURING_MAGNIA_WALL, ALLURING_MAGNIA);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ETCHED_ALLURING_MAGNIA_SLAB, ALLURING_MAGNIA, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ETCHED_ALLURING_MAGNIA_STAIRS, ALLURING_MAGNIA);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ETCHED_ALLURING_MAGNIA_SLAB, ETCHED_ALLURING_MAGNIA, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ETCHED_ALLURING_MAGNIA_STAIRS, ETCHED_ALLURING_MAGNIA);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, ETCHED_ALLURING_MAGNIA_WALL, ETCHED_ALLURING_MAGNIA);

        shaped(RecipeCategory.BUILDING_BLOCKS, ETCHED_REPULSIVE_MAGNIA, 4)
                .define('#', REPULSIVE_MAGNIA)
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_repulsive_magnia", has(REPULSIVE_MAGNIA))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ETCHED_REPULSIVE_MAGNIA, REPULSIVE_MAGNIA);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, ETCHED_REPULSIVE_MAGNIA_WALL, REPULSIVE_MAGNIA);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ETCHED_REPULSIVE_MAGNIA_SLAB, REPULSIVE_MAGNIA, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ETCHED_REPULSIVE_MAGNIA_STAIRS, REPULSIVE_MAGNIA);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ETCHED_REPULSIVE_MAGNIA_SLAB, ETCHED_REPULSIVE_MAGNIA, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ETCHED_REPULSIVE_MAGNIA_STAIRS, ETCHED_REPULSIVE_MAGNIA);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, ETCHED_REPULSIVE_MAGNIA_WALL, ETCHED_REPULSIVE_MAGNIA);

//        shaped(RecipeCategory.BUILDING_BLOCKS, MAGNIA_RADIO)
//                .define('A', ETCHED_ALLURING_MAGNIA)
//                .define('R', ETCHED_REPULSIVE_MAGNIA)
//                .define('!', ALLURING_MAGNIA_SPROUT)
//                .define('@', REPULSIVE_MAGNIA_SPROUT)
//                .define('N', NEBULITE)
//                .pattern("! @")
//                .pattern("RNR")
//                .pattern("AAA")
//                .unlockedBy("has_any_magnia_sprout", has(EnderscapeItemTags.MAGNIA_SPROUTS))
//                .save(output);
//
        shaped(RecipeCategory.BUILDING_BLOCKS, POLARIZED_MAGNIA)
                .define('B', BLISTERED_MAGNIA)
                .define('S', SHADOLINE_INGOT)
                .define('P', POPPED_CHORUS_FRUIT)
                .define('R', REDSTONE)
                .pattern("SSS")
                .pattern("PBP")
                .pattern("SRS")
                .unlockedBy("has_blistered_magnia", has(BLISTERED_MAGNIA))
                .save(output);

        oreSmelting(output, SHADOLINE_SMELTABLES, RecipeCategory.MISC, SHADOLINE_INGOT, 0.7F, 200, "shadoline_ingot");
        oreBlasting(output, SHADOLINE_SMELTABLES, RecipeCategory.MISC, SHADOLINE_INGOT, 0.7F, 100, "shadoline_ingot");

        nineBlockStorageRecipesWithCustomPacking(RecipeCategory.MISC, SHADOLINE_NUGGET, RecipeCategory.MISC, SHADOLINE_INGOT, "enderscape:shadoline_ingot_from_nuggets", "enderscape:shadoline_ingot");

        shapeless(RecipeCategory.MISC, RAW_SHADOLINE, 9)
                .requires(RAW_SHADOLINE_BLOCK)
                .group("raw_shadoline")
                .unlockedBy("has_raw_shadoline_block", has(RAW_SHADOLINE_BLOCK))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, RAW_SHADOLINE_BLOCK)
                .define('#', RAW_SHADOLINE)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .group("raw_shadoline_block")
                .unlockedBy("has_raw_shadoline", has(RAW_SHADOLINE))
                .save(output);

        shapeless(RecipeCategory.MISC, SHADOLINE_INGOT, 9)
                .requires(SHADOLINE_BLOCK)
                .group("shadoline_ingot")
                .unlockedBy("has_shadoline_block", has(SHADOLINE_BLOCK))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, SHADOLINE_BLOCK)
                .define('#', SHADOLINE_INGOT)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .group("shadoline_block")
                .unlockedBy("has_shadoline_ingot", has(SHADOLINE_INGOT))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, SHADOLINE_BLOCK_SLAB, SHADOLINE_BLOCK, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, SHADOLINE_BLOCK_STAIRS, SHADOLINE_BLOCK);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, SHADOLINE_BLOCK_WALL, SHADOLINE_BLOCK);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CHISELED_SHADOLINE, SHADOLINE_BLOCK, 4);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CUT_SHADOLINE, SHADOLINE_BLOCK, 4);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CUT_SHADOLINE_SLAB, SHADOLINE_BLOCK, 8);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CUT_SHADOLINE_STAIRS, SHADOLINE_BLOCK, 4);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, CUT_SHADOLINE_WALL, SHADOLINE_BLOCK, 4);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CUT_SHADOLINE_SLAB, CUT_SHADOLINE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CUT_SHADOLINE_STAIRS, CUT_SHADOLINE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, CUT_SHADOLINE_WALL, CUT_SHADOLINE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, SHADOLINE_PILLAR, SHADOLINE_BLOCK, 4);

        shaped(RecipeCategory.BUILDING_BLOCKS, SHADOLINE_PILLAR)
                .define('#', SHADOLINE_BLOCK)
                .pattern("#")
                .pattern("#")
                .unlockedBy("has_shadoline_block", has(SHADOLINE_BLOCK))
                .save(output);

        shapeless(RecipeCategory.MISC, NEBULITE)
                .requires(NEBULITE_SHARDS, 4)
                .unlockedBy("has_nebulite_shards", has(NEBULITE_SHARDS))
                .save(output, "enderscape:nebulite_from_shards");

        oreSmelting(output, NEBULITE_SMELTABLES, RecipeCategory.MISC, NEBULITE, 1.0F, 200, "nebulite");
        oreBlasting(output, NEBULITE_SMELTABLES, RecipeCategory.MISC, NEBULITE, 1.0F, 100, "nebulite");

        shapeless(RecipeCategory.MISC, NEBULITE, 9)
                .requires(NEBULITE_BLOCK)
                .group("nebulite")
                .unlockedBy("has_nebulite_block", has(NEBULITE_BLOCK))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, NEBULITE_BLOCK)
                .define('#', NEBULITE)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .group("nebulite_block")
                .unlockedBy("has_nebulite", has(NEBULITE))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CHISELED_PURPUR, PURPUR_BLOCK);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, PURPUR_WALL, PURPUR_BLOCK);

        shapeless(RecipeCategory.BUILDING_BLOCKS, DUSK_PURPUR_BLOCK, 4)
                .requires(POPPED_CHORUS_FRUIT, 2)
                .requires(SHADOLINE_INGOT, 2)
                .unlockedBy("has_popped_chorus_fruit", has(POPPED_CHORUS_FRUIT))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, DUSK_PURPUR_PILLAR)
                .define('#', DUSK_PURPUR_BLOCK)
                .pattern("#")
                .pattern("#")
                .unlockedBy("has_dusk_purpur_block", has(DUSK_PURPUR_BLOCK))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, DUSK_PURPUR_SLAB, DUSK_PURPUR_BLOCK, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, DUSK_PURPUR_STAIRS, DUSK_PURPUR_BLOCK);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, DUSK_PURPUR_WALL, DUSK_PURPUR_BLOCK);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CHISELED_DUSK_PURPUR, DUSK_PURPUR_BLOCK);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, DUSK_PURPUR_PILLAR, DUSK_PURPUR_BLOCK);

        shaped(RecipeCategory.BUILDING_BLOCKS, PURPUR_TILES, 4)
                .define('P', PURPUR_BLOCK)
                .define('D', DUSK_PURPUR_BLOCK)
                .pattern("DP")
                .pattern("PD")
                .unlockedBy("has_purpur_block", has(PURPUR_BLOCK))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, PURPUR_TILE_SLAB, PURPUR_TILES, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, PURPUR_TILE_STAIRS, PURPUR_TILES);

        shaped(RecipeCategory.FOOD, CHORUS_CAKE_ROLL_ITEM)
                .define('C', CHORUS_FRUIT)
                .define('S', SUGAR)
                .define('D', DRIFT_JELLY_BOTTLE)
                .pattern("CCC")
                .pattern("SDS")
                .unlockedBy("has_chorus_fruit", has(CHORUS_FRUIT))
                .save(output);

        shaped(RecipeCategory.DECORATIONS, END_LAMP)
                .define('B', BLAZE_ROD)
                .define('C', POPPED_CHORUS_FRUIT)
                .pattern(" C ")
                .pattern("CBC")
                .pattern(" C ")
                .unlockedBy("has_popped_chorus_fruit", has(POPPED_CHORUS_FRUIT))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, VEILED_LEAF_PILE, 6)
                .define('#', VEILED_LEAVES)
                .pattern("###")
                .unlockedBy("has_veiled_leaves", has(VEILED_LEAVES))
                .save(output);

        shapeless(RecipeCategory.MISC, WHITE_DYE)
                .group("white_dye")
                .requires(WISP_FLOWER, 1)
                .unlockedBy("has_wisp_flower", has(WISP_FLOWER))
                .save(output, "enderscape:white_dye_from_wisp_flower");

        woodFromLogs(output, VEILED_WOOD, VEILED_LOG);
        woodFromLogs(output, STRIPPED_VEILED_WOOD, STRIPPED_VEILED_LOG);
        planksFromLogs(output, VEILED_PLANKS, EnderscapeItemTags.VEILED_LOGS, 4);

        hangingSign(output, VEILED_HANGING_SIGN_ITEM, STRIPPED_VEILED_LOG);

        shapeless(RecipeCategory.MISC, YELLOW_DYE)
                .group("yellow_dye")
                .requires(CELESTIAL_GROWTH, 1)
                .unlockedBy("has_celestial_growth", has(CELESTIAL_GROWTH))
                .save(output, "enderscape:yellow_dye_from_celestial_growth");

        shapeless(RecipeCategory.MISC, CYAN_DYE)
                .group("cyan_dye")
                .requires(BULB_FLOWER, 1)
                .unlockedBy("has_bulb_flower", has(BULB_FLOWER))
                .save(output, "enderscape:cyan_dye_from_bulb_flower");

        shaped(RecipeCategory.DECORATIONS, BULB_LANTERN)
                .define('#', IRON_NUGGET)
                .define('@', BULB_FLOWER)
                .pattern("###")
                .pattern("#@#")
                .pattern("###")
                .unlockedBy("has_bulb_flower", has(BULB_FLOWER))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, CELESTIAL_BRICKS, 2)
                .define('#', CELESTIAL_CAP)
                .define('@', CELESTIAL_GROWTH)
                .pattern("@#")
                .pattern("#@")
                .unlockedBy("has_celestial_cap", has(CELESTIAL_CAP))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CELESTIAL_BRICK_SLAB, CELESTIAL_BRICKS, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CELESTIAL_BRICK_STAIRS, CELESTIAL_BRICKS);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, CELESTIAL_BRICK_WALL, CELESTIAL_BRICKS);

        woodFromLogs(output, CELESTIAL_HYPHAE, CELESTIAL_STEM);
        woodFromLogs(output, STRIPPED_CELESTIAL_HYPHAE, STRIPPED_CELESTIAL_STEM);
        planksFromLogs(output, CELESTIAL_PLANKS, EnderscapeItemTags.CELESTIAL_STEMS, 4);

        hangingSign(output, CELESTIAL_HANGING_SIGN_ITEM, STRIPPED_CELESTIAL_STEM);

        shapeless(RecipeCategory.MISC, PURPLE_DYE)
                .group("purple_dye")
                .requires(CORRUPT_GROWTH, 1)
                .unlockedBy("has_corrupt_growth", has(CORRUPT_GROWTH))
                .save(output, "enderscape:purple_dye_from_corrupt_growth");

        shaped(RecipeCategory.BUILDING_BLOCKS, BLINKLAMP)
                .define('#', POPPED_CHORUS_FRUIT)
                .define('@', BLINKLIGHT)
                .pattern("#@#")
                .pattern("@@@")
                .pattern("#@#")
                .unlockedBy("has_blinklight", has(BLINKLIGHT))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, MURUBLIGHT_BRICKS, 2)
                .define('#', MURUBLIGHT_CAP)
                .define('@', CORRUPT_GROWTH)
                .pattern("@#")
                .pattern("#@")
                .unlockedBy("has_murublight_cap", has(MURUBLIGHT_CAP))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MURUBLIGHT_BRICK_SLAB, MURUBLIGHT_BRICKS, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MURUBLIGHT_BRICK_STAIRS, MURUBLIGHT_BRICKS);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, MURUBLIGHT_BRICK_WALL, MURUBLIGHT_BRICKS);

        woodFromLogs(output, MURUBLIGHT_HYPHAE, MURUBLIGHT_STEM);
        woodFromLogs(output, STRIPPED_MURUBLIGHT_HYPHAE, STRIPPED_MURUBLIGHT_STEM);
        planksFromLogs(output, MURUBLIGHT_PLANKS, EnderscapeItemTags.MURUBLIGHT_STEMS, 4);

        hangingSign(output, MURUBLIGHT_HANGING_SIGN_ITEM, STRIPPED_MURUBLIGHT_STEM);
    }

    private void rubbleShield(RecipeOutput output, ItemLike stone, ItemLike shield) {
        shaped(RecipeCategory.COMBAT, shield)
                .define('C', RUBBLE_CHITIN)
                .define('S', SHADOLINE_INGOT)
                .define('#', stone)
                .pattern("CSC")
                .pattern("C#C")
                .pattern(" C ")
                .unlockedBy("has_rubble_chitin", has(RUBBLE_CHITIN))
                .group("rubble_shield")
                .save(output);
    }

    @Override
    public String getName() {
        return "Recipes";
    }
}