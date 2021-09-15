package net.enderscape.entity.motu;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.mob.MobEntity;

public class MotuMoveControl extends MoveControl {
    public MotuMoveControl(MobEntity entity) {
        super(entity);
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = entity.getTarget();
        if (target != null && target.isAlive() && entity.canSee(target)) {
            boolean bl = target.distanceTo(entity) <= 5;

            if (entity.isOnGround()) {
                entity.lookAtEntity(target, 30, 30);

                forwardMovement = bl ? -1 : 1;
                speed = 0.65;
                state = State.STRAFE;
            }

            if (entity.getNavigation().getCurrentPath() != null && bl) {
                entity.getNavigation().stop();
            }
        }
    }
}