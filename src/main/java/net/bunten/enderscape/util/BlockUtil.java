package net.bunten.enderscape.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockUtil extends net.minecraft.BlockUtil {

    public static boolean place(LevelAccessor level, BlockPos pos, BlockState state) {
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

    public static VoxelShape createRotatedShape(double x, double y, double z, double x2, double y2, double z2, Direction direction) {
        double e = 16 - y2;
        return switch (direction) {
            case WEST -> Block.box(y + e, x, z, y2 + e, x2, z2);
            case EAST -> Block.box(y, z, x, y2, z2, x2);
            case NORTH -> Block.box(x, z, y + e, x2, z2, y2 + e);
            case SOUTH -> Block.box(x, z, y, x2, z2, y2);
            case DOWN -> Block.box(x, y + e, z, x2, y2 + e, z2);
            default -> Block.box(x, y, z, x2, y2, z2);
        };
    }

    public static BlockPos random(BlockPos pos, RandomSource random, int x, int y, int z) {
        return pos.offset(Mth.randomBetweenInclusive(random, -x, x), Mth.randomBetweenInclusive(random, -y, y), Mth.randomBetweenInclusive(random, -z, z));
    }

    public static boolean hasTerrainDepth(LevelAccessor level, BlockPos origin, int depth, Direction direction) {
        BlockPos.MutableBlockPos mutable = origin.mutable();

        while (depth > 0) {
            depth--;
            mutable.move(direction);

            if (!level.getBlockState(mutable).isSolidRender(level, mutable)) return false;
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
}