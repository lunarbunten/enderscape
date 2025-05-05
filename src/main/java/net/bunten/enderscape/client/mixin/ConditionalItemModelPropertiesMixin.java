package net.bunten.enderscape.client.mixin;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.item.Enabled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ConditionalItemModelProperties.class)
public abstract class ConditionalItemModelPropertiesMixin  {

    @Inject(method = "bootstrap", at = @At(value = "TAIL"))
    private static void Enderscape$handleEntityEvent(CallbackInfo ci) {
        ConditionalItemModelProperties.ID_MAPPER.put(Enderscape.id("enabled"), Enabled.MAP_CODEC);
    }
}