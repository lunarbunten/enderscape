package net.penumbra.enderscape.entity.ai.behavior.rubblemite;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.penumbra.enderscape.entity.ai.EnderscapeMemory;
import net.penumbra.enderscape.entity.rubblemite.Rubblemite;

public class RubblemitePrepareDashDuringCombat extends Behavior<Rubblemite> {
    public RubblemitePrepareDashDuringCombat() {
        super(ImmutableMap.of(
                EnderscapeMemory.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                EnderscapeMemory.RUBBLEMITE_PREPARING_DASH, MemoryStatus.VALUE_ABSENT,
                EnderscapeMemory.RUBBLEMITE_DASH_ON_COOLDOWN, MemoryStatus.VALUE_ABSENT
        ));
    }
    
    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Rubblemite mob) {
        if (mob.getState() == Rubblemite.State.IDLING && !mob.isVehicle() && mob.onGround() && !mob.hasEffect(MobEffects.SLOWNESS)) {
            double d = mob.distanceToSqr(EnderscapeMemory.getAttackTarget(mob));
            if (d >= 6 && d <= 35) {
                return mob.getRandom().nextInt(10) == 0;
            }
        }
        return false;
    }

    @Override
    protected void start(ServerLevel level, Rubblemite mob, long l) {
        mob.prepareDash();
    }
}