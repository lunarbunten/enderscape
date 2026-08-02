package net.penumbra.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.penumbra.enderscape.block.properties.DirectionSet;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;

public class CorruptPathBlock extends AbstractOvergrowthBlock {

    public static final MapCodec<CorruptPathBlock> CODEC = simpleCodec(CorruptPathBlock::new);

    public CorruptPathBlock(BlockBehaviour.Properties settings) {
        super(true, EnderscapeBlocks.MIRESTONE, DirectionSet.create().all(), settings);
    }

    @Override
    public MapCodec<CorruptPathBlock> codec() {
        return CODEC;
    }
}