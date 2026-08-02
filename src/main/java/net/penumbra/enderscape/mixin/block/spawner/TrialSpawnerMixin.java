package net.penumbra.enderscape.mixin.block.spawner;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TrialSpawner.class)
public abstract class TrialSpawnerMixin {

    @Inject(method = "applyOminous", at = @At("HEAD"), cancellable = true)
    public void Enderscape$applyOminous(ServerLevel level, BlockPos pos, CallbackInfo info) {
        if (level.getBlockState(pos).is(EnderscapeBlocks.END_TRIAL_SPAWNER)) info.cancel();
    }
}