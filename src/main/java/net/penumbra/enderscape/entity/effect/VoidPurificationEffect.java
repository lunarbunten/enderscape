package net.penumbra.enderscape.entity.effect;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.penumbra.enderscape.manager.VoidManager;
import net.penumbra.enderscape.network.ClientboundHealthVoidPurifySoundPayload;
import net.penumbra.enderscape.registry.entity.EnderscapeDamageTypes;

import static net.penumbra.enderscape.registry.tag.EnderscapeEntityTags.HEALED_BY_VOID_PURIFICATION;
import static net.penumbra.enderscape.registry.tag.EnderscapeEntityTags.HURT_BY_VOID_PURIFICATION;

public class VoidPurificationEffect extends MobEffect {
    public VoidPurificationEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFD559);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        super.onEffectStarted(entity, amplifier);

        if (VoidManager.hasVoidedHealth(entity)) {
            VoidManager.modifyVoidedHealth(entity, value -> value - (value % 2));
        }
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplification) {
        if (entity.is(HURT_BY_VOID_PURIFICATION)) {
            entity.hurtServer(level, level.damageSources().source(EnderscapeDamageTypes.VOID_PURIFICATION), 4.0F);
            return true;
        }

        if (VoidManager.hasVoidedHealth(entity)) {
            VoidManager.modifyVoidedHealth(entity, value -> value - 2);
            if (entity instanceof ServerPlayer player) ServerPlayNetworking.send(player, new ClientboundHealthVoidPurifySoundPayload());
        } else if (entity.is(HEALED_BY_VOID_PURIFICATION)) {
            entity.heal(1);
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
        int interval = 80 >> amplification;
        return interval > 0 ? tickCount % interval == 0 : true;
    }
}