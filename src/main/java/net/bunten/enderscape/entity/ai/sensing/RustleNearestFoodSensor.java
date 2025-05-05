package net.bunten.enderscape.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import net.bunten.enderscape.entity.ai.EnderscapeMemory;
import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;

import java.util.Set;

public class RustleNearestFoodSensor extends Sensor<Mob> {

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(EnderscapeMemory.RUSTLE_FOOD);
    }

    @Override
    protected void doTick(ServerLevel level, Mob mob) {
        mob.getBrain().setMemory(EnderscapeMemory.RUSTLE_FOOD, BlockPos.findClosestMatch(mob.blockPosition(), 8, 4, pos -> level.getBlockState(pos).is(EnderscapeBlockTags.RUSTLE_FOOD)));
    }
}