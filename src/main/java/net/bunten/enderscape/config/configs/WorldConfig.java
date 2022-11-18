package net.bunten.enderscape.config.configs;

import org.betterx.bclib.config.ConfigUI;

import net.bunten.enderscape.config.ConfigKeys;
import net.bunten.enderscape.config.basic.BasicServerConfig;

public class WorldConfig extends BasicServerConfig {
    public WorldConfig() {
        super(ConfigKeys.WORLD);
    }

    public static final ConfigToken<Boolean> USE_SUBSURFACE_BIOMES = ConfigToken.Boolean(
        true,
        "use_subsurface_biomes",
        ConfigKeys.WORLD);

    public static final ConfigToken<Boolean> USE_SKY_BIOMES = ConfigToken.Boolean(
            true,
            "use_sky_biomes",
            ConfigKeys.WORLD);

    @ConfigUI(leftPadding = 0, minValue = -64, maxValue = 256)
    public static final ConfigToken<Integer> SUBSURFACE_BIOME_HEIGHT = ConfigToken.Int(
            20,
            "subsurface_biome_height",
            ConfigKeys.WORLD);

    @ConfigUI(leftPadding = 0, minValue = -64, maxValue = 256)
    public static final ConfigToken<Integer> SKY_BIOME_HEIGHT = ConfigToken.Int(
            128,
            "sky_biome_height",
            ConfigKeys.WORLD);

    public boolean useSkyBiomes() {
        return get(USE_SKY_BIOMES);
    }

    public boolean useSubsurfaceBiomes() {
        return get(USE_SUBSURFACE_BIOMES);
    }

    public int getSubsurfaceBiomeHeight() {
        return get(SUBSURFACE_BIOME_HEIGHT);
    }

    public int getSkyBiomeHeight() {
        return get(SKY_BIOME_HEIGHT);
    }
}