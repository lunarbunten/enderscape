package net.enderscape.entity.drifter;

import net.enderscape.entity.rubblemite.RubblemiteEntity;
import net.enderscape.registry.EndEntities;
import net.enderscape.registry.EndItems;
import net.enderscape.registry.EndParticles;
import net.enderscape.registry.EndSounds;
import net.enderscape.util.EndMath;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.List;
import java.util.Random;

public class DrifterEntity extends PassiveEntity {
    private static final TrackedData<Boolean> DRIPPING_JELLY = DataTracker.registerData(DrifterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


    public DrifterEntity(EntityType<? extends DrifterEntity> type, World world) {
        super(type, world);
        moveControl = new DrifterMoveControl(this);
        navigation = new DrifterNavigation(this, world);
        setPathfindingPenalty(PathNodeType.WATER, -1);
        experiencePoints = 1;
    }

    public static <T extends DrifterEntity> boolean canSpawn(EntityType<T> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return EndMath.inRange(pos.getY(), 60, 80);
    }

    protected void initGoals() {
        goalSelector.add(3, new TemptGoal(this, 1.25D, Ingredient.fromTag(EndItems.DRIFTER_FOOD), false));
        goalSelector.add(4, new FleeEntityGoal<>(this, RubblemiteEntity.class, 8, 2.2D, 2.2D));
        goalSelector.add(6, new DrifterWanderGoal(this));
        goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 8));
        goalSelector.add(8, new LookAroundGoal(this));
    }

    public void tick() {
        super.tick();
        if (age > 200 && random.nextInt(15000) == 0) {
            setDrippingJelly(!isDrippingJelly());
        }
        if (isDrippingJelly() && random.nextBoolean()) {
            Vec3d vel = getVelocity();

            double x = vel.x;
            double z = vel.z;

            world.addParticle(EndParticles.DRIPPING_JELLY, getParticleX(0.3D), getRandomBodyY() - 0.25D, getParticleZ(0.3D), x / 2, -0.2F, z / 2);
        }

        List<Entity> list = world.getOtherEntities(this, getBoundingBox().expand(1, 0.5D, 1));
        for (Entity entity : list) {
            if (!entity.isRemoved() && entity instanceof LivingEntity && !(entity instanceof DrifterEntity)) {
                collide((LivingEntity) entity);
            }
        }
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

    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(DRIPPING_JELLY, false);
    }

    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        tag.putBoolean("DrippingJelly", isDrippingJelly());
    }

    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        setDrippingJelly(tag.getBoolean("DrippingJelly"));
    }

    public boolean isDrippingJelly() {
        return dataTracker.get(DRIPPING_JELLY);
    }

    public void setDrippingJelly(boolean value) {
        dataTracker.set(DRIPPING_JELLY, value);
    }

    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource source) {
        return false;
    }

    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 1.08F;
    }

    public boolean hurtByWater() {
        return true;
    }

    public boolean isBaby() {
        return false;
    }

    private void collide(LivingEntity mob) {
        Vec3d vel = mob.getVelocity();
        boolean bl = mob.getPos().getY() >= getPos().getY() + 2F && distanceTo(mob) < 3;
        if (!mob.isSpectator() && vel.y < -0.1F && isAlive() && !mob.isOnGround() && bl) {
            if (!world.isClient()) {
                if (mob.isSneaking()) {
                    mob.setVelocity(vel.x, 0.6F, vel.z);
                    setVelocity(getVelocity().x, -0.4, getVelocity().z);
                } else {
                    mob.setVelocity(vel.x, 1.3F, vel.z);
                    setVelocity(getVelocity().x, 0.3F, getVelocity().z);
                }

                if (mob instanceof ServerPlayerEntity) {
                    ((ServerPlayerEntity) mob).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(mob));
                }
                mob.fallDistance = 0;
                playSound(EndSounds.ENTITY_DRIFTER_BOUNCE, 1, 1);
                damage(DamageSource.FALL, 1);
            }
        }
    }

    public boolean damage(DamageSource source, float amount) {
        int repeat = EndMath.nextInt(random, 4, 8);
        for (int i = 0; i < repeat; i++) {
            world.addParticle(EndParticles.DRIPPING_JELLY, getParticleX(0.5D), getRandomBodyY() - 0.25D, getParticleZ(0.5D), 0, 0, 0);
        }
        return super.damage(source, amount);
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity mob) {
        return !isLeashed();
    }

    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        return world.getBlockState(pos).isAir() ? 1 : 0;
    }

    protected SoundEvent getAmbientSound() {
        return EndSounds.ENTITY_DRIFTER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return EndSounds.ENTITY_DRIFTER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return EndSounds.ENTITY_DRIFTER_DEATH;
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return EndEntities.DRIFTLET.create(world);
    }
}