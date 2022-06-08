package net.bunten.enderscape.world.features;

import com.mojang.serialization.Codec;

import net.bunten.enderscape.util.MathUtil;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public abstract class AbstractIslandFeature extends Feature<DefaultFeatureConfig> {
    public AbstractIslandFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    protected abstract double widthDivider();

    protected abstract double getSize(Random random);

    protected abstract BlockState getTopBlock(WorldAccess world, BlockPos pos);

    protected abstract BlockState getBottomBlock(WorldAccess world, BlockPos pos);

    protected abstract void decorate(StructureWorldAccess world, Random random, BlockPos pos);

    protected void createIsland(StructureWorldAccess world, Random random, BlockPos pos) {
        double size = getSize(random);
        double width = widthDivider();
        for (int y = 0; size > 0.5F; --y) {
            for (int x = MathUtil.floor(-size); x <= MathUtil.ceil(size); ++x) {
                for (int z = MathUtil.floor(-size); z <= MathUtil.ceil(size); ++z) {
                    if ((x * x + z * z) <= (size + 1) * (size + 1)) {
                        BlockState state;
                        if (world.getBlockState(pos.add(x / width, y, z / width).up()).isAir()) {
                            state = getTopBlock(world, pos);
                        } else {
                            state = getBottomBlock(world, pos);
                        }
                        setBlockState(world, pos.add(x / width, y, z / width), state);
                        decorate(world, random, pos);
                    }
                }
            }
            size = size - (random.nextInt(2) + 0.5D);
        }
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos pos = context.getOrigin();

        createIsland(world, random, pos);
        return true;
    }
}