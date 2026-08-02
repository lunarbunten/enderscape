package net.penumbra.enderscape.mixin.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.penumbra.enderscape.manager.ClientsideEntityManager;
import net.penumbra.enderscape.renderer.ItemEntityTint;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;

@Mixin(SubmitNodeCollection.class)
public abstract class SubmitNodeCollectionMixin implements ItemEntityTint {

    @Shadow
    @Final
    private List<SubmitNodeStorage.ItemSubmit> itemSubmits;

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

    @Inject(method = "submitItem", at = @At("RETURN"))
    private void Enderscape$stampItemTint(CallbackInfo info) {
        if (Enderscape$itemTintColor != ItemEntityTint.NONE) {
            ItemEntityTint tint = (ItemEntityTint) (Object) itemSubmits.getLast();
            tint.setColor(Enderscape$itemTintColor);
        }
    }

    @ModifyArgs(
            method = "submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/SubmitNodeStorage$ModelSubmit;<init>(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/client/model/Model;Ljava/lang/Object;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V")
    )
    private void Enderscape$changeModelTint(Args args) {
        if (!IGNORED_RENDER_TYPES.contains(type.name)) {
            if (args.get(2) instanceof EntityRenderState state) {
                args.set(5, ClientsideEntityManager.getTintColor(state, args.get(5)));
            }
        }
    }
}