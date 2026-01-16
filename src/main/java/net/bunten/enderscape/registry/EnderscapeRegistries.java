package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.entity.rubblemite.RubblemiteVariant;
import net.bunten.enderscape.sound.MagniaRadioSong;
import net.bunten.enderscape.sound.StructureMusic;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class EnderscapeRegistries {

    public static final ResourceKey<Registry<MagniaRadioSong>> MAGNIA_RADIO_SONG = ResourceKey.createRegistryKey(Enderscape.id("magnia_radio_song"));
    public static final ResourceKey<Registry<RubblemiteVariant>> RUBBLEMITE_VARIANT = ResourceKey.createRegistryKey(Enderscape.id("rubblemite_variant"));
    public static final ResourceKey<Registry<StructureMusic>> STRUCTURE_MUSIC = ResourceKey.createRegistryKey(Enderscape.id("structure_music"));

    static {
        DynamicRegistries.registerSynced(MAGNIA_RADIO_SONG, MagniaRadioSong.DIRECT_CODEC);
        DynamicRegistries.registerSynced(RUBBLEMITE_VARIANT, RubblemiteVariant.DIRECT_CODEC, RubblemiteVariant.NETWORK_CODEC);
        DynamicRegistries.registerSynced(STRUCTURE_MUSIC, StructureMusic.DIRECT_CODEC);
    }
}