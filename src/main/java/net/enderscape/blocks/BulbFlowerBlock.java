package net.enderscape.blocks;

import net.enderscape.interfaces.LayerMapped;
import net.enderscape.registry.EndBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class BulbFlowerBlock extends PlantBlock implements LayerMapped {
    public BulbFlowerBlock(Settings settings) {
        super(settings);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        return createCuboidShape(2, 0, 2, 14, 13, 14);
    }

    @Override
    public boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isIn(EndBlocks.GROWTH_PLANTABLE_BLOCKS);
    }

    @Override
    public LayerType getLayerType() {
        return LayerType.CUTOUT;
    }
}