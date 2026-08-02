package net.penumbra.enderscape.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.penumbra.enderscape.block.VeiledLeafPileBlock;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import org.joml.SimplexNoise;

public class VeiledLeafPileFeature extends Feature<VeiledLeafPileFeature.Config> {
    public VeiledLeafPileFeature(Codec<Config> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        Config config = context.config();
        WorldGenLevel level = context.level();
        RandomSource random = context.random();

        boolean result = false;

        float radius = config.radius().sample(random);
        float noiseScale = 0.1F;

        for (float x = -radius; x <= radius; x++) {
            for (float z = -radius; z <= radius; z++) {
                BlockPos offset = context.origin().offset((int) x, 0, (int) z);

                float noiseValue = SimplexNoise.noise(x * noiseScale, z * noiseScale);
                float distance = (float) Math.sqrt(x * x + z * z);

                if (distance <= radius * (0.8F + noiseValue * 0.4F) && random.nextFloat() > config.density().sample(random)) {
                    BlockPos.MutableBlockPos mutable = offset.above(3).mutable();

                    for (int i = 0; i < 6; i++) {
                        Block block = EnderscapeBlocks.VEILED_LEAF_PILE;
                        if (level.isEmptyBlock(mutable) && level.isEmptyBlock(mutable.above()) && VeiledLeafPileBlock.canSurvive(level, mutable, block)) {
                            level.setBlock(mutable, block.defaultBlockState().setValue(VeiledLeafPileBlock.LAYERS, config.layers().sample(random)), 2);
                            result = true;
                        }
                        mutable.move(Direction.DOWN);
                    }
                }
            }
        }

        return result;
    }

    public record Config(FloatProvider radius, FloatProvider density, IntProvider layers) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                FloatProviders.codec(1, 64).fieldOf("radius").forGetter(config -> config.radius),
                FloatProviders.codec(0, 1).fieldOf("density").forGetter(config -> config.density),
                IntProviders.codec(1, 8).fieldOf("layers").forGetter(config -> config.layers)
        ).apply(instance, Config::new));
    }
}