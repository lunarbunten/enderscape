package net.bunten.enderscape.world.generator;

import org.betterx.bclib.util.BlocksHelper;

import net.bunten.enderscape.blocks.AbstractVineBlock;
import net.bunten.enderscape.blocks.properties.Stage;
import net.bunten.enderscape.blocks.properties.StateProperties;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.util.BlockUtil;
import net.bunten.enderscape.util.MathUtil;
import net.bunten.enderscape.util.States;
import net.bunten.enderscape.world.features.vegetation.LargeCelestialFungusFeatureConfig;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class LargeCelestialFungusGenerator {

    protected static void place(LevelAccessor world, BlockPos pos, BlockState state) {
        BlockState state2 = world.getBlockState(pos);
        if (!state2.isAir() && BlocksHelper.isInvulnerable(state2, world, pos)) return;
        world.setBlock(pos, state, 2);
    }

    public static boolean tryGenerate(LevelAccessor world, RandomSource random, BlockPos pos, LargeCelestialFungusFeatureConfig config) {
        for (int i = 0; i < config.tries(); i++) {
            int height = config.height().sample(random);
            int radius = (int) (height / config.cap_radius_division());

            if (isEnoughAir(world, pos, height, radius / 2)) {
                generate(world, random, pos, config, height, radius);
                return true;
            }
        }

        return false;
    }

    public static boolean isEnoughAir(LevelAccessor world, BlockPos pos, int height, int radius) {
        for (int i = 1; i < height + 1; i++) {
            if (!world.isEmptyBlock(pos.above(i))) {
                return false;
            }
        }
        return true;
    }

    protected static void generateVines(LevelAccessor world, RandomSource random, BlockPos origin, LargeCelestialFungusFeatureConfig config, int radius) {
        int generatedVines = 0;

        for (int b = 0; b < config.vine_generation_tries(); b++) {
            final var plantable = EnderscapeBlocks.PLANTABLE_FLANGER_BERRY_VINE;

            var yOffset = MathUtil.nextInt(random, 0, (int) (radius * 0.4F));
            var pos = BlockUtil.random(origin, random, (int) (radius * 0.8F), 0, (int) (radius * 0.8F)).below(yOffset);

            // Attempt to find a safe position to generate
            for (int u = -4; u < 4; u++) {
                var pos2 = pos.above(u);

                if (world.getBlockState(pos2).isAir() && world.getBlockState(pos2.above()).is(plantable)) {
                    pos = pos2;
                    break;
                } else {
                    continue;
                }
            }

            // Actual vine generation
            if (pos.closerThan(origin, (int) (radius * 0.6F)) || generatedVines > radius / 4 && random.nextFloat() <= config.excess_vine_discard_chance()) { 
                continue;
            } else if (world.isEmptyBlock(pos) && world.isEmptyBlock(pos.below()) && world.getBlockState(pos.above()).is(plantable)) {
                int length = MathUtil.nextInt(random, (int) (radius * 0.8F), (int) (radius * 2));
                MutableBlockPos mutable = pos.mutable();

                for (int i = 0; i <= length; i++) {
                    if (world.isEmptyBlock(mutable)) {
                        if (i == length || !world.isEmptyBlock(mutable.below())) {
                            place(world, mutable, States.FLANGER_BERRY.setValue(StateProperties.BERRY_STAGE, Util.getRandom(Stage.values(), random)));
                            break;
                        } else {
                            place(world, mutable, States.FLANGER_VINE.setValue(StateProperties.ATTACHED, true).setValue(AbstractVineBlock.AGE, AbstractVineBlock.MAX_AGE));
                        }

                        mutable.move(Direction.DOWN);
                    }
                }
                
                generatedVines++;
            }
        }
    }

    protected static void generateCap(LevelAccessor world, RandomSource random, BlockPos pos, LargeCelestialFungusFeatureConfig config, int radius) {
        for (int x = -radius + 1; x < radius; x++) {
            for (int z = -radius + 1; z < radius; z++) {
                double distance = Math.sqrt(x * x + z * z);

                if (distance <= radius) {
                    for (int capOrder = 1; capOrder < radius; capOrder++) {
                        int droopAmount = distance >= radius * config.cap_droop_percentage() && capOrder == 1 && radius > 3 ? 1 : 0;
                        place(world, pos.offset(x / capOrder, -(2 * capOrder + droopAmount) + 2, z / capOrder), States.CELESTIAL_CAP);
                    }
                }
            }
        }

        generateVines(world, random, pos.below(), config, radius);
    }

    public static void generate(LevelAccessor world, RandomSource random, BlockPos pos, LargeCelestialFungusFeatureConfig config, int height, int radius) {
        world.setBlock(pos, States.AIR, 4);
        place(world, pos.below(), States.END_STONE);
        generateCap(world, random, pos.above(height), config, radius);
        for (int a = 0; a < height; a++) {
            place(world, pos.above(a), States.CELESTIAL_STEM);
        }
    }
}