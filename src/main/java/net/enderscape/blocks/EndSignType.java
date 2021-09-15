package net.enderscape.blocks;

import net.minecraft.util.SignType;
import net.enderscape.Enderscape;
import net.minecraft.util.Identifier;

public class EndSignType extends SignType {
    public EndSignType(String name) {
        super(name);
    }

    public Identifier getTexture() {
        return Enderscape.id("entity/signs/" + getName());
    }
}