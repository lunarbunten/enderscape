package net.enderscape.world.features;

import com.mojang.serialization.Codec;
import net.enderscape.registry.EndBlocks;
import net.enderscape.util.EndMath;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

public class ScrapPillarsFeature extends Feature<DefaultFeatureConfig> {
    public ScrapPillarsFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    protected static void place(WorldAccess world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, state, 3);
    }

    private static boolean isNotFloating(WorldAccess world, BlockPos pos) {
        for (int i = 1; world.getBlockState(pos.down(i)).isOpaque() && i < 10; i++) {
            if (i > 5) {
                return true;
            }
        }
        return false;
    }

    private static boolean generatePillar(WorldAccess world, BlockPos pos, BlockState state, int height) {
        boolean bl = isNotFloating(world, pos);
        if (!bl) {
            return false;
        } else {
            for (int i = -5; i <= height; ++i) {
                place(world, pos.up(i), state);
            }
            return true;
        }
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos pos = context.getOrigin();

        if (!world.isAir(pos)) {
            return false;
        } else {
            if (world.isAir(pos) && world.getBlockState(pos.down()).isIn(EndBlocks.GROWTH_PLANTABLE_BLOCKS)) {
                int height = 1;
                int tries = EndMath.nextInt(random, 3, 6);
                for (int i = 0; i < tries; i++) {
                    BlockPos pos2 = EndMath.random(pos, random, 7, 7);
                    boolean bl = generatePillar(world, pos2, EndBlocks.POLISHED_SCRAP_PILLAR.getDefaultState(), height);
                    if (bl) {
                        height++;
                    }
                }

                return true;
            } else {
                return false;
            }
        }
    }
}