package net.bunten.enderscape.client;

import java.util.ArrayList;
import java.util.List;

import org.betterx.bclib.integration.modmenu.ModMenu;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.blocks.NebuliteOrePackets;
import net.bunten.enderscape.client.hud.HudElement;
import net.bunten.enderscape.client.hud.HudElements;
import net.bunten.enderscape.config.ConfigMenu;
import net.bunten.enderscape.entity.drifter.Drifter;
import net.bunten.enderscape.entity.drifter.DrifterModel;
import net.bunten.enderscape.entity.drifter.DrifterRenderer;
import net.bunten.enderscape.entity.driftlet.Driftlet;
import net.bunten.enderscape.entity.driftlet.DriftletModel;
import net.bunten.enderscape.entity.driftlet.DriftletRenderer;
import net.bunten.enderscape.entity.rubblemite.Rubblemite;
import net.bunten.enderscape.entity.rubblemite.RubblemiteModel;
import net.bunten.enderscape.entity.rubblemite.RubblemiteRenderer;
import net.bunten.enderscape.interfaces.HasColorProvider;
import net.bunten.enderscape.interfaces.HasRenderType;
import net.bunten.enderscape.items.ChargedUsageContext;
import net.bunten.enderscape.items.MirrorPackets;
import net.bunten.enderscape.items.NebuliteChargedItem;
import net.bunten.enderscape.registry.EnderscapeEntities;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;

public class EnderscapeClient implements ClientModInitializer {

    public static final List<HudElement> HUD_ELEMENTS = new ArrayList<>();

    public static final EntityRenderData<Drifter> DRIFTER = EntityRenderData.create(EnderscapeEntities.DRIFTER, DrifterRenderer::new, DrifterModel::createLayer);
    public static final EntityRenderData<Driftlet> DRIFTLET = EntityRenderData.create(EnderscapeEntities.DRIFTLET, DriftletRenderer::new, DriftletModel::createLayer);
    public static final EntityRenderData<Rubblemite> RUBBLEMITE = EntityRenderData.create(EnderscapeEntities.RUBBLEMITE, RubblemiteRenderer::new, RubblemiteModel::createLayer);

    public static boolean playTransdimensionalSound = false;
    public static int postMirrorUseTicks;

    @Override
    public void onInitializeClient() {
        DimensionRenderingRegistry.registerSkyRenderer(Level.END, new EnderscapeSkybox());
        ModMenu.addModMenuScreen(Enderscape.MOD_ID, (parent) -> new ConfigMenu(parent));

        NebuliteOrePackets.initReceivers();
        MirrorPackets.initReceivers();

        EnderscapeParticles.initClient();
        HudElements.init();

        Registry.BLOCK.stream().filter(block -> block instanceof HasRenderType).forEach(block -> {
            BlockRenderLayerMap.INSTANCE.putBlock(block, HasRenderType.class.cast(block).getRenderType());
        });

        Registry.BLOCK.stream().filter(block -> block instanceof HasColorProvider).forEach(block -> {
            ColorProviderRegistry.BLOCK.register(HasColorProvider.class.cast(block).getColorProvider(), block);
        });

        Registry.ITEM.stream().filter((item) -> item instanceof NebuliteChargedItem).forEach(item -> {
            ItemProperties.register(item, Enderscape.id("usable"), (stack, level, user, i) -> {
                return NebuliteChargedItem.canUse(new ChargedUsageContext(stack, level, user)) ? 1 : 0;
            });

            ItemProperties.register(item, Enderscape.id("energy"), (stack, level, user, i) -> {
                float energy = NebuliteChargedItem.getEnergy(stack);
                float max = NebuliteChargedItem.class.cast(stack.getItem()).getMaximumEnergy(stack);
                return energy / max;
            });
        });
    }
}