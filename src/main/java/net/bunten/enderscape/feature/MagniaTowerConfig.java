package net.bunten.enderscape.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record MagniaTowerConfig(FloatProvider floor_replacement_radius, IntProvider pillar_height, IntProvider sphere_radius, IntProvider ring_radius, FloatProvider alluring_magnia_sprout_placement_chance, FloatProvider repulsive_magnia_sprout_placement_chance) implements FeatureConfiguration {

    public static final Codec<MagniaTowerConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    FloatProviders.codec(1, 64).fieldOf("floor_replacement_radius").forGetter(config -> config.floor_replacement_radius),
                    IntProviders.codec(1, 64).fieldOf("height").forGetter(config -> config.pillar_height),
                    IntProviders.codec(0, 64).fieldOf("sphere_radius").forGetter(config -> config.sphere_radius),
                    IntProviders.codec(0, 64).fieldOf("ring_radius").forGetter(config -> config.ring_radius),
                    FloatProviders.codec(0, 1).fieldOf("alluring_magnia_sprout_placement_chance").forGetter(config -> config.alluring_magnia_sprout_placement_chance),
                    FloatProviders.codec(0, 1).fieldOf("repulsive_magnia_sprout_placement_chance").forGetter(config -> config.repulsive_magnia_sprout_placement_chance))
                    .apply(instance, MagniaTowerConfig::new));
}