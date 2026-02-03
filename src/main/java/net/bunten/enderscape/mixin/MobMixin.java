package net.bunten.enderscape.mixin;

import net.bunten.enderscape.entity.EndTrialSpawnable;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {
    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    public void Enderscape$applyOminous(ServerLevelAccessor level, DifficultyInstance instance, MobSpawnType reason, SpawnGroupData data, CallbackInfoReturnable<SpawnGroupData> info) {
        if (reason.equals(MobSpawnType.TRIAL_SPAWNER) && BlockPos.findClosestMatch(blockPosition(), 16, 16, pos -> level.getBlockState(pos).is(EnderscapeBlocks.END_TRIAL_SPAWNER.get())).isPresent()) {
            EndTrialSpawnable.setSpawnedFromEndTrialSpawner(this, true);
        }
    }

    @Inject(method = "serverAiStep", at = @At("HEAD"), cancellable = true)
    public void Enderscape$serverAiStep(CallbackInfo info) {
        if (EnderscapeMobEffects.isStunned(this)) info.cancel();
    }
}