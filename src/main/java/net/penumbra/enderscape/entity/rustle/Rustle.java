package net.penumbra.enderscape.entity.rustle;

import com.google.common.collect.ImmutableList;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.entity.ai.EnderscapeMemory;
import net.penumbra.enderscape.entity.ai.EnderscapePathTypes;
import net.penumbra.enderscape.item.crafting.RustleRecipe;
import net.penumbra.enderscape.manager.RustleItemConversionManager;
import net.penumbra.enderscape.registry.entity.EnderscapeEntities;
import net.penumbra.enderscape.registry.entity.EnderscapeEntityDataSerializers;
import net.penumbra.enderscape.registry.entity.EnderscapeEntityLootTables;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import net.penumbra.enderscape.registry.item.EnderscapeItems;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;
import net.penumbra.enderscape.registry.sound.EnderscapeEntitySounds;
import net.penumbra.enderscape.registry.sound.EnderscapeItemSounds;
import net.penumbra.enderscape.registry.tag.EnderscapeBlockTags;
import net.penumbra.enderscape.registry.tag.EnderscapeItemTags;
import net.penumbra.enderscape.registry.tag.EnderscapePoiTags;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static net.minecraft.world.entity.ai.memory.MemoryModuleType.WALK_TARGET;
import static net.penumbra.enderscape.entity.ai.EnderscapeMemory.RUSTLE_IS_CONVERTING;

public class Rustle extends Animal implements Bucketable, Shearable, InventoryCarrier {

    private static final EntityDimensions BABY_DIMENSIONS = EnderscapeEntities.RUSTLE.getDimensions().scale(0.65F, 0.8F);

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
    }

    private static final Brain.Provider<Rustle> BRAIN_PROVIDER = Brain.provider(
            RustleAI.MEMORY_TYPES,
            RustleAI.SENSOR_TYPES,
            RustleAI::getActivities
    );

    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Rustle.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Rustle.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(Rustle.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SHEARED = SynchedEntityData.defineId(Rustle.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<RustleConversionPhase> CONVERSION_PHASE = SynchedEntityData.defineId(Rustle.class, EnderscapeEntityDataSerializers.RUSTLE_CONVERSION_PHASE);
    private static final EntityDataAccessor<Optional<RustleRecipe>> QUEUED_RECIPE = SynchedEntityData.defineId(Rustle.class, EnderscapeEntityDataSerializers.OPTIONAL_RUSTLE_RECIPE);

    private static final long BUMP_COOLDOWN_TIME = 20;

    public final AnimationState sleepingAnimationState = new AnimationState();
    public final AnimationState conversionBeginAnimationState = new AnimationState();
    public final AnimationState conversionAnimationState = new AnimationState();
    public final AnimationState conversionEndAnimationState = new AnimationState();

    private final ImmutableList<AnimationState> CONVERSION_ANIMATIONS = ImmutableList.of(
            conversionBeginAnimationState,
            conversionAnimationState,
            conversionEndAnimationState
    );

    private final SimpleContainer inventory = new SimpleContainer(1);
    private long lastBumpTimestamp = 0;

    public Rustle(EntityType<? extends Rustle> type, Level world) {
        super(type, world);

        setPathfindingMalus(PathType.WATER, -1);
        setPathfindingMalus(EnderscapePathTypes.VOID_FIRE, 4.0F);
        setPathfindingMalus(EnderscapePathTypes.VOID_SHALE, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.13)
                .add(Attributes.SAFE_FALL_DISTANCE, 3);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);

        builder.define(CONVERSION_PHASE, RustleConversionPhase.INACTIVE);
        builder.define(QUEUED_RECIPE, Optional.empty());

        builder.define(DATA_FLAGS_ID, (byte) 0);
        builder.define(FROM_BUCKET, false);
        builder.define(SLEEPING, false);
        builder.define(SHEARED, false);
    }

    @Override
    public SimpleContainer getInventory() {
        return inventory;
    }

    @Override
    protected boolean canDispenserEquipIntoSlot(final EquipmentSlot slot) {
        return false;
    }

    @Override
    public boolean wantsToPickUp(final ServerLevel level, final ItemStack stack) {
        return false;
    }

    public ItemStack getHeldItem() {
        return getInventory().getItem(0);
    }

    public boolean hasInventoryItem() {
        return !getHeldItem().isEmpty();
    }

    public Optional<RustleRecipe> getRecipeFor(ItemStack stack) {
        return RustleItemConversionManager.getRecipeFor(stack);
    }

    private boolean canConvert(ItemStack stack) {
        return isCurrently(RustleConversionPhase.INACTIVE) && getQueuedRecipe().isEmpty() && getRecipeFor(stack).isPresent() && !hasInventoryItem() && inventory.canAddItem(stack);
    }

    @Override
    protected void dropEquipment(final ServerLevel level) {
        super.dropEquipment(level);

        inventory.removeAllItems().forEach(stack -> spawnAtLocation(level, stack));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);

        output.putBoolean("FromBucket", fromBucket());
        output.putBoolean("Sleeping", isSleeping());
        output.putBoolean("Sheared", isSheared());

        writeInventoryToTag(output);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);

        setFromBucket(input.getBooleanOr("FromBucket", false));
        setSleeping(input.getBooleanOr("Sleeping", false));
        setSheared(input.getBooleanOr("Sheared", false));

        readInventoryFromTag(input);
        restartConversion();
    }

    private void restartConversion() {
        if (!getHeldItem().isEmpty()) {
            Optional<RustleRecipe> recipe = hasInventoryItem() ? getRecipeFor(getHeldItem()) : Optional.empty();

            if (recipe.isPresent()) {
                setQueuedRecipe(recipe);
                setConversionPhase(RustleConversionPhase.BEGINNING);
                getBrain().setMemory(RUSTLE_IS_CONVERTING, true);
            } else {
                getBrain().eraseMemory(RUSTLE_IS_CONVERTING);
                spitOutHeldItem();
            }
        }
    }

    @Override
    public void saveToBucketTag(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);

        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, (tag) -> {
            tag.putInt("Age", getAge());

            if (isSheared()) tag.putBoolean("Sheared", isSheared());
            if (getBrain().hasMemoryValue(EnderscapeMemory.RUSTLE_HAIR_REGROWTH_COOLDOWN)) tag.putInt("HairRegrowthCooldownTicks", getBrain().getMemory(EnderscapeMemory.RUSTLE_HAIR_REGROWTH_COOLDOWN).get());
        });
    }

    @Override
    public void loadFromBucketTag(CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);

        tag.getInt("Age").ifPresent(this::setAge);
        tag.getBoolean("Sheared").ifPresent(this::setSheared);
        tag.getInt("HairRegrowthCooldownTicks").ifPresent(ticks -> getBrain().setMemory(EnderscapeMemory.RUSTLE_HAIR_REGROWTH_COOLDOWN, ticks));
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || fromBucket();
    }

    @Override
    protected Brain<Rustle> makeBrain(final Brain.Packed packed) {
        return BRAIN_PROVIDER.makeBrain(this, packed);
    }

    @Override
    public Brain<Rustle> getBrain() {
        return (Brain<Rustle>) super.getBrain();
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide() && isAlive()) {
            sleepingAnimationState.animateWhen(isSleeping(), tickCount);

            if (sleepingAnimationState.isStarted() && level().getGameTime() % (60 + getRandom().nextInt(20)) == 0) {
                Vec3 pos = position().add(getLookAngle().scale(0.5));
                level().addParticle(EnderscapeParticles.RUSTLE_SLEEPING_BUBBLE, pos.x, getY() + getBbHeight() - 0.1F, pos.z, 0, 0, 0);
            }
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (CONVERSION_PHASE.equals(accessor)) {
            switch (getConversionPhase()) {
                case BEGINNING:
                    soloConversionAnimation(conversionBeginAnimationState);
                    break;
                case CONVERTING:
                    soloConversionAnimation(conversionAnimationState);
                    break;
                case ENDING:
                    soloConversionAnimation(conversionEndAnimationState);
                    break;
            }
        }

        super.onSyncedDataUpdated(accessor);
    }

    private void soloConversionAnimation(AnimationState value) {
        for (AnimationState other : CONVERSION_ANIMATIONS) {
            if (other.equals(value)) {
                value.start(tickCount);
            } else {
                other.stop();
            }
        }
    }

    public RustleConversionPhase getConversionPhase() {
        return getEntityData().get(CONVERSION_PHASE);
    }

    public Rustle setConversionPhase(RustleConversionPhase value) {
        getEntityData().set(CONVERSION_PHASE, value);
        return this;
    }

    public Optional<RustleRecipe> getQueuedRecipe() {
        return getEntityData().get(QUEUED_RECIPE);
    }

    public Rustle setQueuedRecipe(Optional<RustleRecipe> value) {
        getEntityData().set(QUEUED_RECIPE, value);
        return this;
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        ProfilerFiller profiler = Profiler.get();

        profiler.push("rustleBrain");
        getBrain().tick(level, this);
        profiler.pop();

        profiler.push("rustleActivityUpdate");
        RustleAI.updateActivity(this);
        profiler.pop();

        if (isSleeping()) {
            getBrain().getMemory(EnderscapeMemory.RUSTLE_SLEEPING_SPOT).ifPresentOrElse(pos -> {
                if (!level.getPoiManager().exists(pos, type -> type.is(EnderscapePoiTags.RUSTLE_SLEEPING_SPOT))) {
                    getBrain().eraseMemory(EnderscapeMemory.RUSTLE_SLEEPING_SPOT);
                    wakeUp();
                }

                if (isInWaterOrRain() || getDeltaMovement().lengthSqr() > 0.1 || !level.getPoiManager().exists(blockPosition(), type -> type.is(EnderscapePoiTags.RUSTLE_SLEEPING_SPOT))) wakeUp();
            }, this::wakeUp);
        }

        setSleeping(getBrain().hasMemoryValue(EnderscapeMemory.RUSTLE_SLEEP_TICKS));

        super.customServerAiStep(level);
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return level.getBlockState(pos.below()).is(EnderscapeBlockTags.RUSTLE_PREFER_WALK_ON) ? 10 : 0;
    }

    public static boolean canSpawn(EntityType<?> type, LevelAccessor level, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(EnderscapeBlockTags.RUSTLES_SPAWNABLE_ON);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason reason, @Nullable SpawnGroupData data) {
        if (reason == EntitySpawnReason.BUCKET) return data;
        return super.finalizeSpawn(level, difficulty, reason, data);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance instance) {
        return !instance.is(MobEffects.POISON) && !instance.is(EnderscapeMobEffects.VOID_CORRUPTION) && super.canBeAffected(instance);
    }

    @Override
    protected void doPush(Entity entity) {
        super.doPush(entity);

        if (getVehicle() == null && !getPassengers().contains(entity) && !entity.is(getType()) && !entity.isCrouching()) {
            long gameTime = level().getGameTime();

            if (gameTime - lastBumpTimestamp > BUMP_COOLDOWN_TIME) {
                lastBumpTimestamp = gameTime;
                playSound(EnderscapeEntitySounds.RUSTLE_BUMP, 1, getVoicePitch());
                getNavigation().stop();
                if (isSleeping()) wakeUp();
            }
        }
    }

    @Override
    public boolean fromBucket() {
        return getEntityData().get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean value) {
        getEntityData().set(FROM_BUCKET, value);
    }

    @Override
    public boolean isSleeping() {
        return getEntityData().get(SLEEPING);
    }

    public void setSleeping(boolean value) {
        getEntityData().set(SLEEPING, value);
    }

    public void wakeUp() {
        getBrain().eraseMemory(EnderscapeMemory.RUSTLE_SLEEPING_SPOT);
        getBrain().eraseMemory(EnderscapeMemory.RUSTLE_SLEEP_TICKS);
        getBrain().setMemoryWithExpiry(EnderscapeMemory.RUSTLE_SLEEPING_ON_COOLDOWN, true,100);
        setSleeping(false);
    }

    public boolean isSheared() {
        return getEntityData().get(SHEARED);
    }

    public void setSheared(boolean value) {
        getEntityData().set(SHEARED, value);
    }

    @Override
    public void shear(ServerLevel level, SoundSource source, ItemStack stack) {
        playSound(EnderscapeEntitySounds.RUSTLE_SHEAR, 1, 1);
        dropFromLootTable(
                level,
                EnderscapeEntityLootTables.SHEARING_RUSTLE,
                builder -> builder.withParameter(LootContextParams.ORIGIN, position())
                        .withParameter(LootContextParams.THIS_ENTITY, this)
                        .withParameter(LootContextParams.TOOL, stack)
                        .create(LootContextParamSets.SHEARING),
                (level2, item) -> {
                    for (int i = 0; i < item.getCount(); i++) {
                        ItemEntity entity = spawnAtLocation(level2, item.copyWithCount(1), 0.2F);
                        if (entity != null) {
                            entity.setDeltaMovement(
                                    entity.getDeltaMovement()
                                            .add(
                                                    Mth.nextFloat(random, -0.1F, 0.1F),
                                                    Mth.nextFloat(random, 0, 0.05F),
                                                    Mth.nextFloat(random, -0.1F, 0.1F)
                                            )
                            );
                        }
                    }
                }
        );
        setSheared(true);
        RustleAI.refreshNaturalHairGrowthCooldown(this);
    }

    @Override
    public boolean readyForShearing() {
        return isAlive() && !isBaby() && !isSheared();
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        boolean result = super.hurtServer(level, source, amount);
        if (result && isSleeping()) wakeUp();
        return result;
    }

    public static void regrowHair(ServerLevel level, Rustle entity, boolean displayParticles) {
        if (displayParticles) {
            Vec3 pos = entity.position();
            level.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.x, pos.y + 0.5, pos.z, 5, 0.5F, 0.5F, 0.5F, 0);
        }

        entity.setSheared(false);
        RustleAI.refreshNaturalHairGrowthCooldown(entity);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (isAlive()) {
            ItemStack stack = player.getItemInHand(hand);

            if (!isBaby() && !isSleeping() && canConvert(stack)) {
                if (level() instanceof ServerLevel server) {
                    ItemStack copy = stack.copyWithCount(1);

                    getInventory().setItem(0, copy);
                    stack.shrink(1);

                    getBrain().eraseMemory(WALK_TARGET);
                    getBrain().setMemory(RUSTLE_IS_CONVERTING, true);

                    setQueuedRecipe(getRecipeFor(copy));
                    setConversionPhase(RustleConversionPhase.BEGINNING);

                    playEatingSound();
                    playSound(EnderscapeEntitySounds.RUSTLE_SWALLOW, 1.0F, 1.0F);

                    Vec3 position = getEyePosition();
                    server.sendParticles(EnderscapeParticles.RUSTLE_CONVERTING, position.x, position.y + 0.4, position.z, 4, 0.2, 0.2, 0.2, 0.2);

                    return InteractionResult.SUCCESS_SERVER;
                }

                return InteractionResult.CONSUME;
            }

            if (isFood(stack) && isSleeping()) return InteractionResult.PASS;

            if (stack.getItem() == Items.BUCKET) {
                playSound(getPickupSound(), 1, 1);

                ItemStack bucket = getBucketItemStack();
                saveToBucketTag(bucket);
                player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, bucket, false));

                if (player instanceof ServerPlayer server) CriteriaTriggers.FILLED_BUCKET.trigger(server, bucket);
                if (isLeashed()) dropLeash();
                if (level() instanceof ServerLevel) spitOutHeldItem();

                discard();

                return InteractionResult.SUCCESS;
            }

            if (stack.getItem() instanceof ShearsItem && readyForShearing()) {
                if (level() instanceof ServerLevel server) {
                    shear(server, SoundSource.PLAYERS, stack);
                    stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
                    gameEvent(GameEvent.SHEAR, player);

                    return InteractionResult.SUCCESS_SERVER;
                }
                return InteractionResult.CONSUME;
            }
        }

        return super.mobInteract(player, hand);
    }

    public boolean isCurrently(RustleConversionPhase value) {
        return getConversionPhase().equals(value);
    }

    public void spitOutHeldItem() {
        getInventory().removeAllItems().forEach(stack -> BehaviorUtils.throwItem(
                this,
                stack,
                getEyePosition().add(0, 0.4, 0),
                new Vec3(0.2F, 0.2F, 0.2F),
                0.0F)
        );
    }

    @Override
    public SoundEvent getPickupSound() {
        return EnderscapeItemSounds.RUSTLE_BUCKET_FILL;
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(EnderscapeItems.RUSTLE_BUCKET);
    }

    @Override
    public void travel(Vec3 vec) {
        if (isSleeping()) {
            if (getNavigation().getPath() != null) getNavigation().stop();
            vec = Vec3.ZERO;
        }
        super.travel(vec);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return isSleeping() ? EnderscapeEntitySounds.RUSTLE_SNORE : EnderscapeEntitySounds.RUSTLE_AMBIENT;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return EnderscapeEntitySounds.RUSTLE_HURT;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return EnderscapeEntitySounds.RUSTLE_DEATH;
    }

    protected SoundEvent getStepSound() {
        return EnderscapeEntitySounds.RUSTLE_STEP;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(getStepSound(), 0.15F, Mth.nextFloat(random, 0.9F, 1.1F));
    }

    @Override
    protected void usePlayerItem(Player player, InteractionHand hand, ItemStack stack) {
        super.usePlayerItem(player, hand, stack);
        if (isFood(stack)) playEatingSound();
    }

    @Override
    protected void playEatingSound() {
        level().playSound(null, this, EnderscapeEntitySounds.RUSTLE_EAT, getSoundSource(), getSoundVolume(), getVoicePitch());
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(EnderscapeItemTags.RUSTLE_FOOD);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return EnderscapeEntities.RUSTLE.create(level, EntitySpawnReason.BREEDING);
    }
}