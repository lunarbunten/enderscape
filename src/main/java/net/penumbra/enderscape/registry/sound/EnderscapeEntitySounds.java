package net.penumbra.enderscape.registry.sound;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.penumbra.enderscape.Enderscape;

public class EnderscapeEntitySounds {

    public static final Holder.Reference<SoundEvent> ITEM_VOID_LACHRYMA_CORRUPTION = registerHolder("item.void_lachryma_corruption");
    public static final Holder.Reference<SoundEvent> RUSTLE_SPIT = registerHolder("rustle.spit");
    public static final Holder.Reference<SoundEvent> RUSTLE_SPIT_MUSIC_DISC_BLISS = registerHolder("rustle.spit.music_disc_bliss");
    public static final Holder.Reference<SoundEvent> RUSTLE_SWELL = registerHolder("rustle.swell");
    public static final Holder.Reference<SoundEvent> RUSTLE_SWELL_FAST = registerHolder("rustle.swell_fast");
    public static final Holder.Reference<SoundEvent> RUSTLE_SWELL_LONG = registerHolder("rustle.swell_long");

    public static final SoundEvent DRIFTER_AMBIENT = register("drifter.ambient");
    public static final SoundEvent DRIFTER_BOUNCE = register("drifter.bounce");
    public static final SoundEvent DRIFTER_DEATH = register("drifter.death");
    public static final SoundEvent DRIFTER_EAT = register("drifter.eat");
    public static final SoundEvent DRIFTER_HURT = register("drifter.hurt");
    public static final SoundEvent DRIFTER_HURT_SILENT = register("drifter.hurt_silent");
    public static final SoundEvent DRIFTER_JUMP = register("drifter.jump");
    public static final SoundEvent DRIFTER_MILK = register("drifter.milk");
    public static final SoundEvent DRIFTLET_AMBIENT = register("driftlet.ambient");
    public static final SoundEvent DRIFTLET_DEATH = register("driftlet.death");
    public static final SoundEvent DRIFTLET_EAT = register("driftlet.eat");
    public static final SoundEvent DRIFTLET_HURT = register("driftlet.hurt");
    public static final SoundEvent DRIFTLET_JUMP = register("driftlet.jump");
    public static final SoundEvent ENDERMAN_STARE_STEREO = register("enderman.stare_stereo");
    public static final SoundEvent ENDERMITE_AMBIENT = register("endermite.ambient");
    public static final SoundEvent ENDERMITE_DEATH = register("endermite.death");
    public static final SoundEvent ENDERMITE_HURT = register("endermite.hurt");
    public static final SoundEvent PLAYER_HURT_VOID = register("player.hurt_void");
    public static final SoundEvent PLAYER_VOIDED_LOOP = register("player.voided_loop");
    public static final SoundEvent RUBBLEMITE_AMBIENT = register("rubblemite.ambient");
    public static final SoundEvent RUBBLEMITE_DEATH = register("rubblemite.death");
    public static final SoundEvent RUBBLEMITE_EXTRUDE = register("rubblemite.extrude");
    public static final SoundEvent RUBBLEMITE_HOP = register("rubblemite.hop");
    public static final SoundEvent RUBBLEMITE_HURT = register("rubblemite.hurt");
    public static final SoundEvent RUBBLEMITE_PREPARE_DASH = register("rubblemite.prepare_dash");
    public static final SoundEvent RUBBLEMITE_SHIELD = register("rubblemite.shield");
    public static final SoundEvent RUBBLEMITE_STEP = register("rubblemite.step");
    public static final SoundEvent RUSTLE_AMBIENT = register("rustle.ambient");
    public static final SoundEvent RUSTLE_BUMP = register("rustle.bump");
    public static final SoundEvent RUSTLE_SWALLOW = register("rustle.swallow");
    public static final SoundEvent RUSTLE_DEATH = register("rustle.death");
    public static final SoundEvent RUSTLE_EAT = register("rustle.eat");
    public static final SoundEvent RUSTLE_HURT = register("rustle.hurt");
    public static final SoundEvent RUSTLE_SHEAR = register("rustle.shear");
    public static final SoundEvent RUSTLE_SLEEPING_BUBBLE_POP = register("rustle.sleeping_bubble_pop");
    public static final SoundEvent RUSTLE_SNORE = register("rustle.snore");
    public static final SoundEvent RUSTLE_STEP = register("rustle.step");
    public static final SoundEvent SHULKER_BULLET_LOOP = register("shulker_bullet.loop");
    public static final SoundEvent VOID_CORRUPTION_AMBIENT = register("generic.void_corruption.ambient");
    public static final SoundEvent VOID_CORRUPTION_DESTRUCTION = register("generic.void_corruption.destruction");
    public static final SoundEvent VOID_HURT = register("generic.void_hurt");
    public static final SoundEvent VOID_SPLASH = register("generic.void_splash");
    public static final SoundEvent VOID_SPLASH_HEAVY = register("generic.heavy_void_splash");

    private static SoundEvent register(String name) {
        return Enderscape.registerSoundEvent("entity." + name);
    }

    private static Holder.Reference<SoundEvent> registerHolder(String name) {
        return Enderscape.registerSoundEventHolder("entity." + name);
    }
}