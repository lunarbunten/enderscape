package net.enderscape.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import net.enderscape.registry.EndItems;
import net.enderscape.registry.EndSounds;
import net.enderscape.util.UUIDs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;

public class DriftBootsItem extends ArmorItem {
    public DriftBootsItem(Settings settings) {
        super(Material.DRIFT, EquipmentSlot.FEET, settings);
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();

        builder.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(UUIDs.FEET, "Armor modifier", 2, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(UUIDs.FEET, "Speed modifier", 0.2, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));

        return slot == EquipmentSlot.FEET ? builder.build() : super.getAttributeModifiers(slot);
    }

    public enum Material implements ArmorMaterial {
        DRIFT;

        public int getDurability(EquipmentSlot slot) {
            return new int[]{13, 15, 16, 11}[slot.getEntitySlotId()] * 25;
        }

        public int getProtectionAmount(EquipmentSlot slot) {
            return new int[]{2, 5, 6, 2}[slot.getEntitySlotId()];
        }

        public int getEnchantability() {
            return 20;
        }

        public SoundEvent getEquipSound() {
            return EndSounds.ITEM_DRIFT_BOOTS_EQUIP;
        }

        public Ingredient getRepairIngredient() {
            return Ingredient.ofItems(EndItems.NEBULITE);
        }

        @Environment(EnvType.CLIENT)
        public String getName() {
            return "drift";
        }

        public float getToughness() {
            return 0;
        }

        public float getKnockbackResistance() {
            return 0;
        }
    }
}