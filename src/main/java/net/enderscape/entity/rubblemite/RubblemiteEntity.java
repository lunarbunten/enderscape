package net.enderscape.entity.rubblemite;

import net.enderscape.registry.EndSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RubblemiteEntity extends HostileEntity {
    private static final TrackedData<Boolean> DASHING = DataTracker.registerData(RubblemiteEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private int shellTicks;
    private int sinceShellTicks;
    private int eatCooldown;

    public RubblemiteEntity(EntityType<? extends RubblemiteEntity> type, World world) {
        super(type, world);
        setPathfindingPenalty(PathNodeType.WATER, -1);
        eatCooldown = nextFoodTime();
        sinceShellTicks = 100;
        experiencePoints = 5;
    }

    protected void initGoals() {
        goalSelector.add(3, new DashAtTargetGoal(this));
        goalSelector.add(4, new RubblemiteAttackGoal(this, 1, false));
        goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 8));
        goalSelector.add(6, new WanderAroundFarGoal(this, 0.6D));
        goalSelector.add(7, new LookAroundGoal(this));
        targetSelector.add(1, (new RevengeGoal(this)).setGroupRevenge());
        targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(DASHING, false);
    }

    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        tag.putBoolean("Dashing", isDashing());
    }

    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        setDashing(tag.getBoolean("Dashing"));
    }

    public void travel(Vec3d vec) {
        if (insideShell()) {
            if (getNavigation().getCurrentPath() != null) {
                getNavigation().stop();
            }
            vec = Vec3d.ZERO;
        }
        super.travel(vec);
    }

    private int nextFoodTime() {
        return 1000 + random.nextInt(1000);
    }

    public boolean canHideInShell() {
        return sinceShellTicks >= 100;
    }

    public int getShellTicks() {
        return shellTicks;
    }

    public int getSinceShellTicks() {
        return sinceShellTicks;
    }

    public int getEatCooldown() {
        return eatCooldown;
    }

    public boolean insideShell() {
        return shellTicks > 0;
    }

    public boolean isHungry() {
        return eatCooldown == 0;
    }

    public boolean isDashing() {
        return dataTracker.get(DASHING);
    }

    public void setDashing(boolean value) {
        dataTracker.set(DASHING, value);
    }

    public int setShellTicks(int value) {
        return shellTicks = value;
    }

    public int setSinceShellTicks(int value) {
        return sinceShellTicks = value;
    }

    public int setEatCooldown(int value) {
        return eatCooldown = value;
    }

    public void setInsideShell(int value) {
        setShellTicks(value);
        setSinceShellTicks(0);
    }

    public void eat(ItemStack stack) {
        super.eatFood(world, stack);
        setHealth(getMaxHealth());
        setEatCooldown(nextFoodTime());
    }

    public double getHeightOffset() {
        return 0.1D;
    }

    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.13F;
    }

    public boolean hurtByWater() {
        return !insideShell();
    }

    protected boolean canClimb() {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return EndSounds.ENTITY_RUBBLEMITE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return EndSounds.ENTITY_RUBBLEMITE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return EndSounds.ENTITY_RUBBLEMITE_DEATH;
    }

    public boolean damage(DamageSource source, float amount) {
        if (insideShell()) {
            if (amount >= 12) {
                return super.damage(source, amount);
            } else {
                if (source != DamageSource.DROWN) {
                    if (source.getSource() instanceof LivingEntity target) {
                        takeKnockback(0.5D, target.getX() - getX(), target.getZ() - getZ());
                    }
                    playSound(EndSounds.ENTITY_RUBBLEMITE_SHIELD, 1, 1);
                }
                return false;
            }
        } else {
            if (source.getSource() instanceof LivingEntity target && amount < 12 && canHideInShell()) {
                playSound(EndSounds.ENTITY_RUBBLEMITE_SHIELD, 1, 1);
                setInsideShell(40);
                takeKnockback(0.5D, target.getX() - getX(), target.getZ() - getZ());
                return false;
            }
        }
        return super.damage(source, amount);
    }

    public void dash() {
        if (!insideShell()) {
            Vec3d vec = getRotationVector();
            double x = vec.x * (10 * 0.12D);
            double z = vec.z * (10 * 0.12D);
            addVelocity(x, 0.44F, z);
            playSound(EndSounds.ENTITY_RUBBLEMITE_HOP, 1, 1);
            setDashing(true);
            if (world instanceof ServerWorld) {
                Vec3d pos = getPos();
                ((ServerWorld) world).spawnParticles(ParticleTypes.POOF, pos.x, pos.y + 0.5, pos.z, 5, 0, 0, 0, 0.1);
            }
        }
    }

    public boolean shouldStopDashing() {
        return isOnGround() || isTouchingWaterOrRain() || insideShell();
    }

    public void tick() {
        if (isTouchingWaterOrRain()) {
            if (canHideInShell()) {
                setInsideShell(40);
            }
        }
        if (isOnGround() || isTouchingWaterOrRain()) {
            setDashing(false);
        }
        if (insideShell()) {
            setAttacking(false);
            sinceShellTicks = 0;
            if (shellTicks == 1) {
                playSound(EndSounds.ENTITY_RUBBLEMITE_EXTRUDE, 1, 1);
            }
            shellTicks--;
        } else {
            sinceShellTicks++;
        }
        if (eatCooldown > 0) {
            eatCooldown--;
        }
        super.tick();
    }

    public boolean tryAttack(Entity target) {
        if (!insideShell()) {
            if (target.getPos().getY() > getPos().getY() + 0.3F) {
                return false;
            } else {
                float f = (float) getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                boolean bl = target.damage(DamageSource.mob(this), f);
                if (bl) {
                    applyDamageEffects(this, target);
                    onAttacking(target);
                }
                return bl;
            }
        } else {
            return false;
        }
    }

    public EntityGroup getGroup() {
        return EntityGroup.ARTHROPOD;
    }
}