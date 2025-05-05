package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.block.properties.DirectionProperties;
import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.bunten.enderscape.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WispGrowthBlock extends DirectionalPlantBlock {

    public WispGrowthBlock(Properties settings) {
        super(DirectionProperties.create().up(), settings);
    }

    public static final MapCodec<WispGrowthBlock> CODEC = simpleCodec(WispGrowthBlock::new);

    @Override
    public MapCodec<WispGrowthBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean canPlantOn(BlockState state, BlockState floor, BlockGetter level, BlockPos pos, Direction facing) {
        return floor.is(EnderscapeBlockTags.VEILED_WOODLANDS_VEGETATION_PLANTABLE_ON) && floor.isFaceSturdy(level, pos, facing);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return BlockUtil.createRotatedShape(1, 0, 1, 14, 15, 14, getFacing(state));
    }
}