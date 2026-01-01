package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BannerPattern;

import java.util.ArrayList;
import java.util.List;

public class EnderscapeBannerPatterns {

    public static final List<ResourceKey<BannerPattern>> BANNER_PATTERNS = new ArrayList<>();

    public static final ResourceKey<BannerPattern> CRESCENT = register("crescent");

    public static void bootstrap(BootstrapContext<BannerPattern> context) {
        register(context, CRESCENT);
    }

    public static void register(BootstrapContext<BannerPattern> context, ResourceKey<BannerPattern> key) {
        context.register(key, new BannerPattern(key.identifier(), "block.minecraft.banner." + key.identifier().toShortLanguageKey()));
    }

    private static ResourceKey<BannerPattern> register(String name) {
        ResourceKey<BannerPattern> key = ResourceKey.create(Registries.BANNER_PATTERN, Enderscape.id(name));
        BANNER_PATTERNS.add(key);
        return key;
    }
}