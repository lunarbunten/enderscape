package net.bunten.enderscape.world.placed;

import org.betterx.bclib.api.v3.levelgen.features.BCLFeature;
import org.betterx.bclib.api.v3.levelgen.features.BCLFeatureBuilder;

import net.bunten.enderscape.blocks.MurushroomsBlock;
import net.bunten.enderscape.world.EnderscapeFeatures;
import net.bunten.enderscape.world.features.vegetation.MurushroomFeatureConfig;

public class BarrenEndFeatures extends FeaturesClass {

    public static final BCLFeature<?, ?> MURUSHROOMS = BCLFeatureBuilder.start(id("barren", "end_murushrooms"), EnderscapeFeatures.MURUSHROOM)
    .configuration(new MurushroomFeatureConfig(8, 8, MurushroomsBlock.MAX_AGE, 100))
    .buildAndRegister()
    .place()

    .onEveryLayer(2)
    .squarePlacement()
    .modifier(FULL_RANGE)
    .decoration(VEGETAL_DECORATION)
    .onlyInBiome()

    .buildAndRegister();
}