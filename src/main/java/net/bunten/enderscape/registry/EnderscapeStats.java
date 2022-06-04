package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EnderscapeStats {

    public static final Identifier DRIFTER_BOUNCE = register("drifter_bounce", StatFormatter.DEFAULT);
    public static final Identifier MIRROR_TELEPORT = register("mirror_teleport", StatFormatter.DEFAULT);

    private static Identifier register(String name, StatFormatter formatter) {
        var id = Enderscape.id(name);
        Registry.register(Registry.CUSTOM_STAT, id, id);
        Stats.CUSTOM.getOrCreateStat(id, formatter);
        return id;
    }

    public static void init() {
    }
}