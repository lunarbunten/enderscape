package net.bunten.enderscape.client.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin {

    @Inject(method = "getArmorTexture", at = @At("HEAD"), cancellable = true)
    public void getArmorTexture(ArmorItem item, boolean legs, @Nullable String overlay, CallbackInfoReturnable<Identifier> info) {
        if (item == EnderscapeItems.DRIFT_LEGGINGS) {
            info.setReturnValue(Enderscape.id("textures/entity/drift_leggings.png"));
        }
    }
}