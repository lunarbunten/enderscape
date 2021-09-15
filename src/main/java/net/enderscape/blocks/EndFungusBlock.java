package net.enderscape.blocks;

import java.util.Random;

import net.enderscape.interfaces.LayerMapped;
import net.enderscape.registry.EndBlocks;
import net.enderscape.util.FungusType;
import net.enderscape.world.features.CelestialFungusFeature;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class EndFungusBlock extends PlantBlock implements Fertilizable, LayerMapped {
    public static final IntProperty STAGE = Properties.STAGE;
    private final FungusType type;

    public EndFungusBlock(FungusType type, Settings settings) {
        super(settings);
        this.type = type;
        setDefaultState(stateManager.getDefaultState().with(STAGE, 0));
    }

    @Override
    public boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isIn(EndBlocks.END_MYCELIUM_BLOCKS) || floor.isIn(BlockTags.MUSHROOM_GROW_BLOCK);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        return type == FungusType.CELESTIAL ? createCuboidShape(4, 0, 4, 12, 9, 12) : createCuboidShape(2, 0, 2, 14, 12, 14);
    }

    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return random.nextFloat() < 0.45;
    }

    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (state.get(STAGE) == 0) {
            world.setBlockState(pos, state.cycle(STAGE), 4);
        } else {
            switch (type) {
                case CELESTIAL: 
                for (int i = 0; i < 16; i++) {
                        int height = CelestialFungusFeature.getHeight(random);
                        if (CelestialFungusFeature.isEnoughAir(world, pos, height)) {
                            CelestialFungusFeature.generate(world, random, pos, height);
                            break;
                        }
                    }
                    break;
                default: break;
            };
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    public LayerType getLayerType() {
        return LayerType.CUTOUT;
    }
}