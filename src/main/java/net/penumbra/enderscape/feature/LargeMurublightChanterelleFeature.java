package net.penumbra.enderscape.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.penumbra.enderscape.block.MurublightBracketBlock;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;

import static net.penumbra.enderscape.util.BlockUtil.replace;
import static net.penumbra.enderscape.util.BlockUtil.set;

public class LargeMurublightChanterelleFeature extends Feature<LargeMurublightChanterelleFeature.Config> {
    public LargeMurublightChanterelleFeature(Codec<Config> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        LevelAccessor level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        Config config = context.config();
        Direction direction = config.direction();

        if (direction.getAxis() != Direction.Axis.Y) {
            throw new IllegalStateException("Cannot use direction " + direction.getName() + " for " + Config.class);
        }

        BlockPos attached = origin.relative(direction.getOpposite());

        if (level.getBlockState(attached).isFaceSturdy(level, attached, direction)) {
            for (int i = 0; i < config.tries(); i++) {
                int height = config.height().sample(random);

                if (isEnoughAir(level, origin, direction, height)) {
                    return generate(level, origin, random, config, height);
                }
            }
        }

        return false;
    }

    protected boolean generate(LevelAccessor level, BlockPos origin, RandomSource random, LargeMurublightChanterelleFeature.Config config, int height) {
        Direction direction = config.direction();

        if (level.getBlockState(origin.relative(direction.getOpposite())).is(EnderscapeBlocks.CORRUPT_OVERGROWTH)) {
            set(level, origin.relative(direction.getOpposite()), EnderscapeBlocks.MIRESTONE.defaultBlockState());
        }

        set(level, origin, Blocks.AIR.defaultBlockState());
        height--;

        generateCap(level, origin.relative(direction, height), direction, config.cap_radius().sample(random), 1);
        generateMiniCaps(level, origin, random, config, height);

        for (int a = 0; a < height; a++) set(level, origin.relative(direction, a), EnderscapeBlocks.MURUBLIGHT_STEM.defaultBlockState().setValue(RotatedPillarBlock.AXIS, direction.getAxis()));
        for (int b = 0; b < height; b++) if (b % 4 == 0 && b > 0) generateMurublightShelves(level, origin.relative(direction, b), random, config);

        return true;
    }

    protected void generateCap(LevelAccessor level, BlockPos pos, Direction direction, int radius, float rounding) {
        for (int x = -radius + 1; x < radius; x++) {
            for (int z = -radius + 1; z < radius; z++) {
                double distance = Math.sqrt(x * x + z * z);
                if (distance <= radius * rounding) {
                    var y = direction.getAxis() != Direction.Axis.Y ? 1 : 0;

                    int offset = 0;
                    if (radius > 4 && distance >= radius * 0.3F) offset++;
                    if (radius > 3 && distance >= radius * 0.7F) offset++;

                    var pos2 = pos.offset(x, y, z).relative(direction, -offset);
                    replace(level, pos2, EnderscapeBlocks.MURUBLIGHT_CAP.defaultBlockState());
                }
            }
        }
    }

    protected void generateMiniCaps(LevelAccessor level, BlockPos pos, RandomSource random, LargeMurublightChanterelleFeature.Config config, int height) {
        Direction direction = config.direction();

        var mutable = pos.mutable().move(direction.getOpposite());
        var lastX = 0;
        var lastZ = 0;

        for (var i = 0; i < height / 4; i++) {
            mutable.move(direction, 4);

            for (int e = 0; e < 8; e++) {
                var x = Mth.nextInt(random, -1, 1);
                var z = Mth.nextInt(random, -1, 1);

                if (x == 0 || z == 0 || (x == lastX && z == lastZ)) continue;

                generateCap(level, mutable.offset(x, 0, z), direction, 3, 0.7F);

                lastX = x;
                lastZ = z;

                break;
            }
        }
    }

    protected void generateMurublightShelves(LevelAccessor level, BlockPos pos, RandomSource random, LargeMurublightChanterelleFeature.Config config) {
        Direction direction = config.direction();

        for (var dir2 : Direction.values()) {
            if (dir2.getAxis() == direction.getAxis()) continue;

            for (int i = -2; i < 2; i++) {
                var pos2 = pos.relative(dir2).offset(0, i, 0);
                var state = EnderscapeBlocks.MURUBLIGHT_BRACKET.defaultBlockState().setValue(MurublightBracketBlock.FACING, dir2);

                if (state.canSurvive(level, pos2) && level.isEmptyBlock(pos2)) if (random.nextFloat() >= 0.45F) replace(level, pos2, state);
            }
        }
    }

    protected boolean isEnoughAir(LevelAccessor level, BlockPos start, Direction direction, int height) {
        for (int i = 1; i < height + 1; i++) if (!level.isEmptyBlock(start.relative(direction, i))) return false;
        return height % 4 == 0;
    }

    public record Config(Direction direction, IntProvider height, IntProvider cap_radius, int tries) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                (Direction.CODEC.fieldOf("direction")).forGetter(config -> config.direction),
                (IntProviders.codec(1, 64).fieldOf("height")).forGetter(config -> config.height),
                (IntProviders.codec(1, 64).fieldOf("cap_radius")).forGetter(config -> config.cap_radius),
                (Codec.intRange(1, 64).fieldOf("tries")).forGetter(config -> config.tries))
                .apply(instance, Config::new));

        public static final Config DEFAULT = Config.defaultFacing(Direction.DOWN);

        public static Config defaultFacing(Direction direction) {
            return new Config(direction, UniformInt.of(15, 30), UniformInt.of(4, 6), 32);
        }
    }
}