package net.bunten.enderscape.world.features.vegetation;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.util.PlantUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class CelestialGrowthFeature extends Feature<DefaultFeatureConfig> {
    public CelestialGrowthFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos pos = context.getOrigin();

        return PlantUtil.generateCelestialGrowth(world, random, world.getTopPosition(Type.WORLD_SURFACE_WG, pos), 4, 2, 10);
    }
}