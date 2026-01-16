package net.bunten.enderscape.block.properties;

import com.mojang.serialization.Codec;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;

import java.util.ArrayList;
import java.util.List;

public final class DirectionSet {
    public static final Codec<DirectionSet> CODEC = Direction.CODEC
            .listOf()
            .xmap(
                    list -> {
                        DirectionSet set = DirectionSet.create();
                        for (Direction dir : list) set.add(dir);
                        return set;
                    },
                    set -> {
                        List<Direction> list = new ArrayList<>();
                        for (Direction dir : Direction.values()) {
                            if (set.supports(dir)) list.add(dir);
                        }
                        return list;
                    }
            );

    private final List<Direction> list = new ArrayList<>();;
    
    public static DirectionSet create() {
        return new DirectionSet();
    }

    public boolean supports(Direction direction) {
        return list.contains(direction);
    }

    private void add(Direction direction) {
        if (!list.contains(direction)) list.add(direction);
    }

    public DirectionSet up() {
        add(Direction.UP);
        return this;
    }

    public DirectionSet down() {
        add(Direction.DOWN);
        return this;
    }

    public DirectionSet horizontal() {
        for (Direction direction : Direction.values()) {
            if (direction.getAxis() == Axis.Y) continue;
            add(direction);
        }
        return this;
    }

    public DirectionSet vertical() {
        up();
        down();
        return this;
    }

    public DirectionSet all() {
        horizontal();
        vertical();
        return this;
    }
}