package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.block.properties.DirectionProperties;
import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.bunten.enderscape.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChorusSproutsBlock extends DirectionalPlantBlock implements BonemealableBlock {

    public ChorusSproutsBlock(Properties settings) {
        super(DirectionProperties.create().up(), settings);
    }

    public static final MapCodec<ChorusSproutsBlock> CODEC = simpleCodec(ChorusSproutsBlock::new);

    @Override
    public MapCodec<ChorusSproutsBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean canPlantOn(BlockState state, BlockState floor, BlockGetter level, BlockPos pos, Direction facing) {
        return floor.is(EnderscapeBlockTags.CHORUS_VEGETATION_PLANTABLE_ON) && floor.isFaceSturdy(level, pos, facing);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return BlockUtil.createRotatedShape(2, 0, 2, 14, 10, 14, getFacing(state));
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos origin, BlockState state) {
        int i = 0;
        int max = Mth.nextInt(random, 1, 3);
        var range = Mth.nextInt(random, 4, 8);

        for (BlockPos pos : BlockPos.randomInCube(random, 8, origin, range)) {
            if (i >= max) break;
            if (level.isEmptyBlock(pos) && state.canSurvive(level, pos)) {
                level.setBlock(pos, state, 2);
                i++;
            }
        }
    }
}