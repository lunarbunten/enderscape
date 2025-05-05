package net.bunten.enderscape.mixin;

import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.TrialSpawnerBlock;
import net.minecraft.world.level.block.VaultBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityType.class)
public abstract class BlockEntityTypeMixin {

    @Inject(method = "isValid", at = @At("HEAD"), cancellable = true)
    private void isValid(BlockState state, CallbackInfoReturnable<Boolean> info) {
        if (state.getBlock() instanceof SignBlock sign && (sign.type().equals(EnderscapeBlocks.VEILED_WOOD_TYPE) || sign.type().equals(EnderscapeBlocks.CELESTIAL_WOOD_TYPE) || sign.type().equals(EnderscapeBlocks.MURUBLIGHT_WOOD_TYPE))) {
            info.setReturnValue(true);
        }

        if (state.getBlock() instanceof VaultBlock && state.is(EnderscapeBlocks.END_VAULT)) info.setReturnValue(true);
        if (state.getBlock() instanceof TrialSpawnerBlock && state.is(EnderscapeBlocks.END_TRIAL_SPAWNER)) info.setReturnValue(true);
    }
}