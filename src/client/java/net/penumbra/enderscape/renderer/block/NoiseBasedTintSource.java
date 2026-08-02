package net.penumbra.enderscape.renderer.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.penumbra.enderscape.util.OpenSimplexNoise;

@Environment(EnvType.CLIENT)
public class NoiseBasedTintSource implements BlockTintSource {

    private final double colorInterpolation;
    private final double noisePositonScale;
    private final int noiseHeightScale;
    private final int color1;
    private final int color2;

    private final OpenSimplexNoise simplexNoise;

    public NoiseBasedTintSource(long seed, double colorInterpolation, double noisePositonScale, int noiseHeightScale, int color1, int color2) {
        this.colorInterpolation = colorInterpolation;
        this.noisePositonScale = noisePositonScale;
        this.noiseHeightScale = noiseHeightScale;
        this.color1 = color1;
        this.color2 = color2;

        simplexNoise = new OpenSimplexNoise(seed);
    }

    @Override
    public int color(BlockState state) {
        return ARGB.average(color1, color2);
    }

    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        double value = (simplexNoise.eval(pos.getX() * noisePositonScale, pos.getY() * noisePositonScale, pos.getZ() * noisePositonScale) * noiseHeightScale) - (noiseHeightScale);
        double interpolated = Mth.clamp((value + colorInterpolation) / (colorInterpolation / 2), 0.0, 1.0);

        return lerpColor(interpolated, color1, color2);
    }

    private int lerpColor(double interpolated, int color1, int color2) {
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int r = Mth.floor(Mth.lerp(interpolated, r1, r2));
        int g = Mth.floor(Mth.lerp(interpolated, g1, g2));
        int b = Mth.floor(Mth.lerp(interpolated, b1, b2));

        return (255 << 24) | (r << 16) | (g << 8) | b;
    }
}