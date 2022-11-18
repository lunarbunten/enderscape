package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;

public class EnderscapeSounds {

    // Ambience

    public static final SoundEvent DEFAULT_END_ADDITIONS = additions("default_end");
    public static final SoundEvent DEFAULT_END_LOOP = loop("default_end");
    public static final SoundEvent DEFAULT_END_MOOD = mood("default_end");
    public static final SoundEvent DEFAULT_END_MUSIC = register("music.biome.default_end");

    // Items & Blocks

    public static final SoundEvent BLINKLAMP_DECREASE = register("block.blinklamp.decrease");
    public static final SoundEvent BLINKLAMP_INCREASE = register("block.blinklamp.increase");

    public static final SoundEvent BLINKLIGHT_VINES_BLINK = register("block.blinklight_vines.blink");
    public static final SoundEvent BLINKLIGHT_VINES_INBETWEEN = register("block.blinklight_vines.inbetween");

    public static final SoundEvent CHORUS_FLOWER_HUM = register("block.chorus_flower.hum");

    public static final SoundEvent DRIFT_JELLY_BOTTLE_DRINK = register("item.drift_jelly_bottle.drink");
    public static final SoundEvent DRIFT_JELLY_BOTTLE_FINISH = register("item.drift_jelly_bottle.finish");
    public static final SoundEvent DRIFT_JELLY_BOUNCE = register("block.drift_jelly.bounce");
    public static final SoundEvent DRIFT_LEGGINGS_EQUIP = register("item.drift_leggings.equip");

    public static final SoundEvent ELYTRA_START = register("item.elytra.start");
    public static final SoundEvent END_PORTAL_AMBIENT = register("block.end_portal.ambient");
    public static final SoundEvent EYLIUM_FLATTEN = register("block.eylium.flatten");

    public static final SoundEvent MIRROR_FAILURE = register("item.mirror.failure");
    public static final SoundEvent MIRROR_LINK = register("item.mirror.link");
    public static final SoundEvent MIRROR_TELEPORT = register("item.mirror.teleport");
    public static final SoundEvent MIRROR_TRANSDIMENSIONAL_TRAVEL = register("item.mirror.transdimensional_travel");

    public static final SoundEvent NEBULITE_CAULDRON_DIP = register("block.nebulite_cauldron.dip");
    public static final SoundEvent NEBULITE_CAULDRON_FILL = register("block.nebulite_cauldron.fill");
    public static final SoundEvent NEBULITE_CAULDRON_TELEPORT = register("block.nebulite_cauldron.teleport");

    public static final SoundEvent NEBULITE_ORE_AMBIENT = register("block.nebulite_ore.ambient");
    public static final SoundEvent NEBULITE_ORE_AMBIENT_FAR = register("block.nebulite_ore.ambient.far");
    public static final SoundEvent NEBULITE_ORE_AMBIENT_OBSTRUCTED = register("block.nebulite_ore.ambient.obstructed");

    public static final SoundEvent TRIDENT_WARP = register("item.trident.warp");

    // Entities

    public static final SoundEvent DRIFTER_AMBIENT = register("entity.drifter.ambient");
    public static final SoundEvent DRIFTER_BOUNCE = register("entity.drifter.bounce");
    public static final SoundEvent DRIFTER_DEATH = register("entity.drifter.death");
    public static final SoundEvent DRIFTER_EAT = register("entity.drifter.eat");
    public static final SoundEvent DRIFTER_HURT = register("entity.drifter.hurt");
    public static final SoundEvent DRIFTER_MILK = register("entity.drifter.milk");

    public static final SoundEvent DRIFTLET_AMBIENT = register("entity.driftlet.ambient");
    public static final SoundEvent DRIFTLET_DEATH = register("entity.driftlet.death");
    public static final SoundEvent DRIFTLET_EAT = register("entity.driftlet.eat");
    public static final SoundEvent DRIFTLET_HURT = register("entity.driftlet.hurt");

    public static final SoundEvent RUBBLEMITE_AMBIENT = register("entity.rubblemite.ambient");
    public static final SoundEvent RUBBLEMITE_DEATH = register("entity.rubblemite.death");
    public static final SoundEvent RUBBLEMITE_EXTRUDE = register("entity.rubblemite.extrude");
    public static final SoundEvent RUBBLEMITE_HOP = register("entity.rubblemite.hop");
    public static final SoundEvent RUBBLEMITE_HURT = register("entity.rubblemite.hurt");
    public static final SoundEvent RUBBLEMITE_SHIELD = register("entity.rubblemite.shield");

    // Sound Types

    public static final SoundType BLINKLAMP = register("blinklamp", 1, 1.2F);
    public static final SoundType BLINKLIGHT_VINES = register("blinklight_vines", 1, 1.2F);
    public static final SoundType CHORUS = register("chorus", 1, 1.15F);
    public static final SoundType CHORUS_FLOWER = register("chorus_flower", 1, 1.15F);
    public static final SoundType DRIFT_JELLY = register("drift_jelly", 1, 1);
    public static final SoundType END_ROD = register("end_rod", 1, 1.3F);
    public static final SoundType END_STONE = register("end_stone", 1, 1.3F);
    public static final SoundType END_STONE_BRICKS = register("end_stone_bricks", 1, 1);
    public static final SoundType EYLIUM = register("eylium", 1, 1.1F);
    public static final SoundType FLANGER_BERRY_BLOCK = register("flanger_berry_block", 1, 1.2F);
    public static final SoundType FLANGER_BERRY_VINE = register("flanger_berry_vine", 1, 0.9F);
    public static final SoundType FLANGER_FLOWER = register("flanger_flower", 1, 0.9F);
    public static final SoundType FUNGUS = register("fungus", 1, 1.2F);
    public static final SoundType FUNGUS_BRICKS = register("fungus_bricks", 0.8F, 1.2F);
    public static final SoundType FUNGUS_CAP = register("fungus_cap", 0.8F, 1.2F);
    public static final SoundType FUNGUS_WOOD = register("fungus_wood", 1, 1);
    public static final SoundType GROWTH = register("growth", 0.5F, 1);
    public static final SoundType NEBULITE_BLOCK = register("nebulite_block", 1, 1.23F);
    public static final SoundType NEBULITE_ORE = register("nebulite_ore", 1.2F, 1.3F);
    public static final SoundType PURPUR = register("purpur", 0.7F, 1.1F);
    public static final SoundType SHADOW_QUARTZ = register("shadow_quartz", 1.1F, 1.2F);
    public static final SoundType SHADOW_QUARTZ_ORE = register("shadow_quartz_ore", 1.2F, 1.3F);
    public static final SoundType SHULKER = register("shulker", 1, 1.3F);
    public static final SoundType STEM = register("stem", 1, 1);
    public static final SoundType VERADITE = register("veradite", 1, 1.3F);
    public static final SoundType VERADITE_BRICKS = register("veradite_bricks", 1, 1);

    private static String block(String name, String append) {
        return "block." + name + "." + append;
    }

    private static SoundType register(String name, float volume, float pitch) {
        return new SoundType(volume, pitch, register(block(name, "break")), register(block(name, "step")), register(block(name, "place")), register(block(name, "hit")), register(block(name, "fall")));
    }

    private static SoundEvent register(String name) {
        ResourceLocation id = Enderscape.id(name);
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
    }

    public static SoundEvent additions(String name) {
        return register("ambient." + name + ".additions");
    }

    public static SoundEvent loop(String name) {
        return register("ambient." + name + ".loop");
    }

    public static SoundEvent mood(String name) {
        return register("ambient." + name + ".mood");
    }

    public static Music biomeMusic(String biome) {
        return new Music(register("music.biome." + biome), 12000, 24000, false);
    }

    public static SoundEvent discMusic(String name) {
        return register("music.disc." + name);
    }

    public static void init() {
    }
}