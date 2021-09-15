package net.enderscape.entity.drifter;

import net.enderscape.util.EndMath;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.BirdPathNodeMaker;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNodeNavigator;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DrifterNavigation extends EntityNavigation {
    public DrifterNavigation(MobEntity mobEntity, World world) {
        super(mobEntity, world);
    }

    protected PathNodeNavigator createPathNodeNavigator(int range) {
        nodeMaker = new BirdPathNodeMaker();
        nodeMaker.setCanEnterOpenDoors(true);
        return new PathNodeNavigator(nodeMaker, range);
    }

    protected boolean isAtValidPosition() {
        return canSwim() && isInLiquid() || !entity.hasVehicle();
    }

    protected Vec3d getPos() {
        return entity.getPos();
    }

    public Path findPathTo(Entity entity, int distance) {
        return findPathTo(entity.getBlockPos(), distance);
    }

    public void tick() {
        ++tickCount;
        if (shouldRecalculate) {
            recalculatePath();
        }

        if (!isIdle()) {
            Vec3d vec3d2;
            if (isAtValidPosition()) {
                continueFollowingPath();
            } else if (currentPath != null && !currentPath.isFinished()) {
                vec3d2 = currentPath.getNodePosition(entity);
                if (EndMath.floor(entity.getX()) == EndMath.floor(vec3d2.x)
                        && EndMath.floor(entity.getY()) == EndMath.floor(vec3d2.y)
                        && EndMath.floor(entity.getZ()) == EndMath.floor(vec3d2.z)) {
                    currentPath.next();
                }
            }

            DebugInfoSender.sendPathfindingData(world, entity, currentPath, nodeReachProximity);
            if (!isIdle()) {
                assert currentPath != null;
                vec3d2 = currentPath.getNodePosition(entity);
                entity.getMoveControl().moveTo(vec3d2.x, vec3d2.y, vec3d2.z, speed);
            }
        }
    }

    protected boolean canPathDirectlyThrough(Vec3d origin, Vec3d target, int sizeX, int sizeY, int sizeZ) {
        int i = EndMath.floor(origin.x);
        int j = EndMath.floor(origin.y);
        int k = EndMath.floor(origin.z);
        double d = target.x - origin.x;
        double e = target.y - origin.y;
        double f = target.z - origin.z;
        double g = d * d + e * e + f * f;
        if (g < 1.0E-8D) {
            return false;
        } else {
            double h = 1 / Math.sqrt(g);
            d *= h;
            e *= h;
            f *= h;
            double l = 1 / Math.abs(d);
            double m = 1 / Math.abs(e);
            double n = 1 / Math.abs(f);
            double o = (double) i - origin.x;
            double p = (double) j - origin.y;
            double q = (double) k - origin.z;
            if (d >= 0) {
                ++o;
            }

            if (e >= 0) {
                ++p;
            }

            if (f >= 0) {
                ++q;
            }

            o /= d;
            p /= e;
            q /= f;
            int r = d < 0 ? -1 : 1;
            int s = e < 0 ? -1 : 1;
            int t = f < 0 ? -1 : 1;
            int u = EndMath.floor(target.x);
            int v = EndMath.floor(target.y);
            int w = EndMath.floor(target.z);
            int x = u - i;
            int y = v - j;
            int z = w - k;

            while (x * r > 0 || y * s > 0 || z * t > 0) {
                if (o < q && o <= p) {
                    o += l;
                    i += r;
                    x = u - i;
                } else if (p < o && p <= q) {
                    p += m;
                    j += s;
                    y = v - j;
                } else {
                    q += n;
                    k += t;
                    z = w - k;
                }
            }

            return true;
        }
    }

    public boolean isValidPosition(BlockPos pos) {
        return world.getBlockState(pos).hasSolidTopSurface(world, pos, entity);
    }
}