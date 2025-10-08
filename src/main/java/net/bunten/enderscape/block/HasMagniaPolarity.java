package net.bunten.enderscape.block;

import net.bunten.enderscape.block.properties.MagniaPolarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public interface HasMagniaPolarity {

    static boolean has(Block block) {
        return block instanceof HasMagniaPolarity;
    }

    static boolean has(BlockState state) {
        return HasMagniaPolarity.has(state.getBlock());
    }

    static HasMagniaPolarity cast(Block block) {
        return (HasMagniaPolarity) block;
    }

    static HasMagniaPolarity cast(BlockState state) {
        return HasMagniaPolarity.cast(state.getBlock());
    }

    static Optional<MagniaPolarity> optional(BlockState state) {
        return has(state) ? cast(state).getPolarity(state) : Optional.empty();
    }

    Optional<MagniaPolarity> getPolarity(BlockState state);
}