package net.bunten.enderscape.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import net.bunten.enderscape.entity.drifter.AbstractDrifter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;

import java.util.Optional;
import java.util.Set;

public class AdultDrifterSensor extends Sensor<AbstractDrifter> {

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
    }

    protected void doTick(ServerLevel level, AbstractDrifter mob) {
        mob.getBrain()
                .getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
                .ifPresent(entities -> {
                    Optional<AbstractDrifter> optional = entities.findClosest(adult -> adult instanceof AbstractDrifter drifter && !drifter.isBaby()).map(AbstractDrifter.class::cast);
                    mob.getBrain().setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT, optional);
                });
    }
}