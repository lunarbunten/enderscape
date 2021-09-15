package net.enderscape.util;

import net.minecraft.util.StringIdentifiable;

public enum FungusType implements StringIdentifiable {
    CELESTIAL("celestial"), CORRUPT("corrupt");

    private final String name;

    FungusType(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return name;
    }
}