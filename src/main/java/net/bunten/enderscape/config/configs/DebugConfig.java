package net.bunten.enderscape.config.configs;

import org.betterx.bclib.config.ConfigUI;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.config.ConfigKeys;
import net.bunten.enderscape.config.basic.BasicClientConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

public class DebugConfig extends BasicClientConfig {
    public DebugConfig() {
        super(ConfigKeys.DEBUG);
    }

    public static final ConfigToken<Boolean> DISPLAY_DEBUG_HUD = ConfigToken.Boolean(
            false,
            "display_debug_hud",
            ConfigKeys.DEBUG);

    @ConfigUI(leftPadding = 8)
    public static final ConfigToken<Boolean> CLIENT_INFO = ConfigToken.Boolean(
            true,
            "client_info",
            ConfigKeys.DEBUG);

    @ConfigUI(leftPadding = 8)
    public static final ConfigToken<Boolean> MUSIC_INFO = ConfigToken.Boolean(
            true,
            "music_info",
            ConfigKeys.DEBUG);

    @ConfigUI(leftPadding = 8)
    public static final ConfigToken<Boolean> PLAYER_INFO = ConfigToken.Boolean(
            true,
            "player_info",
            ConfigKeys.DEBUG);

    public boolean displayDebugHud() {
        return get(DISPLAY_DEBUG_HUD);
    }

    public boolean clientInfo() {
        return get(CLIENT_INFO);
    }

    public boolean musicInfo() {
        return get(MUSIC_INFO);
    }

    public boolean playerInfo() {
        return get(PLAYER_INFO);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean showInUI(Minecraft client) {
        return Enderscape.IS_DEBUG;
    }
}