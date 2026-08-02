package net.penumbra.enderscape.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

import java.util.List;

public class ScatteredOreFeature extends Feature<ScatteredOreFeature.Config> {
    public ScatteredOreFeature(Codec<Config> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        Config config = context.config();
        WorldGenLevel level = context.level();
        RandomSource random = context.random();

        ChunkPos center = ChunkPos.containing(context.origin());
        BlockPos next = context.origin();

        int size = config.size().sample(random) / 2;
        boolean bl = false;

        for (int i = 0; i < config.tries().sample(random); i++) {
            for (BlockPos pos : BlockPos.withinManhattanStream(next, size, size, size).toList()) {
                if (!canWrite(center, pos)) continue;

                for (OreConfiguration.TargetBlockState target : config.targets()) {
                    if (target.target.test(level.getBlockState(pos), random)) {
                        level.setBlock(pos, target.state, 2);
                        bl = true;
                    }
                }
            }

            next = next.offset(config.scatter_increase().sample(random), config.scatter_increase().sample(random), config.scatter_increase().sample(random));
        }

        return bl;
    }

    private boolean canWrite(ChunkPos center, BlockPos pos) {
        int distanceX = Math.abs(center.x() - SectionPos.blockToSectionCoord(pos.getX()));
        int distanceZ = Math.abs(center.z() - SectionPos.blockToSectionCoord(pos.getZ()));

        return distanceX <= 1 && distanceZ <= 1;
    }

    public record Config(List<OreConfiguration.TargetBlockState> targets, IntProvider tries, IntProvider scatter_increase, IntProvider size) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.list(OreConfiguration.TargetBlockState.CODEC).fieldOf("targets").forGetter(config -> config.targets),
                IntProviders.codec(1, 200).fieldOf("tries").forGetter(config -> config.tries),
                IntProviders.codec(-5, 5).fieldOf("scatter_increase").forGetter(config -> config.scatter_increase),
                IntProviders.codec(0, 5).fieldOf("size").forGetter(config -> config.size))
                .apply(instance, Config::new));
    }
}