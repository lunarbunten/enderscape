package net.bunten.enderscape.world.biomes;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;

import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.world.EnderscapeBiomeTypes;
import net.bunten.enderscape.world.EnderscapeRuleSources;
import net.bunten.enderscape.world.placed.CorruptDepthsFeatures;
import net.bunten.enderscape.world.placed.GeneralEndFeatures;
import net.minecraft.world.level.biome.Biome.Precipitation;

public class CorruptDepthsBiome extends EnderscapeBiome {

    public CorruptDepthsBiome() {
        super("corrupt_depths");

        nebulaColor = 0x414A77;
        nebulaAlpha *= 1.5F;
        starColor = 0xFFBFEC;
    }

    @Override
    public BCLBiome getBCLBiome() {
        BCLBiomeBuilder builder = createBuilder();

        builder.intendedType(EnderscapeBiomeTypes.SUBSURFACE);
        builder.genChance(0.25F);

        builder.temperature(0.7F);
        builder.precipitation(Precipitation.NONE);
        
        builder.music(createMusic());
        builder.loop(createLoop());
        builder.additions(createAdditions(), 0.003F);
        builder.mood(createMood(), 6000, 8, 2);
        builder.particles(EnderscapeParticles.AMBIENT_STARS, 0.001F);
        
        builder.waterColor(DEFAULT_WATER_COLOR);
        builder.waterFogColor(DEFAULT_WATER_FOG_COLOR);

        builder.skyColor(0x15101E);
        builder.fogColor(0x0C0C14);
        builder.fogDensity(1.5F);

        builder.surface(EnderscapeRuleSources.CORRUPT_SURFACE);

        builder.feature(CorruptDepthsFeatures.LARGE_MURUSHROOM);

        builder.feature(CorruptDepthsFeatures.BLINKLIGHT_VINES);
        builder.feature(CorruptDepthsFeatures.CORRUPT_GROWTH);
        builder.feature(CorruptDepthsFeatures.TALL_CORRUPT_GROWTH);

        builder.feature(GeneralEndFeatures.VERADITE);
        builder.feature(GeneralEndFeatures.SCATTERED_VERADITE);

        builder.feature(GeneralEndFeatures.SHADOW_QUARTZ_ORE);
        builder.feature(GeneralEndFeatures.SCATTERED_SHADOW_QUARTZ_ORE);

        builder.feature(GeneralEndFeatures.NEBULITE_ORE);
        builder.feature(CorruptDepthsFeatures.VOID_NEBULITE_ORE);

        return builder.build();
    }
}