package net.penumbra.enderscape.entity.ai.sensing.rustle;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.AcquirePoi;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.pathfinder.Path;
import net.penumbra.enderscape.entity.ai.EnderscapeMemory;
import net.penumbra.enderscape.entity.rustle.RustleAI;
import net.penumbra.enderscape.registry.tag.EnderscapePoiTags;

import java.util.Set;
import java.util.stream.Collectors;

public class RustleNearestSleepingSpotSensor extends Sensor<Mob> {

    public RustleNearestSleepingSpotSensor() {
        super();
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(EnderscapeMemory.RUSTLE_SLEEPING_SPOT);
    }

    @Override
    protected void doTick(ServerLevel level, Mob mob) {
        if (mob.getBrain().hasMemoryValue(EnderscapeMemory.RUSTLE_SLEEP_TICKS) || mob.getBrain().hasMemoryValue(EnderscapeMemory.RUSTLE_SLEEPING_ON_COOLDOWN)) return;

        Set<Pair<Holder<PoiType>, BlockPos>> set = level.getPoiManager().findAllWithType(
                holder -> holder.is(EnderscapePoiTags.RUSTLE_SLEEPING_SPOT), pos -> RustleAI.HAS_STURDY_SURFACE.test(level, pos), mob.blockPosition(), 48, PoiManager.Occupancy.ANY)
                .collect(Collectors.toSet()
        );

        Path path = AcquirePoi.findPathToPois(mob, set);
        if (path != null && path.canReach()) {
            BlockPos target = path.getTarget();
            if (level.getPoiManager().getType(target).isPresent()) {
                mob.getBrain().setMemory(EnderscapeMemory.RUSTLE_SLEEPING_SPOT, target);
            }
        }
    }
}