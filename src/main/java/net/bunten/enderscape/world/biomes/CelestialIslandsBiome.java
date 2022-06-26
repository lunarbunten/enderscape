package net.bunten.enderscape.world.biomes;

import net.bunten.enderscape.registry.EnderscapeMusic;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.world.EnderscapeFeatures;
import net.bunten.enderscape.world.EnderscapeSurfaces;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome.Precipitation;
import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.bclib.world.biomes.BCLBiome;

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

        builder.feature(EnderscapeFeatures.BCL_CELESTIAL_ISLAND);

        builder.feature(EnderscapeFeatures.BCL_CELESTIAL_GROWTH);
        builder.feature(EnderscapeFeatures.BCL_CELESTIAL_VEGETATION);
        builder.feature(EnderscapeFeatures.BCL_UNCOMMON_MURUSHROOMS);

        builder.feature(EnderscapeFeatures.BCL_SHADOW_QUARTZ_ORE);
        builder.feature(EnderscapeFeatures.BCL_SCATTERED_SHADOW_QUARTZ_ORE);

        builder.feature(EnderscapeFeatures.BCL_NEBULITE_ORE);
        builder.feature(EnderscapeFeatures.BCL_VOID_NEBULITE_ORE);

        return builder.build();
    }
}