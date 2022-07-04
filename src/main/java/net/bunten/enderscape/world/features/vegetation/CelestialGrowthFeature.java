package net.bunten.enderscape.world.features.vegetation;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.util.PlantUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class CelestialGrowthFeature extends Feature<CelestialGrowthFeatureConfig> {
    public CelestialGrowthFeature(Codec<CelestialGrowthFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<CelestialGrowthFeatureConfig> context) {
        var config = context.getConfig();

        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos pos = context.getOrigin();

        var horizontalRange = config.horizontalRange;
        var verticalRange = config.verticalRange;
        var verticalCheckRange = config.verticalCheckRange;
        var baseHeight = config.baseHeight;
        var additionalHeight = config.additionalHeight;
        var addedHeightChance = config.addedHeightChance;
        var tries = config.tries;

        return PlantUtil.generateCelestialGrowth(world, random, pos, baseHeight, additionalHeight, addedHeightChance, horizontalRange, verticalRange, verticalCheckRange, tries);
    }
}