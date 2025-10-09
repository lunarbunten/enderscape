package net.bunten.enderscape.item;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.network.ClientboundTransdimensionalTravelSoundPayload;
import net.bunten.enderscape.registry.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
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
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.item.component.TooltipDisplay;
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
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

import static net.minecraft.core.component.DataComponents.LODESTONE_TRACKER;

public class MirrorItem extends NebuliteToolItem {

    public static final TeleportTransition.PostTeleportTransition PLAY_TRANSDIMENSIONAL_TRAVEL_SOUND = entity -> {
        if (entity instanceof ServerPlayer player) ServerPlayNetworking.send(player, new ClientboundTransdimensionalTravelSoundPayload());
    };

    public MirrorItem(Properties settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        updateLodestoneTracker(stack, level);
    }

    public static void updateLodestoneTracker(ItemStack stack, Level level) {
        if (level instanceof ServerLevel server) {
            LodestoneTracker tracker = stack.get(LODESTONE_TRACKER);

            if (tracker != null) {
                LodestoneTracker ticked = tracker.tick(server);
                if (ticked != tracker) stack.set(LODESTONE_TRACKER, ticked);
            }
        }
    }

    @Override
    public int fuelCost(NebuliteToolContext context) {
        ItemStack stack = context.stack();
        LivingEntity user = context.user();
        MirrorContext mirror = MirrorContext.of(context);

        if (mirror.dimension() != mirror.linkedDimension()) {
            return maxFuel(stack);
        } else {
            return 1 + (distanceBetweenPoints(user.blockPosition(), mirror.linkedPos()) / getTotalDistanceForCostIncrease(context));
        }
    }

    @Override
    public boolean displayHudWhen(NebuliteToolContext context) {
        return true;
    }

    @Override
    public boolean hideInvalidOutlineWhen(NebuliteToolContext context) {
        MirrorContext mirrorContext = MirrorContext.of(context);

        boolean isLinked = isLinked(context.stack());
        boolean exceedsCost = fuelExceedsCost(context);
        boolean sameDimension = isSameDimension(mirrorContext, mirrorContext.linkedDimension());
        boolean hasTransdimensional = hasTransdimensional(mirrorContext);

        return isLinked && exceedsCost && (sameDimension || (hasTransdimensional && isSameCoordinateScale(mirrorContext)));
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        MirrorContext context = new MirrorContext(player.getItemInHand(hand), world, player);
        for (MirrorUseChecks check : MirrorUseChecks.CHECKS_IN_ORDER) if (check.fails(context)) return check.getFailureResult(context);
        return teleport(context, false) ? InteractionResult.SUCCESS_SERVER : MirrorUseChecks.TELEPORT_POSITION_IS_SAFE.getFailureResult(context);
    }

    public static boolean teleport(MirrorContext context, boolean fromDispenser) {
        LivingEntity user = context.user();
        GlobalPos prior = new GlobalPos(context.dimension(), user.getOnPos());
        GlobalPos destination = new GlobalPos(context.linkedDimension(), context.linkedPos());
        Optional<Vec3> optional = getTeleportPosition(context);

        boolean sameDimension = isSameDimension(context, destination.dimension());

        if (optional.isPresent()) {
            Vec3 position = optional.get();

            if (!(context.user() instanceof Player player && player.getAbilities().instabuild && !fromDispenser)) useFuel(context);

            doPreTeleportEffects(user, !sameDimension, context, prior.pos().getCenter());
            teleportToLocation(context, BlockPos.containing(position), sameDimension ? TeleportTransition.DO_NOTHING : PLAY_TRANSDIMENSIONAL_TRAVEL_SOUND);
            awardMirrorStatistics(context, prior, destination, fromDispenser);

            return true;
        }

        return false;
    }

    public static Optional<Vec3> getTeleportPosition(MirrorContext context) {
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

    private static void doPreTeleportEffects(LivingEntity user, boolean sameDimension, MirrorContext context, Vec3 priorVec3) {
        user.stopRiding();
        user.fallDistance = 0;

        if (user instanceof ServerPlayer player) {
            if (user.isFallFlying()) player.stopFallFlying();
            EnderscapeServerNetworking.sendMirrorInfoPayload(player, sameDimension);
        }

        context.serverLevel().sendParticles(EnderscapeParticles.MIRROR_TELEPORT_OUT, priorVec3.x, priorVec3.y + 0.5, priorVec3.z, 50, 0.5, 1, 0.5, 0.1);
    }

    private static void teleportToLocation(MirrorContext context, BlockPos destination, TeleportTransition.PostTeleportTransition transition) {
        ServerLevel level = context.linkedLevel();
        Vec3 pos = destination.getBottomCenter();

        LivingEntity user = context.user();
        user.teleport(new TeleportTransition(level, pos, Vec3.ZERO, 0, 0, Relative.union(Relative.ROTATION, Relative.DELTA), transition));

        level.sendParticles(EnderscapeParticles.MIRROR_TELEPORT_IN, pos.x, pos.y + 0.5, pos.z, 50, 0.5, 1, 0.5, 0.1);
        level.playSound(null, pos.x, pos.y, pos.z, EnderscapeItemSounds.MIRROR_TELEPORT, user.getSoundSource(), 0.65F, 1);
        level.gameEvent(GameEvent.TELEPORT, user.position(), GameEvent.Context.of(user));
    }

    private static void awardMirrorStatistics(MirrorContext context, GlobalPos prior, GlobalPos destination, boolean fromDispenser) {
        if (context.user() instanceof ServerPlayer player) {
            if (!player.getAbilities().instabuild || fromDispenser) player.getCooldowns().addCooldown(context.stack(), 100);

            player.awardStat(Stats.ITEM_USED.get(context.stack().getItem()));
            player.awardStat(EnderscapeStats.MIRROR_TELEPORT);

            double distance = Math.sqrt(Math.pow(destination.pos().getX() - prior.pos().getX(), 2) + Math.pow(destination.pos().getZ() - prior.pos().getZ(), 2));
            int centimeterDistance = Math.round((float) distance * 100.0F);
            if (centimeterDistance > 0) {
                player.awardStat(EnderscapeStats.MIRROR_ONE_CM, centimeterDistance);
            }

            EnderscapeCriteria.MIRROR_TELEPORT.trigger(player, context.stack(), prior, destination);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
        ItemStack stack = context.getItemInHand();

        if (state.is(Blocks.LODESTONE)) {
            writeData(stack, pos, context.getLevel().dimension());
            world.playSound(null, pos, EnderscapeItemSounds.MIRROR_LINK, SoundSource.PLAYERS, 1, 1);
            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return wasLinkedBefore(stack) || super.isFoil(stack);
    }

    @Override
    public Component getName(ItemStack stack) {
        return isLinked(stack) ? Component.translatable(getDescriptionId() + ".linked") : getName();
    }

    private MutableComponent tooltip(String name, Object... objects) {
        return Component.translatable("item." + Enderscape.MOD_ID + ".mirror.desc." + name, objects);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText(ItemStack stack, Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag flag) {
        Minecraft client = Minecraft.getInstance();
        EnderscapeConfig config = EnderscapeConfig.getInstance();

        MirrorContext context = new MirrorContext(stack, client.level, client.player);
        
        if (isLinked(stack) && config.mirrorTooltipEnabled) {

            ChatFormatting typeColor = ChatFormatting.GRAY;
            ChatFormatting valueColor = ChatFormatting.BLUE;

            if (!config.mirrorTooltipShiftToDisplay || client.hasShiftDown()) {
                BlockPos user = context.user().blockPosition();
                BlockPos linkedPos = context.linkedPos();
                ResourceKey<Level> linkedDimension = context.linkedDimension();

                if (config.mirrorTooltipDisplayCoordinates) {
                    MutableComponent position = tooltip("position.coordinates", linkedPos.getX(), linkedPos.getY(), linkedPos.getZ()).withStyle(valueColor);
                    MutableComponent unknown = tooltip("position.unknown").withStyle(valueColor);

                    consumer.accept(tooltip("position", isSameDimension(context, linkedDimension) ? position : unknown).withStyle(typeColor));
                }

                if (config.mirrorTooltipDisplayDistance) {
                    float step = getDistanceForCostIncrease(stack) / 2.0F;
                    int roundedDistance = (int) (Math.round(distanceBetweenPoints(user, linkedPos) / step) * step);

                    MutableComponent approximate = tooltip("distance.approximate_value", roundedDistance).withStyle(valueColor);
                    MutableComponent unknown = tooltip("distance.unknown").withStyle(valueColor);

                    consumer.accept(tooltip("distance", isSameDimension(context, linkedDimension) ? approximate : unknown).withStyle(typeColor));
                }

                if (config.mirrorTooltipDisplayDimension) {
                    MutableComponent dimension = Component.translatable(Util.makeDescriptionId("dimension", linkedDimension.location())).withStyle(valueColor);;
                    consumer.accept(tooltip("dimension", dimension).withStyle(typeColor));
                }

            } else {
                consumer.accept(tooltip("unshifted").withStyle(typeColor));
            }
        }
    }

    public static boolean is(ItemStack stack) {
        return stack.getItem() instanceof MirrorItem;
    }

    public static boolean wasLinkedBefore(ItemStack stack) {
        return stack.has(LODESTONE_TRACKER);
    }

    public static boolean isLinked(ItemStack stack) {
        return wasLinkedBefore(stack) && stack.get(LODESTONE_TRACKER).target().isPresent();
    }

    public static boolean isSameCoordinateScale(MirrorContext context) {
        Registry<DimensionType> registry = context.level().registryAccess().lookupOrThrow(Registries.DIMENSION_TYPE);
        return DimensionType.getTeleportationScale(registry.get(context.dimension().location()).get().value(), registry.get(context.linkedDimension().location()).get().value()) == 1;
    }

    public static float getAdditionalCostIncreaseDistance(ItemStack stack, LivingEntity user, float f) {
        MutableFloat mutable = new MutableFloat(f);
        EnchantmentHelper.runIterationOnItem(stack, (holder, i) -> holder.value().modifyUnfilteredValue(EnderscapeEnchantmentEffectComponents.MIRROR_DISTANCE_FOR_COST_INCREASE, user.getRandom(), i, mutable));
        return Math.max(0.0F, mutable.floatValue());
    }

    public static void writeData(ItemStack stack, BlockPos pos, ResourceKey<Level> dimension) {
        stack.set(LODESTONE_TRACKER, new LodestoneTracker(Optional.of(GlobalPos.of(dimension, pos)), true));
    }

    public static boolean isSameDimension(MirrorContext context, ResourceKey<Level> dimension) {
        return context.dimension() == dimension;
    }

    public static boolean hasTransdimensional(MirrorContext context) {
        try {
            var registry = context.level().registryAccess().lookup(Registries.ENCHANTMENT).orElse(null);
            if (registry == null) return false;

            var enchantment = registry.getValue(Enderscape.id("transdimensional"));
            if (enchantment == null) return false;

            var holder = registry.wrapAsHolder(enchantment);
            return EnchantmentHelper.getItemEnchantmentLevel(holder, context.stack()) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private int getTotalDistanceForCostIncrease(NebuliteToolContext context) {
        ItemStack stack = context.stack();
        return (int) getAdditionalCostIncreaseDistance(context.stack(), context.user(), getDistanceForCostIncrease(stack));
    }

    private int getDistanceForCostIncrease(ItemStack stack) {
        if (stack.has(EnderscapeDataComponents.DISTANCE_FOR_COST_TO_INCREASE)) {
            return stack.get(EnderscapeDataComponents.DISTANCE_FOR_COST_TO_INCREASE);
        }

        throw new IllegalStateException("Cost increase distance is not defined.");
    }

    private int distanceBetweenPoints(BlockPos pos, BlockPos pos2) {
        float x = pos.getX() - pos2.getX();
        float z = pos.getZ() - pos2.getZ();
        return (int) Mth.sqrt(x * x + z * z);
    }
}