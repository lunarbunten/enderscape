package net.bunten.enderscape.world;

import org.betterx.bclib.api.v2.generator.BiomeDecider;
import org.betterx.bclib.api.v2.levelgen.biomes.InternalBiomeAPI;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.config.Config;
import net.bunten.enderscape.world.biomes.BarrenDepthsBiome;
import net.bunten.enderscape.world.biomes.BarrenSkiesBiome;
import net.bunten.enderscape.world.biomes.CelestialIslandsBiome;
import net.bunten.enderscape.world.biomes.CelestialPlainsBiome;
import net.bunten.enderscape.world.biomes.CorruptDepthsBiome;
import net.bunten.enderscape.world.biomes.EnderscapeBiome;
import net.bunten.enderscape.world.biomes.SkyIslandsBiome;
import net.bunten.enderscape.world.decider.SkyBiomeDecider;
import net.bunten.enderscape.world.decider.SubsurfaceBiomeDecider;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class EnderscapeBiomes {

    public static final MappedRegistry<EnderscapeBiome> BIOME_REGISTRY = FabricRegistryBuilder.createSimple(EnderscapeBiome.class, Enderscape.id("enderscape_biome")).attribute(RegistryAttribute.SYNCED).buildAndRegister();

    public static final TagKey<Biome> MODIFIED_END_AMBIENCE = register("modified_end_ambience");
    public static final TagKey<Biome> ALL_BIOMES = register("all_biomes");
    public static final TagKey<Biome> LAND_BIOMES = register("land_biomes");
    public static final TagKey<Biome> SKY_BIOMES = register("sky_biomes");
    public static final TagKey<Biome> SUBSURFACE_BIOMES = register("subsurface_biomes");
    public static final TagKey<Biome> VOID_BIOMES = register("void_biomes");

    public static final EnderscapeBiome CELESTIAL_ISLANDS = new CelestialIslandsBiome();
    public static final EnderscapeBiome CELESTIAL_PLAINS = new CelestialPlainsBiome();
    public static final EnderscapeBiome BARREN_DEPTHS = new BarrenDepthsBiome();
    public static final EnderscapeBiome CORRUPT_DEPTHS = new CorruptDepthsBiome();
    public static final EnderscapeBiome BARREN_SKIES = new BarrenSkiesBiome();
    public static final EnderscapeBiome SKY_ISLANDS = new SkyIslandsBiome();

    private static TagKey<Biome> register(String name) {
        return TagKey.create(Registry.BIOME_REGISTRY, Enderscape.id(name));
    }

    private static EnderscapeBiome register(EnderscapeBiome biome) {
        InternalBiomeAPI.registerBuiltinBiome(biome.getBCLBiome());
        return Registry.register(BIOME_REGISTRY, Enderscape.id("enderscape_" + biome.getName()), biome);
    }

    public static int getSubsurfaceBiomeHeight() {
        return Config.WORLD.getSubsurfaceBiomeHeight();
    }

    public static int getSkyBiomeHeight() {
        return Config.WORLD.getSkyBiomeHeight();
    }

    static {
        BiomeDecider.registerDecider(Enderscape.id("subsurface_biome_decider"), new SubsurfaceBiomeDecider());
        BiomeDecider.registerDecider(Enderscape.id("sky_biome_decider"), new SkyBiomeDecider());

        register(CELESTIAL_ISLANDS);
        register(CELESTIAL_PLAINS);
        register(BARREN_DEPTHS);
        register(CORRUPT_DEPTHS);
        register(BARREN_SKIES);
        register(SKY_ISLANDS);
    }
}