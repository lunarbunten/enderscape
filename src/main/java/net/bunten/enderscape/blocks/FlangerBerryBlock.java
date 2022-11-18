package net.bunten.enderscape.blocks;

import net.bunten.enderscape.blocks.properties.Stage;
import net.bunten.enderscape.blocks.properties.StateProperties;
import net.bunten.enderscape.interfaces.HasRenderType;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.util.States;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FlangerBerryBlock extends HalfTransparentBlock implements HasRenderType, BonemealableBlock {
    
    public static final EnumProperty<Stage> STAGE = StateProperties.BERRY_STAGE;

    public FlangerBerryBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(STAGE, Stage.RIPE));
    }

    private boolean isFlower(BlockState state) {
        return state.getValue(STAGE) == Stage.FLOWER;
    }

    private boolean isRipe(BlockState state) {
        return state.getValue(STAGE) == Stage.RIPE;
    }

    protected int getFallDelay() {
        return 2;
    }

    protected boolean canFall(Level world, BlockState state, BlockPos pos) {
        boolean bl = false;
        if (FallingBlock.isFree(world.getBlockState(pos.below()))) {
            if (isRipe(state) && world.getBlockState(pos.above()).getBlock() != EnderscapeBlocks.FLANGER_BERRY_VINE) {
                bl = true;
            }
        }
        if (pos.getY() < world.getMinBuildHeight()) {
            bl = false;
        }
        return bl;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(STAGE) != Stage.FLOWER ? state.getShape(world, pos) : Shapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(STAGE)) {
            case FLOWER -> box(2, 10, 2, 14, 16, 14);
            case UNRIPE -> box(2, 4, 2, 14, 16, 14);
            default -> Shapes.block();
        };
    }
    
    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        world.scheduleTick(pos, this, getFallDelay());
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        world.scheduleTick(pos, this, getFallDelay());
        if (!canSurvive(state, world, pos)) {
            return States.AIR;
        } else {
            return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (canFall(world, state, pos)) {
            FallingBlockEntity.fall(world, pos, state);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return isRipe(state) ? true : world.getBlockState(pos.above()).is(EnderscapeBlocks.FLANGER_BERRY_VINE);
    }

    @Override
    public void onProjectileHit(Level world, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (!isFlower(state) && projectile.getType().is(EntityTypeTags.IMPACT_PROJECTILES)) {
            world.destroyBlock(hit.getBlockPos(), true, projectile);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (random.nextInt(12) == 0 && isBonemealSuccess(world, random, pos, state)) {
            performBonemeal(world, random, pos, state);
        }
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter world, BlockPos pos, BlockState state, boolean isClient) {
        return !isRipe(state);
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return !isRipe(state) && state.canSurvive(world, pos);
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        if (!isRipe(state)) {
            BlockState cycleStage = state.cycle(STAGE);
            world.setBlock(pos, cycleStage, UPDATE_ALL);
            SoundType group = cycleStage.getSoundType();
            world.playSound(null, pos, group.getPlaceSound(), SoundSource.BLOCKS, (group.getVolume() + 1) / 2, group.getPitch() * 0.8F);
            world.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        float stage = state.getValue(STAGE).ordinal() + 1;
        float size = Stage.values().length;
        return (int) ((stage / size) * 6);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
        if (stateFrom.is(this)) {
            return isRipe(stateFrom) ? true : false;
        } else {
            return super.skipRendering(state, stateFrom, direction);
        }
    }

    @Override
    public SoundType getSoundType(BlockState state) {
        return switch (state.getValue(STAGE)) {
            case FLOWER -> EnderscapeSounds.FLANGER_FLOWER;
            case UNRIPE -> EnderscapeSounds.FLANGER_BERRY_BLOCK;
            default -> EnderscapeSounds.FLANGER_BERRY_BLOCK;
        };
    }

    @Override
    @Environment(EnvType.CLIENT)
    public RenderType getRenderType() {
        return RenderType.cutout();
    }
}