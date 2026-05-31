package net.bunten.enderscape.entity.rubblemite;

import com.mojang.serialization.Dynamic;
import io.netty.buffer.ByteBuf;
import net.bunten.enderscape.entity.ai.EnderscapeMemory;
import net.bunten.enderscape.registry.*;
import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.bunten.enderscape.registry.tag.EnderscapeDamageTypeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.entity.variant.VariantUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;

public class Rubblemite extends Monster {

    private static final EntityDataAccessor<Holder<RubblemiteVariant>> DATA_VARIANT_ID = SynchedEntityData.defineId(Rubblemite.class, EnderscapeEntityDataSerializers.RUBBLEMITE_VARIANT_SERIALIZER);
    private static final EntityDataAccessor<State> DATA_STATE = SynchedEntityData.defineId(Rubblemite.class, EnderscapeEntities.RUBBLEMITE_STATE);

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
    protected Brain<?> makeBrain(Brain.Packed packed) {
        return RubblemiteAI.makeBrain(this, packed);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Brain<Rubblemite> getBrain() {
        return (Brain<Rubblemite>) super.getBrain();
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        ProfilerFiller profiler = Profiler.get();

        profiler.push("rubblemiteBrain");
        getBrain().tick(level, this);
        profiler.pop();

        profiler.push("rubblemiteActivityUpdate");
        RubblemiteAI.updateActivity(this);
        profiler.pop();

        super.customServerAiStep(level);
    }

    public Holder<RubblemiteVariant> getVariant() {
        return entityData.get(DATA_VARIANT_ID);
    }

    public void setVariant(Holder<RubblemiteVariant> holder) {
        entityData.set(DATA_VARIANT_ID, holder);
    }

    public Identifier getTexture() {
        RubblemiteVariant variant = getVariant().value();
        return variant.assetInfo().asset().texturePath();
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean bl) {
        super.dropCustomDeathLoot(level, source, bl);

        getVariant().value().extraDropItems().ifPresent(key -> {
            LootTable table = level.getServer().reloadableRegistries().getLootTable(key);
            LootParams.Builder builder = new LootParams.Builder(level)
                    .withParameter(LootContextParams.THIS_ENTITY, this)
                    .withParameter(LootContextParams.ORIGIN, position())
                    .withParameter(LootContextParams.DAMAGE_SOURCE, source)
                    .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, source.getEntity())
                    .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, source.getDirectEntity());

            Player player = getLastHurtByPlayer();
            if (bl && player != null) builder = builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player).withLuck(player.getLuck());

            table.getRandomItems(builder.create(LootContextParamSets.ENTITY), getLootTableSeed(), stack -> spawnAtLocation(level, stack));
        });
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason type, @Nullable SpawnGroupData group) {
        RubblemiteVariant.selectVariantToSpawn(random, registryAccess(), SpawnContext.create(level, blockPosition())).ifPresent(this::setVariant);
        return super.finalizeSpawn(level, difficulty, type, group);
    }

    public static boolean canSpawn(EntityType<Rubblemite> type, ServerLevelAccessor level, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        return level.getDifficulty() != Difficulty.PEACEFUL
                && (EntitySpawnReason.isSpawner(reason) || level.getBlockState(pos.below()).is(EnderscapeBlockTags.RUBBLEMITE_SPAWNABLE_ON))
                && (EntitySpawnReason.ignoresLightRequirements(reason) || isDarkEnoughToSpawn(level, pos, random));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_STATE, State.IDLING);
        builder.define(DATA_VARIANT_ID, VariantUtils.getDefaultOrAny(registryAccess(), EnderscapeRubblemiteVariants.DEFAULT));
    }

    @Override
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

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);

        VariantUtils.writeVariant(output, getVariant());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);

        VariantUtils.readVariant(input, EnderscapeRegistries.RUBBLEMITE_VARIANT).ifPresent(this::setVariant);
    }

    public boolean isInsideShell() {
        return getState() == State.INSIDE_SHELL || brain.hasMemoryValue(EnderscapeMemory.RUBBLEMITE_HIDING_DURATION);
    }

    public boolean isDashing() {
        return getState() == State.DASHING && !brain.hasMemoryValue(EnderscapeMemory.RUBBLEMITE_DASH_ON_COOLDOWN);
    }

    public boolean isPreparingToDash() {
        return getState() == State.PREPARING_DASH || brain.hasMemoryValue(EnderscapeMemory.RUBBLEMITE_PREPARING_DASH);
    }

    public void enterShell(int value) {
        setState(State.INSIDE_SHELL);
        if (value > 0) brain.setMemory(EnderscapeMemory.RUBBLEMITE_HIDING_DURATION, value);
    }

    public void exitShell() {
        setState(State.IDLING);
        brain.eraseMemory(EnderscapeMemory.RUBBLEMITE_HIDING_DURATION);
        brain.setMemoryWithExpiry(EnderscapeMemory.RUBBLEMITE_HIDING_ON_COOLDOWN, true, 100);
        brain.setMemoryWithExpiry(EnderscapeMemory.RUBBLEMITE_DASH_ON_COOLDOWN, true, 20);
    }

    public boolean canHideInShell() {
        return getState() == State.IDLING && !RubblemiteAI.isShellCoolingDown(this);
    }

    public boolean shouldStopDashing() {
        return onGround() || isInWaterOrRain() || getVehicle() != null;
    }

    public void prepareDash() {
        setState(State.PREPARING_DASH);
        getBrain().setMemory(EnderscapeMemory.RUBBLEMITE_PREPARING_DASH, true);
        getBrain().setMemory(EnderscapeMemory.RUBBLEMITE_PREPARING_DASH_TIME, 5);

        playSound(EnderscapeEntitySounds.RUBBLEMITE_PREPARE_DASH, 1, 1);
    }

    public void dash() {
        setState(State.DASHING);
        getBrain().eraseMemory(EnderscapeMemory.RUBBLEMITE_PREPARING_DASH);
        getBrain().setMemoryWithExpiry(EnderscapeMemory.RUBBLEMITE_DASH_ON_COOLDOWN, true, 40);

        Vec3 vec = getLookAngle();
        vec = vec.multiply(1.4, 0, 1.4).add(0, 0.33F, 0);
        setDeltaMovement(vec);

        playSound(EnderscapeEntitySounds.RUBBLEMITE_HOP, 1, 1);

        if (level() instanceof ServerLevel server) {
            Vec3 pos = position();
            server.sendParticles(ParticleTypes.POOF, pos.x, pos.y + 0.5, pos.z, 5, 0, 0, 0, 0.1);
        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (isAlive() && source.is(EnderscapeDamageTypeTags.RUBBLEMITES_CAN_BLOCK)) {
            if (amount >= 12 && super.hurtServer(level, source, amount)) {
                return true;
            } else {
                if (getState() != State.IDLING && super.hurtServer(level, source, 0)) {
                    return true;
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
        if (getState() != State.IDLING) {
            if (getNavigation().getPath() != null) getNavigation().stop();
            vec = Vec3.ZERO;
        }
        super.travel(vec);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return getState() != State.IDLING ? null : EnderscapeEntitySounds.RUBBLEMITE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return getState() != State.IDLING ? EnderscapeEntitySounds.RUBBLEMITE_SHIELD : EnderscapeEntitySounds.RUBBLEMITE_HURT;
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