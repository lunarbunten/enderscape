package net.penumbra.enderscape.mixin.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.world.item.ItemDisplayContext;
import net.penumbra.enderscape.renderer.ItemEntityTint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(SubmitNodeStorage.class)
public class SubmitNodeStorageMixin implements ItemEntityTint {

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

    @WrapOperation(method = "submitItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollection;submitItem(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/item/ItemDisplayContext;III[ILjava/util/List;Lnet/minecraft/client/renderer/item/ItemStackRenderState$FoilType;)V"))
    private void Enderscape$passItemTint(SubmitNodeCollection collection, PoseStack pose, ItemDisplayContext context, int light, int overlay, int outline, int[] layers, List<BakedQuad> quads, ItemStackRenderState.FoilType foil, Operation<Void> original) {
        ItemEntityTint target = (ItemEntityTint) collection;
        target.setColor(Enderscape$itemTintColor);

        original.call(collection, pose, context, light, overlay, outline, layers, quads, foil);
        target.setColor(ItemEntityTint.NONE);
    }
}