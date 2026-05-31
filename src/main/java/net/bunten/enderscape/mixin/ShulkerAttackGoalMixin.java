package net.bunten.enderscape.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(targets = "net.minecraft.world.entity.monster.Shulker$ShulkerAttackGoal")
public abstract class ShulkerAttackGoalMixin extends Goal {

    @Shadow private int attackTime;

    @Shadow
    @Final
    private Shulker this$0;

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    private void tick(CallbackInfo info) {
        if (EnderscapeConfig.getInstance().shulkerBulletEnforceCountLimit > 0 && attackTime % 20 == 0) {
            List<Entity> entities = this$0.level().getEntities(this$0, this$0.getBoundingBox().inflate(50), (entity) -> entity instanceof ShulkerBullet bullet && bullet.getOwner() == this$0);
            if (entities.size() >= EnderscapeConfig.getInstance().shulkerBulletEnforceCountLimit) info.cancel();
        }
    }
}