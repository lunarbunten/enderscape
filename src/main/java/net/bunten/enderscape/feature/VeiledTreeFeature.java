package net.bunten.enderscape.feature;

import com.mojang.serialization.Codec;
import net.bunten.enderscape.feature.generator.VeiledTreeGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class VeiledTreeFeature extends Feature<VeiledTreeConfig> {
    public VeiledTreeFeature(Codec<VeiledTreeConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<VeiledTreeConfig> context) {
        return VeiledTreeGenerator.tryGenerate(context.level(), context.random(), context.origin(), context.config());
    }
}