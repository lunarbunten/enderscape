package net.bunten.enderscape.items;

import org.betterx.bclib.items.BaseArmorItem;

import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;

public class DriftLeggingsItem extends BaseArmorItem {
    public DriftLeggingsItem(Settings settings) {
        super(Material.DRIFT, EquipmentSlot.LEGS, settings);

        var uuid = ARMOR_MODIFIER_UUID_PER_SLOT[slot.getEntitySlotId()];
        var modifier = new EntityAttributeModifier(uuid, "Armor speed modifier", 0.2, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, modifier);
    }

    public enum Material implements ArmorMaterial {
        DRIFT;

        public int getDurability(EquipmentSlot slot) {
            return 495;
        }

        public int getProtectionAmount(EquipmentSlot slot) {
            return 4;
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
            return 2;
        }

        public float getKnockbackResistance() {
            return 0;
        }
    }
}