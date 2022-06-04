package net.bunten.enderscape.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;

import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.util.UUIDs;
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

public class DriftLeggingsItem extends ArmorItem {
    public DriftLeggingsItem(Settings settings) {
        super(Material.DRIFT, EquipmentSlot.LEGS, settings);
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();

        builder.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(UUIDs.LEGS, "Armor modifier", 4, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, new EntityAttributeModifier(UUIDs.LEGS, "Armor toughness", 2, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(UUIDs.LEGS, "Speed modifier", 0.2, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));

        return slot == EquipmentSlot.LEGS ? builder.build() : super.getAttributeModifiers(slot);
    }

    public enum Material implements ArmorMaterial {
        DRIFT;

        public int getDurability(EquipmentSlot slot) {
            return 495;
        }

        public int getProtectionAmount(EquipmentSlot slot) {
            return 0;
        }

        public int getEnchantability() {
            return 20;
        }

        public SoundEvent getEquipSound() {
            return EnderscapeSounds.ITEM_DRIFT_LEGGINGS_EQUIP;
        }

        public Ingredient getRepairIngredient() {
            return Ingredient.ofItems(EnderscapeItems.NEBULITE);
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