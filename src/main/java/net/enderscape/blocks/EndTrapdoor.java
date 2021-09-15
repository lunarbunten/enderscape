package net.enderscape.blocks;

import net.enderscape.interfaces.LayerMapped;
import net.minecraft.block.TrapdoorBlock;

public class EndTrapdoor extends TrapdoorBlock implements LayerMapped {
    public EndTrapdoor(Settings settings) {
        super(settings);
    }

    @Override
    public LayerType getLayerType() {
        return LayerType.CUTOUT;
    }
}