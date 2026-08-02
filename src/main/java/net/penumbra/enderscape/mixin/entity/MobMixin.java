package net.penumbra.enderscape.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.penumbra.enderscape.manager.VoidManager;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import net.penumbra.enderscape.registry.entity.EnderscapeAttachments;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.penumbra.enderscape.registry.entity.EnderscapeAttachments.HAS_END_TRIAL_SPAWNER_EFFECTS;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {

    @Unique
    private final Mob entity = (Mob) (Object) this;

    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @WrapOperation(method = "isSunBurnTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;isInWaterOrRain()Z"))
    public boolean Enderscape$doNotBurnInVoidLachryma(Mob instance, Operation<Boolean> original) {
        return original.call(instance) || VoidManager.inVoidLachryma(instance);
    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    public void Enderscape$applyOminous(ServerLevelAccessor level, DifficultyInstance instance, EntitySpawnReason reason, SpawnGroupData data, CallbackInfoReturnable<SpawnGroupData> info) {
        if (reason.equals(EntitySpawnReason.TRIAL_SPAWNER) && BlockPos.findClosestMatch(blockPosition(), 16, 16, pos -> level.getBlockState(pos).is(EnderscapeBlocks.END_TRIAL_SPAWNER)).isPresent()) {
            setAttached(HAS_END_TRIAL_SPAWNER_EFFECTS, true);
        }
    }

    @Inject(method = "doHurtTarget", at = @At(value = "RETURN"))
    public void Enderscape$triggerEndermanAttack(ServerLevel level, Entity target, CallbackInfoReturnable<Boolean> info) {
        if (entity instanceof EnderMan) {
            entity.setAttached(EnderscapeAttachments.ATTACK_ANIMATION_REMAINING_TICKS, 15);
        }
    }

    @Inject(method = "serverAiStep", at = @At("HEAD"), cancellable = true)
    public void Enderscape$serverAiStep(CallbackInfo info) {
        if (EnderscapeMobEffects.isStunned(this)) info.cancel();
    }
}