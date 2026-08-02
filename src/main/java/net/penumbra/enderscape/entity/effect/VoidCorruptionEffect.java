package net.penumbra.enderscape.entity.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.penumbra.enderscape.registry.tag.EnderscapeEntityTags;

import static net.penumbra.enderscape.manager.VoidManager.*;

public class VoidCorruptionEffect extends MobEffect {
    public VoidCorruptionEffect() {
        super(MobEffectCategory.HARMFUL, 0x472D60);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplification) {
        if (entity.is(EnderscapeEntityTags.HEALED_BY_VOID_CORRUPTION) && isHealingInterval(entity.tickCount, amplification)) {
            entity.heal(1);
        } else {
            modifyVoidTicks(entity, value -> value + (MAXIMUM_VOID_TICKS / 60) * (1 << amplification));
            modifyVoidTickDownDelay(entity, value -> Math.max(value, MAXIMUM_VOID_TICKS));
        }
        
        return true;
    }

    private static boolean isHealingInterval(int tickCount, int amplification) {
        int interval = 50 >> amplification;
        return interval == 0 || tickCount % interval == 0;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
        return true;
    }
}