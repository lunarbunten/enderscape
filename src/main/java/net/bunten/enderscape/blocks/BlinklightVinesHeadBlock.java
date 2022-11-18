package net.bunten.enderscape.blocks;

import net.bunten.enderscape.interfaces.HasColorProvider;
import net.bunten.enderscape.interfaces.HasRenderType;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.util.BlockUtil;
import net.bunten.enderscape.util.MathUtil;
import net.bunten.enderscape.util.States;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class BlinklightVinesHeadBlock extends GrowingPlantHeadBlock implements BlinklightVines, HasColorProvider, HasRenderType {
    public BlinklightVinesHeadBlock(Properties settings) {
        super(settings, Direction.DOWN, SHAPE, false, 0.14);
        registerDefaultState(defaultBlockState().setValue(AGE, 0).setValue(getStageProperty(), 0));
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
    public void playBlinkEffects(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!isOpen(state)) {
            SoundEvent sound = EnderscapeSounds.BLINKLIGHT_VINES_INBETWEEN;
            if (isClosed(state)) {
                sound = EnderscapeSounds.BLINKLIGHT_VINES_BLINK;
                world.gameEvent(GameEvent.BLOCK_CLOSE, pos, GameEvent.Context.of(state));
                var particle = new DustColorTransitionOptions(PARTICLE_COLOR, DustParticleOptions.REDSTONE_PARTICLE_COLOR, 0.65F);
                for (int i = 0; i < 4; i++) {
                    world.sendParticles(particle, pos.getX() + random.nextDouble(), pos.getY() + (random.nextDouble() * 0.3) + 0.13, pos.getZ() + random.nextDouble(), MathUtil.nextInt(random, 3, 6), 0, 0, 0, 0);
                }
            }
            world.playSound(null, pos, sound, SoundSource.BLOCKS, 1, 1);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player mob, InteractionHand hand, BlockHitResult hit) {
        if (!isOpen(state)) {
            return InteractionResult.CONSUME;
        } else {
            if (isMaxAge(state)) {
                if (world instanceof ServerLevel server) close(state, server, pos);
                return InteractionResult.SUCCESS;
            } else {
                return super.use(state, world, pos, mob, hand, hit);
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
    public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor levelAccessor, BlockPos pos, BlockPos pos2) {
        if (!isOpen(state) && direction == growthDirection.getOpposite() && !state.canSurvive(levelAccessor, pos)) {
            return States.AIR;
        }
        return super.updateShape(state, direction, state2, levelAccessor, pos, pos2);
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
    public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
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

    @Environment(EnvType.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos origin, RandomSource random) {
        if (random.nextFloat() > 0.65F) {
            BlockPos pos = BlockUtil.random(origin, random, 3, 3, 3);
            if (!world.getBlockState(pos).isCollisionShapeFullBlock(world, pos)) {
                world.addParticle(EnderscapeParticles.BLINKLIGHT_SPORES, pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble(), 0, 0, 0);
            }
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public RenderType getRenderType() {
        return RenderType.cutout();
    }

    @Override
    @Environment(value=EnvType.CLIENT)
    public BlockColor getColorProvider() {
        return new BlinklightColorProvider();
    }
}