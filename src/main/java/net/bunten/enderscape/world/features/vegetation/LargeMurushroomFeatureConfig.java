package net.bunten.enderscape.world.features.vegetation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record LargeMurushroomFeatureConfig(IntProvider height, IntProvider cap_radius, int tries) implements FeatureConfiguration {
    public static final Codec<LargeMurushroomFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            (IntProvider.codec(1, 64).fieldOf("height")).forGetter(config -> config.height),
            (IntProvider.codec(1, 64).fieldOf("cap_radius")).forGetter(config -> config.cap_radius),
            (Codec.intRange(1, 64).fieldOf("tries")).forGetter(config -> config.tries))
            .apply(instance, LargeMurushroomFeatureConfig::new));
}