package net.penumbra.enderscape;

import com.google.common.reflect.Reflection;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ClientTooltipComponentCallback;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.world.item.ItemStack;
import net.penumbra.enderscape.gui.tooltip.FueledToolTooltip;
import net.penumbra.enderscape.item.tooltip.FueledToolComponent;
import net.penumbra.enderscape.manager.ClientsideLodestoneTeleportationManager;
import net.penumbra.enderscape.manager.ClientsideVariables;
import net.penumbra.enderscape.registry.block.EnderscapeBlockEntities;
import net.penumbra.enderscape.registry.block.EnderscapeBlockTintSources;
import net.penumbra.enderscape.registry.block.EnderscapeFluids;
import net.penumbra.enderscape.registry.entity.EnderscapeEntityRenderers;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import net.penumbra.enderscape.registry.gui.EnderscapeDebugScreenEntries;
import net.penumbra.enderscape.registry.gui.EnderscapeHudElements;
import net.penumbra.enderscape.registry.networking.EnderscapeClientNetworking;
import net.penumbra.enderscape.registry.particle.EnderscapeParticleProviders;
import net.penumbra.enderscape.registry.renderer.EnderscapeModelLayers;
import net.penumbra.enderscape.registry.renderer.EnderscapeRenderPipelines;
import net.penumbra.enderscape.renderer.block.EndHavenCoreRenderer;
import net.penumbra.enderscape.renderer.block.MagniaSproutRenderer;

@Environment(EnvType.CLIENT)
public class EnderscapeClient implements ClientModInitializer {

    private static final ClientsideVariables CLIENTSIDE_VARIABLES = new ClientsideVariables();

    public static ClientsideVariables clientsideVariables() {
        return CLIENTSIDE_VARIABLES;
    }

    @Override
    public void onInitializeClient() {

        Reflection.initialize(
                EnderscapeClientNetworking.class,
                EnderscapeDebugScreenEntries.class,
                EnderscapeRenderPipelines.class,
                EnderscapeParticleProviders.class,
                EnderscapeModelLayers.class,
                EnderscapeEntityRenderers.class,
                EnderscapeBlockTintSources.class,
                EnderscapeHudElements.class
        );

        BlockEntityRenderers.register(EnderscapeBlockEntities.MAGNIA_SPROUT, MagniaSproutRenderer::new);
        BlockEntityRenderers.register(EnderscapeBlockEntities.END_HAVEN_CORE, EndHavenCoreRenderer::new);

        ClientTooltipComponentCallback.EVENT.register((component) -> component instanceof FueledToolComponent(ItemStack stack) ? new FueledToolTooltip(stack) : null);
        ItemTooltipCallback.EVENT.register(ClientsideLodestoneTeleportationManager::appendTooltip);

        FluidRenderingRegistry.register(
                EnderscapeFluids.VOID_LACHRYMA,
                EnderscapeFluids.FLOWING_VOID_LACHRYMA,
                new FluidModel.Unbaked(
                        new Material(Enderscape.id("block/void_lachryma_still")),
                        new Material(Enderscape.id("block/void_lachryma_flow")),
                        null,
                        EnderscapeBlockTintSources.voidLachryma()
                )
        );

        ClientTickEvents.START_CLIENT_TICK.register(minecraft -> {
            LocalPlayer player = minecraft.player;

            if (EnderscapeMobEffects.isStunned(player) && minecraft.screen instanceof AbstractContainerScreen<?>) {
                player.closeContainer();
            }
        });
    }
}