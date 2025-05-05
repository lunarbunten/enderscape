package net.bunten.enderscape.client.entity.drifter;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

@Environment(EnvType.CLIENT)
public class DrifterRenderState extends LivingEntityRenderState {
    boolean leakingJelly;
}
