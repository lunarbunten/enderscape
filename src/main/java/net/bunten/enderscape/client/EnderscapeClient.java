package net.bunten.enderscape.client;

import com.google.common.reflect.Reflection;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.block.MagniaSproutRenderer;
import net.bunten.enderscape.client.entity.EndermanStaticSoundInstance;
import net.bunten.enderscape.client.entity.drifter.DrifterModel;
import net.bunten.enderscape.client.entity.drifter.DrifterRenderer;
import net.bunten.enderscape.client.entity.driftlet.DriftletModel;
import net.bunten.enderscape.client.entity.driftlet.DriftletRenderer;
import net.bunten.enderscape.client.entity.rubblemite.RubblemiteModel;
import net.bunten.enderscape.client.entity.rubblemite.RubblemiteRenderer;
import net.bunten.enderscape.client.entity.rustle.RustleModel;
import net.bunten.enderscape.client.entity.rustle.RustleRenderer;
import net.bunten.enderscape.client.hud.HudElement;
import net.bunten.enderscape.client.item.NebuliteToolTooltip;
import net.bunten.enderscape.client.registry.*;
import net.bunten.enderscape.item.MagniaAttractorItem;
import net.bunten.enderscape.item.NebuliteToolComponent;
import net.bunten.enderscape.item.NebuliteToolContext;
import net.bunten.enderscape.item.NebuliteToolItem;
import net.bunten.enderscape.registry.EnderscapeBlockEntities;
import net.bunten.enderscape.registry.EnderscapeEntities;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Mod(value = Enderscape.MOD_ID, dist = Dist.CLIENT)
public class EnderscapeClient {

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
    
    public EnderscapeClient(IEventBus modBus, ModContainer container) {
        Reflection.initialize(
                EnderscapeHudElements.class
        );

        container.registerExtensionPoint(IConfigScreenFactory.class, EnderscapeConfigMenu::buildMenu);
        
        modBus.addListener(EntityRenderersEvent.RegisterLayerDefinitions.class, event -> {
            event.registerLayerDefinition(EnderscapeEntityRenderData.DRIFTER, DrifterModel::createLayer);
            event.registerLayerDefinition(EnderscapeEntityRenderData.DRIFTLET, DriftletModel::createLayer);
            event.registerLayerDefinition(EnderscapeEntityRenderData.RUBBLEMITE, RubblemiteModel::createLayer);
            event.registerLayerDefinition(EnderscapeEntityRenderData.RUSTLE, RustleModel::createLayer);
        });
        
        modBus.addListener(EntityRenderersEvent.RegisterRenderers.class, event -> {
            event.registerEntityRenderer(EnderscapeEntities.DRIFTER.get(), DrifterRenderer::new);
            event.registerEntityRenderer(EnderscapeEntities.DRIFTLET.get(), DriftletRenderer::new);
            event.registerEntityRenderer(EnderscapeEntities.RUBBLEMITE.get(), RubblemiteRenderer::new);
            event.registerEntityRenderer(EnderscapeEntities.RUSTLE.get(), RustleRenderer::new);
        });
        
        modBus.register(EnderscapeParticleProviders.class);
        modBus.register(EnderscapeBlockRenderLayerMap.class);
        modBus.register(EnderscapeBlockColorProviders.class);

        modBus.addListener(EntityRenderersEvent.RegisterRenderers.class, event -> {
            event.registerBlockEntityRenderer(EnderscapeBlockEntities.MAGNIA_SPROUT.get(), MagniaSproutRenderer::new);
        });

        modBus.addListener(RegisterClientTooltipComponentFactoriesEvent.class, event -> {
            event.register(NebuliteToolComponent.class, tool -> new NebuliteToolTooltip(tool.stack()));
        });

        modBus.addListener(FMLCommonSetupEvent.class, event -> {
            event.enqueueWork(() -> {
                ClampedItemPropertyFunction shieldFunction = (stack, level, user, i) -> user != null && user.isUsingItem() && user.getUseItem() == stack ? 1.0F : 0.0F;
                ItemProperties.register(EnderscapeItems.END_STONE_RUBBLE_SHIELD.get(), ResourceLocation.withDefaultNamespace("blocking"), shieldFunction);
                ItemProperties.register(EnderscapeItems.MIRESTONE_RUBBLE_SHIELD.get(), ResourceLocation.withDefaultNamespace("blocking"), shieldFunction);
                ItemProperties.register(EnderscapeItems.VERADITE_RUBBLE_SHIELD.get(), ResourceLocation.withDefaultNamespace("blocking"), shieldFunction);
                ItemProperties.register(EnderscapeItems.KURODITE_RUBBLE_SHIELD.get(), ResourceLocation.withDefaultNamespace("blocking"), shieldFunction);

                ItemProperties.register(EnderscapeItems.MAGNIA_ATTRACTOR.get(), Enderscape.id("enabled"), (stack, level, user, i) -> MagniaAttractorItem.isEnabled(stack) && NebuliteToolItem.fuelExceedsCost(new NebuliteToolContext(stack, level, user)) ? 1 : 0);
            });
        });
        
        modBus.addListener(RegisterGuiLayersEvent.class, event -> {
            for (var element : HUD_ELEMENTS) {
                if (element.phase == HudElement.RenderPhase.BEFORE_HUD) {
                    event.registerBelowAll(element.id, element::render);
                } else {
                    event.registerAboveAll(element.id, element::render);
                }
            }
        });

        NeoForge.EVENT_BUS.addListener(ClientTickEvent.Pre.class, event -> {
            for (HudElement element : HUD_ELEMENTS) {
                element.tick();
            }
        });
    }
}