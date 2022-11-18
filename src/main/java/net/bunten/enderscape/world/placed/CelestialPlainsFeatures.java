package net.bunten.enderscape.world.placed;

import org.betterx.bclib.api.v3.levelgen.features.BCLFeature;
import org.betterx.bclib.api.v3.levelgen.features.BCLFeatureBuilder;

import net.bunten.enderscape.blocks.GrowthBlock;
import net.bunten.enderscape.blocks.MurushroomsBlock;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.States;
import net.bunten.enderscape.world.EnderscapeFeatures;
import net.bunten.enderscape.world.features.vegetation.GrowthConfig;
import net.bunten.enderscape.world.features.vegetation.LargeCelestialFungusFeatureConfig;
import net.bunten.enderscape.world.features.vegetation.MurushroomFeatureConfig;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;

public class CelestialPlainsFeatures extends FeaturesClass {

    public static final BCLFeature<?, ?> LARGE_FUNGUS = BCLFeatureBuilder.start(id("celestial_plains", "large_fungus"), EnderscapeFeatures.LARGE_CELESTIAL_FUNGUS)
    .configuration(new LargeCelestialFungusFeatureConfig(UniformInt.of(10, 35), 4, 0.75F, 1, 64, 16))
    .buildAndRegister()
    .place()

    .onEveryLayer(68)
    .squarePlacement()
    .onlyInBiome()
    .modifier(FULL_RANGE)
    .decoration(VEGETAL_DECORATION)

    .buildAndRegister();

    public static final BCLFeature<?, ?> GROWTH = BCLFeatureBuilder.start(id("celestial_plains", "growth"), EnderscapeFeatures.GROWTH)
    .configuration(new GrowthConfig(States.CELESTIAL_GROWTH.setValue(GrowthBlock.FACING, Direction.UP), ConstantInt.of(1), UniformInt.of(1, 2), 0.5F))
    .buildAndRegister()
    .place()

    .onEveryLayer(130)
    .squarePlacement()
    .modifier(FULL_RANGE)
    .modifier(environmentScan(Direction.DOWN, BlockPredicate.matchesTag(EnderscapeBlocks.PLANTABLE_GROWTH), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12))
    .modifier(verticalOffset(ConstantInt.of(1)))
    .decoration(VEGETAL_DECORATION)
    .onlyInBiome()

    .buildAndRegister();

    public static final BCLFeature<?, ?> FUNGUS = BCLFeatureBuilder.start(id("celestial_plains", "fungus"), EnderscapeBlocks.CELESTIAL_FUNGUS)
    .buildAndRegister()
    .place()

    .onEveryLayer(25)
    .squarePlacement()
    .modifier(FULL_RANGE)
    .modifier(environmentScan(Direction.DOWN, BlockPredicate.matchesTag(EnderscapeBlocks.PLANTABLE_FUNGUS), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12))
    .modifier(verticalOffset(ConstantInt.of(1)))
    .decoration(VEGETAL_DECORATION)
    .onlyInBiome()

    .buildAndRegister();
    
    public static final BCLFeature<?, ?> BULB_FLOWER = BCLFeatureBuilder.start(id("celestial_plains", "bulb_flower"), EnderscapeBlocks.BULB_FLOWER)
    .buildAndRegister()
    .place()

    .modifier(noiseCount(100, 10, -0.2))
    .squarePlacement()
    .modifier(FULL_RANGE)
    .modifier(environmentScan(Direction.DOWN, BlockPredicate.matchesTag(EnderscapeBlocks.PLANTABLE_BULB_FLOWER), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12))
    .modifier(verticalOffset(ConstantInt.of(1)))
    .decoration(VEGETAL_DECORATION)
    .onlyInBiome()

    .buildAndRegister();

    public static final BCLFeature<?, ?> MURUSHROOMS = BCLFeatureBuilder.start(id("celestial_plains", "murushrooms"), EnderscapeFeatures.MURUSHROOM)
    .configuration(new MurushroomFeatureConfig(8, 8, MurushroomsBlock.MAX_AGE, 100))
    .buildAndRegister()
    .place()

    .onceEvery(2)
    .squarePlacement()
    .onlyInBiome()
    .modifier(FULL_RANGE)
    .decoration(VEGETAL_DECORATION)

    .buildAndRegister();
}