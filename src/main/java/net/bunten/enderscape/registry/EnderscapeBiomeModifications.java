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
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.attribute.*;
import net.minecraft.world.attribute.modifier.FloatModifier;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static net.bunten.enderscape.registry.EnderscapeEnvironmentAttributes.FOG_END_DENSITY;
import static net.minecraft.world.attribute.EnvironmentAttributes.*;

public class EnderscapeBiomeModifications {

    private static final EnderscapeConfig CONFIG = EnderscapeConfig.getInstance();

    private static final Predicate<BiomeSelectionContext> IS_END = (selection) -> selection.getBiomeRegistryEntry().is(BiomeTags.IS_END);
    private static final BackgroundMusic DEFAULT_END_BGM = new BackgroundMusic(Musics.END);

    static {
        registerAmbienceModifications();
        registerFeatureModifications();
    }

    private static void registerAmbienceModifications() {

        conditionalAmbienceModification(
                CONFIG.flashUpdatedVisuals,
                "update_end_biome_sky_light_color_when_default",
                selection -> !selection.getBiome().getAttributes().contains(SKY_LIGHT_COLOR),
                (selection, modification) -> modification.getAttributes().set(SKY_LIGHT_COLOR, 0x654B82)
        );

        conditionalAmbienceModification(
                CONFIG.ambienceUpdateFogDensity,
                "update_end_biome_fog_density_when_default",
                selection -> !selection.getBiome().getAttributes().contains(FOG_START_DISTANCE) && !selection.getBiome().getAttributes().contains(FOG_END_DISTANCE),
                (selection, modification) -> {
                    modification.getAttributes().set(FOG_START_DISTANCE, -20.0F);
                    modification.getAttributes().setModifier(FOG_END_DENSITY, FloatModifier.MULTIPLY, 0.9F);
                }
        );

        conditionalAmbienceModification(
                CONFIG.ambienceUpdateMusicPools,
                "update_end_biome_music_when_default",
                selection -> !selection.getBiome().getAttributes().contains(BACKGROUND_MUSIC) || selection.getBiome().getAttributes().get(BACKGROUND_MUSIC).argument().equals(DEFAULT_END_BGM),
                (selection, modification) -> modification.getAttributes().set(BACKGROUND_MUSIC, new BackgroundMusic(EnderscapeBiomeSounds.DEFAULT_END.music()))
        );

        conditionalAmbienceModification(
                CONFIG.ambienceUpdateGrassColors,
                "update_end_biome_grass_color_when_default",
                selection -> selection.getBiome().getSpecialEffects().grassColorOverride().isEmpty(),
                (selection, modification) -> modification.getEffects().setGrassColor(EnderscapeBiomes.DEFAULT_GRASS_COLOR)
        );

        conditionalAmbienceModification(
                CONFIG.ambienceUpdateFoliageColors,
                "update_end_biome_foliage_color_when_default",
                selection -> selection.getBiome().getSpecialEffects().foliageColorOverride().isEmpty(),
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
                selection -> !selection.getBiome().getAttributes().contains(WATER_FOG_COLOR) || selection.getBiome().getAttributes().get(WATER_FOG_COLOR).argument().equals(329011),
                (selection, modification) -> modification.getEffects().setWaterFogColor(EnderscapeBiomes.DEFAULT_WATER_FOG_COLOR)
        );

        conditionalAmbienceModification(
                CONFIG.ambienceUpdateParticles,
                "update_end_biome_particles_when_missing",
                selection -> !selection.getBiome().getAttributes().contains(AMBIENT_PARTICLES),
                (selection, modification) -> modification.getAttributes().set(AMBIENT_PARTICLES, AmbientParticle.of(EnderscapeParticles.VOID_STARS, 0.003F))
        );

        conditionalAmbienceModification(
                CONFIG.ambienceUpdateSkyColors,
                "update_end_biome_sky_colors_when_default",
                selection -> !selection.getBiome().getAttributes().contains(SKY_COLOR),
                (selection, modification) -> modification.getAttributes().set(SKY_COLOR, EnderscapeBiomes.DEFAULT_SKY_COLOR)
        );

        conditionalAmbienceModification(
                CONFIG.ambienceUpdateFogColors,
                "update_end_biome_fog_colors_when_default",
                selection -> !selection.getBiome().getAttributes().contains(FOG_COLOR),
                (selection, modification) -> {
                    modification.getAttributes().set(FOG_COLOR, EnderscapeBiomes.DEFAULT_FOG_COLOR);
                    modification.getAttributes().set(CLOUD_COLOR, EnderscapeBiomes.DEFAULT_CLOUD_COLOR);
                }
        );

        conditionalAmbienceModification(
                CONFIG.ambienceUpdateLoopSounds,
                "update_end_biome_ambience_when_missing_or_replaceable",
                selection -> true,
                (selection, modification) -> {
                    Optional<Holder<SoundEvent>> loop = Optional.of(EnderscapeBiomeSounds.DEFAULT_END.loop());
                    Optional<AmbientMoodSettings> mood = Optional.of(new AmbientMoodSettings(EnderscapeBiomeSounds.DEFAULT_END.mood(), 6000, 8, 2));
                    List<AmbientAdditionsSettings> additions = List.of(new AmbientAdditionsSettings(EnderscapeBiomeSounds.DEFAULT_END.additions(), 0.00075));

                    if (selection.getBiome().getAttributes().contains(AMBIENT_SOUNDS)) {
                        AmbientSounds sounds = (AmbientSounds) selection.getBiome().getAttributes().get(AMBIENT_SOUNDS).argument();
                        modification.getAttributes().set(AMBIENT_SOUNDS, new AmbientSounds(
                                 sounds.loop().isEmpty() || sounds.loop().get().is(EnderscapeSoundEventTags.AMBIENCE_REPLACEABLE_BY_ENDERSCAPE) ?
                                        loop :
                                        sounds.loop(),
                                sounds.mood().isEmpty() || sounds.mood().get().soundEvent().is(EnderscapeSoundEventTags.AMBIENCE_REPLACEABLE_BY_ENDERSCAPE) ?
                                        mood :
                                        sounds.mood(),
                                sounds.additions().isEmpty() ?
                                        additions :
                                        sounds.additions()
                        ));
                    } else {
                        modification.getAttributes().set(AMBIENT_SOUNDS, new AmbientSounds(
                                loop,
                                mood,
                                additions
                        ));
                    }
                }
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
