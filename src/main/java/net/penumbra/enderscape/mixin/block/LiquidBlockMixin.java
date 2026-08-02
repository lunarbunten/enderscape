package net.penumbra.enderscape.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.penumbra.enderscape.block.fluid.VoidLachrymaFluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LiquidBlock.class)
public abstract class LiquidBlockMixin extends Block {

    public LiquidBlockMixin(Properties properties) {
        super(properties);
    }

    @Shadow
    @Final
    protected FlowingFluid fluid;

    @Inject(method = "shouldSpreadLiquid", at = @At("TAIL"))
    public void Enderscape$animateTick(Level level, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> info) {
        VoidLachrymaFluid.shouldSpreadLiquidTail(level, pos, fluid);
    }
}