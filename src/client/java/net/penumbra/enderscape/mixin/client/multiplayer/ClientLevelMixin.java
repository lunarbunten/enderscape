package net.penumbra.enderscape.mixin.client.multiplayer;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.material.FluidState;
import net.penumbra.enderscape.registry.block.EnderscapeFluids;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;
import net.penumbra.enderscape.registry.sound.EnderscapeBiomeSounds;
import net.penumbra.enderscape.renderer.value.EndFlashParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

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

    @ModifyArg(method = "tickWeatherEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    public ParticleOptions Enderscape$changeParticleType(ParticleOptions particle, @Local(name = "fluid") FluidState fluid) {
        return fluid.is(EnderscapeFluids.VOID_LACHRYMA) ? EnderscapeParticles.SNOWFLAKE : particle;
    }
}