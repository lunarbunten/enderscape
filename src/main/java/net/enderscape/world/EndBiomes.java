package net.enderscape.world;

import com.google.common.collect.Lists;
import net.enderscape.Enderscape;
import net.enderscape.world.biomes.*;
import net.ludocrypt.perorate.world.PerorateEnd;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class EndBiomes {

    private static final List<EnderscapeBiome> ISLAND_BIOMES = Lists.newArrayList();

    public static final EnderscapeBiome CELESTIAL_PLAINS = register(new CelestialPlainsBiome(), false);
    public static final EnderscapeBiome CELESTIAL_ISLANDS = register(new CelestialIslandsBiome(), true);

    private static EnderscapeBiome register(EnderscapeBiome biome, boolean island) {
        if (island) {
            ISLAND_BIOMES.add(biome);
        }

        Identifier id = biome.getRegistryKey().getValue();
        Registry.register(BuiltinRegistries.BIOME, id, biome.getBiome());
        return Registry.register(Enderscape.ENDERSCAPE_BIOME, id, biome);
    }

    public static void init() {
        for (EnderscapeBiome biome : Enderscape.ENDERSCAPE_BIOME) {
            if (biome instanceof EnderscapeIslandsBiome) {
                PerorateEnd.addSmallIslandsBiome(biome.getRegistryKey(), biome.getMixedNoisePoint());
            } else {
                PerorateEnd.addHighlandsBiome(biome.getRegistryKey(), biome.getMixedNoisePoint());
                PerorateEnd.addMidlandsBiome(biome.getRegistryKey(), biome.getMixedNoisePoint());
                PerorateEnd.addBarrensBiome(biome.getRegistryKey(), biome.getMixedNoisePoint());
            }
        }
    }
}