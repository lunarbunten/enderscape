package net.bunten.enderscape.client.entity.drifter;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.registry.EnderscapeEntityRenderData;
import net.bunten.enderscape.entity.drifter.Drifter;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class DrifterRenderer extends MobRenderer<Drifter, DrifterModel> {

    private final DrifterModel adultModel;
    private final DrifterModel babyModel;

    public static final ResourceLocation DRIFTER_TEXTURE = Enderscape.id("textures/entity/drifter/drifter.png");
    public static final ResourceLocation DRIFTLET_TEXTURE = Enderscape.id("textures/entity/drifter/driftlet.png");

    public DrifterRenderer(EntityRendererProvider.Context context) {
        super(context, new DrifterModel(context.bakeLayer(EnderscapeEntityRenderData.DRIFTER)), 1.0F);
        this.adultModel = model;
        this.babyModel = new DrifterModel(context.bakeLayer(EnderscapeEntityRenderData.DRIFTLET));
        addLayer(new DrifterJellyLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Drifter drifter) {
        return drifter.isBaby() ? DRIFTLET_TEXTURE : DRIFTER_TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(Drifter mob, BlockPos pos) {
        return Math.max(3, super.getBlockLightLevel(mob, pos));
    }

    @Override
    public void render(Drifter mob, float f, float g, PoseStack pose, MultiBufferSource source, int i) {
        model = mob.isBaby() ? babyModel : adultModel;
        super.render(mob, f, g, pose, source, i);
    }

    @Override
    protected RenderType getRenderType(Drifter mob, boolean showBody, boolean translucent, boolean showOutline) {
        return showBody ? RenderType.entityTranslucent(getTextureLocation(mob)) : super.getRenderType(mob, showBody, translucent, showOutline);
    }
}