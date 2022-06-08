package net.bunten.enderscape.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;

public class MathUtil extends MathHelper {
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

    public static Vec3d vec3d(BlockPos pos) {
        return new Vec3d(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Vec3f vec3f(BlockPos pos) {
        return new Vec3f(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Vec3i vec3i(BlockPos pos) {
        return new Vec3i(pos.getX(), pos.getY(), pos.getZ());
    }

    public static BlockPos blockPos(Vec3d pos) {
        return new BlockPos(pos.getX(), pos.getY(), pos.getZ());
    }
}