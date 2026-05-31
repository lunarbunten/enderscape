package net.bunten.enderscape.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import net.bunten.enderscape.entity.ai.EnderscapeMemory;
import net.bunten.enderscape.entity.drifter.Drifter;
import net.bunten.enderscape.registry.tag.EnderscapeEntityTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;

import java.util.Set;

public class NearestIntimidatorSensor extends Sensor<Drifter> {

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(
            EnderscapeMemory.NEAREST_VISIBLE_LIVING_ENTITIES
        );
    }

    @Override
    protected void doTick(ServerLevel level, Drifter mob) {
        Brain<Drifter> brain = mob.getBrain();
        NearestVisibleLivingEntities nearby = brain.getMemory(EnderscapeMemory.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
        for (LivingEntity next : nearby.findAll((next) -> next.is(EnderscapeEntityTags.DRIFTERS_INTIMIDATED_BY))) {
            brain.setMemory(EnderscapeMemory.NEAREST_INTIMIDATOR, next);
            break;
        }
    }
}