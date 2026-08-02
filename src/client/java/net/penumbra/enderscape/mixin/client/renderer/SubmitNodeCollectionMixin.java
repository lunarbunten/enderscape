package net.penumbra.enderscape.mixin.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.world.item.ItemDisplayContext;
import net.penumbra.enderscape.manager.ClientsideEntityManager;
import net.penumbra.enderscape.renderer.ItemEntityTint;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;

@Mixin(SubmitNodeCollection.class)
public abstract class SubmitNodeCollectionMixin implements ItemEntityTint {

    @Unique
    private int Enderscape$itemTintColor = ItemEntityTint.NONE;

    @Override
    public void setColor(int value) {
        Enderscape$itemTintColor = value;
    }

    @Override
    public int color() {
        return Enderscape$itemTintColor;
    }

    @Unique
    private static final List<String> IGNORED_RENDER_TYPES = List.of(
            "armor_cutout_no_cull",
            "armor_entity_glint",
            "armor_translucent",
            "energy_swirl",
            "entity_glint",
            "entity_shadow",
            "eyes",
            "glint",
            "glint_translucent"
    );

    @Unique
    private RenderType type;

    @Inject(method = "submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V", at = @At(value = "HEAD"))
    private void Enderscape$cacheRenderType(Model<? super Object> model, Object state, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, int tintedColor, @Nullable TextureAtlasSprite sprite, int outlineColor, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay, CallbackInfo info) {
        this.type = renderType;
    }

    @WrapOperation(method = "submitItem", at = @At(value = "NEW", target = "(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/world/item/ItemDisplayContext;III[ILjava/util/List;Lnet/minecraft/client/renderer/item/ItemStackRenderState$FoilType;)Lnet/minecraft/client/renderer/feature/ItemFeatureRenderer$Submit;"))
    private ItemFeatureRenderer.Submit Enderscape$stampItemTint(PoseStack.Pose pose, ItemDisplayContext context, int light, int overlay, int outline, int[] layers, List<BakedQuad> quads, ItemStackRenderState.FoilType foil, Operation<ItemFeatureRenderer.Submit> original) {
        ItemFeatureRenderer.Submit submit = original.call(pose, context, light, overlay, outline, layers, quads, foil);

        ItemEntityTint tint = (ItemEntityTint) (Object) submit;
        tint.setColor(Enderscape$itemTintColor);

        return submit;
    }

    @ModifyArgs(
            method = "submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$Submit;<init>(Lnet/minecraft/client/renderer/rendertype/RenderType;Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/client/model/Model;Ljava/lang/Object;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lcom/mojang/blaze3d/vertex/PoseStack$Pose;)V")
    )
    private void Enderscape$changeModelTint(Args args) {
        if (!IGNORED_RENDER_TYPES.contains(type.name)) {
            if (args.get(3) instanceof EntityRenderState state) {
                args.set(6, ClientsideEntityManager.getTintColor(state, args.get(6)));
            }
        }
    }
}