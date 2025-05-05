package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.block.properties.MagniaType;

public class AlluringMagniaSproutBlock extends MagniaSproutBlock {
    public AlluringMagniaSproutBlock(Properties settings) {
        super(MagniaType.ALLURING, settings);
    }

    public static final MapCodec<AlluringMagniaSproutBlock> CODEC = simpleCodec(AlluringMagniaSproutBlock::new);

    @Override
    protected MapCodec<AlluringMagniaSproutBlock> codec() {
        return CODEC;
    }
}