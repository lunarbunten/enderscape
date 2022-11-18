package net.bunten.enderscape.world.features.vegetation;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.world.generator.LargeCelestialFungusGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class LargeCelestialFungusFeature extends Feature<LargeCelestialFungusFeatureConfig> {
    public LargeCelestialFungusFeature(Codec<LargeCelestialFungusFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<LargeCelestialFungusFeatureConfig> context) {
        var config = context.config();

        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        BlockPos pos = context.origin();

        if (!world.isEmptyBlock(pos)) {
            return false;
        } else {
            if (world.getBlockState(pos.below()).is(EnderscapeBlocks.LARGE_CELESTIAL_FUNGUS_GENERATABLE)) {
                return LargeCelestialFungusGenerator.tryGenerate(world, random, pos, config);
            } else {
                return false;
            }
        }
    }
}