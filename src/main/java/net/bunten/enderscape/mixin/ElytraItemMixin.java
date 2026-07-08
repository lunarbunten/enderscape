package net.bunten.enderscape.mixin;

import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ElytraItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ElytraItem.class)
public abstract class ElytraItemMixin {

    @Inject(at = @At("RETURN"), method = "getEquipSound", cancellable = true)
    protected void equipSound(CallbackInfoReturnable<Holder<SoundEvent>> info) {
        if (EnderscapeConfig.getInstance().elytraUpdateEquipSound && info.getReturnValue() == SoundEvents.ARMOR_EQUIP_ELYTRA) {
            info.setReturnValue(EnderscapeItemSounds.ELYTRA_EQUIP);
        }
    }
}