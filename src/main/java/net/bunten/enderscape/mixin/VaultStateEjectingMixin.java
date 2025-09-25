package net.bunten.enderscape.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.bunten.enderscape.registry.EnderscapeBlockSounds;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.level.block.entity.vault.VaultState$4")
public abstract class VaultStateEjectingMixin {

    @WrapOperation(
            method = "onEnter",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;)V"
            )
    )
    private void onEnter(ServerLevel level, Entity entity, BlockPos pos, SoundEvent sound, SoundSource source, Operation<Void> original) {
        if (level.getBlockState(pos).is(EnderscapeBlocks.END_VAULT)) {
            original.call(level, entity, pos, EnderscapeBlockSounds.END_VAULT_OPEN_SHUTTER, source);
        } else {
            original.call(level, entity, pos, sound, source);
        }
    }

    @WrapOperation(
            method = "onExit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;)V"
            )
    )
    private void onExit(ServerLevel level, Entity entity, BlockPos pos, SoundEvent sound, SoundSource source, Operation<Void> original) {
        if (level.getBlockState(pos).is(EnderscapeBlocks.END_VAULT)) {
            original.call(level, entity, pos, EnderscapeBlockSounds.END_VAULT_CLOSE_SHUTTER, source);
        } else {
            original.call(level, entity, pos, sound, source);
        }
    }
}