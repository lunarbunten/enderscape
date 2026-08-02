package net.penumbra.enderscape.registry;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.entity.rubblemite.RubblemiteVariant;
import net.penumbra.enderscape.sound.MagniaRadioSong;
import net.penumbra.enderscape.sound.StructureMusic;

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