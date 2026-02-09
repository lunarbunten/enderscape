package net.bunten.enderscape.datagen;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static net.bunten.enderscape.registry.tag.EnderscapeSoundEventTags.AMBIENCE_REPLACEABLE_BY_ENDERSCAPE;
import static net.minecraft.sounds.SoundEvents.*;

public class EnderscapeSoundEventTagProvider extends TagsProvider<SoundEvent> {

    public EnderscapeSoundEventTagProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), Registries.SOUND_EVENT, event.getLookupProvider(), Enderscape.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        tag(AMBIENCE_REPLACEABLE_BY_ENDERSCAPE).add(
                AMBIENT_CAVE.key(),
                AMBIENT_WARPED_FOREST_ADDITIONS.key(),
                AMBIENT_WARPED_FOREST_LOOP.key(),
                AMBIENT_WARPED_FOREST_MOOD.key()
        );
    }
}