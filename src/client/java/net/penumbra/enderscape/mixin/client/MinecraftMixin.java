package net.penumbra.enderscape.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.Music;
import net.minecraft.world.entity.player.Inventory;
import net.penumbra.enderscape.EnderscapeClient;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow @Nullable public LocalPlayer player;
    @Shadow @Nullable public ClientLevel level;

    @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
    private void Enderscape$cancelUseItemStart(CallbackInfo info) {
        if (EnderscapeMobEffects.isStunned(player)) {
            info.cancel();
        }
    }

    @Inject(method = "pickBlockOrEntity", at = @At("HEAD"), cancellable = true)
    private void Enderscape$cancelPickBlock(CallbackInfo info) {
        if (EnderscapeMobEffects.isStunned(player)) {
            info.cancel();
        }
    }

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void Enderscape$cancelAttackStart(CallbackInfoReturnable<Boolean> info) {
        if (EnderscapeMobEffects.isStunned(player)) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
    private void Enderscape$cancelAttack(boolean bl, CallbackInfo info) {
        if (EnderscapeMobEffects.isStunned(player)) {
            info.cancel();
        }
    }

    @Inject(method = "setScreenAndShow", at = @At("HEAD"), cancellable = true)
    private void Enderscape$cancelSetScreen(Screen screen, CallbackInfo info) {
        if (screen instanceof AbstractContainerScreen<?> && EnderscapeMobEffects.isStunned(player)) {
            info.cancel();
            if (player.containerMenu != player.inventoryMenu) player.closeContainer();
        }
    }

    @WrapWithCondition(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;sendOpenInventory()V"))
    private boolean Enderscape$cancelOpenInventory(LocalPlayer instance) {
        return !EnderscapeMobEffects.isStunned(instance);
    }

    @WrapWithCondition(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V"))
    private boolean Enderscape$cancelSwapHands(ClientPacketListener instance, Packet<?> packet) {
        return !EnderscapeMobEffects.isStunned(player);
    }

    @WrapWithCondition(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"))
    private boolean Enderscape$cancelHotbarChange(Inventory instance, int slot) {
        return !EnderscapeMobEffects.isStunned(player);
    }

    @ModifyReturnValue(method = "getSituationalMusic", at = @At("RETURN"))
    public Music Enderscape$getSituationalMusic(Music original) {
        return EnderscapeClient.clientsideVariables().structureMusic.orElse(original);
    }
}