package net.penumbra.enderscape.mixin.client.multiplayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundEvent;
import net.penumbra.enderscape.registry.sound.EnderscapeBiomeSounds;
import net.penumbra.enderscape.renderer.value.EndFlashParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {

    @ModifyArg(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resources/sounds/DirectionalSoundInstance;<init>(Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;Lnet/minecraft/util/RandomSource;Lnet/minecraft/client/Camera;FF)V"
            )
    )
    public SoundEvent Enderscape$changeSoundEvent(SoundEvent original) {
        return EndFlashParameters.improved() ? EnderscapeBiomeSounds.END_FLASH : original;
    }
}