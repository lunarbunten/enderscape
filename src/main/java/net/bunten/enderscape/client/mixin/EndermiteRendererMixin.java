package net.bunten.enderscape.client.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.client.entity.EndermiteEyesLayer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.monster.endermite.EndermiteModel;
import net.minecraft.client.renderer.entity.EndermiteRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.monster.Endermite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(EndermiteRenderer.class)
public abstract class EndermiteRendererMixin extends MobRenderer<Endermite, LivingEntityRenderState, EndermiteModel> {

    public EndermiteRendererMixin(EntityRendererProvider.Context context, EndermiteModel entityModel, float f) {
        super(context, entityModel, f);
    }

    @Unique
    private final EndermiteRenderer renderer = (EndermiteRenderer) (Object) this;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void Enderscape$getBrightnessDependentFogColor(EntityRendererProvider.Context context, CallbackInfo ci) {
        if (EnderscapeConfig.getInstance().endermiteEmissiveEyes) addLayer(new EndermiteEyesLayer(renderer));
    }
}