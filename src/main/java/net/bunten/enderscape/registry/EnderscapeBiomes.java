package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.biome.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public class EnderscapeBiomes {

    public static int lightenFogColor(int color) {
        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        float factor = 0.15F;
        r = Math.min(255, (int) (r / factor));
        g = Math.min(255, (int) (g / factor));
        b = Math.min(255, (int) (b / factor));

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static final int DEFAULT_SKY_COLOR = 0x181321;
    public static final int DEFAULT_FOG_COLOR = lightenFogColor(0x110D18);

    public static final int DEFAULT_GRASS_COLOR = 0xa1b783;
    public static final int DEFAULT_FOLIAGE_COLOR = 0xa1b783;

    public static final int DEFAULT_WATER_COLOR = 0x797aa1;
    public static final int DEFAULT_WATER_FOG_COLOR = 0x383955;

    public static final int DEFAULT_NEBULA_COLOR = 0x7B5B9E;
    public static final float DEFAULT_NEBULA_ALPHA = 0.05F;

    public static final int DEFAULT_STAR_COLOR = 0xE989FF;
    public static final float DEFAULT_STAR_ALPHA = 0.12F;

    public static final int DEFAULT_FLASH_COLOR = 0x2f213f;

    public static final float CORRUPT_BARRENS_DARKENING_FACTOR = 0.65F;
    public static final float VOID_BIOMES_DARKENING_FACTOR = 0.35F;

    public static final List<ResourceKey<Biome>> BIOMES = new ArrayList<>();

    public static final ResourceKey<Biome> CELESTIAL_GROVE = register("celestial_grove");
    public static final ResourceKey<Biome> CORRUPT_BARRENS = register("corrupt_barrens");
    public static final ResourceKey<Biome> MAGNIA_FIELDS = register("magnia_fields");
    public static final ResourceKey<Biome> VEILED_WOODLANDS = register("veiled_woodlands");
    public static final ResourceKey<Biome> VOID_DEPTHS = register("void_depths");
    public static final ResourceKey<Biome> VOID_SKIES = register("void_skies");
    public static final ResourceKey<Biome> VOID_SKY_ISLANDS = register("void_sky_islands");

    public static void bootstrap(BootstrapContext<Biome> context) {
        context.register(CELESTIAL_GROVE, CelestialGroveBiome.create(context));
        context.register(CORRUPT_BARRENS, CorruptBarrensBiome.create(context));
        context.register(MAGNIA_FIELDS, MagniaCragsBiome.create(context));
        context.register(VEILED_WOODLANDS, VeiledWoodlandsBiome.create(context));
        context.register(VOID_DEPTHS, VoidDepthsBiome.create(context));
        context.register(VOID_SKIES, VoidSkiesBiome.create(context));
        context.register(VOID_SKY_ISLANDS, VoidSkyIslandsBiome.create(context));
    }

    private static ResourceKey<Biome> register(String name) {
        ResourceKey<Biome> key = ResourceKey.create(Registries.BIOME, Enderscape.id(name));
        BIOMES.add(key);
        return key;
    }
}