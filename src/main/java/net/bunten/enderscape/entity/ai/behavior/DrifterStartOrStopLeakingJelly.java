package net.bunten.enderscape.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.bunten.enderscape.entity.ai.EnderscapeMemory;
import net.bunten.enderscape.entity.drifter.AbstractDrifter;
import net.bunten.enderscape.entity.drifter.Drifter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class DrifterStartOrStopLeakingJelly extends Behavior<AbstractDrifter> {

    public DrifterStartOrStopLeakingJelly() {
        super(ImmutableMap.of(EnderscapeMemory.DRIFTER_JELLY_CHANGE_COOLDOWN, MemoryStatus.VALUE_ABSENT));
    }

    private static final UniformInt NEXT_COOLDOWN_RANGE_IN_MINUTES = UniformInt.of(15, 20);

    public static void refreshCooldown(Drifter mob) {
        mob.getBrain().setMemory(EnderscapeMemory.DRIFTER_JELLY_CHANGE_COOLDOWN, NEXT_COOLDOWN_RANGE_IN_MINUTES.sample(mob.getRandom()) * 20 * 60);
    }
    
    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AbstractDrifter mob) {
        return mob instanceof Drifter;
    }

    @Override
    protected void start(ServerLevel level, AbstractDrifter mob, long l) {
        if (mob instanceof Drifter drifter) {
            drifter.setDrippingJelly(!drifter.isDrippingJelly());
            refreshCooldown(drifter);
        }
    }
}