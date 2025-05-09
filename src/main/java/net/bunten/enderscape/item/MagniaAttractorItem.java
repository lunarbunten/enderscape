package net.bunten.enderscape.item;

import net.bunten.enderscape.registry.EnderscapeEnchantments;
import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Optional;
import java.util.function.Predicate;

import static net.bunten.enderscape.registry.EnderscapeDataComponents.*;

public class MagniaAttractorItem extends NebuliteToolItem {
    public MagniaAttractorItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean displayHudWhen(NebuliteToolContext context) {
        return is(context.stack()) && fuelExceedsCost(context) && isEnabled(context.stack());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return tryCyclePower(new NebuliteToolContext(player.getItemInHand(hand), level, player)) ? InteractionResultHolder.consume(player.getItemInHand(hand)) : super.use(level, player, hand);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action == ClickAction.SECONDARY && other.isEmpty()) return tryCyclePower(new NebuliteToolContext(stack, player.level(), player));
        return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
    }

    private boolean tryCyclePower(NebuliteToolContext context) {
        if (fuelExceedsCost(context)) {
            ItemStack stack = context.stack();
            setEnabled(stack, !isEnabled(stack));

            context.user().playSound(isEnabled(stack) ? EnderscapeItemSounds.MAGNIA_ATTRACTOR_POWER_ON : EnderscapeItemSounds.MAGNIA_ATTRACTOR_POWER_OFF, 1, 1);
            context.user().gameEvent(GameEvent.ITEM_INTERACT_FINISH);

            return true;
        } else {
            return false;
        }
    }

    public static boolean is(ItemStack stack) {
        return stack.getItem() instanceof MagniaAttractorItem;
    }

    public static ItemStack getValidAttractor(Inventory inventory) {
        Player player = inventory.player;
        Level level = player.level();

        Predicate<ItemStack> isValidAttractor = stack -> stack.getItem() instanceof MagniaAttractorItem && NebuliteToolItem.fuelExceedsCost(new NebuliteToolContext(stack, level, player));

        if (isValidAttractor.test(player.getMainHandItem())) return player.getMainHandItem();
        if (isValidAttractor.test(player.getOffhandItem())) return player.getOffhandItem();

        return inventory.items.stream().filter(isValidAttractor).findFirst().orElse(ItemStack.EMPTY);
    }

    public static void setEnabled(ItemStack stack, boolean value) {
        stack.set(ENABLED, value);
    }

    public static boolean isEnabled(ItemStack stack) {
        if (stack.has(ENABLED)) return stack.get(ENABLED);
        throw new IllegalStateException(stack.getItem() + " missing component of " + ENABLED);
    }

    public static void setEntitiesPulled(ItemStack stack, int value) {
        stack.set(ENTITIES_PULLED, value);
    }

    public static void incrementEntitiesPulled(ItemStack stack, int count) {
        stack.set(ENTITIES_PULLED, getEntitiesPulled(stack) + count);
    }

    public static int getEntitiesPulled(ItemStack stack) {
        if (stack.has(ENTITIES_PULLED)) return stack.get(ENTITIES_PULLED);
        return 0;
    }

    public static void setEntitiesPulledToUseFuel(ItemStack stack, int value) {
        stack.set(ENTITIES_PULLED_TO_USE_FUEL, value);
    }

    public static int getEntitiesPulledToUseFuel(ItemStack stack) {
        if (stack.has(ENTITIES_PULLED_TO_USE_FUEL)) return stack.get(ENTITIES_PULLED_TO_USE_FUEL);
        throw new IllegalStateException(stack.getItem() + " missing component of " + ENTITIES_PULLED_TO_USE_FUEL);
    }

    public static int getEntityPullRange(ItemStack stack) {
        if (stack.has(ENTITY_PULL_RANGE)) return stack.get(ENTITY_PULL_RANGE);
        return 0;
    }

    public static boolean shouldReduceFuel(ItemStack stack) {
        return getEntitiesPulled(stack) >= getEntitiesPulledToUseFuel(stack);
    }

    public static boolean tryUseFuel(NebuliteToolContext context, int count) {
        ItemStack stack = context.stack();
        LivingEntity user = context.user();

        if (MagniaAttractorItem.shouldReduceFuel(stack)) {
            NebuliteToolItem.useFuel(context);
            MagniaAttractorItem.setEntitiesPulled(stack, Math.max(0, count - MagniaAttractorItem.getEntitiesPulledToUseFuel(stack)));
            MagniaAttractorItem.setEnabled(stack, true);

            user.level().playSound(null, user.getX(), user.getY(), user.getZ(), EnderscapeItemSounds.MAGNIA_ATTRACTOR_USE_FUEL, SoundSource.PLAYERS, 1, 1);
            return true;
        }

        return false;
    }
}