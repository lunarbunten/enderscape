package net.bunten.enderscape.world.decider;

import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import org.betterx.bclib.api.v2.generator.BCLibEndBiomeSource;
import org.betterx.bclib.api.v2.generator.TypeBiomeDecider;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI.BiomeType;

public abstract class EnderscapeDecider extends TypeBiomeDecider {
    protected EnderscapeDecider(Registry<Biome> biomeRegistry, BiomeType assignedType) {
        super(biomeRegistry, assignedType);
    }

    abstract boolean canApply(BiomeType originalType, BiomeType suggestedType, double density, int maxHeight, int blockX, int blockY, int blockZ, int quarterX, int quarterY, int quarterZ);

    @Override
    public boolean canProvideFor(BiomeSource source) {
        return source instanceof BCLibEndBiomeSource;
    }

    @Override
    public BiomeType suggestType(BiomeType originalType, BiomeType suggestedType, double density, int maxHeight, int blockX, int blockY, int blockZ, int quarterX, int quarterY, int quarterZ) {
        if (canApply(originalType, suggestedType, density, maxHeight, blockX, blockY, blockZ, quarterX, quarterY, quarterZ) && assignedType != null) return assignedType;
        return suggestedType;
    }
}