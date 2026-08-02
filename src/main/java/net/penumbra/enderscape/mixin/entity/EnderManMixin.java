package net.penumbra.enderscape.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.entity.ai.EnderscapePathTypes;
import net.penumbra.enderscape.entity.enderman.ImprovedEnderman;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;
import net.penumbra.enderscape.registry.server.EnderscapeServerNetworking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.penumbra.enderscape.registry.entity.EnderscapeAttachments.ATTACK_ANIMATION_REMAINING_TICKS;

@Mixin(EnderMan.class)
public abstract class EnderManMixin extends Monster implements ImprovedEnderman {

    protected EnderManMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private final EnderMan enderman = (EnderMan) (Object) this;

    @Unique
    private final AnimationState attackAnimationState = new AnimationState();

    @Unique
    @Override
    public AnimationState attackAnimationState() {
        return attackAnimationState;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void Enderscape$init(CallbackInfo info) {
        setPathfindingMalus(EnderscapePathTypes.VOID_FIRE, 2.0F);
        setPathfindingMalus(EnderscapePathTypes.VOID_LACHRYMA, 1.0F);
        setPathfindingMalus(EnderscapePathTypes.VOID_SHALE, 16.0F);
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    public void Enderscape$aiStep(CallbackInfo info) {
        if (level().isClientSide()) {
            attackAnimationState.animateWhen(enderman.getAttachedOrElse(ATTACK_ANIMATION_REMAINING_TICKS, 0) > 0, tickCount);
        } else {
            if (enderman.hasAttached(ATTACK_ANIMATION_REMAINING_TICKS)) {
                enderman.modifyAttached(ATTACK_ANIMATION_REMAINING_TICKS, value -> value - 1);

                if (enderman.getAttached(ATTACK_ANIMATION_REMAINING_TICKS) <= 0) {
                    enderman.removeAttached(ATTACK_ANIMATION_REMAINING_TICKS);
                }
            }
        }
    }

    @WrapOperation(
            method = "aiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V",
                    ordinal = 0
            )
    )
    public void Enderscape$changeParticleOptions(Level level, ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd, Operation<Void> original) {
        if (EnderscapeConfig.getInstance().entityUpdatePortalParticles) {
            original.call(level, EnderscapeParticles.VOID_ENTITY, x, y, z, xd, yd, zd);
        } else {
            original.call(level, particle, x, y, z, xd, yd, zd);
        }
    }

    @Inject(method = "playStareSound", at = @At("HEAD"), cancellable = true)
    public void Enderscape$playStareSound(CallbackInfo info) {
        if (EnderscapeConfig.getInstance().endermanStereoStareSound) info.cancel();
    }

    @Inject(method = "setTarget", at = @At("TAIL"))
    public void Enderscape$setTarget(LivingEntity target, CallbackInfo info) {
        if (target instanceof ServerPlayer server && enderman.canAttack(server)) {
            EnderscapeServerNetworking.sendTargetedByEndermanPayload(server, getId(), EnderscapeConfig.getInstance().endermanStereoStareSound);
        }
    }

    @Inject(method = "teleport(DDD)Z", at = @At(value = "HEAD"), cancellable = true)
    public void Enderscape$cancelTeleport(double x, double y, double z, CallbackInfoReturnable<Boolean> info) {
        if (EnderscapeMobEffects.isStunned(enderman)) info.setReturnValue(false);
    }
}