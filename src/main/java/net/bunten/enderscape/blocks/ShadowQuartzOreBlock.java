package net.bunten.enderscape.blocks;

import net.minecraft.block.OreBlock;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class ShadowQuartzOreBlock extends OreBlock {
    public ShadowQuartzOreBlock(Settings settings) {
        super(settings, UniformIntProvider.create(1, 3));
    }
}