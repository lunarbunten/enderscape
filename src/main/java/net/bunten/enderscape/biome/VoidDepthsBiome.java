package net.bunten.enderscape.biome;

import net.bunten.enderscape.registry.*;
import net.bunten.enderscape.util.RGBA;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.sounds.Musics;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import static net.minecraft.world.level.levelgen.GenerationStep.Decoration.UNDERGROUND_ORES;
import static net.minecraft.world.level.levelgen.GenerationStep.Decoration.VEGETAL_DECORATION;

public class VoidDepthsBiome {

    public static Biome create(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(features, carvers)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.VERADITE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.MIRESTONE_BLOBS)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.SHADOLINE_ORE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.SCATTERED_SHADOLINE_ORE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.NEBULITE_ORE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.CEILING_NEBULITE_ORE)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.UNCOMMON_CHORUS_PLANTS)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.UNCOMMON_CHORUS_SPROUTS)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.MURUBLIGHT_BRACKET);

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.5F)
                .downfall(0.5F)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(RGBA.darkenColor(EnderscapeBiomes.DEFAULT_SKY_COLOR, EnderscapeBiomes.VOID_BIOMES_DARKENING_FACTOR))
                        .fogColor(RGBA.darkenColor(EnderscapeBiomes.DEFAULT_FOG_COLOR, EnderscapeBiomes.VOID_BIOMES_DARKENING_FACTOR))
                        .waterColor(EnderscapeBiomes.DEFAULT_WATER_COLOR)
                        .waterFogColor(EnderscapeBiomes.DEFAULT_WATER_FOG_COLOR)
                        .foliageColorOverride(EnderscapeBiomes.DEFAULT_FOLIAGE_COLOR)
                        .grassColorOverride(EnderscapeBiomes.DEFAULT_GRASS_COLOR)
                        .ambientParticle(new AmbientParticleSettings(EnderscapeParticles.VOID_STARS, 0.006F))
                        .ambientLoopSound(EnderscapeBiomeSounds.VOID_DEPTHS.loop())
                        .ambientAdditionsSound(new AmbientAdditionsSettings(EnderscapeBiomeSounds.VOID_DEPTHS.additions(), 0.0006))
                        .ambientMoodSound(new AmbientMoodSettings(EnderscapeBiomeSounds.VOID_DEPTHS.mood(), 6000, 8, 2))
                        .backgroundMusic(Musics.createGameMusic(EnderscapeBiomeSounds.VOID_DEPTHS.music()))
                        .build()
                )

                .mobSpawnSettings(spawns.build())
                .generationSettings(builder.build())
                .build();
    }
}
