package net.enderscape.world.biomes;

import net.enderscape.registry.EndParticles;
import net.enderscape.world.EndBiomeFeatures;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

/**
 * The basis for the center island which becomes minecraft:the_end
 */
public class CenterEndBiome extends EnderscapeBiome {
    public CenterEndBiome() {
        super("center_island", createNoise(0, 0, 0, 0, 0));
    }

    @Override
    public Biome getBiome() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        GenerationSettings.Builder builder = new GenerationSettings.Builder();
        builder.surfaceBuilder(ConfiguredSurfaceBuilders.END);
        EndBiomeFeatures.addCenterIslandFeatures(builder, spawnSettings);

        return (new Biome.Builder())
                .precipitation(Biome.Precipitation.NONE)
                .category(Biome.Category.THEEND)
                .depth(0.1F)
                .scale(0.1F)
                .temperature(0.7F)
                .downfall(0)

                .effects((new BiomeEffects.Builder()
                        .particleConfig(new BiomeParticleConfig(EndParticles.DRAGON_EMBER, 0.025F)))

                        .waterColor(54527)
                        .waterFogColor(54527)
                        .fogColor(0x451553)
                        .skyColor(util.getSkyColor())
                        .build())
                .spawnSettings(spawnSettings.build()).generationSettings(builder.build()).build();
    }
}