package net.penumbra.enderscape.entity.ai.behavior.rustle;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.penumbra.enderscape.entity.ai.EnderscapeMemory;
import net.penumbra.enderscape.entity.rustle.Rustle;

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
        Rustle.regrowHair(level, mob, true);
        doStop(level, mob, l);
    }
}