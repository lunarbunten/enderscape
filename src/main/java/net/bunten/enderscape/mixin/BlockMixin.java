package net.bunten.enderscape.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.bunten.enderscape.blocks.SoundTypeModification;
import net.bunten.enderscape.registry.EnderscapeModifications;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/*
 *  Block sound modifications
 */
@Mixin(Block.class)
public abstract class BlockMixin extends BlockBehaviour {
    public BlockMixin(Properties settings) {
        super(settings);
    }

    @Inject(at = @At("HEAD"), method = "getSoundType", cancellable = true)
    public void getSoundType(BlockState state, CallbackInfoReturnable<SoundType> info) {
        ResourceLocation key = Registry.BLOCK.getKey((Block) (Object) this);
        for (SoundTypeModification modification : EnderscapeModifications.MODIFICATIONS) {
            if (modification.getBlocks().contains(key.getPath()) && modification.applies()) info.setReturnValue(modification.getSoundType());
        }
    }
}