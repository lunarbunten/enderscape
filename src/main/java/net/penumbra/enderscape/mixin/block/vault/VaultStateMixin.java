package net.penumbra.enderscape.mixin.block.vault;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.vault.VaultState;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import net.penumbra.enderscape.registry.sound.EnderscapeBlockSounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VaultState.class)
public abstract class VaultStateMixin {

    @WrapOperation(method = "ejectResultItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"))
    private static void ejectResultItem(ServerLevel level, Entity entity, BlockPos pos, SoundEvent sound, SoundSource source, float a, float b, Operation<Void> original) {
        if (level.getBlockState(pos).is(EnderscapeBlocks.END_VAULT)) {
            original.call(level, entity, pos, EnderscapeBlockSounds.END_VAULT_EJECT_ITEM, source, a, b);
        } else {
            original.call(level, entity, pos, sound, source, a, b);
        }
    }
}