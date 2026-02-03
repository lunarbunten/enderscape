package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.biome.util.BiomeParameters;
import net.bunten.enderscape.sound.MagniaRadioSong;
import net.bunten.enderscape.sound.StructureMusic;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(modid = Enderscape.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class EnderscapeRegistries {

    public static final ResourceKey<Registry<BiomeParameters>> BIOME_PARAMETERS_KEY = ResourceKey.createRegistryKey(Enderscape.id("biome_parameters"));
    public static final ResourceKey<Registry<MagniaRadioSong>> MAGNIA_RADIO_SONG = ResourceKey.createRegistryKey(Enderscape.id("magnia_radio_song"));
    public static final ResourceKey<Registry<StructureMusic>> STRUCTURE_MUSIC = ResourceKey.createRegistryKey(Enderscape.id("structure_music"));

    @SubscribeEvent
    public static void registerDynamicRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(BIOME_PARAMETERS_KEY, BiomeParameters.DIRECT_CODEC, BiomeParameters.DIRECT_CODEC);
        event.dataPackRegistry(MAGNIA_RADIO_SONG, MagniaRadioSong.DIRECT_CODEC, MagniaRadioSong.DIRECT_CODEC);
        event.dataPackRegistry(STRUCTURE_MUSIC, StructureMusic.DIRECT_CODEC, StructureMusic.DIRECT_CODEC);
    }
}