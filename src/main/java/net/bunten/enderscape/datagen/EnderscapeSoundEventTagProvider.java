package net.bunten.enderscape.datagen;

import net.bunten.enderscape.registry.EnderscapeMusic;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;

import java.util.concurrent.CompletableFuture;

import static net.bunten.enderscape.registry.tag.EnderscapeSoundEventTags.AMBIENCE_REPLACEABLE_BY_ENDERSCAPE;
import static net.bunten.enderscape.registry.tag.EnderscapeSoundEventTags.STRUCTURE_MUSIC;
import static net.minecraft.sounds.SoundEvents.*;

public class EnderscapeSoundEventTagProvider extends FabricTagsProvider<SoundEvent> {

    public EnderscapeSoundEventTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, Registries.SOUND_EVENT, future);
    }

    protected TagAppender<SoundEvent> tag(TagKey<SoundEvent> key) {
        return TagAppender.forBuilder(getOrCreateRawBuilder(key));
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(AMBIENCE_REPLACEABLE_BY_ENDERSCAPE).add(
                AMBIENT_CAVE.key(),
                AMBIENT_WARPED_FOREST_ADDITIONS.key(),
                AMBIENT_WARPED_FOREST_LOOP.key(),
                AMBIENT_WARPED_FOREST_MOOD.key()
        );

        tag(STRUCTURE_MUSIC).add(
                EnderscapeMusic.STRUCTURE_END_CITY.sound().unwrapKey().get(),
                EnderscapeMusic.STRUCTURE_STRONGHOLD.sound().unwrapKey().get()
        );
    }
}