package net.bunten.enderscape.blocks;

import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;

public class NebuliteDispenserBehavior extends OptionalDispenseItemBehavior {
    
    @Override
    public ItemStack execute(BlockSource pointer, ItemStack stack) {
        var direction = pointer.getBlockState().getValue(DispenserBlock.FACING);
        var pos = pointer.getPos().relative(direction);
        var world = pointer.getLevel();
        var state = world.getBlockState(pos);

        setSuccess(true);

        if (state.is(EnderscapeBlocks.NEBULITE_CAULDRON)) {
            if (NebuliteCauldronBlock.canLevel(world, pos, state)) {
                world.gameEvent(null, GameEvent.FLUID_PLACE, pos);
                NebuliteCauldronBlock.addLevel(world, pos, state);
                stack.shrink(1);
            } else {
                setSuccess(false);
            }

            return stack;
        }

        if (state.is(Blocks.CAULDRON)) {
            world.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            world.setBlock(pos, EnderscapeBlocks.NEBULITE_CAULDRON.defaultBlockState(), 3);
            world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, EnderscapeSounds.NEBULITE_CAULDRON_FILL, SoundSource.BLOCKS, 0.6F, 1);
            stack.shrink(1);

            return stack;
        }

        return super.execute(pointer, stack);
    }
}