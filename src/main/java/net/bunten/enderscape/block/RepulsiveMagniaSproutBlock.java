package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.block.properties.MagniaType;

public class RepulsiveMagniaSproutBlock extends MagniaSproutBlock {
    public RepulsiveMagniaSproutBlock(Properties settings) {
        super(MagniaType.REPULSIVE, settings);
    }

    public static final MapCodec<RepulsiveMagniaSproutBlock> CODEC = simpleCodec(RepulsiveMagniaSproutBlock::new);

    @Override
    protected MapCodec<RepulsiveMagniaSproutBlock> codec() {
        return CODEC;
    }
}