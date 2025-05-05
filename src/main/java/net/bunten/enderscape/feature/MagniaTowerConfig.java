package net.bunten.enderscape.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record MagniaTowerConfig(FloatProvider floor_replacement_radius, IntProvider pillar_height, IntProvider sphere_radius, IntProvider ring_radius, FloatProvider sprout_placement_chance) implements FeatureConfiguration {

    public static final Codec<MagniaTowerConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    FloatProvider.codec(1, 64).fieldOf("floor_replacement_radius").forGetter(config -> config.floor_replacement_radius),
                    IntProvider.codec(1, 64).fieldOf("pillar_height").forGetter(config -> config.pillar_height),
                    IntProvider.codec(0, 64).fieldOf("sphere_radius").forGetter(config -> config.sphere_radius),
                    IntProvider.codec(0, 64).fieldOf("ring_radius").forGetter(config -> config.ring_radius),
                    FloatProvider.codec(0, 1).fieldOf("sprout_placement_chance").forGetter(config -> config.sprout_placement_chance))
                    .apply(instance, MagniaTowerConfig::new));
}