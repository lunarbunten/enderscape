package net.bunten.enderscape.block.state;

import net.bunten.enderscape.block.properties.MagniaPolarity;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.material.MapColor;

import java.util.Arrays;

public enum MagniaPolarityProperty implements StringRepresentable {
    ALLURING("alluring", MapColor.DIAMOND, MagniaPolarity.ALLURING),
    REPULSIVE("repulsive", MapColor.TERRACOTTA_PINK, MagniaPolarity.REPULSIVE);

    private final String name;
    private final MagniaPolarity polarity;
    private MapColor color;

    MagniaPolarityProperty(String name, MapColor color, MagniaPolarity polarity) {
        this.name = name;
        this.polarity = polarity;
        this.color = color;
    }

    public static MagniaPolarityProperty of(MagniaPolarity value) {
        return Arrays.stream(values()).filter(property -> property.get() == value).findFirst().orElseThrow();
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public MagniaPolarity get() {
        return polarity;
    }

    public MapColor getMapColor() {
        return color;
    }
}