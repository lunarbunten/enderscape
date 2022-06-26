package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EnderscapeSounds {

    // Ambience

    public static final SoundEvent AMBIENT_CELESTIAL_PLAINS_ADDITIONS = register("ambient.celestial_plains.additions");
    public static final SoundEvent AMBIENT_CELESTIAL_PLAINS_LOOP = register("ambient.celestial_plains.loop");
    public static final SoundEvent AMBIENT_CELESTIAL_PLAINS_MOOD = register("ambient.celestial_plains.mood");

    public static final SoundEvent AMBIENT_CELESTIAL_ISLANDS_ADDITIONS = register("ambient.celestial_islands.additions");
    public static final SoundEvent AMBIENT_CELESTIAL_ISLANDS_LOOP = register("ambient.celestial_islands.loop");
    public static final SoundEvent AMBIENT_CELESTIAL_ISLANDS_MOOD = register("ambient.celestial_islands.mood");

    public static final SoundEvent AMBIENT_NORMAL_END_ADDITIONS = register("ambient.normal_end.additions");
    public static final SoundEvent AMBIENT_NORMAL_END_LOOP = register("ambient.normal_end.loop");

    // Blocks

    public static final SoundEvent BLOCK_CHORUS_BREAK = register("block.chorus.break");
    public static final SoundEvent BLOCK_CHORUS_STEP = register("block.chorus.step");
    public static final SoundEvent BLOCK_CHORUS_PLACE = register("block.chorus.place");
    public static final SoundEvent BLOCK_CHORUS_HIT = register("block.chorus.hit");
    public static final SoundEvent BLOCK_CHORUS_FALL = register("block.chorus.fall");

    public static final SoundEvent BLOCK_DRIFT_JELLY_BOUNCE = register("block.drift_jelly.bounce");
    public static final SoundEvent BLOCK_DRIFT_JELLY_BREAK = register("block.drift_jelly.break");
    public static final SoundEvent BLOCK_DRIFT_JELLY_STEP = register("block.drift_jelly.step");
    public static final SoundEvent BLOCK_DRIFT_JELLY_PLACE = register("block.drift_jelly.place");
    public static final SoundEvent BLOCK_DRIFT_JELLY_HIT = register("block.drift_jelly.hit");
    public static final SoundEvent BLOCK_DRIFT_JELLY_FALL = register("block.drift_jelly.fall");

    public static final SoundEvent BLOCK_END_PORTAL_AMBIENT = register("block.end_portal.ambient");

    public static final SoundEvent BLOCK_END_STONE_BREAK = register("block.end_stone.break");
    public static final SoundEvent BLOCK_END_STONE_STEP = register("block.end_stone.step");
    public static final SoundEvent BLOCK_END_STONE_PLACE = register("block.end_stone.place");
    public static final SoundEvent BLOCK_END_STONE_HIT = register("block.end_stone.hit");
    public static final SoundEvent BLOCK_END_STONE_FALL = register("block.end_stone.fall");

    public static final SoundEvent BLOCK_END_STONE_BRICKS_BREAK = register("block.end_stone_bricks.break");
    public static final SoundEvent BLOCK_END_STONE_BRICKS_STEP = register("block.end_stone_bricks.step");
    public static final SoundEvent BLOCK_END_STONE_BRICKS_PLACE = register("block.end_stone_bricks.place");
    public static final SoundEvent BLOCK_END_STONE_BRICKS_HIT = register("block.end_stone_bricks.hit");
    public static final SoundEvent BLOCK_END_STONE_BRICKS_FALL = register("block.end_stone_bricks.fall");

    public static final SoundEvent BLOCK_END_ROD_BREAK = register("block.end_rod.break");
    public static final SoundEvent BLOCK_END_ROD_STEP = register("block.end_rod.step");
    public static final SoundEvent BLOCK_END_ROD_PLACE = register("block.end_rod.place");
    public static final SoundEvent BLOCK_END_ROD_HIT = register("block.end_rod.hit");
    public static final SoundEvent BLOCK_END_ROD_FALL = register("block.end_rod.fall");

    public static final SoundEvent BLOCK_EYLIUM_BREAK = register("block.eylium.break");
    public static final SoundEvent BLOCK_EYLIUM_STEP = register("block.eylium.step");
    public static final SoundEvent BLOCK_EYLIUM_PLACE = register("block.eylium.place");
    public static final SoundEvent BLOCK_EYLIUM_HIT = register("block.eylium.hit");
    public static final SoundEvent BLOCK_EYLIUM_FALL = register("block.eylium.fall");

    public static final SoundEvent BLOCK_EYLIUM_FLATTEN = register("block.eylium.flatten");

    public static final SoundEvent BLOCK_FLANGER_BERRY_BLOCK_BREAK = register("block.flanger_berry_block.break");
    public static final SoundEvent BLOCK_FLANGER_BERRY_BLOCK_STEP = register("block.flanger_berry_block.step");
    public static final SoundEvent BLOCK_FLANGER_BERRY_BLOCK_PLACE = register("block.flanger_berry_block.place");
    public static final SoundEvent BLOCK_FLANGER_BERRY_BLOCK_HIT = register("block.flanger_berry_block.hit");
    public static final SoundEvent BLOCK_FLANGER_BERRY_BLOCK_FALL = register("block.flanger_berry_block.fall");

    public static final SoundEvent BLOCK_FLANGER_BERRY_VINE_BREAK = register("block.flanger_berry_vine.break");
    public static final SoundEvent BLOCK_FLANGER_BERRY_VINE_STEP = register("block.flanger_berry_vine.step");
    public static final SoundEvent BLOCK_FLANGER_BERRY_VINE_PLACE = register("block.flanger_berry_vine.place");
    public static final SoundEvent BLOCK_FLANGER_BERRY_VINE_HIT = register("block.flanger_berry_vine.hit");
    public static final SoundEvent BLOCK_FLANGER_BERRY_VINE_FALL = register("block.flanger_berry_vine.fall");

    public static final SoundEvent BLOCK_FLANGER_FLOWER_BREAK = register("block.flanger_flower.break");
    public static final SoundEvent BLOCK_FLANGER_FLOWER_STEP = register("block.flanger_flower.step");
    public static final SoundEvent BLOCK_FLANGER_FLOWER_PLACE = register("block.flanger_flower.place");
    public static final SoundEvent BLOCK_FLANGER_FLOWER_HIT = register("block.flanger_flower.hit");
    public static final SoundEvent BLOCK_FLANGER_FLOWER_FALL = register("block.flanger_flower.fall");

    public static final SoundEvent BLOCK_FUNGUS_BREAK = register("block.fungus.break");
    public static final SoundEvent BLOCK_FUNGUS_STEP = register("block.fungus.step");
    public static final SoundEvent BLOCK_FUNGUS_PLACE = register("block.fungus.place");
    public static final SoundEvent BLOCK_FUNGUS_HIT = register("block.fungus.hit");
    public static final SoundEvent BLOCK_FUNGUS_FALL = register("block.fungus.fall");

    public static final SoundEvent BLOCK_FUNGUS_CAP_BREAK = register("block.fungus_cap.break");
    public static final SoundEvent BLOCK_FUNGUS_CAP_STEP = register("block.fungus_cap.step");
    public static final SoundEvent BLOCK_FUNGUS_CAP_PLACE = register("block.fungus_cap.place");
    public static final SoundEvent BLOCK_FUNGUS_CAP_HIT = register("block.fungus_cap.hit");
    public static final SoundEvent BLOCK_FUNGUS_CAP_FALL = register("block.fungus_cap.fall");

    public static final SoundEvent BLOCK_GROWTH_BREAK = register("block.growth.break");
    public static final SoundEvent BLOCK_GROWTH_STEP = register("block.growth.step");
    public static final SoundEvent BLOCK_GROWTH_PLACE = register("block.growth.place");
    public static final SoundEvent BLOCK_GROWTH_HIT = register("block.growth.hit");
    public static final SoundEvent BLOCK_GROWTH_FALL = register("block.growth.fall");

    public static final SoundEvent BLOCK_NEBULITE_BLOCK_BREAK = register("block.nebulite_block.break");
    public static final SoundEvent BLOCK_NEBULITE_BLOCK_STEP = register("block.nebulite_block.step");
    public static final SoundEvent BLOCK_NEBULITE_BLOCK_PLACE = register("block.nebulite_block.place");
    public static final SoundEvent BLOCK_NEBULITE_BLOCK_HIT = register("block.nebulite_block.hit");
    public static final SoundEvent BLOCK_NEBULITE_BLOCK_FALL = register("block.nebulite_block.fall");

    public static final SoundEvent BLOCK_NEBULITE_CAULDRON_DIP = register("block.nebulite_cauldron.dip");
    public static final SoundEvent BLOCK_NEBULITE_CAULDRON_FILL = register("block.nebulite_cauldron.fill");
    public static final SoundEvent BLOCK_NEBULITE_CAULDRON_TELEPORT = register("block.nebulite_cauldron.teleport");

    public static final SoundEvent BLOCK_NEBULITE_ORE_AMBIENT = register("block.nebulite_ore.ambient");
    public static final SoundEvent BLOCK_NEBULITE_ORE_AMBIENT_FAR = register("block.nebulite_ore.ambient.far");
    public static final SoundEvent BLOCK_NEBULITE_ORE_AMBIENT_OBSTRUCTED = register("block.nebulite_ore.ambient.obstructed");

    public static final SoundEvent BLOCK_NEBULITE_ORE_BREAK = register("block.nebulite_ore.break");
    public static final SoundEvent BLOCK_NEBULITE_ORE_STEP = register("block.nebulite_ore.step");
    public static final SoundEvent BLOCK_NEBULITE_ORE_PLACE = register("block.nebulite_ore.place");
    public static final SoundEvent BLOCK_NEBULITE_ORE_HIT = register("block.nebulite_ore.hit");
    public static final SoundEvent BLOCK_NEBULITE_ORE_FALL = register("block.nebulite_ore.fall");

    public static final SoundEvent BLOCK_PURPUR_BREAK = register("block.purpur.break");
    public static final SoundEvent BLOCK_PURPUR_STEP = register("block.purpur.step");
    public static final SoundEvent BLOCK_PURPUR_PLACE = register("block.purpur.place");
    public static final SoundEvent BLOCK_PURPUR_HIT = register("block.purpur.hit");
    public static final SoundEvent BLOCK_PURPUR_FALL = register("block.purpur.fall");

    public static final SoundEvent BLOCK_SHADOW_QUARTZ_BREAK = register("block.shadow_quartz.break");
    public static final SoundEvent BLOCK_SHADOW_QUARTZ_STEP = register("block.shadow_quartz.step");
    public static final SoundEvent BLOCK_SHADOW_QUARTZ_PLACE = register("block.shadow_quartz.place");
    public static final SoundEvent BLOCK_SHADOW_QUARTZ_HIT = register("block.shadow_quartz.hit");
    public static final SoundEvent BLOCK_SHADOW_QUARTZ_FALL = register("block.shadow_quartz.fall");

    public static final SoundEvent BLOCK_SHADOW_QUARTZ_ORE_BREAK = register("block.shadow_quartz_ore.break");
    public static final SoundEvent BLOCK_SHADOW_QUARTZ_ORE_STEP = register("block.shadow_quartz_ore.step");
    public static final SoundEvent BLOCK_SHADOW_QUARTZ_ORE_PLACE = register("block.shadow_quartz_ore.place");
    public static final SoundEvent BLOCK_SHADOW_QUARTZ_ORE_HIT = register("block.shadow_quartz_ore.hit");
    public static final SoundEvent BLOCK_SHADOW_QUARTZ_ORE_FALL = register("block.shadow_quartz_ore.fall");

    public static final SoundEvent BLOCK_SHULKER_BREAK = register("block.shulker.break");
    public static final SoundEvent BLOCK_SHULKER_STEP = register("block.shulker.step");
    public static final SoundEvent BLOCK_SHULKER_PLACE = register("block.shulker.place");
    public static final SoundEvent BLOCK_SHULKER_HIT = register("block.shulker.hit");
    public static final SoundEvent BLOCK_SHULKER_FALL = register("block.shulker.fall");

    public static final SoundEvent BLOCK_STEM_BREAK = register("block.stem.break");
    public static final SoundEvent BLOCK_STEM_STEP = register("block.stem.step");
    public static final SoundEvent BLOCK_STEM_PLACE = register("block.stem.place");
    public static final SoundEvent BLOCK_STEM_HIT = register("block.stem.hit");
    public static final SoundEvent BLOCK_STEM_FALL = register("block.stem.fall");

    // Entities

    public static final SoundEvent ENTITY_DRIFTER_AMBIENT = register("entity.drifter.ambient");
    public static final SoundEvent ENTITY_DRIFTER_BOUNCE = register("entity.drifter.bounce");
    public static final SoundEvent ENTITY_DRIFTER_DEATH = register("entity.drifter.death");
    public static final SoundEvent ENTITY_DRIFTER_EAT = register("entity.drifter.eat");
    public static final SoundEvent ENTITY_DRIFTER_HURT = register("entity.drifter.hurt");
    public static final SoundEvent ENTITY_DRIFTER_MILK = register("entity.drifter.milk");

    public static final SoundEvent ENTITY_DRIFTLET_AMBIENT = register("entity.driftlet.ambient");
    public static final SoundEvent ENTITY_DRIFTLET_DEATH = register("entity.driftlet.death");
    public static final SoundEvent ENTITY_DRIFTLET_EAT = register("entity.driftlet.eat");
    public static final SoundEvent ENTITY_DRIFTLET_HURT = register("entity.driftlet.hurt");

    public static final SoundEvent ENTITY_RUBBLEMITE_AMBIENT = register("entity.rubblemite.ambient");
    public static final SoundEvent ENTITY_RUBBLEMITE_DEATH = register("entity.rubblemite.death");
    public static final SoundEvent ENTITY_RUBBLEMITE_EXTRUDE = register("entity.rubblemite.extrude");
    public static final SoundEvent ENTITY_RUBBLEMITE_HOP = register("entity.rubblemite.hop");
    public static final SoundEvent ENTITY_RUBBLEMITE_HURT = register("entity.rubblemite.hurt");
    public static final SoundEvent ENTITY_RUBBLEMITE_SHIELD = register("entity.rubblemite.shield");

    // Items

    public static final SoundEvent ITEM_DRIFT_LEGGINGS_EQUIP = register("item.drift_leggings.equip");
    public static final SoundEvent ITEM_DRIFT_JELLY_BOTTLE_DRINK = register("item.drift_jelly_bottle.drink");
    public static final SoundEvent ITEM_DRIFT_JELLY_BOTTLE_FINISH = register("item.drift_jelly_bottle.finish");

    public static final SoundEvent ITEM_ELYTRA_START = register("item.elytra.start");

    public static final SoundEvent ITEM_MIRROR_FAILURE = register("item.mirror.failure");
    public static final SoundEvent ITEM_MIRROR_LINK = register("item.mirror.link");
    public static final SoundEvent ITEM_MIRROR_TELEPORT = register("item.mirror.teleport");

    // Music

    public static final SoundEvent MUSIC_CELESTIAL_PLAINS = register("music.celestial_plains");
    public static final SoundEvent MUSIC_CELESTIAL_ISLANDS = register("music.celestial_islands");

    public static final SoundEvent MUSIC_SKY = register("music.sky");
    public static final SoundEvent MUSIC_VOID = register("music.void");

    public static final SoundEvent MUSIC_DISC_BLISS = register("music.disc.bliss");
    public static final SoundEvent MUSIC_DISC_GLARE = register("music.disc.glare");

    // Sound Groups

    public static final BlockSoundGroup CHORUS = new BlockSoundGroup(1, 1.15F, BLOCK_CHORUS_BREAK, BLOCK_CHORUS_STEP, BLOCK_CHORUS_PLACE, BLOCK_CHORUS_HIT, BLOCK_CHORUS_FALL);
    public static final BlockSoundGroup DRIFT_JELLY = new BlockSoundGroup(1, 1, BLOCK_DRIFT_JELLY_BREAK, BLOCK_DRIFT_JELLY_STEP, BLOCK_DRIFT_JELLY_PLACE, BLOCK_DRIFT_JELLY_HIT, BLOCK_DRIFT_JELLY_FALL);
    public static final BlockSoundGroup END_STONE = new BlockSoundGroup(1, 1.3F, BLOCK_END_STONE_BREAK, BLOCK_END_STONE_STEP, BLOCK_END_STONE_PLACE, BLOCK_END_STONE_HIT, BLOCK_END_STONE_FALL);
    public static final BlockSoundGroup END_STONE_BRICKS = new BlockSoundGroup(1, 1, BLOCK_END_STONE_BRICKS_BREAK, BLOCK_END_STONE_BRICKS_STEP, BLOCK_END_STONE_BRICKS_PLACE, BLOCK_END_STONE_BRICKS_HIT, BLOCK_END_STONE_BRICKS_FALL);
    public static final BlockSoundGroup END_ROD = new BlockSoundGroup(1, 1.3F, BLOCK_END_ROD_BREAK, BLOCK_END_ROD_STEP, BLOCK_END_ROD_PLACE, BLOCK_END_ROD_HIT, BLOCK_END_ROD_FALL);
    public static final BlockSoundGroup EYLIUM = new BlockSoundGroup(1, 1.1F, BLOCK_EYLIUM_BREAK, BLOCK_EYLIUM_STEP, BLOCK_EYLIUM_PLACE, BLOCK_EYLIUM_HIT, BLOCK_EYLIUM_FALL);
    public static final BlockSoundGroup FLANGER_BERRY_BLOCK = new BlockSoundGroup(1, 1.2F, BLOCK_FLANGER_BERRY_BLOCK_BREAK, BLOCK_FLANGER_BERRY_BLOCK_STEP, BLOCK_FLANGER_BERRY_BLOCK_PLACE, BLOCK_FLANGER_BERRY_BLOCK_HIT, BLOCK_FLANGER_BERRY_BLOCK_FALL);
    public static final BlockSoundGroup FLANGER_BERRY_VINE = new BlockSoundGroup(1, 0.9F, BLOCK_FLANGER_BERRY_VINE_BREAK, BLOCK_FLANGER_BERRY_VINE_STEP, BLOCK_FLANGER_BERRY_VINE_PLACE, BLOCK_FLANGER_BERRY_VINE_HIT, BLOCK_FLANGER_BERRY_VINE_FALL);
    public static final BlockSoundGroup FLANGER_FLOWER = new BlockSoundGroup(1, 0.9F, BLOCK_FLANGER_FLOWER_BREAK, BLOCK_FLANGER_FLOWER_STEP, BLOCK_FLANGER_FLOWER_PLACE, BLOCK_FLANGER_FLOWER_HIT, BLOCK_FLANGER_FLOWER_FALL);
    public static final BlockSoundGroup FUNGUS = new BlockSoundGroup(1, 1.2F, BLOCK_FUNGUS_BREAK, BLOCK_FUNGUS_STEP, BLOCK_FUNGUS_PLACE, BLOCK_FUNGUS_HIT, BLOCK_FUNGUS_FALL);
    public static final BlockSoundGroup FUNGUS_CAP = new BlockSoundGroup(0.8F, 1.2F, BLOCK_FUNGUS_CAP_BREAK, BLOCK_FUNGUS_CAP_STEP, BLOCK_FUNGUS_CAP_PLACE, BLOCK_FUNGUS_CAP_HIT, BLOCK_FUNGUS_CAP_FALL);
    public static final BlockSoundGroup GROWTH = new BlockSoundGroup(0.5F, 1, BLOCK_GROWTH_BREAK, BLOCK_GROWTH_STEP, BLOCK_GROWTH_PLACE, BLOCK_GROWTH_HIT, BLOCK_GROWTH_FALL);
    public static final BlockSoundGroup NEBULITE_BLOCK = new BlockSoundGroup(1, 1.23F, BLOCK_NEBULITE_BLOCK_BREAK, BLOCK_NEBULITE_BLOCK_STEP, BLOCK_NEBULITE_BLOCK_PLACE, BLOCK_NEBULITE_BLOCK_HIT, BLOCK_NEBULITE_BLOCK_FALL);
    public static final BlockSoundGroup NEBULITE_ORE = new BlockSoundGroup(1.2F, 1.3F, BLOCK_NEBULITE_ORE_BREAK, BLOCK_NEBULITE_ORE_STEP, BLOCK_NEBULITE_ORE_PLACE, BLOCK_NEBULITE_ORE_HIT, BLOCK_NEBULITE_ORE_FALL);
    public static final BlockSoundGroup PURPUR = new BlockSoundGroup(0.7F, 1.1F, BLOCK_PURPUR_BREAK, BLOCK_PURPUR_STEP, BLOCK_PURPUR_PLACE, BLOCK_PURPUR_HIT, BLOCK_PURPUR_FALL);
    public static final BlockSoundGroup SHADOW_QUARTZ = new BlockSoundGroup(1.1F, 1.2F, BLOCK_SHADOW_QUARTZ_BREAK, BLOCK_SHADOW_QUARTZ_STEP, BLOCK_SHADOW_QUARTZ_PLACE, BLOCK_SHADOW_QUARTZ_HIT, BLOCK_SHADOW_QUARTZ_FALL);
    public static final BlockSoundGroup SHADOW_QUARTZ_ORE = new BlockSoundGroup(1.2F, 1.3F, BLOCK_SHADOW_QUARTZ_ORE_BREAK, BLOCK_SHADOW_QUARTZ_ORE_STEP, BLOCK_SHADOW_QUARTZ_ORE_PLACE, BLOCK_SHADOW_QUARTZ_ORE_HIT, BLOCK_SHADOW_QUARTZ_ORE_FALL);
    public static final BlockSoundGroup SHULKER = new BlockSoundGroup(1, 1.3F, BLOCK_SHULKER_BREAK, BLOCK_SHULKER_STEP, BLOCK_SHULKER_PLACE, BLOCK_SHULKER_HIT, BLOCK_SHULKER_FALL);
    public static final BlockSoundGroup STEM = new BlockSoundGroup(1, 1.2F, BLOCK_STEM_BREAK, BLOCK_STEM_STEP, BLOCK_STEM_PLACE, BLOCK_STEM_HIT, BLOCK_STEM_FALL);

    private static SoundEvent register(String name) {
        Identifier id = Enderscape.id(name);
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
    }

    public static void init() {
    }
}