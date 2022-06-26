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

        var horizontalRange = config.getHorizontalRange();
        var verticalRange = config.getHorizontalRange();
        var verticalCheckRange = config.getVerticalCheckRange();
        var baseHeight = config.getBaseHeight().get(random);
        var additionalHeight = config.getAdditionalHeight().get(random);
        var addedHeightChance = config.getAddedHeightChance();
        var tries = config.getTries();

        return PlantUtil.generateCelestialGrowth(world, random, pos, baseHeight, additionalHeight, addedHeightChance, horizontalRange, verticalRange, verticalCheckRange, tries);
    }
}