package net.bunten.enderscape.blocks;

import java.util.List;

import net.bunten.enderscape.entity.drifter.DrifterEntity;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.event.GameEvent;

public class DriftJellyDispenserBehavior extends FallibleItemDispenserBehavior {
    private final ItemDispenserBehavior fallbackBehavior = new ItemDispenserBehavior();

    private static boolean tryBottleJelly(ServerWorld world, BlockPos pos) {
        List<DrifterEntity> list = world.getEntitiesByClass(DrifterEntity.class, new Box(pos), EntityPredicates.EXCEPT_SPECTATOR);
        for (DrifterEntity mob : list) {

            world.playSoundFromEntity(null, mob, EnderscapeSounds.ENTITY_DRIFTER_MILK, SoundCategory.PLAYERS, 0.5F, 1);
            mob.setDrippingJelly(false);
            mob.emitGameEvent(GameEvent.SHEAR, null);
            
            return true;
        }

        return false;
    }

    private ItemStack tryPutFilledBottle(BlockPointer pointer, ItemStack emptyStack, ItemStack jellyStack) {
        emptyStack.decrement(1);

        if (emptyStack.isEmpty()) {
            pointer.getWorld().emitGameEvent(null, GameEvent.FLUID_PICKUP, pointer.getPos());
            return jellyStack.copy();
        }

        if (((DispenserBlockEntity) pointer.getBlockEntity()).addToFirstFreeSlot(jellyStack.copy()) < 0) {
            fallbackBehavior.dispense(pointer, jellyStack.copy());
        }

        return emptyStack;
    }

    @Override
    public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        var world = pointer.getWorld();
        
        if (!world.isClient()) {
            var blockPos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
            setSuccess(tryBottleJelly(world, blockPos));
            if (isSuccess()) {
                return tryPutFilledBottle(pointer, stack, new ItemStack(EnderscapeItems.DRIFT_JELLY_BOTTLE));
            }
        }

        return stack;
    }
}