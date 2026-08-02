package net.penumbra.enderscape.mixin.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.renderer.item.DyeColorItemModelProperty;
import net.penumbra.enderscape.renderer.item.RubbleShieldVariantItemModelProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(SelectItemModelProperties.class)
public abstract class SelectItemModelPropertiesMixin {

    @Inject(method = "bootstrap", at = @At(value = "TAIL"))
    private static void Enderscape$bootstrap(CallbackInfo info) {
        SelectItemModelProperties.ID_MAPPER.put(Enderscape.id("rubble_shield_variant"), RubbleShieldVariantItemModelProperty.TYPE);
        SelectItemModelProperties.ID_MAPPER.put(Enderscape.id("dye_color"), DyeColorItemModelProperty.TYPE);
    }
}