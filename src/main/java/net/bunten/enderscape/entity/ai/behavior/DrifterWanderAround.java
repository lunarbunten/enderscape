package net.bunten.enderscape.entity.ai.behavior;

import net.bunten.enderscape.entity.ai.EnderscapeMemory;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

public class DrifterWanderAround {

    private static final int HORIZONTAL_WANDER_RANGE = 12;
    private static final int VERTICAL_WANDER_RANGE = 4;

    private static final int VERTICAL_PATHFIND_BIAS = 0;

    public static BehaviorControl<PathfinderMob> create() {
        return create(DrifterWanderAround::getRandomPathfindTarget);
    }

    @Nullable
    private static Vec3 getRandomPathfindTarget(PathfinderMob mob) {
        Vec3 vec3 = mob.getViewVector(0.0F);

        for (int i = 0; i < 10; i++) {
            Vec3 pos = AirAndWaterRandomPos.getPos(mob, DrifterWanderAround.HORIZONTAL_WANDER_RANGE, DrifterWanderAround.VERTICAL_WANDER_RANGE, VERTICAL_PATHFIND_BIAS, vec3.x, vec3.z, (float) (Math.PI / 2));
            if (pos != null && pos.y >= 0) return pos;
        }

        return null;
    }

    public static OneShot<PathfinderMob> create(Function<PathfinderMob, Vec3> randomPath) {
        return BehaviorBuilder.create(
                instance -> instance.group(instance.absent(MemoryModuleType.WALK_TARGET)).apply(instance, memory -> (level, mob, l) -> {
                    if (mob.getBrain().hasMemoryValue(MemoryModuleType.TEMPTING_PLAYER)) return false;

                    Optional<Vec3> randomPos = Optional.ofNullable(randomPath.apply(mob));
                    Optional<GlobalPos> homePos = mob.getBrain().getMemory(EnderscapeMemory.HOME);

                    boolean isHome = homePos.isPresent() && mob.blockPosition().distSqr(homePos.get().pos()) >= 2500;
                    if (isHome) randomPos = Optional.of(homePos.get().pos().getCenter());

                    memory.setOrErase(randomPos.map(pos -> new WalkTarget(pos, isHome ? 1.5F : 1.0F, 0)));
                    return true;
                })
        );
    }
}