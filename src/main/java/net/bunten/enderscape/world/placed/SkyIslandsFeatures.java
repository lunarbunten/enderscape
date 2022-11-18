package net.bunten.enderscape.world.placed;

import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.betterx.bclib.api.v3.levelgen.features.BCLFeature;
import org.betterx.bclib.api.v3.levelgen.features.BCLFeatureBuilder;

public class SkyIslandsFeatures extends FeaturesClass {

    public static final BCLFeature<?, ?> SKY_END_ISLAND = BCLFeatureBuilder.start(id("sky_islands", "island"), Feature.END_ISLAND)
    .configuration(new NoneFeatureConfiguration())
    .buildAndRegister()
    .place()

    .onceEvery(60)
    .squarePlacement()
    .modifier(uniform(VerticalAnchor.absolute(SKY_BIOME_HEIGHT), VerticalAnchor.absolute(SKY_BIOME_HEIGHT + 64)))
    .decoration(RAW_GENERATION)
    .onlyInBiome()

    .buildAndRegister();
}
