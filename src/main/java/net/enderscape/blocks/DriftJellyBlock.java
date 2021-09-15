package net.enderscape.blocks;

import net.enderscape.interfaces.LayerMapped;
import net.enderscape.registry.EndSounds;
import net.enderscape.util.EndMath;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlimeBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class DriftJellyBlock extends SlimeBlock implements LayerMapped {
    public DriftJellyBlock(Settings settings) {
        super(settings);
    }

    private static boolean shouldBreak(BlockView world, BlockPos pos) {
        boolean bl = false;
        BlockPos.Mutable mutable = pos.mutableCopy();
        Direction[] dir = Direction.values();

        for (Direction direction : dir) {
            if (direction != Direction.DOWN) {
                mutable.set(pos, direction);
                BlockState state = world.getBlockState(mutable);
                if (state.getFluidState().isIn(FluidTags.WATER)) {
                    bl = true;
                }
            }
        }
        return bl;
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (shouldBreak(world, pos)) {
            world.breakBlock(pos, true);
        }
    }

    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        world.getBlockTickScheduler().schedule(pos, this, 2);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        world.getBlockTickScheduler().schedule(pos, this, 2);
        if (shouldBreak(world, pos)) {
            return Blocks.AIR.getDefaultState();
        } else {
            return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
        }
    }

    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (entity.bypassesLandingEffects()) {
            super.onLandedUpon(world, state, pos, entity, fallDistance);
        } else {
            entity.handleFallDamage(fallDistance, 0, DamageSource.FALL);
        }
    }

    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.bypassesLandingEffects()) {
            Vec3d vel = entity.getVelocity();

            double x = vel.getX();
            double z = vel.getZ();

            float height = 1.3F;

            if (entity instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) entity;
                if (living.isFallFlying()) {
                    height = 1.8F;
                }
            }

            world.playSound(null, pos, EndSounds.BLOCK_DRIFT_JELLY_BOUNCE, SoundCategory.BLOCKS, 1F, 1.2F);
            entity.setVelocity(EndMath.clamp(x * 2, -23, 23), height, EndMath.clamp(z * 2, -23, 23));
        }
    }

    @Override
    public LayerType getLayerType() {
        return LayerType.TRANSLUCENT;
    }
}