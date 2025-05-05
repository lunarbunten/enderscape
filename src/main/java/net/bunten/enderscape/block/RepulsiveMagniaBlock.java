package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.block.properties.MagniaType;

public class RepulsiveMagniaBlock extends MagniaBlock {
    public RepulsiveMagniaBlock(Properties settings) {
        super(MagniaType.REPULSIVE, settings);
    }

    public static final MapCodec<RepulsiveMagniaBlock> CODEC = simpleCodec(RepulsiveMagniaBlock::new);

    @Override
    protected MapCodec<RepulsiveMagniaBlock> codec() {
        return CODEC;
    }
}