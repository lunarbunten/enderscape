package net.penumbra.enderscape.manager;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.item.LodestoneTrackerContext;
import net.penumbra.enderscape.item.component.FueledTool;
import net.penumbra.enderscape.item.component.LodestoneTeleportation;

import java.util.List;

public class ClientsideLodestoneTeleportationManager {

    private static final Minecraft MINECRAFT = Minecraft.getInstance();
    private static final EnderscapeConfig CONFIG = EnderscapeConfig.getInstance();

    private static final ChatFormatting HEADER_COLOR = ChatFormatting.GRAY;
    private static final ChatFormatting INFO_COLOR = ChatFormatting.DARK_GRAY;
    private static final ChatFormatting VALUE_COLOR = ChatFormatting.BLUE;

    public static void appendTooltip(ItemStack stack, Item.TooltipContext context, TooltipFlag flag, List<Component> lines) {
        if (LodestoneTeleportation.is(stack)) {
            LodestoneTrackerContext tracker = new LodestoneTrackerContext(stack, MINECRAFT.level, MINECRAFT.player);

            if (LodestoneTeleportation.isLinked(stack)) {
                addLodestoneLocation(lines, tracker);
            }

            addAttributes(lines, tracker);
        }
    }

    private static void addAttributes(List<Component> lines, LodestoneTrackerContext tracker) {
        addHeader(lines, Component.translatable("item.modifiers.hand"));
        MutableComponent maximumRange = tooltip("distance.value", LodestoneTeleportation.getMaximumRange(tracker));
        lines.add(CommonComponents.space().append(tooltip("maximum_range", maximumRange)).withStyle(ChatFormatting.DARK_GREEN));
    }

    private static void addLodestoneLocation(List<Component> lines, LodestoneTrackerContext tracker) {
        boolean anyInfoEnabled = CONFIG.mirrorTooltipDisplayCoordinates || CONFIG.mirrorTooltipDisplayDistance || CONFIG.mirrorTooltipDisplayDimension;

        if (anyInfoEnabled && (!CONFIG.mirrorTooltipShiftToDisplay || MINECRAFT.hasShiftDown())) {
            BlockPos linkedPos = tracker.linkedPos();
            ResourceKey<Level> linkedDimension = tracker.linkedDimension();

            addHeader(lines, tooltip("header"));

            if (CONFIG.mirrorTooltipDisplayCoordinates) {
                MutableComponent position = tooltip("position.coordinates", linkedPos.getX(), linkedPos.getY(), linkedPos.getZ()).withStyle(VALUE_COLOR);
                MutableComponent unknown = tooltip("position.unknown").withStyle(VALUE_COLOR);
                MutableComponent component = tooltip("position", LodestoneTeleportation.isSameDimension(tracker, linkedDimension) ? position : unknown);

                lines.add(CommonComponents.space().append(component.withStyle(INFO_COLOR)));
            }

            if (CONFIG.mirrorTooltipDisplayDistance) {
                float step = FueledTool.is(tracker.stack()) ? LodestoneTeleportation.distanceToIncreaseCost(tracker) / 2.0F : 250.0F;
                int roundedDistance = (int) (Math.round(LodestoneTeleportation.distanceFromLodestone(tracker) / step) * step);

                MutableComponent approximate = tooltip("distance.approximate_value", roundedDistance).withStyle(VALUE_COLOR);
                MutableComponent unknown = tooltip("distance.unknown").withStyle(VALUE_COLOR);
                MutableComponent component = tooltip("distance", LodestoneTeleportation.isSameDimension(tracker, linkedDimension) ? approximate : unknown);

                lines.add(CommonComponents.space().append(component.withStyle(INFO_COLOR)));
            }

            if (CONFIG.mirrorTooltipDisplayDimension) {
                MutableComponent dimension = Component.translatable(Util.makeDescriptionId("dimension", linkedDimension.identifier())).withStyle(VALUE_COLOR);
                MutableComponent component = tooltip("dimension", dimension);

                lines.add(CommonComponents.space().append(component.withStyle(INFO_COLOR)));
            }

        } else {
            lines.add(tooltip("unshifted").withStyle(HEADER_COLOR));
        }
    }

    private static void addHeader(List<Component> lines, MutableComponent header) {
        if (!lines.contains(Component.empty())) {
            lines.add(Component.empty());
        }

        lines.add(header.withStyle(HEADER_COLOR));
    }

    private static MutableComponent tooltip(String name, Object... objects) {
        return Component.translatable("item." + Enderscape.MOD_ID + ".lodestone_teleportation.desc." + name, objects);
    }
}
