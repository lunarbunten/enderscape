package net.bunten.enderscape.world.decider;

import org.betterx.bclib.api.v2.generator.BCLBiomeSource;
import org.betterx.bclib.api.v2.generator.BiomeDecider;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI.BiomeType;

import net.bunten.enderscape.config.Config;
import net.bunten.enderscape.world.EnderscapeBiomeTypes;
import net.bunten.enderscape.world.EnderscapeBiomes;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;

public class SkyBiomeDecider extends EnderscapeDecider {
    public SkyBiomeDecider() {
        this(null);
    }

    protected SkyBiomeDecider(Registry<Biome> registry) {
        super(registry, EnderscapeBiomeTypes.SKY);
    }

    @Override
    public BiomeDecider createInstance(BCLBiomeSource source) {
        return new SkyBiomeDecider(source.getBiomeRegistry());
    }

    @Override
    public boolean canApply(BiomeType originalType, BiomeType suggestedType, double density, int maxHeight, int blockX, int blockY, int blockZ, int quarterX, int quarterY, int quarterZ) {
        if (suggestedType == BiomeType.END_CENTER) return false;
        
        return Config.WORLD.useSkyBiomes() && blockY >= EnderscapeBiomes.getSkyBiomeHeight();
    }
}