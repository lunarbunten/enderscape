package net.penumbra.enderscape.references;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxSong;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.registry.sound.EnderscapeJukeboxSongs;

public class EnderscapeItemIds {
    public static final ResourceKey<Item> DRIFTER_SPAWN_EGG = ofSpawnEgg(EnderscapeEntityIds.DRIFTER);
    public static final ResourceKey<Item> RUBBLEMITE_SPAWN_EGG = ofSpawnEgg(EnderscapeEntityIds.RUBBLEMITE);
    public static final ResourceKey<Item> RUSTLE_SPAWN_EGG = ofSpawnEgg(EnderscapeEntityIds.RUSTLE);

    public static final ResourceKey<Item> RUSTLE_BUCKET = of("rustle_bucket");
    public static final ResourceKey<Item> VOID_LACHRYMA_BUCKET = of("void_lachryma_bucket");

    public static final ResourceKey<Item> CHORUS_CAKE_ROLL = of("chorus_cake_roll");

    public static final ResourceKey<Item> BLINKLIGHT = of("blinklight");

    public static final ResourceKey<Item> DRIFT_JELLY_BOTTLE = of("drift_jelly_bottle");

    public static final ResourceKey<Item> PURUBERRY = of("puruberry");

    public static final ResourceKey<Item> MURUBLIGHT_BRACKET = of("murublight_bracket");

    public static final ResourceKey<Item> VOID_TORCH = of("void_torch");

    public static final ResourceKey<Item> END_CITY_KEY = of("end_city_key");
    public static final ResourceKey<Item> RUBBLE_CHITIN = of("rubble_chitin");
    public static final ResourceKey<Item> RUSTLE_SILK = of("rustle_silk");
    public static final ResourceKey<Item> NEBULITE = of("nebulite");
    public static final ResourceKey<Item> NEBULITE_SHARDS = of("nebulite_shards");
    public static final ResourceKey<Item> RAW_SHADOLINE = of("raw_shadoline");
    public static final ResourceKey<Item> SHADOLINE_INGOT = of("shadoline_ingot");
    public static final ResourceKey<Item> SHADOLINE_NUGGET = of("shadoline_nugget");

    public static final ResourceKey<Item> RUBBLE_SHIELD = of("rubble_shield");

    public static final ResourceKey<Item> DRIFT_LEGGINGS = of("drift_leggings");

    public static final ResourceKey<Item> SHADOLINE_HELMET = of("shadoline_helmet");
    public static final ResourceKey<Item> SHADOLINE_CHESTPLATE = of("shadoline_chestplate");
    public static final ResourceKey<Item> SHADOLINE_LEGGINGS = of("shadoline_leggings");
    public static final ResourceKey<Item> SHADOLINE_BOOTS = of("shadoline_boots");

    public static final ResourceKey<Item> DAGGER = of("dagger");

    public static final ResourceKey<Item> MAGNIA_ATTRACTOR = of("magnia_attractor");

    public static final ResourceKey<Item> MIRROR = of("mirror");

    public static final ResourceKey<Item> CRESCENT_BANNER_PATTERN = of("crescent_banner_pattern");

    public static final ResourceKey<Item> STASIS_ARMOR_TRIM_SMITHING_TEMPLATE = of("stasis_armor_trim_smithing_template");

    public static final ResourceKey<Item> MUSIC_DISC_GLARE = ofMusicDisc(EnderscapeJukeboxSongs.GLARE);
    public static final ResourceKey<Item> MUSIC_DISC_BLISS = ofMusicDisc(EnderscapeJukeboxSongs.BLISS);
    public static final ResourceKey<Item> MUSIC_DISC_DECAY = ofMusicDisc(EnderscapeJukeboxSongs.DECAY);

    public static final ResourceKey<Item> HEALING = of("healing");

    private static ResourceKey<Item> ofSpawnEgg(ResourceKey<EntityType<?>> type) {
        return of(type.identifier().getPath() + "_spawn_egg");
    }

    private static ResourceKey<Item> ofMusicDisc(ResourceKey<JukeboxSong> song) {
        return of("music_disc_" + song.identifier().getPath());
    }

    private static ResourceKey<Item> of(String name) {
        return ResourceKey.create(Registries.ITEM, Enderscape.id(name));
    }
}