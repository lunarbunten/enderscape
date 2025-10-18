package net.bunten.enderscape.client.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.client.renderer.EnderscapeRenderPipelines;
import net.bunten.enderscape.client.renderer.LightingStyle;
import net.bunten.enderscape.client.world.EndFlashParameters;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LightTexture.class)
public class LightTextureMixin {

    @Unique
    public final EnderscapeConfig Enderscape$config = EnderscapeConfig.getInstance();

    @Shadow
    @Final
    private Minecraft minecraft;

    @ModifyArgs(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setPipeline(Lcom/mojang/blaze3d/pipeline/RenderPipeline;)V"))
    private void Enderscape$changeEndLightmap(Args args) {
        ClientLevel level = minecraft.level;
        if (level != null && level.dimension().equals(Level.END) && Enderscape$config.lightingStyle != LightingStyle.VANILLA) {
            switch (Enderscape$config.lightingStyle) {
                case IMPROVED -> args.set(0, EnderscapeRenderPipelines.IMPROVED_LIGHTMAP);
                case MIDNIGHT -> args.set(0, EnderscapeRenderPipelines.MIDNIGHT_LIGHTMAP);
            }
        }
    }

    @ModifyVariable(
            method = "updateLightTexture",
            at = @At(value = "STORE"),
            ordinal = 1,
            name = "vector3f2"
    )
    private Vector3f Enderscape$updateEndFlashColor(Vector3f original) {
        ClientLevel level = minecraft.level;
        if (level != null && level.effects().hasEndFlashes() && EnderscapeConfig.getInstance().flashUpdatedVisuals) {
            Vector4f color = EndFlashParameters.color;
            return new Vector3f(color.x, color.y, color.z);
        }
        return original;
    }
}