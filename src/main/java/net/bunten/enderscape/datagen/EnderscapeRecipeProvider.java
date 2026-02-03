package net.bunten.enderscape.datagen;

import com.google.common.collect.ImmutableList;
import net.bunten.enderscape.item.crafting.ToolFuelingRecipe;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.tag.EnderscapeItemTags;
import net.minecraft.data.recipes.*;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;
import static net.bunten.enderscape.registry.EnderscapeItems.*;
import static net.minecraft.data.recipes.ShapedRecipeBuilder.shaped;
import static net.minecraft.data.recipes.ShapelessRecipeBuilder.shapeless;
import static net.minecraft.world.item.Items.*;

public class EnderscapeRecipeProvider extends RecipeProvider {

    private static final ImmutableList<ItemLike> NEBULITE_SMELTABLES = ImmutableList.of(NEBULITE_ORE.get(), MIRESTONE_NEBULITE_ORE.get());
    private static final ImmutableList<ItemLike> SHADOLINE_SMELTABLES = ImmutableList.of(SHADOLINE_ORE.get(), MIRESTONE_SHADOLINE_ORE.get(), RAW_SHADOLINE.get());

    public EnderscapeRecipeProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), event.getLookupProvider());
    }

    @Override
    public void buildRecipes(RecipeOutput output) {
        EnderscapeBlockFamilies.getAllFamilies().forEach((family) -> generateRecipes(output, family, FeatureFlagSet.of(FeatureFlags.VANILLA)));

        VanillaRecipeProvider.TrimTemplate stasisTrim = new VanillaRecipeProvider.TrimTemplate(STASIS_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ResourceLocation.withDefaultNamespace(getItemName(STASIS_ARMOR_TRIM_SMITHING_TEMPLATE.get()) + "_smithing_trim"));
        trimSmithing(output, stasisTrim.template(), stasisTrim.id());

        SpecialRecipeBuilder.special(ToolFuelingRecipe::new).save(output, "tool_fueling");

        shaped(RecipeCategory.MISC, MUSIC_DISC_BLISS.get())
                .define('D', MUSIC_DISC_DECAY.get())
                .define('N', NEBULITE_SHARDS.get())
                .pattern(" N ")
                .pattern("NDN")
                .pattern(" N ")
                .unlockedBy("has_music_disc_decay", has(MUSIC_DISC_DECAY.get()))
                .save(output);

        shapeless(RecipeCategory.FOOD, DRIFT_JELLY_BOTTLE.get(), 4)
                .requires(DRIFT_JELLY_BLOCK.get(), 1)
                .requires(GLASS_BOTTLE, 4)
                .unlockedBy("has_drift_jelly_block", has(DRIFT_JELLY_BLOCK.get()))
                .save(output);

        shaped(RecipeCategory.MISC, DRIFT_JELLY_BLOCK.get())
                .define('#', DRIFT_JELLY_BOTTLE.get())
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_drift_jelly_bottle", has(DRIFT_JELLY_BOTTLE.get()))
                .save(output);

        shaped(RecipeCategory.COMBAT, DRIFT_LEGGINGS.get())
                .define('N', NEBULITE.get())
                .define('D', DRIFT_JELLY_BOTTLE.get())
                .pattern("NNN")
                .pattern("D D")
                .pattern("D D")
                .unlockedBy("has_drift_jelly_bottle", has(DRIFT_JELLY_BOTTLE.get()))
                .save(output);

        shaped(RecipeCategory.COMBAT, DAGGER.get())
                .define('X', SHADOLINE_INGOT.get())
                .define('S', STICK)
                .pattern(" X")
                .pattern("XX")
                .pattern("S ")
                .unlockedBy("has_shadoline_ingot", has(SHADOLINE_INGOT.get()))
                .save(output);
        
        shaped(RecipeCategory.COMBAT, SHADOLINE_HELMET.get())
                .define('X', SHADOLINE_INGOT.get())
                .pattern("XXX")
                .pattern("X X")
                .unlockedBy("has_shadoline_ingot", has(SHADOLINE_INGOT.get()))
                .save(output);

        shaped(RecipeCategory.COMBAT, SHADOLINE_CHESTPLATE.get())
                .define('X', SHADOLINE_INGOT.get())
                .pattern("X X")
                .pattern("XXX")
                .pattern("XXX")
                .unlockedBy("has_shadoline_ingot", has(SHADOLINE_INGOT.get()))
                .save(output);

        shaped(RecipeCategory.COMBAT, SHADOLINE_LEGGINGS.get())
                .define('X', SHADOLINE_INGOT.get())
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .unlockedBy("has_shadoline_ingot", has(SHADOLINE_INGOT.get()))
                .save(output);

        shaped(RecipeCategory.COMBAT, SHADOLINE_BOOTS.get())
                .define('X', SHADOLINE_INGOT.get())
                .pattern("X X")
                .pattern("X X")
                .unlockedBy("has_shadoline_ingot", has(SHADOLINE_INGOT.get()))
                .save(output);

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(
                                SHADOLINE_HELMET.get(),
                                SHADOLINE_CHESTPLATE.get(),
                                SHADOLINE_LEGGINGS.get(),
                                SHADOLINE_BOOTS.get()
                        ),
                        RecipeCategory.MISC,
                        SHADOLINE_NUGGET.get(),
                        0.1F,
                        200
                )
                .unlockedBy("has_shadoline_helmet", has(SHADOLINE_HELMET.get()))
                .unlockedBy("has_shadoline_chestplate", has(SHADOLINE_CHESTPLATE.get()))
                .unlockedBy("has_shadoline_leggings", has(SHADOLINE_LEGGINGS.get()))
                .unlockedBy("has_shadoline_boots", has(SHADOLINE_BOOTS.get()))
                .save(output, getSmeltingRecipeName(SHADOLINE_NUGGET.get()));

        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(
                                SHADOLINE_HELMET.get(),
                                SHADOLINE_CHESTPLATE.get(),
                                SHADOLINE_LEGGINGS.get(),
                                SHADOLINE_BOOTS.get()
                        ),
                        RecipeCategory.MISC,
                        SHADOLINE_NUGGET.get(),
                        0.1F,
                        100
                )
                .unlockedBy("has_shadoline_helmet", has(SHADOLINE_HELMET.get()))
                .unlockedBy("has_shadoline_chestplate", has(SHADOLINE_CHESTPLATE.get()))
                .unlockedBy("has_shadoline_leggings", has(SHADOLINE_LEGGINGS.get()))
                .unlockedBy("has_shadoline_boots", has(SHADOLINE_BOOTS.get()))
                .save(output, getBlastingRecipeName(SHADOLINE_NUGGET.get()));

        shaped(RecipeCategory.DECORATIONS, SHADOLINE_CHAIN.get())
                .define('I', SHADOLINE_INGOT.get())
                .define('N', SHADOLINE_NUGGET.get())
                .pattern("N")
                .pattern("I")
                .pattern("N")
                .unlockedBy("has_shadoline_nugget", has(SHADOLINE_NUGGET.get()))
                .unlockedBy("has_shadoline_ingot", has(SHADOLINE_INGOT.get()))
                .save(output);

        shaped(RecipeCategory.DECORATIONS, SHADOLINE_BARS.get(), 16)
                .define('#', SHADOLINE_INGOT.get())
                .pattern("###")
                .pattern("###")
                .unlockedBy("has_shadoline_ingot", has(SHADOLINE_INGOT.get()))
                .save(output);

                shaped(RecipeCategory.DECORATIONS, VOID_TORCH.get(), 4)
                        .define('V', VOID_SHALE.get())
                        .define('#', STICK)
                        .pattern("V")
                        .pattern("#")
                        .unlockedBy("has_void_shale", has(VOID_SHALE.get()))
                        .save(output);

                shaped(RecipeCategory.DECORATIONS, VOID_LANTERN.get())
                        .define('#', SHADOLINE_NUGGET.get())
                        .define('@', VOID_TORCH.get())
                        .pattern("###")
                        .pattern("#@#")
                        .pattern("###")
                        .unlockedBy("has_void_shale", has(VOID_SHALE.get()))
                        .save(output);

                shaped(RecipeCategory.DECORATIONS, VOID_CAMPFIRE.get())
                        .define('L', ItemTags.LOGS)
                        .define('S', STICK)
                        .define('V', VOID_SHALE.get())
                        .pattern(" S ")
                        .pattern("SVS")
                        .pattern("LLL")
                        .unlockedBy("has_void_shale", has(VOID_SHALE.get()))
                        .save(output);
        
        rubbleShield(output, END_STONE, END_STONE_RUBBLE_SHIELD.get());
        rubbleShield(output, VERADITE.get(), VERADITE_RUBBLE_SHIELD.get());
        rubbleShield(output, MIRESTONE.get(), MIRESTONE_RUBBLE_SHIELD.get());
        rubbleShield(output, KURODITE.get(), KURODITE_RUBBLE_SHIELD.get());

        shaped(RecipeCategory.TOOLS, MAGNIA_ATTRACTOR.get())
                .define('A', ALLURING_MAGNIA_SPROUT.get())
                .define('R', REPULSIVE_MAGNIA_SPROUT.get())
                .define('S', SHADOLINE_INGOT.get())
                .pattern("A R")
                .pattern("SSS")
                .pattern(" S ")
                .unlockedBy("has_magnia_block", has(EnderscapeItemTags.MAGNIA_SPROUTS))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, END_STONE_SLAB.get(), END_STONE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, END_STONE_STAIRS.get(), END_STONE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, END_STONE_WALL.get(), END_STONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_END_STONE.get(), END_STONE);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, POLISHED_END_STONE_WALL.get(), END_STONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_END_STONE_SLAB.get(), END_STONE, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_END_STONE_STAIRS.get(), END_STONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CHISELED_END_STONE.get(), END_STONE);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_END_STONE_SLAB.get(), POLISHED_END_STONE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_END_STONE_STAIRS.get(), POLISHED_END_STONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, END_STONE_BRICKS, EnderscapeBlocks.POLISHED_END_STONE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, POLISHED_END_STONE_WALL.get(), POLISHED_END_STONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, END_STONE_BRICK_SLAB, POLISHED_END_STONE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, END_STONE_BRICK_STAIRS, POLISHED_END_STONE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, END_STONE_BRICK_WALL, POLISHED_END_STONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CHISELED_END_STONE.get(), POLISHED_END_STONE.get());

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MIRESTONE_SLAB.get(), MIRESTONE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MIRESTONE_STAIRS.get(), MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, MIRESTONE_WALL.get(), MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_MIRESTONE.get(), MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, POLISHED_MIRESTONE_WALL.get(), MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_MIRESTONE_SLAB.get(), MIRESTONE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_MIRESTONE_STAIRS.get(), MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CHISELED_MIRESTONE.get(), MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICKS.get(), MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICK_SLAB.get(), MIRESTONE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICK_STAIRS.get(), MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, MIRESTONE_BRICK_WALL.get(), MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_MIRESTONE_SLAB.get(), POLISHED_MIRESTONE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_MIRESTONE_STAIRS.get(), POLISHED_MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICKS.get(), POLISHED_MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, POLISHED_MIRESTONE_WALL.get(), POLISHED_MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICK_SLAB.get(), POLISHED_MIRESTONE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICK_STAIRS.get(), POLISHED_MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, MIRESTONE_BRICK_WALL.get(), POLISHED_MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CHISELED_MIRESTONE.get(), POLISHED_MIRESTONE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICK_SLAB.get(), MIRESTONE_BRICKS.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MIRESTONE_BRICK_STAIRS.get(), MIRESTONE_BRICKS.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, MIRESTONE_BRICK_WALL.get(), MIRESTONE_BRICKS.get());

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, VERADITE_SLAB.get(), VERADITE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, VERADITE_STAIRS.get(), VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, VERADITE_WALL.get(), VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_VERADITE.get(), VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, POLISHED_VERADITE_WALL.get(), VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_VERADITE_SLAB.get(), VERADITE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_VERADITE_STAIRS.get(), VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CHISELED_VERADITE.get(), VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICKS.get(), VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICK_SLAB.get(), VERADITE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICK_STAIRS.get(), VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, VERADITE_BRICK_WALL.get(), VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_VERADITE_SLAB.get(), POLISHED_VERADITE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_VERADITE_STAIRS.get(), POLISHED_VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICKS.get(), POLISHED_VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, POLISHED_VERADITE_WALL.get(), POLISHED_VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICK_SLAB.get(), POLISHED_VERADITE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICK_STAIRS.get(), POLISHED_VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, VERADITE_BRICK_WALL.get(), POLISHED_VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CHISELED_VERADITE.get(), POLISHED_VERADITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICK_SLAB.get(), VERADITE_BRICKS.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, VERADITE_BRICK_STAIRS.get(), VERADITE_BRICKS.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, VERADITE_BRICK_WALL.get(), VERADITE_BRICKS.get());

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, KURODITE_SLAB.get(), KURODITE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, KURODITE_STAIRS.get(), KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, KURODITE_WALL.get(), KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_KURODITE.get(), KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, POLISHED_KURODITE_WALL.get(), KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_KURODITE_SLAB.get(), KURODITE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_KURODITE_STAIRS.get(), KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CHISELED_KURODITE.get(), KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICKS.get(), KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICK_SLAB.get(), KURODITE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICK_STAIRS.get(), KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, KURODITE_BRICK_WALL.get(), KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_KURODITE_SLAB.get(), POLISHED_KURODITE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, POLISHED_KURODITE_STAIRS.get(), POLISHED_KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICKS.get(), POLISHED_KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, POLISHED_KURODITE_WALL.get(), POLISHED_KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICK_SLAB.get(), POLISHED_KURODITE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICK_STAIRS.get(), POLISHED_KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, KURODITE_BRICK_WALL.get(), POLISHED_KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CHISELED_KURODITE.get(), POLISHED_KURODITE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICK_SLAB.get(), KURODITE_BRICKS.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, KURODITE_BRICK_STAIRS.get(), KURODITE_BRICKS.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, KURODITE_BRICK_WALL.get(), KURODITE_BRICKS.get());

        shaped(RecipeCategory.BUILDING_BLOCKS, ETCHED_ALLURING_MAGNIA.get(), 4)
                .define('#', ALLURING_MAGNIA.get())
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_alluring_magnia", has(ALLURING_MAGNIA.get()))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ETCHED_ALLURING_MAGNIA.get(), ALLURING_MAGNIA.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, ETCHED_ALLURING_MAGNIA_WALL.get(), ALLURING_MAGNIA.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ETCHED_ALLURING_MAGNIA_SLAB.get(), ALLURING_MAGNIA.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ETCHED_ALLURING_MAGNIA_STAIRS.get(), ALLURING_MAGNIA.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ETCHED_ALLURING_MAGNIA_SLAB.get(), ETCHED_ALLURING_MAGNIA.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ETCHED_ALLURING_MAGNIA_STAIRS.get(), ETCHED_ALLURING_MAGNIA.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, ETCHED_ALLURING_MAGNIA_WALL.get(), ETCHED_ALLURING_MAGNIA.get());

        shaped(RecipeCategory.BUILDING_BLOCKS, ETCHED_REPULSIVE_MAGNIA.get(), 4)
                .define('#', REPULSIVE_MAGNIA.get())
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_repulsive_magnia", has(REPULSIVE_MAGNIA.get()))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ETCHED_REPULSIVE_MAGNIA.get(), REPULSIVE_MAGNIA.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, ETCHED_REPULSIVE_MAGNIA_WALL.get(), REPULSIVE_MAGNIA.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ETCHED_REPULSIVE_MAGNIA_SLAB.get(), REPULSIVE_MAGNIA.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ETCHED_REPULSIVE_MAGNIA_STAIRS.get(), REPULSIVE_MAGNIA.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ETCHED_REPULSIVE_MAGNIA_SLAB.get(), ETCHED_REPULSIVE_MAGNIA.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, ETCHED_REPULSIVE_MAGNIA_STAIRS.get(), ETCHED_REPULSIVE_MAGNIA.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, ETCHED_REPULSIVE_MAGNIA_WALL.get(), ETCHED_REPULSIVE_MAGNIA.get());

//        shaped(RecipeCategory.BUILDING_BLOCKS, MAGNIA_RADIO)
//                .define('A', ETCHED_ALLURING_MAGNIA)
//                .define('R', ETCHED_REPULSIVE_MAGNIA)
//                .define('!', ALLURING_MAGNIA.get()_SPROUT)
//                .define('@', REPULSIVE_MAGNIA_SPROUT)
//                .define('N', NEBULITE)
//                .pattern("! @")
//                .pattern("RNR")
//                .pattern("AAA")
//                .unlockedBy("has_any_magnia_sprout", has(EnderscapeItemTags.MAGNIA_SPROUTS))
//                .save(output);
//
        shaped(RecipeCategory.BUILDING_BLOCKS, POLARIZED_MAGNIA.get())
                .define('B', BLISTERED_MAGNIA.get())
                .define('S', SHADOLINE_INGOT.get())
                .define('P', POPPED_CHORUS_FRUIT)
                .define('R', REDSTONE)
                .pattern("SSS")
                .pattern("PBP")
                .pattern("SRS")
                .unlockedBy("has_blistered_magnia", has(BLISTERED_MAGNIA.get()))
                .save(output);

        oreSmelting(output, SHADOLINE_SMELTABLES, RecipeCategory.MISC, SHADOLINE_INGOT.get(), 0.7F, 200, "shadoline_ingot");
        oreBlasting(output, SHADOLINE_SMELTABLES, RecipeCategory.MISC, SHADOLINE_INGOT.get(), 0.7F, 100, "shadoline_ingot");

        nineBlockStorageRecipesWithCustomPacking(output, RecipeCategory.MISC, SHADOLINE_NUGGET.get(), RecipeCategory.MISC, SHADOLINE_INGOT.get(), "enderscape:shadoline_ingot_from_nuggets", "enderscape:shadoline_ingot");

        shapeless(RecipeCategory.MISC, RAW_SHADOLINE.get(), 9)
                .requires(RAW_SHADOLINE_BLOCK.get())
                .group("raw_shadoline")
                .unlockedBy("has_raw_shadoline_block", has(RAW_SHADOLINE_BLOCK.get()))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, RAW_SHADOLINE_BLOCK.get())
                .define('#', RAW_SHADOLINE.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .group("raw_shadoline_block")
                .unlockedBy("has_raw_shadoline", has(RAW_SHADOLINE.get()))
                .save(output);

        shapeless(RecipeCategory.MISC, SHADOLINE_INGOT.get(), 9)
                .requires(SHADOLINE_BLOCK.get())
                .group("shadoline_ingot")
                .unlockedBy("has_shadoline_block", has(SHADOLINE_BLOCK.get()))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, SHADOLINE_BLOCK.get())
                .define('#', SHADOLINE_INGOT.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .group("shadoline_block")
                .unlockedBy("has_shadoline_ingot", has(SHADOLINE_INGOT.get()))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, SHADOLINE_BLOCK_SLAB.get(), SHADOLINE_BLOCK.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, SHADOLINE_BLOCK_STAIRS.get(), SHADOLINE_BLOCK.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, SHADOLINE_BLOCK_WALL.get(), SHADOLINE_BLOCK.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CHISELED_SHADOLINE.get(), SHADOLINE_BLOCK.get(), 4);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CUT_SHADOLINE.get(), SHADOLINE_BLOCK.get(), 4);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CUT_SHADOLINE_SLAB.get(), SHADOLINE_BLOCK.get(), 8);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CUT_SHADOLINE_STAIRS.get(), SHADOLINE_BLOCK.get(), 4);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, CUT_SHADOLINE_WALL.get(), SHADOLINE_BLOCK.get(), 4);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CUT_SHADOLINE_SLAB.get(), CUT_SHADOLINE.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CUT_SHADOLINE_STAIRS.get(), CUT_SHADOLINE.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, CUT_SHADOLINE_WALL.get(), CUT_SHADOLINE.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, SHADOLINE_PILLAR.get(), SHADOLINE_BLOCK.get(), 4);

        shaped(RecipeCategory.BUILDING_BLOCKS, SHADOLINE_PILLAR.get())
                .define('#', SHADOLINE_BLOCK.get())
                .pattern("#")
                .pattern("#")
                .unlockedBy("has_shadoline_block", has(SHADOLINE_BLOCK.get()))
                .save(output);

        shapeless(RecipeCategory.MISC, NEBULITE.get())
                .requires(NEBULITE_SHARDS.get(), 4)
                .unlockedBy("has_nebulite_shards", has(NEBULITE_SHARDS.get()))
                .save(output, "enderscape:nebulite_from_shards");

        oreSmelting(output, NEBULITE_SMELTABLES, RecipeCategory.MISC, NEBULITE.get(), 1.0F, 200, "nebulite");
        oreBlasting(output, NEBULITE_SMELTABLES, RecipeCategory.MISC, NEBULITE.get(), 1.0F, 100, "nebulite");

        shapeless(RecipeCategory.MISC, NEBULITE.get(), 9)
                .requires(NEBULITE_BLOCK.get())
                .group("nebulite")
                .unlockedBy("has_nebulite_block", has(NEBULITE_BLOCK.get()))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, NEBULITE_BLOCK.get())
                .define('#', NEBULITE.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .group("nebulite_block")
                .unlockedBy("has_nebulite", has(NEBULITE.get()))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CHISELED_PURPUR.get(), PURPUR_BLOCK);
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, PURPUR_WALL.get(), PURPUR_BLOCK);

        shapeless(RecipeCategory.BUILDING_BLOCKS, DUSK_PURPUR_BLOCK.get(), 4)
                .requires(POPPED_CHORUS_FRUIT, 2)
                .requires(SHADOLINE_INGOT.get(), 2)
                .unlockedBy("has_popped_chorus_fruit", has(POPPED_CHORUS_FRUIT))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, DUSK_PURPUR_PILLAR.get())
                .define('#', DUSK_PURPUR_BLOCK.get())
                .pattern("#")
                .pattern("#")
                .unlockedBy("has_dusk_purpur_block", has(DUSK_PURPUR_BLOCK.get()))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, DUSK_PURPUR_SLAB.get(), DUSK_PURPUR_BLOCK.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, DUSK_PURPUR_STAIRS.get(), DUSK_PURPUR_BLOCK.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, DUSK_PURPUR_WALL.get(), DUSK_PURPUR_BLOCK.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CHISELED_DUSK_PURPUR.get(), DUSK_PURPUR_BLOCK.get());
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, DUSK_PURPUR_PILLAR.get(), DUSK_PURPUR_BLOCK.get());

        shaped(RecipeCategory.BUILDING_BLOCKS, PURPUR_TILES.get(), 4)
                .define('P', PURPUR_BLOCK)
                .define('D', DUSK_PURPUR_BLOCK.get())
                .pattern("DP")
                .pattern("PD")
                .unlockedBy("has_purpur_block", has(PURPUR_BLOCK))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, PURPUR_TILE_SLAB.get(), PURPUR_TILES.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, PURPUR_TILE_STAIRS.get(), PURPUR_TILES.get());

        shaped(RecipeCategory.FOOD, CHORUS_CAKE_ROLL.get())
                .define('C', CHORUS_FRUIT)
                .define('S', SUGAR)
                .define('D', DRIFT_JELLY_BOTTLE.get())
                .pattern("CCC")
                .pattern("SDS")
                .unlockedBy("has_chorus_fruit", has(CHORUS_FRUIT))
                .save(output);

        shaped(RecipeCategory.DECORATIONS, END_LAMP.get())
                .define('B', BLAZE_ROD)
                .define('C', POPPED_CHORUS_FRUIT)
                .pattern(" C ")
                .pattern("CBC")
                .pattern(" C ")
                .unlockedBy("has_popped_chorus_fruit", has(POPPED_CHORUS_FRUIT))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, VEILED_LEAF_PILE.get(), 6)
                .define('#', VEILED_LEAVES.get())
                .pattern("###")
                .unlockedBy("has_veiled_leaves", has(VEILED_LEAVES.get()))
                .save(output);

        shapeless(RecipeCategory.MISC, WHITE_DYE)
                .group("white_dye")
                .requires(WISP_FLOWER.get(), 1)
                .unlockedBy("has_wisp_flower", has(WISP_FLOWER.get()))
                .save(output, "enderscape:white_dye_from_wisp_flower");

        woodFromLogs(output, VEILED_WOOD.get(), VEILED_LOG.get());
        woodFromLogs(output, STRIPPED_VEILED_WOOD.get(), STRIPPED_VEILED_LOG.get());
        planksFromLogs(output, VEILED_PLANKS.get(), EnderscapeItemTags.VEILED_LOGS, 4);

        hangingSign(output, VEILED_HANGING_SIGN.get(), STRIPPED_VEILED_LOG.get());

        shapeless(RecipeCategory.MISC, YELLOW_DYE)
                .group("yellow_dye")
                .requires(CELESTIAL_GROWTH.get(), 1)
                .unlockedBy("has_celestial_growth", has(CELESTIAL_GROWTH.get()))
                .save(output, "enderscape:yellow_dye_from_celestial_growth");

        shapeless(RecipeCategory.MISC, CYAN_DYE)
                .group("cyan_dye")
                .requires(BULB_FLOWER.get(), 1)
                .unlockedBy("has_bulb_flower", has(BULB_FLOWER.get()))
                .save(output, "enderscape:cyan_dye_from_bulb_flower");

        shaped(RecipeCategory.DECORATIONS, BULB_LANTERN.get())
                .define('#', IRON_NUGGET)
                .define('@', BULB_FLOWER.get())
                .pattern("###")
                .pattern("#@#")
                .pattern("###")
                .unlockedBy("has_bulb_flower", has(BULB_FLOWER.get()))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, CELESTIAL_BRICKS.get(), 2)
                .define('#', CELESTIAL_CAP.get())
                .define('@', CELESTIAL_GROWTH.get())
                .pattern("@#")
                .pattern("#@")
                .unlockedBy("has_celestial_cap", has(CELESTIAL_CAP.get()))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CELESTIAL_BRICK_SLAB.get(), CELESTIAL_BRICKS.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, CELESTIAL_BRICK_STAIRS.get(), CELESTIAL_BRICKS.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, CELESTIAL_BRICK_WALL.get(), CELESTIAL_BRICKS.get());

        woodFromLogs(output, CELESTIAL_HYPHAE.get(), CELESTIAL_STEM.get());
        woodFromLogs(output, STRIPPED_CELESTIAL_HYPHAE.get(), STRIPPED_CELESTIAL_STEM.get());
        planksFromLogs(output, CELESTIAL_PLANKS.get(), EnderscapeItemTags.CELESTIAL_STEMS, 4);

        hangingSign(output, CELESTIAL_HANGING_SIGN.get(), STRIPPED_CELESTIAL_STEM.get());

        shapeless(RecipeCategory.MISC, PURPLE_DYE)
                .group("purple_dye")
                .requires(CORRUPT_GROWTH.get(), 1)
                .unlockedBy("has_corrupt_growth", has(CORRUPT_GROWTH.get()))
                .save(output, "enderscape:purple_dye_from_corrupt_growth");

        shaped(RecipeCategory.BUILDING_BLOCKS, BLINKLAMP.get())
                .define('#', POPPED_CHORUS_FRUIT)
                .define('@', BLINKLIGHT.get())
                .pattern("#@#")
                .pattern("@@@")
                .pattern("#@#")
                .unlockedBy("has_blinklight", has(BLINKLIGHT.get()))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, MURUBLIGHT_BRICKS.get(), 2)
                .define('#', MURUBLIGHT_CAP.get())
                .define('@', CORRUPT_GROWTH.get())
                .pattern("@#")
                .pattern("#@")
                .unlockedBy("has_murublight_cap", has(MURUBLIGHT_CAP.get()))
                .save(output);

        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MURUBLIGHT_BRICK_SLAB.get(), MURUBLIGHT_BRICKS.get(), 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, MURUBLIGHT_BRICK_STAIRS.get(), MURUBLIGHT_BRICKS.get());
        stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, MURUBLIGHT_BRICK_WALL.get(), MURUBLIGHT_BRICKS.get());

        woodFromLogs(output, MURUBLIGHT_HYPHAE.get(), MURUBLIGHT_STEM.get());
        woodFromLogs(output, STRIPPED_MURUBLIGHT_HYPHAE.get(), STRIPPED_MURUBLIGHT_STEM.get());
        planksFromLogs(output, MURUBLIGHT_PLANKS.get(), EnderscapeItemTags.MURUBLIGHT_STEMS, 4);

        hangingSign(output, MURUBLIGHT_HANGING_SIGN.get(), STRIPPED_MURUBLIGHT_STEM.get());
    }

    private void rubbleShield(RecipeOutput output, ItemLike stone, ItemLike shield) {
        shaped(RecipeCategory.COMBAT, shield)
                .define('C', RUBBLE_CHITIN.get())
                .define('S', SHADOLINE_INGOT.get())
                .define('#', stone)
                .pattern("CSC")
                .pattern("C#C")
                .pattern(" C ")
                .unlockedBy("has_rubble_chitin", has(RUBBLE_CHITIN.get()))
                .group("rubble_shield")
                .save(output);
    }
}