package net.penumbra.enderscape.item;

import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.manager.VoidManager;
import net.penumbra.enderscape.mixin.item.ItemCooldownsAccessor;
import net.penumbra.enderscape.registry.sound.EnderscapeUiSounds;

import java.util.ArrayList;
import java.util.List;

public class HealingItem extends Item {
    public HealingItem(Item.Properties settings) {
        super(settings.stacksTo(1).rarity(Rarity.EPIC).attributes(ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Enderscape.id("healing_attack_damage"), 999, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build()));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand type) {
        applyEffects(target);
        return InteractionResult.SUCCESS_SERVER;
    }

    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!player.getCooldowns().isOnCooldown(stack)) {
            applyEffects(player);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private static void applyEffects(LivingEntity entity) {
        if (VoidManager.hasVoidedHealth(entity)) {
            VoidManager.setVoidedHealth(entity, 0.0F);
            entity.playSound(EnderscapeUiSounds.HEALTH_VOID_PURIFY, 1.0F, 1.0F);
        }

        if (entity instanceof Player player) {
            player.getFoodData().eat(5000, 5000);
            ((ItemCooldownsAccessor) player.getCooldowns()).getCooldowns().clear();
        }

        entity.heal(5000);
        entity.extinguishFire();
        entity.clearFreeze();

        VoidManager.setVoidTicks(entity, 0);
        VoidManager.setVoidTickDownDelay(entity, 0);

        removeNegativeEffects(entity);
    }

    private static void removeNegativeEffects(LivingEntity entity) {
        List<Holder<MobEffect>> toRemove = new ArrayList<>();

        entity.getActiveEffects().forEach(instance -> {
            if (instance.getEffect().value().getCategory().equals(MobEffectCategory.HARMFUL)) {
                toRemove.add(instance.getEffect());
            }
        });

        toRemove.forEach(entity::removeEffect);
    }
}