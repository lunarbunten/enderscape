package net.bunten.enderscape.world;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.registry.EnderscapeEntities;
import net.bunten.enderscape.world.biomes.CelestialIslandsBiome;
import net.bunten.enderscape.world.biomes.CelestialPlainsBiome;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class EnderscapeBiomes {

    public static final BCLBiome END_HIGHLANDS = new BCLBiome(BiomeKeys.END_HIGHLANDS.getValue(), BuiltinRegistries.BIOME.get(BiomeKeys.END_HIGHLANDS.getValue()));

    public static final BCLBiome CELESTIAL_PLAINS = CelestialPlainsBiome.register();
    public static final BCLBiome CELESTIAL_ISLANDS = CelestialIslandsBiome.register();

    private static void addGlobalFeatures(RegistryEntry<Biome> biome) {
        BiomeAPI.addBiomeFeature(biome, EnderscapeFeatures.BCL_SHADOW_QUARTZ_ORE);
        BiomeAPI.addBiomeFeature(biome, EnderscapeFeatures.BCL_SCATTERED_SHADOW_QUARTZ_ORE);

        BiomeAPI.addBiomeFeature(biome, EnderscapeFeatures.BCL_NEBULITE_ORE);
        BiomeAPI.addBiomeFeature(biome, EnderscapeFeatures.BCL_VOID_NEBULITE_ORE);
    }

    private static void addEndHighlandsFeatures(RegistryEntry<Biome> biome) {
        BiomeAPI.addBiomeMobSpawn(biome, EnderscapeEntities.RUBBLEMITE, 2, 2, 4);

        BiomeAPI.addBiomeFeature(biome, EnderscapeFeatures.BCL_UNCOMMON_CELESTIAL_GROWTH);
        BiomeAPI.addBiomeFeature(biome, EnderscapeFeatures.BCL_MURUSHROOMS);
    }

    private static void addBetterEndAccommodations(RegistryEntry<Biome> biome) {
        BiomeAPI.addBiomeFeature(biome, EnderscapeFeatures.BETTEREND_SHADOW_QUARTZ_ORE);
        BiomeAPI.addBiomeFeature(biome, EnderscapeFeatures.BETTEREND_VOID_NEBULITE_ORE);
        BiomeAPI.addBiomeFeature(biome, EnderscapeFeatures.BETTEREND_MURUSHROOMS);
    }

    public static void init() {
        BiomeAPI.registerEndLandBiome(END_HIGHLANDS);

        BiomeAPI.registerEndLandBiome(CELESTIAL_PLAINS);
        BiomeAPI.registerEndVoidBiome(CELESTIAL_ISLANDS);

        BiomeAPI.registerEndBiomeModification((id, biome) -> {
			if (id.getNamespace() != "enderscape" && id != BiomeKeys.THE_END.getValue()) {
                addGlobalFeatures(biome);
                
                if (id == BiomeKeys.END_HIGHLANDS.getValue()) {
                    addEndHighlandsFeatures(biome);
                } else {
                    BiomeAPI.addBiomeFeature(biome, EnderscapeFeatures.BCL_UNCOMMON_MURUSHROOMS);
                }
			}

            if (Enderscape.hasBetterEnd()) {
                addBetterEndAccommodations(biome);
            }
		});
    }
}