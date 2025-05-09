package net.bunten.enderscape.item;

import net.bunten.enderscape.registry.EnderscapeItems;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class DriftLeggingsItem extends ArmorItem implements Equipable {

    public DriftLeggingsItem(Properties properties) {
        super(EnderscapeItems.DRIFT_LEGGINGS_MATERIAL, Type.LEGGINGS, properties);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        return ItemAttributeModifiers.EMPTY;
    }
}
