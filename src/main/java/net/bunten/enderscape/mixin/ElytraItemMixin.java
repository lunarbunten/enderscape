package net.bunten.enderscape.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ElytraItem.class)
public abstract class ElytraItemMixin {

    @Inject(at = @At("RETURN"), method = "getEquipSound", cancellable = true)
    protected void equipSound(CallbackInfoReturnable<Holder<SoundEvent>> info) {
        if (EnderscapeConfig.getInstance().elytraUpdateEquipSound && info.getReturnValue() == SoundEvents.ARMOR_EQUIP_ELYTRA) {
            info.setReturnValue(EnderscapeItemSounds.ELYTRA_EQUIP);
        }
    }

    @Inject(
            method = "elytraFlightTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void Enderscape$elytraFlightTick(ItemStack itemStack, LivingEntity entity, int flightTicks, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (stack.getDamageValue() == stack.getMaxDamage() - 1 && stack.getItem() == Items.ELYTRA) {
            entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), EnderscapeItemSounds.ELYTRA_BREAK.get(), entity.getSoundSource(), 1.0F, 1.0F);
        }
    }
}