package net.bunten.enderscape.config.basic;

import net.bunten.enderscape.config.ConfigKeys;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

public abstract class BasicSyncedConfig extends BasicConfig {
    public BasicSyncedConfig(String category) {
        super(ConfigKeys.SYNCED, true, category);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public boolean showInUI(Minecraft client) {
        return true;
    }
}