package net.bunten.enderscape.block;

import net.bunten.enderscape.block.properties.DirectionProperties;
import net.bunten.enderscape.block.properties.Part;
import net.bunten.enderscape.block.properties.StateProperties;
import net.bunten.enderscape.feature.GrowthConfig;
import net.bunten.enderscape.util.BlockUtil;
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

public abstract class AbstractGrowthBlock extends DirectionalPlantBlock implements BonemealableBlock {
    public static final EnumProperty<Part> GROWTH = StateProperties.GROWTH_PART;

    public AbstractGrowthBlock(DirectionProperties properties, Properties settings) {
        super(properties, settings);
        registerDefaultState(defaultBlockState().setValue(GROWTH, Part.TOP));
    }

    public static Part getPart(BlockState state) {
        return state.getValue(GROWTH);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(GROWTH, FACING);
    }

    public boolean hasGrowthSupport(BlockState state, BlockState floor) {
        return floor.is(this) && floor.getValue(FACING) == state.getValue(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        int i = getPart(state) == Part.TOP ? 15 : 16;
        return BlockUtil.createRotatedShape(1, 0, 1, 15, i, 15, state.getValue(FACING));
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(world, pos)) world.destroyBlock(pos, true);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor world, BlockPos pos, BlockPos pos2) {
        var plantDirection = getFacing(state);
        var opposite = plantDirection.getOpposite();

        if (opposite == direction && !state.canSurvive(world, pos)) {
            world.scheduleTick(pos, this, 1);
        }

        var up = world.getBlockState(pos.relative(plantDirection));
        var down = world.getBlockState(pos.relative(opposite));
        
        if (down.is(this)) {
            return state.setValue(GROWTH, up.is(this) && getFacing(up) == plantDirection ? Part.MIDDLE : Part.TOP);
        } else {
            return state.setValue(GROWTH, up.is(this) && getFacing(up) == plantDirection ? Part.BOTTOM : Part.TOP);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos origin, BlockState state) {
        return getPart(state) == Part.TOP && world.getBlockState(origin.relative(getFacing(state))).isAir();
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
                generate(world, offset, random, new GrowthConfig(state, ConstantInt.of(1), UniformInt.of(1, 2), 0.5F));
                i++;
            }
        }
    }

    public static boolean generate(LevelAccessor world, BlockPos origin, RandomSource random, GrowthConfig config) {
        BlockState state = config.state();
        Direction direction = state.getValue(AbstractGrowthBlock.FACING);

        int totalHeight = config.base_height().sample(random) + (random.nextFloat() <= config.added_height_chance() ? config.added_height().sample(random) : 0);
        BlockPos.MutableBlockPos mutable = origin.mutable();

        int i = 0;

        while (totalHeight > i) {
            Part part = i == totalHeight - 1 ? Part.TOP : (i == 0 ? Part.BOTTOM : Part.MIDDLE);
            BlockUtil.replace(world, mutable, state.setValue(StateProperties.GROWTH_PART, part));
            mutable.move(direction);
            i++;
        }

        return true;
    }
}