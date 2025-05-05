package net.bunten.enderscape.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.bunten.enderscape.entity.ai.EnderscapeMemory;
import net.bunten.enderscape.entity.rustle.Rustle;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class RustleRegrowHairNaturally extends Behavior<Rustle> {

    public RustleRegrowHairNaturally() {
        super(ImmutableMap.of(EnderscapeMemory.RUSTLE_HAIR_REGROWTH_COOLDOWN, MemoryStatus.VALUE_ABSENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Rustle mob) {
        return mob.isSheared();
    }

    @Override
    protected void start(ServerLevel level, Rustle mob, long l) {
        Rustle.regrowHairWithParticles(level, mob);
        stop(level, mob, l);
    }
}