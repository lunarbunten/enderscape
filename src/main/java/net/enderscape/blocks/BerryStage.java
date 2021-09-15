package net.enderscape.blocks;

import net.minecraft.util.StringIdentifiable;

public enum BerryStage implements StringIdentifiable {
    FLOWER("flower"), UNRIPE("unripe"), RIPE("ripe");

    String name;

    BerryStage(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return name;
    }
}