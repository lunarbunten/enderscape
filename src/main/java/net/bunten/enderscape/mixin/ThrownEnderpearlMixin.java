package net.bunten.enderscape.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ThrownEnderpearl.class)
public abstract class ThrownEnderpearlMixin extends ThrowableItemProjectile {

    public ThrownEnderpearlMixin(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    @ModifyArgs(method = "playSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;)V"))
    private void Enderscape$playSound(Args args) {
        if (EnderscapeConfig.getInstance().enderPearlUpdateTeleportSound) args.set(4, EnderscapeItemSounds.ENDER_PEARL_LAND);
    }

    @Inject(at = @At("TAIL"), method = "playSound")
    public void Enderscape$playSound(CallbackInfo info) {
        if (EnderscapeConfig.getInstance().enderPearlBreakParticles && level() instanceof ServerLevel server) {
            server.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, getItem()), getX(), getY(), getZ(), 12, 0, 0, 0, 0.1);
        }
    }

    @Inject(at = @At("TAIL"), method = "tick")
    public void Enderscape$tick(CallbackInfo info) {
        if (EnderscapeConfig.getInstance().enderPearlAddParticles && level().isClientSide()) {
            Vec3 vel = getDeltaMovement().scale(0.5F).scale(-1.0F);

            double x = random.nextGaussian() * 0.02 + vel.x;
            double y = random.nextGaussian() * 0.02 + vel.y;
            double z = random.nextGaussian() * 0.02 + vel.z;

            level().addParticle(EnderscapeParticles.ENDER_PEARL, getRandomX(4.0) - (vel.x / 2), getRandomY() - (vel.y / 2) + 0.5, getRandomZ(4.0) - (vel.z / 2), x, y, z);
        }
    }
}