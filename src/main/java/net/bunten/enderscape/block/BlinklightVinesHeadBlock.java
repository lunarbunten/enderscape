package net.bunten.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.registry.EnderscapeBlockSounds;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.util.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class BlinklightVinesHeadBlock extends GrowingPlantHeadBlock implements BlinklightVines {
    public static final MapCodec<BlinklightVinesHeadBlock> CODEC = simpleCodec(BlinklightVinesHeadBlock::new);

    public BlinklightVinesHeadBlock(Properties settings) {
        super(settings, Direction.DOWN, SHAPE, false, 0.14);
        registerDefaultState(defaultBlockState().setValue(AGE, 0).setValue(getStageProperty(), 0));
    }

    @Override
    public MapCodec<BlinklightVinesHeadBlock> codec() {
        return CODEC;
    }

    @Override
    public IntegerProperty getStageProperty() {
        return HEAD_STAGE;
    }

    @Override
    public int getStageDuration(BlockState state) {
        return isClosed(state) ? 30 : 4;
    }

    @Override
    public void playBlinkEffects(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!isOpen(state)) {
            SoundEvent sound = EnderscapeBlockSounds.BLINKLIGHT_VINES_INBETWEEN;
            if (isClosed(state)) {
                sound = EnderscapeBlockSounds.BLINKLIGHT_VINES_BLINK;
                level.gameEvent(GameEvent.BLOCK_CLOSE, pos, GameEvent.Context.of(state));
                for (int i = 0; i < 4; i++) {
                    level.sendParticles(BLINKLIGHT_TO_REDSTONE, pos.getX() + random.nextDouble(), pos.getY() + (random.nextDouble() * 0.3) + 0.13, pos.getZ() + random.nextDouble(), Mth.nextInt(random, 3, 6), 0, 0, 0, 0);
                }
            }
            level.playSound(null, pos, sound, SoundSource.BLOCKS, 1, 1);
            level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        }
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult result) {
        if (!isOpen(state)) {
            return InteractionResult.CONSUME;
        } else {
            if (isMaxAge(state)) {
                if (world instanceof ServerLevel server) close(state, server, pos);
                return InteractionResult.SUCCESS;
            } else {
                return super.useWithoutItem(state, world, pos, player, result);
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }
    
    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (random.nextInt(3) == 0 && isOpen(state) && isMaxAge(state)) {
            close(state, world, pos);
            return;
        }
        super.randomTick(state, world, pos, random);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(world, pos)) {
            world.destroyBlock(pos, true);
            return;
        }

        close(state, world, pos);
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess access, BlockPos pos, Direction direction, BlockPos pos2, BlockState state2, RandomSource random) {
        if (!isOpen(state) && direction == growthDirection.getOpposite() && !state.canSurvive(world, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, world, access, pos, direction, pos2, state2, random);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
        return isClosed(state) ? 15 : 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(getStageProperty());
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean bl) {
        return new ItemStack(EnderscapeItems.BLINKLIGHT);
    }

    @Override
    protected Block getBodyBlock() {
        return EnderscapeBlocks.BLINKLIGHT_VINES_BODY;
    }

    @Override
    protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
        return 1;
    }

    @Override
    protected boolean canGrowInto(BlockState state) {
        return state.isAir();
    }

    @Override
    protected BlockState getGrowIntoState(BlockState state, RandomSource random) {
        return super.getGrowIntoState(state, random).setValue(getStageProperty(), 0);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos origin, RandomSource random) {
        if (random.nextFloat() > 0.65F) {
            BlockPos pos = BlockUtil.random(origin, random, 3, 3, 3);
            if (!world.getBlockState(pos).isCollisionShapeFullBlock(world, pos)) {
                world.addParticle(EnderscapeParticles.BLINKLIGHT_SPORES, pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble(), 0, 0, 0);
            }
        }
    }
}