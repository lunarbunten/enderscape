package net.bunten.enderscape.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.bunten.enderscape.entity.ai.EnderscapeMemory;
import net.bunten.enderscape.entity.rubblemite.Rubblemite;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class RubblemiteDashAfterPreparing extends Behavior<Rubblemite> {
    public RubblemiteDashAfterPreparing() {
        super(ImmutableMap.of(
                EnderscapeMemory.RUBBLEMITE_PREPARING_DASH, MemoryStatus.VALUE_PRESENT,
                EnderscapeMemory.RUBBLEMITE_PREPARING_DASH_TIME, MemoryStatus.VALUE_ABSENT,
                EnderscapeMemory.RUBBLEMITE_DASH_ON_COOLDOWN, MemoryStatus.VALUE_ABSENT
        ));
    }

    @Override
    protected void start(ServerLevel level, Rubblemite mob, long l) {
        mob.dash();
    }
}