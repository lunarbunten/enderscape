package net.bunten.enderscape.mixin;

import net.bunten.enderscape.registry.EnderscapeBlockSounds;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.world.level.block.entity.vault.VaultState$3")
public abstract class VaultStateUnlockingMixin {

    @Redirect(method = "onEnter", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;)V"))
    private void onEnter(ServerLevel level, Entity entity, BlockPos pos, SoundEvent sound, SoundSource source) {
        if (level.getBlockState(pos).is(EnderscapeBlocks.END_VAULT)) {
            level.playSound(entity, pos, EnderscapeBlockSounds.END_VAULT_INSERT_ITEM, source);
        } else {
            level.playSound(entity, pos, sound, source);
        }
    }
}