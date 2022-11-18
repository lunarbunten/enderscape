package net.bunten.enderscape.blocks;

import org.jetbrains.annotations.Nullable;

import net.bunten.enderscape.blocks.properties.DirectionProperties;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.util.PlantUtil;
import net.bunten.enderscape.util.States;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LayerLightEngine;
import net.minecraft.world.phys.BlockHitResult;

public class EndMyceliumBlock extends AbstractMyceliumBlock implements BonemealableBlock {
    
    @Nullable
    private final Block pathBlock;
    private final boolean needsAir;

    public EndMyceliumBlock(boolean needsAir, @Nullable Block pathBlock, DirectionProperties properties, Properties settings) {
        super(properties, settings);
        this.needsAir = needsAir;
        this.pathBlock = pathBlock;
    }

    public EndMyceliumBlock(boolean needsAir, DirectionProperties properties, Properties settings) {
        this(needsAir, null, properties, settings);
    }

    private boolean hasAir(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos blockPos = pos.relative(getDirection(state));
        BlockState blockState = world.getBlockState(blockPos);
        int i = LayerLightEngine.getLightBlockInto(world, state, pos, blockState, blockPos, Direction.UP, blockState.getLightBlock(world, blockPos));
        return i < world.getMaxLightLevel();
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (needsAir && !hasAir(state, world, pos)) {
            world.setBlockAndUpdate(pos, States.END_STONE);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        Direction direction = state.getValue(FACING);

        if (pathBlock != null && stack.getItem() instanceof ShovelItem && world.getBlockState(pos.relative(direction)).isAir()) {
            world.playSound(player, pos, EnderscapeSounds.EYLIUM_FLATTEN, SoundSource.BLOCKS, 1, 1);
            if (!world.isClientSide()) {
                world.setBlock(pos, pathBlock.defaultBlockState().setValue(FACING, direction), Block.UPDATE_ALL | Block.UPDATE_IMMEDIATE);
                if (player != null) {
                    stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                }
            }
            
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter world, BlockPos pos, BlockState state, boolean isClient) {
        return world.getBlockState(pos.relative(getDirection(state))).propagatesSkylightDown(world, pos);
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        PlantUtil.generateMycelium(state, world, pos, 12, 12, 0.6F);
    }
}