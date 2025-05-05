package net.bunten.enderscape.mixin;

import net.bunten.enderscape.registry.tag.EnderscapeBlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChorusFlowerBlock.class)
public abstract class ChorusFlowerBlockMixin extends BlockBehaviour {
    public ChorusFlowerBlockMixin(Properties settings) {
        super(settings);
    }

    @Redirect(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 0))
    private boolean randomTick1(BlockState instance, Block block) {
        return instance.is(EnderscapeBlockTags.CHORUS_VEGETATION_PLANTABLE_ON);
    }

    @Redirect(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 3))
    private boolean randomTick2(BlockState instance, Block block) {
        return instance.is(EnderscapeBlockTags.CHORUS_VEGETATION_PLANTABLE_ON);
    }

    @Redirect(method = "canSurvive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 1))
    private boolean canSurvive1(BlockState instance, Block block) {
        return instance.is(EnderscapeBlockTags.CHORUS_VEGETATION_PLANTABLE_ON);
    }
}