package net.penumbra.enderscape.mixin.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.penumbra.enderscape.item.component.*;
import net.penumbra.enderscape.item.tooltip.FueledToolComponent;
import net.penumbra.enderscape.registry.entity.EnderscapeAttributes;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "getAttackDamageBonus", at = @At("RETURN"), cancellable = true)
    public void Enderscape$getAttackDamageBonus(Entity entity, float damage, DamageSource source, CallbackInfoReturnable<Float> info) {
        if (source.getDirectEntity() instanceof LivingEntity mob && EnderscapeAttributes.isBackstab(source.getWeaponItem(), mob.position(), entity)) {
            info.setReturnValue((float) (info.getReturnValue() + EnderscapeAttributes.getBackstabDamage(mob)));
        }
    }

    @Inject(method = "hurtEnemy", at = @At("HEAD"))
    public void Enderscape$hurtEnemy(ItemStack stack, LivingEntity victim, LivingEntity attacker, CallbackInfo info) {
        if (EnderscapeAttributes.isBackstab(stack, attacker.position(), victim) && AttackSounds.is(stack) && attacker.level() instanceof ServerLevel level) {
            level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), AttackSounds.get(stack).backstab().value(), attacker.getSoundSource(), 1.0F, 1.0F);
        }
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void Enderscape$use(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> info) {
        if (StunAttack.is(player.getItemInHand(hand)) && level instanceof ServerLevel server) info.setReturnValue(StunAttack.miss(server, player, hand));
        else if (LodestoneTeleportation.is(player.getItemInHand(hand))) info.setReturnValue(LodestoneTeleportation.use(level, player, hand));
        else if (Togglable.is(player.getItemInHand(hand))) info.setReturnValue(Togglable.use(level, player, hand));
    }

    @Inject(method = "interactLivingEntity", at = @At("HEAD"), cancellable = true)
    public void Enderscape$interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand, CallbackInfoReturnable<InteractionResult> info) {
        if (!StunAttack.is(stack)) return;

        if (player.level() instanceof ServerLevel level) {
            if (StunAttack.apply(level, stack, player, target)) info.setReturnValue(InteractionResult.SUCCESS_SERVER);
        } else if (StunAttack.isMeleeStun(stack)) {
            boolean stuns = StunAttack.canUse(player.level(), stack, player) && StunAttack.canStunVictim(player, target);
            info.setReturnValue(stuns ? InteractionResult.SUCCESS : InteractionResult.CONSUME);
        }
    }

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    public void Enderscape$useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> info) {
        if (LodestoneTeleportation.is(context.getItemInHand()) && LodestoneTeleportation.useOn(context)) info.setReturnValue(InteractionResult.SUCCESS_SERVER);
    }

    @Inject(method = "isFoil", at = @At("RETURN"), cancellable = true)
    public void Enderscape$isFoil(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        if (LodestoneTeleportation.is(stack)) info.setReturnValue(LodestoneTeleportation.isFoil(stack) || info.getReturnValue());
    }

    @Inject(method = "getName(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/network/chat/Component;", at = @At("RETURN"), cancellable = true)
    public void Enderscape$getName(ItemStack stack, CallbackInfoReturnable<Component> info) {
        if (LodestoneTeleportation.is(stack)) info.setReturnValue(LodestoneTeleportation.getName(stack, info.getReturnValue()));
    }

    @Inject(method = "inventoryTick", at = @At("HEAD"))
    public void Enderscape$inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot, CallbackInfo info) {
        if (LodestoneTeleportation.is(stack)) LodestoneTeleportation.inventoryTick(stack, level);
    }

    @Inject(method = "overrideOtherStackedOnMe", at = @At("HEAD"), cancellable = true)
    public void Enderscape$overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access, CallbackInfoReturnable<Boolean> info) {
        if (FueledTool.tryFuel(stack, other, action, player)) info.setReturnValue(true);
        else if (Togglable.is(stack) && Togglable.override(stack, other, action, player)) info.setReturnValue(true);
    }

    @Inject(method = "getBarColor", at = @At("HEAD"), cancellable = true)
    public void Enderscape$setNebuliteToolBarColor(ItemStack stack, CallbackInfoReturnable<Integer> info) {
        if (FueledTool.is(stack)) info.setReturnValue(FueledTool.get(stack).display().barColor());
    }

    @Inject(method = "getTooltipImage", at = @At("HEAD"), cancellable = true)
    public void Enderscape$setNebuliteTooltipImage(ItemStack stack, CallbackInfoReturnable<Optional<TooltipComponent>> info) {
        if (FueledTool.is(stack) && FueledToolComponent.applies(stack)) {
            info.setReturnValue(Optional.of(new FueledToolComponent(stack)));
        }
    }

    @Inject(method = "isBarVisible", at = @At("HEAD"), cancellable = true)
    public void Enderscape$setNebuliteFuelBarVisibility(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        if (FueledTool.is(stack)) info.setReturnValue(true);
    }

    @Inject(method = "getBarWidth", at = @At("HEAD"), cancellable = true)
    public void Enderscape$setNebuliteFuelBarWidth(ItemStack stack, CallbackInfoReturnable<Integer> info) {
        if (FueledTool.is(stack)) {
            Fraction fraction = Fraction.getFraction(FueledTool.currentFuel(stack), FueledTool.maxFuel(stack));
            info.setReturnValue(Mth.clamp(Mth.mulAndTruncate(fraction, 13), 0, 13));
        }
    }
}