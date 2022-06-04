package net.bunten.enderscape.world;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeEntities;
import net.bunten.enderscape.world.biomes.CelestialIslandsBiome;
import net.bunten.enderscape.world.biomes.CelestialPlainsBiome;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import ru.bclib.api.biomes.BiomeAPI;
import ru.bclib.api.tag.NamedCommonBlockTags;
import ru.bclib.api.tag.TagAPI;
import ru.bclib.world.biomes.BCLBiome;

public class EnderscapeBiomes {
    
    public static final BCLBiome END_HIGHLANDS = BiomeAPI.registerEndLandBiome(new BCLBiome(BiomeKeys.END_HIGHLANDS));

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

        TagAPI.addBlockTag(NamedCommonBlockTags.GEN_END_STONES, EnderscapeBlocks.NEBULITE_ORE);
        TagAPI.addBlockTag(NamedCommonBlockTags.GEN_END_STONES, EnderscapeBlocks.SHADOW_QUARTZ_ORE);
    }
}