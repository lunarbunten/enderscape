package net.bunten.enderscape.blocks;

import org.jetbrains.annotations.Nullable;

import net.bunten.enderscape.blocks.properties.StateProperties;
import net.bunten.enderscape.interfaces.HasRenderType;
import net.bunten.enderscape.util.MathUtil;
import net.bunten.enderscape.util.States;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class AbstractVineBlock extends Block implements HasRenderType {

    public static final BooleanProperty ATTACHED = StateProperties.ATTACHED;

    public static final IntegerProperty AGE = StateProperties.AGE_15;
    public static final int MAX_AGE = 15;

    public AbstractVineBlock(Properties settings) {
        super(settings);
    }

    abstract TagKey<Block> getPreferredBlocks();

    protected static boolean getAttached(BlockState state) {
        return state.getValue(ATTACHED);
    }

    protected static boolean isHead(BlockState state) {
        return !getAttached(state);
    }

    protected static boolean isBody(BlockState state) {
        return getAttached(state);
    }

    protected static int getAge(BlockState state) {
        return state.getValue(AGE);
    }

    protected static boolean isMaxAge(BlockState state) {
        return getAge(state) == MAX_AGE;
    }

    protected static int getPlacementAge(RandomSource random) {
        return MathUtil.nextInt(random, (int) (MAX_AGE * 0.1), (int) (MAX_AGE * 0.6));
    }

    protected void growVine(ServerLevel world, RandomSource random, BlockPos pos, BlockState state, float cycleChance) {
        BlockState down = state.setValue(ATTACHED, false);
        BlockState vine = state.setValue(ATTACHED, true);

        if (random.nextFloat() <= cycleChance && getAge(state) < MAX_AGE) {
            down = down.cycle(AGE);
            vine = vine.cycle(AGE);
        }

        world.setBlockAndUpdate(pos.below(), down);
        world.setBlockAndUpdate(pos, vine);
    }

    protected BlockState state(BlockState state, boolean attached, int age) {
        return state.setValue(ATTACHED, attached).setValue(AGE, age);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos().below());
        return state.is(this) ? defaultBlockState() : defaultBlockState().setValue(AGE, getPlacementAge(context.getLevel().getRandom()));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        if (getAttached(state)) {
            return box(2.5, 0, 2.5, 13.5, 16, 13.5);
        } else {
            return box(2.5, 2.5, 2.5, 13.5, 16, 13.5);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        if (!state.canSurvive(world, pos)) {
            return States.AIR;
        } else {
            var down = world.getBlockState(pos.below());

            return down.is(this) ? state(state, true, down.getValue(AGE)) : state(state, false, getAge(state));
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        var above = world.getBlockState(pos.above());
        return world.getFluidState(pos).isEmpty() && (above.is(this) || above.isFaceSturdy(world, pos, Direction.DOWN) && above.is(getPreferredBlocks()));
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return type == PathComputationType.AIR && !hasCollision || super.isPathfindable(state, world, pos, type);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player mob, InteractionHand hand, BlockHitResult hit) {
        var stack = mob.getItemInHand(hand);
        if (stack.getItem() instanceof ShearsItem && getAge(state) < MAX_AGE && !getAttached(state)) {
            mob.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            if (mob instanceof ServerPlayer server) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(server, pos, stack);
            }

            world.playSound(mob, pos, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1, 1);
            world.setBlockAndUpdate(pos, state(state, false, MAX_AGE));
            if (mob != null) {
                stack.hurtAndBreak(1, mob, player -> player.broadcastBreakEvent(hand));
            }

            return InteractionResult.sidedSuccess(world.isClientSide());
        }

        return InteractionResult.PASS;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public RenderType getRenderType() {
        return RenderType.cutout();
    }
}