package net.bunten.enderscape.blocks;

import net.bunten.enderscape.Enderscape;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.WoodType;

public class EnderscapeSignType extends WoodType {
    public EnderscapeSignType(String name) {
        super(name);
    }

    public ResourceLocation getTexturePath() {
        return Enderscape.id("entity/signs/" + name());
    }
}