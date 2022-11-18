package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class EnderscapeStats {

    public static final ResourceLocation DRIFTER_BOUNCE = register("drifter_bounce", StatFormatter.DEFAULT);
    public static final ResourceLocation MIRROR_TELEPORT = register("mirror_teleport", StatFormatter.DEFAULT);

    private static ResourceLocation register(String name, StatFormatter formatter) {
        ResourceLocation id = Enderscape.id(name);
        Registry.register(Registry.CUSTOM_STAT, id, id);
        Stats.CUSTOM.get(id, formatter);
        return id;
    }
}