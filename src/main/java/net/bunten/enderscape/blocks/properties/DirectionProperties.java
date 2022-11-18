package net.bunten.enderscape.blocks.properties;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;

public final class DirectionProperties {
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