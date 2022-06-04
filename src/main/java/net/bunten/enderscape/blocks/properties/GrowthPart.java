package net.bunten.enderscape.blocks.properties;

import net.minecraft.util.StringIdentifiable;

public enum GrowthPart implements StringIdentifiable {
    BOTTOM("bottom"), MIDDLE("middle"), TOP("top");

    String name;

    GrowthPart(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return name;
    }
}