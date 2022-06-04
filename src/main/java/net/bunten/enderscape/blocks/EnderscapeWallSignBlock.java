package net.bunten.enderscape.blocks;

import net.bunten.enderscape.interfaces.EnderscapeSign;
import net.minecraft.block.WallSignBlock;
import net.minecraft.util.SignType;

public class EnderscapeWallSignBlock extends WallSignBlock implements EnderscapeSign {
    public EnderscapeWallSignBlock(SignType type, Settings settings) {
        super(settings, type);
    }
}