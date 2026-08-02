package net.penumbra.enderscape.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.penumbra.enderscape.manager.ClientsideVoidManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @ModifyReturnValue(
            method = "modifyFovBasedOnDeathOrFluid",
            at = @At(value = "RETURN")
    )
    public float Enderscape$modifyFovInsideVoidLachryma(float original) {
        if (ClientsideVoidManager.cameraInVoidLachryma()) {
            float effectScale = minecraft.options.fovEffectScale().get().floatValue();
            original *= Mth.lerp(effectScale, 1.0F, 0.85714287F);
        }

        return original;
    }
}