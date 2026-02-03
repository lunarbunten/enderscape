package net.bunten.enderscape.entity.rubblemite;

import com.mojang.serialization.Dynamic;
import io.netty.buffer.ByteBuf;
import net.bunten.enderscape.entity.ai.EnderscapeMemory;
import net.bunten.enderscape.registry.EnderscapeEntityDataSerializers;
import net.bunten.enderscape.registry.EnderscapeEntitySounds;
import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.bunten.enderscape.registry.tag.EnderscapeDamageTypeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;

public class Rubblemite extends Monster {

    private static final EntityDataAccessor<State> DATA_STATE = SynchedEntityData.defineId(Rubblemite.class, EnderscapeEntityDataSerializers.RUBBLEMITE_STATE_SERIALIZER.get());
    public static final EntityDataAccessor<Integer> VARIANT_DATA = SynchedEntityData.defineId(Rubblemite.class, EntityDataSerializers.INT);

    public final AnimationState insideShellAnimationState = new AnimationState();
    public final AnimationState dashAnimationState = new AnimationState();
    public final AnimationState prepareDashAnimationState = new AnimationState();

    public Rubblemite(EntityType<? extends Rubblemite> type, Level world) {
        super(type, world);
        setPathfindingMalus(PathType.WATER, -1);
        xpReward = 5;
    }

    public static enum State {
        IDLING(0),
        INSIDE_SHELL(1),
        PREPARING_DASH(2),
        DASHING(3);

        public static final IntFunction<Rubblemite.State> BY_ID = ByIdMap.continuous(Rubblemite.State::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final StreamCodec<ByteBuf, Rubblemite.State> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Rubblemite.State::id);
        private final int id;

        private State(final int j) {
            this.id = j;
        }

        public int id() {
            return this.id;
        }
    }

    public State getState() {
        return entityData.get(DATA_STATE);
    }

    public Rubblemite setState(State state) {
        entityData.set(DATA_STATE, state);
        return this;
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
        return Brain.provider(RubblemiteAI.MEMORY_TYPES.get(), RubblemiteAI.SENSOR_TYPES.get());
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
    protected void customServerAiStep() {
        if (level() instanceof ServerLevel server) {
            ProfilerFiller profiler = server.getProfiler();

            profiler.push("rubblemiteBrain");
            getBrain().tick(server, this);
            profiler.pop();

            profiler.push("rubblemiteActivityUpdate");
            RubblemiteAI.updateActivity(this);
            profiler.pop();
        }

        super.customServerAiStep();
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData groupData) {
        RubblemiteVariant.set(this, RubblemiteVariant.pickForSpawning(random, level.getBiome(blockPosition())));
        return super.finalizeSpawn(level, difficulty, spawnType, groupData);
    }

    public static boolean canSpawn(EntityType<Rubblemite> type, ServerLevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return level.getDifficulty() != Difficulty.PEACEFUL
                && (MobSpawnType.isSpawner(reason) || level.getBlockState(pos.below()).is(EnderscapeBlockTags.RUBBLEMITE_SPAWNABLE_ON))
                && (MobSpawnType.ignoresLightRequirements(reason) || isDarkEnoughToSpawn(level, pos, random));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_STATE, State.IDLING);
        builder.define(VARIANT_DATA, RubblemiteVariant.END_STONE.getId());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putInt(RubblemiteVariant.KEY, RubblemiteVariant.get(this).getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        RubblemiteVariant.set(this, RubblemiteVariant.byId(tag.getInt(RubblemiteVariant.KEY)));
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (DATA_STATE.equals(accessor)) {
            resetAnimations();
            switch (getState()) {
                case INSIDE_SHELL:
                    insideShellAnimationState.startIfStopped(tickCount);
                    break;
                case PREPARING_DASH:
                    prepareDashAnimationState.startIfStopped(tickCount);
                    break;
                case DASHING:
                    dashAnimationState.startIfStopped(tickCount);
                default:
                    break;
            }

            refreshDimensions();
        }

        super.onSyncedDataUpdated(accessor);
    }

    private void resetAnimations() {
        insideShellAnimationState.stop();
        prepareDashAnimationState.stop();
        dashAnimationState.stop();
    }

    public boolean isInsideShell() {
        return getState() == State.INSIDE_SHELL || brain.hasMemoryValue(EnderscapeMemory.RUBBLEMITE_HIDING_DURATION.get());
    }

    public boolean isDashing() {
        return getState() == State.DASHING && !brain.hasMemoryValue(EnderscapeMemory.RUBBLEMITE_DASH_ON_COOLDOWN.get());
    }

    public boolean isPreparingToDash() {
        return getState() == State.PREPARING_DASH || brain.hasMemoryValue(EnderscapeMemory.RUBBLEMITE_PREPARING_DASH.get());
    }

    public void enterShell(int value) {
        setState(State.INSIDE_SHELL);
        if (value > 0) brain.setMemory(EnderscapeMemory.RUBBLEMITE_HIDING_DURATION.get(), value);
    }

    public void exitShell() {
        setState(State.IDLING);
        brain.eraseMemory(EnderscapeMemory.RUBBLEMITE_HIDING_DURATION.get());
        brain.setMemoryWithExpiry(EnderscapeMemory.RUBBLEMITE_HIDING_ON_COOLDOWN.get(), true, 100);
        brain.setMemoryWithExpiry(EnderscapeMemory.RUBBLEMITE_DASH_ON_COOLDOWN.get(), true, 20);
    }

    public boolean canHideInShell() {
        return getState() == State.IDLING && !RubblemiteAI.isShellCoolingDown(this);
    }

    public boolean shouldStopDashing() {
        return onGround() || isInWaterOrRain() || getVehicle() != null;
    }

    public void prepareDash() {
        setState(State.PREPARING_DASH);
        getBrain().setMemory(EnderscapeMemory.RUBBLEMITE_PREPARING_DASH.get(), true);
        getBrain().setMemory(EnderscapeMemory.RUBBLEMITE_PREPARING_DASH_TIME.get(), 5);

        playSound(EnderscapeEntitySounds.RUBBLEMITE_PREPARE_DASH.get(), 1, 1);
    }

    public void dash() {
        setState(State.DASHING);
        getBrain().eraseMemory(EnderscapeMemory.RUBBLEMITE_PREPARING_DASH.get());
        getBrain().setMemoryWithExpiry(EnderscapeMemory.RUBBLEMITE_DASH_ON_COOLDOWN.get(), true, 40);

        Vec3 vec = getLookAngle();
        vec = vec.multiply(1.4, 0, 1.4).add(0, 0.33F, 0);
        setDeltaMovement(vec);

        playSound(EnderscapeEntitySounds.RUBBLEMITE_HOP.get(), 1, 1);

        if (level() instanceof ServerLevel server) {
            Vec3 pos = position();
            server.sendParticles(ParticleTypes.POOF, pos.x, pos.y + 0.5, pos.z, 5, 0, 0, 0, 0.1);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isAlive() && source.is(EnderscapeDamageTypeTags.RUBBLEMITES_CAN_BLOCK)) {
            if (amount >= 12 && super.hurt(source, amount)) {
                return true;
            } else {
                if (getState() != State.IDLING && super.hurt(source, 0)) {
                    return true;
                }
                if (canHideInShell() && super.hurt(source, amount)) {
                    enterShell(40);
                    return true;
                }
            }
        }

        return super.hurt(source, amount);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        enterShell(0);
    }

    @Override
    public void travel(Vec3 vec) {
        if (getState() != State.IDLING) {
            if (getNavigation().getPath() != null) getNavigation().stop();
            vec = Vec3.ZERO;
        }
        super.travel(vec);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return getState() != State.IDLING ? null : EnderscapeEntitySounds.RUBBLEMITE_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return getState() != State.IDLING ? EnderscapeEntitySounds.RUBBLEMITE_SHIELD.get() : EnderscapeEntitySounds.RUBBLEMITE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return EnderscapeEntitySounds.RUBBLEMITE_DEATH.get();
    }

    protected SoundEvent getStepSound() {
        return EnderscapeEntitySounds.RUBBLEMITE_STEP.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(getStepSound(), 0.15F, Mth.nextFloat(random, 0.9F, 1.1F));
    }
}