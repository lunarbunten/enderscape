package net.bunten.enderscape.mixin;

import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class NearestAttackableTargetGoalMixin<T extends LivingEntity> extends TargetGoal {

    @Shadow @Final protected Class<T> targetType;

    public NearestAttackableTargetGoalMixin(Mob mob, boolean bl) {
        super(mob, bl);
    }

    @Inject(method = "getTargetConditions", at = @At("RETURN"), cancellable = true)
    public void Enderscape$getTargetConditions(CallbackInfoReturnable<TargetingConditions> info) {
        if (mob instanceof EnderMan && targetType == Endermite.class) {
            info.setReturnValue(info.getReturnValue().selector(NearestAttackableTargetGoalMixin::isEndermiteUnsafe));
        }
    }

    private static boolean isEndermiteUnsafe(LivingEntity entity, ServerLevel level) {
        return BlockPos.findClosestMatch(entity.blockPosition(), 6, 6, pos -> level.getBlockState(pos).is(EnderscapeBlockTags.ENDERMITE_SAFE_WHEN_NEARBY)).isEmpty();
    }
}