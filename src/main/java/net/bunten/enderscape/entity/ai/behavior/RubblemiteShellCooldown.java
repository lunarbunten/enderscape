package net.bunten.enderscape.entity.ai.behavior;

import net.bunten.enderscape.entity.ai.EnderscapeMemory;
import net.bunten.enderscape.entity.rubblemite.Rubblemite;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;

public class RubblemiteShellCooldown extends CountDownCooldownTicks {
    public RubblemiteShellCooldown() {
        super(EnderscapeMemory.RUBBLEMITE_HIDING_DURATION);
    }
    
    @Override
    protected void stop(ServerLevel level, LivingEntity mob, long l) {
        if (mob instanceof Rubblemite rubblemite) rubblemite.exitShell();
        super.stop(level, mob, l);
    }
}