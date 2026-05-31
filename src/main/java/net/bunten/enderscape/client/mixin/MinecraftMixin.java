package net.bunten.enderscape.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.bunten.enderscape.client.EnderscapeClient;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeMobEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.Music;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow
    static Minecraft instance;

    @Shadow @Nullable public LocalPlayer player;

    @Shadow @Nullable public ClientLevel level;

    @Unique
    private boolean Enderscape$shouldPreventAttack() {
        boolean daggerOnCooldown = player != null && player.getMainHandItem().is(EnderscapeItems.DAGGER) && player.getAttackStrengthScale(0.0F) < 1;
        return daggerOnCooldown || EnderscapeMobEffects.isStunned(player);
    }

    @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
    private void Enderscape$startUseItem(CallbackInfo info) {
        if (EnderscapeMobEffects.isStunned(player)) {
            info.cancel();
        }
    }

    @Inject(method = "pickBlockOrEntity", at = @At("HEAD"), cancellable = true)
    private void Enderscape$pickBlock(CallbackInfo info) {
        if (EnderscapeMobEffects.isStunned(player)) {
            info.cancel();
        }
    }

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void Enderscape$startAttack(CallbackInfoReturnable<Boolean> info) {
        if (Enderscape$shouldPreventAttack()) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
    private void Enderscape$continueAttack(boolean bl, CallbackInfo info) {
        if (Enderscape$shouldPreventAttack()) {
            info.cancel();
        }
    }

    @ModifyReturnValue(method = "getSituationalMusic", at = @At("RETURN"))
    public Music getSituationalMusic(Music original) {
        return EnderscapeClient.structureMusic.orElse(original);
    }
}