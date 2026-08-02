package net.penumbra.enderscape.mixin.client.renderer.feature;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
import net.minecraft.util.ARGB;
import net.penumbra.enderscape.renderer.ItemEntityTint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(ItemFeatureRenderer.class)
public class ItemFeatureRendererMixin {

    @ModifyExpressionValue(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/ItemFeatureRenderer;getLayerColorSafe([ILnet/minecraft/client/resources/model/geometry/BakedQuad$MaterialInfo;)I"))
    private int Enderscape$tintItemQuad(int color, @Local(argsOnly = true) SubmitNodeStorage.ItemSubmit submit) {
        int tint = ((ItemEntityTint) (Object) submit).color();

        if (tint == ItemEntityTint.NONE) {
            return color;
        } else {
            return ARGB.multiply(color, tint);
        }
    }
}