package net.penumbra.enderscape.mixin.entity.projectile;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.level.Level;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EyeOfEnder.class)
public abstract class EyeOfEnderMixin extends Entity {

    public EyeOfEnderMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @ModifyArg(
            method = "spawnParticles",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V",
                    ordinal = 1
            ), index = 0
    )
    private ParticleOptions Enderscape$playSound(ParticleOptions particle) {
        return EnderscapeConfig.getInstance().entityUpdatePortalParticles ? EnderscapeParticles.VOID_ENTITY : particle;
    }
}