package net.enderscape.blocks;

import net.minecraft.block.WallSignBlock;
import net.minecraft.util.SignType;

public class EndWallSignBlock extends WallSignBlock implements EndSign {
    public EndWallSignBlock(SignType type, Settings settings) {
        super(settings, type);
    }
}