package net.enderscape.world.features;

import com.mojang.serialization.Codec;
import net.enderscape.util.EndMath;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

public class VoidOreFeature extends Feature<SingleStateFeatureConfig> {
    public VoidOreFeature(Codec<SingleStateFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<SingleStateFeatureConfig> context) {
        SingleStateFeatureConfig config = context.getConfig();
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos pos = context.getOrigin();

        if (!world.isAir(pos)) {
            return false;
        } else {
            for (int i = 0; i < 30; i++) {
                BlockPos pos2 = EndMath.random(pos, random, 8, 4, 8);
                if (world.getBlockState(pos2).isOf(Blocks.END_STONE) && world.isAir(pos.down()) && world.getBlockState(pos2.up()).isOpaque()) {
                    world.setBlockState(pos2, config.state, 2);
                }
            }
            return true;
        }
    }
}