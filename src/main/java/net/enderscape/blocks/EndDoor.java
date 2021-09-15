package net.enderscape.blocks;

import net.enderscape.interfaces.LayerMapped;
import net.minecraft.block.DoorBlock;

public class EndDoor extends DoorBlock implements LayerMapped {
    public EndDoor(Settings settings) {
        super(settings);
    }

    @Override
    public LayerType getLayerType() {
        return LayerType.CUTOUT;
    }
}