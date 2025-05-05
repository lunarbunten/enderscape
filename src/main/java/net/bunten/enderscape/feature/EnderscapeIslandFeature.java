package net.bunten.enderscape.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.ArrayList;
import java.util.List;

public class EnderscapeIslandFeature extends Feature<NoneFeatureConfiguration> {
    public EnderscapeIslandFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
        WorldGenLevel level = featurePlaceContext.level();
        RandomSource random = featurePlaceContext.random();
        BlockPos origin = featurePlaceContext.origin();

        float f = (float) random.nextInt(3) + 4.0F;

        List<BlockPos> list = new ArrayList<>();

        for (int i = 0; f > 0.5F; i--) {
            for (int j = Mth.floor(-f); j <= Mth.ceil(f); j++) {
                for (int k = Mth.floor(-f); k <= Mth.ceil(f); k++) {
                    if ((float) (j * j + k * k) <= (f + 1.0F) * (f + 1.0F)) {
                        list.add(origin.offset(j, i, k));
                    }
                }
            }

            f -= (float) random.nextInt(2) + 0.5F;
        }

        int lowest = Integer.MAX_VALUE;
        int highest = Integer.MIN_VALUE;

        for (BlockPos pos : list) {
            setBlock(level, pos, Blocks.END_STONE.defaultBlockState());
            if (pos.getY() < lowest) lowest = pos.getY();
            if (pos.getY() > highest) highest = pos.getY();
        }

        return true;
    }
}