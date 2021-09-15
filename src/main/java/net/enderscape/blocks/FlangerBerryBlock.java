package net.enderscape.blocks;

import net.enderscape.interfaces.LayerMapped;
import net.enderscape.registry.EndBlocks;
import net.enderscape.util.Util;
import net.minecraft.block.*;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.EntityTypeTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

public class FlangerBerryBlock extends Block implements LayerMapped, Fertilizable {
    private static final EnumProperty<BerryStage> STAGE = EndProperties.BERRY_STAGE;

    public FlangerBerryBlock(Settings settings) {
        super(settings);
        setDefaultState(getState(BerryStage.RIPE));
    }

    public static boolean canFallThrough(BlockState state) {
        Material material = state.getMaterial();
        return state.isAir() || state.isIn(BlockTags.FIRE) || material.isLiquid() || material.isReplaceable();
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    private BlockState getState(BerryStage value) {
        return getDefaultState().with(STAGE, value);
    }

    private BlockState getBlockState(WorldView world, BlockPos pos) {
        return world.getBlockState(pos);
    }

    private void setBlockState(World world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, state, NOTIFY_LISTENERS);
    }

    private boolean isFlower(BlockState state) {
        return state.get(STAGE) == BerryStage.FLOWER;
    }

    private boolean isUnripe(BlockState state) {
        return state.get(STAGE) == BerryStage.UNRIPE;
    }

    private boolean isRipe(BlockState state) {
        return state.get(STAGE) == BerryStage.RIPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return !isFlower(state) ? state.getOutlineShape(world, pos) : VoxelShapes.empty();
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(STAGE)) {
            case FLOWER -> createCuboidShape(2, 10, 2, 14, 16, 14);
            case UNRIPE -> createCuboidShape(2, 4, 2, 14, 16, 14);
            default -> VoxelShapes.fullCube();
        };
    }

    private void spawnFallingBlock(World world, BlockPos pos) {
        FallingBlockEntity fall = new FallingBlockEntity(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, world.getBlockState(pos));
        world.spawnEntity(fall);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if (state.canPlaceAt(world, pos)) {
            if (!world.getBlockState(pos.up()).isOf(EndBlocks.FLANGER_BERRY_VINE)) {
                if (canFallThrough(world.getBlockState(pos.down())) && pos.getY() >= 0) {
                    if (isRipe(state)) {
                        if (world instanceof ServerWorld) {
                            ServerWorld server = (ServerWorld) world;
                            spawnFallingBlock(server, pos);
                        }
                    }
                }
            }
        } else {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
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

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return isRipe(state) ? super.canPlaceAt(state, world, pos) : getBlockState(world, pos.up()).isOf(EndBlocks.FLANGER_BERRY_VINE);
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.canPlaceAt(world, pos)) {
            if (canFallThrough(world.getBlockState(pos.down())) && pos.getY() >= 0 && !world.getBlockState(pos.up()).isOf(EndBlocks.FLANGER_BERRY_VINE)) {
                spawnFallingBlock(world, pos);
            }
        }
    }

    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        world.getBlockTickScheduler().schedule(pos, this, 2);
    }

    @Override
    public LayerType getLayerType() {
        return LayerType.CUTOUT;
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
        BlockSoundGroup group = state.getSoundGroup();
        Util.playSound(world, pos, group.getPlaceSound(), SoundCategory.BLOCKS, 1, group.getPitch() * 0.8F);

        if (isFlower(state)) {
            setBlockState(world, pos, getState(BerryStage.UNRIPE));
        } else if (isUnripe(state)) {
            setBlockState(world, pos, getState(BerryStage.RIPE));
        }
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextInt(20) == 0 && canGrow(world, random, pos, state)) {
            grow(world, random, pos, state);
        }
    }
}