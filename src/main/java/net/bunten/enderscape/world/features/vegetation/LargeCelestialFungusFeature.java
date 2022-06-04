package net.bunten.enderscape.world.features.vegetation;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.PlantUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class LargeCelestialFungusFeature extends Feature<DefaultFeatureConfig> {
    public LargeCelestialFungusFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos pos = context.getOrigin();

        if (!world.isAir(pos)) {
            return false;
        } else {
            if (world.getBlockState(pos.down()).isIn(EnderscapeBlocks.LARGE_CELESTIAL_FUNGUS_GENERATABLE)) {
                PlantUtil.generateLargeCelestialFungus(world, random, pos, 16);
                return true;
            } else {
                return false;
            }
        }
    }
}