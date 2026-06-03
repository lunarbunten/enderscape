package net.bunten.enderscape.registry.ids;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.registry.EnderscapeEntities;
import net.bunten.enderscape.registry.EnderscapeJukeboxSongs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.block.Block;


public class EnderscapeItemIds {
    public static final ResourceKey<Item> DRIFTER_SPAWN_EGG = spawnEgg(EnderscapeEntities.DRIFTER);
    public static final ResourceKey<Item> RUBBLEMITE_SPAWN_EGG = spawnEgg(EnderscapeEntities.RUBBLEMITE);
    public static final ResourceKey<Item> RUSTLE_SPAWN_EGG = spawnEgg(EnderscapeEntities.RUSTLE);

    public static final ResourceKey<Item> RUSTLE_BUCKET = key("rustle_bucket");

    public static final ResourceKey<Item> VEILED_HANGING_SIGN_ITEM = key("veiled_hanging_sign");
    public static final ResourceKey<Item> VEILED_SIGN_ITEM = key("veiled_sign");
    public static final ResourceKey<Item> CELESTIAL_HANGING_SIGN_ITEM = key("celestial_hanging_sign");
    public static final ResourceKey<Item> CELESTIAL_SIGN_ITEM = key("celestial_sign");
    public static final ResourceKey<Item> MURUBLIGHT_HANGING_SIGN_ITEM = key("murublight_hanging_sign");
    public static final ResourceKey<Item> MURUBLIGHT_SIGN_ITEM = key("murublight_sign");

    public static final ResourceKey<Item> VEILED_SHELF_ITEM = keyBlock(EnderscapeBlockIds.VEILED_SHELF);
    public static final ResourceKey<Item> CELESTIAL_SHELF_ITEM = keyBlock(EnderscapeBlockIds.CELESTIAL_SHELF);
    public static final ResourceKey<Item> MURUBLIGHT_SHELF_ITEM = keyBlock(EnderscapeBlockIds.MURUBLIGHT_SHELF);

    public static final ResourceKey<Item> CHORUS_CAKE_ROLL_ITEM = key("chorus_cake_roll");

    public static final ResourceKey<Item> BLINKLIGHT = key("blinklight");
    public static final ResourceKey<Item> DRIFT_JELLY_BOTTLE = key("drift_jelly_bottle");
    public static final ResourceKey<Item> FLANGER_BERRY = key("flanger_berry");
    public static final ResourceKey<Item> MURUBLIGHT_BRACKET_ITEM = key("murublight_bracket");

    public static final ResourceKey<Item> VOID_TORCH_ITEM = key("void_torch");

    public static final ResourceKey<Item> END_CITY_KEY = key("end_city_key");
    public static final ResourceKey<Item> RUBBLE_CHITIN = key("rubble_chitin");
    public static final ResourceKey<Item> NEBULITE = key("nebulite");
    public static final ResourceKey<Item> NEBULITE_SHARDS = key("nebulite_shards");
    public static final ResourceKey<Item> RAW_SHADOLINE = key("raw_shadoline");
    public static final ResourceKey<Item> SHADOLINE_INGOT = key("shadoline_ingot");
    public static final ResourceKey<Item> SHADOLINE_NUGGET = key("shadoline_nugget");

    public static final ResourceKey<Item> END_STONE_RUBBLE_SHIELD = key("end_stone_rubble_shield");
    public static final ResourceKey<Item> VERADITE_RUBBLE_SHIELD = key("veradite_rubble_shield");
    public static final ResourceKey<Item> MIRESTONE_RUBBLE_SHIELD = key("mirestone_rubble_shield");
    public static final ResourceKey<Item> KURODITE_RUBBLE_SHIELD = key("kurodite_rubble_shield");

    public static final ResourceKey<Item> DRIFT_LEGGINGS = key("drift_leggings");

    public static final ResourceKey<Item> SHADOLINE_HELMET = key("shadoline_helmet");
    public static final ResourceKey<Item> SHADOLINE_CHESTPLATE = key("shadoline_chestplate");
    public static final ResourceKey<Item> SHADOLINE_LEGGINGS = key("shadoline_leggings");
    public static final ResourceKey<Item> SHADOLINE_BOOTS = key("shadoline_boots");

    public static final ResourceKey<Item> DAGGER = key("dagger");

    public static final ResourceKey<Item> MAGNIA_ATTRACTOR = key("magnia_attractor");

    public static final ResourceKey<Item> CRACKED_MIRROR = key("cracked_mirror");

    public static final ResourceKey<Item> MIRROR = key("mirror");

    public static final ResourceKey<Item> CRESCENT_BANNER_PATTERN = key("crescent_banner_pattern");

    public static final ResourceKey<Item> STASIS_ARMOR_TRIM_SMITHING_TEMPLATE = key("stasis_armor_trim_smithing_template");

    public static final ResourceKey<Item> MUSIC_DISC_GLARE = musicDisc(EnderscapeJukeboxSongs.GLARE);
    public static final ResourceKey<Item> MUSIC_DISC_BLISS = musicDisc(EnderscapeJukeboxSongs.BLISS);
    public static final ResourceKey<Item> MUSIC_DISC_DECAY = musicDisc(EnderscapeJukeboxSongs.DECAY);

    public static final ResourceKey<Item> HEALING = key("healing");

    private static ResourceKey<Item> spawnEgg(EntityType<?> type) {
        String name = type + "_spawn_egg";
        name = name.replace("entity.enderscape.", "");
        return key(name);
    }

    private static ResourceKey<Item> musicDisc(ResourceKey<JukeboxSong> song) {
        return key("music_disc_" + song.identifier().getPath());
    }

    private static ResourceKey<Item> key(String name) {
        return ResourceKey.create(Registries.ITEM, Enderscape.id(name));
    }

    public static ResourceKey<Item> keyBlock(ResourceKey<Block> resourceKey) {
        return ResourceKey.create(Registries.ITEM, resourceKey.identifier());
    }
}