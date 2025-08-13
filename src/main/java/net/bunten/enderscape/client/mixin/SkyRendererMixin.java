package net.bunten.enderscape.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.client.world.EnderscapeSkybox;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Environment(EnvType.CLIENT)
@Mixin(SkyRenderer.class)
public abstract class SkyRendererMixin {

    @Unique
    private final Minecraft client = Minecraft.getInstance();

    @Inject(method = "renderEndSky", at = @At("HEAD"), cancellable = true)
    public void renderEndSky(CallbackInfo ci) {
        if (EnderscapeConfig.getInstance().skyboxUpdateEnabled && client.level != null)  {
            EnderscapeSkybox.render(new PoseStack(), client.level, client.gameRenderer.getMainCamera(), client.getDeltaTracker());
            ci.cancel();
        }
    }

    @ModifyArgs(method = "renderEndFlash", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ARGB;color(FI)I"))
    private void Enderscape$playSound(Args args) {
        Vector4f thing = EnderscapeSkybox.flashColor;
        args.set(1, ARGB.color(new Vec3(thing.x, thing.y, thing.z)));
    }
}