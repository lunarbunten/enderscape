package net.penumbra.enderscape.mixin.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.penumbra.enderscape.block.VoidCampfireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin extends Block {

    public CampfireBlockMixin(Properties properties) {
        super(properties);
    }

    @Unique
    private static boolean isVoidCampfire(BlockState state) {
        return state.getBlock() instanceof VoidCampfireBlock;
    }

    @WrapOperation(method = "dowse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/CampfireBlock;makeParticles(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;ZZ)V"))
    private static void Enderscape$stopDowseParticles(Level level, BlockPos pos, boolean isSignalFire, boolean smoking, Operation<Void> original) {
        if (!isVoidCampfire(level.getBlockState(pos))) original.call(level, pos, isSignalFire, smoking);
    }

    @Inject(method = "isSmokeyPos", at = @At(value = "HEAD"), cancellable = true)
    private static void Enderscape$voidCampfireIsANo(Level level, BlockPos pos, CallbackInfoReturnable<Boolean> info) {
        if (isVoidCampfire(level.getBlockState(pos))) {
            info.setReturnValue(false);
        }
    }
}