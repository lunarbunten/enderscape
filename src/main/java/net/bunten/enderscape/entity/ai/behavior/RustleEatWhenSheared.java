package net.bunten.enderscape.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.bunten.enderscape.entity.ai.EnderscapeMemory;
import net.bunten.enderscape.entity.rustle.Rustle;
import net.bunten.enderscape.registry.EnderscapeEntitySounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;

public class RustleEatWhenSheared extends Behavior<Rustle> {

    private final float speedModifier;

    public RustleEatWhenSheared(float speedModifier) {
        super(ImmutableMap.of(EnderscapeMemory.RUSTLE_FOOD, MemoryStatus.VALUE_PRESENT));
        this.speedModifier = speedModifier;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Rustle mob) {
        return mob.isSheared() && mob.getRandom().nextInt(300) == 0 && mob.getBrain().getActiveNonCoreActivity().map(activity -> activity == Activity.IDLE).orElse(true);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, Rustle mob, long time) {
        return mob.isSheared() && mob.getBrain().hasMemoryValue(EnderscapeMemory.RUSTLE_FOOD);
    }

    @Override
    protected boolean timedOut(long l) {
        return false;
    }

    @Override
    protected void tick(ServerLevel level, Rustle mob, long time) {
        BlockPos food = mob.getBrain().getMemory(EnderscapeMemory.RUSTLE_FOOD).get();

        if (food.closerThan(mob.blockPosition(), 0.5)) {
            level.destroyBlock(food, false, mob);
            mob.playSound(EnderscapeEntitySounds.RUSTLE_EAT);

            Rustle.regrowHairWithParticles(level, mob);
            stop(level, mob, time);
        } else {
            BehaviorUtils.setWalkAndLookTargetMemories(mob, food, speedModifier, 0);
        }
    }
}