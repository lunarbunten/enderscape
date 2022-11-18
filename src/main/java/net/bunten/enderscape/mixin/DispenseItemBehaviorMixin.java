package net.bunten.enderscape.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.bunten.enderscape.entity.drifter.Drifter;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

/*
 *  Drift Jelly Dispenser behavior
 */
@Mixin(targets = "net/minecraft/core/dispenser/DispenseItemBehavior$24")
public abstract class DispenseItemBehaviorMixin extends OptionalDispenseItemBehavior {

    @Shadow
    protected abstract ItemStack takeLiquid(BlockSource pointer, ItemStack emptyBottleStack, ItemStack filledBottleStack);

    @Unique
    private boolean tryBottleJelly(ServerLevel world, BlockPos pos) {
        List<Drifter> list = world.getEntitiesOfClass(Drifter.class, new AABB(pos), EntitySelector.NO_SPECTATORS);
        for (Drifter mob : list) {
            if (!mob.isDrippingJelly()) continue;
            world.playSound(null, mob, EnderscapeSounds.DRIFTER_MILK, SoundSource.PLAYERS, 0.5F, 1);
            mob.setDrippingJelly(false);
            mob.gameEvent(GameEvent.ENTITY_INTERACT);
            mob.lastJellyTicks = 0;
            return true;
        }

        return false;
    }

    @Inject(at = @At("HEAD"), method = "execute", cancellable = true)
    private void execute(BlockSource pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
        var world = pointer.getLevel();
        var pos = pointer.getPos().relative(pointer.getBlockState().getValue(DispenserBlock.FACING));
        if (!world.isClientSide()) {
            setSuccess(tryBottleJelly(world, pos));
            if (isSuccess()) {
                info.setReturnValue(takeLiquid(pointer, stack, new ItemStack(EnderscapeItems.DRIFT_JELLY_BOTTLE)));
            }
        }
    }
}