package net.enderscape.entity.drifter;

import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class DrifterWanderGoal extends Goal {
    private final PathAwareEntity mob;

    public DrifterWanderGoal(PathAwareEntity mob) {
        this.setControls(EnumSet.of(Control.MOVE));
        this.mob = mob;
    }

    public boolean canStart() {
        return mob.getNavigation().isIdle() && mob.getRandom().nextInt(10) == 0;
    }

    public boolean shouldContinue() {
        return mob.getNavigation().isFollowingPath();
    }

    public void start() {
        Vec3d vec3d = this.getRandomLocation();
        if (vec3d != null) {
            mob.getNavigation().startMovingAlong(mob.getNavigation().findPathTo(new BlockPos(vec3d), 1), 1.0D);
        }

    }

    @Nullable
    private Vec3d getRandomLocation() {
        Vec3d vec3d3 = mob.getRotationVec(0);
        Vec3d vec3d4 = AboveGroundTargeting.find(mob, 8, 7, vec3d3.x, vec3d3.z, 1.5707964F, 3, 1);
        return vec3d4 != null ? vec3d4 : NoPenaltySolidTargeting.find(mob, 8, 4, -2, vec3d3.x, vec3d3.z, 1.5707963705062866D);
    }
}