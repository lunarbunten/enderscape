package net.bunten.enderscape.biome;

import net.bunten.enderscape.registry.*;
import net.bunten.enderscape.util.RGBA;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.EndPlacements;
import net.minecraft.sounds.Musics;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import static net.minecraft.world.level.levelgen.GenerationStep.Decoration.*;

public class CorruptBarrensBiome {

    public static Biome create(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder()
                .addSpawn(MobCategory.MONSTER, 4, new MobSpawnSettings.SpawnerData(EntityType.ENDERMITE, 1, 1))
                .addSpawn(MobCategory.MONSTER, 1, new MobSpawnSettings.SpawnerData(EnderscapeEntities.RUBBLEMITE, 1, 1))
                .addMobCharge(EntityType.ENDERMITE, 0.3, 0.1)
                .addMobCharge(EnderscapeEntities.RUBBLEMITE, 0.6, 0.1);

        BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(features, carvers)
                .addFeature(SURFACE_STRUCTURES, EndPlacements.END_GATEWAY_RETURN)
                .addFeature(SURFACE_STRUCTURES, EnderscapePlacedFeatures.MIRESTONE_PILLARS)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.VOID_SHALE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.VOID_SHALE_BLOBS)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.KURODITE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.SHADOLINE_ORE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.SCATTERED_SHADOLINE_ORE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.NEBULITE_ORE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.COMMON_CEILING_NEBULITE_ORE)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.LARGE_MURUBLIGHT_CHANTERELLES)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.BLINKLIGHT_VINES)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.DOWNWARD_CORRUPT_GROWTH)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.DOWNWARD_TALL_CORRUPT_GROWTH)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.UPWARD_CORRUPT_GROWTH)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.UPWARD_TALL_CORRUPT_GROWTH)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.CORRUPT_BARRENS_VEGETATION)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.MURUBLIGHT_BRACKET);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.5F)
                .downfall(0.5F)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(RGBA.darkenColor(EnderscapeBiomes.DEFAULT_SKY_COLOR, EnderscapeBiomes.CORRUPT_BARRENS_DARKENING_FACTOR))
                        .fogColor(EnderscapeBiomes.lightenFogColor(0x0B090F))
                        .waterColor(0x6a647d)
                        .waterFogColor(0x544f63)
                        .foliageColorOverride(0x847c91)
                        .grassColorOverride(0x847c91)
                        .ambientParticle(new AmbientParticleSettings(EnderscapeParticles.CORRUPT_SPORES, 0.03F))
                        .ambientLoopSound(EnderscapeBiomeSounds.CORRUPT_BARRENS.loop())
                        .ambientAdditionsSound(new AmbientAdditionsSettings(EnderscapeBiomeSounds.CORRUPT_BARRENS.additions(), 0.0015))
                        .ambientMoodSound(new AmbientMoodSettings(EnderscapeBiomeSounds.CORRUPT_BARRENS.mood(), 6000, 8, 2))
                        .backgroundMusic(Musics.createGameMusic(EnderscapeBiomeSounds.CORRUPT_BARRENS.music()))
                        .build()
                )

                .mobSpawnSettings(spawns.build())
                .generationSettings(builder.build())
                .build();
    }
}
