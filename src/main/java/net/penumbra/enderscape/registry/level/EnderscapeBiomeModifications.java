package net.penumbra.enderscape.registry.level;

import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.data.worldgen.placement.EndPlacements;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.registry.entity.EnderscapeEntities;
import net.penumbra.enderscape.registry.tag.EnderscapeBiomeTags;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static net.minecraft.world.attribute.EnvironmentAttributes.WATER_FOG_COLOR;

public class EnderscapeBiomeModifications {

    private static final EnderscapeConfig CONFIG = EnderscapeConfig.getInstance();

    private static final Predicate<BiomeSelectionContext> IS_END = (selection) -> selection.getBiomeHolder().is(BiomeTags.IS_END);

    static {
        registerAmbienceModifications();
        registerFeatureModifications();
    }

    private static void registerAmbienceModifications() {
        conditionalAmbienceModification(
                CONFIG.ambienceUpdateGrassColors,
                "update_end_biome_grass_color_when_default",
                selection -> selection.getBiome().getSpecialEffects().grassColorOverride().isEmpty(),
                (selection, modification) -> modification.getEffects().setGrassColorOverride(EnderscapeBiomes.DEFAULT_GRASS_COLOR)
        );

        conditionalAmbienceModification(
                CONFIG.ambienceUpdateFoliageColors,
                "update_end_biome_foliage_color_when_default",
                selection -> selection.getBiome().getSpecialEffects().foliageColorOverride().isEmpty(),
                (selection, modification) -> modification.getEffects().setFoliageColorOverride(EnderscapeBiomes.DEFAULT_FOLIAGE_COLOR)
        );

        conditionalAmbienceModification(
                CONFIG.ambienceUpdateWaterColors,
                "update_end_biome_water_color_when_default",
                selection -> selection.getBiome().getWaterColor() == 4159204,
                (selection, modification) -> modification.getEffects().setWaterColor(EnderscapeBiomes.DEFAULT_WATER_COLOR)
        );

        conditionalAmbienceModification(
                CONFIG.ambienceUpdateWaterFogColors,
                "update_end_biome_water_fog_color_when_default",
                selection -> !selection.getBiome().getAttributes().contains(WATER_FOG_COLOR) || selection.getBiome().getAttributes().get(WATER_FOG_COLOR).argument().equals(329011),
                (selection, modification) -> modification.getEffects().setWaterFogColor(EnderscapeBiomes.DEFAULT_WATER_FOG_COLOR)
        );
    }

    private static void registerFeatureModifications() {

        BiomeModifications.create(Enderscape.id("add_global_features")).add(
                ModificationPhase.ADDITIONS,
                IS_END.and(selection -> !selection.getBiomeHolder().is(EnderscapeBiomeTags.EXCLUDED_FROM_GLOBAL_FEATURE_ADDITIONS)),
                (selection, modification) -> {
                    BiomeModificationContext.GenerationSettingsContext generation = modification.getGenerationSettings();

                    generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, EnderscapePlacedFeatures.VOID_SHALE);
                    generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, EnderscapePlacedFeatures.VOID_SHALE_BLOBS);
                    generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, EnderscapePlacedFeatures.VERADITE);
                    generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, EnderscapePlacedFeatures.MIRESTONE_BLOBS);
                    generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, EnderscapePlacedFeatures.SHADOLINE_ORE);
                    generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, EnderscapePlacedFeatures.SCATTERED_SHADOLINE_ORE);
                    generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, EnderscapePlacedFeatures.NEBULITE_ORE);
                    generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, EnderscapePlacedFeatures.CEILING_NEBULITE_ORE);
                }
        );

        BiomeModifications.create(Enderscape.id("add_barrens_entities")).add(
                ModificationPhase.ADDITIONS,
                IS_END.and(selection -> selection.getBiomeHolder().is(EnderscapeBiomeTags.HAS_BARRENS_ADDITIONS)),
                (selection, modification) -> {
                    modification.getMobSpawnSettings().addSpawn(
                            MobCategory.MONSTER,
                            new MobSpawnSettings.SpawnerData(
                                    EnderscapeEntities.RUBBLEMITE,
                                    2,
                                    3
                            ),
                            EnderscapeBiomes.RUBBLEMITE_DEFAULT_WEIGHT
                    );

                    modification.getMobSpawnSettings().addMobCharge(
                            EnderscapeEntities.RUBBLEMITE,
                            0.2, 0.6
                    );
                }
        );

        replaceChorusPlants();
        replaceSmallEndIslands();

        BiomeModifications.create(Enderscape.id("remove_minecraft_end_gateways")).add(
                ModificationPhase.REMOVALS,
                IS_END.and(selection -> selection.hasPlacedFeature(EndPlacements.END_GATEWAY_RETURN)),
                (selection, modification) -> modification.getGenerationSettings().removeFeature(EndPlacements.END_GATEWAY_RETURN)
        );
    }

    private static void replaceChorusPlants() {
        BiomeModifications.create(Enderscape.id("add_enderscape_chorus_plants")).add(
                ModificationPhase.ADDITIONS,
                IS_END.and(selection -> selection.hasPlacedFeature(EndPlacements.CHORUS_PLANT) || selection.getBiomeKey() == Biomes.END_MIDLANDS),
                (selection, modification) -> modification.getGenerationSettings().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, EnderscapePlacedFeatures.CHORUS_PLANTS)
        );

        BiomeModifications.create(Enderscape.id("remove_minecraft_chorus_plants")).add(
                ModificationPhase.REMOVALS,
                IS_END.and(selection -> selection.hasPlacedFeature(EndPlacements.CHORUS_PLANT)),
                (selection, modification) -> modification.getGenerationSettings().removeFeature(EndPlacements.CHORUS_PLANT)
        );
    }

    private static void replaceSmallEndIslands() {
        BiomeModifications.create(Enderscape.id("add_enderscape_islands")).add(
                ModificationPhase.ADDITIONS,
                IS_END.and(selection -> selection.hasPlacedFeature(EndPlacements.END_ISLAND_DECORATED)),
                (selection, modification) -> modification.getGenerationSettings().addFeature(GenerationStep.Decoration.RAW_GENERATION, EnderscapePlacedFeatures.SMALL_ISLANDS)
        );

        BiomeModifications.create(Enderscape.id("remove_small_end_islands")).add(
                ModificationPhase.REMOVALS,
                IS_END.and(selection -> selection.hasPlacedFeature(EndPlacements.END_ISLAND_DECORATED)),
                (selection, modification) -> modification.getGenerationSettings().removeFeature(EndPlacements.END_ISLAND_DECORATED)
        );
    }

    private static void conditionalAmbienceModification(boolean condition, String name, Predicate<BiomeSelectionContext> predicate, BiConsumer<BiomeSelectionContext, BiomeModificationContext> consumer) {
        if (!condition) return;
        BiomeModifications.create(Enderscape.id(name)).add(ModificationPhase.REPLACEMENTS, IS_END.and(predicate.or(selection -> selection.getBiomeHolder().is(EnderscapeBiomeTags.OVERRIDES_DEFAULT_AMBIENCE))), consumer);
    }
}
