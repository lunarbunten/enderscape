package net.penumbra.enderscape.block.state;

import net.minecraft.util.StringRepresentable;

public enum EndHavenCoreState implements StringRepresentable {
    INACTIVE("inactive"),
    ACTIVE("active");

    private final String name;

    EndHavenCoreState(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}