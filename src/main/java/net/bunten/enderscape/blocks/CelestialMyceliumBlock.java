package net.bunten.enderscape.blocks;

import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.util.States;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ShovelItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;

public class CelestialMyceliumBlock extends Block {
    public CelestialMyceliumBlock(Settings settings) {
        super(settings);
    }

    private static boolean canSurvive(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.up();
        BlockState blockState = world.getBlockState(blockPos);
        int i = ChunkLightProvider.getRealisticOpacity(world, state, pos, blockState, blockPos, Direction.UP, blockState.getOpacity(world, blockPos));
        return i < world.getMaxLightLevel();
    }

    private static boolean shouldBreak(BlockView world, BlockPos pos) {
        boolean bl = false;
        BlockState state = world.getBlockState(pos.up());
        if (state.getFluidState().isIn(FluidTags.WATER)) {
            bl = true;
        }
        return bl;
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        var stack = player.getStackInHand(hand);
        if (stack.getItem() instanceof ShovelItem && world.getBlockState(pos.up()).isAir()) {
            world.playSound(player, pos, EnderscapeSounds.BLOCK_EYLIUM_FLATTEN, SoundCategory.BLOCKS, 1, 1);
            if (!world.isClient()) {
                world.setBlockState(pos, States.CELESTIAL_PATH, Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                if (player != null) {
                    stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
                }
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!canSurvive(state, world, pos)) {
            world.setBlockState(pos, States.END_STONE);
        }
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return shouldBreak(world, pos) ? States.END_STONE : super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }
}