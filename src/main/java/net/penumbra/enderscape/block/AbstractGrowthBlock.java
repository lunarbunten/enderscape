package net.penumbra.enderscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.penumbra.enderscape.block.properties.DirectionSet;
import net.penumbra.enderscape.block.state.PartProperty;
import net.penumbra.enderscape.block.state.StateProperties;
import net.penumbra.enderscape.feature.GrowthFeature;
import net.penumbra.enderscape.util.BlockUtil;

import java.util.LinkedHashMap;

public abstract class AbstractGrowthBlock extends DirectionalVegetationBlock implements BonemealableBlock {
    public static final EnumProperty<PartProperty> PART = StateProperties.GROWTH_PART;

    private static final LinkedHashMap<Direction, VoxelShape> VOXEL_SHAPES = BlockUtil.createRotatedShapes(1, 0, 1, 15, 16, 15);
    private static final LinkedHashMap<Direction, VoxelShape> TOP_OR_SINGLE_VOXEL_SHAPES = BlockUtil.createRotatedShapes(1, 0, 1, 15, 15, 15);

    public AbstractGrowthBlock(DirectionSet properties, Properties settings) {
        super(properties, settings);
        registerDefaultState(defaultBlockState().setValue(PART, PartProperty.SINGLE));
    }

    public static PartProperty getPart(BlockState state) {
        return state.getValue(PART);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(PART, FACING);
    }

    public boolean hasGrowthSupport(BlockState state, BlockState floor) {
        return floor.is(this) && floor.getValue(FACING) == state.getValue(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = getFacing(state);
        return isTopOrSinglePart(state) ? TOP_OR_SINGLE_VOXEL_SHAPES.get(direction) : VOXEL_SHAPES.get(direction);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(world, pos)) world.destroyBlock(pos, true);
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess access, BlockPos pos, Direction direction, BlockPos pos2, BlockState state2, RandomSource random) {
        var plantDirection = getFacing(state);
        var opposite = plantDirection.getOpposite();

        if (opposite == direction && !state.canSurvive(world, pos)) {
            access.scheduleTick(pos, this, 1);
        }

        var up = world.getBlockState(pos.relative(plantDirection));
        var down = world.getBlockState(pos.relative(opposite));
        
        if (down.is(this)) {
            return state.setValue(PART, up.is(this) && getFacing(up) == plantDirection ? PartProperty.MIDDLE : PartProperty.TOP);
        } else {
            return state.setValue(PART, up.is(this) && getFacing(up) == plantDirection ? PartProperty.BOTTOM : PartProperty.SINGLE);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos origin, BlockState state) {
        return isTopOrSinglePart(state) && world.getBlockState(origin.relative(getFacing(state))).isAir();
    }

    private static boolean isTopOrSinglePart(BlockState state) {
        return getPart(state) == PartProperty.SINGLE || getPart(state) == PartProperty.TOP;
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos origin, BlockState state) {
        world.setBlockAndUpdate(origin.relative(getFacing(state)), state);
    }

    public static void generatePatch(ServerLevel world, RandomSource random, BlockPos origin, BlockState state, int range, int i, int max) {
        for (BlockPos pos : BlockPos.randomInCube(random, 30, origin, range)) {
            if (i >= max) break;

            Direction direction = getFacing(state);
            BlockPos offset = pos.relative(direction);

            if (!world.getBlockState(pos).is(state.getBlock()) && world.isEmptyBlock(offset) && state.canSurvive(world, offset)) {
                generate(world, offset, random, new GrowthFeature.Config(state, ConstantInt.of(1), UniformInt.of(1, 2), 0.5F));
                i++;
            }
        }
    }

    public static boolean generate(LevelAccessor world, BlockPos origin, RandomSource random, GrowthFeature.Config config) {
        BlockState state = config.state();
        Direction direction = state.getValue(AbstractGrowthBlock.FACING);

        int totalHeight = config.base_height().sample(random) + (random.nextFloat() <= config.added_height_chance() ? config.added_height().sample(random) : 0);
        BlockPos.MutableBlockPos mutable = origin.mutable();

        int i = 0;

        while (totalHeight > i) {
            PartProperty part = i == totalHeight - 1 ? (totalHeight == 1 ? PartProperty.SINGLE : PartProperty.TOP) : (i == 0 ? PartProperty.BOTTOM : PartProperty.MIDDLE);
            BlockUtil.replace(world, mutable, state.setValue(StateProperties.GROWTH_PART, part));
            mutable.move(direction);
            i++;
        }

        return true;
    }
}