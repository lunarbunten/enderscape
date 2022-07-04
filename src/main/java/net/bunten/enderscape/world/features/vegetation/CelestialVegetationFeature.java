package net.bunten.enderscape.world.features.vegetation;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.util.PlantUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
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

        var horizontalRange = config.horizontalRange;
        var verticalRange = config.verticalRange;
        var verticalCheckRange = config.verticalCheckRange;
        var tries = config.tries;

        return PlantUtil.generateCelestialVegetation(world, random, pos, horizontalRange, verticalRange, verticalCheckRange, tries);
    }
}