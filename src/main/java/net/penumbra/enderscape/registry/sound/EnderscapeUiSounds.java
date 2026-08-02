package net.penumbra.enderscape.registry.sound;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.penumbra.enderscape.Enderscape;

public class EnderscapeUiSounds {

    public static final Holder.Reference<SoundEvent> RUBBLE_SHIELD_COOLDOWN_OVER = registerHolder("rubble_shield.cooldown_over");
    public static final SoundEvent HEALTH_OUTER_VOID_DEATH = register("health.outer_void_death");
    public static final SoundEvent HEALTH_OUTER_VOID_WARNING = register("health.outer_void_warning");
    public static final SoundEvent HEALTH_VOID_PURIFY = register("health.void_purify");

    private static SoundEvent register(String name) {
        return Enderscape.registerSoundEvent("ui." + name);
    }

    private static Holder.Reference<SoundEvent> registerHolder(String name) {
        return Enderscape.registerSoundEventHolder("ui." + name);
    }
}