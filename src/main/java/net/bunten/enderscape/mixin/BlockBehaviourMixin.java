package net.bunten.enderscape.mixin;

import net.bunten.enderscape.registry.EnderscapeSoundTypeOverrides;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {

    @Inject(at = @At("HEAD"), method = "getSoundType", cancellable = true)
    public void getSoundType(BlockState state, CallbackInfoReturnable<SoundType> info) {
        EnderscapeSoundTypeOverrides.getSoundType(state).ifPresent(info::setReturnValue);
    }
}