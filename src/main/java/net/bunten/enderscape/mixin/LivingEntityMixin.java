package net.bunten.enderscape.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.entity.DashJumpUser;
import net.bunten.enderscape.entity.EndTrialSpawnable;
import net.bunten.enderscape.entity.magnia.MagniaMoveable;
import net.bunten.enderscape.entity.magnia.MagniaProperties;
import net.bunten.enderscape.item.MagniaAttractorItem;
import net.bunten.enderscape.item.NebuliteToolContext;
import net.bunten.enderscape.item.NebuliteToolItem;
import net.bunten.enderscape.particle.DashJumpShockwaveParticleOptions;
import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeMobEffects;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.registry.tag.EnderscapeEntityTags;
import net.bunten.enderscape.registry.tag.EnderscapeItemTags;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.bunten.enderscape.registry.EnderscapeEnchantments.hasRebound;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements MagniaMoveable, DashJumpUser, EndTrialSpawnable {

    @Unique
    private Vec3 Enderscape$deltaMovementAtStartOfTravel;

    @Shadow public abstract boolean isFallFlying();

    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot equipmentSlot);

    @Shadow public abstract float getYHeadRot();

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Unique
    private final LivingEntity mob = (LivingEntity) (Object) this;

    @Unique
    private int Enderscape$elytraGroundTicks = 0;

    @Unique
    @Override
    public MagniaProperties createMagniaProperties() {
        return new MagniaProperties(
                entity -> true,
                entity -> entity.getType().is(EnderscapeEntityTags.AFFECTED_BY_MAGNIA) ? 0.05F : 0.01F * MagniaMoveable.getMagnetismFactor(entity),
                entity -> entity.getType().is(EnderscapeEntityTags.AFFECTED_BY_MAGNIA) ? 0.05F : 0.02F * MagniaMoveable.getMagnetismFactor(entity),
                DEFAULT_MAGNIA_PREDICATE,
                entity -> {
                    if (entity instanceof LivingEntity living && !(entity instanceof Player)) {
                        AttributeInstance gravity = living.getAttribute(Attributes.GRAVITY);
                        if (!gravity.hasModifier(MAGNIA_GRAVITY_MODIFIER.id())) gravity.addTransientModifier(MAGNIA_GRAVITY_MODIFIER);
                    }
                    entity.fallDistance = 0;
                    if (random.nextInt(16) == 0 && level() instanceof ServerLevel server) {
                        server.sendParticles(ParticleTypes.END_ROD, position().x, position().y + 0.5, position().z, 1, 0.3F, 0.3, 0.3F, 0);
                    }
                },
                entity -> {
                    if (entity instanceof LivingEntity living && !(entity instanceof Player)) {
                        AttributeInstance gravity = living.getAttribute(Attributes.GRAVITY);
                        if (gravity.hasModifier(MAGNIA_GRAVITY_MODIFIER.id())) gravity.removeModifier(MAGNIA_GRAVITY_MODIFIER);
                    }
                    entity.fallDistance = 0;
                }
        );
    }

    @Unique
    private static final EntityDataAccessor<Integer> MAGNIA_COOLDOWN_DATA = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);

    @Unique
    private static final EntityDataAccessor<Boolean> DASHED_DATA = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);

    @Unique
    private static final EntityDataAccessor<Integer> DASH_TICKS_DATA = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);

    @Unique
    @Override
    public EntityDataAccessor<Integer> Enderscape$magniaCooldownData() {
        return MAGNIA_COOLDOWN_DATA;
    }

    @Unique
    @Override
    public EntityDataAccessor<Boolean> Enderscape$dashed() {
        return DASHED_DATA;
    }

    @Unique
    @Override
    public EntityDataAccessor<Integer> Enderscape$dashTicks() {
        return DASH_TICKS_DATA;
    }

    @Unique
    private static final EntityDataAccessor<Boolean> SPAWNED_FROM_END_TRIAL_SPAWNER_DATA = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);

    @Unique
    @Override
    public EntityDataAccessor<Boolean> Enderscape$spawnedFromEndTrialSpawner() {
        return SPAWNED_FROM_END_TRIAL_SPAWNER_DATA;
    }

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    public void Enderscape$addAdditionalSaveData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        defineMagniaData(builder);
        defineDashJumpData(builder);
        defineEndTrialSpawnableData(builder);
    }

    @Inject(at = @At("TAIL"), method = "canStandOnFluid", cancellable = true)
    public void Enderscape$canStandOnFluid(FluidState state, CallbackInfoReturnable<Boolean> info) {
        if (isFallFlying() && hasRebound(level(), getItemBySlot(EquipmentSlot.CHEST)) && getDeltaMovement().y() > -0.9) info.setReturnValue(true);
    }

    @Redirect(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;broadcastEntityEvent(Lnet/minecraft/world/entity/Entity;B)V"))
    private void Enderscape$redirectShieldDamageSound(Level level, Entity entity, byte b) {
        if (entity instanceof LivingEntity living && living.getUseItem().is(EnderscapeItemTags.RUBBLE_SHIELDS)) {
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), EnderscapeItemSounds.RUBBLE_SHIELD_BLOCK, entity.getSoundSource(), 2, 1);
        } else {
            level.broadcastEntityEvent(entity, b);
        }
    }

    @Inject(method = "updateFallFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ElytraItem;isFlyEnabled(Lnet/minecraft/world/item/ItemStack;)Z", shift = At.Shift.BEFORE))
    private void Enderscape$updateFallFlying(CallbackInfo info) {
        if (onGround()) {
            if (Enderscape$elytraGroundTicks % 3 == 0 && Enderscape$elytraGroundTicks < 10 && mob.isFallFlying()) {
                ItemStack stack = mob.getItemBySlot(EquipmentSlot.CHEST);
                if (stack.isDamageableItem() && stack.getDamageValue() < stack.getMaxDamage()) stack.hurtAndBreak(1, mob, mob.getEquipmentSlotForItem(stack));
            }
            Enderscape$elytraGroundTicks++;
        }
    }

    @Inject(at = @At("HEAD"), method = "take")
    private void Enderscape$take(Entity entity, int i, CallbackInfo ci) {
        if (isAlive() && !isSpectator() && MagniaMoveable.wasMovedByMagnia(entity) && mob instanceof Player player) {
            ItemStack stack = MagniaAttractorItem.getValidAttractor(player.getInventory());
            if (!stack.isEmpty()) {
                NebuliteToolContext context = new NebuliteToolContext(stack, level(), player);
                if (stack.getItem() instanceof MagniaAttractorItem && NebuliteToolItem.fuelExceedsCost(context)) {
                    MagniaAttractorItem.incrementEntitiesPulled(stack, 1);
                    MagniaAttractorItem.tryUseFuel(context, 1 - MagniaAttractorItem.getEntitiesPulledToUseFuel(stack));
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void Enderscape$headTick(CallbackInfo info) {
        if (DashJumpUser.dashed(mob) && (onGround() || getVehicle() != null || isInLiquid() || isSpectator())) DashJumpUser.setDashed(mob, false);
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void Enderscape$tailTick(CallbackInfo info) {
        MagniaMoveable.tickMagniaCooldown(mob);

        if (Enderscape$hasDriftPhysics() && !Enderscape$shouldCancelDriftPhysics()) {
            Vec3 vel = mob.getDeltaMovement();
            double maxSpeed = 2, frictionMod = Math.min(1, 0.96 + Math.max(0, (Math.hypot(vel.x, vel.z) - maxSpeed) / maxSpeed) * 0.5);
            mob.setDeltaMovement(vel.x / frictionMod, vel.y, vel.z / frictionMod);
        }

        if (!isFallFlying() && Enderscape$elytraGroundTicks > 0) Enderscape$elytraGroundTicks--;

        if (DashJumpUser.dashed(mob)) {
            Vec3 vel = mob.position().subtract(new Vec3(xOld, yOld, zOld)).scale(-1.0F);

            if (mob.isShiftKeyDown()) {
                mob.setDeltaMovement(mob.getDeltaMovement().multiply(0.92, 1, 0.92));
            }

            int ticks = DashJumpUser.dashTicks(mob);
            double x = random.nextGaussian() * 0.02 + vel.x, y = random.nextGaussian() * 0.02 + vel.y, z = random.nextGaussian() * 0.02 + vel.z;

            if (ticks > 40 && ticks % 5 == 0 && ticks != 60) {
                level().addParticle(new DashJumpShockwaveParticleOptions(vel.toVector3f(), ((float) ticks / 60)), getX(), getY() + (getBbHeight() / 2), getZ(), vel.x, vel.y, vel.z);
            }

            level().addParticle(EnderscapeParticles.DASH_JUMP_SPARKS, getRandomX(1.0) - (vel.x / 2), getRandomY() - (vel.y / 2), getRandomZ(1.0) - (vel.z / 2), x, y, z);

            DashJumpUser.setDashTicks(mob, ticks - 1);
            if (ticks <= 0 || (ticks < 50 && vel.lengthSqr() < 0.6)) DashJumpUser.setDashed(mob, false);
        }
    }

    @Redirect(
            method = "travel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;onGround()Z",
                    ordinal = 1
            )
    )
    private boolean Enderscape$travel(LivingEntity instance) {
        return hasRebound(level(), instance.getItemBySlot(EquipmentSlot.CHEST)) ? Enderscape$elytraGroundTicks >= 10 && isFallFlying() : instance.onGround();
    }

    @Redirect(
            method = "updateFallFlying",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;onGround()Z"
            )
    )
    private boolean Enderscape$updateFallFlying(LivingEntity instance) {
        return hasRebound(level(), instance.getItemBySlot(EquipmentSlot.CHEST)) ? Enderscape$elytraGroundTicks >= 10 && isFallFlying() : instance.onGround();
    }

    @Inject(method = "travel", at = @At(value = "HEAD"))
    private void Enderscape$travelHead(Vec3 vec3, CallbackInfo ci) {
        Enderscape$deltaMovementAtStartOfTravel = getDeltaMovement();
    }

    @Inject(
            method = "travel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
                    shift = At.Shift.AFTER,
                    ordinal = 2
            )
    )
    private void Enderscape$travelShiftAfter(Vec3 vec3, CallbackInfo ci) {
        if (onGround() && mob.getDeltaMovement().lengthSqr() > 0.4 && !hasRebound(level(), getItemBySlot(EquipmentSlot.CHEST))) Enderscape$playLandingEffects(Enderscape$deltaMovementAtStartOfTravel.horizontalDistance(), getDeltaMovement().horizontalDistance());
    }

    @Inject(at = @At("HEAD"), method = "makePoofParticles", cancellable = true)
    public void Enderscape$makePoofParticles(CallbackInfo info) {
        if (EnderscapeConfig.getInstance().voidPoofParticlesUponDeath && mob.getType().is(EnderscapeEntityTags.CREATES_VOID_PARTICLES_UPON_DEATH)) {
            info.cancel();

            for (int i = 0; i < 20; i++) {
                double d = random.nextGaussian() * 0.02;
                double e = random.nextGaussian() * 0.02;
                double f = random.nextGaussian() * 0.02;

                level().addParticle(EnderscapeParticles.VOID_POOF, getRandomX(1.0) - d * 10.0, getRandomY() - e * 10.0, getRandomZ(1.0) - f * 10.0, d, e, f);
            }
        }
    }

    @Unique
    private void Enderscape$playLandingEffects(double start, double last) {
        mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(), EnderscapeItemSounds.ELYTRA_LAND, SoundSource.PLAYERS, 1, 1);

        if (level() instanceof ServerLevel server) {
            double difference = start - last;
            float severity = (float)(difference * 10.0 - 3.0);

            Vec3 pos = position();
            int count = (int) (Mth.clamp(mob.getDeltaMovement().lengthSqr() * 40, 5, 100) + (fallDistance + severity) * 5);

            server.sendParticles(ParticleTypes.POOF, pos.x, pos.y + 0.2, pos.z, count, 0, 0, 0, 0.3);
            server.sendParticles(ParticleTypes.EXPLOSION, pos.x, pos.y + 0.2, pos.z, 1, 0, 0, 0, 0.3);
        }
    }

    @Unique
    protected boolean Enderscape$hasDriftPhysics() {
        return mob.getItemBySlot(EquipmentSlot.LEGS).is(EnderscapeItems.DRIFT_LEGGINGS) || mob.hasEffect(EnderscapeMobEffects.LOW_GRAVITY);
    }

    @Inject(
            method = "updateFallFlying",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void Enderscape$travelShiftAfter(CallbackInfo ci) {
        ItemStack stack = this.getItemBySlot(EquipmentSlot.CHEST);
        if (stack.getDamageValue() == stack.getMaxDamage() - 1 && stack.getItem() == Items.ELYTRA) {
            level().playSound(null, getX(), getY(), getZ(), EnderscapeItemSounds.ELYTRA_BREAK, getSoundSource(), 1.0F, 1.0F);
        }
    }

    @Unique
    protected boolean Enderscape$shouldCancelDriftPhysics() {
        if (mob.isSpectator()) return true;
        if (mob.isShiftKeyDown()) return true;
        if (mob.onGround()) return true;
        if (mob.isFallFlying()) return true;
        if (mob.isPassenger()) return true;
        if (mob.isInWaterOrBubble()) return true;
        if (mob.hasEffect(MobEffects.LEVITATION)) return true;
        if (mob instanceof Player player && player.getAbilities().flying) return true;
        return false;
    }
}