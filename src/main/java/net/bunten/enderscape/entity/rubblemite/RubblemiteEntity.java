package net.bunten.enderscape.entity.rubblemite;

import net.bunten.enderscape.registry.EnderscapeEntities;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.util.MathUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;

public class RubblemiteEntity extends HostileEntity {
    private static final String RUBBLEMITE_FLAGS_KEY = "RubblemiteFlags";
    private static final String REMAINING_SHELL_TIME_KEY = "RemainingShellTime";
    private static final String TIME_SINCE_SHELL_KEY = "TimeSinceShell";  

    private static final TrackedData<Integer> RUBBLEMITE_FLAGS = DataTracker.registerData(RubblemiteEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private int remainingShellTime;
    private int timeSinceShell;

    public RubblemiteEntity(EntityType<? extends RubblemiteEntity> type, World world) {
        super(type, world);
        timeSinceShell = 100;
        experiencePoints = 5;
        setPathfindingPenalty(PathNodeType.WATER, -1);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return createMobAttributes()
        .add(EntityAttributes.GENERIC_MAX_HEALTH, 6)
        .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32)
        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.28)
        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10)
        .add(EntityAttributes.GENERIC_ARMOR, 15);
    }

    protected void initGoals() {
        goalSelector.add(3, new DashAtTargetGoal(this));
        goalSelector.add(4, new RubblemiteAttackGoal(this, 1, false));
        goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 8));
        goalSelector.add(6, new WanderAroundFarGoal(this, 0.6D));
        goalSelector.add(7, new LookAroundGoal(this));
        targetSelector.add(1, new RevengeGoal(this, new Class[0]).setGroupRevenge(new Class[0]));
        targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(RUBBLEMITE_FLAGS, 0);
    }

    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);

        tag.putInt(RUBBLEMITE_FLAGS_KEY, getFlags());
        tag.putInt(REMAINING_SHELL_TIME_KEY, remainingShellTime);
        tag.putInt(TIME_SINCE_SHELL_KEY, timeSinceShell);
    }

    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);

        setFlags(tag.getInt(RUBBLEMITE_FLAGS_KEY));
        setRemainingShellTime(tag.getInt(REMAINING_SHELL_TIME_KEY));
        setTimeSinceShell(tag.getInt(TIME_SINCE_SHELL_KEY));
    }

    protected int getFlags() {
        return dataTracker.get(RUBBLEMITE_FLAGS);
    }

    public void setFlags(int value) {
        dataTracker.set(RUBBLEMITE_FLAGS, value);
    }

    public boolean isInsideShell() {
        return getFlags() == 1 || remainingShellTime > 0 && timeSinceShell < 100;
    }

    public boolean isDashing() {
        return getFlags() == 2;
    }

    public int setRemainingShellTime(int value) {
        return remainingShellTime = value;
    }

    public int setTimeSinceShell(int value) {
        return timeSinceShell = value;
    }

    public void enterShell(int value) {
        setFlags(1);
        setRemainingShellTime(value);
        setTimeSinceShell(0);
    }

    public void exitShell() {
        setFlags(0);
        setRemainingShellTime(0);
    }

    public boolean canHideInShell() {
        return !isInsideShell() && timeSinceShell >= 100;
    }

    public boolean shouldStopDashing() {
        return isOnGround() || isTouchingWaterOrRain();
    }

    public void dash() {
        if (getFlags() == 0) {
            
            Vec3d vec = getRotationVector();
            vec = vec.multiply(1.4, 0, 1.4).add(0, 0.33F, 0);
            setVelocity(vec);

            playSound(EnderscapeSounds.ENTITY_RUBBLEMITE_HOP, 1, 1);

            if (world instanceof ServerWorld server) {
                Vec3d pos = getPos();
                server.spawnParticles(ParticleTypes.POOF, pos.x, pos.y + 0.5, pos.z, 5, 0, 0, 0, 0.1);
            }

            setFlags(2);
        }
    }

    protected void onDamageBlocked(Entity attacker) {
        var knockback = 0.5D;
        if (attacker instanceof LivingEntity mob) {
            knockback += EnchantmentHelper.getKnockback(mob) * 0.2; 
        }
        takeKnockback(knockback, attacker.getX() - getX(), attacker.getZ() - getZ());
        playSound(EnderscapeSounds.ENTITY_RUBBLEMITE_SHIELD, 1, 1);
    }

    protected boolean allowsBlockingDamage(DamageSource source, float amount) {
        return isAlive() && source.getSource() instanceof LivingEntity;
    }

    public boolean damage(DamageSource source, float amount) {
        var attacker = source.getSource();
        if (allowsBlockingDamage(source, amount)) {
            if (amount >= 12 && super.damage(source, amount)) {
                return true;
            } else {
                if (isInsideShell()) {
                    onDamageBlocked(attacker);
                    return false;
                } else if (timeSinceShell >= 100) {
                    enterShell(40);
                    onDamageBlocked(attacker);
                    return false;
                }
            }

            if (random.nextInt(1000) == 0) {
                spawnHorde();
            }
        }
 
        return super.damage(source, amount);
    }

    protected void spawnHorde() {
        if (getTarget() != null && world instanceof ServerWorld server) {
            int hordeSize = MathUtil.nextInt(random, 8, 16);
            for (int i = 0; i < hordeSize; i++) {
                trySpawnAlly(server);
            }
        }
    }
    
    protected void trySpawnAlly(ServerWorld server) {
        var target = getTarget();
        var type = EnderscapeEntities.RUBBLEMITE;
        var rubblemite = new RubblemiteEntity(type, world);

        for (int l = 0; l < 50; ++l) {
            int x = MathHelper.floor(getX()) + MathHelper.nextInt(random, 7, 20) * MathHelper.nextInt(random, -1, 1);
            int y = MathHelper.floor(getY()) + MathHelper.nextInt(random, 7, 5) * MathHelper.nextInt(random, -1, 1);
            int z = MathHelper.floor(getZ()) + MathHelper.nextInt(random, 7, 20) * MathHelper.nextInt(random, -1, 1);

            var pos = new BlockPos(x, y, z);

            if (!SpawnHelper.canSpawn(SpawnRestriction.getLocation(type), world, pos, type)) {
                continue;
            }

            rubblemite.setPosition(x, y, z);

            if (world.isPlayerInRange(x, y, z, 7) || !world.doesNotIntersectEntities(rubblemite) || !world.isSpaceEmpty(rubblemite) || world.containsFluid(rubblemite.getBoundingBox())) {
                continue;
            }

            rubblemite.setTarget(target);
            rubblemite.initialize(server, world.getLocalDifficulty(rubblemite.getBlockPos()), SpawnReason.REINFORCEMENT, null, null);

            server.spawnEntityAndPassengers(rubblemite);
            break;
        }
    }

    public void tick() {
        if (isAlive()) {
            if (isTouchingWaterOrRain()) {
                setRemainingShellTime(40);
            }
            
            if (remainingShellTime > 0) {
                setFlags(1);
                timeSinceShell = 0;
                if (remainingShellTime == 1) {
                    playSound(EnderscapeSounds.ENTITY_RUBBLEMITE_EXTRUDE, 1, 1);
                }
    
                remainingShellTime--;
            } else {
                if (isInsideShell() || shouldStopDashing()) {
                    setFlags(0);
                }
    
                timeSinceShell++;
            }
        } else {
            setFlags(0);
            remainingShellTime = 0;
            timeSinceShell = 0;
        }

        super.tick();
    }

    public void travel(Vec3d vec) {
        if (isInsideShell()) {
            if (getNavigation().getCurrentPath() != null) {
                getNavigation().stop();
            }
            vec = Vec3d.ZERO;
        }
        super.travel(vec);
    }

    public boolean tryAttack(Entity target) {
        return isInsideShell() ? false : super.tryAttack(target);
    }

    public EntityGroup getGroup() {
        return EntityGroup.ARTHROPOD;
    }

    public double getHeightOffset() {
        return 0.1D;
    }

    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.13F;
    }

    public boolean hurtByWater() {
        return false;
    }

    protected boolean canClimb() {
        return true;
    }

    protected SoundEvent getAmbientSound() {
        return isInsideShell() ? null : EnderscapeSounds.ENTITY_RUBBLEMITE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return EnderscapeSounds.ENTITY_RUBBLEMITE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return EnderscapeSounds.ENTITY_RUBBLEMITE_DEATH;
    }
}