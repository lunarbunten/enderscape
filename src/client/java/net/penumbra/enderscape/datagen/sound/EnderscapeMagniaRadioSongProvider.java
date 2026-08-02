package net.penumbra.enderscape.datagen.sound;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.penumbra.enderscape.registry.EnderscapeRegistries;
import net.penumbra.enderscape.sound.MagniaRadioSong;

import java.util.concurrent.CompletableFuture;

import static net.penumbra.enderscape.registry.sound.EnderscapeMagniaRadioSongs.MAGNIA_RADIO_SONGS;

public class EnderscapeMagniaRadioSongProvider extends FabricDynamicRegistryProvider {

    public EnderscapeMagniaRadioSongProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        MAGNIA_RADIO_SONGS.forEach((key) -> add(provider, entries, key));
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<MagniaRadioSong> key) {
        entries.add(key, provider.lookupOrThrow(EnderscapeRegistries.MAGNIA_RADIO_SONG).getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Magnia Radio Song";
    }
}
