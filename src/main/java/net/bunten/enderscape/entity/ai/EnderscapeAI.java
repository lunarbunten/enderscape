package net.bunten.enderscape.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;

import java.util.Optional;

public class EnderscapeAI {

    public static Optional<? extends LivingEntity> getAttackTarget(LivingEntity mob) {
        return getPossibleTarget(mob).filter(target -> Sensor.isEntityAttackable(mob, target));
    }

    private static Optional<? extends LivingEntity> getPossibleTarget(LivingEntity mob) {
        Optional<LivingEntity> target = BehaviorUtils.getLivingEntityFromUUIDMemory(mob, EnderscapeMemory.ANGRY_AT);
        if (target.isPresent()) return target;
        if (getAttackablePlayer(mob).isPresent()) return getAttackablePlayer(mob);
        return getAttackableFrom(mob, EnderscapeMemory.NEAREST_VISIBLE_ATTACKABLE_ENEMY);
    }

    private static Optional<? extends LivingEntity> getAttackablePlayer(LivingEntity mob) {
        return getAttackableFrom(mob, EnderscapeMemory.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
    }

    private static Optional<? extends LivingEntity> getAttackableFrom(LivingEntity mob, MemoryModuleType<? extends LivingEntity> memoryType) {
        return mob.getBrain().getMemory(memoryType).filter(next -> next.closerThan(mob, 12));
    }
}