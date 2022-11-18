package net.bunten.enderscape.blocks.properties;

import net.minecraft.util.StringRepresentable;

public enum Part implements StringRepresentable {
    BOTTOM("bottom"), MIDDLE("middle"), TOP("top");

    String name;

    Part(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}