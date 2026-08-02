package net.penumbra.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.redstone.Orientation;
import net.penumbra.enderscape.block.properties.MagniaPolarity;
import net.penumbra.enderscape.block.state.MagniaPolarityProperty;
import net.penumbra.enderscape.block.state.StateProperties;
import net.penumbra.enderscape.registry.sound.EnderscapeBlockSounds;
import net.penumbra.enderscape.util.MagniaUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.ToIntFunction;

public class PolarizedMagniaBlock extends Block implements HasMagniaPolarity, HasMagniaPowerSignal {
    public static final MapCodec<PolarizedMagniaBlock> CODEC = simpleCodec(PolarizedMagniaBlock::new);

    public static final BooleanProperty POWERED = StateProperties.POWERED;
    public static final EnumProperty<MagniaPolarityProperty> POLARITY = StateProperties.MAGNIA_POLARITY;

    protected MapCodec<? extends PolarizedMagniaBlock> codec() {
        return CODEC;
    }

    public PolarizedMagniaBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(POLARITY, MagniaPolarityProperty.ALLURING).setValue(POWERED, false));
    }

    public static MagniaPolarityProperty getPolarityProperty(BlockState state) {
        return state.getValue(POLARITY);
    }

    @NotNull
    public static ToIntFunction<BlockState> lightLevel() {
        return state -> 14;
    }

    public static MapColor getMapColor(BlockState state) {
        return getPolarityProperty(state).getMapColor();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POLARITY, POWERED);
    }

    @Override
    public Optional<MagniaPolarity> getPolarity(BlockState state) {
        return Optional.ofNullable(getPolarityProperty(state).get());
    }

    @Override
    public int getMagniaPowerSignal(BlockState state, BlockState neighbor) {
        return MagniaUtil.isMatchingPolarity(state, neighbor) ? 15 : 0;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState state2, boolean bl) {
        if (state2.getBlock() != state.getBlock() && level instanceof ServerLevel server) checkAndFlip(state, server, pos);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean bl) {
        if (level instanceof ServerLevel server) checkAndFlip(state, server, pos);
    }

    public void checkAndFlip(BlockState state, ServerLevel level, BlockPos pos) {
        boolean powered = level.hasNeighborSignal(pos);
        if (powered != state.getValue(POWERED)) {
            BlockState state2 = state;

            if (!state.getValue(POWERED)) {
                state2 = state.cycle(POLARITY);
                level.playSound(null, pos, EnderscapeBlockSounds.POLARIZED_MAGNIA_SWAP_POLARITY, SoundSource.BLOCKS);
            }

            level.setBlock(pos, state2.setValue(POWERED, powered), 3);
        }
    }
}