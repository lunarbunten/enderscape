package net.bunten.enderscape.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.bunten.enderscape.entity.drifter.DrifterEntity;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.event.GameEvent;

@Mixin(targets = "net/minecraft/block/dispenser/DispenserBehavior$17")
public abstract class DispenserBehaviorMixin extends FallibleItemDispenserBehavior {

    @Shadow @Final
    private ItemDispenserBehavior fallbackBehavior;

    @Shadow
    private ItemStack tryPutFilledBottle(BlockPointer pointer, ItemStack emptyBottleStack, ItemStack filledBottleStack) {
        return null;
    }

    private boolean tryBottleJelly(ServerWorld world, BlockPos pos) {
        List<DrifterEntity> list = world.getEntitiesByClass(DrifterEntity.class, new Box(pos), EntityPredicates.EXCEPT_SPECTATOR);
        for (DrifterEntity mob : list) {

            world.playSoundFromEntity(null, mob, EnderscapeSounds.ENTITY_DRIFTER_MILK, SoundCategory.PLAYERS, 0.5F, 1);
            mob.setDrippingJelly(false);
            mob.emitGameEvent(GameEvent.SHEAR, null);
            
            return true;
        }

        return false;
    }

    @Inject(at = @At("HEAD"), method = "dispenseSilently", cancellable = true)
    private void dispenseSilently(BlockPointer pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
        var world = pointer.getWorld();
        if (!world.isClient()) {
            var blockPos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
            setSuccess(tryBottleJelly(world, blockPos));
            if (isSuccess()) {
                info.setReturnValue(tryPutFilledBottle(pointer, stack, new ItemStack(EnderscapeItems.DRIFT_JELLY_BOTTLE)));
            }
        }
    }
}