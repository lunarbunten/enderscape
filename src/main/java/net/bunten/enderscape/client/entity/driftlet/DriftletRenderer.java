package net.bunten.enderscape.client.entity.driftlet;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.registry.EnderscapeEntityRenderData;
import net.bunten.enderscape.entity.drifter.Driftlet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class DriftletRenderer extends MobRenderer<Driftlet, LivingEntityRenderState, DriftletModel> {
    public DriftletRenderer(Context context) {
        super(context, new DriftletModel(EnderscapeEntityRenderData.DRIFTLET.bakeLayer(context)), 0.9F);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    protected int getBlockLightLevel(Driftlet mob, BlockPos pos) {
        return Math.max(3, super.getBlockLightLevel(mob, pos));
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState mob) {
        return Enderscape.id("textures/entity/drifter/driftlet.png");
    }

    @Override
    protected RenderType getRenderType(LivingEntityRenderState mob, boolean showBody, boolean translucent, boolean showOutline) {
        return showBody ? RenderTypes.entityTranslucent(getTextureLocation(mob)) : super.getRenderType(mob, showBody, translucent, showOutline);
    }
}