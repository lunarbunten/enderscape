package net.bunten.enderscape.world.features;

import org.betterx.bclib.noise.OpenSimplexNoise;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.blocks.GrowthBlock;
import net.bunten.enderscape.blocks.MurushroomsBlock;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.MathUtil;
import net.bunten.enderscape.util.PlantUtil;
import net.bunten.enderscape.util.States;
import net.bunten.enderscape.world.features.vegetation.GrowthConfig;
import net.bunten.enderscape.world.features.vegetation.MurushroomFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class CelestialIslandFeature extends Feature<CelestialIslandFeatureConfig> {
    public CelestialIslandFeature(Codec<CelestialIslandFeatureConfig> codec) {
        super(codec);
    }

    private final GrowthConfig GROWTH_CONFIG = new GrowthConfig(States.CELESTIAL_GROWTH.setValue(GrowthBlock.FACING, Direction.UP), ConstantInt.of(1), UniformInt.of(1, 2), 0.5F);
    private final OpenSimplexNoise NOISE = new OpenSimplexNoise(1512);

    protected BlockState getTopBlockState(BlockPos pos, RandomSource random) {
        double value = NOISE.eval(pos.getX() * 0.2, pos.getZ() * 0.2) + (random.nextFloat() * 0.12F) * 4;
        if (value > 0.42F) {
            return States.END_STONE;
        } else {
            return States.CELESTIAL_MYCELIUM;
        }
    }

    protected void createIsland(WorldGenLevel world, RandomSource random, BlockPos origin, CelestialIslandFeatureConfig config) {
        int initialWidth = config.width().sample(random);
        int initialHeight = config.height().sample(random);

        double width = initialWidth;
        double height = initialHeight;

        float murushroom_chance = config.murushroom_chance();
        int y = 0;

        while (height > 0.5F) {
            for (int x = MathUtil.floor(-width); x <= MathUtil.ceil(width); ++x) {
                for (int z = MathUtil.floor(-width); z <= MathUtil.ceil(width); ++z) {
                    if ((x * x + z * z) <= (width + 1) * (width + 1) && width > 0.35F) {
                        var pos2 = origin.offset(x, y, z);
                        var state = y == 0 ? getTopBlockState(pos2, random) : States.END_STONE;
                        setBlock(world, pos2, state);
                    }
                }
            }

            width -= random.nextFloat() * 0.3F + 0.4F;
            height -= random.nextFloat() * 2F + 0.2F;

            y--;
        }

        if (random.nextFloat() <= murushroom_chance) {
            createMurushrooms(world, random, origin, initialWidth, initialHeight);
        }

        for (BlockPos pos : BlockPos.randomInCube(random, initialWidth * 20, origin, initialWidth)) {
            if (!world.getBlockState(pos).is(EnderscapeBlocks.CELESTIAL_GROWTH) && world.isEmptyBlock(pos.above()) && States.CELESTIAL_GROWTH.canSurvive(world, pos.above()))
            PlantUtil.generateGrowth(world, random, pos.above(), GROWTH_CONFIG);
        }

        for (BlockPos pos : BlockPos.randomInCube(random, initialWidth * 10, origin, initialWidth)) {
            if (world.isEmptyBlock(pos) && States.CELESTIAL_FUNGUS.canSurvive(world, pos))
            world.setBlock(pos, States.CELESTIAL_FUNGUS, 2);
        }

        for (BlockPos pos : BlockPos.randomInCube(random, initialWidth * 3, origin, initialWidth)) {
            if (world.isEmptyBlock(pos) && States.BULB_FLOWER.canSurvive(world, pos))
            world.setBlock(pos, States.BULB_FLOWER, 2);
        }
    }

    protected void createMurushrooms(WorldGenLevel world, RandomSource random, BlockPos pos, int width, int height) {
        var murushroomConfig = new MurushroomFeatureConfig(width, height, MurushroomsBlock.MAX_AGE, 70);
        PlantUtil.generateMurushrooms(world, pos.offset(0, height / 2, 0), random, murushroomConfig);
        
        for (int i = 0; i < 16; i++) {
            var xOffset = MathUtil.nextInt(random, -width, width);
            var zOffset = MathUtil.nextInt(random, -width, width);

            var size = (int) MathUtil.nextFloat(random, width * 0.75F, width);
            var murushroomHeight = 2 + MathUtil.nextInt(random, width / 6, width / 3);

            var pos2 = pos.offset(xOffset, -murushroomHeight, zOffset);

            if ((xOffset * xOffset + zOffset + zOffset) > width * 1.5F && width > 3) {
                PlantUtil.generateLargeMurushroom(world, random, pos2, size);
                break;
            }
        }
    }

    @Override
    public boolean place(FeaturePlaceContext<CelestialIslandFeatureConfig> context) {
        var config = context.config();

        WorldGenLevel world = context.level();
        RandomSource random = context.random();
        BlockPos pos = context.origin();

        createIsland(world, random, pos, config);

        return true;
    }
}