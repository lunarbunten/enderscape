package net.bunten.enderscape.util;

import net.bunten.enderscape.blocks.AbstractMyceliumBlock;
import net.bunten.enderscape.blocks.GrowthBlock;
import net.bunten.enderscape.blocks.MurushroomsBlock;
import net.bunten.enderscape.blocks.properties.Part;
import net.bunten.enderscape.blocks.properties.StateProperties;
import net.bunten.enderscape.world.features.vegetation.GrowthConfig;
import net.bunten.enderscape.world.features.vegetation.LargeCelestialFungusFeatureConfig;
import net.bunten.enderscape.world.features.vegetation.MurushroomFeatureConfig;
import net.bunten.enderscape.world.generator.LargeCelestialFungusGenerator;
import net.bunten.enderscape.world.generator.LargeMurushroomGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class PlantUtil {

    public static void generateMycelium(BlockState state, LevelAccessor world, BlockPos pos, int horizontal_range, int vertical_range, float chance) {
        horizontal_range /= 2;

        world.setBlock(pos, state, 2);
        Direction dir = state.getValue(AbstractMyceliumBlock.FACING);

        for (int x = -horizontal_range + 1; x < horizontal_range; x++) {
            for (int y = -vertical_range; y < vertical_range; y++) {
                for (int z = -horizontal_range + 1; z < horizontal_range; z++) {
                    var pos2 = pos.offset(x, y, z);
                    boolean bl = world.getBlockState(pos2.relative(dir)).propagatesSkylightDown(world, pos2) && world.getBlockState(pos2).is(Blocks.END_STONE);
                    if (bl && MathUtil.sqrt(x * x + y * y + z * z) <= horizontal_range) {
                        if (world.getRandom().nextFloat() < chance) {
                            world.setBlock(pos2, state, 2);
                        }
                    }
                }
            }
        }
    }

    public static boolean generateGrowth(LevelAccessor world, RandomSource random, BlockPos origin, GrowthConfig config) {
        BlockState state = config.state();
        Direction direction = state.getValue(GrowthBlock.FACING);

        int totalHeight = config.base_height().sample(random) + (random.nextFloat() <= config.added_height_chance() ? config.added_height().sample(random) : 0);
        MutableBlockPos mutable = origin.mutable();

        int i = 0;

        while (totalHeight > i) {
            Part part = i == totalHeight - 1 ? Part.TOP : (i == 0 ? Part.BOTTOM : Part.MIDDLE);
            world.setBlock(mutable, state.setValue(StateProperties.GROWTH_PART, part), 2);
            mutable.move(direction);
            i++;
        }
    
        return true;
    }

    public static boolean generateMurushrooms(LevelAccessor world, BlockPos pos, RandomSource random, MurushroomFeatureConfig config) {
        int p = 0;

        for (int i = 0; i < config.tries(); i++) {
            BlockPos pos2 = BlockUtil.random(pos, random, config.horizontal_range(), config.vertical_range(), config.horizontal_range());

            if (world.getBlockState(pos2).isAir()) {
                for (var dir : Direction.values()) {
                    if (dir.getAxis() == Axis.Y) continue;
        
                    BlockPos wall = pos2.relative(dir);
    
                    if (world.getBlockState(wall).isSolidRender(world, wall)) {
                        world.setBlock(pos2, States.MURUSHROOMS.setValue(MurushroomsBlock.FACING, dir.getOpposite()).setValue(MurushroomsBlock.AGE, config.age()), 2);
                        p++;
    
                        break;
                    }
                }
            }
        }
        
        return p > 0;
    }

    public static boolean generateDefaultLargeCelestialFungus(LevelAccessor world, RandomSource random, BlockPos pos) {
        return LargeCelestialFungusGenerator.tryGenerate(world, random, pos, new LargeCelestialFungusFeatureConfig(UniformInt.of(10, 35), 4, 0.75F, 1, 64, 16));
    }

    public static boolean generateDefaultLargeMurushroom(Direction direction, LevelAccessor world, RandomSource random, BlockPos pos) {
        return LargeMurushroomGenerator.generate(direction, world, random, pos, UniformInt.of(6, 16).sample(random), UniformInt.of(4, 4).sample(random), 16);
    }

    public static boolean generateLargeMurushroom(LevelAccessor world, RandomSource random, BlockPos pos, int size) {
        boolean result = false;

        for (int x = -size + 1; x < size; x++) {
            for (int z = -size + 1; z < size; z++) {
                if (Math.sqrt(x * x + z * z) <= size * 0.8) {
                    var pos2 = pos.offset(x, 0, z);
                    if (world.getBlockState(pos2).getMaterial().isReplaceable()) {
                        world.setBlock(pos2, States.MURUSHROOM_CAP, 2);
                        result = true;
                    }
                }
            }
        }

        return result;
    }
}