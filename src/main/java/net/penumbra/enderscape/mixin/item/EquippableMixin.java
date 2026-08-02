package net.penumbra.enderscape.mixin.item;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.Equippable;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.registry.sound.EnderscapeItemSounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Equippable.class)
public abstract class EquippableMixin {

    @Inject(at = @At("RETURN"), method = "equipSound", cancellable = true)
    protected void equipSound(CallbackInfoReturnable<Holder<SoundEvent>> info) {
        if (EnderscapeConfig.getInstance().elytraUpdateEquipSound && info.getReturnValue() == SoundEvents.ARMOR_EQUIP_ELYTRA) {
            info.setReturnValue(EnderscapeItemSounds.ELYTRA_EQUIP);
        }
    }
}