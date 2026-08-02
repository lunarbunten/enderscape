package net.penumbra.enderscape.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.penumbra.enderscape.registry.tag.EnderscapeBlockTags;
import net.penumbra.enderscape.util.BlockUtil;

import java.util.List;
import java.util.Optional;

public class CeilingOreFeature extends Feature<CeilingOreFeature.Config> {
    public CeilingOreFeature(Codec<Config> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        Config config = context.config();

        WorldGenLevel level = context.level();
        RandomSource random = context.random();

        boolean generated = false;

        Optional<BlockPos> closest = BlockPos.findClosestMatch(context.origin(), 4, 32, (pos) -> level.getBlockState(pos).is(EnderscapeBlockTags.ORE_REPLACEABLE) && level.isEmptyBlock(pos.below()) && BlockUtil.hasTerrainDepth(level, pos, config.terrainDepth().sample(random), Direction.UP));

        if (closest.isPresent()) {
            for (OreConfiguration.TargetBlockState target : config.targets()) {
                if (target.target.test(level.getBlockState(closest.get()), random)) {
                    level.setBlock(closest.get(), target.state, 2);
                    generated = true;
                }
            }
        }

        return generated;
    }

    public record Config(List<OreConfiguration.TargetBlockState> targets, IntProvider terrainDepth) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        Codec.list(OreConfiguration.TargetBlockState.CODEC).fieldOf("targets").forGetter(config -> config.targets),
                        (IntProviders.CODEC.fieldOf("terrain_depth")).forGetter(config -> config.terrainDepth))
                .apply(instance, Config::new));
    }
}