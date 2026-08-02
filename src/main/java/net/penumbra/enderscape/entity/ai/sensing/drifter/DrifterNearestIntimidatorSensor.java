package net.penumbra.enderscape.entity.ai.sensing.drifter;

import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.penumbra.enderscape.entity.ai.EnderscapeMemory;
import net.penumbra.enderscape.entity.drifter.Drifter;
import net.penumbra.enderscape.registry.tag.EnderscapeEntityTags;

import java.util.Set;

public class DrifterNearestIntimidatorSensor extends Sensor<Drifter> {

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(
            EnderscapeMemory.NEAREST_VISIBLE_LIVING_ENTITIES
        );
    }

    @Override
    protected void doTick(ServerLevel level, Drifter entity) {
        Brain<Drifter> brain = entity.getBrain();

        if (brain.getMemory(EnderscapeMemory.NEAREST_INTIMIDATOR).filter(LivingEntity::isAlive).isEmpty()) {
            brain.eraseMemory(EnderscapeMemory.NEAREST_INTIMIDATOR);
        }

        if (entity.getVehicle() != null) return;

        NearestVisibleLivingEntities nearby = brain.getMemory(EnderscapeMemory.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());

        for (LivingEntity next : nearby.findAll((next) -> next.is(EnderscapeEntityTags.DRIFTERS_INTIMIDATED_BY))) {
            if (entity.getPassengers().contains(next)) continue;

            brain.setMemory(EnderscapeMemory.NEAREST_INTIMIDATOR, next);
            break;
        }
    }
}