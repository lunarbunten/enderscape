package net.bunten.enderscape.client.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class EnderscapeEntityRenderData {

    public static final ModelLayerLocation RUBBLEMITE = registerModelLayer("rubblemite");

    public static final ModelLayerLocation DRIFTER = registerModelLayer("drifter");
    public static final ModelLayerLocation DRIFTLET = registerModelLayer("driftlet");

    public static final ModelLayerLocation RUSTLE = registerModelLayer("rustle");
    public static final ModelLayerLocation BABY_RUSTLE = registerModelLayer("baby_rustle");

    private static ModelLayerLocation registerModelLayer(String name) {
        return new ModelLayerLocation(Enderscape.id(name), "main");
    }
}