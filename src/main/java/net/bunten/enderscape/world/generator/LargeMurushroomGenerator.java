package net.bunten.enderscape.world.generator;

import org.betterx.bclib.util.BlocksHelper;

import net.bunten.enderscape.blocks.MurushroomsBlock;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.MathUtil;
import net.bunten.enderscape.util.States;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

public class LargeMurushroomGenerator {

    protected static void place(LevelAccessor world, BlockPos pos, BlockState state) {
        BlockState state2 = world.getBlockState(pos);
        if (!state2.isAir() && BlocksHelper.isInvulnerable(state2, world, pos)) return;
        world.setBlock(pos, state, 2);
    }

    public static boolean generate(Direction direction, LevelAccessor world, RandomSource random, BlockPos pos, int height, int capRadius, int tries) {
        for (int i = 0; i < tries; i++) {
            if (isEnoughAir(direction, world, pos, height)) {
                generate(direction, world, random, pos, height, capRadius);
                return true;
            }
        }

        return false;
    }

    public static boolean isEnoughAir(Direction direction, LevelAccessor world, BlockPos start, int height) {
        for (int i = 1; i < height + 1; i++) {
            if (!world.isEmptyBlock(start.relative(direction, i))) {
                return false;
            }
        }
        return height % 4 == 0;
    }

    protected static void generateCap(Direction direction, LevelAccessor world, RandomSource random, BlockPos pos, int capRadius, float capRounding) {
        for (int x = -capRadius + 1; x < capRadius; x++) {
            for (int z = -capRadius + 1; z < capRadius; z++) {
                double distance = Math.sqrt(x * x + z * z);
                if (distance <= capRadius * capRounding) {
                    var y = direction.getAxis() != Axis.Y ? 1 : 0;

                    int offset = 0;
                    if (capRadius > 4 && distance >= capRadius * 0.3F) offset++;
                    if (capRadius > 3 && distance >= capRadius * 0.7F) offset++;
                    
                    var pos2 = pos.offset(x, y, z).relative(direction, -offset);
                    place(world, pos2, States.MURUSHROOM_CAP);
                    if (offset != 0) {
                    }
                }
            }
        }
    }

    protected static void generateMiniCaps(Direction direction, LevelAccessor world, RandomSource random, BlockPos pos, int height) {
        var mutable = pos.mutable().move(direction.getOpposite());
        var lastX = 0;
        var lastZ = 0;

        for (var i = 0; i < height / 4; i++) {
            mutable.move(direction, 4);

            for (int e = 0; e < 8; e++) {
                var x = MathUtil.nextInt(random, -1, 1);
                var z = MathUtil.nextInt(random, -1, 1);

                if (x == 0 || z == 0 || (x == lastX && z == lastZ)) continue;

                generateCap(direction, world, random, mutable.offset(x, 0, z), 3, 0.7F);

                lastX = x;
                lastZ = z;

                break;
            }
        }
    }

    protected static void generateMurushrooms(Direction direction, LevelAccessor world, RandomSource random, BlockPos pos) {
        for (var dir2 : Direction.values()) {
            if (dir2.getAxis() == direction.getAxis()) continue;

            for (int i = -2; i < 2; i++) {
                var pos2 = pos.relative(dir2).offset(0, i, 0);
                var state = States.MURUSHROOMS.setValue(MurushroomsBlock.FACING, dir2);

                if (state.canSurvive(world, pos2) && world.isEmptyBlock(pos2)) {
                    if (random.nextFloat() >= 0.45F) place(world, pos2, state);
                }
            }
        }
    }

    public static void generate(Direction direction, LevelAccessor world, RandomSource random, BlockPos origin, int height, int capRadius) {
        if (world.getBlockState(origin.relative(direction.getOpposite())).is(EnderscapeBlocks.END_MYCELIUM_BLOCKS)) {
            world.setBlock(origin.relative(direction.getOpposite()), States.END_STONE, 2);
        }

        world.setBlock(origin, States.AIR, 4);
        height--;
        
        generateCap(direction, world, random, origin.relative(direction, height), capRadius, 1);
        generateMiniCaps(direction, world, random, origin, height);

        for (int a = 0; a < height; a++) {
            var pos = origin.relative(direction, a);

            place(world, pos, States.MURUSHROOM_STEM.setValue(RotatedPillarBlock.AXIS, direction.getAxis()));
        }

        for (int b = 0; b < height; b++) {
            var pos = origin.relative(direction, b);
            if (b % 4 == 0 && b > 0) {
                generateMurushrooms(direction, world, random, pos);
            }
        }
    }
}