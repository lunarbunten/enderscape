package net.enderscape.world.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

import java.util.Random;

public class EndIslandFeature extends AbstractIslandFeature {
    public EndIslandFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    protected double widthDivider() {
        return 1;
    }

    @Override
    protected double getSize(Random random) {
        return random.nextInt(4) + 3;
    }

    @Override
    protected BlockState getTopBlock(WorldAccess world, BlockPos pos) {
        return Blocks.END_STONE.getDefaultState();
    }

    @Override
    protected BlockState getBottomBlock(WorldAccess world, BlockPos pos) {
        return Blocks.END_STONE.getDefaultState();
    }

    @Override
    protected void decorate(StructureWorldAccess world, Random random, BlockPos pos) {
    }
}