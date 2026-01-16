package net.bunten.enderscape.biome;

import net.bunten.enderscape.registry.*;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.EndPlacements;
import net.minecraft.world.attribute.*;
import net.minecraft.world.attribute.modifier.FloatModifier;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.Optional;

import static net.minecraft.world.level.levelgen.GenerationStep.Decoration.*;

public class MagniaCragsBiome {

    public static Biome create(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder().addSpawn(MobCategory.MONSTER, 2, new MobSpawnSettings.SpawnerData(EnderscapeEntities.RUBBLEMITE, 2, 3));

        BiomeDefaultFeatures.endSpawns(spawns);

        BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(features, carvers)
                .addFeature(UNDERGROUND_STRUCTURES, EnderscapePlacedFeatures.MAGNIA_ARCHES)

                .addFeature(SURFACE_STRUCTURES, EndPlacements.END_GATEWAY_RETURN)
                .addFeature(SURFACE_STRUCTURES, EnderscapePlacedFeatures.MAGNIA_TOWERS)
                .addFeature(SURFACE_STRUCTURES, EnderscapePlacedFeatures.MAGNIA_SPIKES)
                .addFeature(SURFACE_STRUCTURES, EnderscapePlacedFeatures.LARGE_MAGNIA_SPIKES)

                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.VOID_SHALE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.VOID_SHALE_BLOBS)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.VERADITE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.MIRESTONE_BLOBS)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.SHADOLINE_ORE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.SCATTERED_SHADOLINE_ORE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.NEBULITE_ORE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.CEILING_NEBULITE_ORE)

                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.UPWARD_ALLURING_MAGNIA_SPROUTS)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.DOWNWARD_REPULSIVE_MAGNIA_SPROUTS)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.UNCOMMON_CHORUS_PLANTS)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.UNCOMMON_CHORUS_SPROUTS)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.COMMON_DRY_END_GROWTH)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.MURUBLIGHT_BRACKET);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.5F)
                .downfall(0.5F)
                .putAttributes(EnvironmentAttributeMap.builder()
                        .set(EnderscapeEnvironmentAttributes.NEBULA_COLOR, 0x96AD9E)
                        .modify(EnderscapeEnvironmentAttributes.NEBULA_ALPHA, FloatModifier.MULTIPLY, 0.5F)
                        .set(EnderscapeEnvironmentAttributes.STAR_COLOR, 0x89FFD5)
                        .modify(EnderscapeEnvironmentAttributes.STAR_ALPHA, FloatModifier.MULTIPLY, 0.85F)

                        .set(EnvironmentAttributes.SKY_COLOR, 0x151616)
                        .set(EnvironmentAttributes.SKY_LIGHT_COLOR, 0x374745)
                        .set(EnvironmentAttributes.FOG_COLOR, 0x0F1010)
                        .set(EnvironmentAttributes.CLOUD_COLOR, EnderscapeBiomes.DEFAULT_CLOUD_COLOR)

                        .set(EnvironmentAttributes.WATER_FOG_COLOR, 0x5e656e)
                        .set(EnvironmentAttributes.AMBIENT_PARTICLES, AmbientParticle.of(EnderscapeParticles.VOID_STARS, 0.003F))
                        .set(EnvironmentAttributes.AMBIENT_SOUNDS, new AmbientSounds(
                                Optional.of(EnderscapeBiomeSounds.MAGNIA_FIELDS.loop()),
                                Optional.of(new AmbientMoodSettings(EnderscapeBiomeSounds.MAGNIA_FIELDS.mood(), 6000, 8, 2)),
                                List.of(new AmbientAdditionsSettings(EnderscapeBiomeSounds.MAGNIA_FIELDS.additions(), 0.0015))
                        ))
                        .set(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(EnderscapeBiomeSounds.MAGNIA_FIELDS.music()))
                        .build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(0x89919c)
                        .foliageColorOverride(0x919c8c)
                        .grassColorOverride(0x919c8c)
                        .build()
                )

                .mobSpawnSettings(spawns.build())
                .generationSettings(builder.build())
                .build();
    }
}
