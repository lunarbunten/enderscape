package net.bunten.enderscape.entity.rubblemite;

import java.util.EnumSet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;

public class RubblemiteAttackGoal extends Goal {
    protected final Rubblemite mob;
    private final double speed;
    private final boolean pauseWhenMobIdle;
    private Path path;
    private double x;
    private double y;
    private double z;
    private int updateCountdownTicks;
    private int attackTime;
    private long lastUpdateTime;

    public RubblemiteAttackGoal(Rubblemite mob, double speed, boolean pauseWhenMobIdle) {
        this.mob = mob;
        this.speed = speed;
        this.pauseWhenMobIdle = pauseWhenMobIdle;
        setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        long l = mob.level.getGameTime();
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
                path = mob.getNavigation().createPath(target, 0);
                if (path != null) {
                    return true;
                } else {
                    return getSquaredMaxAttackDistance(target) >= mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
                }
            }
        }
    }

    public boolean canContinueToUse() {
        LivingEntity target = mob.getTarget();
        if (target == null) {
            return false;
        } else if (!target.isAlive()) {
            return false;
        } else if (!pauseWhenMobIdle) {
            return !mob.getNavigation().isDone();
        } else if (!mob.isWithinRestriction(target.blockPosition())) {
            return false;
        } else {
            return !(target instanceof Player) || !target.isSpectator() && !((Player) target).isCreative();
        }
    }

    public void start() {
        mob.getNavigation().moveTo(path, speed);
        mob.setAggressive(true);
        updateCountdownTicks = 0;
        attackTime = 0;
    }

    public void stop() {
        LivingEntity target = mob.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
            mob.setTarget(null);
        }

        mob.setAggressive(false);
        mob.getNavigation().stop();
    }

    public void tick() {
        LivingEntity target = mob.getTarget();
        mob.getLookControl().setLookAt(target, 30, 30);
        assert target != null;
        double d = mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
        updateCountdownTicks = Math.max(updateCountdownTicks - 1, 0);
        if ((pauseWhenMobIdle || mob.getSensing().hasLineOfSight(target)) && updateCountdownTicks <= 0 && (x == 0 && y == 0 && z == 0 || target.distanceToSqr(x, y, z) >= 1 || mob.getRandom().nextFloat() < 0.05F)) {
            x = target.getX();
            y = target.getY();
            z = target.getZ();
            updateCountdownTicks = 4 + mob.getRandom().nextInt(7);
            if (d > 1024) {
                updateCountdownTicks += 10;
            } else if (d > 256) {
                updateCountdownTicks += 5;
            }

            if (!mob.getNavigation().moveTo(target, speed)) {
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
            mob.swing(InteractionHand.MAIN_HAND);
            mob.doHurtTarget(target);
        }
    }

    protected double getSquaredMaxAttackDistance(LivingEntity entity) {
        return mob.getBbWidth() * 2 * mob.getBbWidth() * 2 + entity.getBbWidth();
    }
}