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

public class MagniaCragsBiome {

    public static Biome create(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder().addSpawn(MobCategory.MONSTER, 4, new MobSpawnSettings.SpawnerData(EnderscapeEntities.RUBBLEMITE, 2, 3));

        BiomeDefaultFeatures.endSpawns(spawns);

        BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings.Builder(features, carvers)
                .addFeature(UNDERGROUND_STRUCTURES, EnderscapePlacedFeatures.MAGNIA_ARCH)

                .addFeature(SURFACE_STRUCTURES, EndPlacements.END_GATEWAY_RETURN)
                .addFeature(SURFACE_STRUCTURES, EnderscapePlacedFeatures.MAGNIA_TOWER)

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
                .addFeature(VEGETAL_DECORATION, EnderscapePlacedFeatures.MURUBLIGHT_SHELF);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.5F)
                .downfall(0.5F)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(0x151616)
                        .fogColor(0x0F1010)
                        .waterColor(0x89919c)
                        .waterFogColor(0x5e656e)
                        .foliageColorOverride(0x919c8c)
                        .grassColorOverride(0x919c8c)
                        .ambientParticle(new AmbientParticleSettings(EnderscapeParticles.VOID_STARS, 0.003F))
                        .ambientLoopSound(EnderscapeBiomeSounds.MAGNIA_CRAGS.loop())
                        .ambientAdditionsSound(new AmbientAdditionsSettings(EnderscapeBiomeSounds.MAGNIA_CRAGS.additions(), 0.0015))
                        .ambientMoodSound(new AmbientMoodSettings(EnderscapeBiomeSounds.MAGNIA_CRAGS.mood(), 6000, 8, 2))
                        .backgroundMusic(Musics.createGameMusic(EnderscapeBiomeSounds.MAGNIA_CRAGS.music()))
                        .build()
                )

                .mobSpawnSettings(spawns.build())
                .generationSettings(builder.build())
                .build();
    }
}
