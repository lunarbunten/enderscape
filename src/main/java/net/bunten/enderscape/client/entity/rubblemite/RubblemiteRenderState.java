package net.bunten.enderscape.client.entity.rubblemite;

import net.bunten.enderscape.entity.rubblemite.RubblemiteVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

@Environment(EnvType.CLIENT)
public class RubblemiteRenderState extends LivingEntityRenderState {
    RubblemiteVariant variant = RubblemiteVariant.END_STONE;
    boolean isDashing;
    boolean insideShell;
}
