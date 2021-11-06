package net.enderscape.world.biomes;

import net.enderscape.registry.EndMusic;
import net.enderscape.registry.EndParticles;
import net.enderscape.registry.EndSounds;
import net.enderscape.world.EndBiomeFeatures;
import net.enderscape.world.EndBiomes;
import net.enderscape.world.EndSurfaces;
import net.minecraft.sound.BiomeAdditionsSound;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.biome.*;

public class CelestialIslandsBiome extends EnderscapeIslandsBiome {
    public CelestialIslandsBiome() {
        super(EndBiomes.CELESTIAL_PLAINS, "celestial_islands");
    }

    @Override
    public Biome getBiome() {
        SoundEvent loopSound = EndSounds.AMBIENT_CELESTIAL_LOOP;
        SoundEvent additionsSound = EndSounds.AMBIENT_CELESTIAL_ADDITIONS;
        BiomeMoodSound moodSound = new BiomeMoodSound(EndSounds.AMBIENT_CELESTIAL_MOOD, 6000, 8, 2);

        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        GenerationSettings.Builder builder = new GenerationSettings.Builder();
        builder.surfaceBuilder(EndSurfaces.CELESTIAL);
        EndBiomeFeatures.addCelestialIslandFeatures(builder, spawnSettings);

        return (new Biome.Builder())
                .precipitation(Biome.Precipitation.NONE)
                .category(Biome.Category.THEEND)
                .depth(0.1F)
                .scale(0.1F)
                .temperature(0.7F)
                .downfall(0)

                .effects((new BiomeEffects.Builder()
                        .music(EndMusic.CELESTIAL)
                        .loopSound(loopSound)
                        .additionsSound(new BiomeAdditionsSound(additionsSound, 0.003))
                        .moodSound(moodSound)
                        .particleConfig(new BiomeParticleConfig(EndParticles.CELESTIAL_SPORES, util.getIslandSporeCount())))
                        .waterColor(54527)
                        .waterFogColor(54527)
                        .fogColor(0x130F1B)
                        .skyColor(util.getSkyColor())
                        .build())
                .spawnSettings(spawnSettings.build()).generationSettings(builder.build()).build();
    }
}