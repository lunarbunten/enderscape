package net.bunten.enderscape.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.bunten.enderscape.blocks.EnderscapeSignType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.WoodType;

@Environment(EnvType.CLIENT)
@Mixin(Sheets.class)
public class SheetsMixin {

    @Inject(method = "createSignMaterial", at = @At("HEAD"), cancellable = true)
    private static void createSignMaterial(WoodType type, CallbackInfoReturnable<Material> info) {
        if (type instanceof EnderscapeSignType end && ResourceLocation.tryParse(type.name()) != null) {
            info.setReturnValue(new Material(Sheets.SIGN_SHEET, end.getTexturePath()));
        }
    }
}