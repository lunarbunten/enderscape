package net.bunten.enderscape.blocks;

import net.bunten.enderscape.interfaces.LayerMapped;
import net.minecraft.block.TrapdoorBlock;

public class EnderscapeTrapdoor extends TrapdoorBlock implements LayerMapped {
    public EnderscapeTrapdoor(Settings settings) {
        super(settings);
    }

    @Override
    public LayerType getLayerType() {
        return LayerType.CUTOUT;
    }
}