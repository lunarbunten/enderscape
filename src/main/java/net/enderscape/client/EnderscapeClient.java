package net.enderscape.client;

import net.enderscape.Enderscape;
import net.enderscape.entity.drifter.DrifterModel;
import net.enderscape.entity.drifter.DrifterRenderer;
import net.enderscape.entity.driftlet.DriftletModel;
import net.enderscape.entity.driftlet.DriftletRenderer;
import net.enderscape.entity.motu.MotuModel;
import net.enderscape.entity.motu.MotuRenderer;
import net.enderscape.entity.rubblemite.RubblemiteModel;
import net.enderscape.entity.rubblemite.RubblemiteRenderer;
import net.enderscape.interfaces.LayerMapped;
import net.enderscape.registry.EndEntities;
import net.enderscape.registry.EndParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EnderscapeClient implements ClientModInitializer {

    public static final Identifier ICONS_ID = Enderscape.id("textures/gui/icons.png");

    public static final EntityModelLayer DRIFTER = register(EndEntities.DRIFTER);
    public static final EntityModelLayer DRIFTLET = register(EndEntities.DRIFTLET);
    public static final EntityModelLayer MOTU = register(EndEntities.MOTU);
    public static final EntityModelLayer RUBBLEMITE = register(EndEntities.RUBBLEMITE);

    private static EntityModelLayer register(EntityType<?> type) {
        String id = Registry.ENTITY_TYPE.getId(type).getPath();
        return new EntityModelLayer(Enderscape.id(id), id);
    }

    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(EndEntities.DRIFTER, DrifterRenderer::new);
        EntityRendererRegistry.register(EndEntities.DRIFTLET, DriftletRenderer::new);
        EntityRendererRegistry.register(EndEntities.MOTU, MotuRenderer::new);
        EntityRendererRegistry.register(EndEntities.RUBBLEMITE, RubblemiteRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(DRIFTER, DrifterModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(DRIFTLET, DriftletModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(MOTU, MotuModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(RUBBLEMITE, RubblemiteModel::getTexturedModelData);

        for (Block block : Registry.BLOCK) {
            if (block instanceof LayerMapped mapped) {
                switch (mapped.getLayerType()) {
                    case CUTOUT -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
                    case CUTOUT_MIPPED -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutoutMipped());
                    case TRANSLUCENT -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getTranslucent());
                }
            }
        }

        EndParticles.init();
        GameHudRenderer.init();
    }
}