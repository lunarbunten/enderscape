package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.JukeboxSong;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.EnderscapeJukeboxSongs.JUKEBOX_SONGS;

public class EnderscapeJukeboxSongProvider extends FabricDynamicRegistryProvider {

    public EnderscapeJukeboxSongProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        JUKEBOX_SONGS.forEach((key) -> add(provider, entries, key));
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<JukeboxSong> key) {
        entries.add(key, provider.lookupOrThrow(Registries.JUKEBOX_SONG).getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "Jukebox Song";
    }
}
