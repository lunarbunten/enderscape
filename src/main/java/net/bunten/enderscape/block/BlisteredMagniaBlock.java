package net.bunten.enderscape.block;

import net.bunten.enderscape.block.properties.MagniaPolarity;
import net.bunten.enderscape.block.state.OptionalMagniaPolarityProperty;
import net.bunten.enderscape.block.state.StateProperties;
import net.bunten.enderscape.registry.EnderscapeBlockSounds;
import net.bunten.enderscape.util.MagniaUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.ToIntFunction;

public class BlisteredMagniaBlock extends Block implements HasMagniaPolarity, HasMagniaPowerSignal {

    public static final EnumProperty<OptionalMagniaPolarityProperty> POLARITY = StateProperties.OPTIONAL_MAGNIA_POLARITY;

    public BlisteredMagniaBlock(Properties properties) {
        super(properties);

        registerDefaultState(getStateDefinition().any().setValue(POLARITY, OptionalMagniaPolarityProperty.NONE));
    }

    public static OptionalMagniaPolarityProperty getOptionalPolarityProperty(BlockState state) {
        return state.getValue(POLARITY);
    }

    @NotNull
    public static ToIntFunction<BlockState> getLightLevel() {
        return state -> getOptionalPolarityProperty(state).equals(OptionalMagniaPolarityProperty.NONE) ? 0 : 14;
    }
    
    public static MapColor getMapColor(BlockState state) {
        return getOptionalPolarityProperty(state).getMapColor();
    }

    public static OptionalMagniaPolarityProperty selectPolarity(LevelReader level, BlockPos pos) {
        EnumMap<MagniaPolarity, Integer> counts = new EnumMap<>(MagniaPolarity.class);

        for (Direction dir : Direction.values()) {
            BlockState state = level.getBlockState(pos.relative(dir));
            if (state.getBlock() instanceof MagniaBlock && HasMagniaPolarity.has(state) && HasMagniaPolarity.optional(state).isPresent()) {
                MagniaPolarity polarity = HasMagniaPolarity.optional(state).get();
                counts.put(polarity, counts.getOrDefault(polarity, 0) + 1);
            }
        }

        if (counts.isEmpty()) return OptionalMagniaPolarityProperty.NONE;

        return OptionalMagniaPolarityProperty.of(counts.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POLARITY);
    }

    @Override
    public Optional<MagniaPolarity> getPolarity(BlockState state) {
        return getOptionalPolarityProperty(state).optional();
    }

    @Override
    public int getMagniaPowerSignal(BlockState state, BlockState neighbor) {
        return MagniaUtil.isMatchingPolarity(state, neighbor) ? 15 : 0;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(POLARITY, selectPolarity(context.getLevel(), context.getClickedPos()));
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState blockState2, boolean bl) {
        super.onPlace(state, level, pos, blockState2, bl);

        level.playSound(null, pos, getOptionalPolarityProperty(state) == OptionalMagniaPolarityProperty.NONE ? EnderscapeBlockSounds.BLISTERED_MAGNIA_POWER_OFF : EnderscapeBlockSounds.BLISTERED_MAGNIA_POWER_ON, SoundSource.BLOCKS, 1, 1);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        OptionalMagniaPolarityProperty property = selectPolarity(level, pos);
        if (!property.equals(getOptionalPolarityProperty(state))) {
            level.playSound(null, pos, property == OptionalMagniaPolarityProperty.NONE ? EnderscapeBlockSounds.BLISTERED_MAGNIA_POWER_OFF : EnderscapeBlockSounds.BLISTERED_MAGNIA_POWER_ON, SoundSource.BLOCKS, 1, 1);
            level.setBlockAndUpdate(pos, state.setValue(POLARITY, property));
        }
        level.updateNeighborsAt(pos, this);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor level, BlockPos pos, BlockPos pos2) {
        OptionalMagniaPolarityProperty property = selectPolarity(level, pos);
        if (!property.equals(getOptionalPolarityProperty(state))) level.scheduleTick(pos, this, 5);
        return state;
    }
}