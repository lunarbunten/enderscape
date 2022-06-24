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
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

public class PlantUtil {

    private static final BlockState BULB_FLOWER = EnderscapeBlocks.BULB_FLOWER.getDefaultState();
    private static final BlockState CELESTIAL_GROWTH = EnderscapeBlocks.CELESTIAL_GROWTH.getDefaultState();
    private static final BlockState CELESTIAL_FUNGUS = EnderscapeBlocks.CELESTIAL_FUNGUS.getDefaultState();
    private static final BlockState CELESTIAL_MYCELIUM = EnderscapeBlocks.CELESTIAL_MYCELIUM_BLOCK.getDefaultState();
    private static final BlockState MURUSHROOMS = EnderscapeBlocks.MURUSHROOMS.getDefaultState();
    private static final BlockState MURUSHROOM_CAP = EnderscapeBlocks.MURUSHROOM_CAP.getDefaultState();

    private static final WeightedBlockStateProvider PROVIDER = new WeightedBlockStateProvider(new DataPool.Builder<BlockState>().add(CELESTIAL_FUNGUS, 6).add(BULB_FLOWER, 1));

    public static void generateCelestialMycelium(WorldAccess world, BlockPos pos, int xRange, int yRange, float chance) {
        xRange /= 2;

        world.setBlockState(pos, CELESTIAL_MYCELIUM, 2);
        for (int x = -xRange + 1; x < xRange; x++) {
            for (int y = -yRange; y < yRange; y++) {
                for (int z = -xRange + 1; z < xRange; z++) {
                    var pos2 = pos.add(x, y, z);
                    boolean bl = world.getBlockState(pos2.up()).isTranslucent(world, pos2) && world.getBlockState(pos2).isOf(Blocks.END_STONE);
                    if (bl && MathUtil.sqrt(x * x + y * y + z * z) <= xRange) {
                        if (world.getRandom().nextFloat() < chance) {
                            world.setBlockState(pos2, CELESTIAL_MYCELIUM, 2);
                        }
                    }
                }
            }
        }
    }
    
    public static boolean generateCelestialGrowth(WorldAccess world, Random random, BlockPos pos, int xRange, int yRange, int tries) {
        boolean result = false;

        for (int i = 0; i < tries; i++) {
            BlockPos pos2 = MathUtil.random(pos, random, xRange, yRange, xRange);
            boolean bl = world.getBlockState(pos2.down()).isIn(EnderscapeBlocks.GROWTH_PLANTABLE_OM);
            
            if (world.isAir(pos2) && bl) {
                int addedHeight = random.nextBoolean() ? MathUtil.nextInt(random, 0, 2) : 0;

                for (int g = 0; g <= addedHeight; g++) {
                    BlockPos pos3 = pos2.up(g);
                    if (!world.isAir(pos3)) {
                        continue;
                    } else {
                        result = true;

                        GrowthPart growthPart;
                        if (world.getBlockState(pos3.up()).isAir()) {
                            if (g == addedHeight) {
                                growthPart = GrowthPart.TOP;
                            } else if (g == 0) {
                                growthPart = GrowthPart.BOTTOM;
                            } else {
                                growthPart = GrowthPart.MIDDLE;
                            }
                        } else {
                            growthPart = GrowthPart.TOP;
                        }

                        world.setBlockState(pos3, CELESTIAL_GROWTH.with(EnderscapeProperties.GROWTH_PART, growthPart), 2);
                    }
                }
            }
        }

        return result;
    }

    public static boolean generateCelestialVegetation(WorldAccess world, Random random, BlockPos pos, int xRange, int yRange, int tries) {
        boolean result = false;

        for (int i = 0; i < tries; i++) {
            var pos2 = MathUtil.random(pos, random, xRange, yRange, xRange);
            var state = PROVIDER.getBlockState(random, pos2);
            if (world.isAir(pos2) && state.canPlaceAt(world, pos2)) {
                world.setBlockState(pos2, state, 2);
                result = true;
            }
        }

        return result;
    }

    public static boolean generateMurushrooms(WorldAccess world, BlockPos pos, Random random, int age, int xRange, int yRange, int tries) {
        int p = 0;

        for (int i = 0; i < tries; i++) {
            BlockPos pos2 = MathUtil.random(pos, random, xRange, yRange, xRange);

            if (world.getBlockState(pos2).isAir()) {
                for (var dir : Direction.values()) {
                    if (dir.getAxis() == Axis.Y) continue;
        
                    BlockPos wall = pos2.offset(dir);
    
                    if (world.getBlockState(wall).isOpaqueFullCube(world, wall)) {
                        world.setBlockState(pos2, MURUSHROOMS.with(MurushroomsBlock.FACING, dir.getOpposite()).with(MurushroomsBlock.AGE, age), 2);
                        p++;
    
                        break;
                    }
                }
            }
        }
        
        return p > 0;
    }

    public static boolean generateLargeCelestialFungus(WorldAccess world, Random random, BlockPos pos, int tries) {
        for (int i = 0; i < tries; i++) {
            int height = LargeCelestialFungusGenerator.getHeight(random);
            if (LargeCelestialFungusGenerator.isEnoughAir(world, pos, height)) {
                LargeCelestialFungusGenerator.generate(world, random, pos, height);
                return true;
            }
        }

        return false;
    }

    public static boolean generateLargeMurushroom(WorldAccess world, Random random, BlockPos pos, int size) {
        boolean result = false;

        for (int x = -size + 1; x < size; x++) {
            for (int z = -size + 1; z < size; z++) {
                if (Math.sqrt(x * x + z * z) <= size * 0.8) {
                    var pos2 = pos.add(x, 0, z);
                    if (world.getBlockState(pos2).getMaterial().isReplaceable()) {
                        world.setBlockState(pos2, MURUSHROOM_CAP, 2);
                        result = true;
                    }
                }
            }
        }

        return result;
    }

    protected class LargeCelestialFungusGenerator {

        private static final BlockState STEM = EnderscapeBlocks.CELESTIAL_STEM.getDefaultState();
        private static final BlockState CAP = EnderscapeBlocks.CELESTIAL_CAP.getDefaultState();
    
        private static final BlockState VINE = EnderscapeBlocks.FLANGER_BERRY_VINE.getDefaultState();
        private static final BlockState BERRY = EnderscapeBlocks.FLANGER_BERRY_BLOCK.getDefaultState();

        protected static void place(WorldAccess world, BlockPos pos, BlockState state) {
            world.setBlockState(pos, state, 2);
        }

        public static int getHeight(Random random) {
            return MathUtil.nextInt(random, 10, 35);
        }
    
        public static boolean isEnoughAir(WorldAccess world, BlockPos start, int height) {
            for (int i = 1; i < height; i++) {
                if (!world.isAir(start.up(i))) {
                    return false;
                }
            }
            return true;
        }
    
        protected static void generateVines(WorldAccess world, Random random, BlockPos pos, int range) {
            int half = range / 2;
            double f = pos.getX() + (random.nextInt(range) - half);
            double g = pos.getY() - random.nextInt(range);
            double h = pos.getZ() + (random.nextInt(range) - half);
            pos = new BlockPos(f, g, h);
            if (world.isAir(pos) && world.isAir(pos.down()) && world.getBlockState(pos.up()).isOpaque() && random.nextInt(50) == 0) {
                int length = MathUtil.nextInt(random, range / 4, range);
                for (int i = 0; i <= length; ++i) {
                    BlockPos pos2 = pos.down(i);
                    if (world.isAir(pos2)) {
                        if (i == length || !world.isAir(pos2.down())) {
                            BlockState state = BERRY;
                            state = state.with(FlangerBerryBlock.STAGE, Util.getRandom(FlangerBerryStage.values(), random));
                            place(world, pos2, state);
                            break;
                        } else {
                            place(world, pos2, VINE.with(Properties.ATTACHED, true).with(FlangerBerryVine.AGE, FlangerBerryVine.MAX_AGE));
                        }
                    }
                }
            }
        }
    
        protected static void generateCap(WorldAccess world, Random random, BlockPos pos, int radius) {
            for (int x = -radius + 1; x < radius; x++) {
                for (int z = -radius + 1; z < radius; z++) {
                    double distance = Math.sqrt(x * x + z * z);
                    if (distance <= radius) {
                        for (int a = 1; a < radius; a++) {
                            int b = distance >= radius * 0.75F && a == 1 && radius > 3 ? 1 : 0;
                            place(world, pos.add(x / a, -(2 * a + b) + 2, z / a), CAP);
                            for (int i = 0; i < 6; i++) {
                                generateVines(world, random, pos.down(), radius * 2);
                            }
                        }
                    }
                }
            }
        }
    
        public static void generate(WorldAccess world, Random random, BlockPos pos, int height) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
            place(world, pos.down(), Blocks.END_STONE.getDefaultState());
            generateCap(world, random, pos.up(height), (height / 4));
            for (int a = 0; a < height; a++) {
                place(world, pos.up(a), STEM);
            }
        }
    }
}