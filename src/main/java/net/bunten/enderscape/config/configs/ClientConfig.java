package net.bunten.enderscape.config.configs;

import org.betterx.bclib.config.ConfigUI;

import net.bunten.enderscape.config.ConfigKeys;
import net.bunten.enderscape.config.basic.BasicClientConfig;

public class ClientConfig extends BasicClientConfig {
    public ClientConfig() {
        super(ConfigKeys.CLIENT);
    }

    public static final ConfigToken<Boolean> MODIFY_LIGHTMAP = ConfigToken.Boolean(
            true,
            "modify_lightmap",
            ConfigKeys.CLIENT);

    public static final ConfigToken<Boolean> MODIFY_SKYBOX = ConfigToken.Boolean(
            true,
            "modify_skybox",
            ConfigKeys.CLIENT);

    @ConfigUI(leftPadding = 8)
    public static final ConfigToken<Boolean> OVERRIDE_VANILLA_SKY_TEXTURE = DependendConfigToken.Boolean(
            true,
            "override_vanilla_sky_texture",
            ConfigKeys.CLIENT,
            (config) -> config.get(MODIFY_SKYBOX));

    public boolean modifySkybox() {
        return get(MODIFY_SKYBOX);
    }

    public boolean overrideVanillaSkyTexture() {
        return get(OVERRIDE_VANILLA_SKY_TEXTURE);
    }

    public boolean modifyLightmap() {
        return get(MODIFY_LIGHTMAP);
    }

    public static final ConfigToken<Boolean> DISPLAY_MIRROR_TOOLTIP = ConfigToken.Boolean(
            true,
            "display_mirror_tooltip",
            ConfigKeys.CLIENT);

    @ConfigUI(leftPadding = 8)
    public static final ConfigToken<Boolean> DISPLAY_MIRROR_COORDINATES = DependendConfigToken.Boolean(
            false,
            "display_mirror_tooltip.coordinates",
            ConfigKeys.CLIENT,
            (config) -> config.get(DISPLAY_MIRROR_TOOLTIP));

    @ConfigUI(leftPadding = 8)
    public static final ConfigToken<Boolean> DISPLAY_MIRROR_DIMENSION = DependendConfigToken.Boolean(
            true,
            "display_mirror_tooltip.dimension",
            ConfigKeys.CLIENT,
            (config) -> config.get(DISPLAY_MIRROR_TOOLTIP));

    @ConfigUI(leftPadding = 8)
    public static final ConfigToken<Boolean> DISPLAY_MIRROR_DISTANCE = DependendConfigToken.Boolean(
            true,
            "display_mirror_tooltip.distance",
            ConfigKeys.CLIENT,
            (config) -> config.get(DISPLAY_MIRROR_TOOLTIP));

    @ConfigUI(leftPadding = 8)
    public static final ConfigToken<Boolean> SHIFT_FOR_MIRROR_TOOLTIP = DependendConfigToken.Boolean(
            true,
            "display_mirror_tooltip.shift_only",
            ConfigKeys.CLIENT,
            (config) -> config.get(DISPLAY_MIRROR_TOOLTIP));

    public boolean displayMirrorTooltip() {
        return get(DISPLAY_MIRROR_TOOLTIP);
    }

    public boolean displayMirrorCoordinates() {
        return get(DISPLAY_MIRROR_COORDINATES);
    }

    public boolean displayMirrorDimension() {
        return get(DISPLAY_MIRROR_DIMENSION);
    }

    public boolean displayMirrorDistance() {
        return get(DISPLAY_MIRROR_DISTANCE);
    }

    public boolean shiftForMirrorInfo() {
        return get(SHIFT_FOR_MIRROR_TOOLTIP);
    }

    public static final ConfigToken<Boolean> DISPLAY_MIRROR_OVERLAY = ConfigToken.Boolean(
            true,
            "mirror_overlay.display",
            ConfigKeys.CLIENT);

    @ConfigUI(leftPadding = 8, minValue = 0, maxValue = 50)
    public static final ConfigToken<Integer> MIRROR_OVERLAY_INTENSITY = ConfigToken.Int(
            50,
            "mirror_overlay.intensity",
            ConfigKeys.CLIENT);

    @ConfigUI(leftPadding = 8, minValue = 0, maxValue = 50)
    public static final ConfigToken<Integer> MIRROR_VIGNETTE_INTENSITY = ConfigToken.Int(
            50,
            "mirror_overlay.vignette_intensity",
            ConfigKeys.CLIENT);

    public boolean displayMirrorOverlay() {
        return get(DISPLAY_MIRROR_OVERLAY);
    }

    public int mirrorOverlayIntensity() {
        return get(MIRROR_OVERLAY_INTENSITY);
    }

    public int mirrorVignetteIntensity() {
        return get(MIRROR_VIGNETTE_INTENSITY);
    }

    public static final ConfigToken<Boolean> DISPLAY_CHARGED_HUD = ConfigToken.Boolean(
            true,
            "charged_hud.display",
            ConfigKeys.CLIENT);

    @ConfigUI(leftPadding = 8, minValue = -32, maxValue = 32)
    public static final ConfigToken<Integer> CHARGED_HUD_OFFSET = ConfigToken.Int(
            0,
            "charged_hud.y_offset",
            ConfigKeys.CLIENT);

    @ConfigUI(leftPadding = 8, minValue = 25, maxValue = 100)
    public static final ConfigToken<Integer> CHARGED_HUD_OPACITY = ConfigToken.Int(
            100,
            "charged_hud.opacity",
            ConfigKeys.CLIENT);

    public boolean displayChargedHud() {
        return get(DISPLAY_CHARGED_HUD);
    }

    public int chargedHudOffset() {
        return get(CHARGED_HUD_OFFSET);
    }

    public int chargedHudOpacity() {
        return get(CHARGED_HUD_OPACITY);
    }
}