package net.enderscape.blocks;

import net.minecraft.block.SignBlock;
import net.minecraft.util.SignType;

public class EndSignBlock extends SignBlock implements EndSign {
    public EndSignBlock(SignType type, Settings settings) {
        super(settings, type);
    }
}