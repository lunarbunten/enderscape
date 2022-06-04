package net.bunten.enderscape.entity.rubblemite;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.StatusEffects;

import java.util.EnumSet;

public class DashAtTargetGoal extends Goal {
    private final RubblemiteEntity mob;

    public DashAtTargetGoal(RubblemiteEntity mob) {
        this.mob = mob;
        setControls(EnumSet.of(Goal.Control.MOVE));
    }

    public boolean canStart() {
        if (mob.hasPassengers()) {
            return false;
        } else {
            LivingEntity target = mob.getTarget();
            if (target == null) {
                return false;
            } else {
                double d = mob.squaredDistanceTo(target);
                if (d >= 4 && d <= 16) {
                    if (!mob.isOnGround()) {
                        return false;
                    } else {
                        return mob.getRandom().nextInt(10) == 0 && !(mob.hasStatusEffect(StatusEffects.SLOWNESS));
                    }
                } else {
                    return false;
                }
            }
        }
    }

    public boolean shouldContinue() {
        return !mob.isOnGround();
    }

    public void start() {
        mob.dash();
    }
}