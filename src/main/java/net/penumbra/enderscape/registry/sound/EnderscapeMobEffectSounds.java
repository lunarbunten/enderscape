package net.penumbra.enderscape.registry.sound;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.penumbra.enderscape.Enderscape;

public class EnderscapeMobEffectSounds {

    public static final SoundEvent LOW_GRAVITY_APPLY = register("low_gravity.apply");

    private static SoundEvent register(String name) {
        return Enderscape.registerSoundEvent("mob_effect." + name);
    }

    private static Holder.Reference<SoundEvent> registerHolder(String name) {
        return Enderscape.registerSoundEventHolder("mob_effect." + name);
    }
}