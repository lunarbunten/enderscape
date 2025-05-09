package net.bunten.enderscape.item;

import net.bunten.enderscape.registry.EnderscapeItemSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

import static net.bunten.enderscape.item.MirrorItem.hasTransdimensional;

public record MirrorUseChecks(@Nullable Component failureComponent, Predicate<MirrorContext> shouldProceed) {

    public static final MirrorUseChecks IS_RUNNING_ON_SERVER = new MirrorUseChecks(null, context -> !context.level().isClientSide());
    public static final MirrorUseChecks IS_LINKED_TO_LODESTONE = new MirrorUseChecks(Component.translatable("item.enderscape.mirror.message.unlinked"), (context) -> MirrorItem.isLinked(context.stack()));
    public static final MirrorUseChecks CAN_TRAVEL_TO_LODESTONE = new MirrorUseChecks(Component.translatable("item.enderscape.mirror.message.wrong_dimension"), MirrorUseChecks::canTravelTo);
    public static final MirrorUseChecks IS_SAME_COORDINATE_SCALE = new MirrorUseChecks(Component.translatable("item.enderscape.mirror.message.wrong_coordinate_scale"), MirrorItem::isSameCoordinateScale);
    public static final MirrorUseChecks ISNT_TOO_FAR_FROM_LODESTONE = new MirrorUseChecks(Component.translatable("item.enderscape.mirror.message.too_far"), context -> !MirrorUseChecks.isTooFar(context));
    public static final MirrorUseChecks FUEL_EXCEEDS_COST = new MirrorUseChecks(Component.translatable("item.enderscape.mirror.message.need_fuel"), NebuliteToolItem::fuelExceedsCost);
    public static final MirrorUseChecks USER_IS_NOT_TOO_BIG = new MirrorUseChecks(Component.translatable("item.enderscape.mirror.message.unsafe"), context -> !MirrorUseChecks.isTooBig(context));
    public static final MirrorUseChecks TELEPORT_POSITION_IS_SAFE = new MirrorUseChecks(Component.translatable("item.enderscape.mirror.message.unsafe"), context -> MirrorItem.getTeleportPosition(context).isPresent());

    public static final List<MirrorUseChecks> CHECKS_IN_ORDER = List.of(
            IS_RUNNING_ON_SERVER,
            IS_LINKED_TO_LODESTONE,
            CAN_TRAVEL_TO_LODESTONE,
            IS_SAME_COORDINATE_SCALE,
            ISNT_TOO_FAR_FROM_LODESTONE,
            FUEL_EXCEEDS_COST,
            USER_IS_NOT_TOO_BIG,
            TELEPORT_POSITION_IS_SAFE
    );

    public boolean fails(MirrorContext context) {
        return !shouldProceed.test(context);
    }

    public InteractionResult getFailureResult(MirrorContext context) {
        return failureComponent != null ? failureResult(context, Component.translatable(failureComponent.getString()).withStyle(ChatFormatting.RED)) : InteractionResult.CONSUME;
    }

    private static InteractionResult failureResult(MirrorContext context, Component failureComponent) {
        LivingEntity user = context.user();
        ItemStack stack = context.stack();

        user.level().playSound(null, user.getX(), user.getY(), user.getZ(), EnderscapeItemSounds.MIRROR_FAILURE, user.getSoundSource(), 0.65F, Mth.nextFloat(user.getRandom(), 0.9F, 1.1F));

        if (user instanceof Player player) {
            player.displayClientMessage(failureComponent, true);
            player.getCooldowns().addCooldown(stack.getItem(), 20);
        }

        user.gameEvent(GameEvent.ITEM_INTERACT_FINISH);

        return InteractionResult.SUCCESS;
    }

    private static boolean canTravelTo(MirrorContext context) {
        return hasTransdimensional(context) || MirrorItem.isSameDimension(context, context.linkedDimension());
    }

    private static boolean isTooFar(MirrorContext context) {
        return context.item().fuelCost(context) > MirrorItem.maxFuel(context.stack());
    }

    private static boolean isTooBig(MirrorContext context) {
        LivingEntity user = context.user();
        EntityDimensions dimensions = user.getDimensions(user.getPose());
        float limit = 4.0F;
        return dimensions.width() > limit || dimensions.height() > limit;
    }
}