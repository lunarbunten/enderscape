package net.penumbra.enderscape.mixin.block.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TrialSpawnerState.class)
public class TrialSpawnerStateMixin {

    @Shadow @Final private TrialSpawnerState.ParticleEmission particleEmission;

    @Unique
    private static void addParticle(SimpleParticleType type, Vec3 vec3, Level level) {
        level.addParticle(type, vec3.x(), vec3.y(), vec3.z(), 0.0, 0.0, 0.0);
    }

    @Inject(method = "emitParticles", at = @At("HEAD"), cancellable = true)
    private void Enderscape$emitParticles(Level level, BlockPos pos, boolean bl, CallbackInfo info) {
        if (level.getBlockState(pos).is(EnderscapeBlocks.END_TRIAL_SPAWNER)) {
            RandomSource random = level.getRandom();

            if (particleEmission == TrialSpawnerState.ParticleEmission.SMALL_FLAMES) {
                if (random.nextInt(2) == 0) addParticle(ParticleTypes.PORTAL, Vec3.atCenterOf(pos).offsetRandom(random, 0.9F), level);
            }

            if (particleEmission == TrialSpawnerState.ParticleEmission.FLAMES_AND_SMOKE) {
                Vec3 vec3 = Vec3.atCenterOf(pos).offsetRandom(random, 1.0F);
                addParticle(EnderscapeParticles.VOID_STARS, vec3, level);
                addParticle(ParticleTypes.PORTAL, vec3, level);
            }

            if (particleEmission == TrialSpawnerState.ParticleEmission.SMOKE_INSIDE_AND_TOP_FACE) {
                Vec3 vec3 = Vec3.atCenterOf(pos).offsetRandom(random, 0.9F);
                if (random.nextInt(3) == 0) addParticle(EnderscapeParticles.VOID_STARS, vec3, level);

                if (level.getGameTime() % 40L == 0L) {
                   for (int j = 0; j < level.getRandom().nextInt(4) + 20; ++j) {
                       Vec3 center = Vec3.atCenterOf(pos);
                       double horizontalRadius = 0.01;
                       level.addParticle(EnderscapeParticles.END_TRIAL_SPAWNER_EXHALE, center.x, center.y + 0.5, center.z, Mth.nextDouble(random, -horizontalRadius, horizontalRadius), Mth.nextDouble(random, 0.02, 0.04), Mth.nextDouble(random, -horizontalRadius, horizontalRadius));
                   }
                }
            }

            info.cancel();
        }
    }
}
