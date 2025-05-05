package net.bunten.enderscape.feature;

import com.mojang.serialization.Codec;
import net.bunten.enderscape.block.VeiledLeafPileBlock;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.joml.SimplexNoise;

public class VeiledLeafPileFeature extends Feature<VeiledLeafPileConfig> {
    public VeiledLeafPileFeature(Codec<VeiledLeafPileConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<VeiledLeafPileConfig> context) {
        VeiledLeafPileConfig config = context.config();
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
}