package net.bunten.enderscape.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

import java.util.List;

public record VoidShaleConfig(List<OreConfiguration.TargetBlockState> targets, IntProvider maxTerrainDepth) implements FeatureConfiguration {
    public static final Codec<VoidShaleConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.list(OreConfiguration.TargetBlockState.CODEC).fieldOf("targets").forGetter(config -> config.targets),
                    (IntProvider.CODEC.fieldOf("terrain_depth")).forGetter(config -> config.maxTerrainDepth))
            .apply(instance, VoidShaleConfig::new));
}