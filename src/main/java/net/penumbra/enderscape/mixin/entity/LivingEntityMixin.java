package net.penumbra.enderscape.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.entity.magnia.MagniaAffected;
import net.penumbra.enderscape.item.ItemStackContext;
import net.penumbra.enderscape.item.component.EntityMagnet;
import net.penumbra.enderscape.item.component.FueledTool;
import net.penumbra.enderscape.item.component.StunAttack;
import net.penumbra.enderscape.manager.*;
import net.penumbra.enderscape.registry.entity.EnderscapeAttachments;
import net.penumbra.enderscape.registry.entity.EnderscapeAttributes;
import net.penumbra.enderscape.registry.entity.EnderscapeDamageTypes;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;
import net.penumbra.enderscape.registry.sound.EnderscapeEntitySounds;
import net.penumbra.enderscape.registry.tag.EnderscapeDamageTypeTags;
import net.penumbra.enderscape.registry.tag.EnderscapeEntityTags;
import net.penumbra.enderscape.registry.tag.EnderscapeFluidTags;
import net.penumbra.enderscape.registry.tag.EnderscapeMobEffectTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Shadow
    public abstract float getYHeadRot();

    @Unique
    private final LivingEntity entity = (LivingEntity) (Object) this;

    /*
        Tick Injections
     */

    @Inject(at = @At("HEAD"), method = "tick")
    private void Enderscape$headTick(CallbackInfo info) {
        DashJumpManager.tickHead(entity);
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void Enderscape$tailTick(CallbackInfo info) {
        DashJumpManager.tickTail(entity);
        ElytraManager.tickEntityAtTail(entity);
        LowGravityManager.tickTail(entity);
        MiscEntityManager.tickTail(entity);
    }

    /*
        Misc.
     */

    @ModifyReturnValue(method = "createLivingAttributes", at = @At(value = "RETURN"))
    private static AttributeSupplier.Builder Enderscape$createLivingAttributes(AttributeSupplier.Builder builder) {
        return builder.add(EnderscapeAttributes.BACKSTAB_DAMAGE).add(EnderscapeAttributes.BOUNCE_STRENGTH).add(EnderscapeAttributes.STEALTH);
    }

    @ModifyReturnValue(method = "getVisibilityPercent", at = @At(value = "RETURN"))
    public double Enderscape$getVisibilityPercent(double original) {
        return original * EnderscapeAttributes.getStealthMultiplier(entity);
    }

    /*
        Magnia
     */

    @Inject(at = @At("HEAD"), method = "take")
    private void Enderscape$take(Entity taken, int i, CallbackInfo ci) {
        if (isAlive() && !isSpectator() && MagniaAffected.wasMoved(taken) && entity instanceof Player player) {
            ItemStack stack = EntityMagnet.getFirstUsableMagnet(player.getInventory());
            if (!stack.isEmpty() && EntityMagnet.is(stack)) {
                FueledTool.useFuelOrDamage(new ItemStackContext(stack, level(), entity), 1, player.getEquipmentSlotForItem(stack));
            }
        }
    }

    /*
        Elytra
     */

    @ModifyReturnValue(at = @At("RETURN"), method = "getLiquidCollisionShape")
    public VoxelShape Enderscape$fixLiquidCollisionShape(VoxelShape original) {
        if (entity.isFallFlying() && ElytraManager.hasGlidingJumpStrength(entity)) {
            return Block.column(16.0, 0.0, 8.0);
        }

        return original;
    }

    @WrapOperation(method = "canGlide", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;onGround()Z"))
    private boolean Enderscape$changeOnGroundCheck(LivingEntity instance, Operation<Boolean> original) {
        return ElytraManager.shouldDisallowFallFlying(entity, original.call(instance));
    }

    @ModifyReturnValue(at = @At("RETURN"), method = "canStandOnFluid")
    public boolean Enderscape$allowReboundingOnFluid(boolean original) {
        return original || ElytraManager.canReboundFromFluid(entity);
    }

    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getAttributeValue(Lnet/minecraft/core/Holder;)D"), method = "getJumpPower(F)F")
    public double Enderscape$changeJumpStrength(double original) {
        return entity.isFallFlying() && ElytraManager.hasGlidingJumpStrength(entity) ? EnderscapeAttributes.getBounceHeight(entity) : original;
    }

    @Inject(method = "updateFallFlying", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;canGlide()Z", shift = At.Shift.BEFORE))
    private void Enderscape$updateFallFlying(CallbackInfo info) {
        ElytraManager.tickEntityOnGround(entity);
    }

    @Inject(at = @At("HEAD"), method = "handleFallFlyingCollisions")
    private void Enderscape$handleFallFlyingCollisions(double start, double last, CallbackInfo info) {
        ElytraManager.tryPlayLandingEffects(entity, start, last);
    }

    @Inject(at = @At("TAIL"), method = "stopFallFlying")
    public void Enderscape$stopFallFlying(CallbackInfo info) {
        ElytraManager.onStopFallFlying(entity);
    }

    /*
        Void-related changes
     */

    @Inject(at = @At("TAIL"), method = "playSecondaryHurtSound")
    private void Enderscape$playVoidHurt(DamageSource source, CallbackInfo info) {
        if (source.is(EnderscapeDamageTypeTags.VOIDS_HEALTH) && !(entity instanceof Player)) {
            level().playSound(null, position().x, position().y, position().z, EnderscapeEntitySounds.VOID_HURT, getSoundSource());
        }
    }

    @ModifyReturnValue(at = @At("RETURN"), method = "getLiquidCollisionShape")
    public VoxelShape Enderscape$fixLiquidCollisionShapeForVoid(VoxelShape original) {
        if (entity.is(EnderscapeEntityTags.VOID_LACHRYMA_WALKABLE_MOBS)) {
            return Block.column(16.0, 0.0, 16.0 * (8.0 / 9.0));
        }

        return original;
    }

    @Inject(at = @At("HEAD"), method = "canStandOnFluid", cancellable = true)
    public void Enderscape$voidBeingsStandOnVoid(FluidState state, CallbackInfoReturnable<Boolean> info) {
        if (state.is(EnderscapeFluidTags.VOID_LACHRYMA) && entity.is(EnderscapeEntityTags.VOID_LACHRYMA_WALKABLE_MOBS))
            info.setReturnValue(true);
    }

    @ModifyReturnValue(method = "getSecondsToDisableBlocking", at = @At("RETURN"))
    public float Enderscape$getSecondsToDisableBlocking(float original) {
        ItemStack weapon = StunAttack.getWeaponInfo(entity).stack();

        if (entity.hasAttached(EnderscapeAttachments.PERFORMING_STUN_ATTACK) && StunAttack.is(weapon)) {
            return StunAttack.get(weapon).shieldDisableDuration();
        } else {
            return original;
        }
    }

    @Inject(method = "actuallyHurt", at = @At("HEAD"))
    public void Enderscape$applyStun(ServerLevel level, DamageSource source, float amount, CallbackInfo info) {
        if (amount > 0.0F && source.is(EnderscapeDamageTypes.STUN_ATTACK)) {
            StunAttack.applyStun(level, entity, source);
        }
    }

    @Inject(at = @At("HEAD"), method = "makePoofParticles", cancellable = true)
    public void Enderscape$makePoofParticles(CallbackInfo info) {
        if (EnderscapeConfig.getInstance().voidPoofParticlesUponDeath && entity.is(EnderscapeEntityTags.CREATES_VOID_PARTICLES_UPON_DEATH)) {
            info.cancel();

            for (int i = 0; i < 20; i++) {
                double d = random.nextGaussian() * 0.02;
                double e = random.nextGaussian() * 0.02;
                double f = random.nextGaussian() * 0.02;

                level().addParticle(EnderscapeParticles.VOID_POOF, getRandomX(1.0) - d * 10.0, getRandomY() - e * 10.0, getRandomZ(1.0) - f * 10.0, d, e, f);
            }
        }
    }

    @WrapOperation(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setHealth(F)V"))
    public void Enderscape$convertVoidDamage(LivingEntity instance, float health, Operation<Void> original, @Local(argsOnly = true) DamageSource source) {
        original.call(instance, VoidManager.convertVoidDamage(instance, source, health));
    }

    @ModifyArg(method = "setHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F"), index = 2)
    public float Enderscape$setHealthLimit(float original) {
        return Math.max(1.0F, original - VoidManager.getVoidedHealth(entity));
    }

    @WrapOperation(method = "onBelowWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)V"))
    public void Enderscape$onBelowWorld(LivingEntity entity, DamageSource source, float amount, Operation<Void> original) {
        VoidManager.tickInOuterVoid(entity, original);
    }

    @Inject(
            method = "canBeAffected",
            at = @At(value = "RETURN"),
            cancellable = true
    )
    private void Enderscape$cancelAddingUnsupportedEffects(MobEffectInstance instance, CallbackInfoReturnable<Boolean> info) {
        if (VoidManager.hasVoidedHealth(entity) && instance.getEffect().is(EnderscapeMobEffectTags.UNSUPPORTED_WITH_VOIDED_HEALTH)) {
            info.setReturnValue(false);
        }
    }

    @Inject(
            method = "checkTotemDeathProtection",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;setHealth(F)V",
                    shift = At.Shift.BEFORE
            )
    )
    private void Enderscape$healVoidHeartsWithDeathProtection(DamageSource source, CallbackInfoReturnable<Boolean> info) {
        VoidManager.modifyVoidedHealth(entity, value -> Math.min(value, 10.0F));
        VoidManager.setVoidImmunityTicks(entity, 10);
    }

    @Inject(at = @At("RETURN"), method = "isInvulnerableTo", cancellable = true)
    public void Enderscape$setInvulnerableToVoid(ServerLevel level, DamageSource source, CallbackInfoReturnable<Boolean> info) {
        if (is(EnderscapeEntityTags.VOID_IMMUNE) && source.is(EnderscapeDamageTypeTags.IS_VOID) && !source.is(EnderscapeDamageTypes.OUTER_VOID))
            info.setReturnValue(true);
    }

    /*
        Stun-related changes
     */

    @Inject(at = @At("HEAD"), method = "canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z", cancellable = true)
    public void Enderscape$canAttack(LivingEntity target, CallbackInfoReturnable<Boolean> info) {
        if (EnderscapeMobEffects.isStunned(entity)) info.setReturnValue(false);
    }

    @ModifyReturnValue(method = "isImmobile", at = @At("RETURN"))
    public boolean Enderscape$isImmobile(boolean original) {
        return original || EnderscapeMobEffects.isStunned(entity);
    }

    @ModifyExpressionValue(method = "updatingUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isSameItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private boolean Enderscape$cancelItemUsing(boolean original) {
        return original && !EnderscapeMobEffects.isStunned(entity);
    }

    @Unique
    private Vec3 Enderscape$tryCancelMovementVec(Vec3 vec3) {
        if (EnderscapeMobEffects.isStunned(entity)) return Vec3.ZERO;
        return vec3;
    }

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;travelInFluid(Lnet/minecraft/world/phys/Vec3;)V"))
    private Vec3 Enderscape$modifyFluidVec(Vec3 vec3) {
        return Enderscape$tryCancelMovementVec(vec3);
    }

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;travelFallFlying(Lnet/minecraft/world/phys/Vec3;)V"))
    private Vec3 Enderscape$modifyFallFlyingVec(Vec3 vec3) {
        return Enderscape$tryCancelMovementVec(vec3);
    }

    @ModifyArg(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;travelInAir(Lnet/minecraft/world/phys/Vec3;)V"))
    private Vec3 Enderscape$modifyAirVec(Vec3 vec3) {
        return Enderscape$tryCancelMovementVec(vec3);
    }

    @Inject(at = @At("TAIL"), method = "travel")
    public void Enderscape$travel(Vec3 vec3, CallbackInfo ci) {
        if (EnderscapeMobEffects.isStunned(entity) && entity instanceof Mob mob) {
            if (mob.getNavigation().getPath() != null) mob.getNavigation().stop();
        }
    }
}