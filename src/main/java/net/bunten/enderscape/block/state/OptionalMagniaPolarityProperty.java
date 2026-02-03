package net.bunten.enderscape.block.state;

import net.bunten.enderscape.block.properties.MagniaPolarity;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.material.MapColor;

import java.util.Arrays;
import java.util.Optional;

public enum OptionalMagniaPolarityProperty implements StringRepresentable {
    NONE("none", MapColor.SNOW, Optional.empty()),
    ALLURING("alluring", MapColor.DIAMOND, Optional.of(MagniaPolarity.ALLURING)),
    REPULSIVE("repulsive", MapColor.TERRACOTTA_PINK, Optional.of(MagniaPolarity.REPULSIVE));

    private final String name;
    private final Optional<MagniaPolarity> polarity;
    private MapColor color;

    OptionalMagniaPolarityProperty(String name, MapColor color, Optional<MagniaPolarity> polarity) {
        this.name = name;
        this.polarity = polarity;
        this.color = color;
    }

    public static OptionalMagniaPolarityProperty of(MagniaPolarity value) {
        return Arrays.stream(values()).filter(property -> property.optional().filter(type -> type == value).isPresent()).findFirst().orElse(NONE);
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public Optional<MagniaPolarity> optional() {
        return polarity;
    }

    public MapColor getMapColor() {
        return color;
    }
}