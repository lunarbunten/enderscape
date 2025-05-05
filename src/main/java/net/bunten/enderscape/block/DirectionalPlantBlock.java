package net.bunten.enderscape.block;

import net.bunten.enderscape.block.properties.DirectionProperties;
import net.bunten.enderscape.block.properties.StateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

public abstract class DirectionalPlantBlock extends BushBlock {
    public static final EnumProperty<Direction> FACING = StateProperties.FACING;
    public final DirectionProperties directionProperties;

    public DirectionalPlantBlock(DirectionProperties directionProperties, Properties properties) {
        super(properties);
        this.directionProperties = directionProperties;
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.UP));
    }

    public static Direction getFacing(BlockState state) {
        return state.getValue(FACING);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public abstract boolean canPlantOn(BlockState state, BlockState floor, BlockGetter level, BlockPos pos, Direction facing);

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = getFacing(state);
        BlockPos offset = pos.relative(facing.getOpposite());
        BlockState floor = level.getBlockState(offset);
        return canPlantOn(state, floor, level, offset, facing);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        for (Direction dir : context.getNearestLookingDirections()) {
            BlockState state = defaultBlockState().setValue(FACING, dir.getOpposite());
            if (directionProperties.supports(dir.getOpposite()) && state.canSurvive(context.getLevel(), context.getClickedPos())) {
                return state;
            }
        }
    
        return null;
    }
}