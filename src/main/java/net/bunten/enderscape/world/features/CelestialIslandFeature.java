package net.bunten.enderscape.world.features;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.blocks.MurushroomsBlock;
import net.bunten.enderscape.util.MathUtil;
import net.bunten.enderscape.util.OpenSimplexNoise;
import net.bunten.enderscape.util.PlantUtil;
import net.bunten.enderscape.util.States;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class CelestialIslandFeature extends Feature<CelestialIslandFeatureConfig> {
    public CelestialIslandFeature(Codec<CelestialIslandFeatureConfig> codec) {
        super(codec);
    }

    private final OpenSimplexNoise NOISE = new OpenSimplexNoise(1512);

    protected BlockState getTopBlockState(BlockPos pos, Random random) {
        double value =  NOISE.eval(pos.getX() * 0.2, pos.getZ() * 0.2) + (random.nextFloat() * 0.12F) * 4;
        if (value > 0.42F) {
            return States.END_STONE;
        } else {
            return States.CELESTIAL_MYCELIUM;
        }
    }

    protected void createIsland(StructureWorldAccess world, Random random, BlockPos pos, int initialWidth, int initialHeight, float murushroomChance) {
        double width = initialWidth;
        double height = initialHeight;

        int y = 0;

        while (height > 0.5F) {
            for (int x = MathUtil.floor(-width); x <= MathUtil.ceil(width); ++x) {
                for (int z = MathUtil.floor(-width); z <= MathUtil.ceil(width); ++z) {
                    if ((x * x + z * z) <= (width + 1) * (width + 1) && width > 0.35F) {
                        var pos2 = pos.add(x, y, z);
                        var state = y == 0 ? getTopBlockState(pos2, random) : States.END_STONE;
                        setBlockState(world, pos2, state);
                    }
                }
            }

            width -= random.nextFloat() * 0.3F + 0.4F;
            height -= random.nextFloat() * 2F + 0.2F;

            y--;
        }

        createFoliage(world, random, pos, initialWidth, initialHeight, murushroomChance);
    }

    protected void createFoliage(StructureWorldAccess world, Random random, BlockPos pos, int initialWidth, int initialHeight, float murushroomChance) {
        PlantUtil.generateCelestialGrowth(world, random, pos, UniformIntProvider.create(1, 1), UniformIntProvider.create(1, 2), 0.5F, initialWidth, 2, 2, initialWidth * 2);
        PlantUtil.generateCelestialVegetation(world, random, pos, initialWidth, 2, 2, initialWidth * 3);

        if (random.nextFloat() <= murushroomChance) {
            PlantUtil.generateMurushrooms(world, pos.add(0, initialHeight / 2, 0), random, MurushroomsBlock.MAX_AGE, initialWidth, initialHeight, 70);
            for (int i = 0; i < 16; i++) {
                var xOffset = MathUtil.nextInt(random, -initialWidth, initialWidth);
                var zOffset = MathUtil.nextInt(random, -initialWidth, initialWidth);

                var size = (int) MathUtil.nextFloat(random, initialWidth * 0.75F, initialWidth);
                var murushroomHeight = 2 + MathUtil.nextInt(random, initialWidth / 6, initialWidth / 3);

                var pos2 = pos.add(xOffset, -murushroomHeight, zOffset);

                if ((xOffset * xOffset + zOffset + zOffset) > initialWidth * 1.5F && initialWidth > 3) {
                    PlantUtil.generateLargeMurushroom(world, random, pos2, size);
                    break;
                }
            }
        }
    }

    @Override
    public boolean generate(FeatureContext<CelestialIslandFeatureConfig> context) {
        var config = context.getConfig();

        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos pos = context.getOrigin();

        int width = config.width.get(random);
        int height = config.height.get(random);
        float murushroomChance = config.murushroomChance;

        createIsland(world, random, pos, width, height, murushroomChance);

        return true;
    }
}