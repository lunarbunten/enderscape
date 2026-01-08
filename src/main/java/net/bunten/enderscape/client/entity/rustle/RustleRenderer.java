package net.bunten.enderscape.client.entity.rustle;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.entity.drifter.DrifterModel;
import net.bunten.enderscape.client.registry.EnderscapeModelLayers;
import net.bunten.enderscape.entity.drifter.Drifter;
import net.bunten.enderscape.entity.rustle.Rustle;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RustleRenderer extends MobRenderer<Rustle, EntityModel<Rustle>> {

    private final EntityModel<Rustle> adultModel;
    private final EntityModel<Rustle> babyModel;

    public static final ResourceLocation ADULT_TEXTURE = Enderscape.id("textures/entity/rustle/rustle.png");
    public static final ResourceLocation BABY_TEXTURE = Enderscape.id("textures/entity/rustle/baby.png");

    public RustleRenderer(EntityRendererProvider.Context context) {
        super(context, new RustleModel(context.bakeLayer(EnderscapeModelLayers.RUSTLE)), 0.4F);
        this.adultModel = model;
        this.babyModel = new BabyRustleModel(context.bakeLayer(EnderscapeModelLayers.BABY_RUSTLE));
    }

    @Override
    public ResourceLocation getTextureLocation(Rustle mob) {
        return mob.isBaby() ? BABY_TEXTURE : ADULT_TEXTURE;
    }

    @Override
    public void render(Rustle mob, float f, float g, PoseStack pose, MultiBufferSource source, int i) {
        model = mob.isBaby() ? babyModel : adultModel;
        super.render(mob, f, g, pose, source, i);
    }
}