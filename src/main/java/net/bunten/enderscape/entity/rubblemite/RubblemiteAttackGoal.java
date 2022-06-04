package net.bunten.enderscape.entity.rubblemite;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.Hand;

import java.util.EnumSet;

public class RubblemiteAttackGoal extends Goal {
    protected final RubblemiteEntity mob;
    private final double speed;
    private final boolean pauseWhenMobIdle;
    private Path path;
    private double x;
    private double y;
    private double z;
    private int updateCountdownTicks;
    private int attackTime;
    private long lastUpdateTime;

    public RubblemiteAttackGoal(RubblemiteEntity mob, double speed, boolean pauseWhenMobIdle) {
        this.mob = mob;
        this.speed = speed;
        this.pauseWhenMobIdle = pauseWhenMobIdle;
        setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    public boolean canStart() {
        long l = mob.world.getTime();
        if (l - lastUpdateTime < 20L) {
            return false;
        } else {
            lastUpdateTime = l;
            LivingEntity target = mob.getTarget();
            if (target == null) {
                return false;
            } else if (!target.isAlive()) {
                return false;
            } else {
                path = mob.getNavigation().findPathTo(target, 0);
                if (path != null) {
                    return true;
                } else {
                    return getSquaredMaxAttackDistance(target) >= mob.squaredDistanceTo(target.getX(), target.getY(), target.getZ());
                }
            }
        }
    }

    public boolean shouldContinue() {
        LivingEntity target = mob.getTarget();
        if (target == null) {
            return false;
        } else if (!target.isAlive()) {
            return false;
        } else if (!pauseWhenMobIdle) {
            return !mob.getNavigation().isIdle();
        } else if (!mob.isInWalkTargetRange(target.getBlockPos())) {
            return false;
        } else {
            return !(target instanceof PlayerEntity) || !target.isSpectator() && !((PlayerEntity) target).isCreative();
        }
    }

    public void start() {
        mob.getNavigation().startMovingAlong(path, speed);
        mob.setAttacking(true);
        updateCountdownTicks = 0;
        attackTime = 0;
    }

    public void stop() {
        LivingEntity target = mob.getTarget();
        if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(target)) {
            mob.setTarget(null);
        }

        mob.setAttacking(false);
        mob.getNavigation().stop();
    }

    public void tick() {
        LivingEntity target = mob.getTarget();
        mob.getLookControl().lookAt(target, 30, 30);
        assert target != null;
        double d = mob.squaredDistanceTo(target.getX(), target.getY(), target.getZ());
        updateCountdownTicks = Math.max(updateCountdownTicks - 1, 0);
        if ((pauseWhenMobIdle || mob.getVisibilityCache().canSee(target)) && updateCountdownTicks <= 0 && (x == 0 && y == 0 && z == 0 || target.squaredDistanceTo(x, y, z) >= 1 || mob.getRandom().nextFloat() < 0.05F)) {
            x = target.getX();
            y = target.getY();
            z = target.getZ();
            updateCountdownTicks = 4 + mob.getRandom().nextInt(7);
            if (d > 1024) {
                updateCountdownTicks += 10;
            } else if (d > 256) {
                updateCountdownTicks += 5;
            }

            if (!mob.getNavigation().startMovingTo(target, speed)) {
                updateCountdownTicks += 15;
            }
        }

        attackTime = Math.max(attackTime - 1, 0);
        tryAttack(target, d);
    }

    protected void tryAttack(LivingEntity target, double squaredDistance) {
        double d = getSquaredMaxAttackDistance(target);
        if (squaredDistance <= d && attackTime <= 0) {
            attackTime = 20;
            mob.swingHand(Hand.MAIN_HAND);
            mob.tryAttack(target);
        }
    }

    protected double getSquaredMaxAttackDistance(LivingEntity entity) {
        return mob.getWidth() * 2 * mob.getWidth() * 2 + entity.getWidth();
    }
}