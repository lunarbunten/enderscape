package net.bunten.enderscape.blocks;

import org.jetbrains.annotations.Nullable;

import net.bunten.enderscape.blocks.properties.DirectionProperties;
import net.bunten.enderscape.blocks.properties.StateProperties;
import net.bunten.enderscape.interfaces.HasRenderType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public abstract class DirectionalPlantBlock extends BushBlock implements HasRenderType {
    public static final DirectionProperty FACING = StateProperties.FACING;
    public final DirectionProperties properties;

    public DirectionalPlantBlock(DirectionProperties properties, Properties settings) {
        super(settings);
        this.properties = properties;
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.UP));
    }

    public static Direction getFacing(BlockState state) {
        return state.getValue(FACING);
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public abstract boolean canPlantOn(BlockState state, BlockState floor, BlockGetter world, BlockPos pos);

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos offset = pos.relative(getFacing(state).getOpposite());
        return canPlantOn(state, world.getBlockState(offset), world, offset);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        for (Direction dir : context.getNearestLookingDirections()) {
            BlockState state = defaultBlockState().setValue(FACING, dir.getOpposite());
            if (properties.supports(dir.getOpposite()) && state.canSurvive(context.getLevel(), context.getClickedPos())) {
                return state;
            }
        }
    
        return null;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public RenderType getRenderType() {
        return RenderType.cutout();
    }
}