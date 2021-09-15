package net.enderscape.blocks;

import net.minecraft.util.StringIdentifiable;

public enum Part implements StringIdentifiable {
    TOP("top"), MIDDLE("middle"), BOTTOM("bottom");

    String name;

    Part(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return name;
    }
}