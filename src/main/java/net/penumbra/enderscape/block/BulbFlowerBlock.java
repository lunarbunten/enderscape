package net.penumbra.enderscape.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.penumbra.enderscape.block.state.PurifyingPhase;
import net.penumbra.enderscape.registry.tag.EnderscapeBlockTags;

import java.util.List;

import static net.penumbra.enderscape.block.state.PurifyingPhase.POWERLESS;

public class BulbFlowerBlock extends VegetationBlock implements SuspiciousEffectHolder, RandomlyPurifiesEntities {

    private static final MapCodec<BulbFlowerBlock> CODEC = simpleCodec(BulbFlowerBlock::new);
    private static final VoxelShape SHAPE = box(2, 0, 2, 14, 15, 14);

    public BulbFlowerBlock(Properties settings) {
        super(settings);
        registerDefaultState(PurifyingPhase.set(defaultBlockState(), POWERLESS));
    }

    public static int lightLevel(BlockState state) {
        return switch (state.getValue(PHASE)) {
            case ACTIVE -> 11;
            case CHARGING -> 9;
            default -> 7;
        };
    }

    private boolean hasPower(LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(EnderscapeBlockTags.POWERS_BULB_FLOWER);
    }

    private PurifyingPhase pickDormantPhase(LevelReader level, BlockPos pos) {
        return hasPower(level, pos) ? PurifyingPhase.INACTIVE : POWERLESS;
    }

    @Override
    public MapCodec<BulbFlowerBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(EnderscapeBlockTags.SUPPORTS_BULB_FLOWER) && state.isFaceSturdy(level, pos, Direction.UP);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PHASE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);

        if (state != null) {
            return PurifyingPhase.set(state, pickDormantPhase(context.getLevel(), context.getClickedPos()));
        } else {
            return null;
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess access, BlockPos pos, Direction direction, BlockPos pos2, BlockState state2, RandomSource random) {
        if (!canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return PurifyingPhase.powerlessOrInactive(state) ? PurifyingPhase.set(state, pickDormantPhase(level, pos)) : state;
        }
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return PurifyingPhase.inactive(state);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        purifyRandomTick(state, level, pos, random);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        purifyScheduledTick(state, level, pos, random);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        purifyAnimateTick(state, level, pos, random);
    }

    @Override
    public SuspiciousStewEffects getSuspiciousEffects() {
        return new SuspiciousStewEffects(List.of(new SuspiciousStewEffects.Entry(MobEffects.GLOWING, Mth.floor(7 * 20))));
    }
}