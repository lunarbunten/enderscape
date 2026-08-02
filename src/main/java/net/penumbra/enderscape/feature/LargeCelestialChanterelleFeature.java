package net.penumbra.enderscape.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.penumbra.enderscape.block.AbstractVineBlock;
import net.penumbra.enderscape.block.state.StateProperties;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import net.penumbra.enderscape.registry.tag.EnderscapeBlockTags;
import net.penumbra.enderscape.util.BlockUtil;

import static net.penumbra.enderscape.util.BlockUtil.replace;
import static net.penumbra.enderscape.util.BlockUtil.set;

public class LargeCelestialChanterelleFeature extends Feature<LargeCelestialChanterelleFeature.Config> {

    private static final WeightedStateProvider PURUBERRIES = new WeightedStateProvider(WeightedList.<BlockState>builder()
            .add(EnderscapeBlocks.PURUBERRY_FLOWER.defaultBlockState(), 1)
            .add(EnderscapeBlocks.UNRIPE_PURUBERRY_BLOCK.defaultBlockState(), 1)
            .add(EnderscapeBlocks.RIPE_PURUBERRY_BLOCK.defaultBlockState(), 1)
    );

    public LargeCelestialChanterelleFeature(Codec<Config> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        Config config = context.config();

        Direction direction = Direction.UP;
        BlockPos attached = origin.relative(direction.getOpposite());

        if (level.getBlockState(attached).isFaceSturdy(level, attached, direction)) {
            for (int i = 0; i < config.tries(); i++) {
                int height = config.height().sample(random);
                int radius = (int) (height / config.cap_radius_division());

                if (isEnoughAir(level, origin, height, radius / 2)) {
                    return generate(level, origin, random, config, height, radius);
                }
            }
        }

        return false;
    }

    protected boolean generate(WorldGenLevel level, BlockPos pos, RandomSource random, LargeCelestialChanterelleFeature.Config config, int height, int radius) {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 4);
        set(level, pos.below(), Blocks.END_STONE.defaultBlockState());
        generateCap(level, random, pos.above(height), config, radius);

        for (int i = 0; i < height; i++) {
            set(level, pos.above(i), EnderscapeBlocks.CELESTIAL_STEM.defaultBlockState());
        }

        return true;
    }

    protected boolean isEnoughAir(WorldGenLevel level, BlockPos pos, int height, int radius) {
        for (int i = 1; i < height + 1; i++) {
            if (!level.isEmptyBlock(pos.above(i))) {
                return false;
            }
        }
        return true;
    }

    protected void generateVines(WorldGenLevel level, RandomSource random, BlockPos origin, LargeCelestialChanterelleFeature.Config config, int radius) {
        int generatedVines = 0;
        int maxTries = config.vine_generation_tries();
        int maxRadius = (int) (radius * 0.8F);
        int minHeight = (int) (radius * 0.4F);

        for (int i = 0; i < maxTries; i++) {
            BlockPos pos = BlockUtil.random(origin, random, maxRadius, 0, maxRadius).below(Mth.nextInt(random, 0, minHeight));

            for (int offset = -4; offset < 4; offset++) {
                BlockPos testPos = pos.above(offset);
                if (level.getBlockState(testPos).isAir() && level.getBlockState(testPos.above()).is(EnderscapeBlockTags.SUPPORTS_PURUBERRY_VINE)) {
                    pos = testPos;
                    break;
                }
            }

            if (pos.closerThan(origin, (int) (radius * 0.6F)) || (generatedVines > radius / 4 && random.nextFloat() <= config.excess_vine_discard_chance())) {
                continue;
            }

            if (canVineGrow(level, pos)) {
                generateVine(level, random, pos, radius);
                generatedVines++;
            }
        }
    }

    protected boolean canVineGrow(LevelAccessor level, BlockPos pos) {
        return level.isEmptyBlock(pos) && level.isEmptyBlock(pos.below()) && level.getBlockState(pos.above()).is(EnderscapeBlockTags.SUPPORTS_PURUBERRY_VINE);
    }

    protected void generateVine(WorldGenLevel level, RandomSource random, BlockPos start, int radius) {
        int length = Mth.nextInt(random, (int) (radius * 0.8F), radius * 2);
        BlockPos.MutableBlockPos mutable = start.mutable();

        for (int i = 0; i <= length; i++) {
            if (level.isEmptyBlock(mutable)) {
                if (i == length || !level.isEmptyBlock(mutable.below())) {
                    replace(level, mutable, PURUBERRIES.getState(level, random, mutable));
                    break;
                } else {
                    replace(level, mutable, EnderscapeBlocks.PURUBERRY_VINE.defaultBlockState().setValue(StateProperties.ATTACHED, true).setValue(AbstractVineBlock.AGE, AbstractVineBlock.MAX_AGE));
                }

                mutable.move(Direction.DOWN);
            }
        }
    }

    protected void generateCap(WorldGenLevel level, RandomSource random, BlockPos pos, LargeCelestialChanterelleFeature.Config config, int radius) {
        for (int x = -radius + 1; x < radius; x++) {
            for (int z = -radius + 1; z < radius; z++) {
                double distance = Math.sqrt(x * x + z * z);

                if (distance <= radius) {
                    for (int capOrder = 1; capOrder < radius; capOrder++) {
                        int droopAmount = distance >= radius * config.cap_droop_percentage() && capOrder == 1 && radius > 3 ? 1 : 0;
                        replace(level, pos.offset(x / capOrder, -(2 * capOrder + droopAmount) + 2, z / capOrder), EnderscapeBlocks.CELESTIAL_CAP.defaultBlockState());
                    }
                }
            }
        }

        generateVines(level, random, pos.below(), config, radius);
    }

    public record Config(IntProvider height, float cap_radius_division, float cap_droop_percentage, float excess_vine_discard_chance, int vine_generation_tries, int tries) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                (IntProviders.codec(1, 64).fieldOf("height")).forGetter(config -> config.height),
                (Codec.floatRange(1, 64).fieldOf("cap_radius_division")).forGetter(config -> config.cap_radius_division),
                (Codec.floatRange(0, 1).fieldOf("cap_droop_percentage")).forGetter(config -> config.cap_droop_percentage),
                (Codec.floatRange(0, 1).fieldOf("excess_vine_discard_chance")).forGetter(config -> config.excess_vine_discard_chance),
                (Codec.intRange(0, 256).fieldOf("vine_generation_tries")).forGetter(config -> config.vine_generation_tries),
                (Codec.intRange(1, 64).fieldOf("tries")).forGetter(config -> config.tries))
                .apply(instance, Config::new));

        public static final Config DEFAULT = new Config(UniformInt.of(10, 35), 4, 0.75F, 1, 64, 16);
    }
}