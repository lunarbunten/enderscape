package net.penumbra.enderscape.datagen.recipe;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.recipes.*;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.penumbra.enderscape.block.EnderscapeBlockFamilies;
import net.penumbra.enderscape.item.component.RubbleShieldVariant;
import net.penumbra.enderscape.item.crafting.*;
import net.penumbra.enderscape.item.crafting.value.RustleRecipeEffects;
import net.penumbra.enderscape.registry.component.EnderscapeDataComponents;
import net.penumbra.enderscape.registry.item.EnderscapeTrimPatterns;
import net.penumbra.enderscape.registry.sound.EnderscapeEntitySounds;
import net.penumbra.enderscape.registry.tag.EnderscapeItemTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static net.minecraft.data.recipes.RecipeProvider.getItemName;
import static net.minecraft.world.item.Items.*;
import static net.penumbra.enderscape.registry.block.EnderscapeBlocks.*;
import static net.penumbra.enderscape.registry.item.EnderscapeItems.*;

public class EnderscapeRecipeProvider extends FabricRecipeProvider {

    private static final ImmutableList<ItemLike> NEBULITE_SMELTABLES = ImmutableList.of(NEBULITE_ORE, MIRESTONE_NEBULITE_ORE);
    private static final ImmutableList<ItemLike> SHADOLINE_SMELTABLES = ImmutableList.of(SHADOLINE_ORE, MIRESTONE_SHADOLINE_ORE, RAW_SHADOLINE);

    public EnderscapeRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future);
    }

    public static Stream<VanillaRecipeProvider.TrimTemplate> smithingTrims() {
        return Stream.of(
                        Pair.of(STASIS_ARMOR_TRIM_SMITHING_TEMPLATE, EnderscapeTrimPatterns.STASIS)
                )
                .map(pair -> {
                    Item item = pair.getFirst();
                    ResourceKey<TrimPattern> pattern = pair.getSecond();
                    ResourceKey<Recipe<?>> recipe = ResourceKey.create(Registries.RECIPE, Identifier.withDefaultNamespace(getItemName(item) + "_smithing_trim"));
                    return new VanillaRecipeProvider.TrimTemplate(item, pattern, recipe);
                });
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider lookup, RecipeOutput output) {
        return new RecipeProvider(lookup, output) {

            @Override
            public void buildRecipes() {
                EnderscapeBlockFamilies.getAllFamilies().forEach((family) -> generateFamilyRecipes(family, FeatureFlagSet.of(FeatureFlags.VANILLA)));
                smithingTrims().forEach(trimTemplate -> trimSmithing(trimTemplate.template(), trimTemplate.patternId(), trimTemplate.recipeId()));

                SuspiciousEffectHolder holder = SuspiciousEffectHolder.tryGet(BULB_FLOWER.asItem());
                if (holder != null) suspiciousStew(BULB_FLOWER.asItem(), holder);

                SpecialRecipeBuilder.special(MirrorDyingRecipe::new).save(output, "mirror_dying");
                SpecialRecipeBuilder.special(ToolFuelingRecipe::new).save(output, "tool_fueling");

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

                shaped(RecipeCategory.COMBAT, DAGGER)
                        .define('X', SHADOLINE_INGOT)
                        .define('S', STICK)
                        .pattern(" X")
                        .pattern("XX")
                        .pattern("S ")
                        .unlockedBy("has_shadoline_ingot", has(SHADOLINE_INGOT))
                        .save(output);

                shaped(RecipeCategory.COMBAT, SHADOLINE_HELMET)
                        .define('X', SHADOLINE_INGOT)
                        .define('C', RUBBLE_CHITIN)
                        .pattern("XXX")
                        .pattern("C C")
                        .unlockedBy("has_shadoline_ingot", has(SHADOLINE_INGOT))
                        .save(output);

                shaped(RecipeCategory.COMBAT, SHADOLINE_CHESTPLATE)
                        .define('X', SHADOLINE_INGOT)
                        .define('C', RUBBLE_CHITIN)
                        .pattern("X X")
                        .pattern("XXX")
                        .pattern("CCC")
                        .unlockedBy("has_shadoline_ingot", has(SHADOLINE_INGOT))
                        .save(output);

                shaped(RecipeCategory.COMBAT, SHADOLINE_LEGGINGS)
                        .define('X', SHADOLINE_INGOT)
                        .define('C', RUBBLE_CHITIN)
                        .pattern("XXX")
                        .pattern("X X")
                        .pattern("C C")
                        .unlockedBy("has_shadoline_ingot", has(SHADOLINE_INGOT))
                        .save(output);

                shaped(RecipeCategory.COMBAT, SHADOLINE_BOOTS)
                        .define('X', SHADOLINE_INGOT)
                        .define('C', RUBBLE_CHITIN)
                        .pattern("C C")
                        .pattern("X X")
                        .unlockedBy("has_shadoline_ingot", has(SHADOLINE_INGOT))
                        .save(output);

                SimpleCookingRecipeBuilder.smelting(
                                Ingredient.of(
                                        SHADOLINE_HELMET,
                                        SHADOLINE_CHESTPLATE,
                                        SHADOLINE_LEGGINGS,
                                        SHADOLINE_BOOTS
                                ),
                                RecipeCategory.MISC,
                                CookingBookCategory.MISC,
                                SHADOLINE_NUGGET,
                                0.1F,
                                200
                        )
                        .unlockedBy("has_shadoline_helmet", has(SHADOLINE_HELMET))
                        .unlockedBy("has_shadoline_chestplate", has(SHADOLINE_CHESTPLATE))
                        .unlockedBy("has_shadoline_leggings", has(SHADOLINE_LEGGINGS))
                        .unlockedBy("has_shadoline_boots", has(SHADOLINE_BOOTS))
                        .save(output, getSmeltingRecipeName(SHADOLINE_NUGGET));

                SimpleCookingRecipeBuilder.blasting(
                                Ingredient.of(
                                        SHADOLINE_HELMET,
                                        SHADOLINE_CHESTPLATE,
                                        SHADOLINE_LEGGINGS,
                                        SHADOLINE_BOOTS
                                ),
                                RecipeCategory.MISC,
                                CookingBookCategory.MISC,
                                SHADOLINE_NUGGET,
                                0.1F,
                                100
                        )
                        .unlockedBy("has_shadoline_helmet", has(SHADOLINE_HELMET))
                        .unlockedBy("has_shadoline_chestplate", has(SHADOLINE_CHESTPLATE))
                        .unlockedBy("has_shadoline_leggings", has(SHADOLINE_LEGGINGS))
                        .unlockedBy("has_shadoline_boots", has(SHADOLINE_BOOTS))
                        .save(output, getBlastingRecipeName(SHADOLINE_NUGGET));

                shaped(RecipeCategory.DECORATIONS, SHADOLINE_CHAIN)
                        .define('I', SHADOLINE_INGOT)
                        .define('N', SHADOLINE_NUGGET)
                        .pattern("N")
                        .pattern("I")
                        .pattern("N")
                        .unlockedBy("has_shadoline_nugget", has(SHADOLINE_NUGGET))
                        .unlockedBy("has_shadoline_ingot", has(SHADOLINE_INGOT))
                        .save(output);

                shaped(RecipeCategory.DECORATIONS, SHADOLINE_BARS, 16)
                        .define('#', SHADOLINE_INGOT)
                        .pattern("###")
                        .pattern("###")
                        .unlockedBy("has_shadoline_ingot", has(SHADOLINE_INGOT))
                        .save(output);

                shaped(RecipeCategory.DECORATIONS, VOID_TORCH_ITEM, 4)
                        .define('V', VOID_SHALE)
                        .define('#', STICK)
                        .pattern("V")
                        .pattern("#")
                        .unlockedBy("has_void_shale", has(VOID_SHALE))
                        .save(output);

                shaped(RecipeCategory.DECORATIONS, VOID_LANTERN)
                        .define('#', SHADOLINE_NUGGET)
                        .define('@', VOID_TORCH_ITEM)
                        .pattern("###")
                        .pattern("#@#")
                        .pattern("###")
                        .unlockedBy("has_void_shale", has(VOID_SHALE))
                        .save(output);

                shaped(RecipeCategory.DECORATIONS, VOID_CAMPFIRE)
                        .define('L', ItemTags.LOGS)
                        .define('S', STICK)
                        .define('V', VOID_SHALE)
                        .pattern(" S ")
                        .pattern("SVS")
                        .pattern("LLL")
                        .unlockedBy("has_void_shale", has(VOID_SHALE))
                        .save(output);

                rubbleShield(RubbleShieldVariant.END_STONE, END_STONE);
                rubbleShield(RubbleShieldVariant.MIRESTONE, MIRESTONE);
                rubbleShield(RubbleShieldVariant.VERADITE, VERADITE);
                rubbleShield(RubbleShieldVariant.KURODITE, KURODITE);

                shaped(RecipeCategory.TOOLS, MAGNIA_ATTRACTOR)
                        .define('A', ALLURING_MAGNIA_SPROUT)
                        .define('R', REPULSIVE_MAGNIA_SPROUT)
                        .define('S', SHADOLINE_INGOT)
                        .pattern("A R")
                        .pattern("SSS")
                        .pattern(" S ")
                        .unlockedBy("has_any_magnia_sprout", has(EnderscapeItemTags.MAGNIA_SPROUTS))
                        .save(output);

                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, END_STONE_SLAB, END_STONE, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, END_STONE_STAIRS, END_STONE);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, END_STONE_WALL, END_STONE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, POLISHED_END_STONE, END_STONE);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, POLISHED_END_STONE_WALL, END_STONE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, POLISHED_END_STONE_SLAB, END_STONE, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, POLISHED_END_STONE_STAIRS, END_STONE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, CHISELED_END_STONE, END_STONE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, POLISHED_END_STONE_SLAB, POLISHED_END_STONE, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, POLISHED_END_STONE_STAIRS, POLISHED_END_STONE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, END_STONE_BRICKS, POLISHED_END_STONE);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, POLISHED_END_STONE_WALL, POLISHED_END_STONE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, END_STONE_BRICK_SLAB, POLISHED_END_STONE, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, END_STONE_BRICK_STAIRS, POLISHED_END_STONE);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, END_STONE_BRICK_WALL, POLISHED_END_STONE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, CHISELED_END_STONE, POLISHED_END_STONE);

                shapeless(RecipeCategory.BUILDING_BLOCKS, OVERGROWN_END_STONE_BRICKS)
                        .requires(END_STONE_BRICKS)
                        .requires(CELESTIAL_GROWTH)
                        .unlockedBy("has_celestial_growth", has(CELESTIAL_GROWTH))
                        .save(output, getConversionRecipeName(OVERGROWN_END_STONE_BRICKS, CELESTIAL_GROWTH));

                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, OVERGROWN_END_STONE_BRICK_SLAB, OVERGROWN_END_STONE_BRICKS, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, OVERGROWN_END_STONE_BRICK_STAIRS, OVERGROWN_END_STONE_BRICKS);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, OVERGROWN_END_STONE_BRICK_WALL, OVERGROWN_END_STONE_BRICKS);

                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, MIRESTONE_SLAB, MIRESTONE, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, MIRESTONE_STAIRS, MIRESTONE);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, MIRESTONE_WALL, MIRESTONE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, POLISHED_MIRESTONE, MIRESTONE);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, POLISHED_MIRESTONE_WALL, MIRESTONE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, POLISHED_MIRESTONE_SLAB, MIRESTONE, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, POLISHED_MIRESTONE_STAIRS, MIRESTONE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, CHISELED_MIRESTONE, MIRESTONE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICKS, MIRESTONE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICK_SLAB, MIRESTONE, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICK_STAIRS, MIRESTONE);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, MIRESTONE_BRICK_WALL, MIRESTONE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, POLISHED_MIRESTONE_SLAB, POLISHED_MIRESTONE, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, POLISHED_MIRESTONE_STAIRS, POLISHED_MIRESTONE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICKS, POLISHED_MIRESTONE);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, POLISHED_MIRESTONE_WALL, POLISHED_MIRESTONE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICK_SLAB, POLISHED_MIRESTONE, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICK_STAIRS, POLISHED_MIRESTONE);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, MIRESTONE_BRICK_WALL, POLISHED_MIRESTONE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, CHISELED_MIRESTONE, POLISHED_MIRESTONE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICK_SLAB, MIRESTONE_BRICKS, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICK_STAIRS, MIRESTONE_BRICKS);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, MIRESTONE_BRICK_WALL, MIRESTONE_BRICKS);

                shapeless(RecipeCategory.BUILDING_BLOCKS, OVERGROWN_MIRESTONE_BRICKS)
                        .requires(MIRESTONE_BRICKS)
                        .requires(CORRUPT_GROWTH)
                        .unlockedBy("has_corrupt_growth", has(CORRUPT_GROWTH))
                        .save(output, getConversionRecipeName(OVERGROWN_MIRESTONE_BRICKS, CORRUPT_GROWTH));

                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, OVERGROWN_MIRESTONE_BRICK_SLAB, OVERGROWN_MIRESTONE_BRICKS, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, OVERGROWN_MIRESTONE_BRICK_STAIRS, OVERGROWN_MIRESTONE_BRICKS);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, OVERGROWN_MIRESTONE_BRICK_WALL, OVERGROWN_MIRESTONE_BRICKS);

                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, VERADITE_SLAB, VERADITE, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, VERADITE_STAIRS, VERADITE);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, VERADITE_WALL, VERADITE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, POLISHED_VERADITE, VERADITE);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, POLISHED_VERADITE_WALL, VERADITE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, POLISHED_VERADITE_SLAB, VERADITE, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, POLISHED_VERADITE_STAIRS, VERADITE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, CHISELED_VERADITE, VERADITE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICKS, VERADITE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICK_SLAB, VERADITE, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICK_STAIRS, VERADITE);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, VERADITE_BRICK_WALL, VERADITE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, POLISHED_VERADITE_SLAB, POLISHED_VERADITE, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, POLISHED_VERADITE_STAIRS, POLISHED_VERADITE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICKS, POLISHED_VERADITE);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, POLISHED_VERADITE_WALL, POLISHED_VERADITE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICK_SLAB, POLISHED_VERADITE, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICK_STAIRS, POLISHED_VERADITE);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, VERADITE_BRICK_WALL, POLISHED_VERADITE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, CHISELED_VERADITE, POLISHED_VERADITE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICK_SLAB, VERADITE_BRICKS, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICK_STAIRS, VERADITE_BRICKS);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, VERADITE_BRICK_WALL, VERADITE_BRICKS);

                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, KURODITE_SLAB, KURODITE, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, KURODITE_STAIRS, KURODITE);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, KURODITE_WALL, KURODITE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, POLISHED_KURODITE, KURODITE);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, POLISHED_KURODITE_WALL, KURODITE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, POLISHED_KURODITE_SLAB, KURODITE, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, POLISHED_KURODITE_STAIRS, KURODITE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, CHISELED_KURODITE, KURODITE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICKS, KURODITE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICK_SLAB, KURODITE, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICK_STAIRS, KURODITE);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, KURODITE_BRICK_WALL, KURODITE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, POLISHED_KURODITE_SLAB, POLISHED_KURODITE, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, POLISHED_KURODITE_STAIRS, POLISHED_KURODITE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICKS, POLISHED_KURODITE);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, POLISHED_KURODITE_WALL, POLISHED_KURODITE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICK_SLAB, POLISHED_KURODITE, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICK_STAIRS, POLISHED_KURODITE);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, KURODITE_BRICK_WALL, POLISHED_KURODITE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, CHISELED_KURODITE, POLISHED_KURODITE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICK_SLAB, KURODITE_BRICKS, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICK_STAIRS, KURODITE_BRICKS);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, KURODITE_BRICK_WALL, KURODITE_BRICKS);

                shaped(RecipeCategory.BUILDING_BLOCKS, ETCHED_ALLURING_MAGNIA, 4)
                        .define('#', ALLURING_MAGNIA)
                        .pattern("##")
                        .pattern("##")
                        .unlockedBy("has_alluring_magnia", has(ALLURING_MAGNIA))
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS, ETCHED_REPULSIVE_MAGNIA, 4)
                        .define('#', REPULSIVE_MAGNIA)
                        .pattern("##")
                        .pattern("##")
                        .unlockedBy("has_repulsive_magnia", has(REPULSIVE_MAGNIA))
                        .save(output);

//                shaped(RecipeCategory.BUILDING_BLOCKS, MAGNIA_RADIO)
//                        .define('A', ETCHED_ALLURING_MAGNIA)
//                        .define('R', ETCHED_REPULSIVE_MAGNIA)
//                        .define('!', ALLURING_MAGNIA_SPROUT)
//                        .define('@', REPULSIVE_MAGNIA_SPROUT)
//                        .define('N', NEBULITE)
//                        .pattern("! @")
//                        .pattern("RNR")
//                        .pattern("AAA")
//                        .unlockedBy("has_any_magnia_sprout", has(EnderscapeItemTags.MAGNIA_SPROUTS))
//                        .save(output);

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

                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, ETCHED_ALLURING_MAGNIA, ALLURING_MAGNIA);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, ETCHED_REPULSIVE_MAGNIA, REPULSIVE_MAGNIA);

                stonecutterResultFromBase(RecipeCategory.DECORATIONS, ETCHED_ALLURING_MAGNIA_STAIRS, ETCHED_ALLURING_MAGNIA);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, ETCHED_ALLURING_MAGNIA_SLAB, ETCHED_ALLURING_MAGNIA);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, ETCHED_ALLURING_MAGNIA_WALL, ETCHED_ALLURING_MAGNIA);

                stonecutterResultFromBase(RecipeCategory.DECORATIONS, ETCHED_REPULSIVE_MAGNIA_STAIRS, ETCHED_REPULSIVE_MAGNIA);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, ETCHED_REPULSIVE_MAGNIA_SLAB, ETCHED_REPULSIVE_MAGNIA);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, ETCHED_REPULSIVE_MAGNIA_WALL, ETCHED_REPULSIVE_MAGNIA);

                oreSmelting(SHADOLINE_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.MISC, SHADOLINE_INGOT, 0.7F, 200, "shadoline_ingot");
                oreBlasting(SHADOLINE_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.MISC, SHADOLINE_INGOT, 0.7F, 100, "shadoline_ingot");

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

                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, SHADOLINE_BLOCK_SLAB, SHADOLINE_BLOCK, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, SHADOLINE_BLOCK_STAIRS, SHADOLINE_BLOCK);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, SHADOLINE_BLOCK_WALL, SHADOLINE_BLOCK);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, CHISELED_SHADOLINE, SHADOLINE_BLOCK, 4);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, CUT_SHADOLINE, SHADOLINE_BLOCK, 4);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, CUT_SHADOLINE_SLAB, SHADOLINE_BLOCK, 8);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, CUT_SHADOLINE_STAIRS, SHADOLINE_BLOCK, 4);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, CUT_SHADOLINE_WALL, SHADOLINE_BLOCK, 4);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, CUT_SHADOLINE_SLAB, CUT_SHADOLINE, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, CUT_SHADOLINE_STAIRS, CUT_SHADOLINE);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, CUT_SHADOLINE_WALL, CUT_SHADOLINE);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, SHADOLINE_PILLAR, SHADOLINE_BLOCK, 4);

                shaped(RecipeCategory.BUILDING_BLOCKS, SHADOLINE_PILLAR)
                        .define('#', SHADOLINE_BLOCK)
                        .pattern("#")
                        .pattern("#")
                        .unlockedBy("has_shadoline_block", has(SHADOLINE_BLOCK))
                        .save(output);

                shapeless(RecipeCategory.MISC, NEBULITE)
                        .requires(NEBULITE_SHARDS, 4)
                        .unlockedBy("has_nebulite_shards", has(NEBULITE_SHARDS))
                        .group("nebulite")
                        .save(output, "enderscape:nebulite_from_shards");

                oreSmelting(NEBULITE_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.MISC, NEBULITE, 1.0F, 200, "nebulite");
                oreBlasting(NEBULITE_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.MISC, NEBULITE, 1.0F, 100, "nebulite");

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

                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, CHISELED_PURPUR, PURPUR_BLOCK);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, PURPUR_WALL, PURPUR_BLOCK);

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

                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, DUSK_PURPUR_SLAB, DUSK_PURPUR_BLOCK, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, DUSK_PURPUR_STAIRS, DUSK_PURPUR_BLOCK);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, DUSK_PURPUR_WALL, DUSK_PURPUR_BLOCK);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, CHISELED_DUSK_PURPUR, DUSK_PURPUR_BLOCK);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, DUSK_PURPUR_PILLAR, DUSK_PURPUR_BLOCK);

                shaped(RecipeCategory.BUILDING_BLOCKS, PURPUR_TILES, 4)
                        .define('P', PURPUR_BLOCK)
                        .define('D', DUSK_PURPUR_BLOCK)
                        .pattern("DP")
                        .pattern("PD")
                        .unlockedBy("has_purpur_block", has(PURPUR_BLOCK))
                        .save(output);

                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, PURPUR_TILE_SLAB, PURPUR_TILES, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, PURPUR_TILE_STAIRS, PURPUR_TILES);

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

                shapeless(RecipeCategory.MISC, DYE.white())
                        .group("white_dye")
                        .requires(WISP_FLOWER, 1)
                        .unlockedBy("has_wisp_flower", has(WISP_FLOWER))
                        .save(output, "enderscape:white_dye_from_wisp_flower");

                woodFromLogs(VEILED_WOOD, VEILED_LOG);
                woodFromLogs(STRIPPED_VEILED_WOOD, STRIPPED_VEILED_LOG);
                planksFromLogs(VEILED_PLANKS, EnderscapeItemTags.VEILED_LOGS, 4);

                shelf(VEILED_SHELF_ITEM, STRIPPED_VEILED_LOG);

                shapeless(RecipeCategory.MISC, DYE.yellow())
                        .group("yellow_dye")
                        .requires(CELESTIAL_GROWTH, 1)
                        .unlockedBy("has_celestial_growth", has(CELESTIAL_GROWTH))
                        .save(output, "enderscape:yellow_dye_from_celestial_growth");

                shapeless(RecipeCategory.MISC, DYE.cyan())
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

                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, CELESTIAL_BRICK_SLAB, CELESTIAL_BRICKS, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, CELESTIAL_BRICK_STAIRS, CELESTIAL_BRICKS);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, CELESTIAL_BRICK_WALL, CELESTIAL_BRICKS);

                woodFromLogs(CELESTIAL_HYPHAE, CELESTIAL_STEM);
                woodFromLogs(STRIPPED_CELESTIAL_HYPHAE, STRIPPED_CELESTIAL_STEM);
                planksFromLogs(CELESTIAL_PLANKS, EnderscapeItemTags.CELESTIAL_STEMS, 4);

                shelf(CELESTIAL_SHELF_ITEM, STRIPPED_CELESTIAL_STEM);

                shapeless(RecipeCategory.MISC, DYE.purple())
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

                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, MURUBLIGHT_BRICK_SLAB, MURUBLIGHT_BRICKS, 2);
                stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, MURUBLIGHT_BRICK_STAIRS, MURUBLIGHT_BRICKS);
                stonecutterResultFromBase(RecipeCategory.DECORATIONS, MURUBLIGHT_BRICK_WALL, MURUBLIGHT_BRICKS);

                woodFromLogs(MURUBLIGHT_HYPHAE, MURUBLIGHT_STEM);
                woodFromLogs(STRIPPED_MURUBLIGHT_HYPHAE, STRIPPED_MURUBLIGHT_STEM);
                planksFromLogs(MURUBLIGHT_PLANKS, EnderscapeItemTags.MURUBLIGHT_STEMS, 4);

                shelf(MURUBLIGHT_SHELF_ITEM, STRIPPED_MURUBLIGHT_STEM);

                simpleFamilyCorruption(EnderscapeBlockFamilies.VERADITE, EnderscapeBlockFamilies.KURODITE);
                simpleFamilyCorruption(EnderscapeBlockFamilies.VERADITE_BRICKS, EnderscapeBlockFamilies.KURODITE_BRICKS);
                simpleFamilyCorruption(EnderscapeBlockFamilies.POLISHED_VERADITE, EnderscapeBlockFamilies.POLISHED_KURODITE);

                simpleFamilyCorruption(EnderscapeBlockFamilies.CELESTIAL_PLANKS, EnderscapeBlockFamilies.MURUBLIGHT_PLANKS);
                simpleFamilyCorruption(EnderscapeBlockFamilies.CELESTIAL_BRICKS, EnderscapeBlockFamilies.MURUBLIGHT_BRICKS);

                simpleCorruption(CELESTIAL_STEM, MURUBLIGHT_STEM);
                simpleCorruption(STRIPPED_CELESTIAL_STEM, STRIPPED_MURUBLIGHT_STEM);
                simpleCorruption(CELESTIAL_HYPHAE, MURUBLIGHT_HYPHAE);
                simpleCorruption(STRIPPED_CELESTIAL_HYPHAE, STRIPPED_MURUBLIGHT_HYPHAE);

                simpleCorruption(CELESTIAL_CAP, MURUBLIGHT_CAP);
                simpleCorruption(CELESTIAL_CHANTERELLE, MURUBLIGHT_CHANTERELLE);
                simpleCorruption(CELESTIAL_GROWTH, CORRUPT_GROWTH);

                simpleCorruption(CELESTIAL_HANGING_SIGN, MURUBLIGHT_HANGING_SIGN);
                simpleCorruption(CELESTIAL_SHELF, MURUBLIGHT_SHELF);

                rustleSwellFast(CORRUPT_OVERGROWTH, MIRESTONE, 1);
                rustleSwellFast(CORRUPT_PATH, MIRESTONE, 1);

                rustleSwellFast(OVERGROWN_MIRESTONE_BRICKS, MIRESTONE_BRICKS, 1);
                rustleSwellFast(OVERGROWN_MIRESTONE_BRICK_SLAB, MIRESTONE_BRICK_SLAB, 1);
                rustleSwellFast(OVERGROWN_MIRESTONE_BRICK_STAIRS, MIRESTONE_BRICK_STAIRS, 1);
                rustleSwellFast(OVERGROWN_MIRESTONE_BRICK_WALL, MIRESTONE_BRICK_WALL, 1);

                rustleSwellFast(CORRUPT_GROWTH, RUSTLE_SILK, 1);
                rustleSwellNormally(MURUBLIGHT_CHANTERELLE, RUSTLE_SILK, 2);
                rustleSwellNormally(MURUBLIGHT_CAP, RUSTLE_SILK, 4);

                rustleRecipe(
                        MUSIC_DISC_DECAY,
                        MUSIC_DISC_BLISS,
                        1,
                        RustleRecipeEffects.Builder.create()
                                .swellDuration(60)
                                .swellSound(EnderscapeEntitySounds.RUSTLE_SWELL_LONG)
                                .spitSound(EnderscapeEntitySounds.RUSTLE_SPIT_MUSIC_DISC_BLISS)
                                .build(),
                        50.0F
                );
            }

            // Rustle

            private void rustleSwellFast(ItemLike input, ItemLike result, int count) {
                rustleRecipe(
                        input,
                        result,
                        count,
                        RustleRecipeEffects.Builder.create()
                                .swellDuration(10)
                                .swellSound(EnderscapeEntitySounds.RUSTLE_SWELL_FAST)
                                .build(),
                        count * 2
                );
            }

            private void rustleSwellNormally(ItemLike input, ItemLike result, int count) {
                rustleRecipe(input, result, count, RustleRecipeEffects.DEFAULT, count * 2);
            }

            private void rustleRecipe(ItemLike input, ItemLike result, int count, RustleRecipeEffects effects, float experience) {
                rustle(Ingredient.of(input), result, count, effects, experience).unlockedBy("has_" + getItemName(input), has(input)).save(output, getConversionRecipeName(result, input));
            }

            private SingleItemRecipeBuilder rustle(Ingredient ingredient, ItemLike result, int count, RustleRecipeEffects effects, float experience) {
                return new SingleItemRecipeBuilder(
                        EnderscapeRecipeCategory.RUSTLE,
                        (info, _, template) -> new RustleRecipe(
                                info,
                                ingredient,
                                template,
                                effects,
                                experience
                        ),
                        ingredient,
                        result,
                        count
                );
            }

            // Void Lachryma

            private final List<String> VOID_LACHRYMA_ITEM_CONVERSIONS = Lists.newArrayList();

            private void generateFamilyRecipes(BlockFamily family, FeatureFlagSet flagSet) {
                family.getVariants().forEach((variant, result) -> {
                    if (result.requiredFeatures().isSubsetOf(flagSet)) {
                        if (family.shouldGenerateCraftingRecipe()) {
                            Block base = getBaseBlockForCrafting(family, variant);

                            if (!craftedByVanilla(base, result)) {
                                generateCraftingRecipe(family, variant, result, base);

                                if (variant == BlockFamily.Variant.CRACKED) {
                                    smeltingResultFromBase(result, base);
                                }
                            }
                        }

                        if (family.shouldGenerateStonecutterRecipe()) {
                            Block base = family.getBaseBlock();
                            generateStonecutterRecipe(family, variant, base);
                        }
                    }
                });
            }

            private boolean craftedByVanilla(Block base, Block result) {
                return isVanilla(base) && isVanilla(result);
            }

            private boolean isVanilla(Block block) {
                return BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(Identifier.DEFAULT_NAMESPACE);
            }

            private void simpleFamilyCorruption(BlockFamily input, BlockFamily result) {
                simpleCorruption(input.getBaseBlock(), result.getBaseBlock());

                input.getVariants().entrySet().stream()
                        .filter(entry -> entry.getKey() != BlockFamily.Variant.WALL_SIGN)
                        .forEach(entry -> simpleCorruption(entry.getValue(), result.get(entry.getKey())));
            }

            private void simpleCorruption(ItemLike input, ItemLike result) {
                String name = getConversionRecipeName(result, input);
                Stream<String> existingConversions = VOID_LACHRYMA_ITEM_CONVERSIONS.stream().filter(name::equals);

                if (existingConversions.findFirst().isEmpty()) {
                    voidLachryma(Ingredient.of(input), result).unlockedBy("has_" + getItemName(input), has(input)).save(output, name);
                    VOID_LACHRYMA_ITEM_CONVERSIONS.add(name);
                }
            }

            private SingleItemRecipeBuilder voidLachryma(Ingredient ingredient, ItemLike result) {
                return new SingleItemRecipeBuilder(
                        EnderscapeRecipeCategory.VOID_LACHRYMA,
                        (info, _, _) -> VoidLachrymaRecipe.Builder.create(
                                info,
                                ingredient,
                                new ItemStackTemplate(result.asItem())
                        ).conversionChance(0.25F).build(),
                        ingredient,
                        result,
                        1
                );
            }

            private void rubbleShield(Identifier variant, ItemLike variantItem) {
                DataComponentPatch patch = DataComponentPatch.builder().set(EnderscapeDataComponents.RUBBLE_SHIELD_VARIANT, variant).build();
                ItemStackTemplate template = new ItemStackTemplate(RUBBLE_SHIELD, patch);

                new ShapedRecipeBuilder(registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, template)
                        .define('C', RUBBLE_CHITIN)
                        .define('S', SHADOLINE_INGOT)
                        .define('#', variantItem)
                        .pattern("CSC")
                        .pattern("C#C")
                        .pattern(" C ")
                        .unlockedBy("has_rubble_chitin", has(RUBBLE_CHITIN))
                        .group("rubble_shield")
                        .save(output, "rubble_shield_" + variant.getPath());
            }
        };
    }

    @Override
    public String getName() {
        return "Recipes";
    }
}