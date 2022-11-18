package net.bunten.enderscape.world.placed;

import org.betterx.bclib.api.v3.levelgen.features.BCLFeature;
import org.betterx.bclib.api.v3.levelgen.features.BCLFeatureBuilder;

import net.bunten.enderscape.blocks.BlinklightVinesHeadBlock;
import net.bunten.enderscape.blocks.GrowthBlock;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.States;
import net.bunten.enderscape.world.EnderscapeFeatures;
import net.bunten.enderscape.world.features.vegetation.GrowthConfig;
import net.bunten.enderscape.world.features.vegetation.LargeMurushroomFeatureConfig;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

public class CorruptDepthsFeatures extends FeaturesClass {

    public static final BCLFeature<?, ?> CORRUPT_GROWTH = BCLFeatureBuilder.start(id("corrupt_depths", "growth"), EnderscapeFeatures.GROWTH)
    .configuration(new GrowthConfig(States.CORRUPT_GROWTH.setValue(GrowthBlock.FACING, Direction.DOWN), ConstantInt.of(1), UniformInt.of(1, 2), 0.5F))
    .buildAndRegister()
    .place()

    .count(35)
    .squarePlacement()
    .modifier(VOID_RANGE)
    .modifier(environmentScan(Direction.UP, BlockPredicate.matchesTag(EnderscapeBlocks.PLANTABLE_GROWTH), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12))
    .modifier(verticalOffset(ConstantInt.of(-1)))
    .decoration(VEGETAL_DECORATION)
    .onlyInBiome()

    .buildAndRegister();

    public static final BCLFeature<?, ?> TALL_CORRUPT_GROWTH = BCLFeatureBuilder.start(id("corrupt_depths", "tall_growth"), EnderscapeFeatures.GROWTH)
    .configuration(new GrowthConfig(States.CORRUPT_GROWTH.setValue(GrowthBlock.FACING, Direction.DOWN), UniformInt.of(4, 8)))
    .buildAndRegister()
    .place()

    .modifier(noiseCount(35, 6, 0))
    .squarePlacement()
    .modifier(VOID_RANGE)
    .modifier(environmentScan(Direction.UP, BlockPredicate.matchesTag(EnderscapeBlocks.PLANTABLE_GROWTH), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12))
    .modifier(verticalOffset(ConstantInt.of(-1)))
    .decoration(VEGETAL_DECORATION)
    .onlyInBiome()

    .buildAndRegister();

    public static final BCLFeature<?, ?> BLINKLIGHT_VINES = BCLFeatureBuilder.startColumn(id("corrupt_depths", "blinklight_vines"))
    .allowedPlacement(BlockPredicate.ONLY_IN_AIR_PREDICATE)
    .direction(Direction.DOWN)
    .add(UniformInt.of(4, 15), States.BLINKLIGHT_BODY)
    .add(1, States.BLINKLIGHT_HEAD.setValue(BlinklightVinesHeadBlock.AGE, BlinklightVinesHeadBlock.MAX_AGE))
    .prioritizeTip()

    .buildAndRegister()
    .place()

    .modifier(noiseCount(16, 4, -0.36))
    .onceEvery(1)
    .squarePlacement()
    .modifier(VOID_RANGE)
    .modifier(environmentScan(Direction.UP, BlockPredicate.hasSturdyFace(Direction.DOWN), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12))
    .modifier(verticalOffset(ConstantInt.of(-1)))
    .decoration(VEGETAL_DECORATION)
    .onlyInBiome()

    .buildAndRegister();

    public static final BCLFeature<?, ?> LARGE_MURUSHROOM = BCLFeatureBuilder.start(id("corrupt_depths", "large_murushroom"), EnderscapeFeatures.LARGE_MURUSHROOM)
    .configuration(new LargeMurushroomFeatureConfig(UniformInt.of(15, 30), UniformInt.of(4, 6), 32))
    .buildAndRegister()
    .place()

    .onceEvery(1)
    .squarePlacement()
    .modifier(VOID_RANGE)
    .decoration(VEGETAL_DECORATION)
    .onlyInBiome()
    
    .buildAndRegister();

    public static final BCLFeature<?, ?> VOID_NEBULITE_ORE = BCLFeatureBuilder.start(id("corrupt_depths", "void_nebulite_ore"), EnderscapeFeatures.VOID_FACING_ORE)
    .configuration(new BlockStateConfiguration(States.NEBULITE_ORE))
    .buildAndRegister()
    .place()

    .count(4)
    .squarePlacement()
    .modifier(FULL_RANGE)
    .decoration(UNDERGROUND_ORES)
    .onlyInBiome()

    .buildAndRegister();
}