package net.bunten.enderscape.blocks.properties;

import net.minecraft.util.StringRepresentable;

public enum Stage implements StringRepresentable {
    FLOWER("flower"), UNRIPE("unripe"), RIPE("ripe");

    String name;

    Stage(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}