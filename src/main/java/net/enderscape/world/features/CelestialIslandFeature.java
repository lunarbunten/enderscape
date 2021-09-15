package net.enderscape.world.features;

import com.mojang.serialization.Codec;
import net.enderscape.registry.EndBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

import java.util.Random;

public class CelestialIslandFeature extends AbstractIslandFeature {
    public CelestialIslandFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    protected double widthDivider() {
        return 3;
    }

    @Override
    protected double getSize(Random random) {
        return random.nextInt(12) + 6;
    }

    @Override
    protected BlockState getTopBlock(WorldAccess world, BlockPos pos) {
        return EndBlocks.CELESTIAL_MYCELIUM_BLOCK.getDefaultState();
    }

    @Override
    protected BlockState getBottomBlock(WorldAccess world, BlockPos pos) {
        return Blocks.END_STONE.getDefaultState();
    }

    @Override
    protected void decorate(StructureWorldAccess world, Random random, BlockPos pos) {
        ChorusFlowerBlock.generate(world, pos.up(), random, (int) getSize(random) * 3);
        world.setBlockState(pos, getTopBlock(world, pos), 2);
    }
}