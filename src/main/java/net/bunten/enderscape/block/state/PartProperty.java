package net.bunten.enderscape.block.state;

import net.minecraft.util.StringRepresentable;

public enum PartProperty implements StringRepresentable {
    SINGLE("single"), BOTTOM("bottom"), MIDDLE("middle"), TOP("top");

    private final String name;

    PartProperty(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}