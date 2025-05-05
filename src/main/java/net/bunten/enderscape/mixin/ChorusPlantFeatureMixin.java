package net.bunten.enderscape.mixin;

import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ChorusPlantFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChorusPlantFeature.class)
public abstract class ChorusPlantFeatureMixin {

    @Redirect(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 0))
    private boolean getStateWithConnections(BlockState instance, Block block) {
        return instance.is(EnderscapeBlockTags.CHORUS_VEGETATION_PLANTABLE_ON);
    }
}