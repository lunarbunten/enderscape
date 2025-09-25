package net.bunten.enderscape.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChorusFlowerBlock.class)
public abstract class ChorusFlowerBlockMixin extends BlockBehaviour {
    public ChorusFlowerBlockMixin(Properties settings) {
        super(settings);
    }

    @Unique
    private static boolean Enderscape$placeable(BlockState instance, Block block, Operation<Boolean> original) {
        return instance.is(EnderscapeBlockTags.CHORUS_VEGETATION_PLANTABLE_ON) || original.call(instance, block);
    }

    @WrapOperation(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 0))
    private boolean Enderscape$randomTick1(BlockState instance, Block block, Operation<Boolean> original) {
        return Enderscape$placeable(instance, block, original);
    }

    @WrapOperation(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 3))
    private boolean Enderscape$randomTick2(BlockState instance, Block block, Operation<Boolean> original) {
        return Enderscape$placeable(instance, block, original);
    }

    @WrapOperation(method = "canSurvive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 1))
    private boolean Enderscape$canSurvive(BlockState instance, Block block, Operation<Boolean> original) {
        return Enderscape$placeable(instance, block, original);
    }
}