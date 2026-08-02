package net.penumbra.enderscape.entity.ai.behavior.rubblemite;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.penumbra.enderscape.entity.ai.EnderscapeMemory;
import net.penumbra.enderscape.entity.rubblemite.Rubblemite;

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