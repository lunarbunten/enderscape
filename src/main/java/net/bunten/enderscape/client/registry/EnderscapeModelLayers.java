package net.bunten.enderscape.client.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.entity.drifter.DrifterModel;
import net.bunten.enderscape.client.entity.rubblemite.RubblemiteModel;
import net.bunten.enderscape.client.entity.rustle.BabyRustleModel;
import net.bunten.enderscape.client.entity.rustle.RustleModel;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class EnderscapeModelLayers {

    public static final ModelLayerLocation RUBBLEMITE = registerModelLayer("rubblemite");

    public static final ModelLayerLocation DRIFTER = registerModelLayer("drifter");
    public static final ModelLayerLocation DRIFTLET = registerModelLayer("driftlet");

    public static final ModelLayerLocation RUSTLE = registerModelLayer("rustle");
    public static final ModelLayerLocation BABY_RUSTLE = registerModelLayer("baby_rustle");

    static {
        ModelLayerRegistry.registerModelLayer(RUBBLEMITE, RubblemiteModel::createLayer);

        ModelLayerRegistry.registerModelLayer(DRIFTER, DrifterModel::createDrifterLayer);
        ModelLayerRegistry.registerModelLayer(DRIFTLET, DrifterModel::createDriftletLayer);

        ModelLayerRegistry.registerModelLayer(RUSTLE, RustleModel::createLayer);
        ModelLayerRegistry.registerModelLayer(BABY_RUSTLE, BabyRustleModel::createLayer);
    }

    private static ModelLayerLocation registerModelLayer(String name) {
        return new ModelLayerLocation(Enderscape.id(name), "main");
    }
}