package net.penumbra.enderscape.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.penumbra.enderscape.block.VoidCampfireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin {

    @Unique
    private static boolean isVoidCampfire(BlockState state) {
        return state.getBlock() instanceof VoidCampfireBlock;
    }

    @Inject(method = "particleTick", at = @At(value = "HEAD"), cancellable = true)
    private static void Enderscape$stopParticleTick(Level level, BlockPos pos, BlockState state, CampfireBlockEntity entity, CallbackInfo info) {
        if (isVoidCampfire(level.getBlockState(pos))) {
            info.cancel();
        }
    }
}