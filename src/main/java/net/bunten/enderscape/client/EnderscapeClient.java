package net.bunten.enderscape.client;

import com.google.common.reflect.Reflection;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.block.MagniaSproutRenderer;
import net.bunten.enderscape.client.hud.HudElement;
import net.bunten.enderscape.client.item.NebuliteToolTooltip;
import net.bunten.enderscape.client.registry.*;
import net.bunten.enderscape.client.sound.EndermanStaticSoundInstance;
import net.bunten.enderscape.item.MagniaAttractorItem;
import net.bunten.enderscape.item.NebuliteToolComponent;
import net.bunten.enderscape.item.NebuliteToolContext;
import net.bunten.enderscape.item.NebuliteToolItem;
import net.bunten.enderscape.registry.EnderscapeBlockEntities;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
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

    public static int postMirrorUseTicks;

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
                EnderscapeEntityRenderData.class,
                EnderscapeBlockRenderLayerMap.class,
                EnderscapeBlockColorProviders.class,
                EnderscapeHudElements.class
        );

        BlockEntityRenderers.register(EnderscapeBlockEntities.MAGNIA_SPROUT, MagniaSproutRenderer::new);

        TooltipComponentCallback.EVENT.register((component) -> component instanceof NebuliteToolComponent tool ? new NebuliteToolTooltip(tool.stack()) : null);

        ClampedItemPropertyFunction shieldFunction = (stack, level, user, i) -> user != null && user.isUsingItem() && user.getUseItem() == stack ? 1.0F : 0.0F;
        ItemProperties.register(EnderscapeItems.END_STONE_RUBBLE_SHIELD, ResourceLocation.withDefaultNamespace("blocking"), shieldFunction);
        ItemProperties.register(EnderscapeItems.MIRESTONE_RUBBLE_SHIELD, ResourceLocation.withDefaultNamespace("blocking"), shieldFunction);
        ItemProperties.register(EnderscapeItems.VERADITE_RUBBLE_SHIELD, ResourceLocation.withDefaultNamespace("blocking"), shieldFunction);
        ItemProperties.register(EnderscapeItems.KURODITE_RUBBLE_SHIELD, ResourceLocation.withDefaultNamespace("blocking"), shieldFunction);

        ItemProperties.register(EnderscapeItems.MAGNIA_ATTRACTOR, Enderscape.id("enabled"), (stack, level, user, i) -> MagniaAttractorItem.isEnabled(stack) && NebuliteToolItem.fuelExceedsCost(new NebuliteToolContext(stack, level, user)) ? 1 : 0);
    }
}