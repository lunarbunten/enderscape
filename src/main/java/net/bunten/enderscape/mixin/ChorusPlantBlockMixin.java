package net.bunten.enderscape.mixin;

import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChorusPlantBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChorusPlantBlock.class)
public abstract class ChorusPlantBlockMixin extends BlockBehaviour {
    public ChorusPlantBlockMixin(Properties settings) {
        super(settings);
    }

    @Redirect(method = "getStateWithConnections", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 2))
    private static boolean getStateWithConnections(BlockState instance, Block block) {
        return instance.is(EnderscapeBlockTags.CHORUS_VEGETATION_PLANTABLE_ON);
    }

    @Redirect(method = "updateShape", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 2))
    private boolean updateShape(BlockState instance, Block block) {
        return instance.is(EnderscapeBlockTags.CHORUS_VEGETATION_PLANTABLE_ON);
    }

    @Redirect(method = "canSurvive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 2))
    private boolean canSurvive1(BlockState instance, Block block) {
        return instance.is(EnderscapeBlockTags.CHORUS_VEGETATION_PLANTABLE_ON);
    }

    @Redirect(method = "canSurvive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 4))
    private boolean canSurvive2(BlockState instance, Block block) {
        return instance.is(EnderscapeBlockTags.CHORUS_VEGETATION_PLANTABLE_ON);
    }
}