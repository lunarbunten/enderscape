package net.penumbra.enderscape.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.entity.ai.EnderscapePathTypes;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;
import net.penumbra.enderscape.registry.sound.EnderscapeEntitySounds;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Endermite.class)
public abstract class EndermiteMixin extends Monster {
    protected EndermiteMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void Enderscape$init(CallbackInfo info) {
        setPathfindingMalus(PathType.WATER, -1.0F);
        setPathfindingMalus(EnderscapePathTypes.VOID_FIRE, 2.0F);
        setPathfindingMalus(EnderscapePathTypes.VOID_LACHRYMA, 1.0F);
        setPathfindingMalus(EnderscapePathTypes.VOID_SHALE, 0.0F);
    }

    @WrapOperation(
            method = "aiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V",
                    ordinal = 0
            )
    )
    public void Enderscape$changeParticleOptions(Level level, ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd, Operation<Void> original) {
        if (EnderscapeConfig.getInstance().entityUpdatePortalParticles) {
            original.call(level, EnderscapeParticles.VOID_ENTITY, x, y, z, xd, yd, zd);
        } else {
            original.call(level, particle, x, y, z, xd, yd, zd);
        }
    }

    @Inject(method = "getAmbientSound", at = @At("RETURN"), cancellable = true)
    public void Enderscape$getAmbientSound(CallbackInfoReturnable<SoundEvent> info) {
        if (EnderscapeConfig.getInstance().endermiteUpdateSounds) info.setReturnValue(EnderscapeEntitySounds.ENDERMITE_AMBIENT);
    }

    @Inject(method = "getHurtSound", at = @At("RETURN"), cancellable = true)
    public void Enderscape$getHurtSound(DamageSource source, CallbackInfoReturnable<SoundEvent> info) {
        if (EnderscapeConfig.getInstance().endermiteUpdateSounds) info.setReturnValue(EnderscapeEntitySounds.ENDERMITE_HURT);
    }

    @Inject(method = "getDeathSound", at = @At("RETURN"), cancellable = true)
    public void Enderscape$getDeathSound(CallbackInfoReturnable<SoundEvent> info) {
        if (EnderscapeConfig.getInstance().endermiteUpdateSounds) info.setReturnValue(EnderscapeEntitySounds.ENDERMITE_DEATH);
    }

    @WrapOperation(
            method = "checkEndermiteSpawnRules",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/Endermite;checkAnyLightMonsterSpawnRules(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/EntitySpawnReason;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)Z"
            )
    )
    private static boolean Enderscape$changeSpawnRules(EntityType<Endermite> type, LevelAccessor level, EntitySpawnReason reason, BlockPos pos, RandomSource random, Operation<Boolean> original) {
        if (EnderscapeConfig.getInstance().endermiteNaturalSpawnsObeyLightLevel && level instanceof ServerLevelAccessor server) {
            return checkMonsterSpawnRules(type, server, reason, pos, random);
        } else {
            return original.call(type, level, reason, pos, random);
        }
    }

    @Override
    @Intrinsic
    public float getWalkTargetValue(final BlockPos pos, final LevelReader level) {
        return 0.0F;
    }
}