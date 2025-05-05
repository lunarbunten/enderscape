package net.bunten.enderscape.feature.generator;

import net.bunten.enderscape.block.AbstractVineBlock;
import net.bunten.enderscape.block.properties.StateProperties;
import net.bunten.enderscape.feature.LargeCelestialChanterelleConfig;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.bunten.enderscape.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;

import static net.bunten.enderscape.util.BlockUtil.place;
import static net.bunten.enderscape.util.BlockUtil.replace;

public class LargeCelestialChanterelleGenerator {

    public static final WeightedStateProvider FLANGER_BERRIES = new WeightedStateProvider(WeightedList.<BlockState>builder()
            .add(EnderscapeBlocks.FLANGER_BERRY_FLOWER.defaultBlockState(), 1)
            .add(EnderscapeBlocks.UNRIPE_FLANGER_BERRY_BLOCK.defaultBlockState(), 1)
            .add(EnderscapeBlocks.RIPE_FLANGER_BERRY_BLOCK.defaultBlockState(), 1)
    );

    public static boolean tryGenerate(LevelAccessor level, BlockPos origin, RandomSource random, LargeCelestialChanterelleConfig config) {
        Direction direction = Direction.UP;
        BlockPos attached = origin.relative(direction.getOpposite());

        if (level.getBlockState(attached).isFaceSturdy(level, attached, direction)) {
            for (int i = 0; i < config.tries(); i++) {
                int height = config.height().sample(random);
                int radius = (int) (height / config.cap_radius_division());

                if (isEnoughAir(level, origin, height, radius / 2)) {
                    generate(level, origin, random, config, height, radius);
                    return true;
                }
            }
        }

        return false;
    }

    public static void generate(LevelAccessor level, BlockPos pos, RandomSource random, LargeCelestialChanterelleConfig config, int height, int radius) {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 4);
        place(level, pos.below(), Blocks.END_STONE.defaultBlockState());
        generateCap(level, random, pos.above(height), config, radius);

        for (int i = 0; i < height; i++) {
            place(level, pos.above(i), EnderscapeBlocks.CELESTIAL_STEM.defaultBlockState());
        }
    }

    public static boolean isEnoughAir(LevelAccessor level, BlockPos pos, int height, int radius) {
        for (int i = 1; i < height + 1; i++) {
            if (!level.isEmptyBlock(pos.above(i))) {
                return false;
            }
        }
        return true;
    }

    protected static void generateVines(LevelAccessor level, RandomSource random, BlockPos origin, LargeCelestialChanterelleConfig config, int radius) {
        int generatedVines = 0;
        int maxTries = config.vine_generation_tries();
        int maxRadius = (int) (radius * 0.8F);
        int minHeight = (int) (radius * 0.4F);

        for (int i = 0; i < maxTries; i++) {
            BlockPos pos = BlockUtil.random(origin, random, maxRadius, 0, maxRadius).below(Mth.nextInt(random, 0, minHeight));

            for (int offset = -4; offset < 4; offset++) {
                BlockPos testPos = pos.above(offset);
                if (level.getBlockState(testPos).isAir() && level.getBlockState(testPos.above()).is(EnderscapeBlockTags.FLANGER_BERRY_VINE_PLANTABLE_ON)) {
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

    private static boolean canVineGrow(LevelAccessor level, BlockPos pos) {
        return level.isEmptyBlock(pos) && level.isEmptyBlock(pos.below()) && level.getBlockState(pos.above()).is(EnderscapeBlockTags.FLANGER_BERRY_VINE_PLANTABLE_ON);
    }

    private static void generateVine(LevelAccessor level, RandomSource random, BlockPos start, int radius) {
        int length = Mth.nextInt(random, (int) (radius * 0.8F), radius * 2);
        MutableBlockPos mutable = start.mutable();

        for (int i = 0; i <= length; i++) {
            if (level.isEmptyBlock(mutable)) {
                if (i == length || !level.isEmptyBlock(mutable.below())) {
                    replace(level, mutable, FLANGER_BERRIES.getState(random, mutable));
                    break;
                } else {
                    replace(level, mutable, EnderscapeBlocks.FLANGER_BERRY_VINE.defaultBlockState().setValue(StateProperties.ATTACHED, true).setValue(AbstractVineBlock.AGE, AbstractVineBlock.MAX_AGE));
                }

                mutable.move(Direction.DOWN);
            }
        }
    }

    protected static void generateCap(LevelAccessor level, RandomSource random, BlockPos pos, LargeCelestialChanterelleConfig config, int radius) {
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
}