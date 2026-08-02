package net.penumbra.enderscape.mixin.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.DripParticle;
import net.penumbra.enderscape.registry.tag.EnderscapeFluidTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.penumbra.enderscape.registry.particle.EnderscapeParticleProviders.VOID_LACHRYMA_PARTICLE_COLOR;

@Environment(EnvType.CLIENT)
@Mixin(targets = "net.minecraft.client.particle.DripParticle$CoolingDripHangParticle")
public abstract class CoolingDripHangParticleMixin {

    @Unique
    private final DripParticle.CoolingDripHangParticle particle = (DripParticle.CoolingDripHangParticle) (Object) this;

    @Inject(method = "preMoveUpdate", at = @At("TAIL"))
    public void Enderscape$changeParticleColor(CallbackInfo info) {
        if (particle.getType().is(EnderscapeFluidTags.VOID_LACHRYMA)) {
            particle.setColor(
                    VOID_LACHRYMA_PARTICLE_COLOR.x(),
                    VOID_LACHRYMA_PARTICLE_COLOR.y(),
                    VOID_LACHRYMA_PARTICLE_COLOR.z()
            );
        }
    }
}