package net.enderscape.world.surface;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import java.util.Random;

public class EndSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
    private final BlockState END_STONE = Blocks.END_STONE.getDefaultState();
    private OctavePerlinNoiseSampler surfaceNoise;
    private long seed;
    public EndSurfaceBuilder() {
        super(TernarySurfaceConfig.CODEC);
    }

    public static EndSurfaceBuilder register(String name) {
        return Registry.register(Registry.SURFACE_BUILDER, name, new EndSurfaceBuilder());
    }

    public void initSeed(long value) {
        if (surfaceNoise == null) {
            surfaceNoise = new OctavePerlinNoiseSampler(new ChunkRandom(seed), ImmutableList.of(0));
        }
        seed = value;
    }

    @Override
    public void generate(Random random, Chunk chunk, Biome biome, int x, int z, int y, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int i, long l, TernarySurfaceConfig config) {
        SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, y, noise, defaultBlock, defaultFluid, seaLevel, i, l, new TernarySurfaceConfig(END_STONE, END_STONE, END_STONE));
    }

    public ConfiguredSurfaceBuilder<TernarySurfaceConfig> configured() {
        return withConfig(new TernarySurfaceConfig(END_STONE, END_STONE, END_STONE));
    }
}