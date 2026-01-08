package net.bunten.enderscape.client.entity.drifter;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.registry.EnderscapeModelLayers;
import net.bunten.enderscape.entity.drifter.Drifter;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;

public class DrifterRenderer extends AgeableMobRenderer<Drifter, DrifterRenderState, DrifterModel> {

    public static final Identifier DRIFTER_TEXTURE = Enderscape.id("textures/entity/drifter/drifter.png");
    public static final Identifier DRIFTLET_TEXTURE = Enderscape.id("textures/entity/drifter/driftlet.png");

    public DrifterRenderer(EntityRendererProvider.Context context) {
        super(context, new DrifterModel(context.bakeLayer(EnderscapeModelLayers.DRIFTER)), new DrifterModel(context.bakeLayer(EnderscapeModelLayers.DRIFTLET)), 1.0F);
        addLayer(new DrifterJellyLayer(this));
    }

    @Override
    public DrifterRenderState createRenderState() {
        return new DrifterRenderState();
    }

    @Override
    public void extractRenderState(Drifter mob, DrifterRenderState state, float f) {
        super.extractRenderState(mob, state, f);
        state.leakingJelly = mob.isDrippingJelly();
    }

    @Override
    public Identifier getTextureLocation(DrifterRenderState state) {
        return state.isBaby ? DRIFTLET_TEXTURE : DRIFTER_TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(Drifter mob, BlockPos pos) {
        return Math.max(3, super.getBlockLightLevel(mob, pos));
    }

    @Override
    protected RenderType getRenderType(DrifterRenderState mob, boolean showBody, boolean translucent, boolean showOutline) {
        return showBody ? RenderTypes.entityTranslucent(getTextureLocation(mob)) : super.getRenderType(mob, showBody, translucent, showOutline);
    }
}