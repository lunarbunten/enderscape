package net.bunten.enderscape.registry.tag;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;

public class EnderscapeSoundEventTags {
    public static final TagKey<SoundEvent> AMBIENCE_REPLACEABLE_BY_ENDERSCAPE = register("ambience_replaceable_by_enderscape");

    private static TagKey<SoundEvent> register(String name) {
        return TagKey.create(Registries.SOUND_EVENT, Enderscape.id(name));
    }
}