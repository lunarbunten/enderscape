package net.bunten.enderscape.world.features.ores;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.MathUtil;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class ScatteredOreFeature extends Feature<SingleStateFeatureConfig> {
    public ScatteredOreFeature(Codec<SingleStateFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<SingleStateFeatureConfig> context) {
        boolean result = false;
        
        SingleStateFeatureConfig config = context.getConfig();
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos pos = context.getOrigin();

        BlockState state = config.state;
        BlockPos pos2 = MathUtil.random(pos, random, 8, 4, 8);
        
        for (int i = 0; i < 70; i++) {
            pos2 = pos2.add(MathUtil.nextInt(random, -1, 1), MathUtil.nextInt(random, -1, 1), MathUtil.nextInt(random, -1, 1));
            if (world.getBlockState(pos2).isIn(EnderscapeBlocks.ORE_REPLACEABLES)) {
                world.setBlockState(pos2, state, 2);
                result = true;
            }
        }

        return result;
    }
}