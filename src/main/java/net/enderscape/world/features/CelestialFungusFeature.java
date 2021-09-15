package net.enderscape.world.features;

import com.mojang.serialization.Codec;
import net.enderscape.blocks.BerryStage;
import net.enderscape.blocks.EndProperties;
import net.enderscape.registry.EndBlocks;
import net.enderscape.util.EndMath;
import net.enderscape.util.Util;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

public class CelestialFungusFeature extends Feature<DefaultFeatureConfig> {
    private static final BlockState STEM = EndBlocks.CELESTIAL_STEM.getDefaultState();
    private static final BlockState CAP = EndBlocks.CELESTIAL_CAP.getDefaultState();
    public CelestialFungusFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    protected static void place(WorldAccess world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, state, 3);
    }

    public static int getHeight(Random random) {
        return EndMath.nextInt(random, 10, 35);
    }

    public static boolean isEnoughAir(WorldAccess world, BlockPos start, int height) {
        for (int i = 1; i < height; i++) {
            if (!world.isAir(start.up(i))) {
                return false;
            }
        }
        return true;
    }

    private static void generateVines(WorldAccess world, Random random, BlockPos pos, int range) {
        int half = range / 2;
        double f = pos.getX() + (random.nextInt(range) - half);
        double g = pos.getY() - random.nextInt(range);
        double h = pos.getZ() + (random.nextInt(range) - half);
        pos = new BlockPos(f, g, h);
        if (world.isAir(pos) && world.isAir(pos.down()) && world.getBlockState(pos.up()).isOpaque() && random.nextInt(50) == 0) {
            int length = EndMath.nextInt(random, range / 4, range);
            for (int i = 0; i <= length; ++i) {
                BlockPos pos2 = pos.down(i);
                if (world.isAir(pos2)) {
                    if (i == length || !world.isAir(pos2.down())) {
                        BlockState state = EndBlocks.FLANGER_BERRY_BLOCK.getDefaultState();
                        state = state.with(EndProperties.BERRY_STAGE, Util.getRandom(BerryStage.values(), random));
                        place(world, pos2, state);
                        break;
                    } else {
                        place(world, pos2, EndBlocks.FLANGER_BERRY_VINE.getDefaultState().with(Properties.ATTACHED, true));
                    }
                }
            }
        }
    }

    private static void generateCap(WorldAccess world, Random random, BlockPos pos, int radius) {
        for (int x = -radius + 1; x < radius; x++) {
            for (int z = -radius + 1; z < radius; z++) {
                double distance = Math.sqrt(x * x + z * z);
                if (distance <= radius) {
                    for (int a = 1; a < radius; a++) {
                        int b = distance >= radius * 0.75F && a == 1 && radius > 3 ? 1 : 0;
                        place(world, pos.add(x / a, -(2 * a + b) + 2, z / a), CAP);
                        for (int i = 0; i < 6; i++) {
                            generateVines(world, random, pos.down(), radius * 2);
                        }
                    }
                }
            }
        }
    }

    public static void generate(WorldAccess world, Random random, BlockPos pos, int height) {
        generateCap(world, random, pos.up(height), (height / 4));
        for (int a = 0; a < height; a++) {
            place(world, pos.up(a), STEM);
        }
        place(world, pos.down(), Blocks.END_STONE.getDefaultState());
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos pos = context.getOrigin();

        if (!world.isAir(pos)) {
            return false;
        } else {
            if (world.getBlockState(pos.down()).isIn(EndBlocks.GROWTH_PLANTABLE_BLOCKS)) {
                for (int i = 0; i < 16; i++) {
                    int height = getHeight(random);
                    if (isEnoughAir(world, pos, height)) {
                        generate(world, random, pos, height);
                        break;
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }
}