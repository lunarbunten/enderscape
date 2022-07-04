package net.bunten.enderscape.blocks;

import net.bunten.enderscape.blocks.properties.EnderscapeProperties;
import net.bunten.enderscape.blocks.properties.FlangerBerryStage;
import net.bunten.enderscape.interfaces.LayerMapped;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.util.States;
import net.bunten.enderscape.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.TransparentBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.tag.EntityTypeTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;

public class FlangerBerryBlock extends TransparentBlock implements LayerMapped, Fertilizable {
    
    public static final EnumProperty<FlangerBerryStage> STAGE = EnderscapeProperties.FLANGER_BERRY_STAGE;

    public FlangerBerryBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(STAGE, FlangerBerryStage.RIPE));
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        if (stateFrom.isOf(this)) {
            return isRipe(stateFrom) ? true : false;
        } else {
            return super.isSideInvisible(state, stateFrom, direction);
        }
    }

    @Override
    public BlockSoundGroup getSoundGroup(BlockState state) {
        return switch (state.get(STAGE)) {
            case FLOWER -> EnderscapeSounds.FLANGER_FLOWER;
            case UNRIPE -> EnderscapeSounds.FLANGER_BERRY_BLOCK;
            default -> EnderscapeSounds.FLANGER_BERRY_BLOCK;
        };
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    private boolean isFlower(BlockState state) {
        return state.get(STAGE) == FlangerBerryStage.FLOWER;
    }

    private boolean isRipe(BlockState state) {
        return state.get(STAGE) == FlangerBerryStage.RIPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(STAGE) != FlangerBerryStage.FLOWER ? state.getOutlineShape(world, pos) : VoxelShapes.empty();
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(STAGE)) {
            case FLOWER -> createCuboidShape(2, 10, 2, 14, 16, 14);
            case UNRIPE -> createCuboidShape(2, 4, 2, 14, 16, 14);
            default -> VoxelShapes.fullCube();
        };
    }

    /**
     * Gets the amount of time in ticks this block will wait before attempting to start falling.
     */
    protected int getFallDelay() {
        return 2;
    }

    protected boolean canFall(World world, BlockState state, BlockPos pos) {
        boolean bl = false;
        if (FallingBlock.canFallThrough(world.getBlockState(pos.down()))) {
            if (isRipe(state) && world.getBlockState(pos.up()).getBlock() != EnderscapeBlocks.FLANGER_BERRY_VINE) {
                bl = true;
            }
        }
        if (pos.getY() < world.getBottomY()) {
            bl = false;
        }
        return bl;
    }
    
    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        world.createAndScheduleBlockTick(pos, this, getFallDelay());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        world.createAndScheduleBlockTick(pos, this, getFallDelay());
        if (!canPlaceAt(state, world, pos)) {
            return States.AIR;
        } else {
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (canFall(world, state, pos)) {
            FallingBlockEntity.spawnFromBlock(world, pos, state);
        }
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return isRipe(state) ? true : world.getBlockState(pos.up()).isOf(EnderscapeBlocks.FLANGER_BERRY_VINE);
    }

    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        if (!isFlower(state)) {
            if (projectile.getType().isIn(EntityTypeTags.IMPACT_PROJECTILES)) {
                BlockPos pos = hit.getBlockPos();
                world.breakBlock(pos, true, projectile);
            } else {
                if (isRipe(state)) {
                    if (projectile instanceof FireworkRocketEntity) {
                        BlockPos pos = hit.getBlockPos();
                        dropStacks(state, world, pos);
                        world.breakBlock(pos, true, projectile);
                    }
                }
            }
        }
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextInt(12) == 0 && canGrow(world, random, pos, state)) {
            grow(world, random, pos, state);
        }
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return !isRipe(state);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return !isRipe(state) && state.canPlaceAt(world, pos);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (!isRipe(state)) {
            BlockState cycleStage = state.cycle(STAGE);
            world.setBlockState(pos, cycleStage, NOTIFY_ALL);
            Util.playPlaceSound(world, pos, cycleStage.getSoundGroup());
            world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
            return;
        }
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        float stage = state.get(STAGE).ordinal() + 1;
        float size = FlangerBerryStage.values().length;
        return (int) ((stage / size) * 6);
    }

    @Override
    public LayerType getLayerType() {
        return LayerType.CUTOUT;
    }

    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.DESTROY;
    }
}