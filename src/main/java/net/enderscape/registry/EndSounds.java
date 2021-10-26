package net.enderscape.registry;

import net.enderscape.Enderscape;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EndSounds {

    // Ambience

    public static final SoundEvent AMBIENT_CELESTIAL_ADDITIONS = register("ambient.celestial.additions");
    public static final SoundEvent AMBIENT_CELESTIAL_LOOP = register("ambient.celestial.loop");

    public static final SoundEvent AMBIENT_CORRUPTION_ADDITIONS = register("ambient.corruption.additions");
    public static final SoundEvent AMBIENT_CORRUPTION_LOOP = register("ambient.corruption.loop");

    public static final SoundEvent AMBIENT_END_ADDITIONS = register("ambient.end.additions");
    public static final SoundEvent AMBIENT_END_LOOP = register("ambient.end.loop");

    // Blocks

    public static final SoundEvent BLOCK_CHORUS_BREAK = register("block.chorus.break");
    public static final SoundEvent BLOCK_CHORUS_STEP = register("block.chorus.step");
    public static final SoundEvent BLOCK_CHORUS_PLACE = register("block.chorus.place");
    public static final SoundEvent BLOCK_CHORUS_HIT = register("block.chorus.hit");
    public static final SoundEvent BLOCK_CHORUS_FALL = register("block.chorus.fall");

    public static final SoundEvent BLOCK_DEEP_FUNGUS_BREAK = register("block.deep_fungus.break");
    public static final SoundEvent BLOCK_DEEP_FUNGUS_STEP = register("block.deep_fungus.step");
    public static final SoundEvent BLOCK_DEEP_FUNGUS_PLACE = register("block.deep_fungus.place");
    public static final SoundEvent BLOCK_DEEP_FUNGUS_HIT = register("block.deep_fungus.hit");
    public static final SoundEvent BLOCK_DEEP_FUNGUS_FALL = register("block.deep_fungus.fall");

    public static final SoundEvent BLOCK_DRIFT_JELLY_BOUNCE = register("block.drift_jelly.bounce");

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

    public static final SoundEvent BLOCK_EYLIUM_BREAK = register("block.eylium.break");
    public static final SoundEvent BLOCK_EYLIUM_STEP = register("block.eylium.step");
    public static final SoundEvent BLOCK_EYLIUM_PLACE = register("block.eylium.place");
    public static final SoundEvent BLOCK_EYLIUM_HIT = register("block.eylium.hit");
    public static final SoundEvent BLOCK_EYLIUM_FALL = register("block.eylium.fall");

    public static final SoundEvent BLOCK_FLANGER_BERRY_VINE_BREAK = register("block.flanger_berry_vine.break");
    public static final SoundEvent BLOCK_FLANGER_BERRY_VINE_STEP = register("block.flanger_berry_vine.step");
    public static final SoundEvent BLOCK_FLANGER_BERRY_VINE_PLACE = register("block.flanger_berry_vine.place");
    public static final SoundEvent BLOCK_FLANGER_BERRY_VINE_HIT = register("block.flanger_berry_vine.hit");
    public static final SoundEvent BLOCK_FLANGER_BERRY_VINE_FALL = register("block.flanger_berry_vine.fall");

    public static final SoundEvent BLOCK_GROWTH_BREAK = register("block.growth.break");
    public static final SoundEvent BLOCK_GROWTH_STEP = register("block.growth.step");
    public static final SoundEvent BLOCK_GROWTH_PLACE = register("block.growth.place");
    public static final SoundEvent BLOCK_GROWTH_HIT = register("block.growth.hit");
    public static final SoundEvent BLOCK_GROWTH_FALL = register("block.growth.fall");

    public static final SoundEvent BLOCK_NEBULITE_CAULDRON_DIP = register("block.nebulite_cauldron.dip");
    public static final SoundEvent BLOCK_NEBULITE_CAULDRON_FILL = register("block.nebulite_cauldron.fill");

    public static final SoundEvent BLOCK_NEBULITE_ORE_AMBIENT = register("block.nebulite_ore.ambient");

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
    public static final SoundEvent ENTITY_DRIFTLET_HURT = register("entity.driftlet.hurt");

    public static final SoundEvent ENTITY_RUBBLEMITE_AMBIENT = register("entity.rubblemite.ambient");
    public static final SoundEvent ENTITY_RUBBLEMITE_DEATH = register("entity.rubblemite.death");
    public static final SoundEvent ENTITY_RUBBLEMITE_EXTRUDE = register("entity.rubblemite.extrude");
    public static final SoundEvent ENTITY_RUBBLEMITE_HOP = register("entity.rubblemite.hop");
    public static final SoundEvent ENTITY_RUBBLEMITE_HURT = register("entity.rubblemite.hurt");
    public static final SoundEvent ENTITY_RUBBLEMITE_SHIELD = register("entity.rubblemite.shield");

    // Items

    public static final SoundEvent ITEM_DRIFT_BOOTS_EQUIP = register("item.drift_boots.equip");
    public static final SoundEvent ITEM_DRIFT_JELLY_BOTTLE_DRINK = register("item.drift_jelly_bottle.drink");

    public static final SoundEvent ITEM_ELYTRA_START = register("item.elytra.start");

    public static final SoundEvent ITEM_MIRROR_FAILURE = register("item.mirror.failure");
    public static final SoundEvent ITEM_MIRROR_LINK = register("item.mirror.link");
    public static final SoundEvent ITEM_MIRROR_TELEPORT = register("item.mirror.teleport");
    public static final SoundEvent ITEM_MIRROR_TELEPORT_INTERDIMENSIONAL = register("item.mirror.teleport.interdimensional");

    // Music

    public static final SoundEvent MUSIC_CELESTIAL = register("music.celestial");
    public static final SoundEvent MUSIC_CORRUPTION = register("music.corruption");
    public static final SoundEvent MUSIC_ELYTRA = register("music.elytra");
    public static final SoundEvent MUSIC_END = register("music.end");
    public static final SoundEvent MUSIC_GLARE = register("music.glare");
    public static final SoundEvent MUSIC_VOID = register("music.void");

    // Sound Groups

    public static final BlockSoundGroup CHORUS = new BlockSoundGroup(1, 1.15F, BLOCK_CHORUS_BREAK, BLOCK_CHORUS_STEP, BLOCK_CHORUS_PLACE, BLOCK_CHORUS_HIT, BLOCK_CHORUS_FALL);
    public static final BlockSoundGroup DEEP_FUNGUS = new BlockSoundGroup(0.8F, 1.2F, BLOCK_DEEP_FUNGUS_BREAK, BLOCK_DEEP_FUNGUS_STEP, BLOCK_DEEP_FUNGUS_PLACE, BLOCK_DEEP_FUNGUS_HIT, BLOCK_DEEP_FUNGUS_FALL);
    public static final BlockSoundGroup END_STONE = new BlockSoundGroup(1, 1.3F, BLOCK_END_STONE_BREAK, BLOCK_END_STONE_STEP, BLOCK_END_STONE_PLACE, BLOCK_END_STONE_HIT, BLOCK_END_STONE_FALL);
    public static final BlockSoundGroup END_STONE_BRICKS = new BlockSoundGroup(1, 1, BLOCK_END_STONE_BRICKS_BREAK, BLOCK_END_STONE_BRICKS_STEP, BLOCK_END_STONE_BRICKS_PLACE, BLOCK_END_STONE_BRICKS_HIT, BLOCK_END_STONE_BRICKS_FALL);
    public static final BlockSoundGroup EYLIUM = new BlockSoundGroup(1, 1.1F, BLOCK_EYLIUM_BREAK, BLOCK_EYLIUM_STEP, BLOCK_EYLIUM_PLACE, BLOCK_EYLIUM_HIT, BLOCK_EYLIUM_FALL);
    public static final BlockSoundGroup FLANGER_BERRY_VINE = new BlockSoundGroup(1, 0.9F, BLOCK_FLANGER_BERRY_VINE_BREAK, BLOCK_FLANGER_BERRY_VINE_STEP, BLOCK_FLANGER_BERRY_VINE_PLACE, BLOCK_FLANGER_BERRY_VINE_HIT, BLOCK_FLANGER_BERRY_VINE_FALL);
    public static final BlockSoundGroup GROWTH = new BlockSoundGroup(0.5F, 1, BLOCK_GROWTH_BREAK, BLOCK_GROWTH_STEP, BLOCK_GROWTH_PLACE, BLOCK_GROWTH_HIT, BLOCK_GROWTH_FALL);
    public static final BlockSoundGroup NEBULITE_BLOCK = new BlockSoundGroup(1.2F, 1.3F, SoundEvents.BLOCK_NETHERITE_BLOCK_BREAK, SoundEvents.BLOCK_NETHERITE_BLOCK_STEP, SoundEvents.BLOCK_NETHERITE_BLOCK_PLACE, SoundEvents.BLOCK_NETHERITE_BLOCK_HIT, SoundEvents.BLOCK_NETHERITE_BLOCK_FALL);
    public static final BlockSoundGroup NEBULITE_ORE = new BlockSoundGroup(1.2F, 1.3F, BLOCK_NEBULITE_ORE_BREAK, BLOCK_NEBULITE_ORE_STEP, BLOCK_NEBULITE_ORE_PLACE, BLOCK_NEBULITE_ORE_HIT, BLOCK_NEBULITE_ORE_FALL);
    public static final BlockSoundGroup PURPUR = new BlockSoundGroup(0.7F, 1.1F, BLOCK_PURPUR_BREAK, BLOCK_PURPUR_STEP, BLOCK_PURPUR_PLACE, BLOCK_PURPUR_HIT, BLOCK_PURPUR_FALL);
    public static final BlockSoundGroup SHADOW_QUARTZ = new BlockSoundGroup(1.1F, 1.2F, BLOCK_SHADOW_QUARTZ_BREAK, BLOCK_SHADOW_QUARTZ_STEP, BLOCK_SHADOW_QUARTZ_PLACE, BLOCK_SHADOW_QUARTZ_HIT, BLOCK_SHADOW_QUARTZ_FALL);
    public static final BlockSoundGroup SHULKER = new BlockSoundGroup(1, 1.3F, BLOCK_SHULKER_BREAK, BLOCK_SHULKER_STEP, BLOCK_SHULKER_PLACE, BLOCK_SHULKER_HIT, BLOCK_SHULKER_FALL);
    public static final BlockSoundGroup STEM = new BlockSoundGroup(1, 1.2F, BLOCK_STEM_BREAK, BLOCK_STEM_STEP, BLOCK_STEM_PLACE, BLOCK_STEM_HIT, BLOCK_STEM_FALL);

    private static SoundEvent register(String name) {
        Identifier id = Enderscape.id(name);
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
    }

    public static void init() {
    }
}