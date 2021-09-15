package net.enderscape.world.biomes;

import net.enderscape.Enderscape;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.MixedNoisePoint;

/**
 * Template for creating a basic end biome.
 *
 * @see EnderscapeIslandsBiome
 */
public abstract class EnderscapeBiome {
    protected final String name;
    protected final MixedNoisePoint noise;
    protected final BiomeUtil util = new BiomeUtil();

    public EnderscapeBiome(String name, MixedNoisePoint noise) {
        this.name = name;
        this.noise = noise;
    }

    protected static RegistryKey<Biome> register(String name) {
        return RegistryKey.of(Registry.BIOME_KEY, Enderscape.id(name));
    }

    protected static MixedNoisePoint createNoise(float temperature, float humidity, float altitude, float weirdness, float weight) {
        return new MixedNoisePoint(temperature, humidity, altitude, weirdness, weight);
    }

    /**
     * The actual biome used for generation.
     */
    public abstract Biome getBiome();

    /**
     * The biome's registry key that will be used to register it.
     */
    public final RegistryKey<Biome> getRegistryKey() {
        return register(name);
    }

    /**
     * The mixed noise point used for placing the biome.
     */
    public final MixedNoisePoint getMixedNoisePoint() {
        return noise;
    }
}