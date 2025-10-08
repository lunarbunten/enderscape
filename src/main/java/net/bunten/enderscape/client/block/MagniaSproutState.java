package net.bunten.enderscape.client.block;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class MagniaSproutState extends BlockEntityRenderState {
    AABB range;
    Vec3 color;
    float intensity;
}
