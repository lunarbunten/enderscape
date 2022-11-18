package net.bunten.enderscape.blocks;

import net.bunten.enderscape.blocks.properties.DirectionProperties;
import net.bunten.enderscape.blocks.properties.Part;
import net.bunten.enderscape.blocks.properties.StateProperties;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.BlockUtil;
import net.bunten.enderscape.util.MathUtil;
import net.bunten.enderscape.util.PlantUtil;
import net.bunten.enderscape.world.features.vegetation.GrowthConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GrowthBlock extends DirectionalPlantBlock implements BonemealableBlock {
    public static final EnumProperty<Part> GROWTH = StateProperties.GROWTH_PART;

    public GrowthBlock(DirectionProperties properties, Properties settings) {
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

    public boolean hasGrowthSupport(BlockState state, BlockState floor, BlockPos pos) {
        return floor.is(this) ? floor.getValue(FACING) == state.getValue(FACING) : false;
    }

    @Override
    public boolean canPlantOn(BlockState state, BlockState floor, BlockGetter world, BlockPos pos) {
        return floor.is(EnderscapeBlocks.PLANTABLE_GROWTH) || hasGrowthSupport(state, floor, pos);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        int i = getPart(state) == Part.TOP ? 15 : 16;
        return BlockUtil.createRotatedShape(1, 0, 1, 15, i, 15, state.getValue(FACING));
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(world, pos)) {
            world.destroyBlock(pos, true);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        var plantDirection = getFacing(state);
        var opposite = plantDirection.getOpposite();

        if (opposite == direction && !state.canSurvive(world, pos)) {
            world.scheduleTick(pos, this, 1);
        }

        var up = world.getBlockState(pos.relative(plantDirection));
        var down = world.getBlockState(pos.relative(opposite));
        
        if (down.is(this)) {
            return state.setValue(GROWTH, !up.is(this) ? Part.TOP : Part.MIDDLE);
        } else {
            return state.setValue(GROWTH, up.is(this) ? Part.BOTTOM : Part.TOP);
        }
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos origin, BlockState state) {
        if (getPart(state) == Part.TOP && world.getBlockState(origin.relative(getFacing(state))).isAir()) {
            Direction direction = getFacing(state);
            world.setBlockAndUpdate(origin.relative(direction), state);
            return;
        }
        
        int i = 0;
        int max = MathUtil.nextInt(random, 2, 4);
        var range = MathUtil.nextInt(random, 3, 10);

        for (BlockPos pos : BlockPos.randomInCube(random, 30, origin, range)) {
            if (i >= max) break;

            Direction direction = getFacing(state);
            BlockPos offset = pos.relative(direction);

            if (!world.getBlockState(pos).is(this) && world.isEmptyBlock(offset) && state.canSurvive(world, offset)) {
                PlantUtil.generateGrowth(world, random, offset, new GrowthConfig(state, ConstantInt.of(1), UniformInt.of(1, 2), 0.5F));
                i++;
            }
        }
    }
}