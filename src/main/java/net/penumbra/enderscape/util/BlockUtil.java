package net.penumbra.enderscape.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.LinkedHashMap;
import java.util.function.Predicate;

public class BlockUtil extends net.minecraft.util.BlockUtil {

    public static boolean set(LevelAccessor level, BlockPos pos, BlockState state) {
        if (!isInvulnerable(level.getBlockState(pos), level, pos)) return level.setBlock(pos, state, 2);
        return false;
    }

    public static boolean replace(LevelAccessor level, BlockPos pos, BlockState state) {
        if (canReplace(level.getBlockState(pos), level, pos)) return level.setBlock(pos, state, 2);
        return false;
    }

    public static boolean canReplace(BlockState state, BlockGetter level, BlockPos pos) {
        return !isInvulnerable(state, level, pos) && state.canBeReplaced();
    }

    public static boolean isInvulnerable(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getDestroySpeed(level, pos) < 0;
    }

    // Automatically creates rotated voxel shapes based on the original given.
    // Caches the built values into a hashmap which getShape() later retrieves from to prevent recalculating.

    public static LinkedHashMap<Direction, VoxelShape> createRotatedShapes(double x, double y, double z, double x2, double y2, double z2) {
        LinkedHashMap<Direction, VoxelShape> map = new LinkedHashMap<>();
        double e = 16 - y2;

        for (Direction direction : Direction.values()) {
            switch (direction) {
                case WEST -> map.put(direction, Block.box(y + e, x, z, y2 + e, x2, z2));
                case EAST -> map.put(direction, Block.box(y, z, x, y2, z2, x2));
                case NORTH -> map.put(direction, Block.box(x, z, y + e, x2, z2, y2 + e));
                case SOUTH -> map.put(direction, Block.box(x, z, y, x2, z2, y2));
                case DOWN -> map.put(direction, Block.box(x, y + e, z, x2, y2 + e, z2));
                default -> map.put(direction, Block.box(x, y, z, x2, y2, z2));
            }
        }

        return map;
    }

    public static BlockPos random(BlockPos pos, RandomSource random, int x, int y, int z) {
        return pos.offset(Mth.randomBetweenInclusive(random, -x, x), Mth.randomBetweenInclusive(random, -y, y), Mth.randomBetweenInclusive(random, -z, z));
    }

    public static boolean hasTerrainDepth(LevelAccessor level, BlockPos origin, int depth, Direction direction) {
        BlockPos.MutableBlockPos mutable = origin.mutable();

        while (depth > 0) {
            depth--;
            mutable.move(direction);

            if (!level.getBlockState(mutable).isSolidRender()) return false;
        }

        return depth == 0;
    }

    public static boolean isBlockObstructed(Level level, BlockPos pos) {
        int i = 0;
        for (var dir : Direction.values()) {
            var pos2 = pos.relative(dir);
            if (level.getBlockState(pos2).canOcclude()) {
                i++;
                if (i == 6) return true;
            }
        }
        return false;
    }

    public static boolean hasAirForPath(LevelReader level, BlockPos pos, Direction direction) {
        BlockState relative = level.getBlockState(pos.relative(direction));

        return !relative.isSolid() || relative.getBlock() instanceof FenceGateBlock;
    }

    public static boolean hasAirAbove(LevelReader level, BlockPos pos, Direction direction) {
        BlockState state = level.getBlockState(pos);
        BlockState relative = level.getBlockState(pos.relative(direction));

        return LightEngine.getLightDampeningInto(state, relative, direction, relative.getLightDampening()) < 15;
    }

    public static void replaceSpherically(Level level, BlockPos pos, BlockState state, int radius, Direction direction, Predicate<BlockState> replaceable) {
        for (int x = -radius + 1; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                for (int z = -radius + 1; z < radius; z++) {

                    BlockPos offset = pos.offset(x, y, z);
                    boolean canReplace = canReplace(level, offset, direction, replaceable);

                    if (Mth.sqrt(x * x + y * y + z * z) <= radius && canReplace) {
                        if (level.getRandom().nextFloat() < 0.6F) {
                            level.setBlock(offset, state, 2);
                        }
                    }
                }
            }
        }
    }

    private static boolean canReplace(Level level, BlockPos pos, Direction direction, Predicate<BlockState> replaceable) {
        return level.getBlockState(pos.relative(direction)).propagatesSkylightDown() && replaceable.test(level.getBlockState(pos));
    }
}