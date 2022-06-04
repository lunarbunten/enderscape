package net.bunten.enderscape.blocks;

import net.minecraft.util.SignType;
import net.bunten.enderscape.Enderscape;
import net.minecraft.util.Identifier;

public class EnderscapeSignType extends SignType {
    public EnderscapeSignType(String name) {
        super(name);
    }

    public Identifier getTexturePath() {
        return Enderscape.id("entity/signs/" + getName());
    }
}