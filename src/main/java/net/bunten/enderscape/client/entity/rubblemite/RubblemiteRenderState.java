package net.bunten.enderscape.client.entity.rubblemite;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.entity.rubblemite.RubblemiteVariant;
import net.bunten.enderscape.registry.EnderscapeRubblemiteVariants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class RubblemiteRenderState extends LivingEntityRenderState {
    private static final ResourceLocation DEFAULT_TEXTURE = Enderscape.id("textures/entity/rubblemite/end_stone.png");
    public ResourceLocation texture = DEFAULT_TEXTURE;
    boolean isDashing;
    boolean insideShell;
}