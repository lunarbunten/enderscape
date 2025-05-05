package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.block.properties.MagniaType;

public class AlluringMagniaBlock extends MagniaBlock {
    public AlluringMagniaBlock(Properties settings) {
        super(MagniaType.ALLURING, settings);
    }

    public static final MapCodec<AlluringMagniaBlock> CODEC = simpleCodec(AlluringMagniaBlock::new);

    @Override
    protected MapCodec<AlluringMagniaBlock> codec() {
        return CODEC;
    }
}