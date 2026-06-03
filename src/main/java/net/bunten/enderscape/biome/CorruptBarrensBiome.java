package net.bunten.enderscape.biome;

import net.bunten.enderscape.registry.*;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.EndPlacements;
import net.minecraft.util.ARGB;
import net.minecraft.world.attribute.*;
import net.minecraft.world.attribute.modifier.FloatModifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
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

public class CorruptBarrensBiome {

    public static Biome create(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder()
                .addSpawn(MobCategory.MONSTER, 4, new MobSpawnSettings.SpawnerData(EntityTypes.ENDERMITE, 1, 1))
                .addSpawn(MobCategory.MONSTER, 1, new MobSpawnSettings.SpawnerData(EnderscapeEntities.RUBBLEMITE, 1, 1))
                .addMobCharge(EntityTypes.ENDERMITE, 0.3, 0.1)
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
                .putAttributes(EnvironmentAttributeMap.builder()
                        .set(EnderscapeEnvironmentAttributes.AMBIENT_LIGHT_FACTOR, 0.75F)

                        .set(EnderscapeEnvironmentAttributes.NEBULA_COLOR, 0x555189)
                        .modify(EnderscapeEnvironmentAttributes.NEBULA_ALPHA, FloatModifier.MULTIPLY, 0.8F)
                        .set(EnderscapeEnvironmentAttributes.STAR_COLOR, 0x9F89FF)
                        .modify(EnderscapeEnvironmentAttributes.STAR_ALPHA, FloatModifier.MULTIPLY, 0.7F)

                        .set(EnvironmentAttributes.SKY_COLOR, ARGB.scaleRGB(EnderscapeBiomes.DEFAULT_SKY_COLOR, EnderscapeBiomes.CORRUPT_BARRENS_DARKENING_FACTOR))
                        .set(EnvironmentAttributes.SKY_LIGHT_COLOR, 0x242035)
                        .set(EnvironmentAttributes.FOG_COLOR, 0x0B090F)
                        .set(EnvironmentAttributes.FOG_START_DISTANCE, -40.0F)
                        .modify(EnderscapeEnvironmentAttributes.FOG_END_DENSITY, FloatModifier.MULTIPLY, 0.5F)
                        .set(EnvironmentAttributes.CLOUD_COLOR, EnderscapeBiomes.DEFAULT_CLOUD_COLOR)

                        .set(EnvironmentAttributes.WATER_FOG_COLOR, 0x544f63)
                        .set(EnvironmentAttributes.AMBIENT_PARTICLES, AmbientParticle.of(EnderscapeParticles.CORRUPT_SPORES, 0.03F))
                        .set(EnvironmentAttributes.AMBIENT_SOUNDS, new AmbientSounds(
                                Optional.of(EnderscapeBiomeSounds.CORRUPT_BARRENS.loop()),
                                Optional.of(new AmbientMoodSettings(EnderscapeBiomeSounds.CORRUPT_BARRENS.mood(), 6000, 8, 2)),
                                List.of(new AmbientAdditionsSettings(EnderscapeBiomeSounds.CORRUPT_BARRENS.additions(), 0.0015))
                        ))
                        .set(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(EnderscapeBiomeSounds.CORRUPT_BARRENS.music()))
                        .build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(0x6a647d)
                        .foliageColorOverride(0x847c91)
                        .grassColorOverride(0x847c91)
                        .build()
                )

                .mobSpawnSettings(spawns.build())
                .generationSettings(builder.build())
                .build();
    }
}
