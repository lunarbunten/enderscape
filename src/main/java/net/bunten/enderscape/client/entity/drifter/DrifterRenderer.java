package net.bunten.enderscape.client.entity.drifter;

import com.google.common.collect.ImmutableList;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.registry.EnderscapeModelLayers;
import net.bunten.enderscape.entity.drifter.Drifter;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.HitboxRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;

public class DrifterRenderer extends AgeableMobRenderer<Drifter, DrifterRenderState, DrifterModel> {

    public static final ResourceLocation DRIFTER_TEXTURE = Enderscape.id("textures/entity/drifter/drifter.png");
    public static final ResourceLocation DRIFTLET_TEXTURE = Enderscape.id("textures/entity/drifter/driftlet.png");

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
    protected void extractAdditionalHitboxes(Drifter mob, ImmutableList.Builder<HitboxRenderState> builder, float f) {
        super.extractAdditionalHitboxes(mob, builder, f);

        double d = -Mth.lerp(f, mob.xOld, mob.getX());
        double e = -Mth.lerp(f, mob.yOld, mob.getY());
        double g = -Mth.lerp(f, mob.zOld, mob.getZ());

        AABB aABB = mob.getBounceHitbox();
        HitboxRenderState state = new HitboxRenderState(
                aABB.minX - mob.getX(),
                aABB.minY - mob.getY(),
                aABB.minZ - mob.getZ(),
                aABB.maxX - mob.getX(),
                aABB.maxY - mob.getY(),
                aABB.maxZ - mob.getZ(),
                (float)(d + Mth.lerp(f, mob.xOld, mob.getX())),
                (float)(e + Mth.lerp(f, mob.yOld, mob.getY())),
                (float)(g + Mth.lerp(f, mob.zOld, mob.getZ())),
                0.25F,
                1.0F,
                0.0F
        );

        builder.add(state);
    }

    @Override
    public ResourceLocation getTextureLocation(DrifterRenderState state) {
        return state.isBaby ? DRIFTLET_TEXTURE : DRIFTER_TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(Drifter mob, BlockPos pos) {
        return Math.max(3, super.getBlockLightLevel(mob, pos));
    }

    @Override
    protected RenderType getRenderType(DrifterRenderState mob, boolean showBody, boolean translucent, boolean showOutline) {
        return showBody ? RenderType.entityTranslucent(getTextureLocation(mob)) : super.getRenderType(mob, showBody, translucent, showOutline);
    }
}