package net.penumbra.enderscape.registry.sound;

import net.minecraft.sounds.SoundEvent;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.sound.BiomeSounds;

public class EnderscapeBiomeSounds {

    public static final BiomeSounds DEFAULT_END = BiomeSounds.of("default_end");
    public static final BiomeSounds CELESTIAL_GROVE = BiomeSounds.of("celestial_grove");
    public static final BiomeSounds CORRUPT_BARRENS = BiomeSounds.of("corrupt_barrens");
    public static final BiomeSounds MAGNIA_FIELDS = BiomeSounds.of("magnia_fields");
    public static final BiomeSounds VEILED_WOODLANDS = BiomeSounds.of("veiled_woodlands");
    public static final BiomeSounds VOID_DEPTHS = BiomeSounds.noMusic("void_depths");
    public static final BiomeSounds VOID_SKIES = BiomeSounds.noMusic("void_skies");
    public static final BiomeSounds VOID_SKY_ISLANDS = BiomeSounds.noMusic("void_sky_islands");

    public static final SoundEvent END_FLASH = Enderscape.registerSoundEvent("ambient.weather.end_flash");

}