package net.enderscape.blocks;

import java.util.Random;

import net.enderscape.registry.EndItems;
import net.enderscape.registry.EndSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class NebuliteCauldronBlock extends Block {
    public NebuliteCauldronBlock(Settings settings) {
        super(settings);
    }

    public static final int MAX_LEVELS = 3;
    public static final IntProperty LEVEL = IntProperty.of("level", 1, MAX_LEVELS);

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.union(createCuboidShape(0, 0, 4, 16, 3, 12), createCuboidShape(4, 0, 0, 12, 3, 16), createCuboidShape(2, 0, 2, 14, 3, 14), getRaycastShape(state, world, pos)), BooleanBiFunction.ONLY_FIRST);
    }

    public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return createCuboidShape(2, 4, 2, 14, 16, 14);
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity mob, Hand hand, BlockHitResult result) {
        ItemStack stack = mob.getStackInHand(hand);
        if (stack.isOf(EndItems.NEBULITE) && canLevel(world, pos, state) && !world.isClient()) {
            addLevel(world, pos, state);
            if (!mob.getAbilities().creativeMode && !world.isClient()) {
                stack.decrement(1);
            }
            return ActionResult.SUCCESS;
        } else {
            return ActionResult.PASS;
        }
    }

    public static boolean canLevel(World world, BlockPos pos, BlockState state) {
        return state.contains(LEVEL) && state.get(LEVEL) < MAX_LEVELS && !isFull(world, pos, state);
    }

    public static boolean isFull(World world, BlockPos pos, BlockState state) {
        return state.contains(LEVEL) && state.get(LEVEL) == MAX_LEVELS;
    }

    public static void addLevel(World world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, state.with(LEVEL, state.get(LEVEL) + 1), NOTIFY_ALL);
        world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, EndSounds.BLOCK_NEBULITE_CAULDRON_FILL, SoundCategory.BLOCKS, 0.6F, 1);
    }

    public static void reduceLevel(World world, BlockPos pos, BlockState state) {
        if (state.get(LEVEL) > 1) {
            world.setBlockState(pos, state.with(LEVEL, state.get(LEVEL) - 1), NOTIFY_ALL);
        } else {
            world.setBlockState(pos, Blocks.CAULDRON.getDefaultState(), NOTIFY_ALL);
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        double d = pos.getX() + 0.5D + (0.5D - random.nextDouble());
        double e = pos.getY() + 1;
        double f = pos.getZ() + 0.5D + (0.5D - random.nextDouble());

        double g = random.nextFloat() * 0.04D;

        world.addParticle(new DustParticleEffect(new Vec3f(Vec3d.unpackRgb(11741394)), 1), d, e, f, 0, g, 0);
    }
}