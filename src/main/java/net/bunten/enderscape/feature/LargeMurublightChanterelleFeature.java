package net.bunten.enderscape.feature;

import com.mojang.serialization.Codec;
import net.bunten.enderscape.feature.generator.LargeMurublightChanterelleGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class LargeMurublightChanterelleFeature extends Feature<LargeMurublightChanterelleConfig> {
    public LargeMurublightChanterelleFeature(Codec<LargeMurublightChanterelleConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<LargeMurublightChanterelleConfig> context) {
        return LargeMurublightChanterelleGenerator.tryGenerate(context.level(), context.random(), context.origin(), context.config());
    }
}