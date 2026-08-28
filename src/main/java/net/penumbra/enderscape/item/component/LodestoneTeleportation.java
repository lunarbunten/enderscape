package net.penumbra.enderscape.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.penumbra.enderscape.item.ItemStackContext;
import net.penumbra.enderscape.item.LodestoneTeleportationCheck;
import net.penumbra.enderscape.item.LodestoneTrackerContext;
import net.penumbra.enderscape.item.component.value.LodestoneTeleportationRules;
import net.penumbra.enderscape.item.component.value.LodestoneTeleportationSounds;
import net.penumbra.enderscape.item.component.value.LodestoneTeleportationVisuals;
import net.penumbra.enderscape.network.ClientboundTransdimensionalTravelSoundPayload;
import net.penumbra.enderscape.registry.component.EnderscapeDataComponents;
import net.penumbra.enderscape.registry.enchantment.EnderscapeEnchantmentEffectComponents;
import net.penumbra.enderscape.registry.entity.EnderscapeStats;
import net.penumbra.enderscape.registry.server.EnderscapeCriteria;
import net.penumbra.enderscape.registry.server.EnderscapeServerNetworking;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.Optional;

import static net.minecraft.core.component.DataComponents.LODESTONE_TRACKER;
import static net.penumbra.enderscape.registry.component.EnderscapeDataComponents.LODESTONE_TELEPORTATION;

public record LodestoneTeleportation(
        boolean nameHasPrefix,
        LodestoneTeleportationRules rules,
        LodestoneTeleportationVisuals visuals,
        LodestoneTeleportationSounds sounds
) {

    private static final boolean DEFAULT_NAME_HAS_PREFIX = false;

    public static final Codec<LodestoneTeleportation> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.BOOL.optionalFieldOf("name_has_prefix", DEFAULT_NAME_HAS_PREFIX).forGetter(LodestoneTeleportation::nameHasPrefix),
                    LodestoneTeleportationRules.CODEC.optionalFieldOf("rules", LodestoneTeleportationRules.DEFAULT).forGetter(LodestoneTeleportation::rules),
                    LodestoneTeleportationVisuals.CODEC.optionalFieldOf("visuals", LodestoneTeleportationVisuals.DEFAULT).forGetter(LodestoneTeleportation::visuals),
                    LodestoneTeleportationSounds.CODEC.optionalFieldOf("sounds", LodestoneTeleportationSounds.DEFAULT).forGetter(LodestoneTeleportation::sounds)
            ).apply(instance, LodestoneTeleportation::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, LodestoneTeleportation> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            LodestoneTeleportation::nameHasPrefix,
            LodestoneTeleportationRules.STREAM_CODEC,
            LodestoneTeleportation::rules,
            LodestoneTeleportationVisuals.STREAM_CODEC,
            LodestoneTeleportation::visuals,
            LodestoneTeleportationSounds.STREAM_CODEC,
            LodestoneTeleportation::sounds,
            LodestoneTeleportation::new
    );

    public static final LodestoneTeleportation DEFAULT = new LodestoneTeleportation(
            DEFAULT_NAME_HAS_PREFIX,
            LodestoneTeleportationRules.DEFAULT,
            LodestoneTeleportationVisuals.DEFAULT,
            LodestoneTeleportationSounds.DEFAULT
    );

    public static boolean is(ItemStack stack) {
        return stack.has(EnderscapeDataComponents.LODESTONE_TELEPORTATION);
    }

    public static LodestoneTeleportation get(ItemStack stack) {
        return stack.get(EnderscapeDataComponents.LODESTONE_TELEPORTATION);
    }

    public static void inventoryTick(ItemStack stack, ServerLevel level) {
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

    public static int fuelCost(ItemStackContext context) {
        ItemStack stack = context.stack();
        LivingEntity user = context.user();
        LodestoneTrackerContext tracker = LodestoneTrackerContext.of(context);

        if (FueledTool.is(stack) && user != null) {
            if (tracker.dimension() != tracker.linkedDimension()) {
                return FueledTool.maxFuel(stack);
            } else if (LodestoneTeleportation.get(stack).rules().increaseCostWithDistance()) {
                return 1 + (distanceFromLodestone(tracker) / distanceToIncreaseCost(context));
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }

    public static boolean hideInvalidOutlineWhen(ItemStackContext context) {
        LodestoneTrackerContext tracker = LodestoneTrackerContext.of(context);

        boolean isLinked = isLinked(context.stack());
        boolean withinRange = isWithinRange(tracker);
        boolean sameDimension = isSameDimension(tracker, tracker.linkedDimension());

        return isLinked && withinRange && (sameDimension || (isTransdimensionalAllowed(tracker) && isSameCoordinateScale(tracker)));
    }

    public static boolean isTransdimensionalAllowed(LodestoneTrackerContext tracker) {
        ItemStack stack = tracker.stack();
        if (LodestoneTeleportation.is(stack)) {
            LodestoneTeleportation teleportation = get(stack);
            return !teleportation.rules.restrictTransdimensional() || isTransdimensionalEnabledViaEnchantment(tracker.stack());
        } else throw new IllegalStateException(stack.getItem() + " missing component of " + LODESTONE_TELEPORTATION);
    }

    public static boolean wasLinkedBefore(ItemStack stack) {
        return stack.has(LODESTONE_TRACKER);
    }

    public static boolean isFoil(ItemStack stack) {
        return wasLinkedBefore(stack);
    }

    public static Component getName(ItemStack stack, Component original) {
        MutableComponent component = original.copy();

        if (isLinked(stack) && stack.has(LODESTONE_TELEPORTATION) && stack.get(LODESTONE_TELEPORTATION).nameHasPrefix()) {
            component = Component.translatable("item.enderscape.lodestone_teleportation.linked_prefix", component);
        }

        if (stack.has(EnderscapeDataComponents.DYE_COLOR)) {
            DyeColor color = stack.get(EnderscapeDataComponents.DYE_COLOR);
            component = Component.translatable("item.enderscape.dye_color.prefix_" + color.getSerializedName(), component);
        }

        return component;
    }

    public static boolean useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        ItemStack stack = context.getItemInHand();

        if (LodestoneTeleportation.is(stack) && state.is(Blocks.LODESTONE)) {
            writeData(stack, pos, context.getLevel().dimension());
            level.playSound(null, pos, LodestoneTeleportation.get(stack).sounds().link().value(), SoundSource.PLAYERS, 1, 1);
            return true;
        }

        return false;
    }

    public static void writeData(ItemStack stack, BlockPos pos, ResourceKey<Level> dimension) {
        stack.set(LODESTONE_TRACKER, new LodestoneTracker(Optional.of(GlobalPos.of(dimension, pos)), true));
    }

    public static InteractionResult use(Level level, Player player, InteractionHand hand) {
        LodestoneTrackerContext context = new LodestoneTrackerContext(player.getItemInHand(hand), level, player);
        for (LodestoneTeleportationCheck check : LodestoneTeleportationCheck.CHECKS_IN_ORDER) if (check.fails(context)) return check.getFailureResult(context);
        return teleport(context, false) ? InteractionResult.SUCCESS_SERVER : LodestoneTeleportationCheck.TELEPORT_POSITION_IS_SAFE.getFailureResult(context);
    }

    public static boolean teleport(LodestoneTrackerContext context, boolean fromDispenser) {
        LivingEntity user = context.user();
        GlobalPos prior = new GlobalPos(context.dimension(), user.getOnPos());
        GlobalPos destination = new GlobalPos(context.linkedDimension(), context.linkedPos());
        Optional<Vec3> optional = getTeleportPosition(context);

        boolean sameDimension = isSameDimension(context, destination.dimension());

        if (optional.isPresent()) {
            Vec3 position = optional.get();

            if (!(context.user() instanceof Player player && player.getAbilities().instabuild && !fromDispenser)) {
                FueledTool.useFuelOrDamage(context, 1, user.getEquipmentSlotForItem(context.stack()));
            }

            doPreTeleportEffects(context, prior.pos().getCenter(), !sameDimension);
            teleportToLocation(context, BlockPos.containing(position), sameDimension ? TeleportTransition.DO_NOTHING : entity -> {
                if (entity instanceof ServerPlayer player) {
                    LodestoneTeleportation teleportation = LodestoneTeleportation.get(context.stack());
                    ServerPlayNetworking.send(player, new ClientboundTransdimensionalTravelSoundPayload(teleportation.sounds().transdimensionalTravel().value().location()));
                }
            });
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

    public static boolean isWithinRange(LodestoneTrackerContext context) {
        ItemStack stack = context.stack();

        if (FueledTool.is(stack) && isSameDimension(context, context.linkedDimension())) {
            boolean withinMaxFuel = FueledTool.fuelCost(context) <= FueledTool.maxFuel(stack);
            boolean withinRange = distanceFromLodestone(context) <= getMaximumRange(context);
            return withinMaxFuel && withinRange;
        } else {
            return true;
        }
    }

    public static boolean isSameCoordinateScale(LodestoneTrackerContext context) {
        Registry<DimensionType> registry = context.level().registryAccess().lookupOrThrow(Registries.DIMENSION_TYPE);
        return DimensionType.getTeleportationScale(registry.get(context.dimension().identifier()).get().value(), registry.get(context.linkedDimension().identifier()).get().value()) == 1;
    }

    public static int distanceToIncreaseCost(ItemStackContext context) {
        return getMaximumRange(context) / FueledTool.maxFuel(context.stack());
    }

    public static int getMaximumRange(ItemStackContext context) {
        ItemStack stack = context.stack();
        if (LodestoneTeleportation.is(stack)) {
            LodestoneTeleportation teleportation = get(stack);
            return (int) modifyMaximumRangeViaEnchantments(stack, context.user(), teleportation.rules().maximumRange());
        } else throw new IllegalStateException(stack.getItem() + " missing component of " + LODESTONE_TELEPORTATION);
    }

    public static int distanceFromLodestone(LodestoneTrackerContext context) {
        return distanceBetween(context.user().blockPosition(), context.linkedPos());
    }

    private static int distanceBetween(BlockPos pos, BlockPos pos2) {
        if (pos == null || pos2 == null) {
            return 0;
        } else {
            float x = pos.getX() - pos2.getX();
            float z = pos.getZ() - pos2.getZ();
            return (int) Mth.sqrt(x * x + z * z);
        }
    }

    private static void doPreTeleportEffects(LodestoneTrackerContext context, Vec3 priorVec3, boolean sameDimension) {
        LodestoneTeleportation teleportation = LodestoneTeleportation.get(context.stack());
        LivingEntity user = context.user();

        user.stopRiding();
        user.fallDistance = 0;

        if (user instanceof ServerPlayer player) {
            if (user.isFallFlying()) player.stopFallFlying();
            EnderscapeServerNetworking.sendLodestoneTeleportationInfoPayload(player, sameDimension, teleportation.visuals());
        }

        context.serverLevel().sendParticles(teleportation.visuals().teleportOutParticle(), priorVec3.x, priorVec3.y + 0.5, priorVec3.z, 50, 0.5, 1, 0.5, 0.1);
    }

    private static void teleportToLocation(LodestoneTrackerContext context, BlockPos destination, TeleportTransition.PostTeleportTransition transition) {
        LodestoneTeleportation teleportation = LodestoneTeleportation.get(context.stack());
        ServerLevel level = context.linkedLevel();
        Vec3 pos = destination.getBottomCenter();

        LivingEntity user = context.user();
        user.teleport(new TeleportTransition(level, pos, Vec3.ZERO, 0, 0, Relative.union(Relative.ROTATION, Relative.DELTA), transition));

        level.sendParticles(teleportation.visuals().teleportInParticle(), pos.x, pos.y + 0.5, pos.z, 50, 0.5, 1, 0.5, 0.1);
        level.playSound(null, pos.x, pos.y, pos.z, teleportation.sounds().teleportSuccess(), user.getSoundSource(), 0.65F, 1);
        level.gameEvent(GameEvent.TELEPORT, user.position(), GameEvent.Context.of(user));
    }

    private static void awardStatistics(LodestoneTrackerContext context, GlobalPos prior, GlobalPos destination, boolean fromDispenser) {
        if (context.user() instanceof ServerPlayer player) {
            if (!player.getAbilities().instabuild || fromDispenser) player.getCooldowns().addCooldown(context.stack(), 100);

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

    private static float modifyMaximumRangeViaEnchantments(ItemStack stack, LivingEntity user, float original) {
        MutableFloat mutable = new MutableFloat(original);
        EnchantmentHelper.runIterationOnItem(stack, (holder, i) -> holder.value().modifyUnfilteredValue(EnderscapeEnchantmentEffectComponents.LODESTONE_TELEPORTATION_MAXIMUM_RANGE, user.getRandom(), i, mutable));
        return Math.max(0.0F, mutable.floatValue());
    }

    private static boolean isTransdimensionalEnabledViaEnchantment(ItemStack stack) {
        return EnchantmentHelper.has(stack, EnderscapeEnchantmentEffectComponents.LODESTONE_TELEPORTATION_ENABLE_TRANSDIMENSIONAL);
    }
}