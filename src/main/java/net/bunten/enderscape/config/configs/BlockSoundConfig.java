package net.bunten.enderscape.config.configs;

import net.bunten.enderscape.config.ConfigKeys;
import net.bunten.enderscape.config.basic.BasicServerConfig;

public class BlockSoundConfig extends BasicServerConfig {
    public BlockSoundConfig() {
        super(ConfigKeys.BLOCK_SOUNDS);
    }

    public static final ConfigToken<Boolean> USE_CHORUS_SOUNDS = ConfigToken.Boolean(
            true,
            "use_chorus_sounds",
            ConfigKeys.BLOCK_SOUNDS);

    public static final ConfigToken<Boolean> USE_PURPUR_SOUNDS = ConfigToken.Boolean(
            true,
            "use_purpur_sounds",
            ConfigKeys.BLOCK_SOUNDS);

    public static final ConfigToken<Boolean> USE_END_ROD_SOUNDS = ConfigToken.Boolean(
            true,
            "use_end_rod_sounds",
            ConfigKeys.BLOCK_SOUNDS);

    public static final ConfigToken<Boolean> USE_END_STONE_SOUNDS = ConfigToken.Boolean(
            true,
            "use_end_stone_sounds",
            ConfigKeys.BLOCK_SOUNDS);

    public static final ConfigToken<Boolean> USE_END_STONE_BRICK_SOUNDS = ConfigToken.Boolean(
            true,
            "use_end_stone_brick_sounds",
            ConfigKeys.BLOCK_SOUNDS);

    public static final ConfigToken<Boolean> USE_SHULKER_SOUNDS = ConfigToken.Boolean(
            true,
            "use_shulker_sounds",
            ConfigKeys.BLOCK_SOUNDS);

    public boolean useChorusSounds() {
        return get(USE_CHORUS_SOUNDS);
    }

    public boolean usePurpurSounds() {
        return get(USE_PURPUR_SOUNDS);
    }

    public boolean useEndRodSounds() {
        return get(USE_END_ROD_SOUNDS);
    }

    public boolean useEndStoneSounds() {
        return get(USE_END_STONE_SOUNDS);
    }
    
    public boolean useEndStoneBrickSounds() {
        return get(USE_END_STONE_BRICK_SOUNDS);
    }
    
    public boolean useShulkerSounds() {
        return get(USE_SHULKER_SOUNDS);
    }
}