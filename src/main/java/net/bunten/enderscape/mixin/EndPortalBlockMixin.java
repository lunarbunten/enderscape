package net.bunten.enderscape.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.EndPortalBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(EndPortalBlock.class)
public abstract class EndPortalBlockMixin extends BaseEntityBlock {

    protected EndPortalBlockMixin(Properties properties) {
        super(properties);
    }

    @ModifyArgs(method = "animateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private void Enderscape$ReplaceParticles(Args args) {
        if (EnderscapeConfig.getInstance().endPortalUpdateParticles) {
            args.set(0, EnderscapeParticles.END_PORTAL_STARS.get());
            args.set(2, ((double) args.get(2)) + 0.2);
            args.set(5, 0.0185);
        }
    }

    @WrapWithCondition(method = "animateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"))
    private boolean Enderscape$ReduceParticleCount(Level instance, ParticleOptions particleOptions, double d, double e, double f, double g, double h, double i) {
        if (EnderscapeConfig.getInstance().endPortalUpdateParticles && instance.getRandom().nextBoolean()) return false;
        return true;
    }
}