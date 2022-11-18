package net.bunten.enderscape.world.placed;

import org.betterx.bclib.api.v3.levelgen.features.BCLFeature;
import org.betterx.bclib.api.v3.levelgen.features.BCLFeatureBuilder;

import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.States;
import net.bunten.enderscape.world.EnderscapeFeatures;
import net.bunten.enderscape.world.features.ores.ScatteredOreFeatureConfig;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

public class GeneralEndFeatures extends FeaturesClass {

    /*
        Rocks
    */

    public static final BCLFeature<?, ?> VERADITE = BCLFeatureBuilder.start(id("general", "veradite"), Feature.ORE)
    .configuration(new OreConfiguration(new TagMatchTest(EnderscapeBlocks.ORE_REPLACEABLES), States.VERADITE, 24, 0))
    .buildAndRegister()
    .place()

    .count(2)
    .squarePlacement()
    .modifier(FULL_RANGE)
    .decoration(UNDERGROUND_ORES)
    .onlyInBiome()

    .buildAndRegister();

    public static final BCLFeature<?, ?> SCATTERED_VERADITE = BCLFeatureBuilder.start(id("general", "scattered_veradite"), EnderscapeFeatures.SCATTERED_ORE)
    .configuration(new ScatteredOreFeatureConfig(States.VERADITE, UniformInt.of(50, 80), UniformInt.of(-1, 1), ConstantInt.of(3)))
    .buildAndRegister()
    .place()

    .onceEvery(3)
    .squarePlacement()
    .modifier(FULL_RANGE)
    .decoration(UNDERGROUND_ORES)
    .onlyInBiome()

    .buildAndRegister();

    /*
        Ores
    */

    public static final BCLFeature<?, ?> SHADOW_QUARTZ_ORE = BCLFeatureBuilder.start(id("general", "shadow_quartz_ore"), Feature.ORE)
    .configuration(new OreConfiguration(new TagMatchTest(EnderscapeBlocks.ORE_REPLACEABLES), States.SHADOW_QUARTZ_ORE, 10, 0))
    .buildAndRegister()
    .place()

    .onceEvery(2)
    .squarePlacement()
    .modifier(FULL_RANGE)
    .decoration(UNDERGROUND_ORES)
    .onlyInBiome()

    .buildAndRegister();

    public static final BCLFeature<?, ?> SCATTERED_SHADOW_QUARTZ_ORE = BCLFeatureBuilder.start(id("general", "scattered_shadow_quartz_ore"), EnderscapeFeatures.SCATTERED_ORE)
    .configuration(new ScatteredOreFeatureConfig(States.SHADOW_QUARTZ_ORE, UniformInt.of(70, 90), UniformInt.of(-1, 1), ConstantInt.of(1)))
    .buildAndRegister()
    .place()

    .onEveryLayer(1)
    .squarePlacement()
    .modifier(FULL_RANGE)
    .decoration(UNDERGROUND_ORES)
    .onlyInBiome()

    .buildAndRegister();

    public static final BCLFeature<?, ?> NEBULITE_ORE = BCLFeatureBuilder.start(id("general", "nebulite_ore"), Feature.SCATTERED_ORE)
    .configuration(new OreConfiguration(new TagMatchTest(EnderscapeBlocks.ORE_REPLACEABLES), States.NEBULITE_ORE, 2, 0.5F))
    .buildAndRegister()
    .place()

    .onEveryLayer(1)
    .squarePlacement()
    .modifier(FULL_RANGE)
    .decoration(UNDERGROUND_ORES)
    .onlyInBiome()

    .buildAndRegister();

    public static final BCLFeature<?, ?> VOID_NEBULITE_ORE = BCLFeatureBuilder.start(id("general", "void_nebulite_ore"), EnderscapeFeatures.VOID_FACING_ORE)
    .configuration(new BlockStateConfiguration(States.NEBULITE_ORE))
    .buildAndRegister()
    .place()

    .count(2)
    .squarePlacement()
    .modifier(FULL_RANGE)
    .decoration(UNDERGROUND_ORES)
    .onlyInBiome()

    .buildAndRegister();
}