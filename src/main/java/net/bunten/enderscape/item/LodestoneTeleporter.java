package net.bunten.enderscape.item;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.item.component.FueledTool;
import net.bunten.enderscape.item.component.value.LodestoneTeleportationVisuals;
import net.bunten.enderscape.network.ClientboundTransdimensionalTravelSoundPayload;
import net.bunten.enderscape.registry.*;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static net.bunten.enderscape.item.component.FueledTool.useFuel;
import static net.bunten.enderscape.registry.EnderscapeDataComponents.DISTANCE_TO_INCREASE;
import static net.bunten.enderscape.registry.EnderscapeDataComponents.INCREASE_WITH_DISTANCE;
import static net.minecraft.core.component.DataComponents.LODESTONE_TRACKER;

public class LodestoneTeleporter extends EnchantableItem {
    static boolean allowsTransdimensionalByDefault;

    public LodestoneTeleporter(boolean allow, Properties properties) {
        super(properties);
        allowsTransdimensionalByDefault = allow;
    }

    public static boolean is(ItemStack stack) {
        return stack.getItem() instanceof LodestoneTeleporter;
    }

    public static int fuelCost(ItemStackContext context) {
        ItemStack stack = context.stack();
        LivingEntity user = context.user();
        LodestoneTrackerContext tracker = LodestoneTrackerContext.of(context);

        if (FueledTool.is(stack) && user != null) {
            if (tracker.dimension() != tracker.linkedDimension()) {
                return FueledTool.maxFuel(stack);
            } else if (stack.get(INCREASE_WITH_DISTANCE) == true) {
                return 1 + (distanceBetweenPoints(user.blockPosition(), tracker.linkedPos()) / getTotalDistanceForCostIncrease(context));
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        updateLodestoneTracker(stack, level);
    }

    public static void updateLodestoneTracker(ItemStack stack, Level level) {
        if (level instanceof ServerLevel server && stack.has(LODESTONE_TRACKER)) {
            LodestoneTracker tracker = stack.get(LODESTONE_TRACKER);
            LodestoneTracker ticked = tracker.tick(server);
            if (ticked != tracker) stack.set(LODESTONE_TRACKER, ticked);
        }
    }

    public static boolean isLinked(ItemStack stack) {
        return wasLinkedBefore(stack) && stack.get(LODESTONE_TRACKER).target().isPresent();
    }

    public static boolean hideInvalidOutlineWhen(ItemStackContext context) {
        LodestoneTrackerContext tracker = LodestoneTrackerContext.of(context);

        boolean isLinked = isLinked(context.stack());
        boolean exceedsCost = FueledTool.fuelExceedsCost(context);
        boolean sameDimension = isSameDimension(tracker, tracker.linkedDimension());

        return isLinked && exceedsCost && (sameDimension || (isTransdimensionalAllowed(tracker) && isSameCoordinateScale(tracker)));
    }

    public static boolean isTransdimensionalAllowed(LodestoneTrackerContext tracker) {
        return allowsTransdimensionalByDefault || isTransdimensionalAdditionallyAllowed(tracker.stack(), tracker.user());
    }

    public static boolean wasLinkedBefore(ItemStack stack) {
        return stack.has(LODESTONE_TRACKER);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return wasLinkedBefore(stack);
    }

    public Component getName(ItemStack stack) {
        return Component.translatable("item.enderscape.lodestone_teleportation.linked_prefix", Component.translatable(stack.getItem().getDescriptionId()));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        ItemStack stack = context.getItemInHand();

        if (FueledTool.is(stack) && state.is(Blocks.LODESTONE)) {
            writeData(stack, pos, context.getLevel().dimension());
            level.playSound(null, pos, EnderscapeItemSounds.MIRROR_LINK.value(), SoundSource.PLAYERS, 1, 1);
            return InteractionResult.SUCCESS_NO_ITEM_USED;
        }

        return InteractionResult.PASS;
    }

    public static void writeData(ItemStack stack, BlockPos pos, ResourceKey<Level> dimension) {
        stack.set(LODESTONE_TRACKER, new LodestoneTracker(Optional.of(GlobalPos.of(dimension, pos)), true));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        LodestoneTrackerContext context = new LodestoneTrackerContext(player.getItemInHand(hand), level, player);
        for (LodestoneTeleportationCheck check : LodestoneTeleportationCheck.CHECKS_IN_ORDER) if (check.fails(context)) return check.getFailureResult(context);
        return teleport(context, false) ? InteractionResultHolder.success(player.getItemInHand(hand)) : LodestoneTeleportationCheck.TELEPORT_POSITION_IS_SAFE.getFailureResult(context);
    }

    public static boolean teleport(LodestoneTrackerContext context, boolean fromDispenser) {
        LivingEntity user = context.user();
        GlobalPos prior = new GlobalPos(context.dimension(), user.getOnPos());
        GlobalPos destination = new GlobalPos(context.linkedDimension(), context.linkedPos());
        Optional<Vec3> optional = getTeleportPosition(context);

        boolean sameDimension = isSameDimension(context, destination.dimension());

        if (optional.isPresent()) {
            Vec3 position = optional.get();

            if (!(context.user() instanceof Player player && player.getAbilities().instabuild && !fromDispenser)) useFuel(context);

            doPreTeleportEffects(context, prior.pos().getCenter(), !sameDimension);
            teleportToLocation(context, BlockPos.containing(position), !sameDimension);
            awardStatistics(context, prior, destination, fromDispenser);

            return true;
        }

        return false;
    }

    public static Optional<Vec3> getTeleportPosition(LodestoneTrackerContext context) {
        ServerLevel level = context.linkedLevel();
        LivingEntity user = context.user();
        EntityDimensions dimensions = user.getDimensions(Pose.STANDING);

        Vec3 offsetPos = context.linkedPos().above().getBottomCenter().add(0.0, dimensions.height() / 2.0, 0.0);

        VoxelShape shape = Shapes.create(AABB.ofSize(offsetPos, dimensions.width() + 1, dimensions.height() + 1, dimensions.width() + 1).inflate(1.0E-6));
        Optional<Vec3> freePos = level.findFreePosition(user, shape, offsetPos, dimensions.width(), dimensions.height(), dimensions.width());

        if (freePos.isPresent()) {
            Vec3 pos = freePos.get();
            BlockPos.MutableBlockPos mutable = BlockPos.containing(pos).mutable();
            int i = 0;

            while (i < 8) {
                if (level.getBlockState(mutable.below()).isFaceSturdy(level, mutable, Direction.UP)) {
                    return Optional.of(Vec3.atLowerCornerOf(mutable));
                }

                mutable.move(Direction.DOWN);
                i++;
            }
        }

        return Optional.empty();
    }

    public static boolean isSameDimension(LodestoneTrackerContext context, ResourceKey<Level> dimension) {
        return context.dimension() == dimension;
    }

    private static void doPreTeleportEffects(LodestoneTrackerContext context, Vec3 priorVec3, boolean sameDimension) {
        LivingEntity user = context.user();

        user.stopRiding();
        user.fallDistance = 0;

        if (user instanceof ServerPlayer player) {
            if (user.isFallFlying()) player.stopFallFlying();
            EnderscapeServerNetworking.sendLodestoneTeleportationInfoPayload(player, sameDimension, LodestoneTeleportationVisuals.DEFAULT);
        }

        context.serverLevel().sendParticles(LodestoneTeleportationVisuals.DEFAULT_TELEPORT_OUT_PARTICLE, priorVec3.x, priorVec3.y + 0.5, priorVec3.z, 50, 0.5, 1, 0.5, 0.1);
    }

    private static void teleportToLocation(LodestoneTrackerContext context, BlockPos destination, boolean transdimensional) {
        ServerLevel level = context.linkedLevel();
        Vec3 pos = destination.getBottomCenter();

        LivingEntity user = context.user();
        user.teleportTo(level, pos.x, pos.y, pos.z, Set.of(), 0, 0);
        if (user instanceof ServerPlayer player && transdimensional) player.connection.send(new ClientboundTransdimensionalTravelSoundPayload(EnderscapeItemSounds.MIRROR_TRANSDIMENSIONAL_TRAVEL.value().getLocation()));

        level.sendParticles(LodestoneTeleportationVisuals.DEFAULT_TELEPORT_IN_PARTICLE, pos.x, pos.y + 0.5, pos.z, 50, 0.5, 1, 0.5, 0.1);
        level.playSound(null, pos.x, pos.y, pos.z, EnderscapeItemSounds.MIRROR_TELEPORT, user.getSoundSource(), 0.65F, 1);
        level.gameEvent(GameEvent.TELEPORT, user.position(), GameEvent.Context.of(user));
    }

    private static void awardStatistics(LodestoneTrackerContext context, GlobalPos prior, GlobalPos destination, boolean fromDispenser) {
        if (context.user() instanceof ServerPlayer player) {
            if (!player.getAbilities().instabuild || fromDispenser) player.getCooldowns().addCooldown(context.stack().getItem(), 100);

            player.awardStat(Stats.ITEM_USED.get(context.stack().getItem()));
            player.awardStat(EnderscapeStats.MIRROR_TELEPORT);

            double distance = Math.sqrt(Math.pow(destination.pos().getX() - prior.pos().getX(), 2) + Math.pow(destination.pos().getZ() - prior.pos().getZ(), 2));
            int centimeterDistance = Math.round((float) distance * 100.0F);
            if (centimeterDistance > 0) {
                player.awardStat(EnderscapeStats.MIRROR_ONE_CM, centimeterDistance);
            }

            EnderscapeCriteria.LODESTONE_TELEPORTATION.trigger(player, context.stack(), prior, destination);
        }
    }

    public static boolean isSameCoordinateScale(LodestoneTrackerContext tracker) {
        HolderLookup.RegistryLookup<DimensionType> registry = tracker.level().registryAccess().lookupOrThrow(Registries.DIMENSION_TYPE);
        ResourceKey<DimensionType> beginning = ResourceKey.create(Registries.DIMENSION_TYPE, tracker.dimension().location());
        ResourceKey<DimensionType> linked = ResourceKey.create(Registries.DIMENSION_TYPE, tracker.linkedDimension().location());
        return DimensionType.getTeleportationScale(registry.get(beginning).get().value(), registry.get(linked).get().value()) == 1;
    }

    public static int getTotalDistanceForCostIncrease(ItemStackContext context) {
        ItemStack stack = context.stack();
        return (int) getAddedDistanceToIncreaseCost(context.stack(), context.user(), stack.get(DISTANCE_TO_INCREASE));
    }

    public static float getAddedDistanceToIncreaseCost(ItemStack stack, LivingEntity user, float distanceToIncreaseCost) {
        MutableFloat mutable = new MutableFloat(distanceToIncreaseCost);
        EnchantmentHelper.runIterationOnItem(stack, (holder, i) -> holder.value().modifyUnfilteredValue(EnderscapeEnchantmentEffectComponents.LODESTONE_TELEPORTATION_DISTANCE_TO_INCREASE_COST.get(), user.getRandom(), i, mutable));
        return Math.max(0.0F, mutable.floatValue());
    }

    public static boolean isTransdimensionalAdditionallyAllowed(ItemStack stack, LivingEntity user) {
        MutableFloat mutable = new MutableFloat(0);
        EnchantmentHelper.runIterationOnItem(stack, (holder, i) -> holder.value().modifyUnfilteredValue(EnderscapeEnchantmentEffectComponents.LODESTONE_TELEPORTATION_ENABLE_TRANSDIMENSIONAL.get(), user.getRandom(), i, mutable));
        return mutable.floatValue() > 0;
    }

    @OnlyIn(Dist.CLIENT)
    public static void appendHoverText(ItemStack stack, List<Component> list) {
        Minecraft client = Minecraft.getInstance();
        EnderscapeConfig config = EnderscapeConfig.getInstance();

        LodestoneTrackerContext context = new LodestoneTrackerContext(stack, client.level, client.player);

        if (FueledTool.is(stack) && LodestoneTeleporter.isLinked(stack) && config.mirrorTooltipEnabled) {

            ChatFormatting headerColor = ChatFormatting.GRAY;
            ChatFormatting infoColor = ChatFormatting.DARK_GRAY;
            ChatFormatting valueColor = ChatFormatting.BLUE;

            if (!config.mirrorTooltipShiftToDisplay || Screen.hasShiftDown()) {
                BlockPos user = context.user().blockPosition();
                BlockPos linkedPos = context.linkedPos();
                ResourceKey<Level> linkedDimension = context.linkedDimension();

                list.add(tooltip("header").withStyle(headerColor));

                if (config.mirrorTooltipDisplayCoordinates) {
                    MutableComponent position = tooltip("position.coordinates", linkedPos.getX(), linkedPos.getY(), linkedPos.getZ()).withStyle(valueColor);
                    MutableComponent unknown = tooltip("position.unknown").withStyle(valueColor);
                    MutableComponent component = tooltip("position", isSameDimension(context, linkedDimension) ? position : unknown);

                    list.add(CommonComponents.space().append(component.withStyle(infoColor)));
                }

                if (config.mirrorTooltipDisplayDistance) {
                    float step = stack.get(DISTANCE_TO_INCREASE) / 2.0F;
                    int roundedDistance = (int) (Math.round(distanceBetweenPoints(user, linkedPos) / step) * step);

                    MutableComponent approximate = tooltip("distance.approximate_value", roundedDistance).withStyle(valueColor);
                    MutableComponent unknown = tooltip("distance.unknown").withStyle(valueColor);
                    MutableComponent component = tooltip("distance", isSameDimension(context, linkedDimension) ? approximate : unknown);

                    list.add(CommonComponents.space().append(component.withStyle(infoColor)));
                }

                if (config.mirrorTooltipDisplayDimension) {
                    MutableComponent dimension = Component.translatable(Util.makeDescriptionId("dimension", linkedDimension.location())).withStyle(valueColor);;
                    MutableComponent component = tooltip("dimension", dimension);

                    list.add(CommonComponents.space().append(component.withStyle(infoColor)));
                }

            } else {
                list.add(tooltip("unshifted").withStyle(headerColor));
            }
        }
    }

    private static MutableComponent tooltip(String name, Object... objects) {
        return Component.translatable("item." + Enderscape.MOD_ID + ".lodestone_teleportation.desc." + name, objects);
    }

    public static int distanceBetweenPoints(BlockPos pos, BlockPos pos2) {
        float x = pos.getX() - pos2.getX();
        float z = pos.getZ() - pos2.getZ();
        return (int) Mth.sqrt(x * x + z * z);
    }
}
