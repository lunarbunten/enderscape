package net.bunten.enderscape.item;

import net.bunten.enderscape.Enderscape;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;

public class HealingItem extends Item {
    public HealingItem(Item.Properties settings) {
        super(settings.stacksTo(1).rarity(Rarity.EPIC).attributes(ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Enderscape.id("healing_attack_damage"), 999, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build()));
    }

    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!player.getCooldowns().isOnCooldown(stack)) {
            player.heal(5000);
            player.getFoodData().eat(5000, 5000);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}