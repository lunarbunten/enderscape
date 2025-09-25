package net.bunten.enderscape.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ChorusPlantFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChorusPlantFeature.class)
public abstract class ChorusPlantFeatureMixin {

    @Unique
    private static boolean Enderscape$placeable(BlockState instance, Block block, Operation<Boolean> original) {
        return instance.is(EnderscapeBlockTags.CHORUS_VEGETATION_PLANTABLE_ON) || original.call(instance, block);
    }

    @WrapOperation(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 0))
    private boolean getStateWithConnections(BlockState instance, Block block, Operation<Boolean> original) {
        return Enderscape$placeable(instance, block, original);
    }
}