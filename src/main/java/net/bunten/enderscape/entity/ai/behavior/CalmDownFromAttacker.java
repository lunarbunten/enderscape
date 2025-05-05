package net.bunten.enderscape.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.bunten.enderscape.entity.ai.EnderscapeMemory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Optional;

public class CalmDownFromAttacker extends Behavior<LivingEntity> {

    private final int distanceToForgive;

    public CalmDownFromAttacker(int distanceToForgive) {
        super(ImmutableMap.of(EnderscapeMemory.HURT_BY_ENTITY, MemoryStatus.VALUE_PRESENT));
        this.distanceToForgive = distanceToForgive;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, LivingEntity mob) {
        Optional<LivingEntity> attacker = mob.getBrain().getMemory(EnderscapeMemory.HURT_BY_ENTITY);
        return !attacker.map(entity -> entity.distanceToSqr(mob) <= distanceToForgive * distanceToForgive).orElse(false);
    }

    @Override
    protected void start(ServerLevel level, LivingEntity mob, long l) {
        mob.getBrain().eraseMemory(EnderscapeMemory.HURT_BY_ENTITY);
    }
}