package net.bunten.enderscape.blocks;

import net.bunten.enderscape.interfaces.LayerMapped;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.MathUtil;
import net.bunten.enderscape.util.PlantUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BulbFlowerBlock extends PlantBlock implements LayerMapped, Fertilizable {
    public BulbFlowerBlock(Settings settings) {
        super(settings);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        return createCuboidShape(2, 0, 2, 14, 13, 14);
    }

    @Override
    public boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isIn(EnderscapeBlocks.BULB_FLOWER_PLANTABLE_ON);
    }

    @Override
    public LayerType getLayerType() {
        return LayerType.CUTOUT;
    }

    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        var range = MathUtil.nextInt(random, 3, 10);
        var tries = MathUtil.nextInt(random, 3, 8);
        
        PlantUtil.generateCelestialVegetation(world, random, pos, range, 3, 3, tries);
    }
}