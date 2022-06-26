package net.bunten.enderscape.world.features.vegetation;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.util.PlantUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class CelestialVegetationFeature extends Feature<CelestialVegetationFeatureConfig> {
    public CelestialVegetationFeature(Codec<CelestialVegetationFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<CelestialVegetationFeatureConfig> context) {
        var config = context.getConfig();

        Random random = context.getRandom();
        BlockPos pos = context.getOrigin();
        StructureWorldAccess world = context.getWorld();

        var horizontalRange = config.getHorizontalRange();
        var verticalRange = config.getVerticalRange();
        var verticalCheckRange = config.getVerticalCheckRange();
        var tries = config.getTries();

        return PlantUtil.generateCelestialVegetation(world, random, pos, horizontalRange, verticalRange, verticalCheckRange, tries);
    }
}