package net.bunten.enderscape.blocks;

import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.util.MathUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NebuliteCauldronBlock extends Block {

    public static final int MAX_LEVELS = 3;
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 1, MAX_LEVELS);

    public NebuliteCauldronBlock(Properties settings) {
        super(settings);
    }

    public static int getLuminance(BlockState state) {
        return state.getValue(LEVEL) * 3;
    }

    public static boolean canLevel(Level world, BlockPos pos, BlockState state) {
        return state.hasProperty(LEVEL) && state.getValue(LEVEL) < MAX_LEVELS && !isFull(world, pos, state);
    }

    public static void addLevel(Level world, BlockPos pos, BlockState state) {
        world.setBlock(pos, state.setValue(LEVEL, state.getValue(LEVEL) + 1), UPDATE_ALL);
        world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, EnderscapeSounds.NEBULITE_CAULDRON_FILL, SoundSource.BLOCKS, 0.6F, 1);
    }

    public static void reduceLevel(Level world, BlockPos pos, BlockState state) {
        if (state.getValue(LEVEL) > 1) {
            world.setBlock(pos, state.setValue(LEVEL, state.getValue(LEVEL) - 1), UPDATE_ALL);
        } else {
            world.setBlock(pos, Blocks.CAULDRON.defaultBlockState(), UPDATE_ALL);
        }
    }

    public static boolean isFull(Level world, BlockPos pos, BlockState state) {
        return state.hasProperty(LEVEL) && state.getValue(LEVEL) == MAX_LEVELS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.join(Shapes.block(), Shapes.or(box(0, 0, 4, 16, 3, 12), box(4, 0, 0, 12, 3, 16), box(2, 0, 2, 14, 3, 14), getInteractionShape(state, world, pos)), BooleanOp.ONLY_FIRST);
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return box(2, 4, 2, 14, 16, 14);
    }

    protected boolean isEntityTouchingFluid(BlockState state, BlockPos pos, Entity entity) {
        return entity.getY() < pos.getY() + 0.4F && entity.getBoundingBox().maxY > pos.getY() + 0.25;
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (!world.isClientSide() && entity instanceof LivingEntity mob && isEntityTouchingFluid(state, pos, mob)) {
            double x = mob.getX();
            double y = mob.getY();
            double z = mob.getZ();
            for (int i = 0; i < 16; ++i) {
                double x2 = mob.getX() + (mob.getRandom().nextDouble() - 0.5) * 8;
                double y2 = MathUtil.clamp(mob.getY() + (mob.getRandom().nextInt(16) - 8), world.getMinBuildHeight(), (world.getMinBuildHeight() + ((ServerLevel)world).getLogicalHeight() - 1));
                double z2 = mob.getZ() + (mob.getRandom().nextDouble() - 0.5) * 8;
                if (mob.isPassenger()) {
                    mob.stopRiding();
                }
                if (!mob.randomTeleport(x2, y2, z2, true)) continue;
                world.gameEvent(GameEvent.TELEPORT, mob.position(), GameEvent.Context.of(mob));
                world.playSound(null, x, y, z, EnderscapeSounds.NEBULITE_CAULDRON_TELEPORT, SoundSource.BLOCKS, 1, 1);
                mob.playSound(EnderscapeSounds.NEBULITE_CAULDRON_TELEPORT, 1, 1);
                break;
            }
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
        return new ItemStack(Blocks.CAULDRON);
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return state.getValue(LEVEL);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        double x = pos.getX() + 0.5D + (0.5D - random.nextDouble());
        double y = pos.getY() + 1;
        double z = pos.getZ() + 0.5D + (0.5D - random.nextDouble());
        double ys = random.nextFloat() * 0.04D;
        world.addParticle(EnderscapeParticles.RISING_NEBULITE_CLOUD, x, y, z, 0, ys, 0);
    }
}