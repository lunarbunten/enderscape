package net.bunten.enderscape.feature.generator;

import net.bunten.enderscape.block.MurublightShelfBlock;
import net.bunten.enderscape.feature.LargeMurublightChanterelleConfig;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;

import static net.bunten.enderscape.util.BlockUtil.place;
import static net.bunten.enderscape.util.BlockUtil.replace;

public class LargeMurublightChanterelleGenerator {

    public static boolean tryGenerate(LevelAccessor level, RandomSource random, BlockPos origin, LargeMurublightChanterelleConfig config) {
        Direction direction = config.direction();

        if (direction.getAxis() != Axis.Y) {
            throw new IllegalStateException("Cannot use direction " + direction.getName() + " for " + LargeMurublightChanterelleConfig.class);
        }

        BlockPos attached = origin.relative(direction.getOpposite());

        if (level.getBlockState(attached).isFaceSturdy(level, attached, direction)) {
            for (int i = 0; i < config.tries(); i++) {
                int height = config.height().sample(random);

                if (isEnoughAir(level, origin, direction, height)) return generate(level, origin, random, config, height);
            }
        }

        return false;
    }

    public static boolean generate(LevelAccessor level, BlockPos origin, RandomSource random, LargeMurublightChanterelleConfig config, int height) {
        Direction direction = config.direction();

        if (level.getBlockState(origin.relative(direction.getOpposite())).is(EnderscapeBlocks.CORRUPT_OVERGROWTH.get())) {
            place(level, origin.relative(direction.getOpposite()), EnderscapeBlocks.MIRESTONE.get().defaultBlockState());
        }

        place(level, origin, Blocks.AIR.defaultBlockState());
        height--;

        generateCap(level, origin.relative(direction, height), direction, config.cap_radius().sample(random), 1);
        generateMiniCaps(level, origin, random, config, height);

        for (int a = 0; a < height; a++) place(level, origin.relative(direction, a), EnderscapeBlocks.MURUBLIGHT_STEM.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, direction.getAxis()));
        for (int b = 0; b < height; b++) if (b % 4 == 0 && b > 0) generateMurublightShelves(level, origin.relative(direction, b), random, config);

        return true;
    }

    protected static void generateCap(LevelAccessor level, BlockPos pos, Direction direction, int radius, float rounding) {
        for (int x = -radius + 1; x < radius; x++) {
            for (int z = -radius + 1; z < radius; z++) {
                double distance = Math.sqrt(x * x + z * z);
                if (distance <= radius * rounding) {
                    var y = direction.getAxis() != Axis.Y ? 1 : 0;

                    int offset = 0;
                    if (radius > 4 && distance >= radius * 0.3F) offset++;
                    if (radius > 3 && distance >= radius * 0.7F) offset++;
                    
                    var pos2 = pos.offset(x, y, z).relative(direction, -offset);
                    replace(level, pos2, EnderscapeBlocks.MURUBLIGHT_CAP.get().defaultBlockState());
                }
            }
        }
    }

    protected static void generateMiniCaps(LevelAccessor level, BlockPos pos, RandomSource random, LargeMurublightChanterelleConfig config, int height) {
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

    protected static void generateMurublightShelves(LevelAccessor level, BlockPos pos, RandomSource random, LargeMurublightChanterelleConfig config) {
        Direction direction = config.direction();

        for (var dir2 : Direction.values()) {
            if (dir2.getAxis() == direction.getAxis()) continue;

            for (int i = -2; i < 2; i++) {
                var pos2 = pos.relative(dir2).offset(0, i, 0);
                var state = EnderscapeBlocks.MURUBLIGHT_BRACKET.get().defaultBlockState().setValue(MurublightShelfBlock.FACING, dir2);

                if (state.canSurvive(level, pos2) && level.isEmptyBlock(pos2)) if (random.nextFloat() >= 0.45F) replace(level, pos2, state);
            }
        }
    }

    public static boolean isEnoughAir(LevelAccessor level, BlockPos start, Direction direction, int height) {
        for (int i = 1; i < height + 1; i++) if (!level.isEmptyBlock(start.relative(direction, i))) return false;
        return height % 4 == 0;
    }
}