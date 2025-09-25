package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.registry.tag.EnderscapeBiomeTags;
import net.bunten.enderscape.registry.tag.EnderscapeSoundEventTags;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.EndPlacements;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class EnderscapeBiomeModifications {

    private static final EnderscapeConfig CONFIG = EnderscapeConfig.getInstance();

    private static final Predicate<BiomeSelectionContext> IS_END = (selection) -> selection.getBiomeRegistryEntry().is(BiomeTags.IS_END);
    private static final WeightedList<Music> DEFAULT_END_MUSIC_POOL = WeightedList.of(Musics.END);

    static {
        registerAmbienceModifications();
        registerFeatureModifications();
    }

    private static void registerAmbienceModifications() {
        conditionalAmbienceModification(
                CONFIG.ambienceUpdateMusicPools,
                "update_end_biome_music_when_default",
                selection -> selection.getBiome().getBackgroundMusic().isPresent() && selection.getBiome().getBackgroundMusic().get().equals(DEFAULT_END_MUSIC_POOL),
                (selection, modification) -> modification.getEffects().setMusic(new Music(EnderscapeBiomeSounds.DEFAULT_END.music(), 12000, 24000, false))
        );

        conditionalAmbienceModification(
                CONFIG.ambienceUpdateGrassColors,
                "update_end_biome_grass_color_when_default",
                selection -> selection.getBiome().getSpecialEffects().getGrassColorOverride().isEmpty(),
                (selection, modification) -> modification.getEffects().setGrassColor(EnderscapeBiomes.DEFAULT_GRASS_COLOR)
        );

        conditionalAmbienceModification(
                CONFIG.ambienceUpdateFoliageColors,
                "update_end_biome_foliage_color_when_default",
                selection -> selection.getBiome().getSpecialEffects().getFoliageColorOverride().isEmpty(),
                (selection, modification) -> modification.getEffects().setFoliageColor(EnderscapeBiomes.DEFAULT_FOLIAGE_COLOR)
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
                selection -> selection.getBiome().getWaterFogColor() == 329011,
                (selection, modification) -> modification.getEffects().setWaterFogColor(EnderscapeBiomes.DEFAULT_WATER_FOG_COLOR)
        );

        conditionalAmbienceModification(
                CONFIG.ambienceUpdateParticles,
                "update_end_biome_particles_when_missing",
                selection -> selection.getBiome().getAmbientParticle().isEmpty(),
                (selection, modification) -> modification.getEffects().setParticleConfig(new AmbientParticleSettings(EnderscapeParticles.VOID_STARS, 0.003F))
        );

        conditionalAmbienceModification(
                CONFIG.ambienceUpdateSkyColors,
                "update_end_biome_sky_colors_when_default",
                selection -> selection.getBiome().getSkyColor() == 0,
                (selection, modification) -> modification.getEffects().setSkyColor(EnderscapeBiomes.DEFAULT_SKY_COLOR)
        );

        conditionalAmbienceModification(
                CONFIG.ambienceUpdateFogColors,
                "update_end_biome_fog_colors_when_default",
                selection -> selection.getBiome().getSkyColor() == 10518688,
                (selection, modification) -> modification.getEffects().setFogColor(EnderscapeBiomes.DEFAULT_FOG_COLOR)
        );

        conditionalAmbienceModification(
                CONFIG.ambienceUpdateLoopSounds,
                "update_end_biome_ambient_loop_when_missing_or_replaceable",
                selection -> {
                    Optional<Holder<SoundEvent>> loop = selection.getBiome().getAmbientLoop();
                    return loop.isEmpty() || loop.get().is(EnderscapeSoundEventTags.AMBIENCE_REPLACEABLE_BY_ENDERSCAPE);
                },
                (selection, modification) -> modification.getEffects().setAmbientSound(EnderscapeBiomeSounds.DEFAULT_END.loop())
        );

        conditionalAmbienceModification(
                CONFIG.ambienceUpdateAdditionSounds,
                "update_end_biome_ambient_additions_when_missing_or_replaceable",
                selection -> {
                    Optional<AmbientAdditionsSettings> additions = selection.getBiome().getAmbientAdditions();
                    return additions.isEmpty() || additions.get().getSoundEvent().is(EnderscapeSoundEventTags.AMBIENCE_REPLACEABLE_BY_ENDERSCAPE);
                },
                (selection, modification) -> modification.getEffects().setAdditionsSound(new AmbientAdditionsSettings(EnderscapeBiomeSounds.DEFAULT_END.additions(), 0.00075))
        );

        conditionalAmbienceModification(
                CONFIG.ambienceUpdateMoodSounds,
                "update_end_biome_ambience_mood_when_missing_or_replaceable",
                selection -> {
                    Optional<AmbientMoodSettings> additions = selection.getBiome().getAmbientMood();
                    return additions.isEmpty() || additions.get().getSoundEvent().is(EnderscapeSoundEventTags.AMBIENCE_REPLACEABLE_BY_ENDERSCAPE);
                },
                (selection, modification) -> modification.getEffects().setMoodSound(new AmbientMoodSettings(EnderscapeBiomeSounds.DEFAULT_END.mood(), 6000, 8, 2))
        );
    }

    private static void registerFeatureModifications() {

        BiomeModifications.create(Enderscape.id("add_global_features")).add(
                ModificationPhase.ADDITIONS,
                IS_END.and(selection -> !selection.getBiomeRegistryEntry().is(EnderscapeBiomeTags.EXCLUDED_FROM_GLOBAL_FEATURE_ADDITIONS)),
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

        BiomeModifications.create(Enderscape.id("add_new_barrens_content")).add(
                ModificationPhase.ADDITIONS,
                IS_END.and(selection -> selection.getBiomeRegistryEntry().is(EnderscapeBiomeTags.HAS_BARRENS_ADDITIONS)),
                (selection, modification) -> {
                    modification.getSpawnSettings().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EnderscapeEntities.RUBBLEMITE, 2, 3), 2);

                    BiomeModificationContext.GenerationSettingsContext generation = modification.getGenerationSettings();

                    generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, EnderscapePlacedFeatures.CHORUS_SPROUTS);
                    generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, EnderscapePlacedFeatures.DRY_END_GROWTH);
                    generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, EnderscapePlacedFeatures.MURUBLIGHT_BRACKET);
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
        BiomeModifications.create(Enderscape.id(name)).add(ModificationPhase.REPLACEMENTS, IS_END.and(predicate.or(selection -> selection.getBiomeRegistryEntry().is(EnderscapeBiomeTags.OVERRIDES_DEFAULT_AMBIENCE))), consumer);
    }
}
