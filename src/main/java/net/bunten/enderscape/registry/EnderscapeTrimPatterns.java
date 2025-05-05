package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.trim.TrimPattern;

import java.util.ArrayList;
import java.util.List;

public class EnderscapeTrimPatterns {

    public static final List<ResourceKey<TrimPattern>> TRIM_PATTERNS = new ArrayList<>();

    public static final ResourceKey<TrimPattern> STASIS = register("stasis");

    public static void bootstrap(BootstrapContext<TrimPattern> context) {
        register(context, STASIS);
    }

    public static void register(BootstrapContext<TrimPattern> bootstrapContext, ResourceKey<TrimPattern> key) {
        TrimPattern pattern = new TrimPattern(key.location(), Component.translatable(Util.makeDescriptionId("trim_pattern", key.location())), false);
        bootstrapContext.register(key, pattern);
    }

    private static ResourceKey<TrimPattern> register(String name) {
        ResourceKey<TrimPattern> key = ResourceKey.create(Registries.TRIM_PATTERN, Enderscape.id(name));
        TRIM_PATTERNS.add(key);
        return key;
    }
}