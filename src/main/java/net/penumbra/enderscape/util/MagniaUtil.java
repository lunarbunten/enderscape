package net.penumbra.enderscape.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.penumbra.enderscape.block.HasMagniaPolarity;
import net.penumbra.enderscape.block.HasMagniaPowerSignal;

import java.util.Objects;

public class MagniaUtil {

    public static int getStrongestPowerSignal(BlockState state, SignalGetter level, BlockPos pos) {
        return getStrongestPowerSignal(state, level, pos, Direction.values());
    }

    public static int getStrongestPowerSignal(BlockState state, SignalGetter level, BlockPos pos, Direction... directions) {
        if (!HasMagniaPolarity.has(state)) {
            return 0;
        } else {
            int strongest = 0;

            for (Direction direction : directions) {
                BlockState neighbor = level.getBlockState(pos.relative(direction));

                if (HasMagniaPowerSignal.has(neighbor) && (!HasMagniaPolarity.has(neighbor) || isMatchingPolarity(state, neighbor))) {
                    int weakening = state.is(neighbor.getBlock()) ? 1 : 0;
                    int signal = HasMagniaPowerSignal.get(neighbor, state) - weakening;
                    strongest = Math.max(strongest, signal);
                }
            }

            return strongest;
        }
    }

    public static boolean isMatchingPolarity(BlockState state, BlockState neighbor) {
        return HasMagniaPolarity.has(state) && HasMagniaPolarity.has(neighbor) && Objects.equals(HasMagniaPolarity.optional(state), HasMagniaPolarity.optional(neighbor));
    }
}