package net.penumbra.enderscape.mixin.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
import net.penumbra.enderscape.renderer.ItemEntityTint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(ItemFeatureRenderer.Submit.class)
public class ItemSubmitMixin implements ItemEntityTint {

    @Unique
    private int Enderscape$itemTintColor;

    @Override
    public void setColor(int value) {
        Enderscape$itemTintColor = value;
    }

    @Override
    public int color() {
        return Enderscape$itemTintColor;
    }
}