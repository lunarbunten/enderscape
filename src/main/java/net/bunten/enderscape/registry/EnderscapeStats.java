package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class EnderscapeStats {

    public static final Identifier DRIFTER_BOUNCE = register("drifter_bounce", StatFormatter.DEFAULT);
    public static final Identifier ITEMS_ATTRACTED = register("items_attracted", StatFormatter.DEFAULT);
    public static final Identifier MIRROR_TELEPORT = register("mirror_teleport", StatFormatter.DEFAULT);
    public static final Identifier MIRROR_ONE_CM = register("mirror_one_cm", StatFormatter.DISTANCE);
    public static final Identifier RUBBLE_SHIELD_DASH_ONE_CM = register("rubble_shield_dash_one_cm", StatFormatter.DISTANCE);

    private static Identifier register(String name, StatFormatter formatter) {
        Identifier id = Enderscape.id(name);
        Registry.register(BuiltInRegistries.CUSTOM_STAT, id, id);
        Stats.CUSTOM.get(id, formatter);
        return id;
    }
}