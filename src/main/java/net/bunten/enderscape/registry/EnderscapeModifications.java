package net.bunten.enderscape.registry;

import java.util.ArrayList;
import java.util.List;

import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.blocks.SoundTypeModification;
import net.bunten.enderscape.config.Config;
import net.bunten.enderscape.items.ChanceFunction;
import net.bunten.enderscape.world.EnderscapeBiomes;
import net.bunten.enderscape.world.biomes.EnderscapeBiome;
import net.bunten.enderscape.world.placed.BarrenEndFeatures;
import net.bunten.enderscape.world.placed.GeneralEndFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.EndPlacements;
import net.minecraft.sounds.Music;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class EnderscapeModifications {

    public static final List<SoundTypeModification> MODIFICATIONS = new ArrayList<>();

    public static final SoundTypeModification CHORUS = new SoundTypeModification(EnderscapeSounds.CHORUS, (type) -> Config.BLOCK_SOUNDS.useChorusSounds());
    public static final SoundTypeModification CHORUS_FLOWER = new SoundTypeModification(EnderscapeSounds.CHORUS_FLOWER, (type) -> Config.BLOCK_SOUNDS.useChorusSounds());
    public static final SoundTypeModification PURPUR = new SoundTypeModification(EnderscapeSounds.PURPUR, (type) -> Config.BLOCK_SOUNDS.usePurpurSounds());
    public static final SoundTypeModification END_ROD = new SoundTypeModification(EnderscapeSounds.END_ROD, (type) -> Config.BLOCK_SOUNDS.useEndRodSounds());
    public static final SoundTypeModification END_STONE = new SoundTypeModification(EnderscapeSounds.END_STONE, (type) -> Config.BLOCK_SOUNDS.useEndStoneSounds());
    public static final SoundTypeModification END_STONE_BRICKS = new SoundTypeModification(EnderscapeSounds.END_STONE_BRICKS, (type) -> Config.BLOCK_SOUNDS.useEndStoneBrickSounds());
    public static final SoundTypeModification END_ORE = new SoundTypeModification(EnderscapeSounds.SHADOW_QUARTZ_ORE, (type) -> Config.BLOCK_SOUNDS.useEndStoneSounds());
    public static final SoundTypeModification SHULKER = new SoundTypeModification(EnderscapeSounds.SHULKER, (type) -> Config.BLOCK_SOUNDS.useShulkerSounds());

    public static final LootItemFunctionType CHANCE = register("chance", new ChanceFunction.Serializer());

    private static LootItemFunctionType register(String name, Serializer<? extends LootItemFunction> serializer) {
        return Registry.register(Registry.LOOT_FUNCTION_TYPE, Enderscape.id(name), new LootItemFunctionType(serializer));
    }

    private static void addVanillaBlocks() {
        CHORUS.add("chorus_plant");
        CHORUS_FLOWER.add("chorus_flower");

        PURPUR.add("purpur_block");
        PURPUR.add("purpur_pillar");
        PURPUR.add("purpur_stairs");
        PURPUR.add("purpur_slab");
        
        END_ROD.add("end_rod");

        END_STONE.add("end_stone");

        END_STONE_BRICKS.add("end_stone_bricks");
        END_STONE_BRICKS.add("end_stone_brick_stairs");
        END_STONE_BRICKS.add("end_stone_brick_slab");
        END_STONE_BRICKS.add("end_stone_brick_wall");
        END_STONE_BRICKS.add("end_portal_frame");
    
        SHULKER.add("shulker_box");

        for (DyeColor color : DyeColor.values()) {
            SHULKER.add(color.getName() + "_shulker_box");
        }
    }

    private static void addEndlessEncoreBlocks() {
        END_STONE_BRICKS.add("cracked_end_stone_bricks");
        END_STONE_BRICKS.add("end_stone_pillar");
        END_STONE_BRICKS.add("chiseled_end_stone");

        END_ORE.add("end_diamond_ore");
        END_ORE.add("end_iolite_ore");

        PURPUR.add("chiseled_purpur");
        PURPUR.add("purpur_wall");
        PURPUR.add("end_lamp");

        PURPUR.add("purepur_block");
        PURPUR.add("purepur_stairs");
        PURPUR.add("purepur_slab");
        PURPUR.add("purepur_wall");
        PURPUR.add("purepur_pillar");
        PURPUR.add("chiseled_purepur");

        PURPUR.add("grongron");
        PURPUR.add("grongron_tiles");
        PURPUR.add("grongron_tile_stairs");
        PURPUR.add("grongron_tile_slab");
        PURPUR.add("grongron_tile_wall");
        PURPUR.add("chiseled_grongron");
        PURPUR.add("grongron_pillar");
    }

    private static void addLootInjection(LootTable.Builder builder, String id) {
        builder.pool(LootPool.lootPool().add(LootTableReference.lootTableReference(Enderscape.id("inject/" + id)).setWeight(1).setQuality(0)).build());
    }

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, builder, source) -> {
            if (BuiltInLootTables.END_CITY_TREASURE.equals(id)) {
                if (Config.LOOT.addEndCityFood()) addLootInjection(builder, "end_city_food");
                if (Config.LOOT.addEndCityEnchantments()) addLootInjection(builder, "end_city_lightspeed");
                if (Config.LOOT.addEndCityMusicDiscs()) addLootInjection(builder, "end_city_music_discs");
                if (Config.LOOT.addEndCityTools()) addLootInjection(builder, "end_city_tools");
            }
            
            if (BuiltInLootTables.NETHER_BRIDGE.equals(id)) {
                if (Config.LOOT.addNetherFortressAccommodations()) addLootInjection(builder, "nether_fortress_accommodations");
            }
        });
    }

    private static void modifyBiomeAmbience() {
        BiomeModifications.create(Enderscape.id("modify_end_ambience")).add(ModificationPhase.REPLACEMENTS, (context) -> context.getBiomeRegistryEntry().is(EnderscapeBiomes.MODIFIED_END_AMBIENCE), (selectionContext, modificationContext) -> {
            if (Config.AMBIENCE.modifyDefaultMusic()) modificationContext.getEffects().setMusic(new Music(EnderscapeSounds.DEFAULT_END_MUSIC, 12000, 24000, false));
            
            if (Config.AMBIENCE.modifyDefaultAmbience()) modificationContext.getEffects().setAmbientSound(EnderscapeSounds.DEFAULT_END_LOOP);
            if (Config.AMBIENCE.modifyDefaultAdditions()) modificationContext.getEffects().setAdditionsSound(new AmbientAdditionsSettings(EnderscapeSounds.DEFAULT_END_ADDITIONS, 0.001));
            if (Config.AMBIENCE.modifyDefaultMood()) modificationContext.getEffects().setMoodSound(new AmbientMoodSettings(EnderscapeSounds.DEFAULT_END_MOOD, 6000, 8, 2));
            if (Config.AMBIENCE.modifyDefaultParticles()) modificationContext.getEffects().setParticleConfig(new AmbientParticleSettings(EnderscapeParticles.AMBIENT_STARS, 0.002F));
            
            if (Config.AMBIENCE.modifyDefaultSkyColor()) modificationContext.getEffects().setSkyColor(EnderscapeBiome.DEFAULT_SKY_COLOR);
            if (Config.AMBIENCE.modifyDefaultFogColor()) modificationContext.getEffects().setFogColor(EnderscapeBiome.DEFAULT_FOG_COLOR);
        });
    }

    private static void addGlobalBiomeModifications(Holder<Biome> biome) {
        BiomeAPI.addBiomeFeature(biome, GeneralEndFeatures.VERADITE);
        BiomeAPI.addBiomeFeature(biome, GeneralEndFeatures.SCATTERED_VERADITE);
        
        BiomeAPI.addBiomeFeature(biome, GeneralEndFeatures.SHADOW_QUARTZ_ORE);
        BiomeAPI.addBiomeFeature(biome, GeneralEndFeatures.SCATTERED_SHADOW_QUARTZ_ORE);

        BiomeAPI.addBiomeFeature(biome, GeneralEndFeatures.NEBULITE_ORE);
        BiomeAPI.addBiomeFeature(biome, GeneralEndFeatures.VOID_NEBULITE_ORE);
    }

    @SuppressWarnings("unchecked")
    private static void addDefaultEndBiomeModifications(Holder<Biome> biome, boolean midlands) {
        BiomeAPI.addBiomeMobSpawn(biome, EnderscapeEntities.RUBBLEMITE, 2, 2, 4);
        BiomeAPI.addBiomeFeature(biome, BarrenEndFeatures.MURUSHROOMS);

        if (midlands) {
            BiomeAPI.addBiomeFeature(biome, GenerationStep.Decoration.SURFACE_STRUCTURES, EndPlacements.END_GATEWAY_RETURN);
            BiomeAPI.addBiomeFeature(biome, GenerationStep.Decoration.VEGETAL_DECORATION, EndPlacements.CHORUS_PLANT);
        }
    }

    private static void modifyBiomes() {
        BiomeAPI.registerEndBiomeModification((id, biome) -> {
            if (id == Biomes.THE_END.location()) return;

			if (id.getNamespace() != Enderscape.MOD_ID) {
                addGlobalBiomeModifications(biome);

                boolean highlands = id == Biomes.END_HIGHLANDS.location();
                boolean midlands = id == Biomes.END_MIDLANDS.location();
                
                if (highlands || midlands) {
                    addDefaultEndBiomeModifications(biome, midlands);
                }
			}
		});
    }
    
    static {
        addVanillaBlocks();
        addEndlessEncoreBlocks();
        modifyBiomes();
        modifyBiomeAmbience();
        modifyLootTables();
    }
}