package net.bunten.enderscape.client.entity.drifter;

import com.google.common.collect.ImmutableList;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.registry.EnderscapeEntityRenderData;
import net.bunten.enderscape.entity.drifter.Drifter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.HitboxRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;

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
        return Enderscape.id("textures/entity/drifter/drifter.png");
    }
}