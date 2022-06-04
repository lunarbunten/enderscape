package net.bunten.enderscape.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.bunten.enderscape.blocks.EnderscapeSignBlock;
import net.bunten.enderscape.blocks.EnderscapeWallSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;

@Mixin(BlockEntityType.class)
public abstract class BlockEntityTypeMixin {
    
    @Inject(method = "supports", at = @At("HEAD"), cancellable = true)
    private void supports(BlockState state, CallbackInfoReturnable<Boolean> info) {
        if (state.getBlock() instanceof EnderscapeSignBlock || state.getBlock() instanceof EnderscapeWallSignBlock) {
            info.setReturnValue(BlockEntityType.SIGN.equals(this));
        }
    }
}