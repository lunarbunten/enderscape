package net.bunten.enderscape.client.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.client.entity.EndermiteEyesLayer;
import net.minecraft.client.model.EndermiteModel;
import net.minecraft.client.renderer.entity.EndermiteRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.monster.Endermite;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(EndermiteRenderer.class)
public abstract class EndermiteRendererMixin extends MobRenderer<Endermite, EndermiteModel<Endermite>> {

    @Unique
    private final EndermiteRenderer renderer = (EndermiteRenderer) (Object) this;

    public EndermiteRendererMixin(EntityRendererProvider.Context context, EndermiteModel<Endermite> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void Enderscape$getBrightnessDependentFogColor(EntityRendererProvider.Context context, CallbackInfo ci) {
        if (EnderscapeConfig.getInstance().endermiteEmissiveEyes) addLayer(new EndermiteEyesLayer(renderer));
    }
}