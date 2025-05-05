package net.bunten.enderscape.util;

import net.bunten.enderscape.biome.util.SkyParameters;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

import java.awt.*;
import java.util.function.Function;

public record RGBA(int color, float alpha) {

    public static int darkenColor(int color, float factor) {
        Color original = new Color(color);

        int r = (int) (original.getRed() * factor);
        int g = (int) (original.getGreen() * factor);
        int b = (int) (original.getBlue() * factor);

        return (r << 16) | (g << 8) | b;
    }

    public static final double[] GAUSSIAN_SAMPLE_KERNEL = generateGaussianKernel(2);

    public static float sampleFloat(BiomeManager manager, Vec3 pos, Function<SkyParameters, Float> provided, float defaults) {
        return sampleFloat(pos, (x, y, z) -> SkyParameters.getSkyParametersFor(manager.getNoiseBiomeAtQuart(x, y, z)).map(provided).orElse(defaults));
    }

    public static Vector4f sampleVector4f(BiomeManager manager, Vec3 pos, Function<SkyParameters, RGBA> provided, RGBA defaults) {
        return sampleVector4f(pos, (x, y, z) -> {
            RGBA rgba = SkyParameters.getSkyParametersFor(manager.getNoiseBiomeAtQuart(x, y, z)).map(provided).orElse(defaults);
            Vec3 color = Vec3.fromRGB24(rgba.color());
            return new Vector4f((float) color.x, (float) color.y, (float) color.z, rgba.alpha());
        });
    }

    public static double[] generateGaussianKernel(int radius) {
        int size = radius * 2 + 1;
        double[] kernel = new double[size];
        double sigma = radius / 2.0;
        double sum = 0.0;

        for (int i = 0; i < size; i++) {
            double x = i - radius;
            kernel[i] = Math.exp(-0.5 * (x * x) / (sigma * sigma));
            sum += kernel[i];
        }

        for (int i = 0; i < size; i++) kernel[i] /= sum;

        return kernel;
    }

    public static float sampleFloat(Vec3 vec3, FloatFetcher fetcher) {
        int i = Mth.floor(vec3.x());
        int j = Mth.floor(vec3.y());
        int k = Mth.floor(vec3.z());
        double d = vec3.x() - i;
        double e = vec3.y() - j;
        double f = vec3.z() - k;
        double g = 0.0;
        float result = 0.0F;

        int kernelSize = GAUSSIAN_SAMPLE_KERNEL.length;
        int radius = kernelSize / 2;

        for (int l = 0; l < kernelSize - 1; l++) {
            double h = Mth.lerp(d, GAUSSIAN_SAMPLE_KERNEL[l + 1], GAUSSIAN_SAMPLE_KERNEL[l]);
            int m = i - radius + l;

            for (int n = 0; n < kernelSize - 1; n++) {
                double o = Mth.lerp(e, GAUSSIAN_SAMPLE_KERNEL[n + 1], GAUSSIAN_SAMPLE_KERNEL[n]);
                int p = j - radius + n;

                for (int q = 0; q < kernelSize - 1; q++) {
                    double r = Mth.lerp(f, GAUSSIAN_SAMPLE_KERNEL[q + 1], GAUSSIAN_SAMPLE_KERNEL[q]);
                    int s = k - radius + q;
                    double t = h * o * r;
                    g += t;

                    result += fetcher.fetch(m, p, s) * ((float) t);
                }
            }
        }

        result *= (1.0F / (float) g);

        return result;
    }

    public static Vector4f sampleVector4f(Vec3 vec3, Vec4Fetcher fetcher) {
        int i = Mth.floor(vec3.x());
        int j = Mth.floor(vec3.y());
        int k = Mth.floor(vec3.z());
        double d = vec3.x() - i;
        double e = vec3.y() - j;
        double f = vec3.z() - k;
        double g = 0.0;
        Vector4f result = new Vector4f(0, 0, 0, 0);

        int kernelSize = GAUSSIAN_SAMPLE_KERNEL.length;
        int radius = kernelSize / 2;

        for (int l = 0; l < kernelSize - 1; l++) {
            double h = Mth.lerp(d, GAUSSIAN_SAMPLE_KERNEL[l + 1], GAUSSIAN_SAMPLE_KERNEL[l]);
            int m = i - radius + l;

            for (int n = 0; n < kernelSize - 1; n++) {
                double o = Mth.lerp(e, GAUSSIAN_SAMPLE_KERNEL[n + 1], GAUSSIAN_SAMPLE_KERNEL[n]);
                int p = j - radius + n;

                for (int q = 0; q < kernelSize - 1; q++) {
                    double r = Mth.lerp(f, GAUSSIAN_SAMPLE_KERNEL[q + 1], GAUSSIAN_SAMPLE_KERNEL[q]);
                    int s = k - radius + q;
                    double t = h * o * r;
                    g += t;
                    result.add(fetcher.fetch(m, p, s).mul((float) t));
                }
            }
        }

        result.mul(1.0F / (float) g);
        return result;
    }

    @FunctionalInterface
    public interface FloatFetcher {
        float fetch(int x, int y, int z);
    }

    @FunctionalInterface
    public interface Vec4Fetcher {
        Vector4f fetch(int x, int y, int z);
    }
}
