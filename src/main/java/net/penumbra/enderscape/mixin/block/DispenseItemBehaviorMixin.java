package net.penumbra.enderscape.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.penumbra.enderscape.entity.ai.behavior.drifter.DrifterStartOrStopLeakingJelly;
import net.penumbra.enderscape.entity.drifter.Drifter;
import net.penumbra.enderscape.registry.item.EnderscapeItems;
import net.penumbra.enderscape.registry.sound.EnderscapeEntitySounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(targets = "net/minecraft/core/dispenser/DispenseItemBehavior$9")
public abstract class DispenseItemBehaviorMixin extends OptionalDispenseItemBehavior {

    @Shadow
    protected abstract ItemStack takeLiquid(BlockSource source, ItemStack empty, ItemStack filled);

    @Unique
    private boolean tryExtractDriftJelly(Level level, BlockPos pos) {
        List<Drifter> list = level.getEntitiesOfClass(Drifter.class, new AABB(pos), EntitySelector.NO_SPECTATORS);

        for (Drifter mob : list) {
            if (!mob.isDrippingJelly()) continue;

            mob.gameEvent(GameEvent.ENTITY_INTERACT);
            mob.playSound(EnderscapeEntitySounds.DRIFTER_MILK, 0.5F, 1);
            mob.setDrippingJelly(false);

            DrifterStartOrStopLeakingJelly.refreshCooldown(mob);
            return true;
        }

        return false;
    }

    @Inject(at = @At("HEAD"), method = "execute", cancellable = true)
    private void execute(BlockSource source, ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
        Level level = source.level();
        BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));

        if (!level.isClientSide()) {
            setSuccess(tryExtractDriftJelly(level, pos));
            if (isSuccess()) info.setReturnValue(takeLiquid(source, stack, new ItemStack(EnderscapeItems.DRIFT_JELLY_BOTTLE)));
        }
    }
}