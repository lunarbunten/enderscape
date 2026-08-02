package net.penumbra.enderscape.mixin.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.material.FluidState;
import net.penumbra.enderscape.registry.block.EnderscapeFluids;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Environment(EnvType.CLIENT)
@Mixin(WeatherEffectRenderer.class)
public abstract class WeatherEffectRendererMixin {

    @Unique
    private FluidState cachedFluidState;

    @ModifyVariable(method = "tickRainParticles", at = @At("STORE"), name = "fluid")
    public FluidState Enderscape$cacheFluidState(FluidState value) {
        return cachedFluidState = value;
    }

    @ModifyArg(method = "tickRainParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    public ParticleOptions Enderscape$changeParticleType(ParticleOptions particle) {
        return cachedFluidState != null && cachedFluidState.is(EnderscapeFluids.VOID_LACHRYMA) ? EnderscapeParticles.SNOWFLAKE : particle;
    }
}