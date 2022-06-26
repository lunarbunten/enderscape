package net.bunten.enderscape.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.bunten.enderscape.client.ClientUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.sound.MusicSound;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Inject(method = "getMusicType", at = @At("HEAD"), cancellable = true)
    public void getMusicType(CallbackInfoReturnable<MusicSound> info) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!(client.currentScreen instanceof CreditsScreen) && client.player != null && client.world.getRegistryKey() == World.END) {
            info.setReturnValue(ClientUtil.getEndMusic());
        }
    }
}