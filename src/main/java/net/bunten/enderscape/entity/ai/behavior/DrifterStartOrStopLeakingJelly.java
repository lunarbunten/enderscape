package net.bunten.enderscape.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.bunten.enderscape.entity.ai.EnderscapeMemory;
import net.bunten.enderscape.entity.drifter.Drifter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class DrifterStartOrStopLeakingJelly extends Behavior<Drifter> {

    public DrifterStartOrStopLeakingJelly() {
        super(ImmutableMap.of(EnderscapeMemory.DRIFTER_JELLY_CHANGE_COOLDOWN.get(), MemoryStatus.VALUE_ABSENT));
    }

    private static final UniformInt NEXT_COOLDOWN_RANGE_IN_MINUTES = UniformInt.of(15, 20);

    public static void refreshCooldown(Drifter mob) {
        mob.getBrain().setMemory(EnderscapeMemory.DRIFTER_JELLY_CHANGE_COOLDOWN.get(), NEXT_COOLDOWN_RANGE_IN_MINUTES.sample(mob.getRandom()) * 20 * 60);
    }
    
    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Drifter mob) {
        return !mob.isBaby();
    }

    @Override
    protected void start(ServerLevel level, Drifter mob, long l) {
        mob.setDrippingJelly(!mob.isDrippingJelly());
        refreshCooldown(mob);
    }
}