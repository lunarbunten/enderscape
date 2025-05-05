package net.bunten.enderscape.item;

import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.gameevent.GameEvent;
import org.apache.commons.lang3.math.Fraction;

import java.util.Optional;

import static net.bunten.enderscape.registry.EnderscapeDataComponents.*;

public abstract class NebuliteToolItem extends Item {

    public NebuliteToolItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    public abstract boolean displayHudWhen(NebuliteToolContext context);

    public boolean hideInvalidOutlineWhen(NebuliteToolContext context) {
        return fuelExceedsCost(context);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action == ClickAction.PRIMARY && other.is(EnderscapeItems.NEBULITE)) {
            if (currentFuel(stack) < maxFuel(stack)) {
                setFuel(stack, currentFuel(stack) + 1);
                other.shrink(1);
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

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        TooltipDisplay display = stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
        return (!display.shows(CURRENT_NEBULITE_FUEL) || !display.shows(MAXIMUM_NEBULITE_FUEL)) ? Optional.empty() : Optional.of(new NebuliteToolComponent(stack));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Mth.clamp(Mth.mulAndTruncate(computeFuelAmount(stack), 13), 0, 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xFF66FF;
    }

    public static boolean is(ItemStack stack) {
        return stack.getItem() instanceof NebuliteToolItem;
    }

    public static NebuliteToolItem of(ItemStack stack) {
        return of(stack.getItem());
    }

    public static NebuliteToolItem of(Item item) {
        return (NebuliteToolItem) item;
    }

    public static Fraction computeFuelAmount(ItemStack stack) {
        return Fraction.getFraction(currentFuel(stack), maxFuel(stack));
    }

    public static int currentFuel(ItemStack stack) {
        return stack.has(CURRENT_NEBULITE_FUEL) ? Math.max(0, stack.get(CURRENT_NEBULITE_FUEL)) : 0;
    }

    public static void setFuel(ItemStack stack, int value) {
        if (stack.has(MAXIMUM_NEBULITE_FUEL)) stack.set(CURRENT_NEBULITE_FUEL, Mth.clamp(value, 0, stack.get(MAXIMUM_NEBULITE_FUEL)));
    }

    public static int maxFuel(ItemStack stack) {
        if (stack.has(MAXIMUM_NEBULITE_FUEL)) return Math.max(0, stack.get(MAXIMUM_NEBULITE_FUEL));
        throw new IllegalStateException(stack.getItem() + " missing component of " + MAXIMUM_NEBULITE_FUEL);
    }

    public static void setMaxFuel(ItemStack stack, int value) {
        stack.set(MAXIMUM_NEBULITE_FUEL, Math.max(0, value));
    }

    public int fuelCost(NebuliteToolContext context) {
        if (context.stack().has(NEBULITE_FUEL_PER_USE)) return context.stack().get(NEBULITE_FUEL_PER_USE);
        throw new IllegalStateException(context.stack().getItem() + " missing component of " + NEBULITE_FUEL_PER_USE);
    }

    public static void useFuel(NebuliteToolContext context) {
        ItemStack stack = context.stack();
        setFuel(stack, currentFuel(stack) - NebuliteToolItem.of(stack).fuelCost(context));
    }

    public static boolean fuelExceedsCost(NebuliteToolContext context) {
        ItemStack stack = context.stack();
        if (NebuliteToolItem.is(stack)) {
            return currentFuel(stack) >= NebuliteToolItem.of(stack).fuelCost(context);
        } else {
            return false;
        }
    }
}