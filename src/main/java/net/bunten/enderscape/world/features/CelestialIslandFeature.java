package net.bunten.enderscape.world.features;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.blocks.MurushroomsBlock;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.MathUtil;
import net.bunten.enderscape.util.OpenSimplexNoise;
import net.bunten.enderscape.util.PlantUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class CelestialIslandFeature extends Feature<DefaultFeatureConfig> {
    public CelestialIslandFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    private final OpenSimplexNoise NOISE = new OpenSimplexNoise(1512);

    private final BlockState END_STONE = Blocks.END_STONE.getDefaultState();
    private final BlockState CELESTIAL = EnderscapeBlocks.CELESTIAL_MYCELIUM_BLOCK.getDefaultState();

    protected int getWidth(Random random) {
        return 3 + random.nextInt(3);
    }

    protected int getHeight(Random random) {
        return 8 + random.nextInt(6);
    }

    protected BlockState getTopBlockState(BlockPos pos, Random random) {
        double value =  NOISE.eval(pos.getX() * 0.2, pos.getZ() * 0.2) + (random.nextFloat() * 0.12F) * 4;
        if (value > 0.42F) {
            return END_STONE;
        } else {
            return CELESTIAL;
        }
    }

    protected void createIsland(StructureWorldAccess world, Random random, BlockPos pos) {
        int initialWidth = getWidth(random);
        int initialHeight = getHeight(random);

        double width = initialWidth;
        double height = initialHeight;

        int y = 0;

        while (height > 0.5F) {
            for (int x = MathUtil.floor(-width); x <= MathUtil.ceil(width); ++x) {
                for (int z = MathUtil.floor(-width); z <= MathUtil.ceil(width); ++z) {
                    if ((x * x + z * z) <= (width + 1) * (width + 1) && width > 0.35F) {
                        var pos2 = pos.add(x, y, z);
                        var state = y == 0 ? getTopBlockState(pos2, random) : END_STONE;
                        setBlockState(world, pos2, state);
                    }
                }
            }

            width -= random.nextFloat() * 0.3F + 0.4F;
            height -= random.nextFloat() * 2F + 0.2F;

            y--;
        }

        createFoliage(world, random, pos, initialWidth, initialHeight);
    }

    protected void createFoliage(StructureWorldAccess world, Random random, BlockPos pos, int initialWidth, int initialHeight) {
        PlantUtil.generateCelestialGrowth(world, random, pos, initialWidth, 2, initialWidth * 6);
        PlantUtil.generateCelestialVegetation(world, random, pos, initialWidth, 2, initialWidth * 6);

        if (random.nextInt(4) == 0) {
            PlantUtil.generateMurushrooms(world, pos.add(0, initialHeight / 2, 0), random, MurushroomsBlock.MAX_AGE, initialWidth, initialHeight, 200);
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
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos pos = context.getOrigin();

        createIsland(world, random, pos);

        return true;
    }
}