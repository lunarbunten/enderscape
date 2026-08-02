package net.penumbra.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.penumbra.enderscape.registry.tag.EnderscapeBlockTags;

public class WispGrowthBlock extends VegetationBlock {

    public static final MapCodec<WispGrowthBlock> CODEC = simpleCodec(WispGrowthBlock::new);
    private static final VoxelShape VOXEL_SHAPE = box(1, 0, 1, 14, 15, 14);

    public WispGrowthBlock(Properties settings) {
        super(settings);
    }

    @Override
    public MapCodec<WispGrowthBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(EnderscapeBlockTags.SUPPORTS_WISP_GROWTH) && state.isFaceSturdy(level, pos, Direction.UP);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return VOXEL_SHAPE;
    }
}