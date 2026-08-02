package net.penumbra.enderscape.mixin.entity.silverfish;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.penumbra.enderscape.config.EnderscapeConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.entity.monster.Silverfish$SilverfishMergeWithStoneGoal")
public abstract class SilverfishMergeWithStoneGoalMixin extends RandomStrollGoal {

    public SilverfishMergeWithStoneGoalMixin(PathfinderMob pathfinderMob, double d) {
        super(pathfinderMob, d);
    }

    @Inject(method = "canUse", at = @At("RETURN"), cancellable = true)
    private void onCanUse(CallbackInfoReturnable<Boolean> info) {
        if (EnderscapeConfig.getInstance().silverfishDelayBeforeInfestingStone && mob.tickCount < 60) info.setReturnValue(false);
    }
}