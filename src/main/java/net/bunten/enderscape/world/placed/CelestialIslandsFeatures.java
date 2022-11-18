package net.bunten.enderscape.world.placed;

import org.betterx.bclib.api.v3.levelgen.features.BCLFeature;
import org.betterx.bclib.api.v3.levelgen.features.BCLFeatureBuilder;

import net.bunten.enderscape.world.EnderscapeFeatures;
import net.bunten.enderscape.world.features.CelestialIslandFeatureConfig;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;

public class CelestialIslandsFeatures extends FeaturesClass {

    public static final BCLFeature<?, ?> ISLAND = BCLFeatureBuilder.start(id("celestial_islands", "island"), EnderscapeFeatures.CELESTIAL_ISLAND)
    .configuration(new CelestialIslandFeatureConfig(UniformInt.of(3, 5), UniformInt.of(8, 13), 0.25F))
    .buildAndRegister()
    .place()

    .onceEvery(12)
    .squarePlacement()
    .modifier(uniform(VerticalAnchor.absolute(55), VerticalAnchor.absolute(70)))
    .decoration(RAW_GENERATION)
    .onlyInBiome()

    .buildAndRegister();
}