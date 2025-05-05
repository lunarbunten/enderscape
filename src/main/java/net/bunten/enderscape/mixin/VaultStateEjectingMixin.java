package net.bunten.enderscape.mixin;

import net.bunten.enderscape.registry.EnderscapeBlockSounds;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.world.level.block.entity.vault.VaultState$4")
public abstract class VaultStateEjectingMixin {

    @Redirect(
            method = "onEnter",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;)V"
            )
    )
    private void onEnter(ServerLevel level, Player player, BlockPos pos, SoundEvent sound, SoundSource source) {
        if (level.getBlockState(pos).is(EnderscapeBlocks.END_VAULT)) {
            level.playSound(player, pos, EnderscapeBlockSounds.END_VAULT_OPEN_SHUTTER, source);
        } else {
            level.playSound(player, pos, sound, source);
        }
    }

    @Redirect(
            method = "onExit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;)V"
            )
    )
    private void onExit(ServerLevel level, Player player, BlockPos pos, SoundEvent sound, SoundSource source) {
        if (level.getBlockState(pos).is(EnderscapeBlocks.END_VAULT)) {
            level.playSound(player, pos, EnderscapeBlockSounds.END_VAULT_CLOSE_SHUTTER, source);
        } else {
            level.playSound(player, pos, sound, source);
        }
    }
}