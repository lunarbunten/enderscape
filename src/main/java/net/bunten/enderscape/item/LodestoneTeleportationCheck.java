package net.bunten.enderscape.item;

import net.bunten.enderscape.item.component.FueledTool;
import net.bunten.enderscape.item.component.LodestoneTeleportation;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public record LodestoneTeleportationCheck(@Nullable Component failureComponent, Predicate<LodestoneTrackerContext> shouldProceed) {

    public static final LodestoneTeleportationCheck IS_RUNNING_ON_SERVER = new LodestoneTeleportationCheck(null, context -> !context.level().isClientSide());
    public static final LodestoneTeleportationCheck IS_LINKED_TO_LODESTONE = new LodestoneTeleportationCheck(Component.translatable("item.enderscape.lodestone_teleportation.message.unlinked"), (context) -> LodestoneTeleportation.isLinked(context.stack()));
    public static final LodestoneTeleportationCheck CAN_TRAVEL_TO_LODESTONE = new LodestoneTeleportationCheck(Component.translatable("item.enderscape.lodestone_teleportation.message.wrong_dimension"), LodestoneTeleportationCheck::canTravelTo);
    public static final LodestoneTeleportationCheck IS_SAME_COORDINATE_SCALE = new LodestoneTeleportationCheck(Component.translatable("item.enderscape.lodestone_teleportation.message.wrong_coordinate_scale"), LodestoneTeleportation::isSameCoordinateScale);
    public static final LodestoneTeleportationCheck ISNT_TOO_FAR_FROM_LODESTONE = new LodestoneTeleportationCheck(Component.translatable("item.enderscape.lodestone_teleportation.message.too_far"), context -> !LodestoneTeleportationCheck.isTooFar(context));
    public static final LodestoneTeleportationCheck FUEL_EXCEEDS_COST = new LodestoneTeleportationCheck(Component.translatable("item.enderscape.lodestone_teleportation.message.need_fuel"), FueledTool::fuelExceedsCost);
    public static final LodestoneTeleportationCheck USER_IS_NOT_TOO_BIG = new LodestoneTeleportationCheck(Component.translatable("item.enderscape.lodestone_teleportation.message.unsafe"), context -> !LodestoneTeleportationCheck.isTooBig(context));
    public static final LodestoneTeleportationCheck TELEPORT_POSITION_IS_SAFE = new LodestoneTeleportationCheck(Component.translatable("item.enderscape.lodestone_teleportation.message.unsafe"), context -> LodestoneTeleportation.getTeleportPosition(context).isPresent());

    public static final List<LodestoneTeleportationCheck> CHECKS_IN_ORDER = List.of(
            IS_RUNNING_ON_SERVER,
            IS_LINKED_TO_LODESTONE,
            CAN_TRAVEL_TO_LODESTONE,
            IS_SAME_COORDINATE_SCALE,
            ISNT_TOO_FAR_FROM_LODESTONE,
            FUEL_EXCEEDS_COST,
            USER_IS_NOT_TOO_BIG,
            TELEPORT_POSITION_IS_SAFE
    );

    public boolean fails(LodestoneTrackerContext context) {
        return !shouldProceed.test(context);
    }

    public InteractionResult getFailureResult(LodestoneTrackerContext context) {
        return failureComponent != null ? failureResult(context, Component.translatable(failureComponent.getString()).withStyle(ChatFormatting.RED)) : InteractionResult.CONSUME;
    }

    private static InteractionResult failureResult(LodestoneTrackerContext context, Component failureComponent) {
        LivingEntity user = context.user();
        ItemStack stack = context.stack();

        user.level().playSound(null, user.getX(), user.getY(), user.getZ(), LodestoneTeleportation.get(stack).sounds().teleportFailure(), user.getSoundSource(), 0.65F, Mth.nextFloat(user.getRandom(), 0.9F, 1.1F));

        if (user instanceof Player player) {
            player.sendOverlayMessage(failureComponent);
            player.getCooldowns().addCooldown(stack, 20);
        }

        user.gameEvent(GameEvent.ITEM_INTERACT_FINISH);

        return InteractionResult.SUCCESS_SERVER;
    }

    private static boolean canTravelTo(LodestoneTrackerContext context) {
        return LodestoneTeleportation.isTransdimensionalAllowed(context) || LodestoneTeleportation.isSameDimension(context, context.linkedDimension());
    }

    private static boolean isTooFar(LodestoneTrackerContext context) {
        ItemStack stack = context.stack();

        if (FueledTool.is(stack)) {
            return FueledTool.fuelCost(context) > FueledTool.maxFuel(stack);
        } else {
            return false;
        }
    }

    private static boolean isTooBig(LodestoneTrackerContext context) {
        ItemStack stack = context.stack();

        if (LodestoneTeleportation.is(stack)) {
            LivingEntity user = context.user();
            EntityDimensions dimensions = user.getDimensions(user.getPose());
            Vec2 maximum = LodestoneTeleportation.get(stack).maximumEntitySize();

            return dimensions.width() > maximum.x || dimensions.height() > maximum.y;
        } else {
            return true;
        }
    }
}