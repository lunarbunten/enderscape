package net.bunten.enderscape.world.features.vegetation;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.blocks.MurushroomsBlock;
import net.bunten.enderscape.util.PlantUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class MurushroomFeature extends Feature<DefaultFeatureConfig> {
    public MurushroomFeature(Codec<DefaultFeatureConfig> codec) {
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
            return PlantUtil.generateMurushrooms(world, pos, random, MurushroomsBlock.MAX_AGE, 8, 8, 300);
        }
    }
}