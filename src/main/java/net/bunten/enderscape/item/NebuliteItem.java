package net.bunten.enderscape.item;

import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;

public class NebuliteItem extends Item {
    public NebuliteItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action == ClickAction.PRIMARY && other.getItem() instanceof NebuliteToolItem) {
            if (NebuliteToolItem.currentFuel(other) < NebuliteToolItem.maxFuel(other)) {
                int added = Math.min(stack.getCount(), NebuliteToolItem.maxFuel(other) - NebuliteToolItem.currentFuel(other));
                NebuliteToolItem.setFuel(other, NebuliteToolItem.currentFuel(other) + added);
                stack.shrink(added);
                player.playSound(EnderscapeItemSounds.NEBULITE_TOOL_ADD_FUEL, 1.0F, 1.0F);
                player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            } else {
                player.playSound(EnderscapeItemSounds.NEBULITE_TOOL_FUEL_FULL, 1.0F, 1.0F);
                player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            }

            return true;
        }

        return false;
    }
}