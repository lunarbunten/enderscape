package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class EnderscapeEntitySounds {

    public static final Supplier<SoundEvent> DRIFTER_AMBIENT = register("drifter.ambient");
    public static final Supplier<SoundEvent> DRIFTER_BOUNCE = register("drifter.bounce");
    public static final Supplier<SoundEvent> DRIFTER_DEATH = register("drifter.death");
    public static final Supplier<SoundEvent> DRIFTER_EAT = register("drifter.eat");
    public static final Supplier<SoundEvent> DRIFTER_HURT = register("drifter.hurt");
    public static final Supplier<SoundEvent> DRIFTER_HURT_SILENT = register("drifter.hurt_silent");
    public static final Supplier<SoundEvent> DRIFTER_JUMP = register("drifter.jump");
    public static final Supplier<SoundEvent> DRIFTER_MILK = register("drifter.milk");
    public static final Supplier<SoundEvent> DRIFTLET_AMBIENT = register("driftlet.ambient");
    public static final Supplier<SoundEvent> DRIFTLET_DEATH = register("driftlet.death");
    public static final Supplier<SoundEvent> DRIFTLET_EAT = register("driftlet.eat");
    public static final Supplier<SoundEvent> DRIFTLET_HURT = register("driftlet.hurt");
    public static final Supplier<SoundEvent> DRIFTLET_JUMP = register("driftlet.jump");
    public static final Supplier<SoundEvent> ENDERMAN_STARE_STEREO = register("enderman.stare_stereo");
    public static final Supplier<SoundEvent> ENDERMAN_STATIC = register("enderman.static");
    public static final Supplier<SoundEvent> ENDERMITE_AMBIENT = register("endermite.ambient");
    public static final Supplier<SoundEvent> ENDERMITE_DEATH = register("endermite.death");
    public static final Supplier<SoundEvent> ENDERMITE_HURT = register("endermite.hurt");
    public static final Supplier<SoundEvent> RUBBLEMITE_AMBIENT = register("rubblemite.ambient");
    public static final Supplier<SoundEvent> RUBBLEMITE_DEATH = register("rubblemite.death");
    public static final Supplier<SoundEvent> RUBBLEMITE_EXTRUDE = register("rubblemite.extrude");
    public static final Supplier<SoundEvent> RUBBLEMITE_HOP = register("rubblemite.hop");
    public static final Supplier<SoundEvent> RUBBLEMITE_PREPARE_DASH = register("rubblemite.prepare_dash");
    public static final Supplier<SoundEvent> RUBBLEMITE_HURT = register("rubblemite.hurt");
    public static final Supplier<SoundEvent> RUBBLEMITE_SHIELD = register("rubblemite.shield");
    public static final Supplier<SoundEvent> RUBBLEMITE_STEP = register("rubblemite.step");
    public static final Supplier<SoundEvent> RUSTLE_AMBIENT = register("rustle.ambient");
    public static final Supplier<SoundEvent> RUSTLE_BUMP = register("rustle.bump");
    public static final Supplier<SoundEvent> RUSTLE_EAT = register("rustle.eat");
    public static final Supplier<SoundEvent> RUSTLE_HURT = register("rustle.hurt");
    public static final Supplier<SoundEvent> RUSTLE_DEATH = register("rustle.death");
    public static final Supplier<SoundEvent> RUSTLE_SHEAR = register("rustle.shear");
    public static final Supplier<SoundEvent> RUSTLE_STEP = register("rustle.step");
    public static final Supplier<SoundEvent> RUSTLE_SLEEPING_BUBBLE_POP = register("rustle.sleeping_bubble_pop");
    public static final Supplier<SoundEvent> RUSTLE_SNORE = register("rustle.snore");
    public static final Supplier<SoundEvent> SHULKER_BULLET_LOOP = register("shulker_bullet.loop");

    private static Supplier<SoundEvent> register(String name) {
        var event = Enderscape.registerSoundEvent("entity." + name);
        return () -> event;
    }
}