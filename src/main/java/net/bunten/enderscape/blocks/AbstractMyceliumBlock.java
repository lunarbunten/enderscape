package net.bunten.enderscape.blocks;

import net.bunten.enderscape.blocks.properties.DirectionProperties;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public abstract class AbstractMyceliumBlock extends DirectionalBlock {
    public final DirectionProperties properties;

    public AbstractMyceliumBlock(DirectionProperties properties, Properties settings) {
        super(settings);
        this.properties = properties;
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.UP));
    }

    public static Direction getDirection(BlockState state) {
        return state.getValue(FACING);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState();
        Direction dir = context.getNearestLookingDirection().getOpposite();
    
        return (properties.supports(dir)) ? state.setValue(FACING, dir) : state;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }
}