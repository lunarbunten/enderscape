package net.bunten.enderscape.blocks;

import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.world.event.GameEvent;

public class NebuliteDispenserBehavior extends FallibleItemDispenserBehavior {
    
    @Override
    public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        var direction = pointer.getBlockState().get(DispenserBlock.FACING);
        var pos = pointer.getPos().offset(direction);
        var world = pointer.getWorld();
        var state = world.getBlockState(pos);

        setSuccess(true);

        if (state.isOf(EnderscapeBlocks.NEBULITE_CAULDRON)) {
            if (NebuliteCauldronBlock.canLevel(world, pos, state)) {
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
                NebuliteCauldronBlock.addLevel(world, pos, state);
                stack.decrement(1);
            } else {
                setSuccess(false);
            }

            return stack;
        }

        if (state.isOf(Blocks.CAULDRON)) {
            world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
            world.setBlockState(pos, EnderscapeBlocks.NEBULITE_CAULDRON.getDefaultState(), 3);
            world.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, EnderscapeSounds.BLOCK_NEBULITE_CAULDRON_FILL, SoundCategory.BLOCKS, 0.6F, 1);
            stack.decrement(1);

            return stack;
        }

        return super.dispenseSilently(pointer, stack);
    }
}