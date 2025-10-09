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
import net.minecraft.core.*;
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
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
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
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static net.minecraft.core.component.DataComponents.LODESTONE_TRACKER;

public class MirrorItem extends NebuliteToolItem {

    public MirrorItem(Properties settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean bl) {
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
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        MirrorContext context = new MirrorContext(stack, world, player);
        for (MirrorUseChecks check : MirrorUseChecks.CHECKS_IN_ORDER) if (check.fails(context)) return new InteractionResultHolder<>(check.getFailureResult(context), stack);
        return teleport(context, false) ? InteractionResultHolder.success(stack) : new InteractionResultHolder<>(MirrorUseChecks.TELEPORT_POSITION_IS_SAFE.getFailureResult(context), stack);
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
            teleportToLocation(context, BlockPos.containing(position), !sameDimension);
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

    private static void teleportToLocation(MirrorContext context, BlockPos destination, boolean transdimensional) {
        ServerLevel level = context.linkedLevel();
        Vec3 pos = destination.getBottomCenter();

        LivingEntity user = context.user();
        user.teleportTo(level, pos.x, pos.y, pos.z, Set.of(), 0, 0);
        if (user instanceof ServerPlayer player && transdimensional) ServerPlayNetworking.send(player, new ClientboundTransdimensionalTravelSoundPayload());

        level.sendParticles(EnderscapeParticles.MIRROR_TELEPORT_IN, pos.x, pos.y + 0.5, pos.z, 50, 0.5, 1, 0.5, 0.1);
        level.playSound(null, pos.x, pos.y, pos.z, EnderscapeItemSounds.MIRROR_TELEPORT, user.getSoundSource(), 0.65F, 1);
        level.gameEvent(GameEvent.TELEPORT, user.position(), GameEvent.Context.of(user));
    }

    private static void awardMirrorStatistics(MirrorContext context, GlobalPos prior, GlobalPos destination, boolean fromDispenser) {
        if (context.user() instanceof ServerPlayer player) {
            if (!player.getAbilities().instabuild || fromDispenser) player.getCooldowns().addCooldown(context.stack().getItem(), 100);

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
        return isLinked(stack) ? Component.translatable(getDescriptionId() + ".linked") : super.getName(stack);
    }

    private MutableComponent tooltip(String name, Object... objects) {
        return Component.translatable("item." + Enderscape.MOD_ID + ".mirror.desc." + name, objects);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText(ItemStack stack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag flag) {
        Minecraft client = Minecraft.getInstance();
        EnderscapeConfig config = EnderscapeConfig.getInstance();

        MirrorContext context = new MirrorContext(stack, client.level, client.player);
        
        if (isLinked(stack) && config.mirrorTooltipEnabled) {

            ChatFormatting typeColor = ChatFormatting.GRAY;
            ChatFormatting valueColor = ChatFormatting.BLUE;

            if (!config.mirrorTooltipShiftToDisplay || Screen.hasShiftDown()) {
                BlockPos user = context.user().blockPosition();
                BlockPos linkedPos = context.linkedPos();
                ResourceKey<Level> linkedDimension = context.linkedDimension();

                if (config.mirrorTooltipDisplayCoordinates) {
                    MutableComponent position = tooltip("position.coordinates", linkedPos.getX(), linkedPos.getY(), linkedPos.getZ()).withStyle(valueColor);
                    MutableComponent unknown = tooltip("position.unknown").withStyle(valueColor);

                    list.add(tooltip("position", isSameDimension(context, linkedDimension) ? position : unknown).withStyle(typeColor));
                }

                if (config.mirrorTooltipDisplayDistance) {
                    float step = getDistanceForCostIncrease(stack) / 2.0F;
                    int roundedDistance = (int) (Math.round(distanceBetweenPoints(user, linkedPos) / step) * step);

                    MutableComponent approximate = tooltip("distance.approximate_value", roundedDistance).withStyle(valueColor);
                    MutableComponent unknown = tooltip("distance.unknown").withStyle(valueColor);

                    list.add(tooltip("distance", isSameDimension(context, linkedDimension) ? approximate : unknown).withStyle(typeColor));
                }

                if (config.mirrorTooltipDisplayDimension) {
                    MutableComponent dimension = Component.translatable(Util.makeDescriptionId("dimension", linkedDimension.location())).withStyle(valueColor);;
                    list.add(tooltip("dimension", dimension).withStyle(typeColor));
                }

            } else {
                list.add(tooltip("unshifted").withStyle(typeColor));
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
        HolderLookup.RegistryLookup<DimensionType> registry = context.level().registryAccess().lookupOrThrow(Registries.DIMENSION_TYPE);
        ResourceKey<DimensionType> beginning = ResourceKey.create(Registries.DIMENSION_TYPE, context.dimension().location());
        ResourceKey<DimensionType> linked = ResourceKey.create(Registries.DIMENSION_TYPE, context.linkedDimension().location());
        return DimensionType.getTeleportationScale(registry.get(beginning).get().value(), registry.get(linked).get().value()) == 1;
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
            HolderLookup.RegistryLookup<Enchantment> registry = context.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            Optional<Holder.Reference<Enchantment>> enchantment = registry.get(EnderscapeEnchantments.TRANSDIMENSIONAL);
            return EnchantmentHelper.getItemEnchantmentLevel(enchantment.get(), context.stack()) > 0;
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