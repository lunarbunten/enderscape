package net.bunten.enderscape.blocks;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.DropExperienceBlock;

public class ShadowQuartzOreBlock extends DropExperienceBlock {
    public ShadowQuartzOreBlock(Properties settings) {
        super(settings, UniformInt.of(1, 3));
    }
}