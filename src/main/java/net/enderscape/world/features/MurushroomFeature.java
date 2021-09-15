package net.enderscape.world.features;

import com.mojang.serialization.Codec;
import net.enderscape.blocks.MurushroomsBlock;
import net.enderscape.registry.EndBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

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
            MurushroomsBlock.generate(EndBlocks.MURUSHROOMS.getDefaultState(), world, pos, random, 500, 8);
            return true;
        }
    }
}