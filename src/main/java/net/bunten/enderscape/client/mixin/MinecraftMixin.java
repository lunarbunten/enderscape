package net.bunten.enderscape.client.mixin;

import net.bunten.enderscape.client.EnderscapeClient;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeMobEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Optionull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

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

    @Inject(method = "pickBlock", at = @At("HEAD"), cancellable = true)
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

    @Inject(method = "getSituationalMusic", at = @At("HEAD"), cancellable = true)
    public void getSituationalMusic(CallbackInfoReturnable<Music> info) {
        Music music = Optionull.map(instance.screen, Screen::getBackgroundMusic);

        if (music != null) {
            if (instance.screen instanceof WinScreen && !instance.getMusicManager().isPlayingMusic(Musics.CREDITS)) instance.getSoundManager().stop();
            return;
        }

        LocalPlayer player = instance.player;

        if (player != null && !instance.gui.getBossOverlay().shouldPlayMusic()) {
            EnderscapeClient.structureMusic.ifPresentOrElse(info::setReturnValue, () -> {
                Level level = player.level();
                if (level.dimension() == Level.END) {
                    Biome biome = level.getBiome(player.blockPosition()).value();
                    Optional<Music> optional = biome.getBackgroundMusic();
                    optional.ifPresent(info::setReturnValue);
                }
            });
        }
    }
}