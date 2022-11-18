package net.bunten.enderscape.blocks;

import java.util.Optional;

import net.bunten.enderscape.interfaces.HasColorProvider;
import net.bunten.enderscape.interfaces.HasRenderType;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.util.BlockUtil;
import net.bunten.enderscape.util.States;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BlinklightVinesBodyBlock extends GrowingPlantBodyBlock implements BlinklightVines, HasColorProvider, HasRenderType {
    public BlinklightVinesBodyBlock(Properties settings) {
        super(settings, Direction.DOWN, SHAPE, false);
        registerDefaultState(defaultBlockState().setValue(getStageProperty(), 0));
    }

    
    @Override
    public IntegerProperty getStageProperty() {
        return BODY_STAGE;
    }

    @Override
    public int getStageDuration(BlockState state) {
        return 6;
    }

    @Override
    public void playBlinkEffects(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter world, BlockPos pos, BlockState state, boolean isClient) {
        Optional<BlockPos> head = BlockUtil.getTopConnectedBlock(world, pos, state.getBlock(), growthDirection, getHeadBlock());
        if (head.isPresent()) {
            BlockPos headPos = head.get();
            return getHeadBlock() instanceof BlinklightVines vines && vines.isOpen(world.getBlockState(headPos)) && world.getBlockState(headPos.relative(growthDirection)).isAir();
        } else {
            return false;
        }
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (random.nextInt(24) == 0 && isOpen(state)) close(state, world, pos);
        super.randomTick(state, world, pos, random);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(world, pos)) {
            world.destroyBlock(pos, true);
            return;
        }

        state = state.cycle(getStageProperty());
        world.setBlock(pos, state, 3);
        playBlinkEffects(state, world, pos, random);
        if (getStage(state) > 0) world.scheduleTick(pos, this, getStageDuration(state));
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
        return isClosed(state) ? 6 : 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(getStageProperty());
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
        return new ItemStack(EnderscapeItems.BLINKLIGHT);
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock) EnderscapeBlocks.BLINKLIGHT_VINES_HEAD;
    }

    @Environment(EnvType.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos origin, RandomSource random) {
        if (random.nextFloat() < 0.9F) return;
        world.addParticle(EnderscapeParticles.BLINKLIGHT_SPORES, origin.getX() + random.nextDouble(), origin.getY() + random.nextDouble(), origin.getZ() + random.nextDouble(), 0, 0, 0);
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