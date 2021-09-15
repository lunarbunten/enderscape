package net.enderscape.mixin;

import net.enderscape.blocks.EndSignBlock;
import net.enderscape.blocks.EndWallSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityType.class)
public abstract class BlockEntityTypeMixin {
    @Inject(method = "supports", at = @At("HEAD"), cancellable = true)
    private void supports(BlockState state, CallbackInfoReturnable<Boolean> info) {
        if (state.getBlock() instanceof EndSignBlock || state.getBlock() instanceof EndWallSignBlock) {
            info.setReturnValue(BlockEntityType.SIGN.equals(this));
        }
    }
}