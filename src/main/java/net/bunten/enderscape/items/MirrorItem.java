package net.bunten.enderscape.items;

import java.util.List;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.config.Config;
import net.bunten.enderscape.criteria.EnderscapeCriteria;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.bunten.enderscape.registry.EnderscapeSounds;
import net.bunten.enderscape.registry.EnderscapeStats;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;

public class MirrorItem extends NebuliteChargedItem implements Vanishable {
    public MirrorItem(Properties settings) {
        super(settings);
    }

    @Override
    public int getMaximumEnergy(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        int value;

        if (nbt == null) {
            value = 5;
        } else {
            value = nbt.contains(MAX_ENERGY) ? nbt.getInt(MAX_ENERGY) : 5;
        }

        return Math.max(3, value);
    }

    @Override
    public int getUseCost(ChargedUsageContext context) {
        ItemStack stack = context.getStack();
        LivingEntity user = context.getUser();

        if (user instanceof Player player && player.getAbilities().instabuild) {
            return 0;
        } else {
            if (!MirrorData.isSameDimension(stack, user.getLevel().dimension())) {
                return getMaximumEnergy(stack) - MirrorData.getLightspeed(stack);
            } else {
                return 1 + (MirrorData.roundedHorizontalDistance(user.blockPosition(), MirrorData.pos(stack)) / MirrorData.costIncreaseDistance(stack));
            }
        }
    }

    @Override
    public int energyPerGem(ItemStack stack) {
        return 1;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        MirrorUsageContext context = new MirrorUsageContext(player.getItemInHand(hand), world, player);
        for (MirrorCheck check : MirrorCheck.values()) {
            if (check.fails(context)) return check.getResult(context);
        }
        return teleport(context);
    }

    private InteractionResultHolder<ItemStack> teleport(MirrorUsageContext context) {
        ItemStack stack = context.getStack();
        ServerPlayer player = context.getServerPlayer();
        BlockPos linked = context.getLinkedPos();
        Vec3 initial = player.position();
        boolean same = MirrorData.isSameDimension(context);

        removeUseCost(context);

        if (player.isFallFlying()) player.stopFallFlying();
        if (player.isPassenger()) player.stopRiding();
        player.fallDistance = 0;

        MirrorPackets.sendPreTeleportPacket(player, !same);
        context.getUsageServerLevel().sendParticles(EnderscapeParticles.VANISHING_NEBULITE_CLOUD, initial.x, initial.y + 0.5, initial.z, 50, 0.5, 1, 0.5, 0.1);

        ServerLevel world = context.getLinkedLevel();
        FabricDimensions.teleport(player, world, new PortalInfo(new Vec3(linked.getX() + 0.5, linked.getY() + 1, linked.getZ() + 0.5), Vec3.ZERO, 0, 0));
        Vec3 after = player.position();

        world.sendParticles(EnderscapeParticles.RISING_NEBULITE_CLOUD, after.x, after.y + 0.5, after.z, 50, 0.5, 1, 0.5, 0.1);
        world.playSound(null, after.x, after.y, after.z, EnderscapeSounds.MIRROR_TELEPORT, player.getSoundSource(), 0.65F, 1);
        world.gameEvent(GameEvent.TELEPORT, player.position(), GameEvent.Context.of(player));
        
        if (!player.getAbilities().instabuild) player.getCooldowns().addCooldown(this, 100);
        player.awardStat(Stats.ITEM_USED.get(this));
        player.awardStat(EnderscapeStats.MIRROR_TELEPORT);

        if (same) {
            EnderscapeCriteria.MIRROR_TELEPORT_SAME.trigger(player, stack, initial, same);
        } else {
            EnderscapeCriteria.MIRROR_TELEPORT_DIFFERENT.trigger(player, stack, initial, same);
        }
        
        EnderscapeCriteria.MIRROR_TELEPORT_ANY.trigger(player, stack, initial, same);

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = world.getBlockState(pos);
        ItemStack stack = context.getItemInHand();

        if (MirrorData.isLinkable(state)) {
            MirrorData.writeData(stack, pos, context.getLevel().dimension(), state);
            world.playSound(null, pos, EnderscapeSounds.MIRROR_LINK, SoundSource.PLAYERS, 1, 1);
            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return MirrorData.isLinked(stack) || super.isFoil(stack);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return MirrorData.isLinked(stack) || getEnergy(stack) > 0;
    }

    @Override
    public Component getName(ItemStack stack) {
        if (MirrorData.isLinked(stack)) {
            return Component.translatable(getDescriptionId(stack) + ".linked", MirrorData.linkedBlockName(stack));
        } else {
            return super.getName(stack);
        }
    }

    @Environment(EnvType.CLIENT)
    private MutableComponent getDimensionKey(MirrorUsageContext context) {
        return Component.translatable(Util.makeDescriptionId("dimension", context.getLinkedDimension().location()));
    }

    @Environment(EnvType.CLIENT)
    private MutableComponent tooltip(String name, Object... objects) {
        return Component.translatable("item." + Enderscape.MOD_ID + ".mirror.desc." + name, objects);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        Minecraft client = Minecraft.getInstance();
        MirrorUsageContext context = new MirrorUsageContext(stack, world, client.player);
        
        if (MirrorData.isLinked(stack) && Config.CLIENT.displayMirrorTooltip()) {
            if (Config.CLIENT.shiftForMirrorInfo() ? Screen.hasShiftDown() : true) {
                BlockPos player = context.getPlayer().blockPosition();
                BlockPos linked = MirrorData.pos(stack);

                if (Config.CLIENT.displayMirrorCoordinates()) {
                    MutableComponent position = tooltip("position.coordinates", linked.getX(), linked.getY(), linked.getZ());
                    MutableComponent unknown_position = tooltip("position.unknown");

                    tooltip.add(tooltip("position", (MirrorData.isSameDimension(context) ? position : unknown_position)).withStyle(ChatFormatting.DARK_GREEN));
                }

                if (Config.CLIENT.displayMirrorDistance()) {
                    int int_distance = MirrorData.roundedHorizontalDistance(player, linked);
                    MutableComponent value = tooltip("distance.value", int_distance, tooltip("distance.append"));
                    MutableComponent unknown = tooltip("distance.unknown");
                    
                    tooltip.add(tooltip("distance", MirrorData.isSameDimension(context) ? value : unknown).withStyle(ChatFormatting.DARK_GREEN));
                }

                if (Config.CLIENT.displayMirrorDimension()) {
                    tooltip.add(tooltip("dimension", getDimensionKey(context)).withStyle(ChatFormatting.DARK_GREEN));
                }

            } else {
                tooltip.add(tooltip("unshifted").withStyle(ChatFormatting.GRAY));
            }
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean showUI(ItemStack stack, Player player) {
        return MirrorData.isLinked(stack) && !player.isSpectator() && player.getUseItem().isEmpty();
    }
}