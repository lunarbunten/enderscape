package net.bunten.enderscape.world.features.vegetation;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.util.PlantUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class CelestialVegetationFeature extends Feature<DefaultFeatureConfig> {
    public CelestialVegetationFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        Random random = context.getRandom();
        BlockPos pos = context.getOrigin();
        StructureWorldAccess world = context.getWorld();

        return PlantUtil.generateCelestialVegetation(world, random, pos, 16, 3, 32);
    }
}