package net.bunten.enderscape.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;

import net.bunten.enderscape.util.UUIDs;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class HealingItem extends Item {
    public HealingItem(Item.Settings settings) {
        super(settings.maxCount(1));
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(UUIDs.ATTACK_DAMAGE, "Attack Damage", 999, EntityAttributeModifier.Operation.ADDITION));
        return slot == EquipmentSlot.MAINHAND ? builder.build() : super.getAttributeModifiers(slot);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        boolean cooling = user.getItemCooldownManager().isCoolingDown(this);
        ItemStack stack = user.getStackInHand(hand);
        if (!cooling) {
            if (user.isSneaking()) {
            } else {
                user.heal(5000);
                user.getHungerManager().add(5000, 5000);
                user.getItemCooldownManager().set(this, 5);
            }
            return TypedActionResult.success(stack);
        } else {
            return TypedActionResult.fail(stack);
        }
    }
}