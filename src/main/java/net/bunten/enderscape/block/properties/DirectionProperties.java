package net.bunten.enderscape.block.properties;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;

public final class DirectionProperties {
    public static final Codec<DirectionProperties> CODEC = Codec.unit(DirectionProperties::create);
    private final List<Direction> list = new ArrayList<>();;
    
    public static DirectionProperties create() {
        return new DirectionProperties();
    }

    public boolean supports(Direction direction) {
        return list.contains(direction);
    }

    private void add(Direction direction) {
        if (!list.contains(direction)) list.add(direction);
    }

    public DirectionProperties up() {
        add(Direction.UP);
        return this;
    }

    public DirectionProperties down() {
        add(Direction.DOWN);
        return this;
    }

    public DirectionProperties horizontal() {
        for (Direction direction : Direction.values()) {
            if (direction.getAxis() == Axis.Y) continue;
            add(direction);
        }
        return this;
    }

    public DirectionProperties vertical() {
        up();
        down();
        return this;
    }

    public DirectionProperties all() {
        horizontal();
        vertical();
        return this;
    }
}