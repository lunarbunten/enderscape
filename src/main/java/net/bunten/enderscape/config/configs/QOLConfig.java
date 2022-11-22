package net.bunten.enderscape.config.configs;

import net.bunten.enderscape.config.ConfigKeys;
import net.bunten.enderscape.config.basic.BasicServerConfig;

public class QOLConfig extends BasicServerConfig {
    public QOLConfig() {
        super(ConfigKeys.QOL);
    }

    public static final ConfigToken<Boolean> PLAY_ELYTRA_OPEN_SOUND = ConfigToken.Boolean(
            true,
            "play_elytra_open_sound",
            ConfigKeys.QOL);

    public static final ConfigToken<Boolean> ENDER_PEARLS_STACK_TO_64 = ConfigToken.Boolean(
            true,
            "ender_pearls_stack_to_64",
            ConfigKeys.QOL);

    public static final ConfigToken<Boolean> TRIDENT_RETURNS_IN_VOID = ConfigToken.Boolean(
            true,
            "trident_returns_in_void",
            ConfigKeys.QOL);

    public boolean playElytraOpenSound() {
        return get(PLAY_ELYTRA_OPEN_SOUND);
    }

    public boolean shouldEnderPearlsStackTo64() {
        return get(ENDER_PEARLS_STACK_TO_64);
    }

    public boolean shouldTridentsReturnInVoid() {
        return get(TRIDENT_RETURNS_IN_VOID);
    }
}