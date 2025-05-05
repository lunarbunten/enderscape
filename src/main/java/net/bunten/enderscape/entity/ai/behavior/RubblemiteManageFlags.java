package net.bunten.enderscape.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import net.bunten.enderscape.entity.ai.EnderscapeMemory;
import net.bunten.enderscape.entity.rubblemite.Rubblemite;
import net.bunten.enderscape.registry.EnderscapeEntitySounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;

public class RubblemiteManageFlags extends Behavior<Rubblemite> {
    public RubblemiteManageFlags() {
        super(ImmutableMap.of());
    }

    @Override
    protected void start(ServerLevel level, Rubblemite mob, long l) {
        Brain<Rubblemite> brain = mob.getBrain();
        if (mob.isAlive()) {
            if (mob.isInWaterOrRain()) mob.enterShell(40);
            if (brain.hasMemoryValue(EnderscapeMemory.RUBBLEMITE_HIDING_DURATION)) {
                int i = brain.getMemory(EnderscapeMemory.RUBBLEMITE_HIDING_DURATION).get();
                if (i == 2) mob.playSound(EnderscapeEntitySounds.RUBBLEMITE_EXTRUDE, 1, 1);
            } else {
                if (mob.isInsideShell() || mob.shouldStopDashing()) mob.setFlags(Rubblemite.DEFAULT_FLAG);
            }
        } else {
            brain.eraseMemory(EnderscapeMemory.RUBBLEMITE_HIDING_DURATION);
            brain.eraseMemory(EnderscapeMemory.RUBBLEMITE_HIDING_ON_COOLDOWN);
        }
    }
}