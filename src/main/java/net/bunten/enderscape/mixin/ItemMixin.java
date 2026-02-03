package net.bunten.enderscape.mixin;

import net.bunten.enderscape.item.FueledTool;
import net.bunten.enderscape.item.LodestoneTeleporter;
import net.bunten.enderscape.registry.EnderscapeAttributes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "getAttackDamageBonus", at = @At("RETURN"), cancellable = true)
    public void Enderscape$getAttackDamageBonus(Entity entity, float damage, DamageSource source, CallbackInfoReturnable<Float> info) {
        if (source.getDirectEntity() instanceof LivingEntity mob && EnderscapeAttributes.isBackstab(source.getWeaponItem(), mob.position(), entity)) {
            info.setReturnValue((float) (info.getReturnValue() + EnderscapeAttributes.getBackstabDamage(mob)));
        }
    }

    @Inject(method = "isBarVisible", at = @At("HEAD"), cancellable = true)
    public void Enderscape$setFueledFuelBarVisibility(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        if (FueledTool.is(stack)) info.setReturnValue(true);
    }

    @Inject(method = "getBarWidth", at = @At("HEAD"), cancellable = true)
    public void Enderscape$setFueledFuelBarWidth(ItemStack stack, CallbackInfoReturnable<Integer> info) {
        if (FueledTool.is(stack)) {
            Fraction fraction = Fraction.getFraction(FueledTool.currentFuel(stack), FueledTool.maxFuel(stack));
            info.setReturnValue(Mth.clamp(Mth.mulAndTruncate(fraction, 13), 0, 13));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Inject(method = "appendHoverText", at = @At("HEAD"))
    public void Enderscape$getName(ItemStack stack, Item.TooltipContext context, List<Component> list, TooltipFlag flag, CallbackInfo ci) {
        if (LodestoneTeleporter.is(stack)) LodestoneTeleporter.appendHoverText(stack, list);
    }
}