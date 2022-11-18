package net.bunten.enderscape.config.configs;

import net.bunten.enderscape.config.ConfigKeys;
import net.bunten.enderscape.config.basic.BasicServerConfig;

public class AmbienceConfig extends BasicServerConfig {
    public AmbienceConfig() {
        super(ConfigKeys.AMBIENCE);
    }

    public static final ConfigToken<Boolean> MODIFY_DEFAULT_MUSIC = ConfigToken.Boolean(
            true,
            "modify_default_music",
            ConfigKeys.AMBIENCE);

    public static final ConfigToken<Boolean> MODIFY_DEFAULT_AMBIENCE = ConfigToken.Boolean(
            true,
            "modify_default_ambience",
            ConfigKeys.AMBIENCE);

    public static final ConfigToken<Boolean> MODIFY_DEFAULT_ADDITIONS = ConfigToken.Boolean(
            true,
            "modify_default_additions",
            ConfigKeys.AMBIENCE);

    public static final ConfigToken<Boolean> MODIFY_DEFAULT_MOOD = ConfigToken.Boolean(
            true,
            "modify_default_mood",
            ConfigKeys.AMBIENCE);

    public static final ConfigToken<Boolean> MODIFY_DEFAULT_PARTICLES = ConfigToken.Boolean(
            true,
            "modify_default_particles",
            ConfigKeys.AMBIENCE);

    public static final ConfigToken<Boolean> MODIFY_DEFAULT_SKY_COLOR = ConfigToken.Boolean(
            true,
            "modify_default_sky_color",
            ConfigKeys.AMBIENCE);

    public static final ConfigToken<Boolean> MODIFY_DEFAULT_FOG_COLOR = ConfigToken.Boolean(
            true,
            "modify_default_fog_color",
            ConfigKeys.AMBIENCE);

    public boolean modifyDefaultMusic() {
        return get(MODIFY_DEFAULT_MUSIC);
    }

    public boolean modifyDefaultAmbience() {
        return get(MODIFY_DEFAULT_AMBIENCE);
    }

    public boolean modifyDefaultAdditions() {
        return get(MODIFY_DEFAULT_ADDITIONS);
    }

    public boolean modifyDefaultMood() {
        return get(MODIFY_DEFAULT_MOOD);
    }

    public boolean modifyDefaultParticles() {
        return get(MODIFY_DEFAULT_PARTICLES);
    }

    public boolean modifyDefaultSkyColor() {
        return get(MODIFY_DEFAULT_SKY_COLOR);
    }

    public boolean modifyDefaultFogColor() {
        return get(MODIFY_DEFAULT_FOG_COLOR);
    }

    public static final ConfigToken<Boolean> CHORUS_FLOWER_HUMMING = ConfigToken.Boolean(
            true,
            "chorus_flower_humming",
            ConfigKeys.AMBIENCE);

    public static final ConfigToken<Boolean> CHORUS_FLOWER_POLLEN = ConfigToken.Boolean(
            true,
            "chorus_flower_pollen",
            ConfigKeys.AMBIENCE);

    public boolean chorusFlowerHumming() {
        return get(CHORUS_FLOWER_HUMMING);
    }

    public boolean chorusFlowerPollen() {
        return get(CHORUS_FLOWER_POLLEN);
    }
}