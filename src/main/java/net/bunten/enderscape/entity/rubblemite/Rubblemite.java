package net.bunten.enderscape.entity.rubblemite;

import java.util.Arrays;
import java.util.Comparator;

import org.jetbrains.annotations.Nullable;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.registry.EnderscapeEntities;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.util.MathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class Rubblemite extends Monster {
    private static final String RUBBLEMITE_FLAGS_KEY = "RubblemiteFlags";
    private static final String REMAINING_SHELL_TIME_KEY = "RemainingShellTime";
    private static final String TIME_SINCE_SHELL_KEY = "TimeSinceShell"; 
    private static final String VARIANT_KEY = "Variant";
    
    private static final int DEFUALT_FLAG = 0;
    private static final int INSIDE_SHELL_FLAG = 1;
    private static final int DASHING_FLAG = 2;

    private static final EntityDataAccessor<Integer> RUBBLEMITE_FLAGS = SynchedEntityData.defineId(Rubblemite.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(Rubblemite.class, EntityDataSerializers.INT);

    private int remainingShellTime;
    private int timeSinceShell;

    public Rubblemite(EntityType<? extends Rubblemite> type, Level world) {
        super(type, world);
        timeSinceShell = 100;
        xpReward = 5;
        setPathfindingMalus(BlockPathTypes.WATER, -1);
    }

    protected void registerGoals() {
        goalSelector.addGoal(3, new DashAtTargetGoal(this));
        goalSelector.addGoal(4, new RubblemiteAttackGoal(this, 1, false));
        goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8));
        goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.6D));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]).setAlertOthers(new Class[0]));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType type, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        setVariant(Variant.getVariant(random));
        return super.finalizeSpawn(world, difficulty, type, groupData, tag);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes()
        .add(Attributes.MAX_HEALTH, 6)
        .add(Attributes.FOLLOW_RANGE, 32)
        .add(Attributes.MOVEMENT_SPEED, 0.28)
        .add(Attributes.ATTACK_DAMAGE, 10)
        .add(Attributes.ARMOR, 15);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(RUBBLEMITE_FLAGS, DEFUALT_FLAG);
        entityData.define(VARIANT, Variant.END_STONE.getId());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putInt(RUBBLEMITE_FLAGS_KEY, getFlags());
        tag.putInt(REMAINING_SHELL_TIME_KEY, remainingShellTime);
        tag.putInt(TIME_SINCE_SHELL_KEY, timeSinceShell);
        tag.putInt(VARIANT_KEY, getVariant().getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        setFlags(tag.getInt(RUBBLEMITE_FLAGS_KEY));
        setRemainingShellTime(tag.getInt(REMAINING_SHELL_TIME_KEY));
        setTimeSinceShell(tag.getInt(TIME_SINCE_SHELL_KEY));
    }

    @Override
    public ResourceLocation getDefaultLootTable() {
        return Enderscape.id("entities/rubblemite/" + getVariant().name);
    }

    public Variant getVariant() {
        return Variant.BY_ID[entityData.get(VARIANT)];
    }

    private void setVariant(Variant variant) {
        entityData.set(VARIANT, variant.getId());
    }

    protected int getFlags() {
        return entityData.get(RUBBLEMITE_FLAGS);
    }

    public void setFlags(int value) {
        entityData.set(RUBBLEMITE_FLAGS, value);
    }

    public boolean isInsideShell() {
        return getFlags() == INSIDE_SHELL_FLAG || remainingShellTime > 0 && timeSinceShell < 100;
    }

    public boolean isDashing() {
        return getFlags() == DASHING_FLAG;
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
        return isOnGround() || isInWaterOrRain();
    }

    public void dash() {
        if (getFlags() == DEFUALT_FLAG) {
            
            Vec3 vec = getLookAngle();
            vec = vec.multiply(1.4, 0, 1.4).add(0, 0.33F, 0);
            setDeltaMovement(vec);

            playSound(EnderscapeSounds.RUBBLEMITE_HOP, 1, 1);

            if (level instanceof ServerLevel server) {
                Vec3 pos = position();
                server.sendParticles(ParticleTypes.POOF, pos.x, pos.y + 0.5, pos.z, 5, 0, 0, 0, 0.1);
            }

            setFlags(2);
        }
    }

    protected void onDamageBlocked(Entity attacker) {
        var knockback = 0.5D;
        if (attacker instanceof LivingEntity mob) {
            knockback += EnchantmentHelper.getKnockbackBonus(mob) * 0.2; 
        }
        knockback(knockback, attacker.getX() - getX(), attacker.getZ() - getZ());
        playSound(EnderscapeSounds.RUBBLEMITE_SHIELD, 1, 1);
    }

    protected boolean allowsBlockingDamage(DamageSource source, float amount) {
        return isAlive() && source.getDirectEntity() instanceof LivingEntity;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        var attacker = source.getDirectEntity();
        if (allowsBlockingDamage(source, amount)) {
            if (amount >= 12 && super.hurt(source, amount)) {
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
 
        return super.hurt(source, amount);
    }

    protected void spawnHorde() {
        if (getTarget() != null && level instanceof ServerLevel server) {
            int hordeSize = MathUtil.nextInt(random, 8, 16);
            for (int i = 0; i < hordeSize; i++) {
                trySpawnAlly(server);
            }
        }
    }
    
    protected void trySpawnAlly(ServerLevel server) {
        var target = getTarget();
        var type = EnderscapeEntities.RUBBLEMITE;
        var rubblemite = new Rubblemite(type, level);

        for (int l = 0; l < 50; ++l) {
            int x = MathUtil.floor(getX()) + MathUtil.nextInt(random, 7, 20) * MathUtil.nextInt(random, -1, 1);
            int y = MathUtil.floor(getY()) + MathUtil.nextInt(random, 7, 5) * MathUtil.nextInt(random, -1, 1);
            int z = MathUtil.floor(getZ()) + MathUtil.nextInt(random, 7, 20) * MathUtil.nextInt(random, -1, 1);

            var pos = new BlockPos(x, y, z);

            if (!NaturalSpawner.isSpawnPositionOk(SpawnPlacements.getPlacementType(type), level, pos, type)) {
                continue;
            }

            rubblemite.setPos(x, y, z);

            if (level.hasNearbyAlivePlayer(x, y, z, 7) || !level.isUnobstructed(rubblemite) || !level.noCollision(rubblemite) || level.containsAnyLiquid(rubblemite.getBoundingBox())) {
                continue;
            }

            rubblemite.setTarget(target);
            rubblemite.finalizeSpawn(server, level.getCurrentDifficultyAt(rubblemite.blockPosition()), MobSpawnType.REINFORCEMENT, null, null);

            server.addFreshEntityWithPassengers(rubblemite);
            break;
        }
    }

    @Override
    public void aiStep() {
        if (isAlive()) {
            if (isInWaterOrRain()) {
                setRemainingShellTime(40);
            }
            
            if (remainingShellTime > 0) {
                setFlags(1);
                timeSinceShell = 0;
                if (remainingShellTime == 1) {
                    playSound(EnderscapeSounds.RUBBLEMITE_EXTRUDE, 1, 1);
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

        super.aiStep();
    }

    @Override
    public void travel(Vec3 vec) {
        if (isInsideShell()) {
            if (getNavigation().getPath() != null) {
                getNavigation().stop();
            }
            vec = Vec3.ZERO;
        }
        super.travel(vec);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        return isInsideShell() ? false : super.doHurtTarget(target);
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public double getMyRidingOffset() {
        return 0.1D;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.13F;
    }

    @Override
    public boolean isSensitiveToWater() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return isInsideShell() ? null : EnderscapeSounds.RUBBLEMITE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return EnderscapeSounds.RUBBLEMITE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return EnderscapeSounds.RUBBLEMITE_DEATH;
    }

    @FunctionalInterface
    static interface BooleanFunction<T> {
        boolean get(T value);
    }

    public static enum Variant {
        END_STONE(0, "end_stone", (random) -> random.nextFloat() > 0.2F),
        VERADITE(1, "veradite", (random) -> true);

        public static final Variant[] BY_ID = Arrays.stream(Variant.values()).sorted(Comparator.comparingInt(Variant::getId)).toArray(Variant[]::new);
        private final int id;
        private final String name;
        private final BooleanFunction<RandomSource> function;

        private Variant(int id, String name, BooleanFunction<RandomSource> function) {
            this.id = id;
            this.name = name;
            this.function = function;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public boolean applies(RandomSource random) {
            return function.get(random);
        }

        private static Variant getVariant(RandomSource random) {
            for (Variant var : Variant.values()) {
                if (var.applies(random)) return var;
            }

            return Variant.END_STONE;
        }
    }
}