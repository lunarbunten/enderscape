package net.bunten.enderscape.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record LargeMurublightChanterelleConfig(Direction direction, IntProvider height, IntProvider cap_radius, int tries) implements FeatureConfiguration {
    public static final Codec<LargeMurublightChanterelleConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            (Direction.CODEC.fieldOf("direction")).forGetter(config -> config.direction),
            (IntProvider.codec(1, 64).fieldOf("height")).forGetter(config -> config.height),
            (IntProvider.codec(1, 64).fieldOf("cap_radius")).forGetter(config -> config.cap_radius),
            (Codec.intRange(1, 64).fieldOf("tries")).forGetter(config -> config.tries))
            .apply(instance, LargeMurublightChanterelleConfig::new));
            
    public static final LargeMurublightChanterelleConfig DEFAULT = LargeMurublightChanterelleConfig.defaultFacing(Direction.DOWN);

    public static LargeMurublightChanterelleConfig defaultFacing(Direction direction) {
        return new LargeMurublightChanterelleConfig(direction, UniformInt.of(15, 30), UniformInt.of(4, 6), 32);
    }
}