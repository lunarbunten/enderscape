package net.bunten.enderscape.world.features.vegetation;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.util.PlantUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class GrowthFeature extends Feature<GrowthConfig> {
    public GrowthFeature(Codec<GrowthConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<GrowthConfig> context) {
        var config = context.config();

        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        BlockPos pos = context.origin();

        return PlantUtil.generateGrowth(world, random, pos, config);
    }
}