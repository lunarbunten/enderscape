package net.bunten.enderscape.client;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.entity.drifter.DrifterModel;
import net.bunten.enderscape.entity.drifter.DrifterRenderer;
import net.bunten.enderscape.entity.driftlet.DriftletModel;
import net.bunten.enderscape.entity.driftlet.DriftletRenderer;
import net.bunten.enderscape.entity.rubblemite.RubblemiteModel;
import net.bunten.enderscape.entity.rubblemite.RubblemiteRenderer;
import net.bunten.enderscape.interfaces.LayerMapped;
import net.bunten.enderscape.registry.EnderscapeEntities;
import net.bunten.enderscape.registry.EnderscapeParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;

public class EnderscapeClient implements ClientModInitializer {

    public static final EntityModelLayer DRIFTER = register(EnderscapeEntities.DRIFTER);
    public static final EntityModelLayer DRIFTLET = register(EnderscapeEntities.DRIFTLET);
    public static final EntityModelLayer RUBBLEMITE = register(EnderscapeEntities.RUBBLEMITE);

    private static EntityModelLayer register(EntityType<?> type) {
        String id = Registry.ENTITY_TYPE.getId(type).getPath();
        return new EntityModelLayer(Enderscape.id(id), id);
    }

    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(EnderscapeEntities.DRIFTER, DrifterRenderer::new);
        EntityRendererRegistry.register(EnderscapeEntities.DRIFTLET, DriftletRenderer::new);
        EntityRendererRegistry.register(EnderscapeEntities.RUBBLEMITE, RubblemiteRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(DRIFTER, DrifterModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(DRIFTLET, DriftletModel::getTexturedModelData);
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

        EnderscapeParticles.init();
        MirrorHud.init();
    }
}