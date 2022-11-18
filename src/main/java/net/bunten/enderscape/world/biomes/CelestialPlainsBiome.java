package net.bunten.enderscape.world.biomes;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;

import net.bunten.enderscape.registry.EnderscapeEntities;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.world.EnderscapeRuleSources;
import net.bunten.enderscape.world.placed.CelestialPlainsFeatures;
import net.bunten.enderscape.world.placed.GeneralEndFeatures;
import net.minecraft.data.worldgen.placement.EndPlacements;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.levelgen.GenerationStep;

public class CelestialPlainsBiome extends EnderscapeBiome {

    public CelestialPlainsBiome() {
        super("celestial_plains");

        nebulaColor = 0x896247;
        nebulaAlpha *= 1.25F;
    }

    @Override
    public BCLBiome getBCLBiome() {
        BCLBiomeBuilder builder = createBuilder();

        builder.endLandBiome();
        builder.genChance(0.3F);

        builder.temperature(0.7F);
        builder.precipitation(Precipitation.NONE);
        
        builder.music(createMusic());
        builder.loop(createLoop());
        builder.additions(createAdditions(), 0.003F);
        builder.mood(createMood(), 6000, 8, 2);
        builder.particles(EnderscapeParticles.CELESTIAL_SPORES, 0.015F);
        
        builder.waterColor(DEFAULT_WATER_COLOR);
        builder.waterFogColor(DEFAULT_WATER_FOG_COLOR);

        builder.skyColor(DEFAULT_SKY_COLOR);
        builder.fogColor(0x160D0D);
        builder.fogDensity(1.25F);

        builder.surface(EnderscapeRuleSources.CELESTIAL_SURFACE);

        builder.spawn(EnderscapeEntities.DRIFTER, 4, 2, 5);
        builder.spawn(EnderscapeEntities.DRIFTLET, 2, 2, 5);
        builder.spawn(EntityType.ENDERMAN, 10, 4, 8);
        
        builder.feature(GenerationStep.Decoration.SURFACE_STRUCTURES, EndPlacements.END_GATEWAY_RETURN);
        builder.feature(GenerationStep.Decoration.VEGETAL_DECORATION, EndPlacements.CHORUS_PLANT);
        
        builder.feature(CelestialPlainsFeatures.LARGE_FUNGUS);

        builder.feature(CelestialPlainsFeatures.GROWTH);
        builder.feature(CelestialPlainsFeatures.FUNGUS);
        builder.feature(CelestialPlainsFeatures.BULB_FLOWER);
        builder.feature(CelestialPlainsFeatures.MURUSHROOMS);

        builder.feature(GeneralEndFeatures.VERADITE);
        builder.feature(GeneralEndFeatures.SCATTERED_VERADITE);

        builder.feature(GeneralEndFeatures.SHADOW_QUARTZ_ORE);
        builder.feature(GeneralEndFeatures.SCATTERED_SHADOW_QUARTZ_ORE);

        builder.feature(GeneralEndFeatures.NEBULITE_ORE);
        builder.feature(GeneralEndFeatures.VOID_NEBULITE_ORE);

        return builder.build();
    }
}