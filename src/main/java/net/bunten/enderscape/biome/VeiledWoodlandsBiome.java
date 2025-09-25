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

public class VeiledWoodlandsBiome {

    public static Biome create(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder()
                .addSpawn(MobCategory.CREATURE, 6, new MobSpawnSettings.SpawnerData(EnderscapeEntities.RUSTLE, 2, 3))
                .addSpawn(MobCategory.MONSTER, 2, new MobSpawnSettings.SpawnerData(EnderscapeEntities.RUBBLEMITE, 2, 3));

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

                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.UNCOMMON_CHORUS_PLANTS)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.UNCOMMON_CHORUS_SPROUTS)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.COMMON_DRY_END_GROWTH)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.VEILED_TREES)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.VEILED_WOODLANDS_VEGETATION)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.WISP_FLOWER_PATCHES)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.MURUBLIGHT_BRACKET);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.5F)
                .downfall(0.5F)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(0x161621)
                        .fogColor(EnderscapeBiomes.lightenFogColor(0x101017))
                        .waterColor(0x96a3b1)
                        .waterFogColor(0x464d59)
                        .foliageColorOverride(0xa1b298)
                        .grassColorOverride(0xa1b298)
                        .ambientParticle(new AmbientParticleSettings(EnderscapeParticles.VOID_STARS, 0.003F))
                        .ambientLoopSound(EnderscapeBiomeSounds.VEILED_WOODLANDS.loop())
                        .ambientAdditionsSound(new AmbientAdditionsSettings(EnderscapeBiomeSounds.VEILED_WOODLANDS.additions(), 0.0006))
                        .ambientMoodSound(new AmbientMoodSettings(EnderscapeBiomeSounds.VEILED_WOODLANDS.mood(), 6000, 8, 2))
                        .backgroundMusic(Musics.createGameMusic(EnderscapeBiomeSounds.VEILED_WOODLANDS.music()))
                        .build()
                )

                .mobSpawnSettings(spawns.build())
                .generationSettings(builder.build())
                .build();
    }
}
