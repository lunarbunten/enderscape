package net.bunten.enderscape.blocks;

import net.bunten.enderscape.interfaces.LayerMapped;
import net.minecraft.block.Block;
import net.minecraft.block.FlowerPotBlock;

public class EnderscapeFlowerPotBlock extends FlowerPotBlock implements LayerMapped {
    public EnderscapeFlowerPotBlock(Block content, Settings settings) {
        super(content, settings);
    }

    @Override
    public LayerType getLayerType() {
        return LayerType.CUTOUT;
    }
}