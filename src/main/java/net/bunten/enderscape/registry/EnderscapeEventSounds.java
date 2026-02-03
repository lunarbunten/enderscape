package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;

public class EnderscapeEventSounds {

    public static final SoundEvent APPLY_EFFECT_LOW_GRAVITY = register("mob_effect.low_gravity");

    private static SoundEvent register(String name) {
        return Enderscape.registerSoundEvent("event." + name);
    }
}