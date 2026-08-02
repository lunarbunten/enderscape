package net.penumbra.enderscape.mixin.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.monster.enderman.EndermanModel;
import net.minecraft.client.renderer.entity.EndermanRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EndermanRenderState;
import net.minecraft.world.entity.monster.EnderMan;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.renderer.entity.LightDependantEnderEyesLayer;
import net.penumbra.enderscape.renderer.entity.StoresSkylightFactor;
import net.penumbra.enderscape.renderer.entity.enderman.ImprovedEndermanRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(EndermanRenderer.class)
public abstract class EndermanRendererMixin extends MobRenderer<EnderMan, EndermanRenderState, EndermanModel<EndermanRenderState>> {

    public EndermanRendererMixin(EntityRendererProvider.Context context, EndermanModel<EndermanRenderState> model, float shadow) {
        super(context, model, shadow);
    }

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/monster/EnderMan;Lnet/minecraft/client/renderer/entity/state/EndermanRenderState;F)V",
            at = @At(value = "TAIL")
    )
    public void Enderscape$extract(EnderMan entity, EndermanRenderState state, float partialTicks, CallbackInfo info) {
        ImprovedEndermanRenderState.extract(entity, state);
        StoresSkylightFactor.extract(entity, state);
    }

    @ModifyArg(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/EndermanRenderer;addLayer(Lnet/minecraft/client/renderer/entity/layers/RenderLayer;)Z",
                    ordinal = 0
            )
    )
    public RenderLayer<?, ?> Enderscape$changeEyesLayer(RenderLayer<?, ?> original) {
        if (EnderscapeConfig.getInstance().endermanLightSensitiveEyes) {
            return LightDependantEnderEyesLayer.enderman(this);
        }
        return original;
    }
}