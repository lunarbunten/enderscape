package net.bunten.enderscape.blocks;

import java.util.Random;

import net.bunten.enderscape.registry.EnderscapeSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

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

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient() && entity instanceof LivingEntity mob && isEntityTouchingFluid(state, pos, mob)) {
            double x = mob.getX();
            double y = mob.getY();
            double z = mob.getZ();
            for (int i = 0; i < 16; ++i) {
                double x2 = mob.getX() + (mob.getRandom().nextDouble() - 0.5) * 8;
                double y2 = MathHelper.clamp(mob.getY() + (mob.getRandom().nextInt(16) - 8), world.getBottomY(), (world.getBottomY() + ((ServerWorld)world).getLogicalHeight() - 1));
                double z2 = mob.getZ() + (mob.getRandom().nextDouble() - 0.5) * 8;
                if (mob.hasVehicle()) {
                    mob.stopRiding();
                }
                if (!mob.teleport(x2, y2, z2, true)) continue;
                world.emitGameEvent(GameEvent.TELEPORT, mob.getPos(), GameEvent.Emitter.of(mob));
                world.playSound(null, x, y, z, EnderscapeSounds.BLOCK_NEBULITE_CAULDRON_TELEPORT, SoundCategory.BLOCKS, 1, 1);
                mob.playSound(EnderscapeSounds.BLOCK_NEBULITE_CAULDRON_TELEPORT, 1, 1);
                break;
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        double d = pos.getX() + 0.5D + (0.5D - random.nextDouble());
        double e = pos.getY() + 1;
        double f = pos.getZ() + 0.5D + (0.5D - random.nextDouble());

        double g = random.nextFloat() * 0.04D;

        world.addParticle(new DustParticleEffect(new Vec3f(Vec3d.unpackRgb(0xB328D2)), 1), d, e, f, 0, g, 0);
    }

    protected boolean isEntityTouchingFluid(BlockState state, BlockPos pos, Entity entity) {
        return entity.getY() < pos.getY() + 0.4F && entity.getBoundingBox().maxY > pos.getY() + 0.25;
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(Blocks.CAULDRON);
    }

    public static boolean canLevel(World world, BlockPos pos, BlockState state) {
        return state.contains(LEVEL) && state.get(LEVEL) < MAX_LEVELS && !isFull(world, pos, state);
    }

    public static void addLevel(World world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, state.with(LEVEL, state.get(LEVEL) + 1), NOTIFY_ALL);
        world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, EnderscapeSounds.BLOCK_NEBULITE_CAULDRON_FILL, SoundCategory.BLOCKS, 0.6F, 1);
    }

    public static void reduceLevel(World world, BlockPos pos, BlockState state) {
        if (state.get(LEVEL) > 1) {
            world.setBlockState(pos, state.with(LEVEL, state.get(LEVEL) - 1), NOTIFY_ALL);
        } else {
            world.setBlockState(pos, Blocks.CAULDRON.getDefaultState(), NOTIFY_ALL);
        }
    }

    public static boolean isFull(World world, BlockPos pos, BlockState state) {
        return state.contains(LEVEL) && state.get(LEVEL) == MAX_LEVELS;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }
    
    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(LEVEL);
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}