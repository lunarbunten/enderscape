package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.block.properties.DirectionProperties;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class CorruptPathBlock extends AbstractOvergrowthBlock {

    public static final MapCodec<CorruptPathBlock> CODEC = simpleCodec(CorruptPathBlock::new);

    public CorruptPathBlock(BlockBehaviour.Properties settings) {
        super(true, EnderscapeBlocks.MIRESTONE, DirectionProperties.create().all(), settings);
    }

    @Override
    public MapCodec<CorruptPathBlock> codec() {
        return CODEC;
    }
}