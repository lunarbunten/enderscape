package net.bunten.enderscape.world;

import net.bunten.enderscape.registry.EnderscapeEntities;
import net.bunten.enderscape.world.biomes.CelestialIslandsBiome;
import net.bunten.enderscape.world.biomes.CelestialPlainsBiome;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.EndPlacedFeatures;
import ru.bclib.api.biomes.BiomeAPI;
import ru.bclib.world.biomes.BCLBiome;

public class EnderscapeBiomes {

    public static final BCLBiome END_HIGHLANDS = new BCLBiome(BiomeKeys.END_HIGHLANDS.getValue(), BuiltinRegistries.BIOME.get(BiomeKeys.END_HIGHLANDS.getValue()));

    public static final BCLBiome CELESTIAL_PLAINS = CelestialPlainsBiome.register();
    public static final BCLBiome CELESTIAL_ISLANDS = CelestialIslandsBiome.register();

    private static void addGlobalModifications(RegistryEntry<Biome> biome) {
        BiomeAPI.addBiomeFeature(biome, EnderscapeBCLFeatures.SHADOW_QUARTZ_ORE);
        BiomeAPI.addBiomeFeature(biome, EnderscapeBCLFeatures.SCATTERED_SHADOW_QUARTZ_ORE);

        BiomeAPI.addBiomeFeature(biome, EnderscapeBCLFeatures.NEBULITE_ORE);
        BiomeAPI.addBiomeFeature(biome, EnderscapeBCLFeatures.VOID_NEBULITE_ORE);
    }

    private static void addDefaultEndModifications(RegistryEntry<Biome> biome, boolean midlands) {
        BiomeAPI.addBiomeMobSpawn(biome, EnderscapeEntities.RUBBLEMITE, 2, 2, 4);

        BiomeAPI.addBiomeFeature(biome, EnderscapeBCLFeatures.UNCOMMON_CELESTIAL_GROWTH);
        BiomeAPI.addBiomeFeature(biome, EnderscapeBCLFeatures.MURUSHROOMS);

        if (midlands) {
            BiomeAPI.addBiomeFeature(biome, GenerationStep.Feature.SURFACE_STRUCTURES, EndPlacedFeatures.END_GATEWAY_RETURN);
            BiomeAPI.addBiomeFeature(biome, GenerationStep.Feature.VEGETAL_DECORATION, EndPlacedFeatures.CHORUS_PLANT);
        }
    }

    private static void registerBiomeModifications() {
        BiomeAPI.registerEndBiomeModification((id, biome) -> {
            if (id == BiomeKeys.THE_END.getValue()) return;

			if (id.getNamespace() != "enderscape") {
                addGlobalModifications(biome);

                boolean highlands = id == BiomeKeys.END_HIGHLANDS.getValue();
                boolean midlands = id == BiomeKeys.END_MIDLANDS.getValue();
                
                if (highlands || midlands) {
                    addDefaultEndModifications(biome, midlands);
                } else {
                    BiomeAPI.addBiomeFeature(biome, EnderscapeBCLFeatures.UNCOMMON_MURUSHROOMS);
                }
			}
		});
    }

    public static void init() {
        BiomeAPI.registerEndLandBiome(END_HIGHLANDS);
        
        BiomeAPI.registerEndLandBiome(CELESTIAL_PLAINS);
        BiomeAPI.registerEndVoidBiome(CELESTIAL_ISLANDS);

        registerBiomeModifications();
    }
}