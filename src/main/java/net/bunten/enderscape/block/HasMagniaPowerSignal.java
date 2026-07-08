package net.bunten.enderscape.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface HasMagniaPowerSignal {

    static boolean has(Block block) {
        return block instanceof HasMagniaPowerSignal;
    }

    static boolean has(BlockState state) {
        return HasMagniaPowerSignal.has(state.getBlock());
    }

    static HasMagniaPowerSignal cast(Block block) {
        return (HasMagniaPowerSignal) block;
    }

    static HasMagniaPowerSignal cast(BlockState state) {
        return HasMagniaPowerSignal.cast(state.getBlock());
    }

    static int get(BlockState state, BlockState neighbor) {
        return has(state) ? cast(state).getMagniaPowerSignal(state, neighbor) : 0;
    }

    int getMagniaPowerSignal(BlockState state, BlockState neighbor);
}