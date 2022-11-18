package net.bunten.enderscape.items;

import org.betterx.bclib.items.BaseArmorItem;

import net.bunten.enderscape.registry.EnderscapeItems;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

public class DriftLeggingsItem extends BaseArmorItem {
    public DriftLeggingsItem(Properties settings) {
        super(Material.DRIFT, EquipmentSlot.LEGS, settings);

        var uuid = ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()];
        var modifier = new AttributeModifier(uuid, "Armor speed modifier", 0.2, AttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, modifier);
    }

    public enum Material implements ArmorMaterial {
        DRIFT;

        public int getDurabilityForSlot(EquipmentSlot slot) {
            return 495;
        }

        public int getDefenseForSlot(EquipmentSlot slot) {
            return 4;
        }

        public int getEnchantmentValue() {
            return 20;
        }

        public SoundEvent getEquipSound() {
            return EnderscapeSounds.DRIFT_LEGGINGS_EQUIP;
        }

        public Ingredient getRepairIngredient() {
            return Ingredient.of(EnderscapeItems.NEBULITE);
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