package net.penumbra.enderscape.registry.level;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.EndPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ARGB;
import net.minecraft.world.attribute.*;
import net.minecraft.world.attribute.modifier.ColorModifier;
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
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.registry.entity.EnderscapeEntities;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;
import net.penumbra.enderscape.registry.sound.EnderscapeBiomeSounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.minecraft.world.attribute.EnvironmentAttributes.*;
import static net.minecraft.world.level.levelgen.GenerationStep.Decoration.*;
import static net.penumbra.enderscape.registry.level.EnderscapeEnvironmentAttributes.*;

public class EnderscapeBiomes {

    public static final int RUBBLEMITE_DEFAULT_WEIGHT = 2;

    public static final int DEFAULT_SKY_COLOR = 0x181321;
    public static final int DEFAULT_FOG_COLOR = 0x110D18;
    public static final int DEFAULT_CLOUD_COLOR = 0x00000000;
    public static final int DEFAULT_GRASS_COLOR = 0xa1b783;
    public static final int DEFAULT_FOLIAGE_COLOR = 0xa1b783;
    public static final int DEFAULT_WATER_COLOR = 0x797aa1;
    public static final int DEFAULT_WATER_FOG_COLOR = 0x383955;

    private static final int VOID_BIOMES_BLEND_COLOR = ARGB.colorFromFloat(1.0F, 0.35F, 0.25F, 0.45F);

    public static final List<ResourceKey<Biome>> BIOMES = new ArrayList<>();

    public static final ResourceKey<Biome> CELESTIAL_GROVE = register("celestial_grove");
    public static final ResourceKey<Biome> CORRUPT_BARRENS = register("corrupt_barrens");
    public static final ResourceKey<Biome> MAGNIA_FIELDS = register("magnia_fields");
    public static final ResourceKey<Biome> VEILED_WOODLANDS = register("veiled_woodlands");
    public static final ResourceKey<Biome> VOID_DEPTHS = register("void_depths");
    public static final ResourceKey<Biome> VOID_SKIES = register("void_skies");
    public static final ResourceKey<Biome> VOID_SKY_ISLANDS = register("void_sky_islands");

    public static void bootstrap(BootstrapContext<Biome> context) {
        context.register(CELESTIAL_GROVE, celestialGrove(context));
        context.register(CORRUPT_BARRENS, corruptBarrens(context));
        context.register(MAGNIA_FIELDS, magniaCrags(context));
        context.register(VEILED_WOODLANDS, veiledWoodlands(context));
        context.register(VOID_DEPTHS, voidDepths(context));
        context.register(VOID_SKIES, voidSkies(context));
        context.register(VOID_SKY_ISLANDS, voidSkyIslands(context));
    }

    private static ResourceKey<Biome> register(String name) {
        ResourceKey<Biome> key = ResourceKey.create(Registries.BIOME, Enderscape.id(name));
        BIOMES.add(key);
        return key;
    }

    public static Biome celestialGrove(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder()
                .addSpawn(
                        MobCategory.CREATURE,
                        6,
                        new MobSpawnSettings.SpawnerData(
                                EnderscapeEntities.DRIFTER,
                                2,
                                3
                        )
                );

        BiomeDefaultFeatures.endSpawns(spawns);

        BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(features, carvers)
                .addFeature(LAKES, EnderscapePlacedFeatures.UNCOMMON_VOID_LAKES)

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

        EnvironmentAttributeMap attributes = EnvironmentAttributeMap.builder()
                .set(NEBULA_COLOR, 0x875643)
                .set(STAR_COLOR, 0xFFA589)

                .modify(NEBULA_BRIGHTNESS, FloatModifier.MULTIPLY, 1.15F)
                .modify(STAR_BRIGHTNESS, FloatModifier.MULTIPLY, 1.4F)

                .set(SKY_LIGHT_COLOR, 0x6B3D4D)
                .set(FOG_COLOR, 0x120D14)
                .set(FOG_END_DENSITY, 0.85F)
                .set(CLOUD_COLOR, DEFAULT_CLOUD_COLOR)

                .set(WATER_FOG_COLOR, 0x3d7363)
                .set(AMBIENT_PARTICLES, AmbientParticle.of(EnderscapeParticles.CELESTIAL_SPORES, 0.012F))
                .set(AMBIENT_SOUNDS, new AmbientSounds(
                        Optional.of(EnderscapeBiomeSounds.CELESTIAL_GROVE.loop()),
                        Optional.of(new AmbientMoodSettings(EnderscapeBiomeSounds.CELESTIAL_GROVE.mood(), 6000, 8, 2)),
                        List.of(new AmbientAdditionsSettings(EnderscapeBiomeSounds.CELESTIAL_GROVE.additions(), 0.0015))
                ))
                .set(BACKGROUND_MUSIC, new BackgroundMusic(EnderscapeBiomeSounds.CELESTIAL_GROVE.music()))
                .build();

        BiomeSpecialEffects specialEffects = specialEffects(0x4ec7ab, 0xB6DB62, 0xB6DB61);

        return createBiome(builder, spawns.build(), attributes, specialEffects);
    }

    public static Biome corruptBarrens(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder()
                .addSpawn(
                        MobCategory.MONSTER,
                        RUBBLEMITE_DEFAULT_WEIGHT * 2,
                        new MobSpawnSettings.SpawnerData(
                                EntityTypes.ENDERMITE,
                                1,
                                1
                        )
                )
                .addSpawn(
                        MobCategory.MONSTER,
                        RUBBLEMITE_DEFAULT_WEIGHT,
                        new MobSpawnSettings.SpawnerData(
                                EnderscapeEntities.RUBBLEMITE,
                                1,
                                1
                        )
                )
                .addMobCharge(
                        EntityTypes.ENDERMITE,
                        1, 2
                )
                .addMobCharge(
                        EnderscapeEntities.RUBBLEMITE,
                        1, 1
                );

        BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(features, carvers)
                .addFeature(LAKES, EnderscapePlacedFeatures.VERY_COMMON_VOID_LAKES)

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
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.MURUBLIGHT_BRACKET)

                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.COMMON_VOID_FIRE_ON_VOID_SHALE)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.VOID_FIRE_ON_KURODITE);

        EnvironmentAttributeMap attributes = EnvironmentAttributeMap.builder()
                .modify(AMBIENT_LIGHT_COLOR, ColorModifier.MULTIPLY_RGB, ARGB.colorFromFloat(1.0F, 0.7F, 0.55F, 0.75F))

                .modify(NEBULA_BRIGHTNESS, FloatModifier.MULTIPLY, 0.9F)
                .modify(STAR_BRIGHTNESS, FloatModifier.MULTIPLY, 1.2F)

                .set(SKY_LIGHT_COLOR, 0x242035)
                .modify(SKY_COLOR, ColorModifier.MULTIPLY_RGB, ARGB.colorFromFloat(1.0F, 0.4F, 0.3F, 0.5F))
                .set(SKY_LIGHT_COLOR, 0x242035)
                .set(FOG_COLOR, 0x07040B)
                .set(FOG_START_DISTANCE, -20.0F)
                .modify(FOG_END_DENSITY, FloatModifier.MULTIPLY, 0.5F)
                .set(CLOUD_COLOR, DEFAULT_CLOUD_COLOR)

                .set(WATER_FOG_COLOR, 0x544f63)
                .set(AMBIENT_PARTICLES, AmbientParticle.of(EnderscapeParticles.VOID_STARS, 0.0045F))
                .set(AMBIENT_SOUNDS, new AmbientSounds(
                        Optional.of(EnderscapeBiomeSounds.CORRUPT_BARRENS.loop()),
                        Optional.of(new AmbientMoodSettings(EnderscapeBiomeSounds.CORRUPT_BARRENS.mood(), 6000, 8, 2)),
                        List.of(new AmbientAdditionsSettings(EnderscapeBiomeSounds.CORRUPT_BARRENS.additions(), 0.0015))
                ))
                .set(BACKGROUND_MUSIC, new BackgroundMusic(EnderscapeBiomeSounds.CORRUPT_BARRENS.music()))
                .build();

        BiomeSpecialEffects specialEffects = specialEffects(0x6a647d, 0x847c91, 0x847c91);

        return createBiome(builder, spawns.build(), attributes, specialEffects);
    }

    public static Biome veiledWoodlands(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder()
                .addSpawn(
                        MobCategory.CREATURE,
                        6,
                        new MobSpawnSettings.SpawnerData(
                                EnderscapeEntities.RUSTLE,
                                2,
                                3
                        )
                )
                .addSpawn(
                        MobCategory.MONSTER,
                        RUBBLEMITE_DEFAULT_WEIGHT,
                        new MobSpawnSettings.SpawnerData(
                                EnderscapeEntities.RUBBLEMITE,
                                2,
                                3
                        )
                )
                .addMobCharge(
                        EnderscapeEntities.RUBBLEMITE,
                        0.2, 0.6
                );

        BiomeDefaultFeatures.endSpawns(spawns);

        BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(features, carvers)
                .addFeature(LAKES, EnderscapePlacedFeatures.COMMON_VOID_LAKES)

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
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.WISP_FLOWERS)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.MURUBLIGHT_BRACKET)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.VOID_FIRE_ON_VOID_SHALE);

        EnvironmentAttributeMap attributes = EnvironmentAttributeMap.builder()
                .set(NEBULA_COLOR, 0x969BAA)
                .set(STAR_COLOR, 0xB493FF)

                .modify(NEBULA_BRIGHTNESS, FloatModifier.MULTIPLY, 0.75F)

                .set(SKY_COLOR, 0x161621)
                .set(SKY_LIGHT_COLOR, 0x242035)
                .set(FOG_COLOR, 0x101017)
                .set(FOG_END_DENSITY, 0.75F)
                .set(CLOUD_COLOR, DEFAULT_CLOUD_COLOR)

                .set(WATER_FOG_COLOR, 0x464d59)
                .set(AMBIENT_PARTICLES, AmbientParticle.of(EnderscapeParticles.VOID_STARS, 0.003F))
                .set(AMBIENT_SOUNDS, new AmbientSounds(
                        Optional.of(EnderscapeBiomeSounds.VEILED_WOODLANDS.loop()),
                        Optional.of(new AmbientMoodSettings(EnderscapeBiomeSounds.VEILED_WOODLANDS.mood(), 6000, 8, 2)),
                        List.of(new AmbientAdditionsSettings(EnderscapeBiomeSounds.VEILED_WOODLANDS.additions(), 0.0006))
                ))
                .set(BACKGROUND_MUSIC, new BackgroundMusic(EnderscapeBiomeSounds.VEILED_WOODLANDS.music()))
                .build();

        BiomeSpecialEffects specialEffects = specialEffects(0x96a3b1, 0xa1b298, 0xa1b298);

        return createBiome(builder, spawns.build(), attributes, specialEffects);
    }

    public static Biome magniaCrags(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder()
                .addSpawn(
                        MobCategory.MONSTER,
                        RUBBLEMITE_DEFAULT_WEIGHT,
                        new MobSpawnSettings.SpawnerData(
                                EnderscapeEntities.RUBBLEMITE,
                                2,
                                3
                        )
                )
                .addMobCharge(
                        EnderscapeEntities.RUBBLEMITE,
                        0.2, 0.6
                );

        BiomeDefaultFeatures.endSpawns(spawns);

        BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(features, carvers)
                .addFeature(LAKES, EnderscapePlacedFeatures.VOID_LAKES)

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
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.MURUBLIGHT_BRACKET)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.VOID_FIRE_ON_VOID_SHALE);

        EnvironmentAttributeMap attributes = EnvironmentAttributeMap.builder()
                .set(NEBULA_COLOR, 0x96AD9E)
                .set(STAR_COLOR, 0x89FFD5)

                .modify(NEBULA_BRIGHTNESS, FloatModifier.MULTIPLY, 0.5F)
                .modify(STAR_BRIGHTNESS, FloatModifier.MULTIPLY, 0.85F)

                .set(SKY_COLOR, 0x151616)
                .set(SKY_LIGHT_COLOR, 0x374745)
                .set(FOG_COLOR, 0x0F1010)
                .set(CLOUD_COLOR, DEFAULT_CLOUD_COLOR)

                .set(WATER_FOG_COLOR, 0x5e656e)
                .set(AMBIENT_PARTICLES, AmbientParticle.of(EnderscapeParticles.VOID_STARS, 0.003F))
                .set(AMBIENT_SOUNDS, new AmbientSounds(
                        Optional.of(EnderscapeBiomeSounds.MAGNIA_FIELDS.loop()),
                        Optional.of(new AmbientMoodSettings(EnderscapeBiomeSounds.MAGNIA_FIELDS.mood(), 6000, 8, 2)),
                        List.of(new AmbientAdditionsSettings(EnderscapeBiomeSounds.MAGNIA_FIELDS.additions(), 0.0015))
                ))
                .set(BACKGROUND_MUSIC, new BackgroundMusic(EnderscapeBiomeSounds.MAGNIA_FIELDS.music()))
                .build();

        BiomeSpecialEffects specialEffects = specialEffects(0x89919c, 0x919c8c, 0x919c8c);

        return createBiome(builder, spawns.build(), attributes, specialEffects);
    }

    public static Biome voidDepths(BootstrapContext<Biome> context) {
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
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.MURUBLIGHT_BRACKET)
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.VOID_FIRE_ON_VOID_SHALE);

        EnvironmentAttributeMap.Builder attributes = EnvironmentAttributeMap.builder()
                .set(CLOUD_COLOR, DEFAULT_CLOUD_COLOR)
                .set(AMBIENT_SOUNDS, new AmbientSounds(
                        Optional.of(EnderscapeBiomeSounds.VOID_DEPTHS.loop()),
                        Optional.of(new AmbientMoodSettings(EnderscapeBiomeSounds.VOID_DEPTHS.mood(), 6000, 8, 2)),
                        List.of(new AmbientAdditionsSettings(EnderscapeBiomeSounds.VOID_DEPTHS.additions(), 0.0006))
                ));

        voidBiomeEnvironmentAttributes(attributes);

        return createBiome(builder, new MobSpawnSettings.Builder().build(), attributes.build());
    }

    public static Biome voidSkies(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(features, carvers)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.VERADITE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.MIRESTONE_BLOBS)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.SHADOLINE_ORE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.SCATTERED_SHADOLINE_ORE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.NEBULITE_ORE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.CEILING_NEBULITE_ORE);

        EnvironmentAttributeMap.Builder attributes = EnvironmentAttributeMap.builder()
                .set(CLOUD_COLOR, DEFAULT_CLOUD_COLOR)
                .set(AMBIENT_SOUNDS, new AmbientSounds(
                        Optional.of(EnderscapeBiomeSounds.VOID_SKIES.loop()),
                        Optional.of(new AmbientMoodSettings(EnderscapeBiomeSounds.VOID_SKIES.mood(), 6000, 8, 2)),
                        List.of(new AmbientAdditionsSettings(EnderscapeBiomeSounds.VOID_SKIES.additions(), 0.0006))
                ));

        voidBiomeEnvironmentAttributes(attributes);

        return createBiome(builder, new MobSpawnSettings.Builder().build(), attributes.build());
    }

    public static Biome voidSkyIslands(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(features, carvers)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.VERADITE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.MIRESTONE_BLOBS)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.SHADOLINE_ORE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.SCATTERED_SHADOLINE_ORE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.NEBULITE_ORE)
                .addFeature(UNDERGROUND_ORES, EnderscapePlacedFeatures.CEILING_NEBULITE_ORE)
                .addFeature(SURFACE_STRUCTURES, EnderscapePlacedFeatures.SMALL_SKY_ISLANDS);

        EnvironmentAttributeMap.Builder attributes = EnvironmentAttributeMap.builder()
                .set(CLOUD_COLOR, DEFAULT_CLOUD_COLOR)
                .set(AMBIENT_SOUNDS, new AmbientSounds(
                        Optional.of(EnderscapeBiomeSounds.VOID_SKY_ISLANDS.loop()),
                        Optional.of(new AmbientMoodSettings(EnderscapeBiomeSounds.VOID_SKY_ISLANDS.mood(), 6000, 8, 2)),
                        List.of(new AmbientAdditionsSettings(EnderscapeBiomeSounds.VOID_SKY_ISLANDS.additions(), 0.0006))
                ));

        voidBiomeEnvironmentAttributes(attributes);

        return createBiome(builder, new MobSpawnSettings.Builder().build(), attributes.build());
    }

    private static void voidBiomeEnvironmentAttributes(EnvironmentAttributeMap.Builder attributes) {
        attributes.set(AMBIENT_ENTITY_VOIDING_RATE, 1.0F);

        attributes.modify(AMBIENT_LIGHT_COLOR, ColorModifier.MULTIPLY_RGB, VOID_BIOMES_BLEND_COLOR);
        attributes.set(SKY_LIGHT_COLOR, 0x000000);

        attributes.modify(SKY_COLOR, ColorModifier.MULTIPLY_RGB, VOID_BIOMES_BLEND_COLOR);
        attributes.modify(FOG_COLOR, ColorModifier.MULTIPLY_RGB, VOID_BIOMES_BLEND_COLOR);

        attributes.modify(NEBULA_COLOR, ColorModifier.MULTIPLY_RGB, VOID_BIOMES_BLEND_COLOR);
        attributes.modify(STAR_COLOR, ColorModifier.MULTIPLY_RGB, VOID_BIOMES_BLEND_COLOR);

        attributes.modify(NEBULA_BRIGHTNESS, FloatModifier.MULTIPLY, 0.9F);
        attributes.modify(STAR_BRIGHTNESS, FloatModifier.MULTIPLY, 1.2F);

        attributes.set(FOG_START_DISTANCE, -40.0F);
        attributes.set(FOG_END_DENSITY, 0.3F);

        attributes.set(BACKGROUND_MUSIC, BackgroundMusic.EMPTY);
        attributes.set(MUSIC_VOLUME, 0.0F);

        attributes.set(AMBIENT_PARTICLES, AmbientParticle.of(EnderscapeParticles.VOID_STARS, 0.006F));
    }

    public static Biome createBiome(BiomeGenerationSettings.Builder generation, MobSpawnSettings mobSpawns, EnvironmentAttributeMap environmentAttributes, BiomeSpecialEffects specialEffects) {
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.5F)
                .downfall(0.5F)
                .putAttributes(environmentAttributes)
                .specialEffects(specialEffects)
                .mobSpawnSettings(mobSpawns)
                .generationSettings(generation.build())
                .build();
    }

    public static Biome createBiome(BiomeGenerationSettings.Builder generation, MobSpawnSettings mobSpawns, EnvironmentAttributeMap environmentAttributes) {
        return createBiome(generation, mobSpawns, environmentAttributes, specialEffects(DEFAULT_WATER_COLOR, DEFAULT_FOLIAGE_COLOR, DEFAULT_GRASS_COLOR));
    }

    private static BiomeSpecialEffects specialEffects(int waterColor, int foliageColor, int grassColor) {
        return new BiomeSpecialEffects.Builder()
                .waterColor(waterColor)
                .foliageColorOverride(foliageColor)
                .grassColorOverride(grassColor)
                .build();
    }
}
