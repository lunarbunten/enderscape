package net.penumbra.enderscape.item.component;

import net.minecraft.resources.Identifier;
import net.penumbra.enderscape.Enderscape;

import java.util.ArrayList;
import java.util.List;

public class RubbleShieldVariant {

    public static final List<Identifier> VARIANTS = new ArrayList<>();

    public static final Identifier END_STONE = create("end_stone");
    public static final Identifier MIRESTONE = create("mirestone");
    public static final Identifier VERADITE = create("veradite");
    public static final Identifier KURODITE = create("kurodite");

    private static Identifier create(String name) {
        Identifier id = Enderscape.id(name);
        VARIANTS.add(id);
        return id;
    }
}