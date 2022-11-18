package net.bunten.enderscape.world.biomes;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;

import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.world.EnderscapeBiomeTypes;
import net.bunten.enderscape.world.EnderscapeRuleSources;
import net.minecraft.world.level.biome.Biome.Precipitation;

public class BarrenSkiesBiome extends EnderscapeBiome {

    public BarrenSkiesBiome() {
        super("barren_skies");

        nebulaAlpha *= 1.75F;
        starAlpha *= 1.75F;
    }

    @Override
    public BCLBiome getBCLBiome() {
        BCLBiomeBuilder builder = createBuilder();

        builder.intendedType(EnderscapeBiomeTypes.SKY);
        builder.genChance(0.6F);

        builder.temperature(0.7F);
        builder.precipitation(Precipitation.NONE);
        
        builder.music(createMusic());
        builder.loop(createLoop());
        builder.additions(createAdditions(), 0.003F);
        builder.mood(createMood(), 6000, 8, 2);
        builder.particles(EnderscapeParticles.AMBIENT_STARS, 0.002F);
        
        builder.waterColor(DEFAULT_WATER_COLOR);
        builder.waterFogColor(DEFAULT_WATER_FOG_COLOR);

        builder.skyColor(DEFAULT_SKY_COLOR);
        builder.fogColor(DEFAULT_FOG_COLOR);
        builder.fogDensity(DEFAULT_FOG_DENSITY);

        builder.surface(EnderscapeRuleSources.DEFAULT_END_SURFACE);

        return builder.build();
    }
}