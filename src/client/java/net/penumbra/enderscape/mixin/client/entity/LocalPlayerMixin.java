package net.penumbra.enderscape.mixin.client.entity;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.penumbra.enderscape.manager.EndHavenManager;
import net.penumbra.enderscape.network.ServerboundRespawnFromEndHavenPayload;
import net.penumbra.enderscape.registry.entity.EnderscapeDamageTypes;
import net.penumbra.enderscape.registry.sound.EnderscapeUiSounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {

    @Shadow
    public abstract boolean shouldShowDeathScreen();

    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @Inject(method = "respawn", at = @At("HEAD"))
    public void Enderscape$setSpawnFromHaven(CallbackInfo info) {
        if (EndHavenManager.promptHavenRespawnChoice(this) && !shouldShowDeathScreen()) {
            ClientPlayNetworking.send(new ServerboundRespawnFromEndHavenPayload());
        }
    }

    @Inject(method = "tickDeath", at = @At("HEAD"))
    public void Enderscape$playVoidDeathSound(CallbackInfo info) {
        if (deathTime == 1 && getLastDamageSource() != null && getLastDamageSource().is(EnderscapeDamageTypes.OUTER_VOID) && gameMode().isSurvival()) {
            playSound(EnderscapeUiSounds.HEALTH_OUTER_VOID_DEATH, 1.0F, 1.0F);
        }
    }
}