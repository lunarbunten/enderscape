package net.penumbra.enderscape.entity.ai.behavior.rubblemite;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.penumbra.enderscape.entity.ai.EnderscapeMemory;
import net.penumbra.enderscape.entity.rubblemite.Rubblemite;
import net.penumbra.enderscape.registry.sound.EnderscapeEntitySounds;

public class RubblemiteManageState extends Behavior<Rubblemite> {
    public RubblemiteManageState() {
        super(ImmutableMap.of());
    }

    @Override
    protected void start(ServerLevel level, Rubblemite mob, long l) {
        Brain<Rubblemite> brain = mob.getBrain();
        if (mob.isAlive()) {
            if (mob.isInWaterOrRain()) {
                if (mob.isInsideShell()) {
                    brain.setMemory(EnderscapeMemory.RUBBLEMITE_HIDING_DURATION, 40);
                } else {
                    mob.enterShell(40);
                }
            }

            if (brain.hasMemoryValue(EnderscapeMemory.RUBBLEMITE_HIDING_DURATION)) {
                int i = brain.getMemory(EnderscapeMemory.RUBBLEMITE_HIDING_DURATION).get();
                if (i == 2) mob.playSound(EnderscapeEntitySounds.RUBBLEMITE_EXTRUDE, 1, 1);
            } else {
                if (!brain.hasMemoryValue(EnderscapeMemory.RUBBLEMITE_PREPARING_DASH) && (mob.isInsideShell() || mob.shouldStopDashing())) mob.setState(Rubblemite.State.IDLING);
            }
        } else {
            brain.eraseMemory(EnderscapeMemory.RUBBLEMITE_HIDING_DURATION);
            brain.eraseMemory(EnderscapeMemory.RUBBLEMITE_HIDING_ON_COOLDOWN);
        }
    }
}