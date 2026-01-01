package net.bunten.enderscape.client.entity.drifter;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.registry.EnderscapeEntityRenderData;
import net.bunten.enderscape.entity.drifter.Drifter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class DrifterRenderer extends MobRenderer<Drifter, DrifterRenderState, DrifterModel> {

    public DrifterRenderer(Context context) {
        super(context, new DrifterModel(EnderscapeEntityRenderData.DRIFTER.bakeLayer(context)), 1);
        addLayer(new DrifterJellyLayer(this));
    }

    @Override
    public DrifterRenderState createRenderState() {
        return new DrifterRenderState();
    }

    @Override
    protected int getBlockLightLevel(Drifter mob, BlockPos pos) {
        return Math.max(3, super.getBlockLightLevel(mob, pos));
    }

    @Override
    public void extractRenderState(Drifter mob, DrifterRenderState state, float f) {
        super.extractRenderState(mob, state, f);
        state.leakingJelly = mob.isDrippingJelly();
    }

    @Override
    public Identifier getTextureLocation(DrifterRenderState state) {
        return Enderscape.id("textures/entity/drifter/drifter.png");
    }
}