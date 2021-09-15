package net.enderscape.entity.motu;

import net.enderscape.registry.EndSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MotuAttackGoal extends Goal {
    private final MotuEntity mob;
    public int cooldown;

    public MotuAttackGoal(MotuEntity mob) {
        this.mob = mob;
    }

    public boolean canStart() {
        return mob.getTarget() != null;
    }

    public void start() {
        cooldown = 0;
    }

    public void stop() {
    }

    public void tick() {
        LivingEntity target = mob.getTarget();
        assert target != null && target.isAlive();
        if (target.squaredDistanceTo(mob) < 4096 && mob.canSee(target)) {
            World world = mob.world;
            cooldown++;
            if (cooldown == 20) {
                Vec3d vec = mob.getRotationVec(1);

                double x = target.getX() - mob.getX();
                double y = target.getBodyY(0.5) - (0.5 + mob.getBodyY(0.5));
                double z = target.getZ() - mob.getZ();

                MotuLaserEntity fireball = new MotuLaserEntity(world, mob, x, y, z);
                fireball.updatePosition(mob.getX() + (vec.x * 0.5), mob.getEyeY(), fireball.getZ() + (vec.z * 0.5));
                world.spawnEntity(fireball);

                if (!mob.isSilent()) {
                    mob.playSound(EndSounds.ENTITY_MOTU_SHOOT, 1, 1);
                }

                cooldown = -20;
            }
        } else if (cooldown > 0) {
            cooldown--;
        }
    }
}