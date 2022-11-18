package net.bunten.enderscape.blocks;

import org.jetbrains.annotations.Nullable;

import net.bunten.enderscape.blocks.properties.StateProperties;
import net.bunten.enderscape.util.MathUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class NebuliteBlock extends Block {
    public static final BooleanProperty POWERED = StateProperties.POWERED;

    public NebuliteBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(POWERED, false));
    }

    public boolean isPowered(BlockState state) {
        return state.getValue(POWERED);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(POWERED, ctx.getLevel().hasNeighborSignal(ctx.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClientSide()) return;
        if (isPowered(state) != world.hasNeighborSignal(pos)) {
            if (isPowered(state)) {
                world.scheduleTick(pos, this, 2);
            } else {
                world.setBlock(pos, state.cycle(POWERED), Block.UPDATE_CLIENTS);
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (isPowered(state) && !world.hasNeighborSignal(pos)) {
            world.setBlock(pos, (BlockState) state.cycle(POWERED), Block.UPDATE_CLIENTS);
        }
    }

    @Environment(EnvType.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (isPowered(state)) NebuliteOreBlock.makeParticles(MathUtil.nextInt(random, 2, 4), MathUtil.nextInt(random, 8, 12), world, pos, random);
    }
}