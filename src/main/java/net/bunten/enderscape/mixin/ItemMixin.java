package net.bunten.enderscape.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.bunten.enderscape.config.Config;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

/*
 *  Adjusts Ender Pearl Stack Size
 */
@Mixin(Item.class)
public abstract class ItemMixin {

    @Unique
    private final Item item = (Item) (Object) this;

    @Inject(method = "getMaxStackSize", at = @At("HEAD"), cancellable = true)
    public final void getMaxStackSize(CallbackInfoReturnable<Integer> info) {
        if (item.equals(Items.ENDER_PEARL) && Config.QOL.shouldEnderPearlsStackTo64()) {
            info.setReturnValue(64);
        }
    }
}