package net.bunten.enderscape.client;

import com.google.common.reflect.Reflection;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.block.MagniaSproutRenderer;
import net.bunten.enderscape.client.hud.HudElement;
import net.bunten.enderscape.client.item.FueledToolTooltip;
import net.bunten.enderscape.client.registry.*;
import net.bunten.enderscape.client.sound.EndermanStareSoundInstance;
import net.bunten.enderscape.client.sound.EndermanStaticSoundInstance;
import net.bunten.enderscape.item.ItemStackContext;
import net.bunten.enderscape.item.component.Enabled;
import net.bunten.enderscape.item.component.FueledTool;
import net.bunten.enderscape.item.tooltip.FueledToolComponent;
import net.bunten.enderscape.registry.EnderscapeBlockEntities;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class EnderscapeClient implements ClientModInitializer {

    public static final List<HudElement> HUD_ELEMENTS = new ArrayList<>();

    public static Optional<Music> structureMusic = Optional.empty();

    public static final int MAX_STARE_STICKS = 100;
    public static int stareTicks;

    public static Optional<ResourceLocation> lodestoneTeleportationOverlayTexture = Optional.empty();
    public static Optional<ResourceLocation> lodestoneTeleportationVignetteTexture = Optional.empty();
    public static final int MAX_LODESTONE_TELEPORTATION_TICKS = 60;
    public static int lodestoneTeleportationTicks;

    @Nullable public static EndermanStareSoundInstance stareSoundInstance = null;
    @Nullable public static EndermanStaticSoundInstance staticSoundInstance = null;

    public static void register(HudElement element) {
        Objects.requireNonNull(element);
        HUD_ELEMENTS.add(element);
	}

    @Override
    public void onInitializeClient() {

        Reflection.initialize(
                EnderscapeClientNetworking.class,
                EnderscapeParticleProviders.class,
                EnderscapeModelLayers.class,
                EnderscapeEntityRenderers.class,
                EnderscapeBlockRenderLayerMap.class,
                EnderscapeBlockColorProviders.class,
                EnderscapeHudElements.class
        );

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> resetTemporaryData());
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> resetTemporaryData());

        BlockEntityRenderers.register(EnderscapeBlockEntities.MAGNIA_SPROUT, MagniaSproutRenderer::new);

        TooltipComponentCallback.EVENT.register((component) -> component instanceof FueledToolComponent tool ? new FueledToolTooltip(tool.stack()) : null);

        ClampedItemPropertyFunction shieldFunction = (stack, level, user, i) -> user != null && user.isUsingItem() && user.getUseItem() == stack ? 1.0F : 0.0F;
        ItemProperties.register(EnderscapeItems.END_STONE_RUBBLE_SHIELD, ResourceLocation.withDefaultNamespace("blocking"), shieldFunction);
        ItemProperties.register(EnderscapeItems.MIRESTONE_RUBBLE_SHIELD, ResourceLocation.withDefaultNamespace("blocking"), shieldFunction);
        ItemProperties.register(EnderscapeItems.VERADITE_RUBBLE_SHIELD, ResourceLocation.withDefaultNamespace("blocking"), shieldFunction);
        ItemProperties.register(EnderscapeItems.KURODITE_RUBBLE_SHIELD, ResourceLocation.withDefaultNamespace("blocking"), shieldFunction);

        ItemProperties.register(EnderscapeItems.MAGNIA_ATTRACTOR, Enderscape.id("enabled"), (stack, level, user, i) -> Enabled.get(stack) && FueledTool.fuelExceedsCost(new ItemStackContext(stack, level, user)) ? 1 : 0);
    }

    private void resetTemporaryData() {
        lodestoneTeleportationOverlayTexture = Optional.empty();
        lodestoneTeleportationVignetteTexture = Optional.empty();
        structureMusic = Optional.empty();
        stareTicks = 0;
        lodestoneTeleportationTicks = 0;
        stareSoundInstance = null;
        staticSoundInstance = null;
    }
}