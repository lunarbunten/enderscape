package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.pathfinder.PathComputationType;

public class FlangerBerryVine extends AbstractVineBlock implements BonemealableBlock {
    public FlangerBerryVine(Properties settings) {
        super(settings);

        registerDefaultState(defaultBlockState().setValue(ATTACHED, false).setValue(AGE, 0));
    }

    public static final MapCodec<FlangerBerryVine> CODEC = simpleCodec(FlangerBerryVine::new);

    @Override
    public MapCodec<FlangerBerryVine> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ATTACHED, AGE);
    }

    @Override
    public TagKey<Block> getPreferredBlocks() {
        return EnderscapeBlockTags.FLANGER_BERRY_VINE_PLANTABLE_ON;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor world, BlockPos pos, BlockPos pos2) {
        var down = world.getBlockState(pos.below());
        return state.canSurvive(world, pos) && down.is(EnderscapeBlockTags.FLANGER_BERRY_VINE_SUPPORTS) ? state(state, true, getAge(state)) : super.updateShape(state, direction, state2, world, pos, pos2);
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return type == PathComputationType.AIR && !hasCollision || super.isPathfindable(state, type);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (random.nextInt(12) == 0 && isBonemealSuccess(world, random, pos, state)) {
            performBonemeal(world, random, pos, state);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        for (pos = isBody(state) ? pos.below() : pos; pos.getY() >= world.getMinBuildHeight(); pos = pos.below()) {
            if (world.getBlockState(pos).is(this) && world.getBlockState(pos.below()).isAir()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        for (pos = isBody(state) ? pos.below() : pos; pos.getY() > world.getMinBuildHeight(); pos = pos.below()) {
            var state2 = world.getBlockState(pos);
            if (state2.is(this)) {
                var down = world.getBlockState(pos.below());
                if (state2.getValue(AGE) == MAX_AGE) {
                    if (down.isAir()) {
                        world.setBlockAndUpdate(pos, state(state, true, MAX_AGE));
                        BlockState cycleStage = EnderscapeBlocks.FLANGER_BERRY_FLOWER.defaultBlockState();
                        world.setBlock(pos.below(), cycleStage, UPDATE_ALL);
                        SoundType group = cycleStage.getSoundType();
                        world.playSound(null, pos, group.getPlaceSound(), SoundSource.BLOCKS, (group.getVolume() + 1) / 2, group.getPitch() * 0.8F);
                        break; 
                    }
                } else {
                    if (down.isAir()) {
                        growVine(world, random, pos, state, 1);
                        break;
                    }
                }
            }
        }
    }
}