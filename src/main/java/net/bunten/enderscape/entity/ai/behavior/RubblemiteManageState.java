package net.bunten.enderscape.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.bunten.enderscape.entity.ai.EnderscapeMemory;
import net.bunten.enderscape.entity.rubblemite.Rubblemite;
import net.bunten.enderscape.registry.EnderscapeEntitySounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;

public class RubblemiteManageState extends Behavior<Rubblemite> {
    public RubblemiteManageState() {
        super(ImmutableMap.of());
    }

    @Override
    protected void start(ServerLevel level, Rubblemite mob, long l) {
        Brain<Rubblemite> brain = mob.getBrain();
        if (mob.isAlive()) {
            if (mob.isInWaterOrRain()) mob.enterShell(40);
            if (brain.hasMemoryValue(EnderscapeMemory.RUBBLEMITE_HIDING_DURATION.get())) {
                int i = brain.getMemory(EnderscapeMemory.RUBBLEMITE_HIDING_DURATION.get()).get();
                if (i == 2) mob.playSound(EnderscapeEntitySounds.RUBBLEMITE_EXTRUDE.get(), 1, 1);
            } else {
                if (!mob.isPreparingToDash() && (mob.isInsideShell() || mob.shouldStopDashing())) mob.setState(Rubblemite.State.IDLING);
            }
        } else {
            brain.eraseMemory(EnderscapeMemory.RUBBLEMITE_HIDING_DURATION.get());
            brain.eraseMemory(EnderscapeMemory.RUBBLEMITE_HIDING_ON_COOLDOWN.get());
        }
    }
}