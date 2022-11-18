package net.bunten.enderscape.config.configs;

import net.bunten.enderscape.config.ConfigKeys;
import net.bunten.enderscape.config.basic.BasicServerConfig;

public class LootConfig extends BasicServerConfig {
    public LootConfig() {
        super(ConfigKeys.LOOT);
    }

    public static final ConfigToken<Boolean> END_CITY_TOOLS = ConfigToken.Boolean(
            true,
            "end_city_tools",
            ConfigKeys.LOOT);

    public static final ConfigToken<Boolean> END_CITY_FOOD = ConfigToken.Boolean(
            true,
            "end_city_food",
            ConfigKeys.LOOT);

    public static final ConfigToken<Boolean> END_CITY_ENCHANTMENTS = ConfigToken.Boolean(
            true,
            "end_city_enchantments",
            ConfigKeys.LOOT);

    public static final ConfigToken<Boolean> END_CITY_MUSIC_DISCS = ConfigToken.Boolean(
            true,
            "end_city_music_discs",
            ConfigKeys.LOOT);

    public static final ConfigToken<Boolean> NETHER_FORTRESS_ACCOMMODATIONS = ConfigToken.Boolean(
            true,
            "nether_fortress_accommodations",
            ConfigKeys.LOOT);

    public boolean addEndCityFood() {
        return get(END_CITY_FOOD);
    }

    public boolean addEndCityTools() {
        return get(END_CITY_TOOLS);
    }

    public boolean addEndCityEnchantments() {
        return get(END_CITY_ENCHANTMENTS);
    }

    public boolean addEndCityMusicDiscs() {
        return get(END_CITY_MUSIC_DISCS);
    }

    public boolean addNetherFortressAccommodations() {
        return get(NETHER_FORTRESS_ACCOMMODATIONS);
    }
}