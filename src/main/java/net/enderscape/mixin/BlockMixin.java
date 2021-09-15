package net.enderscape.mixin;

import net.enderscape.registry.EndBlocks;
import net.enderscape.registry.EndSounds;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.BlockTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin extends AbstractBlock {
    public BlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(at = @At("HEAD"), method = "getSoundGroup", cancellable = true)
    public void getSoundGroup(BlockState state, CallbackInfoReturnable<BlockSoundGroup> info) {
        if (state.isIn(EndBlocks.PURPUR_BLOCKS)) {
            info.setReturnValue(EndSounds.PURPUR);
        } else if (state.isIn(EndBlocks.CHORUS_BLOCKS)) {
            info.setReturnValue(EndSounds.CHORUS);
        } else if (state.isIn(EndBlocks.END_STONE_BLOCKS)) {
            info.setReturnValue(EndSounds.END_STONE);
        } else if (state.isIn(EndBlocks.END_STONE_BRICK_BLOCKS)) {
            info.setReturnValue(EndSounds.END_STONE_BRICKS);
        } else if (state.isIn(BlockTags.SHULKER_BOXES)) {
            info.setReturnValue(EndSounds.SHULKER);
        }
    }
}