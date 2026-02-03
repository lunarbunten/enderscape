package net.bunten.enderscape.mixin;

import net.bunten.enderscape.item.FueledTool;
import net.bunten.enderscape.item.ItemStackContext;
import net.bunten.enderscape.registry.EnderscapeAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    private final ItemStack stack = (ItemStack) (Object) this;

    @Unique
    private double Enderscape$cachedBaseAttackDamage;

    @Inject(method = "addModifierTooltip", at = @At("HEAD"))
    public void Enderscape$cacheAttackDamage(Consumer<Component> consumer, @Nullable Player player, Holder<Attribute> holder, AttributeModifier modifier, CallbackInfo ci) {
        if (holder.is(Attributes.ATTACK_DAMAGE) && modifier.is(Item.BASE_ATTACK_DAMAGE_ID)) Enderscape$cachedBaseAttackDamage = modifier.amount() + (player != null ? player.getAttributeBaseValue(holder) : 0);
    }

    @Inject(method = "addModifierTooltip", at = @At("HEAD"), cancellable = true)
    public void Enderscape$apply(Consumer<Component> consumer, @Nullable Player player, Holder<Attribute> holder, AttributeModifier modifier, CallbackInfo ci) {
        if (holder.is(EnderscapeAttributes.BACKSTAB_DAMAGE) && modifier.is(EnderscapeAttributes.BASE_BACKSTAB_DAMAGE_ID)) {
            double base = Enderscape$cachedBaseAttackDamage > 0 ? Enderscape$cachedBaseAttackDamage : 0;
            double total = modifier.amount() + (player != null ? player.getAttributeBaseValue(holder) : 0) + base;

            consumer.accept(
                    CommonComponents.space()
                            .append(
                                    Component.translatable(
                                            "attribute.modifier.equals." + modifier.operation().id(),
                                            ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(total),
                                            Component.translatable(holder.value().getDescriptionId())
                                    )
                            )
                            .withStyle(ChatFormatting.DARK_GREEN)
            );

            ci.cancel();
        }
    }
}