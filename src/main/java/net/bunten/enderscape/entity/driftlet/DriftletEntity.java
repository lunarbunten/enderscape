package net.bunten.enderscape.entity.driftlet;

import net.bunten.enderscape.entity.AbstractDrifterEntity;
import net.bunten.enderscape.entity.drifter.DrifterEntity;
import net.bunten.enderscape.entity.rubblemite.RubblemiteEntity;
import net.bunten.enderscape.registry.EnderscapeEntities;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class DriftletEntity extends AbstractDrifterEntity {
    public static int MAX_GROWTH_AGE = 24000;
    private int growthAge;
    
    public DriftletEntity(EntityType<? extends AbstractDrifterEntity> type, World world) {
        super(type, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return createMobAttributes()
        .add(EntityAttributes.GENERIC_MAX_HEALTH, 8)
        .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16)
        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1)
        .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.35)
        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2);
    }

    @Override
    protected void initGoals() {
        goalSelector.add(1, new EscapeDangerGoal(this, 2));
        goalSelector.add(2, new TemptGoal(this, 1.25D, Ingredient.fromTag(EnderscapeItems.DRIFTER_FOOD), false));
        goalSelector.add(3, new FollowParentGoal(this, 1.25));
        goalSelector.add(4, new FleeEntityGoal<>(this, RubblemiteEntity.class, 8, 2.2D, 2.2D));
        goalSelector.add(4, new FleeEntityGoal<>(this, SlimeEntity.class, 8, 2.2D, 2.2D));
        goalSelector.add(5, new DrifterWanderGoal(this));
        goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8));
        goalSelector.add(6, new LookAtEntityGoal(this, DriftletEntity.class, 8));
        goalSelector.add(7, new LookAroundGoal(this));
    }

    public void tickMovement() {
        super.tickMovement();
        if (!world.isClient()) {
            setGrowthAge(growthAge + 1);
        }
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Age", growthAge);
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setGrowthAge(nbt.getInt("Age"));
    }

    private void setGrowthAge(int value) {
        growthAge = value;
        if (growthAge >= MAX_GROWTH_AGE && world.isSpaceEmpty(getBoundingBox().expand(1))) {
            growUp();
        }
    }

    private void increaseAge(int seconds) {
        setGrowthAge(growthAge + seconds * 20);
    }

    private int getTicksUntilGrowth() {
        return Math.max(0, MAX_GROWTH_AGE - growthAge);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (stack.isIn(EnderscapeItems.DRIFTER_FOOD)) {
            eat(player, hand, stack);
            increaseAge(PassiveEntity.toGrowUpAge(getTicksUntilGrowth()));
            world.addParticle(ParticleTypes.HAPPY_VILLAGER, getParticleX(1), getRandomBodyY() + 0.5, getParticleZ(1), 0, 0, 0);
            return ActionResult.SUCCESS;
        } else {
            return super.interactMob(player, hand);
        }
    }

    private void growUp() {
        if (world instanceof ServerWorld server) {
            DrifterEntity mob = EnderscapeEntities.DRIFTER.create(world);
            
            mob.refreshPositionAndAngles(getX(), getY(), getZ(), getYaw(), getPitch());
            mob.initialize(server, world.getLocalDifficulty(mob.getBlockPos()), SpawnReason.CONVERSION, null, null);
            mob.setAiDisabled(isAiDisabled());

            if (hasCustomName()) {
                mob.setCustomName(getCustomName());
                mob.setCustomNameVisible(isCustomNameVisible());
            }

            if (isLeashed()) detachLeash(true, true);
            if (hasVehicle()) mob.startRiding(getVehicle());

            mob.setPersistent();
            server.spawnEntityAndPassengers(mob);
            discard();
        }
    }

    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.5F;
    }

    protected SoundEvent getAmbientSound() {
        return EnderscapeSounds.ENTITY_DRIFTLET_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return EnderscapeSounds.ENTITY_DRIFTLET_HURT;
    }

    protected SoundEvent getDeathSound() {
        return EnderscapeSounds.ENTITY_DRIFTLET_DEATH;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }
}