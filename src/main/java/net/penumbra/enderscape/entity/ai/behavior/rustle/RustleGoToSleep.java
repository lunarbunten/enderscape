package net.penumbra.enderscape.entity.ai.behavior.rustle;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.schedule.Activity;
import net.penumbra.enderscape.entity.ai.EnderscapeMemory;
import net.penumbra.enderscape.entity.rustle.Rustle;
import net.penumbra.enderscape.entity.rustle.RustleAI;

import static net.minecraft.world.entity.ai.memory.MemoryStatus.VALUE_ABSENT;
import static net.minecraft.world.entity.ai.memory.MemoryStatus.VALUE_PRESENT;
import static net.penumbra.enderscape.entity.ai.EnderscapeMemory.RUSTLE_SLEEPING_ON_COOLDOWN;
import static net.penumbra.enderscape.entity.ai.EnderscapeMemory.RUSTLE_SLEEPING_SPOT;

public class RustleGoToSleep extends Behavior<Rustle> {

    private final float speedModifier;

    public RustleGoToSleep(float speedModifier) {
        super(ImmutableMap.of(RUSTLE_SLEEPING_SPOT, VALUE_PRESENT, RUSTLE_SLEEPING_ON_COOLDOWN, VALUE_ABSENT));
        this.speedModifier = speedModifier;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Rustle mob) {
        return mob.getBrain().getActiveNonCoreActivity().map(activity -> activity == Activity.IDLE).orElse(true);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, Rustle mob, long time) {
        return mob.getBrain().hasMemoryValue(RUSTLE_SLEEPING_SPOT);
    }

    @Override
    protected void tick(ServerLevel level, Rustle mob, long time) {
        BlockPos sleepingSpot = mob.getBrain().getMemory(RUSTLE_SLEEPING_SPOT).get();

        if (sleepingSpot.closerThan(mob.blockPosition(), 0.05)) {
            mob.getBrain().setMemory(EnderscapeMemory.RUSTLE_SLEEP_TICKS, RustleAI.SLEEPING_TIME_RANGE_IN_MINUTES.sample(mob.getRandom()) * 60 * 20);
            doStop(level, mob, time);
        } else {
            BehaviorUtils.setWalkAndLookTargetMemories(mob, sleepingSpot, speedModifier, 0);
        }
    }
}