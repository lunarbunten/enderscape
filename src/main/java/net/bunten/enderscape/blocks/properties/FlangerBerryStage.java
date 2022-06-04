package net.bunten.enderscape.blocks.properties;

import net.minecraft.util.StringIdentifiable;

public enum FlangerBerryStage implements StringIdentifiable {
    FLOWER("flower"), UNRIPE("unripe"), RIPE("ripe");

    String name;

    FlangerBerryStage(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return name;
    }
}