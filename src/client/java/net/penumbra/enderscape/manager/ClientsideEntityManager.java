package net.penumbra.enderscape.manager;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.util.Ease;
import net.minecraft.util.Mth;
import net.penumbra.enderscape.renderer.StunTicksPercentage;
import net.penumbra.enderscape.renderer.VoidTicksPercentage;

public class ClientsideEntityManager {
    public static <S extends EntityRenderState> int getTintColor(S state, int color) {
        if (state instanceof StunTicksPercentage percentage) {
            color = mixColor(Ease.outQuint(percentage.getStunTicksPercentage()), color, 0x7F7F7F);
        }

        if (state instanceof VoidTicksPercentage percentage) {
            color = mixColor(percentage.getVoidTicksPercentage(), color, ARGB.linearLerp(
                    Mth.sin(state.ageInTicks / 3),
                    0x4A3456,
                    0x543860
            ));
        }

        return color;
    }

    private static int mixColor(float alpha, int color, int multiplier) {
        return ARGB.linearLerp(
                alpha,
                color,
                ARGB.color(255, ARGB.multiply(color, multiplier))
        );
    }
}