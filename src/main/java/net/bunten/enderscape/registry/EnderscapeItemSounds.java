package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;

public class EnderscapeItemSounds {

    public static final Holder.Reference<SoundEvent> DRIFT_JELLY_BOTTLE_DRINK = registerHolder("drift_jelly_bottle.drink");
    public static final Holder.Reference<SoundEvent> DRIFT_LEGGINGS_EQUIP = registerHolder("drift_leggings.equip");
    public static final Holder.Reference<SoundEvent> DAGGER_ATTACK_STRONG = registerHolder("dagger.attack.strong");
    public static final Holder.Reference<SoundEvent> DAGGER_ATTACK_CRIT = registerHolder("dagger.attack.crit");
    public static final Holder.Reference<SoundEvent> DAGGER_ATTACK_KNOCKBACK = registerHolder("dagger.attack.knockback");
    public static final Holder.Reference<SoundEvent> DAGGER_BACKSTAB = registerHolder("dagger.backstab");
    public static final Holder.Reference<SoundEvent> DAGGER_STUN = registerHolder("dagger.stun");
    public static final Holder.Reference<SoundEvent> ELYTRA_EQUIP = registerHolder("elytra.equip");
    public static final Holder.Reference<SoundEvent> MAGNIA_ATTRACTOR_MOVE = registerHolder("magnia_attractor.move");
    public static final Holder.Reference<SoundEvent> MAGNIA_ATTRACTOR_POWER_OFF = registerHolder("magnia_attractor.power_off");
    public static final Holder.Reference<SoundEvent> MAGNIA_ATTRACTOR_POWER_ON = registerHolder("magnia_attractor.power_on");
    public static final Holder.Reference<SoundEvent> MAGNIA_ATTRACTOR_USE_FUEL = registerHolder("magnia_attractor.use_fuel");
    public static final Holder.Reference<SoundEvent> MIRROR_FAILURE = registerHolder("mirror.failure");
    public static final Holder.Reference<SoundEvent> MIRROR_LINK = registerHolder("mirror.link");
    public static final Holder.Reference<SoundEvent> MIRROR_TELEPORT = registerHolder("mirror.teleport");
    public static final Holder.Reference<SoundEvent> MIRROR_TRANSDIMENSIONAL_TRAVEL = registerHolder("mirror.transdimensional_travel");
    public static final Holder.Reference<SoundEvent> NEBULITE_TOOL_ADD_FUEL = registerHolder("nebulite_tool.add_fuel");
    public static final Holder.Reference<SoundEvent> NEBULITE_TOOL_FUEL_FULL = registerHolder("nebulite_tool.fuel_full");
    public static final Holder.Reference<SoundEvent> RUBBLE_SHIELD_BLOCK = registerHolder("rubble_shield.block");
    public static final Holder.Reference<SoundEvent> RUBBLE_SHIELD_COOLDOWN_OVER = registerHolder("rubble_shield.cooldown_over");
    public static final Holder.Reference<SoundEvent> RUBBLE_SHIELD_DASH = registerHolder("rubble_shield.dash");
    public static final Holder.Reference<SoundEvent> SHADOLINE_ARMOR_EQUIP = registerHolder("shadoline_armor.equip");
    public static final Holder.Reference<SoundEvent> SHULKER_SHELL_EQUIP = registerHolder("shulker_shell.equip");

    public static final SoundEvent CRACKED_MIRROR_TRY_LINK = register("cracked_mirror.try_link");
    public static final SoundEvent CRACKED_MIRROR_TRY_TELEPORT = register("cracked_mirror.try_teleport");
    public static final SoundEvent ELYTRA_BREAK = register("elytra.break");
    public static final SoundEvent ELYTRA_GLIDING = register("elytra.gliding");
    public static final SoundEvent ELYTRA_LAND = register("elytra.land");
    public static final SoundEvent ELYTRA_START_GLIDING = register("elytra.start_gliding");
    public static final SoundEvent ELYTRA_STOP_GLIDING = register("elytra.stop_gliding");
    public static final SoundEvent ENDER_PEARL_LAND = register("ender_pearl.land");
    public static final SoundEvent ENDER_PEARL_THROW = register("ender_pearl.throw");
    public static final SoundEvent RUSTLE_BUCKET_EMPTY = register("rustle_bucket.empty");
    public static final SoundEvent RUSTLE_BUCKET_FILL = register("rustle_bucket.fill");
    public static final SoundEvent TRIDENT_WARP = register("trident.warp");

    private static SoundEvent register(String name) {
        return Enderscape.registerSoundEvent("item." + name);
    }

    private static Holder.Reference<SoundEvent> registerHolder(String name) {
        return Enderscape.registerSoundEventHolder("item." + name);
    }
}