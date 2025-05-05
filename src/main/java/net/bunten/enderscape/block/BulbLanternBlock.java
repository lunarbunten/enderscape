package net.bunten.enderscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BulbLanternBlock extends LanternBlock {
    public BulbLanternBlock(Properties settings) {
        super(settings);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (state.getValue(HANGING)) {
            return Shapes.join(box(5, 2, 5, 11, 8, 11), box(3, 6, 3, 13, 10, 13), BooleanOp.OR);
        } else {
            return Shapes.join(box(3, 4, 3, 13, 8, 13), box(5, 0, 5, 11, 6, 11), BooleanOp.OR);
        }
    }
}