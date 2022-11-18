package net.bunten.enderscape.client;

import com.mojang.math.Vector3f;

import net.bunten.enderscape.world.biomes.EnderscapeBiome;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class SkyInfo {

    public static final float[] nebula = new float[4];
    public static final float[] stars = new float[4];

    public static Vector3f lightColor = new Vector3f(Vec3.fromRGB24(EnderscapeBiome.DEFAULT_LIGHT_COLOR));
    public static float lightIntensity = EnderscapeBiome.DEFAULT_LIGHT_INTENSITY;
    
}