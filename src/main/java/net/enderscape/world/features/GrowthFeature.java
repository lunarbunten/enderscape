package net.enderscape.world.features;

import com.mojang.serialization.Codec;
import net.enderscape.blocks.EndProperties;
import net.enderscape.blocks.Part;
import net.enderscape.registry.EndBlocks;
import net.enderscape.util.EndMath;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

public class GrowthFeature extends Feature<SingleStateFeatureConfig> {
    public GrowthFeature(Codec<SingleStateFeatureConfig> codec) {
        super(codec);
    }

    public static boolean generate(BlockState state, WorldAccess world, Random random, BlockPos pos, int xRange, int yRange) {
        for (int i = 0; i < 8; i++) {
            BlockPos pos2 = EndMath.random(pos, random, xRange, yRange, xRange);
            if (world.getBlockState(pos2.down()).isIn(EndBlocks.GROWTH_PLANTABLE_BLOCKS)) {
                int height = random.nextBoolean() ? EndMath.nextInt(random, 0, 2) : 0;
                for (int g = 0; g <= height; ++g) {
                    BlockPos pos3 = pos2.up(g);
                    if (world.isAir(pos3)) {
                        if (g == height || !world.isAir(pos3.up())) {
                            world.setBlockState(pos3, state.with(EndProperties.PART, Part.TOP), 2);
                            break;
                        } else if (g == 0) {
                            world.setBlockState(pos3, state.with(EndProperties.PART, Part.BOTTOM), 2);
                        } else {
                            world.setBlockState(pos3, state.with(EndProperties.PART, Part.MIDDLE), 2);
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean generate(FeatureContext<SingleStateFeatureConfig> context) {
        SingleStateFeatureConfig config = context.getConfig();
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos pos = context.getOrigin();

        BlockState state = config.state;
        pos = world.getTopPosition(Type.WORLD_SURFACE_WG, pos);
        generate(state, world, random, pos, 4, 2);

        return true;
    }
}