package net.bunten.enderscape.registry;

import net.bunten.enderscape.biome.util.SkyParameters;
import net.bunten.enderscape.sound.MagniaRadioSong;
import net.bunten.enderscape.sound.StructureMusic;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class EnderscapeRegistries {

    public static final ResourceKey<Registry<MagniaRadioSong>> MAGNIA_RADIO_SONG = ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace("enderscape/magnia_radio_song"));
    public static final ResourceKey<Registry<SkyParameters>> SKY_PARAMETERS = ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace("enderscape/end_sky_parameters"));
    public static final ResourceKey<Registry<StructureMusic>> STRUCTURE_MUSIC = ResourceKey.createRegistryKey(ResourceLocation.withDefaultNamespace("enderscape/structure_music"));

    static {
        DynamicRegistries.registerSynced(MAGNIA_RADIO_SONG, MagniaRadioSong.DIRECT_CODEC);
        DynamicRegistries.registerSynced(SKY_PARAMETERS, SkyParameters.DIRECT_CODEC);
        DynamicRegistries.registerSynced(STRUCTURE_MUSIC, StructureMusic.DIRECT_CODEC);
    }
}