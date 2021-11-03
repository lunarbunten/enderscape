package net.enderscape.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.enderscape.registry.EndBlocks;
import net.enderscape.registry.EndSounds;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.sound.BlockSoundGroup;

@Mixin(Block.class)
public abstract class BlockMixin extends AbstractBlock {
    public BlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(at = @At("HEAD"), method = "getSoundGroup", cancellable = true)
    public void getSoundGroup(BlockState state, CallbackInfoReturnable<BlockSoundGroup> info) {
        // this sucks and should be changed Thanks
        if (state.isIn(EndBlocks.PURPUR_SOUND_BLOCKS)) {
            info.setReturnValue(EndSounds.PURPUR);
        } else if (state.isIn(EndBlocks.CHORUS_SOUND_BLOCKS)) {
            info.setReturnValue(EndSounds.CHORUS);
        } else if (state.isIn(EndBlocks.END_STONE_SOUND_BLOCKS)) {
            info.setReturnValue(EndSounds.END_STONE);
        } else if (state.isIn(EndBlocks.END_STONE_BRICK_SOUND_BLOCKS)) {
            info.setReturnValue(EndSounds.END_STONE_BRICKS);
        } else if (state.isIn(EndBlocks.SHULKER_SOUND_BLOCKS)) {
            info.setReturnValue(EndSounds.SHULKER);
        }
    }
}