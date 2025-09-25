package net.bunten.enderscape.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.function.Predicate;

public class ConditionalLookAtTargetSink<T extends Mob> extends Behavior<T> {
    private final Predicate<T> shouldStopUsing;

    public ConditionalLookAtTargetSink(Predicate<T> shouldStopUsing, int minDuration, int maxDuration) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_PRESENT), minDuration, maxDuration);
        this.shouldStopUsing = shouldStopUsing;
    }

    @Override
    protected boolean canStillUse(ServerLevel level, T mob, long time) {
        return !shouldStopUsing.test(mob) && mob.getBrain().getMemory(MemoryModuleType.LOOK_TARGET).filter(tracker -> tracker.isVisibleBy(mob)).isPresent();
    }

    @Override
    protected void stop(ServerLevel level, T mob, long time) {
        mob.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
    }

    @Override
    protected void tick(ServerLevel level, T mob, long time) {
        mob.getBrain().getMemory(MemoryModuleType.LOOK_TARGET).ifPresent(tracker -> mob.getLookControl().setLookAt(tracker.currentPosition()));
    }
}