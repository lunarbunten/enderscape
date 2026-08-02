package net.penumbra.enderscape.registry.renderer;

import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.renderer.entity.drifter.DrifterModel;
import net.penumbra.enderscape.renderer.entity.enderman.ImprovedEndermanModel;
import net.penumbra.enderscape.renderer.entity.rubblemite.RubblemiteModel;
import net.penumbra.enderscape.renderer.entity.rustle.BabyRustleModel;
import net.penumbra.enderscape.renderer.entity.rustle.RustleModel;

public class EnderscapeModelLayers {

    public static final ModelLayerLocation DRIFTER = register("drifter", DrifterModel::createDrifterLayer);
    public static final ModelLayerLocation DRIFTLET = register("driftlet", DrifterModel::createDriftletLayer);
    public static final ModelLayerLocation ENDERMAN = register("enderman", ImprovedEndermanModel::createLayer);
    public static final ModelLayerLocation RUBBLEMITE = register("rubblemite", RubblemiteModel::createLayer);
    public static final ModelLayerLocation RUSTLE = register("rustle", RustleModel::createLayer);
    public static final ModelLayerLocation RUSTLE_BABY = register("rustle_baby", BabyRustleModel::createLayer);

    private static ModelLayerLocation register(String name, ModelLayerRegistry.TexturedLayerDefinitionProvider provider) {
        return register(name, "main", provider);
    }

    private static ModelLayerLocation register(String name, String layer, ModelLayerRegistry.TexturedLayerDefinitionProvider provider) {
        ModelLayerLocation location = new ModelLayerLocation(Enderscape.id(name), layer);
        ModelLayerRegistry.registerModelLayer(location, provider);
        return location;
    }
}