package net.bunten.enderscape.mixin;

import net.bunten.enderscape.registry.tag.EnderscapeItemTags;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    @Unique
    private final Enchantment enchantment = (Enchantment) (Object) this;

    @Inject(method = "canEnchant", at = @At("RETURN"), cancellable = true)
    public void Enderscape$canEnchant(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        if (Enderscape$incompatibleWithSweepingEdge(stack)) info.setReturnValue(false);
    }

    @Inject(method = "isSupportedItem", at = @At("RETURN"), cancellable = true)
    public void Enderscape$isSupportedItem(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        if (Enderscape$incompatibleWithSweepingEdge(stack)) info.setReturnValue(false);
    }

    @Unique
    private boolean Enderscape$incompatibleWithSweepingEdge(ItemStack stack) {
        return stack.is(EnderscapeItemTags.SWEEPING_EDGE_INCOMPATIBLE) && enchantment.description().contains(Component.translatable("enchantment.minecraft.sweeping_edge"));
    }
}