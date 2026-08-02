package net.penumbra.enderscape.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.penumbra.enderscape.registry.tag.EnderscapeBlockTags;

public class WispFlowerBlock extends TallFlowerBlock {
    public WispFlowerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(EnderscapeBlockTags.SUPPORTS_WISP_FLOWER);
    }
}