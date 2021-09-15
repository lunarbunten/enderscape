package net.enderscape.world.surface;

import com.google.common.collect.ImmutableList;
import net.enderscape.registry.EndBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

import java.util.Random;

public class CelestialPlainsSurfaceBuilder extends SurfaceBuilder<TernarySurfaceConfig> {
    private final BlockState END_STONE = Blocks.END_STONE.getDefaultState();
    private final BlockState CELESTIAL = EndBlocks.CELESTIAL_MYCELIUM_BLOCK.getDefaultState();
    private OctavePerlinNoiseSampler surfaceNoise;
    private long seed;
    public CelestialPlainsSurfaceBuilder() {
        super(TernarySurfaceConfig.CODEC);
    }

    public void initSeed(long value) {
        if (surfaceNoise == null) {
            surfaceNoise = new OctavePerlinNoiseSampler(new ChunkRandom(seed), ImmutableList.of(0));
        }
        seed = value;
    }

    @Override
    public void generate(Random random, Chunk chunk, Biome biome, int x, int z, int y, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int i, long l, TernarySurfaceConfig config) {
        double sample = surfaceNoise.sample(x * 0.086, 109, z * 0.086) + (random.nextFloat() / 4);
        BlockState state = sample > 0 ? CELESTIAL : END_STONE;
        SurfaceBuilder.DEFAULT.generate(random, chunk, biome, x, z, y, noise, defaultBlock, defaultFluid, seaLevel, i, l, new TernarySurfaceConfig(state, END_STONE, END_STONE));
    }
}