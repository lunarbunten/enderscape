package net.bunten.enderscape.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(targets = "net.minecraft.world.entity.monster.Shulker$ShulkerAttackGoal")
public abstract class ShulkerAttackGoalMixin extends Goal {
    @Mutable
    @Unique @Final Shulker enderscape$capturedShulker;

    @Inject(
            method = "<init>(Lnet/minecraft/world/entity/monster/Shulker;)V",
            at = @At("RETURN")
    )
    private void revamped_phantoms_init(Shulker captured, CallbackInfo ci) {
        enderscape$capturedShulker = captured;
    }

    @Shadow private int attackTime;

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    private void tick(CallbackInfo info) {
        if (EnderscapeConfig.getInstance().shulkerBulletEnforceCountLimit > 0 && attackTime % 20 == 0) {
            List<Entity> entities = enderscape$capturedShulker.level().getEntities(enderscape$capturedShulker, enderscape$capturedShulker.getBoundingBox().inflate(50), (entity) -> entity instanceof ShulkerBullet bullet && bullet.getOwner() == enderscape$capturedShulker);
            if (entities.size() >= EnderscapeConfig.getInstance().shulkerBulletEnforceCountLimit) info.cancel();
        }
    }
}