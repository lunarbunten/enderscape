package net.penumbra.enderscape.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.penumbra.enderscape.block.VoidFireBlock;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseFireBlock.class)
public abstract class FireBlockMixin extends Block {

    public FireBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "getState", at = @At("HEAD"), cancellable = true)
    private static void Enderscape$animateTick(BlockGetter level, BlockPos pos, CallbackInfoReturnable<BlockState> info) {
        BlockState belowState = level.getBlockState(pos.below());

        if (VoidFireBlock.canSurviveOnBlock(belowState)) {
            info.setReturnValue(EnderscapeBlocks.VOID_FIRE.defaultBlockState());
        }
    }
}