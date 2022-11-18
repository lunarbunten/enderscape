package net.bunten.enderscape.world.biomes;

import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeBuilder;

import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.world.EnderscapeBiomeTypes;
import net.bunten.enderscape.world.EnderscapeRuleSources;
import net.bunten.enderscape.world.placed.BarrenEndFeatures;
import net.bunten.enderscape.world.placed.GeneralEndFeatures;
import net.minecraft.data.worldgen.placement.EndPlacements;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.levelgen.GenerationStep;

public class BarrenDepthsBiome extends EnderscapeBiome {

    public BarrenDepthsBiome() {
        super("barren_depths");
    }

    @Override
    public BCLBiome getBCLBiome() {
        BCLBiomeBuilder builder = createBuilder();

        builder.intendedType(EnderscapeBiomeTypes.SUBSURFACE);

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
        builder.fogColor(DEFAULT_FOG_COLOR);
        builder.fogDensity(DEFAULT_FOG_DENSITY);

        builder.surface(EnderscapeRuleSources.DEFAULT_END_SURFACE);
        
        builder.feature(GenerationStep.Decoration.SURFACE_STRUCTURES, EndPlacements.END_GATEWAY_RETURN);
        builder.feature(GenerationStep.Decoration.VEGETAL_DECORATION, EndPlacements.CHORUS_PLANT);
        
        builder.feature(BarrenEndFeatures.MURUSHROOMS);

        builder.feature(GeneralEndFeatures.VERADITE);
        builder.feature(GeneralEndFeatures.SCATTERED_VERADITE);

        builder.feature(GeneralEndFeatures.SHADOW_QUARTZ_ORE);
        builder.feature(GeneralEndFeatures.SCATTERED_SHADOW_QUARTZ_ORE);

        builder.feature(GeneralEndFeatures.NEBULITE_ORE);
        builder.feature(GeneralEndFeatures.VOID_NEBULITE_ORE);

        return builder.build();
    }
}