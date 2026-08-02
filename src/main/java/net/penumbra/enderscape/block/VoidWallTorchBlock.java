package net.penumbra.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public class VoidWallTorchBlock extends VoidTorchBlock {
    public static final MapCodec<VoidWallTorchBlock> CODEC = simpleCodec(VoidWallTorchBlock::new);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(5.0, 3.0, 13.0, 11.0, 16.0));

    @Override
    public MapCodec<VoidWallTorchBlock> codec() {
        return CODEC;
    }

    public VoidWallTorchBlock(final BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected VoxelShape getShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
        return getShape(state);
    }

    public static VoxelShape getShape(final BlockState state) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    protected boolean canSurvive(final BlockState state, final LevelReader level, final BlockPos pos) {
        return canSurvive(level, pos, state.getValue(FACING));
    }

    public static boolean canSurvive(final LevelReader level, final BlockPos pos, final Direction facing) {
        BlockPos relativePos = pos.relative(facing.getOpposite());
        BlockState relativeState = level.getBlockState(relativePos);
        return relativeState.isFaceSturdy(level, relativePos, facing);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        BlockState state = this.defaultBlockState();
        LevelReader level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction[] directions = context.getNearestLookingDirections();

        for (Direction direction : directions) {
            if (direction.getAxis().isHorizontal()) {
                Direction facing = direction.getOpposite();
                state = state.setValue(FACING, facing);
                if (state.canSurvive(level, pos)) {
                    return state;
                }
            }
        }

        return null;
    }

    @Override
    protected BlockState updateShape(
            final BlockState state,
            final LevelReader level,
            final ScheduledTickAccess ticks,
            final BlockPos pos,
            final Direction directionToNeighbour,
            final BlockPos neighbourPos,
            final BlockState neighbourState,
            final RandomSource random
    ) {
        return directionToNeighbour.getOpposite() == state.getValue(FACING) && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : state;
    }

    @Override
    public void animateTick(final BlockState state, final Level level, final BlockPos pos, final RandomSource random) {
        if (random.nextInt(2) == 0) {
            Direction direction = state.getValue(FACING);
            Direction opposite = direction.getOpposite();

            Vec3 particlePos = Vec3.atCenterOf(pos).add(
                    0.27 * opposite.getStepX(),
                    0.42,
                    0.27 * opposite.getStepX()
            );

            level.addParticle(
                    EnderscapeParticles.VOID_STARS,
                    particlePos.x() + Mth.nextDouble(random, -0.2, 0.2),
                    particlePos.y(),
                    particlePos.z() + Mth.nextDouble(random, -0.2, 0.2),
                    0.0,
                    0.015,
                    0.0
            );
        }
    }

    @Override
    protected BlockState rotate(final BlockState state, final Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(final BlockState state, final Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}