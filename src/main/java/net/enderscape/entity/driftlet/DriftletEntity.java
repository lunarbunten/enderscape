package net.enderscape.entity.driftlet;

import net.enderscape.entity.drifter.DrifterEntity;
import net.enderscape.entity.drifter.DrifterMoveControl;
import net.enderscape.entity.drifter.DrifterNavigation;
import net.enderscape.entity.drifter.DrifterWanderGoal;
import net.enderscape.entity.rubblemite.RubblemiteEntity;
import net.enderscape.registry.EndEntities;
import net.enderscape.registry.EndItems;
import net.enderscape.registry.EndSounds;
import net.enderscape.util.EndMath;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

public class DriftletEntity extends PassiveEntity {
    public DriftletEntity(EntityType<? extends DriftletEntity> type, World world) {
        super(type, world);
        moveControl = new DrifterMoveControl(this);
        navigation = new DrifterNavigation(this, world);
        setPathfindingPenalty(PathNodeType.WATER, -1);
        experiencePoints = 1;
    }

    public static <T extends DriftletEntity> boolean canSpawn(EntityType<T> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return EndMath.inRange(pos.getY(), 60, 80);
    }

    protected void initGoals() {
        goalSelector.add(3, new TemptGoal(this, 1.25D, Ingredient.fromTag(EndItems.DRIFTER_FOOD), false));
        goalSelector.add(4, new FleeEntityGoal<>(this, RubblemiteEntity.class, 8, 2.2D, 2.2D));
        goalSelector.add(4, new FleeEntityGoal<>(this, SlimeEntity.class, 8, 2.2D, 2.2D));
        goalSelector.add(6, new DrifterWanderGoal(this));
        goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 8));
        goalSelector.add(8, new LookAtEntityGoal(this, DriftletEntity.class, 8));
        goalSelector.add(9, new LookAroundGoal(this));
    }

    public void tick() {
        if (age > 4000 && random.nextInt(10000) == 0) {
            growUp();
        }
        super.tick();
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public float getSoundPitch() {
        return EndMath.nextFloat(random, 0.8F, 1.2F);
    }

    @Override
    public SoundEvent getEatSound(ItemStack stack) {
        return EndSounds.ENTITY_DRIFTER_EAT;
    }

    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource source) {
        return false;
    }

    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.5F;
    }

    public boolean hurtByWater() {
        return true;
    }

    public boolean isBaby() {
        return true;
    }

    public LivingEntity growUp() {
        if (isAlive()) {
            DrifterEntity mob = EndEntities.DRIFTER.create(world);
            mob.updatePosition(getX(), getY(), getZ());
            mob.setYaw(getYaw());

            if (hasCustomName()) {
                mob.setCustomName(getCustomName());
                mob.setCustomNameVisible(isCustomNameVisible());
            }

            if (isLeashed()) {
                mob.attachLeash(mob.getHoldingEntity(), true);
                detachLeash(true, false);
            }

            if (hasVehicle()) {
                mob.startRiding(getVehicle());
            }

            mob.setHealth(mob.getMaxHealth());
            mob.setBreedingAge(-24000);
            mob.setBaby(false);
            world.spawnEntity(mob);
            remove(RemovalReason.DISCARDED);

            return mob;
        }
        return this;
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity mob) {
        return !isLeashed();
    }

    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        return world.getBlockState(pos).isAir() ? 0 : 1;
    }

    protected SoundEvent getAmbientSound() {
        return EndSounds.ENTITY_DRIFTLET_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return EndSounds.ENTITY_DRIFTLET_HURT;
    }

    protected SoundEvent getDeathSound() {
        return EndSounds.ENTITY_DRIFTLET_DEATH;
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }
}