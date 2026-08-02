package net.penumbra.enderscape.mixin.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.penumbra.enderscape.registry.entity.EnderscapeAttachments;
import net.penumbra.enderscape.renderer.StunTicksPercentage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState> extends EntityRenderer<T, S> {

    protected LivingEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("RETURN"))
    private void Enderscape$extractRenderState(T entity, S state, float partialTicks, CallbackInfo info) {
        if (state instanceof StunTicksPercentage instance) {
            float stunDuration = (float) entity.getAttachedOrElse(EnderscapeAttachments.STUN_DURATION, 0);
            float stunTicks = (float) entity.getAttachedOrElse(EnderscapeAttachments.STUN_TICKS, 0);

            if (stunDuration > 0.0F && !entity.isSpectator()) {
                instance.setStunTicksPercentage(Mth.clamp(stunTicks / stunDuration, 0.0F, 1.0F));
            } else {
                instance.setStunTicksPercentage(0.0F);
            }
        }
    }

    @Inject(
            method = "isShaking",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    public <S extends LivingEntityRenderState> void Enderscape$setShaking(S state, CallbackInfoReturnable<Boolean> info) {
        if (state instanceof StunTicksPercentage stunned && stunned.getStunTicksPercentage() > 0.0F) {
            info.setReturnValue(true);
        }
    }
}