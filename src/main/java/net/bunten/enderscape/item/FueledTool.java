package net.bunten.enderscape.item;

import net.bunten.enderscape.item.component.Enabled;
import net.bunten.enderscape.item.component.ThresholdCounter;
import net.bunten.enderscape.item.component.value.LodestoneTeleportationCost;
import net.bunten.enderscape.item.tooltip.FueledToolComponent;
import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.bunten.enderscape.registry.tag.EnderscapeItemTags;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import org.apache.commons.lang3.math.Fraction;

import java.util.Optional;

import static net.bunten.enderscape.item.LodestoneTeleporter.distanceBetweenPoints;
import static net.bunten.enderscape.item.LodestoneTeleporter.getTotalDistanceForCostIncrease;
import static net.bunten.enderscape.registry.EnderscapeDataComponents.*;

public abstract class FueledTool extends Item {

    public FueledTool(Properties properties) {
        super(properties);
    }

    public static boolean is(ItemStack stack) {
        return stack.getItem() instanceof FueledTool;
    }

    public abstract boolean displayHudWhen(ItemStackContext context);

    public boolean hideInvalidOutlineWhen(ItemStackContext context) {
        return fuelExceedsCost(context);
    }

    public static int currentFuel(ItemStack stack) {
        return stack.has(CURRENT_FUEL) ? Math.max(0, stack.get(CURRENT_FUEL)) : 0;
    }

    public static void setFuel(ItemStack stack, int value) {
        if (FueledTool.is(stack)) {
            stack.set(CURRENT_FUEL, Mth.clamp(value, 0, stack.get(MAXIMUM_FUEL)));
        }
    }

    public static int maxFuel(ItemStack stack) {
        if (FueledTool.is(stack) && stack.get(MAXIMUM_FUEL) == null) {
            throw new IllegalStateException(stack.getItem() + " missing component of " + MAXIMUM_FUEL);
        } else if (FueledTool.is(stack)) {
            return Math.max(0, stack.get(MAXIMUM_FUEL));
        } else {
            return 0;
        }
    }

    public static TagKey<Item> fuels(ItemStack stack) {
        return EnderscapeItemTags.NEBULITE_TOOL_FUELS;
    }

    public static int fuelCost(ItemStackContext context) {
        ItemStack stack = context.stack();
        LivingEntity user = context.user();
        LodestoneTrackerContext tracker = LodestoneTrackerContext.of(context);
        if (FueledTool.is(stack) && stack.has(FUEL_PER_USE)) {
            if (LodestoneTeleporter.isLinked(stack)) {
                if (tracker.dimension() != tracker.linkedDimension()) {
                    return FueledTool.maxFuel(stack);
                } else if (LodestoneTeleportationCost.DEFAULT_INCREASE_WITH_DISTANCE) {
                    return stack.get(FUEL_PER_USE) + (distanceBetweenPoints(user.blockPosition(), tracker.linkedPos()) / getTotalDistanceForCostIncrease(context));
                } else {
                    return stack.get(FUEL_PER_USE);
                }
            } else {
                return stack.get(FUEL_PER_USE);
            }
        } return 0;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        boolean showTooltip = !stack.has(DataComponents.HIDE_TOOLTIP) && !stack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP);
        return showTooltip ? Optional.of(new FueledToolComponent(stack)) : Optional.empty();
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Mth.clamp(Mth.mulAndTruncate(computeFuelAmount(stack), 13), 0, 13);
    }

    public static Fraction computeFuelAmount(ItemStack stack) {
        return Fraction.getFraction(currentFuel(stack), maxFuel(stack));
    }


    @Override
    public int getBarColor(ItemStack stack) {
        return 0xFF66FF;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack fueled, ItemStack fuel, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action == ClickAction.PRIMARY && FueledTool.is(fueled) && fuel.is(FueledTool.fuels(fueled))) {
            if (FueledTool.currentFuel(fueled) < FueledTool.maxFuel(fueled)) {
                FueledTool.setFuel(fueled, FueledTool.currentFuel(fueled) + 1);
                fuel.shrink(1);

                player.playSound(EnderscapeItemSounds.NEBULITE_TOOL_ADD_FUEL.value(), 1.0F, 1.0F);
                player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            } else {
                player.playSound(EnderscapeItemSounds.NEBULITE_TOOL_FUEL_FULL.value(), 1.0F, 1.0F);
                player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack fueled, Slot fuel, ClickAction action, Player player) {
        if (action == ClickAction.PRIMARY && fuel.getItem().is(FueledTool.fuels(fueled))) {
            if (FueledTool.currentFuel(fueled) < FueledTool.maxFuel(fueled)) {
                int added = Math.min(fuel.getItem().getCount(), FueledTool.maxFuel(fueled) - FueledTool.currentFuel(fueled));
                FueledTool.setFuel(fueled, FueledTool.currentFuel(fueled) + added);
                fuel.getItem().shrink(added);

                player.playSound(EnderscapeItemSounds.NEBULITE_TOOL_ADD_FUEL.value(), 1.0F, 1.0F);
                player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            } else {
                player.playSound(EnderscapeItemSounds.NEBULITE_TOOL_FUEL_FULL.value(), 1.0F, 1.0F);
                player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            }
            return true;
        }
        return false;
    }

    public static boolean useFuel(ItemStackContext context) {
        ItemStack stack = context.stack();
        if (FueledTool.is(stack)) {
            boolean hasCounter = ThresholdCounter.is(stack);
            if (hasCounter) ThresholdCounter.increment(stack, 1);

            if (!hasCounter || ThresholdCounter.pastThreshold(context.serverLevel(), stack)) {
                setFuel(stack, currentFuel(stack) - fuelCost(context));

                if (hasCounter) ThresholdCounter.set(stack, 0);
                if (Enabled.is(stack)) Enabled.set(stack, true);

                LivingEntity user = context.user();
                Holder<SoundEvent> useFuelSound = Holder.direct(SoundEvents.EMPTY);

                if (useFuelSound.value() != SoundEvents.EMPTY && user != null) {
                    context.level().playSound(null, user.getX(), user.getY(), user.getZ(), useFuelSound.value(), user.getSoundSource(), 1, 1);
                }

                return true;
            }
        }
        return false;
    }

    public static boolean fuelExceedsCost(ItemStackContext context) {
        ItemStack stack = context.stack();

        if (FueledTool.is(stack)) {
            return currentFuel(stack) >= FueledTool.fuelCost(context);
        } else {
            return true;
        }
    }
}
