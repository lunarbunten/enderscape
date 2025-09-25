package net.bunten.enderscape.feature.generator;

import net.bunten.enderscape.block.VeiledLeafPileBlock;
import net.bunten.enderscape.block.VeiledVinesBlock;
import net.bunten.enderscape.block.state.StateProperties;
import net.bunten.enderscape.feature.GrowthConfig;
import net.bunten.enderscape.feature.VeiledLeafPileConfig;
import net.bunten.enderscape.feature.VeiledTreeConfig;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.SimplexNoise;

import java.util.ArrayList;
import java.util.List;

import static net.bunten.enderscape.block.VeiledLeavesBlock.DISTANCE;
import static net.bunten.enderscape.util.BlockUtil.place;
import static net.bunten.enderscape.util.BlockUtil.replace;

public class VeiledTreeGenerator {

    public static final Block LEAVES = EnderscapeBlocks.VEILED_LEAVES;
    public static final Block LOG = EnderscapeBlocks.VEILED_LOG;

    public static boolean tryGenerate(LevelAccessor level, RandomSource random, BlockPos origin, VeiledTreeConfig config) {
        if (level.getBlockState(origin.below()).isSolidRender(level, origin.below()) && BlockUtil.hasTerrainDepth(level, origin.below(), 8, Direction.DOWN)) {
            int heightOffset = Mth.nextInt(random, 0, 2);

            generateLogs(level, origin.above(heightOffset), random, config);

            for (Direction dir : Direction.values()) if (dir.getAxis() != Direction.Axis.Y) {
                setLeaves(level, origin.above(heightOffset).relative(dir));
            }

            generateLeaves(level, origin.above(heightOffset + 1), random, config);
            if (config.leafPileConfig().isPresent()) generateLeafPiles(level, origin, random, config.leafPileConfig().get());

            return true;
        }

        return false;
    }

    private static void generateLogs(LevelAccessor level, BlockPos pos, RandomSource random, VeiledTreeConfig config) {
        BlockPos.MutableBlockPos mutable = pos.above().mutable();

        for (int i = 0; i < config.log_height().sample(random); i++) {
            setLog(level, mutable);
            mutable.move(Direction.DOWN);
        }

        for (int i = 0; i < config.branch_count().sample(random); i++) {
            generateBranch(level, mutable.mutable(), random, config);
        }
    }

    private static void generateBranch(LevelAccessor level, BlockPos.MutableBlockPos mutable, RandomSource random, VeiledTreeConfig config) {
        for (int i = 0; i < (config.branch_segments().sample(random) * random.nextInt(1, 2)); i++) {

            int x = (int) (Mth.nextInt(random, 3, 4) * (random.nextBoolean() ? 1 : -1) * calculateHorizontalFactor(random, i));
            int y = (int) (Mth.nextInt(random, -4, -6) * calculateVerticalFactor(random, i));
            int z = (int) (Mth.nextInt(random, 3, 4) * (random.nextBoolean() ? 1 : -1) * calculateHorizontalFactor(random, i));

            BlockPos end = mutable.offset(x, y, z);

            for (int o = 0; o <= 20; o++) {
                double t = (double) o / 20;

                int x1 = (int) Mth.lerp(t, mutable.getX(), end.getX());
                int y1 = (int) Mth.lerp(t, mutable.getY(), end.getY());
                int z1 = (int) Mth.lerp(t, mutable.getZ(), end.getZ());

                BlockPos current = new BlockPos(x1, y1, z1);
                setLog(level, current);
            }

            mutable.move(x, y, z);
        }
    }

    private static void generateLeaves(LevelAccessor level, BlockPos origin, RandomSource random, VeiledTreeConfig config) {
        List<BlockPos> leavesBlocks = new ArrayList<>();

        int radius = config.leaf_radius().sample(random);

        for (int x = -radius + 1; x < radius; x++) {
            for (int z = -radius + 1; z < radius; z++) {
                float distance = (float) Math.sqrt(x * x + z * z);

                if (distance <= radius) {
                    int yOffset = (int) (Math.pow(distance, 2) * 0.1F);

                    BlockPos offset = origin.offset(x, yOffset, z);
                    if (setLeaves(level, offset)) leavesBlocks.add(offset);
                    if (setLeaves(level, offset.above())) leavesBlocks.add(offset.above());

                    if (distance >= radius * 0.8F) {
                        for (int i = 1; i <= random.nextInt(3); i++) {
                            if (setLeaves(level, offset.above(i))) leavesBlocks.add(offset.above(i));
                            if (setLeaves(level, offset.above(i + 1))) leavesBlocks.add(offset.above(i + 1));
                        }
                    }
                }
            }
        }

        if (!leavesBlocks.isEmpty() && config.vineConfig().isPresent()) generateVines(level, leavesBlocks, random, config);
    }

    private static void generateVines(LevelAccessor level, List<BlockPos> leaves, RandomSource random, VeiledTreeConfig config) {
        GrowthConfig vineConfig = config.vineConfig().get();
        BlockState state = vineConfig.state();

        for (BlockPos pos : leaves.stream().filter((pos) -> level.isEmptyBlock(pos.above())).toList()) {
            if (random.nextFloat() > config.vine_generation_chance().sample(random)) continue;

            Direction direction = state.getValue(StateProperties.FACING);
            BlockPos offset = pos.relative(direction);

            if (level.isEmptyBlock(offset) && state.canSurvive(level, offset)) VeiledVinesBlock.generate(level, offset, random, vineConfig);
        }
    }

    private static void generateLeafPiles(LevelAccessor level, BlockPos origin, RandomSource random, VeiledLeafPileConfig config) {
        float radius = config.radius().sample(random);
        float noiseScale = 0.1F;

        for (float x = -radius; x <= radius; x++) {
            for (float z = -radius; z <= radius; z++) {
                BlockPos offset = origin.offset((int) x, 0, (int) z);

                float noiseValue = SimplexNoise.noise(x * noiseScale, z * noiseScale);
                float distance = (float) Math.sqrt(x * x + z * z);

                if (distance <= radius * (0.8F + noiseValue * 0.4F) && random.nextFloat() > config.density().sample(random)) {
                    BlockPos.MutableBlockPos mutable = offset.above(3).mutable();

                    for (int i = 0; i < 6; i++) {
                        Block block = EnderscapeBlocks.VEILED_LEAF_PILE;
                        if (level.isEmptyBlock(mutable) && level.isEmptyBlock(mutable.above()) && VeiledLeafPileBlock.canSurvive(level, mutable, block)) {
                            level.setBlock(mutable, block.defaultBlockState().setValue(VeiledLeafPileBlock.LAYERS, config.layers().sample(random)), 2);
                        }
                        mutable.move(Direction.DOWN);
                    }
                }
            }
        }
    }

    private static boolean setLeaves(LevelAccessor level, BlockPos pos) {
        return replace(level, pos, LEAVES.defaultBlockState().setValue(DISTANCE, 1));
    }

    private static boolean setLog(LevelAccessor level, BlockPos pos) {
        return place(level, pos, LOG.defaultBlockState());
    }

    private static float calculateVerticalFactor(RandomSource random, int g) {
        return g % 2 == 0 ? Mth.nextFloat(random, 0.8F, 1.2F) : Mth.nextFloat(random, 0.4F, 0.6F);
    }

    private static float calculateHorizontalFactor(RandomSource random, int index) {
        return index % 2 == 0 ? Mth.nextFloat(random, 0.5F, 0.6F) : Mth.nextFloat(random, 0.9F, 1.2F);
    }
}