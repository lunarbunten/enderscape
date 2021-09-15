package net.enderscape.blocks;

import net.enderscape.interfaces.LayerMapped;
import net.enderscape.registry.EndBlocks;
import net.enderscape.util.Util;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

public class FlangerBerryVine extends Block implements LayerMapped, Fertilizable {
    private static final BooleanProperty ATTACHED = Properties.ATTACHED;

    public FlangerBerryVine(Settings settings) {
        super(settings);
        setDefaultState(getVineState(false));
    }

    private BlockState getBlockState(WorldView world, BlockPos pos) {
        return world.getBlockState(pos);
    }

    private void setBlockState(World world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, state, NOTIFY_LISTENERS);
    }

    private BlockState getVineState(boolean value) {
        return getDefaultState().with(ATTACHED, value);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        if (state.get(ATTACHED)) {
            return createCuboidShape(4, 0, 4, 12, 16, 12);
        } else {
            return createCuboidShape(4, 6, 4, 12, 16, 12);
        }
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.getStackInHand(hand).getItem().equals(getPickStack(world, pos, state).getItem())) {
            if (getBlockState(world, pos.down()).equals(getVineState(false))) {
                setBlockState(world, pos.down(), getVineState(true));
                setBlockState(world, pos.down(2), getVineState(false));
                BlockSoundGroup group = state.getSoundGroup();
                Util.playSound(world, pos, group.getPlaceSound(), SoundCategory.BLOCKS, (group.getVolume() + 1) / 2, group.getPitch() * 0.8F);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if (state.canPlaceAt(world, pos)) {
            if (getBlockState(world, pos.down()).isOf(EndBlocks.FLANGER_BERRY_BLOCK)) {
                return getVineState(true);
            } else if (getBlockState(world, pos.down()).isOf(this)) {
                return getVineState(true);
            } else {
                return getVineState(false);
            }
        } else {
            return Blocks.AIR.getDefaultState();
        }
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return getBlockState(world, pos.up()).isOf(EndBlocks.CELESTIAL_CAP) || getBlockState(world, pos.up()).isOf(this);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ATTACHED);
    }

    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return !state.get(ATTACHED);
    }

    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (getBlockState(world, pos.down()).isAir()) {
            if (getBlockState(world, pos.down(2)).isAir() && random.nextFloat() < 0.3F) {
                setBlockState(world, pos.down(), getVineState(false));
            } else {
                BlockSoundGroup group = EndBlocks.FLANGER_BERRY_BLOCK.getDefaultState().getSoundGroup();
                Util.playSound(world, pos, group.getPlaceSound(), SoundCategory.BLOCKS, 1, group.getPitch() * 0.8F);

                setBlockState(world, pos.down(), EndBlocks.FLANGER_BERRY_BLOCK.getDefaultState().with(EndProperties.BERRY_STAGE, BerryStage.FLOWER));
                setBlockState(world, pos, getVineState(true));
            }
        }
    }

    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return type == NavigationType.AIR && !collidable || super.canPathfindThrough(state, world, pos, type);
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextInt(20) == 0) {
            grow(world, random, pos, state);
        }
    }

    @Override
    public LayerType getLayerType() {
        return LayerType.CUTOUT;
    }
}