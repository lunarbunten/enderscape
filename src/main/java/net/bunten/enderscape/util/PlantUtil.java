package net.bunten.enderscape.util;

import net.bunten.enderscape.blocks.FlangerBerryBlock;
import net.bunten.enderscape.blocks.FlangerBerryVine;
import net.bunten.enderscape.blocks.MurushroomsBlock;
import net.bunten.enderscape.blocks.properties.EnderscapeProperties;
import net.bunten.enderscape.blocks.properties.FlangerBerryStage;
import net.bunten.enderscape.blocks.properties.GrowthPart;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

public class PlantUtil {

    private static final WeightedBlockStateProvider PROVIDER = new WeightedBlockStateProvider(new DataPool.Builder<BlockState>().add(States.CELESTIAL_FUNGUS, 4).add(States.BULB_FLOWER, 1));

    public static void generateCelestialMycelium(WorldAccess world, BlockPos pos, int horizontalRange, int verticalRange, float chance) {
        horizontalRange /= 2;

        world.setBlockState(pos, States.CELESTIAL_MYCELIUM, 2);
        for (int x = -horizontalRange + 1; x < horizontalRange; x++) {
            for (int y = -verticalRange; y < verticalRange; y++) {
                for (int z = -horizontalRange + 1; z < horizontalRange; z++) {
                    var pos2 = pos.add(x, y, z);
                    boolean bl = world.getBlockState(pos2.up()).isTranslucent(world, pos2) && world.getBlockState(pos2).isOf(Blocks.END_STONE);
                    if (bl && MathUtil.sqrt(x * x + y * y + z * z) <= horizontalRange) {
                        if (world.getRandom().nextFloat() < chance) {
                            world.setBlockState(pos2, States.CELESTIAL_MYCELIUM, 2);
                        }
                    }
                }
            }
        }
    }
    
    public static boolean generateCelestialGrowth(WorldAccess world, Random random, BlockPos origin, IntProvider baseHeight, IntProvider additionalHeight, float addedHeightChance, int horizontalRange, int verticalRange, int verticalCheckRange, int tries) {
        boolean result = false;

        for (int i = 0; i < tries; i++) {
            BlockPos pos = MathUtil.random(origin, random, horizontalRange, verticalRange, horizontalRange);
            
            // Tries to find acceptable area to generate
            for (int u = -verticalCheckRange; u < verticalCheckRange; u++) {
                var pos2 = pos.up(u);

                if (world.getBlockState(pos2).isAir() && world.getBlockState(pos2.down()).isIn(EnderscapeBlocks.GROWTH_PLANTABLE_OM)) {
                    pos = pos2;
                    break;
                } else {
                    continue;
                }
            }

            // Actual generation
            if (world.getBlockState(pos).isAir() && world.getBlockState(pos.down()).isIn(EnderscapeBlocks.GROWTH_PLANTABLE_OM)) {
                int totalHeight = baseHeight.get(random) + (random.nextFloat() <= addedHeightChance ? additionalHeight.get(random) : 0);
                Mutable mutable = pos.mutableCopy();

                for (int g = 0; g < totalHeight; g++) {
                    if (world.isAir(mutable)) {
                        GrowthPart growthPart;
                        if (world.getBlockState(mutable.up()).isAir()) {
                            if (g == totalHeight - 1) {
                                growthPart = GrowthPart.TOP;
                            } else if (g == 0) {
                                growthPart = GrowthPart.BOTTOM;
                            } else {
                                growthPart = GrowthPart.MIDDLE;
                            }
                        } else {
                            growthPart = GrowthPart.TOP;
                        }

                        world.setBlockState(mutable, States.CELESTIAL_GROWTH.with(EnderscapeProperties.GROWTH_PART, growthPart), 2);
                        mutable.move(Direction.UP);
                    }
                }

                result = true;
            }
        }

        return result;
    }

    public static boolean generateCelestialVegetation(WorldAccess world, Random random, BlockPos origin, int horizontalRange, int verticalRange, int verticalCheckRange, int tries) {
        boolean result = false;

        for (int i = 0; i < tries; i++) {
            var pos = MathUtil.random(origin, random, horizontalRange, verticalRange, horizontalRange);
            var state = PROVIDER.getBlockState(random, pos);

            // Tries to find acceptable area to generate
            for (int u = -verticalCheckRange; u < verticalCheckRange; u++) {
                var pos2 = pos.up(u);
        
                if (world.getBlockState(pos2).isAir() && state.canPlaceAt(world, pos2)) {
                    pos = pos2;
                 break;
                } else {
                    continue;
                }
            }

            if (world.isAir(pos) && state.canPlaceAt(world, pos)) {
                world.setBlockState(pos, state, 2);
                result = true;
            }
        }

        return result;
    }

    public static boolean generateMurushrooms(WorldAccess world, BlockPos pos, Random random, int age, int horizontalRange, int verticalRange, int tries) {
        int p = 0;

        for (int i = 0; i < tries; i++) {
            BlockPos pos2 = MathUtil.random(pos, random, horizontalRange, verticalRange, horizontalRange);

            if (world.getBlockState(pos2).isAir()) {
                for (var dir : Direction.values()) {
                    if (dir.getAxis() == Axis.Y) continue;
        
                    BlockPos wall = pos2.offset(dir);
    
                    if (world.getBlockState(wall).isOpaqueFullCube(world, wall)) {
                        world.setBlockState(pos2, States.MURUSHROOMS.with(MurushroomsBlock.FACING, dir.getOpposite()).with(MurushroomsBlock.AGE, age), 2);
                        p++;
    
                        break;
                    }
                }
            }
        }
        
        return p > 0;
    }

    public static boolean generateDefaultLargeCelestialFungus(WorldAccess world, Random random, BlockPos pos) {
        return generateLargeCelestialFungus(world, random, pos, UniformIntProvider.create(10, 35).get(random), 4, 1, 0.75F, 1, 64, 16);
    }

    public static boolean generateLargeCelestialFungus(WorldAccess world, Random random, BlockPos pos, int height, float capRadiusDivision, float stemCapDivision, float percentageForCapDrooping, float excessVineDiscardChance, int vineGenerationTries, int tries) {
        return LargeCelestialFungusGenerator.generateLargeCelestialFungus(world, random, pos, height, capRadiusDivision, stemCapDivision, percentageForCapDrooping, excessVineDiscardChance, vineGenerationTries, tries);
    }

    public static boolean generateLargeMurushroom(WorldAccess world, Random random, BlockPos pos, int size) {
        boolean result = false;

        for (int x = -size + 1; x < size; x++) {
            for (int z = -size + 1; z < size; z++) {
                if (Math.sqrt(x * x + z * z) <= size * 0.8) {
                    var pos2 = pos.add(x, 0, z);
                    if (world.getBlockState(pos2).getMaterial().isReplaceable()) {
                        world.setBlockState(pos2, States.MURUSHROOM_CAP, 2);
                        result = true;
                    }
                }
            }
        }

        return result;
    }

    protected class LargeCelestialFungusGenerator {

        protected static void place(WorldAccess world, BlockPos pos, BlockState state) {
            world.setBlockState(pos, state, 2);
        }

        public static boolean generateLargeCelestialFungus(WorldAccess world, Random random, BlockPos pos, int height, float capRadiusDivision, float stemCapDivision, float percentageForCapDrooping, float excessVineDiscardChance, int vineGenerationTries, int tries) {
            for (int i = 0; i < tries; i++) {
                if (LargeCelestialFungusGenerator.isEnoughAir(world, pos, height)) {
                    LargeCelestialFungusGenerator.generate(world, random, pos, height, capRadiusDivision, stemCapDivision, percentageForCapDrooping, excessVineDiscardChance, vineGenerationTries);
                    return true;
                }
            }
    
            return false;
        }
    
        public static boolean isEnoughAir(WorldAccess world, BlockPos start, int height) {
            for (int i = 1; i < height + 1; i++) {
                if (!world.isAir(start.up(i))) {
                    return false;
                }
            }
            return true;
        }
    
        protected static void generateVines(WorldAccess world, Random random, BlockPos origin, int range, float excessVineDiscardChance, int vineGenerationTries) {
            int generatedVines = 0;

            for (int b = 0; b < vineGenerationTries; b++) {
                final var supportBlocks = EnderscapeBlocks.FLANGER_BERRY_VINE_SUPPORT_BLOCKS;

                var yOffset = MathUtil.nextInt(random, 0, (int) (range * 0.4F));
                var pos = MathUtil.random(origin, random, (int) (range * 0.8F), 0, (int) (range * 0.8F)).down(yOffset);

                // Attempt to find a safe position to generate
                for (int u = -4; u < 4; u++) {
                    var pos2 = pos.up(u);
    
                    if (world.getBlockState(pos2).isAir() && world.getBlockState(pos2.up()).isIn(supportBlocks)) {
                        pos = pos2;
                        break;
                    } else {
                        continue;
                    }
                }
    
                // Actual vine generation
                if (pos.isWithinDistance(origin, (int) (range * 0.6F)) || generatedVines > range / 4 && random.nextFloat() <= excessVineDiscardChance) { 
                    continue;
                } else if (world.isAir(pos) && world.isAir(pos.down()) && world.getBlockState(pos.up()).isIn(supportBlocks)) {
                    int length = MathUtil.nextInt(random, (int) (range * 0.8F), (int) (range * 2));
                    Mutable mutable = pos.mutableCopy();
    
                    for (int i = 0; i <= length; i++) {
                        if (world.isAir(mutable)) {
                            if (i == length || !world.isAir(mutable.down())) {
                                BlockState state = States.FLANGER_BERRY;
                                state = state.with(FlangerBerryBlock.STAGE, Util.getRandom(FlangerBerryStage.values(), random));
                                place(world, mutable, state);
                                
                                break;
                            } else {
                                place(world, mutable, States.FLANGER_VINE.with(Properties.ATTACHED, true).with(FlangerBerryVine.AGE, FlangerBerryVine.MAX_AGE));
                            }
    
                            mutable.move(Direction.DOWN);
                        }
                    }
                    
                    generatedVines++;
                }
            }
        }
    
        protected static void generateCap(WorldAccess world, Random random, BlockPos pos, int radius, float stemCapDivision, float percentageForCapDrooping, float excessVineDiscardChance, int vineGenerationTries) {
            for (int x = -radius + 1; x < radius; x++) {
                for (int z = -radius + 1; z < radius; z++) {
                    double distance = Math.sqrt(x * x + z * z);

                    if (distance <= radius) {
                        for (int capOrder = 1; capOrder < radius; capOrder++) {
                            int droopAmount = distance >= radius * percentageForCapDrooping && capOrder == 1 && radius > 3 ? 1 : 0;
                            place(world, pos.add(x / capOrder, -(2 * capOrder + droopAmount) + 2, z / capOrder), States.CELESTIAL_CAP);
                        }
                    }
                }
            }

            generateVines(world, random, pos.down(), radius, excessVineDiscardChance, vineGenerationTries);
        }
    
        public static void generate(WorldAccess world, Random random, BlockPos pos, int height, float capRadiusDivision, float stemCapDivision, float percentageForCapDrooping, float excessVineDiscardChance, int vineGenerationTries) {
            world.setBlockState(pos, States.AIR, 4);
            place(world, pos.down(), States.END_STONE);
            generateCap(world, random, pos.up(height), (int) (height / capRadiusDivision), stemCapDivision, percentageForCapDrooping, excessVineDiscardChance, vineGenerationTries);
            for (int a = 0; a < height; a++) {
                place(world, pos.up(a), States.CELESTIAL_STEM);
            }
        }
    }
}