package net.bunten.enderscape.items;

import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.util.MathUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public enum MirrorCheck {
    NOT_LOCAL((context) -> !context.getUsageLevel().isClientSide()),
    LINKED("unlinked", (context) -> MirrorData.isLinked(context.getStack())),
    CAN_TRAVEL_TO("wrong_dimension", (context) -> MirrorData.canTravelTo(context)),
    NOT_TOO_FAR("too_far", (context) -> !MirrorData.isTooFar(context)),
    HAS_ENERGY("need_energy", (context) -> NebuliteChargedItem.canUse(context)),
    NOT_MISSING("missing", (context) -> MirrorData.isLinkedBlockActive(context)),
    NOT_OBSTRUCTED("obstructed", (context) -> !MirrorData.isObstructed(context));

    @Nullable
    public String message;
    private Predicate<MirrorUsageContext> predicate;

    private MirrorCheck(@Nullable String message, Predicate<MirrorUsageContext> predicate) {
        this.predicate = predicate;
        this.message = message;
    }

    private MirrorCheck(Predicate<MirrorUsageContext> predicate) {
        this(null, predicate);
    }

    public Predicate<MirrorUsageContext> getPredicate() {
        return predicate;
    }

    public boolean fails(MirrorUsageContext context) {
        return !predicate.test(context);
    }

    public InteractionResultHolder<ItemStack> getResult(MirrorUsageContext context) {
        if (message != null) {
            return fail(message, context);
        } else {
            return InteractionResultHolder.consume(context.getStack());
        }
    }

    private static InteractionResultHolder<ItemStack> fail(String string, MirrorUsageContext context) {
        Player player = context.getPlayer();
        ItemStack stack = context.getStack();

        player.getLevel().playSound(null, player.getX(), player.getY(), player.getZ(), EnderscapeSounds.MIRROR_FAILURE, player.getSoundSource(), 0.65F, MathUtil.nextFloat(player.getRandom(), 0.9F, 1.1F));
        player.displayClientMessage(Component.translatable("item." + Enderscape.MOD_ID + ".mirror.message." + string, MirrorData.linkedBlockName(stack)).withStyle(ChatFormatting.RED), true);
        player.getCooldowns().addCooldown(stack.getItem(), 20);

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }
}