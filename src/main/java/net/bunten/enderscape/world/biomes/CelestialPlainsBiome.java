package net.bunten.enderscape.world.biomes;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;

import net.bunten.enderscape.registry.EnderscapeEntities;
import net.bunten.enderscape.registry.EnderscapeMusic;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.world.EnderscapeFeatures;
import net.bunten.enderscape.world.EnderscapeSurfaces;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome.Precipitation;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.EndPlacedFeatures;

public class CelestialPlainsBiome implements EnderscapeBiome, CelestialBiome {

    @Override
    public String getName() {
        return "celestial_plains";
    }

    public static BCLBiome register() {
        return new CelestialPlainsBiome().getBCLBiome();
    }

    @Override
    public BCLBiome getBCLBiome() {
        BCLBiomeBuilder builder = BCLBiomeBuilder.start(getIdentifier());

        builder.temperature(0.7F);
        builder.precipitation(Precipitation.NONE);
        
        builder.music(EnderscapeMusic.CELESTIAL_PLAINS);
        builder.loop(EnderscapeSounds.AMBIENT_CELESTIAL_PLAINS_LOOP);
        builder.additions(EnderscapeSounds.AMBIENT_CELESTIAL_PLAINS_ADDITIONS, 0.003F);
        builder.mood(EnderscapeSounds.AMBIENT_CELESTIAL_PLAINS_MOOD, 6000, 8, 2);
        builder.particles(EnderscapeParticles.CELESTIAL_SPORES, 0.015F);
        
        builder.waterColor(0x3F76E4);
        builder.waterFogColor(0x50533);

        builder.skyColor(0);
        builder.fogColor(getFogColor());
        builder.fogDensity(getFogDensity());

        builder.surface(EnderscapeSurfaces.CELESTIAL_SURFACE);

        builder.spawn(EnderscapeEntities.DRIFTER, 4, 2, 5);
        builder.spawn(EnderscapeEntities.DRIFTLET, 2, 2, 5);
        builder.spawn(EntityType.ENDERMAN, 10, 4, 8);
        
        builder.feature(GenerationStep.Feature.VEGETAL_DECORATION, EndPlacedFeatures.CHORUS_PLANT);
        
        builder.feature(EnderscapeFeatures.BCL_LARGE_CELESTIAL_FUNGUS);
        builder.feature(EnderscapeFeatures.BCL_CELESTIAL_VEGETATION);

        builder.feature(EnderscapeFeatures.BCL_CELESTIAL_GROWTH);
        builder.feature(EnderscapeFeatures.BCL_UNCOMMON_MURUSHROOMS);

        builder.feature(EnderscapeFeatures.BCL_SHADOW_QUARTZ_ORE);
        builder.feature(EnderscapeFeatures.BCL_SCATTERED_SHADOW_QUARTZ_ORE);

        builder.feature(EnderscapeFeatures.BCL_NEBULITE_ORE);
        builder.feature(EnderscapeFeatures.BCL_VOID_NEBULITE_ORE);

        return builder.build();
    }
}