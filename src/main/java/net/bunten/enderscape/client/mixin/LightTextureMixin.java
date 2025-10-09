package net.bunten.enderscape.client.mixin;

import net.bunten.enderscape.client.world.EnderscapeSkybox;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LightTexture.class)
public class LightTextureMixin {

    @ModifyVariable(
            method = "updateLightTexture",
            at = @At(value = "STORE"),
            ordinal = 1,
            name = "vector3f2"
    )
    private Vector3f changeEndFlashColor(Vector3f original) {
        Vector4f color = EnderscapeSkybox.flashColor;
        return new Vector3f(color.x, color.y, color.z);
    }
}