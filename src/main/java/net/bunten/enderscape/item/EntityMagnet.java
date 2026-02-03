package net.bunten.enderscape.item;

import net.bunten.enderscape.item.component.Enabled;
import net.bunten.enderscape.registry.EnderscapeEnchantmentEffectComponents;
import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.function.Predicate;

import static net.bunten.enderscape.registry.EnderscapeDataComponents.*;

public class EntityMagnet extends FueledTool {

    public static final Holder<SoundEvent> DEFAULT_PULL_ENTITY_SOUND = EnderscapeItemSounds.MAGNIA_ATTRACTOR_MOVE;

    static boolean canDepositIntoBundlesByDefault;

    public EntityMagnet(boolean allow, Properties properties) {
        super(properties);
        canDepositIntoBundlesByDefault = allow;
    }

    @Override
    public boolean displayHudWhen(ItemStackContext context) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return tryCyclePower(new ItemStackContext(player.getItemInHand(hand), level, player)) ? InteractionResultHolder.consume(player.getItemInHand(hand)) : super.use(level, player, hand);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action == ClickAction.SECONDARY && other.isEmpty()) return tryCyclePower(new ItemStackContext(stack, player.level(), player));
        return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
    }

    private boolean tryCyclePower(ItemStackContext context) {
        if (fuelExceedsCost(context)) {
            ItemStack stack = context.stack();
            setEnabled(stack, !isEnabled(stack));

            context.user().playSound(isEnabled(stack) ? EnderscapeItemSounds.MAGNIA_ATTRACTOR_POWER_ON.value() : EnderscapeItemSounds.MAGNIA_ATTRACTOR_POWER_OFF.value(), 1, 1);
            context.user().gameEvent(GameEvent.ITEM_INTERACT_FINISH);

            return true;
        } else {
            return false;
        }
    }

    public static boolean is(ItemStack stack) {
        return stack.getItem() instanceof EntityMagnet;
    }

    public static ItemStack getValidAttractor(Inventory inventory) {
        Player player = inventory.player;
        Level level = player.level();

        Predicate<ItemStack> isValidAttractor = stack -> stack.getItem() instanceof EntityMagnet && FueledTool.fuelExceedsCost(new ItemStackContext(stack, level, player));

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

    public static boolean tryUseFuel(ItemStackContext context, int count) {
        ItemStack stack = context.stack();
        LivingEntity user = context.user();

        if (EntityMagnet.shouldReduceFuel(stack)) {
            FueledTool.useFuel(context);
            EntityMagnet.setEntitiesPulled(stack, Math.max(0, count - EntityMagnet.getEntitiesPulledToUseFuel(stack)));
            EntityMagnet.setEnabled(stack, true);

            user.level().playSound(null, user.getX(), user.getY(), user.getZ(), EnderscapeItemSounds.MAGNIA_ATTRACTOR_USE_FUEL.value(), SoundSource.PLAYERS, 1, 1);
            return true;
        }

        return false;
    }

    public static boolean tryAddToBundle(Inventory inventory, ItemStack toAdd) {
        ItemStack magnet = getValidAttractor(inventory);

        if (magnet.isEmpty() || !Enabled.get(magnet) || !EntityMagnet.canDepositIntoBundles(magnet, inventory.player)) {
            return false;
        }

        for (ItemStack stack : inventory.items) {
            if (stack.getItem() instanceof BundleItem && stack.has(DataComponents.BUNDLE_CONTENTS)) {
                BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
                for (ItemStack bundle : contents.items()) {
                    if (ItemStack.isSameItemSameComponents(bundle, toAdd)) {
                        BundleContents.Mutable mutableContents = new BundleContents.Mutable(contents);
                        if (mutableContents.tryInsert(toAdd) > 0) {
                            stack.set(DataComponents.BUNDLE_CONTENTS, mutableContents.toImmutable());
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static boolean canDepositIntoBundles(ItemStack stack, LivingEntity user) {
        if (EntityMagnet.is(stack)) {
            return canDepositIntoBundlesByDefault || canAdditionallyDepositIntoBundles(stack, user);
        } else return false;
    }

    public static boolean canAdditionallyDepositIntoBundles(ItemStack stack, LivingEntity user) {
        MutableFloat mutable = new MutableFloat(0);
        EnchantmentHelper.runIterationOnItem(stack, (holder, i) -> holder.value().modifyUnfilteredValue(EnderscapeEnchantmentEffectComponents.MAGNET_ENABLE_DEPOSIT_INTO_BUNDLES.get(), user.getRandom(), i, mutable));
        return mutable.floatValue() > 0;
    }
}
