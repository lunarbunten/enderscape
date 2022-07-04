package net.bunten.enderscape.world.biomes;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;

import net.bunten.enderscape.registry.EnderscapeMusic;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.world.EnderscapeBCLFeatures;
import net.bunten.enderscape.world.EnderscapeSurfaces;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome.Precipitation;

public class CelestialIslandsBiome implements EnderscapeBiome, CelestialBiome {

    @Override
    public String getName() {
        return "celestial_islands";
    }

    public static BCLBiome register() {
        return new CelestialIslandsBiome().getBCLBiome();
    }

    @Override
    public BCLBiome getBCLBiome() {
        BCLBiomeBuilder builder = BCLBiomeBuilder.start(getIdentifier());

        builder.temperature(0.7F);
        builder.precipitation(Precipitation.NONE);

        builder.music(EnderscapeMusic.CELESTIAL_ISLANDS);
        builder.loop(EnderscapeSounds.AMBIENT_CELESTIAL_ISLANDS_LOOP);
        builder.additions(EnderscapeSounds.AMBIENT_CELESTIAL_ISLANDS_ADDITIONS, 0.003F);
        builder.mood(EnderscapeSounds.AMBIENT_CELESTIAL_ISLANDS_MOOD, 6000, 8, 2);
        builder.particles(EnderscapeParticles.CELESTIAL_SPORES, 0.001F);
        
        builder.waterColor(0x3F76E4);
        builder.waterFogColor(0x50533);

        builder.skyColor(0);
        builder.fogColor(getFogColor());
        builder.fogDensity(getFogDensity());

        builder.surface(EnderscapeSurfaces.CELESTIAL_SURFACE);

        builder.spawn(EntityType.ENDERMAN, 30, 4, 8);

        builder.feature(EnderscapeBCLFeatures.CELESTIAL_ISLAND);

        builder.feature(EnderscapeBCLFeatures.CELESTIAL_GROWTH);
        builder.feature(EnderscapeBCLFeatures.CELESTIAL_VEGETATION);
        builder.feature(EnderscapeBCLFeatures.UNCOMMON_MURUSHROOMS);

        builder.feature(EnderscapeBCLFeatures.SHADOW_QUARTZ_ORE);
        builder.feature(EnderscapeBCLFeatures.SCATTERED_SHADOW_QUARTZ_ORE);

        builder.feature(EnderscapeBCLFeatures.NEBULITE_ORE);
        builder.feature(EnderscapeBCLFeatures.VOID_NEBULITE_ORE);

        return builder.build();
    }
}