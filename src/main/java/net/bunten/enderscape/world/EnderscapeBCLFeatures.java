package net.bunten.enderscape.world;

import org.betterx.bclib.api.v3.levelgen.features.BCLFeature;
import org.betterx.bclib.api.v3.levelgen.features.BCLFeatureBuilder;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.blocks.MurushroomsBlock;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.States;
import net.bunten.enderscape.world.features.CelestialIslandFeatureConfig;
import net.bunten.enderscape.world.features.vegetation.CelestialGrowthFeatureConfig;
import net.bunten.enderscape.world.features.vegetation.CelestialVegetationFeatureConfig;
import net.bunten.enderscape.world.features.vegetation.LargeCelestialFungusFeatureConfig;
import net.bunten.enderscape.world.features.vegetation.MurushroomFeatureConfig;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;

public class EnderscapeBCLFeatures {

    private static final GenerationStep.Feature RAW_GENERATION = GenerationStep.Feature.RAW_GENERATION;
    private static final GenerationStep.Feature UNDERGROUND_ORES = GenerationStep.Feature.UNDERGROUND_ORES;
    private static final GenerationStep.Feature VEGETAL_DECORATION = GenerationStep.Feature.VEGETAL_DECORATION;

    // Celestial Plains

    public static final BCLFeature<?, ?> LARGE_CELESTIAL_FUNGUS = BCLFeatureBuilder.start(Enderscape.id("large_celestial_fungus"), EnderscapeFeatures.LARGE_CELESTIAL_FUNGUS)
    .configuration(new LargeCelestialFungusFeatureConfig(UniformIntProvider.create(10, 35), 4, 1, 0.75F, 1, 64, 16))
    .buildAndRegister()
    .place()

    .count(68)
    .squarePlacement()
    .onlyInBiome()
    .modifier(PlacedFeatures.EIGHT_ABOVE_AND_BELOW_RANGE)
    .decoration(VEGETAL_DECORATION)

    .buildAndRegister();

    public static final BCLFeature<?, ?> CELESTIAL_GROWTH = BCLFeatureBuilder.start(Enderscape.id("celestial_growth"), EnderscapeFeatures.CELESTIAL_GROWTH)
    .configuration(new CelestialGrowthFeatureConfig(24, 4, 4, UniformIntProvider.create(1, 1), UniformIntProvider.create(1, 2), 0.5F, 30))
    .buildAndRegister()
    .place()

    .onEveryLayer(10)
    .squarePlacement()
    .onlyInBiome()
    .modifier(PlacedFeatures.EIGHT_ABOVE_AND_BELOW_RANGE)
    .decoration(VEGETAL_DECORATION)

    .buildAndRegister();

    public static final BCLFeature<?, ?> UNCOMMON_CELESTIAL_GROWTH = BCLFeatureBuilder.start(Enderscape.id("uncommon_celestial_growth"), EnderscapeFeatures.CELESTIAL_GROWTH)
    .configuration(new CelestialGrowthFeatureConfig(24, 4, 4, UniformIntProvider.create(1, 1), UniformIntProvider.create(1, 2), 0.5F, 30))
    .buildAndRegister()
    .place()

    .onceEvery(16)
    .squarePlacement()
    .onlyInBiome()
    .modifier(PlacedFeatures.EIGHT_ABOVE_AND_BELOW_RANGE)
    .decoration(VEGETAL_DECORATION)

    .buildAndRegister();

    public static final BCLFeature<?, ?> MURUSHROOMS = BCLFeatureBuilder.start(Enderscape.id("murushrooms"), EnderscapeFeatures.MURUSHROOM)
    .configuration(new MurushroomFeatureConfig(8, 8, MurushroomsBlock.MAX_AGE, 100))
    .buildAndRegister()
    .place()

    .onEveryLayer(2)
    .squarePlacement()
    .onlyInBiome()
    .modifier(PlacedFeatures.BOTTOM_TO_TOP_RANGE)
    .decoration(VEGETAL_DECORATION)

    .buildAndRegister();

    public static final BCLFeature<?, ?> UNCOMMON_MURUSHROOMS = BCLFeatureBuilder.start(Enderscape.id("uncommon_murushrooms"), EnderscapeFeatures.MURUSHROOM)
    .configuration(new MurushroomFeatureConfig(8, 8, MurushroomsBlock.MAX_AGE, 100))
    .buildAndRegister()
    .place()

    .onceEvery(2)
    .squarePlacement()
    .onlyInBiome()
    .modifier(PlacedFeatures.BOTTOM_TO_TOP_RANGE)
    .decoration(VEGETAL_DECORATION)

    .buildAndRegister();

    public static final BCLFeature<?, ?> CELESTIAL_VEGETATION = BCLFeatureBuilder.start(Enderscape.id("celestial_vegetation"), EnderscapeFeatures.CELESTIAL_VEGETATION)
    .configuration(new CelestialVegetationFeatureConfig(16, 3, 3, 8))
    .buildAndRegister()
    .place()

    .onEveryLayer(8)
    .squarePlacement()
    .onlyInBiome()
    .modifier(PlacedFeatures.EIGHT_ABOVE_AND_BELOW_RANGE)
    .decoration(VEGETAL_DECORATION)

    .buildAndRegister();

    // Celestial Islands

    public static final BCLFeature<?, ?> CELESTIAL_ISLAND = BCLFeatureBuilder.start(Enderscape.id("celestial_island"), EnderscapeFeatures.CELESTIAL_ISLAND)
    .configuration(new CelestialIslandFeatureConfig(UniformIntProvider.create(3, 5), UniformIntProvider.create(8, 13), 0.25F))
    .buildAndRegister()
    .place()

    .onceEvery(12)
    .squarePlacement()
    .onlyInBiome()
    .modifier(uniform(YOffset.fixed(55), YOffset.fixed(70)))
    .decoration(RAW_GENERATION)

    .buildAndRegister();

    // Ores

    public static final BCLFeature<?, ?> SHADOW_QUARTZ_ORE = BCLFeatureBuilder.start(Enderscape.id("shadow_quartz_ore"), Feature.ORE)
    .configuration(new OreFeatureConfig(new TagMatchRuleTest(EnderscapeBlocks.ORE_REPLACEABLES), States.SHADOW_QUARTZ_ORE, 10, 0))
    .buildAndRegister()
    .place()

    .onceEvery(3)
    .squarePlacement()
    .modifier(PlacedFeatures.BOTTOM_TO_TOP_RANGE)
    .decoration(UNDERGROUND_ORES)

    .buildAndRegister();

    public static final BCLFeature<?, ?> SCATTERED_SHADOW_QUARTZ_ORE = BCLFeatureBuilder.start(Enderscape.id("scattered_shadow_quartz_ore"), EnderscapeFeatures.SCATTERED_ORE)
    .configuration(new SingleStateFeatureConfig(States.SHADOW_QUARTZ_ORE))
    .buildAndRegister()
    .place()

    .onEveryLayer(1)
    .squarePlacement()
    .modifier(PlacedFeatures.BOTTOM_TO_TOP_RANGE)
    .decoration(UNDERGROUND_ORES)

    .buildAndRegister();

    public static final BCLFeature<?, ?> NEBULITE_ORE = BCLFeatureBuilder.start(Enderscape.id("nebulite_ore"), Feature.SCATTERED_ORE)
    .configuration(new OreFeatureConfig(new TagMatchRuleTest(EnderscapeBlocks.ORE_REPLACEABLES), States.NEBULITE_ORE, 2, 0.5F))
    .buildAndRegister()
    .place()

    .onEveryLayer(1)
    .squarePlacement()
    .modifier(PlacedFeatures.BOTTOM_TO_TOP_RANGE)
    .decoration(UNDERGROUND_ORES)

    .buildAndRegister();

    public static final BCLFeature<?, ?> VOID_NEBULITE_ORE = BCLFeatureBuilder.start(Enderscape.id("void_nebulite_ore"), EnderscapeFeatures.VOID_FACING_ORE)
    .configuration(new SingleStateFeatureConfig(States.NEBULITE_ORE))
    .buildAndRegister()
    .place()

    .count(2)
    .squarePlacement()
    .modifier(PlacedFeatures.BOTTOM_TO_TOP_RANGE)
    .decoration(UNDERGROUND_ORES)

    .buildAndRegister();

    public static HeightRangePlacementModifier uniform(YOffset minOffset, YOffset maxOffset) {
        return HeightRangePlacementModifier.uniform(minOffset, maxOffset);
    }

    public static HeightRangePlacementModifier trapezoid(YOffset minOffset, YOffset maxOffset) {
        return HeightRangePlacementModifier.trapezoid(minOffset, maxOffset);
    }
}