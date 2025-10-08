package net.bunten.enderscape.biome;

import net.bunten.enderscape.registry.*;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.EndPlacements;
import net.minecraft.sounds.Musics;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import static net.minecraft.world.level.levelgen.GenerationStep.Decoration.*;

public class CelestialGroveBiome {

    public static Biome create(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder()
                .addSpawn(MobCategory.CREATURE, 6, new MobSpawnSettings.SpawnerData(EnderscapeEntities.DRIFTER, 3, 6))
                .addSpawn(MobCategory.CREATURE, 2, new MobSpawnSettings.SpawnerData(EnderscapeEntities.DRIFTLET, 2, 3));

        BiomeDefaultFeatures.endSpawns(spawns);

        BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(features, carvers)
                .addFeature(SURFACE_STRUCTURES, EndPlacements.END_GATEWAY_RETURN)

                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.VOID_SHALE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.VOID_SHALE_BLOBS)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.VERADITE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.MIRESTONE_BLOBS)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.SHADOLINE_ORE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.SCATTERED_SHADOLINE_ORE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.NEBULITE_ORE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.CEILING_NEBULITE_ORE)

                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.CHORUS_PLANTS)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.COMMON_CHORUS_SPROUTS)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.LARGE_CELESTIAL_CHANTERELLES)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.CELESTIAL_GROWTH)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.CELESTIAL_GROVE_VEGETATION)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.BULB_FLOWERS)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.COMMON_DRY_END_GROWTH)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.UNCOMMON_MURUBLIGHT_BRACKET);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.5F)
                .downfall(0.5F)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(EnderscapeBiomes.DEFAULT_SKY_COLOR)
                        .fogColor(EnderscapeBiomes.lightenFogColor(0x120D14))
                        .waterColor(0x4ec7ab)
                        .waterFogColor(0x3d7363)
                        .foliageColorOverride(0xB6DB62)
                        .grassColorOverride(0xB6DB61)
                        .ambientParticle(new AmbientParticleSettings(EnderscapeParticles.CELESTIAL_SPORES, 0.012F))
                        .ambientLoopSound(EnderscapeBiomeSounds.CELESTIAL_GROVE.loop())
                        .ambientAdditionsSound(new AmbientAdditionsSettings(EnderscapeBiomeSounds.CELESTIAL_GROVE.additions(), 0.0015))
                        .ambientMoodSound(new AmbientMoodSettings(EnderscapeBiomeSounds.CELESTIAL_GROVE.mood(), 6000, 8, 2))
                        .backgroundMusic(Musics.createGameMusic(EnderscapeBiomeSounds.CELESTIAL_GROVE.music()))
                        .build()
                )

                .mobSpawnSettings(spawns.build())
                .generationSettings(builder.build())
                .build();
    }
}
