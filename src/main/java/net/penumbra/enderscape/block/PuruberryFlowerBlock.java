package net.penumbra.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;

public class PuruberryFlowerBlock extends Block implements BonemealableBlock {

    public PuruberryFlowerBlock(Properties settings) {
        super(settings);
    }

    public static final MapCodec<PuruberryFlowerBlock> CODEC = simpleCodec(PuruberryFlowerBlock::new);

    @Override
    public MapCodec<PuruberryFlowerBlock> codec() {
        return CODEC;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return box(2, 10, 2, 14, 16, 14);
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess access, BlockPos pos, Direction direction, BlockPos pos2, BlockState state2, RandomSource random) {
        if (!canSurvive(state, world, pos)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(state, world, access, pos, direction, pos2, state2, random);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return (Block.canSupportCenter(world, pos.above(), Direction.DOWN) && !world.isWaterAt(pos)) || attachedToVine(world, pos);
    }

    private boolean attachedToVine(LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.above()).is(EnderscapeBlocks.PURUBERRY_VINE);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (isBonemealSuccess(world, random, pos, state)) {
            performBonemeal(world, random, pos, state);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        return attachedToVine(world, pos);
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return attachedToVine(world, pos) && random.nextFloat() > 0.8F;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        BlockState unripe = EnderscapeBlocks.UNRIPE_PURUBERRY_BLOCK.defaultBlockState();
        world.setBlock(pos, unripe, UPDATE_ALL);
        SoundType group = unripe.getSoundType();
        world.playSound(null, pos, group.getPlaceSound(), SoundSource.BLOCKS, (group.getVolume() + 1) / 2, group.getPitch() * 0.8F);
        world.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        return 2;
    }
}