package net.bunten.enderscape.world.features.ores;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.MathUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class VoidOreFeature extends Feature<SingleStateFeatureConfig> {
    public VoidOreFeature(Codec<SingleStateFeatureConfig> codec) {
        super(codec);
    }

    private boolean hasTerrainDepth(WorldAccess world, BlockPos origin) {
        Mutable mutable = origin.mutableCopy();
        int depth = 16;

        for (int i = 0; i < depth; i++) {
            if (i >= depth) {
                return true;
            } else {
                if (!world.getBlockState(mutable).isOpaqueFullCube(world, mutable)) return false;
                mutable.move(Direction.UP);
            }
        }

        return true;
    }

    @Override
    public boolean generate(FeatureContext<SingleStateFeatureConfig> context) {
        boolean result = false;

        SingleStateFeatureConfig config = context.getConfig();
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos origin = context.getOrigin();

        for (int i = 0; i < 30; i++) {
            BlockPos pos = MathUtil.random(origin, random, 8, 4, 8);
            if (world.getBlockState(pos).isIn(EnderscapeBlocks.ORE_REPLACEABLES) && world.isAir(pos.down()) && hasTerrainDepth(world, pos)) {
                world.setBlockState(pos, config.state, 2);
                result = true;
            }
        }

        return result;
    }
}