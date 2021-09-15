package net.enderscape.entity.drifter;

import net.enderscape.util.EndMath;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;

public class DrifterMoveControl extends MoveControl {
    private final MobEntity mob;

    public DrifterMoveControl(MobEntity mob) {
        super(mob);
        this.mob = mob;
    }

    public void tick() {
        if (isMoving()) {
            state = MoveControl.State.WAIT;
            mob.setNoGravity(true);
            float x = (float) (targetX - mob.getX());
            float y = (float) (targetY - mob.getY());
            float z = (float) (targetZ - mob.getZ());

            float yaw = (float) ((EndMath.atan2(z, x) * 57.2957763671875D) - 90);
            mob.setYaw(wrapDegrees(mob.getYaw(), yaw, 20));
            float speed = (float) (super.speed * mob.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));

            mob.setMovementSpeed(speed);
            float k = EndMath.sqrt(x * x + z * z);
            float pitch = (float) -(EndMath.atan2(y, k) * 57.2957763671875D);
            mob.setPitch(EndMath.lerp(0.05F, mob.getPitch(), pitch));
            mob.setUpwardSpeed(y > 0 ? speed : -speed);
        }
    }
}