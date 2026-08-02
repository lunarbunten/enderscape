package net.penumbra.enderscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.redstone.Orientation;
import net.penumbra.enderscape.block.state.StateProperties;
import net.penumbra.enderscape.registry.sound.EnderscapeBlockSounds;
import org.jetbrains.annotations.Nullable;

public class BlinklampBlock extends Block {

    private static final int MAX_LUMINANCE = StateProperties.MAX_BLINKLAMP_LUMINANCE;
    private static final IntegerProperty LUMINANCE = StateProperties.BLINKLAMP_LUMINANCE;

    public BlinklampBlock(Properties properties) {
        super(properties.lightLevel(BlinklampBlock::getLightLevel));
        registerDefaultState(defaultBlockState().setValue(LUMINANCE, MAX_LUMINANCE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LUMINANCE);
    }

    public static int getLightLevel(BlockState state) {
        return getLuminance(state) * 15 / MAX_LUMINANCE;
    }

    public static int getLuminance(BlockState state) {
        return state.getValue(LUMINANCE);
    }

    public static MapColor getColor(BlockState state) {
        return getLuminance(state) > 0 ? MapColor.COLOR_PINK : MapColor.COLOR_BLACK;
    }

    private int findLuminance(Level world, BlockPos pos) {
        int signal = world.getBestNeighborSignal(pos);
        return (15 - signal) * MAX_LUMINANCE / 15;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(LUMINANCE, findLuminance(context.getLevel(), context.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, @Nullable Orientation orientation, boolean notify) {
        if (!world.isClientSide()) {
            int luminance = findLuminance(world, pos);
            if (luminance != getLuminance(state)) {
                world.scheduleTick(pos, this, 5);
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        int luminance = findLuminance(world, pos);
        SoundEvent sound = luminance > getLuminance(state) ? EnderscapeBlockSounds.BLINKLAMP_INCREASE : EnderscapeBlockSounds.BLINKLAMP_DECREASE;

        world.setBlock(pos, state.setValue(LUMINANCE, luminance), 3);
        world.playSound(null, pos, sound, SoundSource.BLOCKS, 1, 1);
    }
}