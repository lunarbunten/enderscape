package net.bunten.enderscape.blocks;

import net.bunten.enderscape.interfaces.LayerMapped;
import net.minecraft.block.DoorBlock;

public class EnderscapeDoor extends DoorBlock implements LayerMapped {
    public EnderscapeDoor(Settings settings) {
        super(settings);
    }

    @Override
    public LayerType getLayerType() {
        return LayerType.CUTOUT;
    }
}