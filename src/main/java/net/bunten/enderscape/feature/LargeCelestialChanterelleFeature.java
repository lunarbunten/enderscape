package net.bunten.enderscape.feature;

import com.mojang.serialization.Codec;
import net.bunten.enderscape.feature.generator.LargeCelestialChanterelleGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class LargeCelestialChanterelleFeature extends Feature<LargeCelestialChanterelleConfig> {
    public LargeCelestialChanterelleFeature(Codec<LargeCelestialChanterelleConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<LargeCelestialChanterelleConfig> context) {
        return LargeCelestialChanterelleGenerator.tryGenerate(context.level(), context.origin(), context.random(), context.config());
    }
}