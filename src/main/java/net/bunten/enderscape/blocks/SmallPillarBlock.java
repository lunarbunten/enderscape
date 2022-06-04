package net.bunten.enderscape.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class SmallPillarBlock extends PillarBlock {
    public SmallPillarBlock(Settings settings) {
        super(settings);
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (state.get(AXIS)) {
            default: {
                return createCuboidShape(0, 4, 4, 16, 12, 12);
            }
            case Z: {
                return createCuboidShape(4, 4, 0, 12, 12, 16);
            }
            case Y: 
        }
        return createCuboidShape(4, 0, 4, 12, 16, 12);
    }
}