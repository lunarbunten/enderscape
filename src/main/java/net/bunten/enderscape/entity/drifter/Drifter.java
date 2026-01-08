package net.bunten.enderscape.entity.drifter;

import com.mojang.serialization.Dynamic;
import net.bunten.enderscape.entity.ai.behavior.DrifterStartOrStopLeakingJelly;
import net.bunten.enderscape.registry.*;
import net.bunten.enderscape.registry.tag.EnderscapeItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import static net.bunten.enderscape.registry.EnderscapeEntitySounds.*;

public class Drifter extends Animal {

    private static final String DRIPPING_JELLY_KEY = "DrippingJelly";
    private static final EntityDataAccessor<Boolean> DRIPPING_JELLY = SynchedEntityData.defineId(Drifter.class, EntityDataSerializers.BOOLEAN);

    public Drifter(EntityType<? extends Drifter> type, Level world) {
        super(type, world);

        moveControl = new FlyingMoveControl(this, 20, true);

        setPathfindingMalus(PathType.WATER, -1);
        setPathfindingMalus(PathType.WATER_BORDER, 16);
        DrifterStartOrStopLeakingJelly.refreshCooldown(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createAnimalAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.1)
                .add(Attributes.FOLLOW_RANGE, 24)
                .add(Attributes.TEMPT_RANGE, 24)
                .add(Attributes.GRAVITY, 0.005)
                .add(Attributes.JUMP_STRENGTH, 0.1)
                .add(Attributes.MAX_HEALTH, 16)
                .add(Attributes.FLYING_SPEED, 0.4);
    }

    public boolean isDrippingJelly() {
        return !isBaby() && entityData.get(DRIPPING_JELLY);
    }

    public void setDrippingJelly(boolean value) {
        entityData.set(DRIPPING_JELLY, value);
    }

    protected Vec3 getEntityBounceVelocity(LivingEntity mob) {
        var vel = mob.getDeltaMovement();
        float boost = mob.isFallFlying() ? 1.5F : 1;

        double height = 1.6;
        if (mob.isFallFlying()) height += 0.4;
        if (mob.isShiftKeyDown()) height /= 2;

        return new Vec3(vel.x * boost, height, vel.z * boost);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DRIPPING_JELLY, false);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean(DRIPPING_JELLY_KEY, isDrippingJelly());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setDrippingJelly(input.getBooleanOr(DRIPPING_JELLY_KEY, false));
    }

    private boolean hasFeatherFalling(LivingEntity mob) {
        try {
            var registry = level().registryAccess().lookup(Registries.ENCHANTMENT).orElse(null);
            if (registry == null) return false;

            var enchantment = registry.getValue(Enchantments.FEATHER_FALLING);
            if (enchantment == null) return false;

            var holder = registry.wrapAsHolder(enchantment);
            return EnchantmentHelper.getItemEnchantmentLevel(holder, mob.getItemBySlot(EquipmentSlot.FEET)) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (onGround()) jumpFromGround();
        if (isInLiquid()) setDeltaMovement(getDeltaMovement().add(0, 0.025, 0));

        if (!isBaby()) {
            if (isAlive() && !level().isClientSide()) {
                level().getEntities(this, getBounceHitbox()).forEach((entity) -> {
                    if (entity instanceof LivingEntity mob && mob.isAlive() && !mob.isSpectator() && !(mob instanceof Drifter)) collide(mob, getBounceHitbox());
                });
            }

            if (level() instanceof ServerLevel server) {
                if (isInLove() && !isDrippingJelly() && random.nextInt(8) == 0) {
                    Vec3 pos = position();
                    server.sendParticles(EnderscapeParticles.DRIFT_JELLY_DRIPPING, pos.x, pos.y + 0.5, pos.z, 1, 0.4F, 1, 0.4F, 0.1);
                }

                if (isDrippingJelly() && random.nextBoolean()) {
                    Vec3 pos = position();
                    server.sendParticles(EnderscapeParticles.DRIFT_JELLY_DRIPPING, pos.x, pos.y + 0.5, pos.z, 1, 0.4F, 1, 0.4F, 0.1);
                }
            }
        }
    }

    public AABB getBounceHitbox() {
        AABB inflated = getBoundingBox().inflate(0.25, 0, 0.25);
        return new AABB(
                inflated.minX,
                inflated.maxY - getBbHeight() * 0.3,
                inflated.minZ,
                inflated.maxX,
                inflated.maxY,
                inflated.maxZ
        );
    }

    private void collide(LivingEntity mob, AABB bounceHitbox) {
        boolean isLeashOwner = isLeashed() && getLeashData().leashHolder == mob;
        if (!mob.onGround() && !isLeashOwner && mob.getDeltaMovement().y < -0.1F && bounceHitbox.intersects(mob.getBoundingBox())) {
            Vec3 velocity = new Vec3(mob.getDeltaMovement().x, mob.isShiftKeyDown() ? -0.4 : 0.6, mob.getDeltaMovement().z);
            setDeltaMovement(velocity);

            mob.setDeltaMovement(getEntityBounceVelocity(mob));
            gameEvent(EnderscapeGameEvents.BOUNCE, mob);

            if (mob instanceof ServerPlayer player) {
                EnderscapeCriteria.BOUNCE_ON_DRIFTER.trigger(player, this);
                player.awardStat(EnderscapeStats.DRIFTER_BOUNCE);
                player.connection.send(new ClientboundSetEntityMotionPacket(mob));
            }

            mob.fallDistance = 0;
            playSound(DRIFTER_BOUNCE, 1, 1);
            hurt(level().damageSources().source(EnderscapeDamageTypes.STOMP, mob), hasFeatherFalling(mob) ? 0 : 1);
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(Items.GLASS_BOTTLE) && isDrippingJelly()) {
            if (!level().isClientSide()) {
                stack.consume(1, player);

                ItemStack jelly = new ItemStack(EnderscapeItems.DRIFT_JELLY_BOTTLE);
                if (!player.getInventory().add(jelly)) player.drop(jelly, false);

                gameEvent(GameEvent.ENTITY_INTERACT, player);
                playSound(DRIFTER_MILK, 0.5F, 1);
                setDrippingJelly(false);

                DrifterStartOrStopLeakingJelly.refreshCooldown(this);

                return InteractionResult.SUCCESS_SERVER;
            }
            return InteractionResult.CONSUME;
        } else {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return isBaby() ? DRIFTLET_AMBIENT : DRIFTER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        if (source.is(EnderscapeDamageTypes.STOMP) && source.getEntity() instanceof LivingEntity mob && hasFeatherFalling(mob)) return DRIFTER_HURT_SILENT;
        return isBaby() ? DRIFTLET_HURT : DRIFTER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return isBaby() ? DRIFTLET_DEATH : DRIFTER_DEATH;
    }

    private SoundEvent getEatingSound() {
        return isBaby() ? DRIFTLET_EAT : DRIFTER_EAT;
    }

    public SoundEvent getJumpSound() {
        return isBaby() ? DRIFTLET_JUMP : DRIFTER_JUMP;
    }

    @Override
    public void spawnChildFromBreeding(ServerLevel world, Animal other) {
        super.spawnChildFromBreeding(world, other);
        if (!world.isClientSide()) {
            ((Drifter) other).setDrippingJelly(true);
            setDrippingJelly(true);
        }
    }

    @Nullable
    public Drifter getBreedOffspring(ServerLevel level, AgeableMob parent) {
        return EnderscapeEntities.DRIFTER.create(level, EntitySpawnReason.BREEDING);
    }

    public static boolean canSpawn(EntityType<?> type, LevelAccessor level, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
        return true;
    }

    @Override
    protected Brain.Provider<Drifter> brainProvider() {
        return Brain.provider(DrifterAI.MEMORY_TYPES, DrifterAI.SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return DrifterAI.makeBrain(brainProvider().makeBrain(dynamic));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Brain<Drifter> getBrain() {
        return (Brain<Drifter>) super.getBrain();
    }

    @Override
    protected void customServerAiStep(ServerLevel serverLevel) {
        ProfilerFiller profiler = Profiler.get();

        profiler.push("drifterBrain");
        getBrain().tick(serverLevel, this);
        profiler.pop();

        profiler.push("drifterActivityUpdate");
        DrifterAI.updateActivity(this);
        profiler.pop();

        super.customServerAiStep(serverLevel);
    }

    @Override
    public void jumpFromGround() {
        super.jumpFromGround();
        if (isAlive()) playSound(getJumpSound(), 1.0F, 1.0F);
    }

    @Override
    protected void usePlayerItem(Player player, InteractionHand hand, ItemStack stack) {
        super.usePlayerItem(player, hand, stack);
        if (isFood(stack)) playEatingSound();
    }

    @Override
    protected void playEatingSound() {
        level().playSound(null, this, getEatingSound(), getSoundSource(), getSoundVolume(), getVoicePitch());
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        navigation.setRequiredPathLength(48.0F);
        return navigation;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader world) {
        return world.getBlockState(pos).isAir() ? 10 : 0;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public float getVoicePitch() {
        return Mth.nextFloat(random, 0.8F, 1.2F);
    }

    @Override
    public boolean causeFallDamage(double f, float g, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void checkFallDamage(double d, boolean bl, BlockState blockState, BlockPos blockPos) {
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    protected void jumpInLiquid(TagKey<Fluid> tagKey) {
        setDeltaMovement(getDeltaMovement().add(0.0, 0.1, 0.0));
    }

    @Override
    public Vec3 getLeashOffset() {
        return isBaby() ? new Vec3(0, getEyeHeight() + 0.26F, getBbWidth() * 0.05F) : new Vec3(0, getEyeHeight() + 0.38F, 0);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(EnderscapeItemTags.DRIFTER_FOOD);
    }
}