package net.bunten.enderscape.entity.rubblemite;

import com.mojang.serialization.Dynamic;
import net.bunten.enderscape.entity.ai.EnderscapeMemory;
import net.bunten.enderscape.registry.EnderscapeEntitySounds;
import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.bunten.enderscape.registry.tag.EnderscapeDamageTypeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Rubblemite extends Monster {
    private static final String RUBBLEMITE_FLAGS_KEY = "RubblemiteFlags";
    
    public static final int DEFAULT_FLAG = 0;
    public static final int INSIDE_SHELL_FLAG = 1;
    public static final int DASHING_FLAG = 2;

    private static final EntityDataAccessor<Integer> RUBBLEMITE_FLAGS = SynchedEntityData.defineId(Rubblemite.class, EntityDataSerializers.INT);

    public Rubblemite(EntityType<? extends Rubblemite> type, Level world) {
        super(type, world);
        setPathfindingMalus(PathType.WATER, -1);
        xpReward = 5;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes()
                .add(Attributes.ARMOR, 15)
                .add(Attributes.ATTACK_DAMAGE, 6)
                .add(Attributes.MAX_HEALTH, 6)
                .add(Attributes.MOVEMENT_SPEED, 0.28)
                .add(Attributes.SAFE_FALL_DISTANCE, 6);
    }

    @Override
    protected Brain.Provider<Rubblemite> brainProvider() {
        return Brain.provider(RubblemiteAI.MEMORY_TYPES, RubblemiteAI.SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return RubblemiteAI.makeBrain(brainProvider().makeBrain(dynamic));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Brain<Rubblemite> getBrain() {
        return (Brain<Rubblemite>) super.getBrain();
    }

    @Override
    protected void customServerAiStep(ServerLevel serverLevel) {
        ProfilerFiller profiler = Profiler.get();

        profiler.push("rubblemiteBrain");
        getBrain().tick(serverLevel, this);
        profiler.pop();

        profiler.push("rubblemiteActivityUpdate");
        RubblemiteAI.updateActivity(this);
        profiler.pop();

        super.customServerAiStep(serverLevel);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason type, @Nullable SpawnGroupData groupData) {
        RubblemiteVariant.set(this, RubblemiteVariant.pickForSpawning(random, level.getBiome(blockPosition())));
        return super.finalizeSpawn(level, difficulty, type, groupData);
    }

    public static boolean canSpawn(EntityType<Rubblemite> type, LevelAccessor level, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        return level.getDifficulty() != Difficulty.PEACEFUL && (level.getBlockState(pos.below()).is(EnderscapeBlockTags.RUBBLEMITE_SPAWNABLE_ON) || EntitySpawnReason.isSpawner(reason));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(RUBBLEMITE_FLAGS, DEFAULT_FLAG);
        builder.define(RubblemiteVariant.DATA, RubblemiteVariant.END_STONE.getId());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putInt(RUBBLEMITE_FLAGS_KEY, getFlags());
        tag.putInt(RubblemiteVariant.KEY, RubblemiteVariant.get(this).getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        setFlags(tag.getInt(RUBBLEMITE_FLAGS_KEY));
        RubblemiteVariant.set(this, RubblemiteVariant.byId(tag.getInt(RubblemiteVariant.KEY)));
    }

    public int getFlags() {
        return entityData.get(RUBBLEMITE_FLAGS);
    }

    public void setFlags(int value) {
        entityData.set(RUBBLEMITE_FLAGS, value);
    }

    public boolean isInsideShell() {
        return getFlags() == INSIDE_SHELL_FLAG || brain.hasMemoryValue(EnderscapeMemory.RUBBLEMITE_HIDING_DURATION);
    }

    public boolean isDashing() {
        return getFlags() == DASHING_FLAG && !brain.hasMemoryValue(EnderscapeMemory.RUBBLEMITE_DASH_ON_COOLDOWN);
    }

    public void enterShell(int value) {
        setFlags(INSIDE_SHELL_FLAG);
        if (value > 0) brain.setMemory(EnderscapeMemory.RUBBLEMITE_HIDING_DURATION, value);
    }

    public void exitShell() {
        setFlags(DEFAULT_FLAG);
        brain.eraseMemory(EnderscapeMemory.RUBBLEMITE_HIDING_DURATION);
        brain.setMemoryWithExpiry(EnderscapeMemory.RUBBLEMITE_HIDING_ON_COOLDOWN, true, 100);
        brain.setMemoryWithExpiry(EnderscapeMemory.RUBBLEMITE_DASH_ON_COOLDOWN, true, 20);
    }

    public boolean canHideInShell() {
        return getFlags() == DEFAULT_FLAG && !RubblemiteAI.isShellCoolingDown(this);
    }

    public boolean shouldStopDashing() {
        return onGround() || isInWaterOrRain() || getVehicle() != null;
    }

    public void dash() {
        Vec3 vec = getLookAngle();
        vec = vec.multiply(1.4, 0, 1.4).add(0, 0.33F, 0);
        setDeltaMovement(vec);

        playSound(EnderscapeEntitySounds.RUBBLEMITE_HOP, 1, 1);

        if (level() instanceof ServerLevel server) {
            Vec3 pos = position();
            server.sendParticles(ParticleTypes.POOF, pos.x, pos.y + 0.5, pos.z, 5, 0, 0, 0, 0.1);
        }

        setFlags(DASHING_FLAG);
    }

    protected void onDamageBlocked(DamageSource source) {
        if (source.getDirectEntity() != null) {
            float knockback = 1;

            if (source.getDirectEntity() instanceof LivingEntity living) {
                Registry<Enchantment> enchantments = this.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
                knockback += EnchantmentHelper.getEnchantmentLevel(enchantments.getOrThrow(Enchantments.KNOCKBACK), living);
            }

            double x = source.getDirectEntity().getX() - getX();
            double z = source.getDirectEntity().getZ() - getZ();

            while (x * x + z * z < 1.0E-4) {
                x = (Math.random() - Math.random()) * 0.01;
                z = (Math.random() - Math.random()) * 0.01;
            }

            knockback(knockback * 0.5F, x, z);
        }

        playSound(EnderscapeEntitySounds.RUBBLEMITE_SHIELD, 1, 1);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (isAlive() && source.is(EnderscapeDamageTypeTags.RUBBLEMITES_CAN_BLOCK)) {
            if (amount >= 12 && super.hurtServer(level, source, amount)) {
                return true;
            } else {
                if (isDashing() || isInsideShell()) {
                    onDamageBlocked(source);
                    return false;
                }
                if (canHideInShell() && super.hurtServer(level, source, amount)) {
                    enterShell(40);
                    return true;
                }
            }
        }

        return super.hurtServer(level, source, amount);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        enterShell(0);
    }

    @Override
    public void travel(Vec3 vec) {
        if (isInsideShell()) {
            if (getNavigation().getPath() != null) getNavigation().stop();
            vec = Vec3.ZERO;
        }
        super.travel(vec);
    }

    @Override
    public void lookAt(Entity entity, float f, float g) {
        if (isInsideShell() || isDashing()) return;
        super.lookAt(entity, f, g);
    }

    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity entity) {
        return !isInsideShell() && super.doHurtTarget(serverLevel, entity);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return isInsideShell() ? null : EnderscapeEntitySounds.RUBBLEMITE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return EnderscapeEntitySounds.RUBBLEMITE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return EnderscapeEntitySounds.RUBBLEMITE_DEATH;
    }

    protected SoundEvent getStepSound() {
        return EnderscapeEntitySounds.RUBBLEMITE_STEP;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(getStepSound(), 0.15F, Mth.nextFloat(random, 0.9F, 1.1F));
    }
}