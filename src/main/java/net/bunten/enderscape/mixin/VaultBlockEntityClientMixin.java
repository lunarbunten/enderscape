package net.bunten.enderscape.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.bunten.enderscape.registry.EnderscapeBlockSounds;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.VaultBlock;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultSharedData;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.UUID;

@Mixin(VaultBlockEntity.Client.class)
public abstract class VaultBlockEntityClientMixin {

    @Shadow
    private static void emitIdleParticles(Level level, BlockPos blockPos, VaultSharedData vaultSharedData, ParticleOptions particleOptions) {
    }

    @Shadow
    private static Vec3 keyholePos(BlockPos blockPos, Direction direction) {
        return null;
    }

    @Shadow
    private static boolean isWithinConnectionRange(BlockPos blockPos, VaultSharedData vaultSharedData, Player player) {
        return false;
    }

    @Inject(method = "emitConnectionParticlesForNearbyPlayers", at = @At("HEAD"), cancellable = true)
    private static void getStateWithConnections(Level level, BlockPos pos, BlockState blockState, VaultSharedData vaultSharedData, CallbackInfo info) {
        if (level.getBlockState(pos).is(EnderscapeBlocks.END_VAULT)) {
            Set<UUID> set = vaultSharedData.getConnectedPlayers();
            if (!set.isEmpty()) {
                Vec3 vec3 = keyholePos(pos, blockState.getValue(VaultBlock.FACING));

                for (UUID uUID : set) {
                    Player player = level.getPlayerByUUID(uUID);
                    if (player != null && isWithinConnectionRange(pos, vaultSharedData, player)) {
                        RandomSource randomSource = level.random;
                        Vec3 vec32 = vec3.vectorTo(player.position().add(0.0, player.getBbHeight() / 2.0F, 0.0));
                        int i = Mth.nextInt(randomSource, 2, 5);

                        for (int j = 0; j < i; j++) {
                            Vec3 vec33 = vec32.offsetRandom(randomSource, 1.0F);
                            level.addParticle(EnderscapeParticles.END_VAULT_CONNECTION, vec3.x(), vec3.y(), vec3.z(), vec33.x(), vec33.y(), vec33.z());
                        }
                    }
                }
            }

            info.cancel();
        }
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/vault/VaultBlockEntity$Client;emitIdleParticles(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/vault/VaultSharedData;Lnet/minecraft/core/particles/ParticleOptions;)V"))
    private static void getStateWithConnections(Level level, BlockPos pos, VaultSharedData data, ParticleOptions options, Operation<Void> original) {
        if (level.getBlockState(pos).is(EnderscapeBlocks.END_VAULT)) {
            original.call(level, pos, data, EnderscapeParticles.VOID_STARS);
        } else {
            original.call(level, pos, data, options);
        }
    }

    @WrapOperation(method = "playIdleSounds", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playLocalSound(Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V"))
    private static void getStateWithConnections(Level level, BlockPos pos, SoundEvent sound, SoundSource source, float f, float g, boolean bl, Operation<Void> original) {
        if (level.getBlockState(pos).is(EnderscapeBlocks.END_VAULT)) {
            original.call(level, pos, EnderscapeBlockSounds.END_VAULT_AMBIENT, source, f, g, bl);
        } else {
            original.call(level, pos, sound, source, f, g, bl);
        }
    }
}