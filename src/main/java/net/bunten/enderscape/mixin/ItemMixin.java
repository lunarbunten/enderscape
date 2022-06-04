package net.bunten.enderscape.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(method = "getMaxCount", at = @At("HEAD"), cancellable = true)
    public final void getMaxCount(CallbackInfoReturnable<Integer> info) {
        Item item = (Item) (Object) this;
        if (item.equals(Items.ENDER_PEARL)) {
            info.setReturnValue(64);
        }
    }
}