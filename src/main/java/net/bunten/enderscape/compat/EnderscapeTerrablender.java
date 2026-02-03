package net.bunten.enderscape.compat;

import net.bunten.enderscape.registry.EnderscapeBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import terrablender.api.EndBiomeRegistry;

public class EnderscapeTerrablender {
    public static void initialize() {
        addBiomeForVanillaWorldgen(EnderscapeBiomes.MAGNIA_FIELDS, 0.8);
        addBiomeForVanillaWorldgen(EnderscapeBiomes.VEILED_WOODLANDS, 0.7);
        addBiomeForVanillaWorldgen(EnderscapeBiomes.CORRUPT_BARRENS, 0.5);
        addBiomeForVanillaWorldgen(EnderscapeBiomes.CELESTIAL_GROVE, 0.3);
    }

    private static void addBiomeForVanillaWorldgen(ResourceKey<Biome> key, double doubleWeight) {
        int weight = (int) Math.round(doubleWeight * 10);
        EndBiomeRegistry.registerHighlandsBiome(key, weight);
        EndBiomeRegistry.registerMidlandsBiome(key, weight);
        EndBiomeRegistry.registerEdgeBiome(key, weight);
    }
}
