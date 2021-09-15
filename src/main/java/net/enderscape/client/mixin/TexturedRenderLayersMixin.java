package net.enderscape.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.enderscape.blocks.EndSignType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;

@Environment(EnvType.CLIENT)
@Mixin(TexturedRenderLayers.class)
public class TexturedRenderLayersMixin {
    @Inject(method = "createSignTextureId", at = @At("HEAD"), cancellable = true)
    private static void replaceSignTextureId(SignType type, CallbackInfoReturnable<SpriteIdentifier> info) {
        if (type instanceof EndSignType end) {
            Identifier id = Identifier.tryParse(type.getName());
            if (id != null) {
                info.setReturnValue(new SpriteIdentifier(TexturedRenderLayers.SIGNS_ATLAS_TEXTURE, end.getTexture()));
            }
        }
    }
}