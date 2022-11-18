package net.bunten.enderscape.client;

import net.bunten.enderscape.Enderscape;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry.TexturedModelDataProvider;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

@Environment(EnvType.CLIENT)
public class EntityRenderData<T extends Entity> {
    private final EntityType<T> type;
    private final EntityRendererProvider<T> renderer;
    private final TexturedModelDataProvider dataProvider;
    private final ModelLayerLocation modelLayerLocation;

    public EntityRenderData(EntityType<T> type, EntityRendererProvider<T> renderer, TexturedModelDataProvider dataProvider) {
        this.type = type;
        this.renderer = renderer;
        this.dataProvider = dataProvider;
        this.modelLayerLocation = registerModelLayer(type);

        register();
    }

    private void register() {
        EntityRendererRegistry.register(type, renderer);
        EntityModelLayerRegistry.registerModelLayer(modelLayerLocation, dataProvider);
    }

    private static ModelLayerLocation registerModelLayer(EntityType<?> type) {
        String id = Registry.ENTITY_TYPE.getKey(type).getPath();
        return new ModelLayerLocation(Enderscape.id(id), id);
    }

    public static <T extends Entity> EntityRenderData<T> create(EntityType<T> type, EntityRendererProvider<T> renderer, TexturedModelDataProvider dataProvider) {
        return new EntityRenderData<>(type, renderer, dataProvider);
    }

    public EntityType<T> getEntityType() {
        return type;
    }

    public EntityRendererProvider<T> getRenderer() {
        return renderer;
    }

    public TexturedModelDataProvider getTexturedDataProvider() {
        return dataProvider;
    }

    public ModelLayerLocation getModelLayerLocation() {
        return modelLayerLocation;
    }

    public ModelPart bakeLayer(Context context) {
        return context.bakeLayer(getModelLayerLocation());
    }
}