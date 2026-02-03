package net.bunten.enderscape.feature;

import net.bunten.enderscape.block.BlisteredMagniaBlock;
import net.bunten.enderscape.block.MagniaSproutBlock;
import net.bunten.enderscape.block.state.StateProperties;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.bunten.enderscape.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.HashSet;
import java.util.Set;

public class MagniaArchFeature extends Feature<NoneFeatureConfiguration> {

    public MagniaArchFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    private static final int RADIUS = 2;

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        RandomSource random = context.random();
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();

        Set<BlockPos> mains = new HashSet<>();
        Set<BlockPos> decorations = new HashSet<>();

        int maxDistance = UniformInt.of(3, 7).sample(random);
        if (maxDistance <= 4 && random.nextInt(3) == 0) {
            maxDistance *= 2;
        }

        BlockPos leftSide = new BlockPos(origin.getX() - maxDistance, origin.getY(), origin.getZ());
        BlockPos rightSide = new BlockPos(origin.getX() + maxDistance, origin.getY(), origin.getZ());

        if (!BlockUtil.hasTerrainDepth(level, leftSide, 12, Direction.DOWN)) return false;
        if (!BlockUtil.hasTerrainDepth(level, rightSide, 12, Direction.DOWN)) return false;

        calculateArch(random, origin, level, mains, maxDistance);
        placeArch(mains, RADIUS, random, level, decorations, origin);
        placeMagniaSprouts(decorations, random, level);

        return true;
    }

    private void calculateArch(RandomSource random, BlockPos origin, WorldGenLevel level, Set<BlockPos> mains, int maxDistance) {
        int windDirection = random.nextBoolean() ? -1 : 1;
        boolean weirdShape = random.nextInt(3) == 0;

        for (float angle = 0; angle < Mth.PI * 2; angle += 0.1F) {
            BlockPos pos = BlockPos.containing(origin.getX() + (windDirection * Mth.sin(angle) * maxDistance), origin.getY() + (Mth.cos(angle) * 2 * maxDistance), origin.getZ() - ((weirdShape ? Mth.cos(angle) : Mth.sin(angle)) * maxDistance)).below((angle > -2 && angle <= 2 && maxDistance > 4) ? 4 : 0);

            if (level.getBlockState(pos).canBeReplaced()) mains.add(pos);
        }
    }

    private void placeArch(Set<BlockPos> mains, int radius, RandomSource random, WorldGenLevel level, Set<BlockPos> decorations, BlockPos origin) {
        mains.forEach(mainPos -> {
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        BlockPos pos = mainPos.offset(x, y, z);
                        if (x * x + y * y + z * z <= UniformInt.of(radius, radius * radius).sample(random) && level.getBlockState(pos).canBeReplaced()) {
                            level.setBlock(pos, EnderscapeBlocks.ALLURING_MAGNIA.get().defaultBlockState(), radius);
                            decorations.add(pos);

                            tryReplaceFloor(level, pos, random);
                        }
                    }
                }
            }
        });
    }

    private void placeMagniaSprouts(Set<BlockPos> decorations, RandomSource random, WorldGenLevel level) {
        decorations.forEach(pos -> {
            for (Direction dir : Direction.values()) {
                BlockPos relative = pos.relative(dir);

                if (random.nextInt(15) == 0 && level.getBlockState(pos).is(EnderscapeBlocks.ALLURING_MAGNIA.get()) && level.getBlockState(relative).canBeReplaced() && level.getBlockState(pos.relative(dir, 2)).canBeReplaced()) {
                    if (random.nextInt(200) == 0) {
                        BlockState state = EnderscapeBlocks.BLISTERED_MAGNIA.get().defaultBlockState().setValue(StateProperties.OPTIONAL_MAGNIA_POLARITY, BlisteredMagniaBlock.selectPolarity(level, relative));
                        level.setBlock(relative, state, 2);
                        level.scheduleTick(relative, state.getBlock(), 1);
                    } else {
                        level.setBlock(relative, EnderscapeBlocks.ALLURING_MAGNIA_SPROUT.get().defaultBlockState().setValue(MagniaSproutBlock.FACING, dir), 2);
                    }
                }
            }
        });
    }

    private void tryReplaceFloor(WorldGenLevel level, BlockPos origin, RandomSource random) {
        float radius = Mth.randomBetween(random, 3.0F, 6.0F);

        for (float y = -radius; y <= radius; y++) {
            for (float x = -radius; x <= radius; x++) {
                for (float z = -radius; z <= radius; z++) {
                    BlockPos offset = origin.offset((int) x, (int) y, (int) z);
                    float distance = (float) Math.sqrt(x * x + y * y + z * z);

                    if (distance <= (radius + Mth.nextFloat(random, -2.5F, 0.5F)) && level.getBlockState(offset).is(EnderscapeBlockTags.MAGNIA_ARCH_REPLACEABLE)) {
                        level.setBlock(offset, EnderscapeBlocks.ALLURING_MAGNIA.get().defaultBlockState(), 2);
                    }
                }
            }
        }
    }
}