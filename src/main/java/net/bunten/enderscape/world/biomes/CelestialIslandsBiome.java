package net.bunten.enderscape.world.biomes;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;

import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.world.EnderscapeRuleSources;
import net.bunten.enderscape.world.placed.CelestialIslandsFeatures;
import net.bunten.enderscape.world.placed.CelestialPlainsFeatures;
import net.bunten.enderscape.world.placed.GeneralEndFeatures;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome.Precipitation;

public class CelestialIslandsBiome extends EnderscapeBiome {

    public CelestialIslandsBiome() {
        super("celestial_islands");

        nebulaColor = 0x896247;
        nebulaAlpha *= 1.12F;
    }

    @Override
    public BCLBiome getBCLBiome() {
        BCLBiomeBuilder builder = createBuilder();

        builder.endVoidBiome();
        builder.genChance(0.3F);

        builder.temperature(0.7F);
        builder.precipitation(Precipitation.NONE);

        builder.music(createMusic());
        builder.loop(createLoop());
        builder.additions(createAdditions(), 0.003F);
        builder.mood(createMood(), 6000, 8, 2);
        builder.particles(EnderscapeParticles.CELESTIAL_SPORES, 0.001F);
        
        builder.waterColor(DEFAULT_WATER_COLOR);
        builder.waterFogColor(DEFAULT_WATER_FOG_COLOR);

        builder.skyColor(DEFAULT_SKY_COLOR);
        builder.fogColor(0x160D0D);
        builder.fogDensity(1.25F);

        builder.surface(EnderscapeRuleSources.CELESTIAL_SURFACE);

        builder.spawn(EntityType.ENDERMAN, 30, 4, 8);

        builder.feature(CelestialIslandsFeatures.ISLAND);

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