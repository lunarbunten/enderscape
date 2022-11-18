package net.bunten.enderscape.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockUtil extends net.minecraft.BlockUtil {

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

    public static boolean hasTerrainDepth(LevelAccessor world, BlockPos origin, int depth, Direction direction) {
        MutableBlockPos mutable = origin.mutable();

        while (depth > 0) {
            depth--;
            mutable.move(direction);

            if (!world.getBlockState(mutable).isSolidRender(world, mutable)) return false;
        }

        return depth == 0;
    }

    public static BlockPos random(BlockPos pos, RandomSource random, int x, int y, int z) {
        return pos.offset(Mth.randomBetweenInclusive(random, -x, x), Mth.randomBetweenInclusive(random, -y, y), Mth.randomBetweenInclusive(random, -z, z));
    }
}