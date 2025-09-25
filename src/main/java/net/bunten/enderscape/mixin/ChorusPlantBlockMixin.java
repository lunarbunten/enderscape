package net.bunten.enderscape.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChorusPlantBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChorusPlantBlock.class)
public abstract class ChorusPlantBlockMixin extends BlockBehaviour {
    public ChorusPlantBlockMixin(Properties settings) {
        super(settings);
    }

    @Unique
    private static boolean Enderscape$placeable(BlockState instance, Block block, Operation<Boolean> original) {
        return instance.is(EnderscapeBlockTags.CHORUS_VEGETATION_PLANTABLE_ON) || original.call(instance, block);
    }

    @WrapOperation(method = "getStateWithConnections", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 2))
    private static boolean Enderscape$getStateWithConnections(BlockState instance, Block block, Operation<Boolean> original) {
        return Enderscape$placeable(instance, block, original);
    }

    @WrapOperation(method = "updateShape", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 2))
    private boolean Enderscape$updateShape(BlockState instance, Block block, Operation<Boolean> original) {
        return Enderscape$placeable(instance, block, original);
    }

    @WrapOperation(method = "canSurvive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 2))
    private boolean Enderscape$canSurvive1(BlockState instance, Block block, Operation<Boolean> original) {
        return Enderscape$placeable(instance, block, original);
    }

    @WrapOperation(method = "canSurvive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 4))
    private boolean Enderscape$canSurvive2(BlockState instance, Block block, Operation<Boolean> original) {
        return Enderscape$placeable(instance, block, original);
    }
}