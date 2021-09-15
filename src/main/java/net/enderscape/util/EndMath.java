package net.enderscape.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class EndMath extends MathHelper {

    public static BlockPos random(BlockPos pos, Random random, int x, int z) {
        return pos.add(nextInt(random, -x, x), 0, nextInt(random, -z, z));
    }

    public static BlockPos random(BlockPos pos, Random random, int x, int y, int z) {
        return pos.add(nextInt(random, -x, x), nextInt(random, -y, y), nextInt(random, -z, z));
    }

    public static boolean inRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    public static boolean inRange(float value, float min, float max) {
        return value >= min && value <= max;
    }

    public static boolean inRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

    public static boolean inAverage(int value, int average, int range) {
        return value >= average - range && value <= average + range;
    }
}