package net.bunten.enderscape.feature;

import com.mojang.serialization.Codec;
import net.bunten.enderscape.block.AbstractGrowthBlock;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class GrowthFeature extends Feature<GrowthConfig> {
    public GrowthFeature(Codec<GrowthConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<GrowthConfig> context) {
        return AbstractGrowthBlock.generate(context.level(), context.origin(), context.random(), context.config());
    }
}