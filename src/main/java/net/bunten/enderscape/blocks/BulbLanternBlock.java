package net.bunten.enderscape.blocks;

import net.bunten.enderscape.interfaces.LayerMapped;
import net.minecraft.block.BlockState;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class BulbLanternBlock extends LanternBlock implements LayerMapped {
    public BulbLanternBlock(Settings settings) {
        super(settings);
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(HANGING)) {
            return VoxelShapes.combineAndSimplify(createCuboidShape(5, 2, 5, 11, 8, 11), createCuboidShape(2, 6, 2, 14, 10, 14), BooleanBiFunction.OR);
        } else {
            return VoxelShapes.combineAndSimplify(createCuboidShape(2, 4, 2, 14, 8, 14), createCuboidShape(5, 0, 5, 11, 6, 11), BooleanBiFunction.OR);
        }
    }

    @Override
    public LayerType getLayerType() {
        return LayerType.CUTOUT;
    }
}