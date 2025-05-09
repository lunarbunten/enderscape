package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class VeiledLeafPileBlock extends Block {

    public static final MapCodec<VeiledLeafPileBlock> CODEC = simpleCodec(VeiledLeafPileBlock::new);
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;

    @Override
    public MapCodec<VeiledLeafPileBlock> codec() {
        return CODEC;
    }

    public VeiledLeafPileBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(LAYERS, 1));
    }

    @Override
    protected void entityInside(BlockState state, Level levelZ, BlockPos pos, Entity entity) {
        int layers = getLayers(state);

        if (layers >= 4) {
            float scale = 0.8F - ((layers - 4) * 0.21F);
            entity.makeStuckInBlock(state, new Vec3(scale, scale, scale));
        }
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        if (Objects.requireNonNull(type) == PathComputationType.LAND) return getLayers(state) < 5;
        return false;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int layers = getLayers(state);
        return Block.box(0, 0, 0, 16, layers * 2, 16.0);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getLayers(state) == 8 ? Shapes.block() : Shapes.empty();
    }

    @NotNull
    private static Integer getLayers(BlockState state) {
        return state.getValue(LAYERS);
    }

    @Override
    protected int getLightBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return 1;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canSurvive(level, pos, state.getBlock());
    }

    public static boolean canSurvive(LevelReader level, BlockPos pos, Block block) {
        BlockState floor = level.getBlockState(pos.below());
        return Block.isFaceFull(floor.getCollisionShape(level, pos.below()), Direction.UP) || floor.is(block) && getLayers(floor) == 8;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor world, BlockPos pos, BlockPos pos2) {
        return !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, state2, world, pos, pos2);
    }

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        int i = getLayers(state);
        if (!context.getItemInHand().is(asItem()) || i >= 8) {
            return i == 1;
        } else {
            return context.replacingClickedOnBlock() ? context.getClickedFace() == Direction.UP : true;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        if (state.is(this)) {
            int i = getLayers(state);
            return state.setValue(LAYERS, Integer.valueOf(Math.min(8, i + 1)));
        } else {
            return super.getStateForPlacement(context);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYERS);
    }
}
