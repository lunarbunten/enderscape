package net.penumbra.enderscape.mixin.entity.shulker;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.penumbra.enderscape.config.EnderscapeConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;

@Mixin(targets = "net.minecraft.world.entity.monster.Shulker$ShulkerAttackGoal")
public abstract class ShulkerAttackGoalMixin extends Goal {

    @Shadow
    @Final
    Shulker this$0;

    @Inject(method = "canUse", at = @At("RETURN"), cancellable = true)
    private void Enderscape$enforceBulletLimit(CallbackInfoReturnable<Boolean> info) {
        int limit = EnderscapeConfig.getInstance().shulkerBulletEnforceCountLimit;

        if (limit > 0 && info.getReturnValue()) {
            Predicate<Entity> predicate = (entity) -> entity instanceof ShulkerBullet bullet && bullet.getOwner() == this$0;
            List<Entity> bullets = this$0.level().getEntities(this$0, this$0.getBoundingBox().inflate(50), predicate);
            if (bullets.size() >= limit) info.setReturnValue(false);
        }
    }
}