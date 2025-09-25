package net.bunten.enderscape.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.client.sound.ClientsideMagniaRadioHandler;
import net.bunten.enderscape.client.world.EnderscapeSkybox;
import net.bunten.enderscape.entity.EndTrialSpawnable;
import net.bunten.enderscape.registry.EnderscapeBlockSounds;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.registry.EnderscapeRegistries;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Environment(EnvType.CLIENT)
@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Unique
    private static final Minecraft client = Minecraft.getInstance();

    @Shadow
    private ClientLevel level;

    @Unique private final ClientsideMagniaRadioHandler Enderscape$magniaRadioHandler = new ClientsideMagniaRadioHandler();

    @Inject(method = "renderEndSky", at = @At("HEAD"), cancellable = true)
    public void renderEndSky(PoseStack pose, CallbackInfo ci) {
        if (EnderscapeConfig.getInstance().skyboxUpdateEnabled)  {
            EnderscapeSkybox.render(pose, level, client.gameRenderer.getMainCamera(), client.getTimer());
            ci.cancel();
        }
    }

    @ModifyArgs(method = "levelEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/sounds/SimpleSoundInstance;forLocalAmbience(Lnet/minecraft/sounds/SoundEvent;FF)Lnet/minecraft/client/resources/sounds/SimpleSoundInstance;"))
    public void levelEvent(Args args) {
        if (EnderscapeConfig.getInstance().endPortalUpdateTravelSound && level.dimension() == Level.END) {
            args.set(0, EnderscapeBlockSounds.END_PORTAL_TRAVEL);
        }
    }

    @Inject(at = @At("HEAD"), method = "levelEvent", cancellable = true)
    public void Enderscape$tryToStartFallFlying(int eventID, BlockPos pos, int extraID, CallbackInfo ci) {
        RandomSource random = level.getRandom();

        if (eventID == -624641) {
            Enderscape$magniaRadioHandler.playAmbientSound(pos);
        }

        if (eventID == -624642) {
            level.registryAccess().registryOrThrow(EnderscapeRegistries.MAGNIA_RADIO_SONG).getHolder(extraID).ifPresent(reference -> Enderscape$magniaRadioHandler.playSong(reference, pos));
        }

        if (eventID == -624643) {
            Enderscape$magniaRadioHandler.stopAllInstances(pos);
            Enderscape$magniaRadioHandler.notifyNearbyEntities(level, pos, false);
        }

        if (eventID == 3015 && level.getBlockState(pos).is(EnderscapeBlocks.END_VAULT)) {
            if (level.getBlockEntity(pos) instanceof VaultBlockEntity entity) {
                VaultBlockEntity.Client.emitActivationParticles(level, entity.getBlockPos(), entity.getBlockState(), entity.getSharedData(), EnderscapeParticles.VOID_STARS);
                level.playLocalSound(pos, EnderscapeBlockSounds.END_VAULT_ACTIVATE, SoundSource.BLOCKS, 1, Mth.nextFloat(random, 0.8F, 1.2F), true);
            }

            ci.cancel();
        }

        if (eventID == 3016 && level.getBlockState(pos).is(EnderscapeBlocks.END_VAULT)) {
            VaultBlockEntity.Client.emitDeactivationParticles(level, pos, EnderscapeParticles.VOID_STARS);
            level.playLocalSound(pos, EnderscapeBlockSounds.END_VAULT_DEACTIVATE, SoundSource.BLOCKS, 1, Mth.nextFloat(random, 0.8F, 1.2F), true);

            ci.cancel();
        }

        if (eventID == 3017 && level.getBlockState(pos).is(EnderscapeBlocks.END_VAULT)) {
            for (int b = 0; b < 20; b++) {

                double x = (double) pos.getX() + 0.4 + random.nextDouble() * 0.2;
                double y = (double) pos.getY() + 0.4 + random.nextDouble() * 0.2;
                double z = (double) pos.getZ() + 0.4 + random.nextDouble() * 0.2;

                double xd = random.nextGaussian() * 0.02;
                double yd = random.nextGaussian() * 0.02;
                double zd = random.nextGaussian() * 0.02;

                level.addParticle(EnderscapeParticles.VOID_STARS, x, y, z, xd, yd, zd * 0.25);
                level.addParticle(ParticleTypes.REVERSE_PORTAL, x, y, z, xd, yd, zd);
            }

            ci.cancel();
        }

        if (eventID == 3011 && level.getBlockState(pos).is(EnderscapeBlocks.END_TRIAL_SPAWNER)) {
            for (int i = 0; i < 20; i++) {
                double x = (double) pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 2.0;
                double y = (double) pos.getY() + 0.5 + (random.nextDouble() - 0.5) * 2.0;
                double z = (double) pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 2.0;

                level.addParticle(ParticleTypes.REVERSE_PORTAL, x, y, z, 0.0, 0.0, 0.0);
                level.addParticle(EnderscapeParticles.VOID_STARS, x, y, z, 0.0, 0.0, 0.0);
            }

            ci.cancel();
        }

        if (eventID == 3012 && !level.getEntitiesOfClass(Mob.class, new AABB(pos), EndTrialSpawnable::spawnedFromEndTrialSpawner).isEmpty()) {
            level.playLocalSound(pos, SoundEvents.TRIAL_SPAWNER_SPAWN_MOB, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, true);

            for (int i = 0; i < 20; i++) {
                double x = (double) pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 2.0;
                double y = (double) pos.getY() + 0.5 + (random.nextDouble() - 0.5) * 2.0;
                double z = (double) pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 2.0;

                level.addParticle(ParticleTypes.REVERSE_PORTAL, x, y, z, 0.0, 0.0, 0.0);
                level.addParticle(EnderscapeParticles.VOID_STARS, x, y, z, 0.0, 0.0, 0.0);
            }

            ci.cancel();
        }

        if (eventID == 3021 && level.getBlockState(pos).is(EnderscapeBlocks.END_TRIAL_SPAWNER)) {
            level.playLocalSound(pos, SoundEvents.TRIAL_SPAWNER_SPAWN_ITEM, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, true);

            for (int i = 0; i < 20; i++) {
                double x = (double) pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 2.0;
                double y = (double) pos.getY() + 0.5 + (random.nextDouble() - 0.5) * 2.0;
                double z = (double) pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 2.0;

                level.addParticle(ParticleTypes.REVERSE_PORTAL, x, y, z, 0.0, 0.0, 0.0);
                level.addParticle(EnderscapeParticles.VOID_STARS, x, y, z, 0.0, 0.0, 0.0);
            }

            ci.cancel();
        }

        if (eventID == 3013 && level.getBlockState(pos).is(EnderscapeBlocks.END_TRIAL_SPAWNER)) {
            level.playLocalSound(pos, SoundEvents.TRIAL_SPAWNER_DETECT_PLAYER, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, true);

            for (int j = 0; j < 30 + Math.min(j, 10) * 5; j++) {
                double x = (double) pos.getX() + 0.5 + (double) (2.0F * random.nextFloat() - 1.0F) * 0.65;
                double y = (double) pos.getY() + 0.1 + (double)random.nextFloat() * 0.8;
                double z = (double) pos.getZ() + 0.5 + (double) (2.0F * random.nextFloat() - 1.0F) * 0.65;

                level.addParticle(EnderscapeParticles.END_TRIAL_SPAWNER_DETECTION, x, y, z, 0.0, 0.0, 0.0);
            }

            ci.cancel();
        }

        if (eventID == 3014 && level.getBlockState(pos).is(EnderscapeBlocks.END_TRIAL_SPAWNER)) {
            level.playLocalSound(pos, SoundEvents.TRIAL_SPAWNER_EJECT_ITEM, SoundSource.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, true);

            for (int i = 0; i < 20; i++) {
                double x = (double)pos.getX() + 0.4 + random.nextDouble() * 0.2;
                double y = (double)pos.getY() + 0.4 + random.nextDouble() * 0.2;
                double z = (double)pos.getZ() + 0.4 + random.nextDouble() * 0.2;

                double xd = random.nextGaussian() * 0.02;
                double yd = random.nextGaussian() * 0.02;
                double zd = random.nextGaussian() * 0.02;

                level.addParticle(EnderscapeParticles.VOID_STARS, x, y, z, xd, yd, zd * 0.25);
                level.addParticle(ParticleTypes.REVERSE_PORTAL, x, y, z, xd, yd, zd);
            }

            ci.cancel();
        }

        if (eventID == 3017 && level.getBlockState(pos).is(EnderscapeBlocks.END_TRIAL_SPAWNER)) {
            for (int i = 0; i < 20; i++) {
                double x = (double)pos.getX() + 0.4 + random.nextDouble() * 0.2;
                double y = (double)pos.getY() + 0.4 + random.nextDouble() * 0.2;
                double z = (double)pos.getZ() + 0.4 + random.nextDouble() * 0.2;

                double xd = random.nextGaussian() * 0.02;
                double yd = random.nextGaussian() * 0.02;
                double zd = random.nextGaussian() * 0.02;

                level.addParticle(EnderscapeParticles.VOID_STARS, x, y, z, xd, yd, zd * 0.25);
                level.addParticle(ParticleTypes.REVERSE_PORTAL, x, y, z, xd, yd, zd);
            }

            ci.cancel();
        }
    }
}