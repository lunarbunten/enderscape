package net.penumbra.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.penumbra.enderscape.registry.tag.EnderscapeBlockTags;

public class DryEndGrowthBlock extends VegetationBlock implements BonemealableBlock {

    public static final MapCodec<DryEndGrowthBlock> CODEC = simpleCodec(DryEndGrowthBlock::new);
    private static final VoxelShape VOXEL_SHAPE = box(1, 0, 1, 14, 13, 14);

    public DryEndGrowthBlock(Properties settings) {
        super(settings);
    }

    @Override
    public MapCodec<DryEndGrowthBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(EnderscapeBlockTags.SUPPORTS_DRY_END_GROWTH) && state.isFaceSturdy(level, pos, Direction.UP);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return VOXEL_SHAPE;
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