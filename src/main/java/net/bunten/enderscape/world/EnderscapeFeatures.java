package net.bunten.enderscape.world;

import org.betterx.bclib.api.v2.levelgen.features.BCLFeature;
import org.betterx.bclib.api.v2.levelgen.features.BCLFeatureBuilder;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.blocks.MurushroomsBlock;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.world.features.CelestialIslandFeature;
import net.bunten.enderscape.world.features.ores.ScatteredOreFeature;
import net.bunten.enderscape.world.features.ores.VoidOreFeature;
import net.bunten.enderscape.world.features.vegetation.CelestialGrowthFeature;
import net.bunten.enderscape.world.features.vegetation.CelestialGrowthFeatureConfig;
import net.bunten.enderscape.world.features.vegetation.CelestialVegetationFeature;
import net.bunten.enderscape.world.features.vegetation.CelestialVegetationFeatureConfig;
import net.bunten.enderscape.world.features.vegetation.LargeCelestialFungusFeature;
import net.bunten.enderscape.world.features.vegetation.LargeCelestialFungusFeatureConfig;
import net.bunten.enderscape.world.features.vegetation.MurushroomFeature;
import net.bunten.enderscape.world.features.vegetation.MurushroomFeatureConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;

public abstract class EnderscapeFeatures {

    private static final GenerationStep.Feature RAW_GENERATION = GenerationStep.Feature.RAW_GENERATION;
    private static final GenerationStep.Feature LAKES = GenerationStep.Feature.LAKES;
    private static final GenerationStep.Feature LOCAL_MODIFICATIONS = GenerationStep.Feature.LOCAL_MODIFICATIONS;
    private static final GenerationStep.Feature UNDERGROUND_STRUCTURES = GenerationStep.Feature.UNDERGROUND_STRUCTURES;
    private static final GenerationStep.Feature SURFACE_STRUCTURES = GenerationStep.Feature.SURFACE_STRUCTURES;
    private static final GenerationStep.Feature STRONGHOLDS = GenerationStep.Feature.STRONGHOLDS;
    private static final GenerationStep.Feature UNDERGROUND_ORES = GenerationStep.Feature.UNDERGROUND_ORES;
    private static final GenerationStep.Feature UNDERGROUND_DECORATION = GenerationStep.Feature.UNDERGROUND_DECORATION;
    private static final GenerationStep.Feature FLUID_SPRINGS = GenerationStep.Feature.FLUID_SPRINGS;
    private static final GenerationStep.Feature VEGETAL_DECORATION = GenerationStep.Feature.VEGETAL_DECORATION;
    private static final GenerationStep.Feature TOP_LAYER_MODIFICATION = GenerationStep.Feature.TOP_LAYER_MODIFICATION;

    private static final BlockState CELESTIAL_FUNGUS = EnderscapeBlocks.CELESTIAL_FUNGUS.getDefaultState();
    private static final BlockState BULB_FLOWER = EnderscapeBlocks.BULB_FLOWER.getDefaultState();

    private static final BlockState END_STONE = Blocks.END_STONE.getDefaultState();

    private static final BlockState NEBULITE_ORE = EnderscapeBlocks.NEBULITE_ORE.getDefaultState();
    private static final BlockState SHADOW_QUARTZ_ORE = EnderscapeBlocks.SHADOW_QUARTZ_ORE.getDefaultState();

    public static final Feature<LargeCelestialFungusFeatureConfig> LARGE_CELESTIAL_FUNGUS = register("large_celestial_fungus", new LargeCelestialFungusFeature(LargeCelestialFungusFeatureConfig.CODEC));
    public static final Feature<DefaultFeatureConfig> CELESTIAL_ISLAND = register("celestial_island", new CelestialIslandFeature(DefaultFeatureConfig.CODEC));

    public static final Feature<CelestialGrowthFeatureConfig> CELESTIAL_GROWTH = register("celestial_growth", new CelestialGrowthFeature(CelestialGrowthFeatureConfig.CODEC));
    public static final Feature<MurushroomFeatureConfig> MURUSHROOM = register("murushroom", new MurushroomFeature(MurushroomFeatureConfig.CODEC));

    public static final Feature<SingleStateFeatureConfig> SCATTERED_ORE = register("scattered_ore", new ScatteredOreFeature(SingleStateFeatureConfig.CODEC));
    public static final Feature<SingleStateFeatureConfig> VOID_FACING_ORE = register("void_facing_ore", new VoidOreFeature(SingleStateFeatureConfig.CODEC));

    public static final Feature<CelestialVegetationFeatureConfig> CELESTIAL_VEGETATION = register("celestial_vegetation", new CelestialVegetationFeature(CelestialVegetationFeatureConfig.CODEC));

    public static final BCLFeature<?, ?> BCL_CELESTIAL_ISLAND = BCLFeatureBuilder.start(Enderscape.id("celestial_island"), CELESTIAL_ISLAND)
    .onceEvery(12)
    .squarePlacement()
    .onlyInBiome()
    .modifier(uniform(YOffset.fixed(55), YOffset.fixed(70)))
    .decoration(RAW_GENERATION)
    .buildAndRegister();

    public static final BCLFeature<?, ?> BCL_LARGE_CELESTIAL_FUNGUS = BCLFeatureBuilder.start(Enderscape.id("large_celestial_fungus"), LARGE_CELESTIAL_FUNGUS)
    .count(68)
    .squarePlacement()
    .onlyInBiome()
    .modifier(PlacedFeatures.EIGHT_ABOVE_AND_BELOW_RANGE)
    .decoration(VEGETAL_DECORATION)
    .buildAndRegister(new LargeCelestialFungusFeatureConfig(UniformIntProvider.create(10, 35), 4, 1, 0.75F, 1, 64, 16));

    public static final BCLFeature<?, ?> BCL_CELESTIAL_GROWTH = BCLFeatureBuilder.start(Enderscape.id("celestial_growth"), CELESTIAL_GROWTH)
    .countLayers(10)
    .squarePlacement()
    .onlyInBiome()
    .modifier(PlacedFeatures.EIGHT_ABOVE_AND_BELOW_RANGE)
    .decoration(VEGETAL_DECORATION)
    .buildAndRegister(new CelestialGrowthFeatureConfig(24, 4, 4, UniformIntProvider.create(1, 1), UniformIntProvider.create(1, 2), 0.5F, 30));

    public static final BCLFeature<?, ?> BCL_CELESTIAL_VEGETATION = BCLFeatureBuilder.start(Enderscape.id("celestial_vegetation"), CELESTIAL_VEGETATION).decoration(VEGETAL_DECORATION)
    .countLayers(8)
    .squarePlacement()
    .onlyInBiome()
    .modifier(PlacedFeatures.EIGHT_ABOVE_AND_BELOW_RANGE)
    .buildAndRegister(new CelestialVegetationFeatureConfig(16, 3, 3, 8));

    public static final BCLFeature<?, ?> BCL_UNCOMMON_CELESTIAL_GROWTH = BCLFeatureBuilder.start(Enderscape.id("uncommon_celestial_growth"), CELESTIAL_GROWTH)
    .countLayers(4)
    .onceEvery(12)
    .modifier(PlacedFeatures.EIGHT_ABOVE_AND_BELOW_RANGE)
    .squarePlacement()
    .decoration(VEGETAL_DECORATION)
    .buildAndRegister(new CelestialGrowthFeatureConfig(24, 4, 4, UniformIntProvider.create(1, 1), UniformIntProvider.create(1, 2), 0.5F, 30));

    public static final BCLFeature<?, ?> BCL_MURUSHROOMS = BCLFeatureBuilder.start(Enderscape.id("murushrooms"), MURUSHROOM)
    .countLayers(1)
    .modifier(uniform(YOffset.aboveBottom(20), YOffset.fixed(50)))
    .squarePlacement()
    .decoration(VEGETAL_DECORATION)
    .buildAndRegister(new MurushroomFeatureConfig(8, 8, MurushroomsBlock.MAX_AGE, 100));

    public static final BCLFeature<?, ?> BCL_UNCOMMON_MURUSHROOMS = BCLFeatureBuilder.start(Enderscape.id("uncommon_murushrooms"), MURUSHROOM)
    .onceEvery(3)
    .modifier(uniform(YOffset.aboveBottom(20), YOffset.fixed(50)))
    .squarePlacement()
    .decoration(VEGETAL_DECORATION)
    .buildAndRegister(new MurushroomFeatureConfig(8, 8, MurushroomsBlock.MAX_AGE, 100));

    public static final BCLFeature<?, ?> BCL_SHADOW_QUARTZ_ORE = BCLFeatureBuilder.start(Enderscape.id("shadow_quartz_ore"), Feature.ORE)
    .countLayers(2)
    .modifier(uniform(YOffset.getBottom(), YOffset.fixed(60)))
    .squarePlacement()
    .decoration(UNDERGROUND_ORES)
    .buildAndRegister(new OreFeatureConfig(new BlockMatchRuleTest(Blocks.END_STONE), SHADOW_QUARTZ_ORE, 10, 0));

    public static final BCLFeature<?, ?> BCL_SCATTERED_SHADOW_QUARTZ_ORE = BCLFeatureBuilder.start(Enderscape.id("scattered_shadow_quartz_ore"), SCATTERED_ORE)
    .countLayers(2)
    .modifier(uniform(YOffset.getBottom(), YOffset.fixed(70)))
    .squarePlacement()
    .decoration(UNDERGROUND_ORES)
    .buildAndRegister(new SingleStateFeatureConfig(SHADOW_QUARTZ_ORE));

    public static final BCLFeature<?, ?> BCL_NEBULITE_ORE = BCLFeatureBuilder.start(Enderscape.id("nebulite_ore"), Feature.SCATTERED_ORE)
    .count(1)
    .modifier(trapezoid(YOffset.getBottom(), YOffset.fixed(50)))
    .squarePlacement()
    .decoration(UNDERGROUND_ORES)
    .buildAndRegister(new OreFeatureConfig(new BlockMatchRuleTest(Blocks.END_STONE), NEBULITE_ORE, 2, 0.25F));

    public static final BCLFeature<?, ?> BCL_VOID_NEBULITE_ORE = BCLFeatureBuilder.start(Enderscape.id("void_nebulite_ore"), VOID_FACING_ORE)
    .count(2)
    .modifier(uniform(YOffset.getBottom(), YOffset.fixed(20)))
    .squarePlacement()
    .decoration(UNDERGROUND_ORES)
    .buildAndRegister(new SingleStateFeatureConfig(NEBULITE_ORE));

    public static final BCLFeature<?, ?> BETTEREND_VOID_NEBULITE_ORE = BCLFeatureBuilder.start(Enderscape.id("betterend_void_nebulite_ore"), VOID_FACING_ORE)
    .countLayers(2)
    .modifier(uniform(YOffset.aboveBottom(20), YOffset.getTop()))
    .squarePlacement()
    .decoration(UNDERGROUND_ORES)
    .buildAndRegister(new SingleStateFeatureConfig(NEBULITE_ORE));

    public static final BCLFeature<?, ?> BETTEREND_SHADOW_QUARTZ_ORE = BCLFeatureBuilder.start(Enderscape.id("betterend_shadow_quartz_ore"), SCATTERED_ORE)
    .countLayers(2)
    .modifier(uniform(YOffset.aboveBottom(70), YOffset.getTop()))
    .squarePlacement()
    .decoration(UNDERGROUND_ORES)
    .buildAndRegister(new SingleStateFeatureConfig(SHADOW_QUARTZ_ORE));

    public static final BCLFeature<?, ?> BETTEREND_MURUSHROOMS = BCLFeatureBuilder.start(Enderscape.id("betterend_uncommon_murushrooms"), MURUSHROOM)
    .onceEvery(2)
    .modifier(uniform(YOffset.aboveBottom(50), YOffset.getTop()))
    .squarePlacement()
    .decoration(VEGETAL_DECORATION)
    .buildAndRegister(new MurushroomFeatureConfig(8, 8, MurushroomsBlock.MAX_AGE, 100));

    private static <T extends Feature<?>> T register(String name, T entry) {
        return Registry.register(Registry.FEATURE, Enderscape.id(name), entry);
    }

    public static HeightRangePlacementModifier uniform(YOffset minOffset, YOffset maxOffset) {
        return HeightRangePlacementModifier.uniform(minOffset, maxOffset);
    }

    public static HeightRangePlacementModifier trapezoid(YOffset minOffset, YOffset maxOffset) {
        return HeightRangePlacementModifier.trapezoid(minOffset, maxOffset);
    }

    public static void init() {
    }
}