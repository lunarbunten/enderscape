package net.bunten.enderscape.blocks;

import net.bunten.enderscape.interfaces.EnderscapeSign;
import net.minecraft.block.SignBlock;
import net.minecraft.util.SignType;

public class EnderscapeSignBlock extends SignBlock implements EnderscapeSign {
    public EnderscapeSignBlock(SignType type, Settings settings) {
        super(settings, type);
    }
}