package net.bunten.enderscape.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.bunten.enderscape.item.ItemStackContext;
import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import static net.bunten.enderscape.registry.EnderscapeDataComponents.TOGGLABLE;

public record Togglable(
        boolean inHand,
        boolean inInventory,
        Holder<SoundEvent> toggleOnSound,
        Holder<SoundEvent> toggleOffSound
) {

    public static final Togglable MAGNIA_ATTRACTOR = new Togglable(
            true,
            true,
            EnderscapeItemSounds.MAGNIA_ATTRACTOR_POWER_ON,
            EnderscapeItemSounds.MAGNIA_ATTRACTOR_POWER_OFF
    );

    public static final Codec<Togglable> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.BOOL.optionalFieldOf("in_hand", true).forGetter(Togglable::inHand),
                    Codec.BOOL.optionalFieldOf("in_inventory", true).forGetter(Togglable::inInventory),
                    SoundEvent.CODEC.optionalFieldOf("toggle_on_sound", SoundEvents.UI_BUTTON_CLICK).forGetter(Togglable::toggleOnSound),
                    SoundEvent.CODEC.optionalFieldOf("toggle_off_sound", SoundEvents.UI_BUTTON_CLICK).forGetter(Togglable::toggleOffSound)
            ).apply(instance, Togglable::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Togglable> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            Togglable::inHand,
            ByteBufCodecs.BOOL,
            Togglable::inInventory,
            SoundEvent.STREAM_CODEC,
            Togglable::toggleOnSound,
            SoundEvent.STREAM_CODEC,
            Togglable::toggleOffSound,
            Togglable::new
    );

    public static boolean is(ItemStack stack) {
        return stack.has(TOGGLABLE);
    }

    public static Togglable get(ItemStack stack) {
        return stack.get(TOGGLABLE);
    }

    public static InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        return tryToggle(new ItemStackContext(stack, level, player)) ? InteractionResultHolder.consume(stack) : InteractionResultHolder.pass(stack);
    }

    public static boolean override(ItemStack stack, ItemStack other, ClickAction action, Player player) {
        if (action == ClickAction.SECONDARY && other.isEmpty()) return tryToggle(new ItemStackContext(stack, player.level(), player));
        return false;
    }

    private static boolean tryToggle(ItemStackContext context) {
        ItemStack stack = context.stack();

        if (!FueledTool.is(stack) || FueledTool.fuelExceedsCost(context)) {
            Togglable togglable = Togglable.get(stack);

            Enabled.set(stack, !Enabled.get(stack));

            context.user().playSound(Enabled.get(stack) ? togglable.toggleOnSound().value() : togglable.toggleOffSound().value(), 1, 1);
            context.user().gameEvent(GameEvent.ITEM_INTERACT_FINISH);

            return true;
        } else {
            return false;
        }
    }
}