package net.bunten.enderscape.entity.rubblemite;

import java.util.EnumSet;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class DashAtTargetGoal extends Goal {
    private final Rubblemite mob;

    public DashAtTargetGoal(Rubblemite mob) {
        this.mob = mob;
        setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        if (mob.isVehicle()) {
            return false;
        } else {
            LivingEntity target = mob.getTarget();
            if (target == null) {
                return false;
            } else {
                double d = mob.distanceToSqr(target);
                if (d >= 4 && d <= 16) {
                    if (!mob.isOnGround()) {
                        return false;
                    } else {
                        return mob.getRandom().nextInt(10) == 0 && !(mob.hasEffect(MobEffects.MOVEMENT_SLOWDOWN));
                    }
                } else {
                    return false;
                }
            }
        }
    }

    public boolean canContinueToUse() {
        return !mob.isOnGround();
    }

    public void start() {
        mob.dash();
    }
}