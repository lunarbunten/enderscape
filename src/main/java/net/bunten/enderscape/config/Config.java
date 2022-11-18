package net.bunten.enderscape.config;

import java.util.ArrayList;
import java.util.List;

import org.betterx.bclib.config.NamedPathConfig;

import net.bunten.enderscape.config.configs.AmbienceConfig;
import net.bunten.enderscape.config.configs.BlockSoundConfig;
import net.bunten.enderscape.config.configs.ClientConfig;
import net.bunten.enderscape.config.configs.DebugConfig;
import net.bunten.enderscape.config.configs.LootConfig;
import net.bunten.enderscape.config.configs.QOLConfig;
import net.bunten.enderscape.config.configs.WorldConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class Config {

    public static final List<NamedPathConfig> CONFIGS = new ArrayList<>();

    @Environment(EnvType.CLIENT)
    public static final DebugConfig DEBUG = new DebugConfig();

    @Environment(EnvType.CLIENT)
    public static final ClientConfig CLIENT = new ClientConfig();
    
    public static final AmbienceConfig AMBIENCE = new AmbienceConfig();
    public static final BlockSoundConfig BLOCK_SOUNDS = new BlockSoundConfig();
    public static final LootConfig LOOT = new LootConfig();
    public static final QOLConfig QOL = new QOLConfig();
    public static final WorldConfig WORLD = new WorldConfig();

    public static void save() {
        CONFIGS.forEach((config) -> config.saveChanges());
    }
}