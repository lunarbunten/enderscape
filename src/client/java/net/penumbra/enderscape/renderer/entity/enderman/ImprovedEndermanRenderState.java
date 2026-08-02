package net.penumbra.enderscape.renderer.entity.enderman;

import net.minecraft.client.renderer.entity.state.EndermanRenderState;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.monster.EnderMan;
import net.penumbra.enderscape.entity.enderman.ImprovedEnderman;

public interface ImprovedEndermanRenderState {
    static void extract(EnderMan entity, EndermanRenderState state) {
        if (state instanceof ImprovedEndermanRenderState improved) {

            if (entity instanceof ImprovedEnderman enderman) {
                improved.attackAnimationState().copyFrom(enderman.attackAnimationState());
            }
        }
    }

    AnimationState attackAnimationState();
}