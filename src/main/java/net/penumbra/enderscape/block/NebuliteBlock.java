package net.penumbra.enderscape.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class NebuliteBlock extends Block implements HasMagniaPowerSignal {

    public NebuliteBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getMagniaPowerSignal(BlockState state, BlockState neighbor) {
        return 15;
    }
}