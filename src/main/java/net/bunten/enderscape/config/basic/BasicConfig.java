package net.bunten.enderscape.config.basic;

import org.betterx.bclib.config.NamedPathConfig;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public abstract class BasicConfig extends NamedPathConfig {
    private String category;

    public BasicConfig(String group, String category) {
        super(Enderscape.MOD_ID, group);
        this.category = category;
        Config.CONFIGS.add(this);
    }

    public BasicConfig(String group, boolean autoSync, String category) {
        super(Enderscape.MOD_ID, group, autoSync);
        this.category = category;
        Config.CONFIGS.add(this);
    }

    public BasicConfig(String group, boolean autoSync, boolean diffContent, String category) {
        super(Enderscape.MOD_ID, group, autoSync, diffContent);
        this.category = category;
        Config.CONFIGS.add(this);
    }

    public MutableComponent getComponent() {
        return Component.translatable("menu." + Enderscape.MOD_ID + ".config." + category);
    }

    @Environment(EnvType.CLIENT)
    public abstract boolean showInUI(Minecraft client);
}