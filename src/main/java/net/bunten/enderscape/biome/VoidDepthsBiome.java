package net.bunten.enderscape.biome;

import net.bunten.enderscape.registry.*;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.util.ARGB;
import net.minecraft.world.attribute.*;
import net.minecraft.world.attribute.modifier.FloatModifier;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.Optional;

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
                .putAttributes(EnvironmentAttributeMap.builder()
                        .set(EnderscapeEnvironmentAttributes.AMBIENT_LIGHT_FACTOR, 0.5F)

                        .set(EnderscapeEnvironmentAttributes.NEBULA_COLOR, ARGB.scaleRGB(EnderscapeEnvironmentAttributes.DEFAULT_NEBULA_COLOR, EnderscapeBiomes.VOID_BIOMES_DARKENING_FACTOR))
                        .set(EnderscapeEnvironmentAttributes.STAR_COLOR, ARGB.scaleRGB(EnderscapeEnvironmentAttributes.DEFAULT_STAR_COLOR, EnderscapeBiomes.VOID_BIOMES_DARKENING_FACTOR))

                        .set(EnvironmentAttributes.SKY_COLOR, ARGB.scaleRGB(EnderscapeBiomes.DEFAULT_SKY_COLOR, EnderscapeBiomes.VOID_BIOMES_DARKENING_FACTOR))
                        .set(EnvironmentAttributes.SKY_LIGHT_COLOR, 0x000000)
                        .set(EnvironmentAttributes.FOG_COLOR, ARGB.scaleRGB(EnderscapeBiomes.DEFAULT_FOG_COLOR, EnderscapeBiomes.VOID_BIOMES_DARKENING_FACTOR))
                        .set(EnvironmentAttributes.FOG_START_DISTANCE, -40.0F)
                        .set(EnderscapeEnvironmentAttributes.FOG_END_DENSITY, 0.3F)
                        .set(EnvironmentAttributes.CLOUD_COLOR, EnderscapeBiomes.DEFAULT_CLOUD_COLOR)

                        .set(EnvironmentAttributes.AMBIENT_PARTICLES, AmbientParticle.of(EnderscapeParticles.VOID_STARS, 0.006F))
                        .set(EnvironmentAttributes.AMBIENT_SOUNDS, new AmbientSounds(
                                Optional.of(EnderscapeBiomeSounds.VOID_DEPTHS.loop()),
                                Optional.of(new AmbientMoodSettings(EnderscapeBiomeSounds.VOID_DEPTHS.mood(), 6000, 8, 2)),
                                List.of(new AmbientAdditionsSettings(EnderscapeBiomeSounds.VOID_DEPTHS.additions(), 0.0006))
                        ))
                        .set(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(EnderscapeBiomeSounds.VOID_DEPTHS.music()))
                        .build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(EnderscapeBiomes.DEFAULT_WATER_COLOR)
                        .foliageColorOverride(EnderscapeBiomes.DEFAULT_FOLIAGE_COLOR)
                        .grassColorOverride(EnderscapeBiomes.DEFAULT_GRASS_COLOR)
                        .build()
                )

                .mobSpawnSettings(spawns.build())
                .generationSettings(builder.build())
                .build();
    }
}
