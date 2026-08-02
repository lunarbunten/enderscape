package net.penumbra.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.penumbra.enderscape.block.properties.DirectionSet;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;

public class VeiledVinesBlock extends AbstractGrowthBlock {

    public static final MapCodec<VeiledVinesBlock> CODEC = simpleCodec(VeiledVinesBlock::new);

    public VeiledVinesBlock(Properties settings) {
        super(DirectionSet.create().up(), settings);
    }

    @Override
    protected MapCodec<? extends VeiledVinesBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean canPlantOn(BlockState state, BlockState floor, BlockGetter level, BlockPos pos, Direction facing) {
        return floor.isFaceSturdy(level, pos, facing) || floor.is(EnderscapeBlocks.VEILED_LEAVES) || hasGrowthSupport(state, floor);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (random.nextFloat() > 0.12F) return;
        level.addParticle(EnderscapeParticles.VEILED_LEAVES, pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble(), 0.25, random.nextGaussian() * 0.025, 0.25);
    }
}