package net.penumbra.enderscape.feature;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.DripstoneUtils;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.penumbra.enderscape.block.BlisteredMagniaBlock;
import net.penumbra.enderscape.block.MagniaBlock;
import net.penumbra.enderscape.block.MagniaSproutBlock;
import net.penumbra.enderscape.block.state.StateProperties;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import net.penumbra.enderscape.util.BlockUtil;

import java.util.List;

public class MagniaSpikeFeature extends Feature<MagniaSpikeFeature.Config> {

    public MagniaSpikeFeature(Codec<Config> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        Config config = context.config();

        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        if (level.getBlockState(origin).isAir() && hasSolidCeiling(level, origin, 1) && BlockUtil.hasTerrainDepth(level, origin, config.minimum_terrain_depth(), Direction.UP)) {
            List<BlockPos> placedBlocks = Lists.newArrayList();

            generateTower(level, origin, random, placedBlocks, config);
            generateSprouts(level, random, placedBlocks, config);

            return true;
        }

        return false;
    }


    private void generateTower(WorldGenLevel level, BlockPos origin, RandomSource random, List<BlockPos> placedBlocks, Config config) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                boolean atEdgeX = x == -1 || x == 1;
                boolean atEdgeZ = z == -1 || z == 1;

                int height = getPillarHeight(random, x, z, atEdgeX, atEdgeZ, config);

                for (int y = 0; y < height; y++) {
                    BlockPos currentPos = origin.offset(x, -y, z);

                    if (level.isStateAtPosition(currentPos, DripstoneUtils::isEmptyOrWater)) {
                        level.setBlock(currentPos, EnderscapeBlocks.REPULSIVE_MAGNIA.defaultBlockState(), 2);
                        placedBlocks.add(currentPos);
                    }
                }
            }
        }
    }

    private void generateSprouts(WorldGenLevel level, RandomSource random, List<BlockPos> placedBlocks, Config config) {
        for (BlockPos pos : placedBlocks) {
            for (Direction direction : Direction.values()) {
                BlockState floor = level.getBlockState(pos);

                if (floor.getBlock() instanceof MagniaBlock) {
                    float chance = config.repulsive_magnia_sprout_placement_chance().sample(random);

                    if (random.nextFloat() <= chance) {
                        BlockPos relative = pos.relative(direction);

                        if (level.getBlockState(relative).canBeReplaced() && level.getBlockState(pos.relative(direction, 2)).canBeReplaced()) {
                            if (random.nextInt(80) == 0) {
                                BlockState state = EnderscapeBlocks.BLISTERED_MAGNIA.defaultBlockState().setValue(StateProperties.OPTIONAL_MAGNIA_POLARITY, BlisteredMagniaBlock.selectPolarity(level, relative));

                                level.setBlock(relative, state, 2);
                                level.scheduleTick(relative, state.getBlock(), 1);
                            } else {
                                level.setBlock(relative, EnderscapeBlocks.REPULSIVE_MAGNIA_SPROUT.defaultBlockState().setValue(MagniaSproutBlock.FACING, direction), 2);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean hasSolidCeiling(WorldGenLevel level, BlockPos origin, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (level.getBlockState(origin.offset(x, 1, z)).isAir()) return false;
            }
        }
        return true;
    }

    private int getPillarHeight(RandomSource random, int x, int z, boolean corner, boolean edge, Config config) {
        int height = config.height().sample(random);

        if (height <= config.height().maxInclusive() - 2 && random.nextInt(10) == 0) height *= 2;

        if (corner && edge) {
            height /= Mth.nextInt(random, 4, 6);
        } else if (!(x == 0 && z == 0)) {
            height /= Mth.nextInt(random, 2, 4);

            if (random.nextBoolean()) height += 3;
        }

        return height;
    }

    public record Config(IntProvider height, FloatProvider repulsive_magnia_sprout_placement_chance, int minimum_terrain_depth) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        IntProviders.codec(1, 64).fieldOf("height").forGetter(config -> config.height),
                        FloatProviders.codec(0, 1).fieldOf("repulsive_magnia_sprout_placement_chance").forGetter(config -> config.repulsive_magnia_sprout_placement_chance),
                        Codec.INT.fieldOf("minimum_terrain_depth").forGetter(config -> config.minimum_terrain_depth))
                        .apply(instance, Config::new));
    }
}
