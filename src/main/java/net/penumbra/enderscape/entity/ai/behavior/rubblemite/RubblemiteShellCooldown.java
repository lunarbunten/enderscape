package net.penumbra.enderscape.entity.ai.behavior.rubblemite;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
import net.penumbra.enderscape.entity.ai.EnderscapeMemory;
import net.penumbra.enderscape.entity.rubblemite.Rubblemite;

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