package net.enderscape.world;

import net.enderscape.registry.EndEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;

public class EndBiomeFeatures {

    public static void createDefaultStructures(GenerationSettings.Builder builder, boolean hasEndCity) {
        builder.feature(Feature.SURFACE_STRUCTURES, ConfiguredFeatures.END_GATEWAY);
        if (hasEndCity) {
            builder.structureFeature(ConfiguredStructureFeatures.END_CITY);
        }
    }

    public static void addCenterIslandFeatures(GenerationSettings.Builder builder, SpawnSettings.Builder settings) {
        builder.feature(Feature.SURFACE_STRUCTURES, ConfiguredFeatures.END_SPIKE);
        DefaultBiomeFeatures.addEndMobs(settings);
    }

    public static void addEndFeatures(GenerationSettings.Builder builder, SpawnSettings.Builder settings) {
        builder.feature(Feature.UNDERGROUND_ORES, EndConfiguredFeatures.NEBULITE_ORE);
        builder.feature(Feature.UNDERGROUND_ORES, EndConfiguredFeatures.VOID_NEBULITE_ORE);
        builder.feature(Feature.UNDERGROUND_ORES, EndConfiguredFeatures.SCATTERED_SHADOW_QUARTZ_ORE);

        builder.feature(Feature.VEGETAL_DECORATION, EndConfiguredFeatures.SPARSE_CHORUS_TREE);
        builder.feature(Feature.VEGETAL_DECORATION, EndConfiguredFeatures.VERY_UNCOMMON_CELESTIAL_GROWTH);

        settings.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EndEntities.RUBBLEMITE, 1, 1, 2));
        DefaultBiomeFeatures.addEndMobs(settings);
    }

    public static void addCelestialFeatures(GenerationSettings.Builder builder, SpawnSettings.Builder settings) {
        builder.feature(Feature.UNDERGROUND_ORES, EndConfiguredFeatures.NEBULITE_ORE);
        builder.feature(Feature.UNDERGROUND_ORES, EndConfiguredFeatures.VOID_NEBULITE_ORE);
        builder.feature(Feature.UNDERGROUND_ORES, EndConfiguredFeatures.SCATTERED_SHADOW_QUARTZ_ORE);

        builder.feature(Feature.VEGETAL_DECORATION, EndConfiguredFeatures.CELESTIAL_FUNGUS);
        builder.feature(Feature.VEGETAL_DECORATION, EndConfiguredFeatures.CHORUS_TREE);

        builder.feature(Feature.VEGETAL_DECORATION, EndConfiguredFeatures.CELESTIAL_GROWTH);
        builder.feature(Feature.VEGETAL_DECORATION, EndConfiguredFeatures.CELESTIAL_FUNGI_PATCH);

        builder.feature(Feature.VEGETAL_DECORATION, EndConfiguredFeatures.BULB_FLOWER_PATCH);

        builder.feature(Feature.UNDERGROUND_DECORATION, EndConfiguredFeatures.MURUSHROOM);

        settings.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EndEntities.DRIFTER, 2, 1, 3));
        settings.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EndEntities.DRIFTLET, 1, 1, 2));
        settings.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.ENDERMAN, 50, 4, 8));
    }

    public static void addIslandsFeatures(GenerationSettings.Builder builder, SpawnSettings.Builder settings) {
        builder.feature(Feature.RAW_GENERATION, EndConfiguredFeatures.END_ISLAND);
        settings.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.ENDERMAN, 1, 4, 8));
    }

    public static void addCelestialIslandFeatures(GenerationSettings.Builder builder, SpawnSettings.Builder settings) {
        builder.feature(Feature.RAW_GENERATION, EndConfiguredFeatures.CELESTIAL_ISLAND);
        builder.feature(Feature.VEGETAL_DECORATION, EndConfiguredFeatures.CELESTIAL_GROWTH);
        settings.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.ENDERMAN, 1, 4, 8));
    }
}