package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.block.dispenser.MirrorDispenserBehavior;
import net.bunten.enderscape.registry.tag.EnderscapeBiomeTags;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.data.worldgen.placement.EndPlacements;
import net.minecraft.sounds.Music;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;
import static net.bunten.enderscape.registry.EnderscapeItems.*;
import static net.minecraft.world.item.Items.CHORUS_FRUIT;
import static net.minecraft.world.level.block.Blocks.CHORUS_FLOWER;
import static net.minecraft.world.level.block.Blocks.CHORUS_PLANT;

public class EnderscapeModifications {

    private static final EnderscapeConfig CONFIG = EnderscapeConfig.getInstance();

    public static void registerCompostableItem(float chance, ItemLike item) {
        ComposterBlock.COMPOSTABLES.put(item.asItem(), chance);
    }

    private static void registerVanillaCompat() {
        registerCompostableItem(0.1F, VEILED_LEAF_PILE);
        registerCompostableItem(0.3F, WISP_SPROUTS);
        registerCompostableItem(0.3F, CELESTIAL_CAP);
        registerCompostableItem(0.3F, CELESTIAL_GROWTH);
        registerCompostableItem(0.3F, CHORUS_FRUIT);
        registerCompostableItem(0.3F, CORRUPT_GROWTH);
        registerCompostableItem(0.3F, MURUBLIGHT_CAP);
        registerCompostableItem(0.3F, VEILED_LEAVES);
        registerCompostableItem(0.5F, BLINKLIGHT);
        registerCompostableItem(0.5F, CHORUS_SPROUTS);
        registerCompostableItem(0.5F, DRY_END_GROWTH);
        registerCompostableItem(0.5F, WISP_GROWTH);
        registerCompostableItem(0.5F, FLANGER_BERRY);
        registerCompostableItem(0.5F, MURUBLIGHT_SHELF);
        registerCompostableItem(0.5F, VEILED_SAPLING);
        registerCompostableItem(0.65F, BULB_FLOWER);
        registerCompostableItem(0.65F, CELESTIAL_CHANTERELLE);
        registerCompostableItem(0.65F, MURUBLIGHT_CHANTERELLE);
        registerCompostableItem(0.65F, RIPE_FLANGER_BERRY_BLOCK);
        registerCompostableItem(0.65F, WISP_FLOWER);

        FuelRegistry.INSTANCE.add(VOID_SHALE, 200);

        FlammableBlockRegistry.getDefaultInstance().add(DRY_END_GROWTH, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(CHORUS_SPROUTS, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(WISP_GROWTH, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(WISP_SPROUTS, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(WISP_FLOWER, 60, 100);

        FlammableBlockRegistry.getDefaultInstance().add(CHORUS_PLANT, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(CHORUS_FLOWER, 5, 5);

        FlammableBlockRegistry.getDefaultInstance().add(VEILED_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(VEILED_WOOD, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_VEILED_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_VEILED_WOOD, 5, 5);

        FlammableBlockRegistry.getDefaultInstance().add(VEILED_PLANKS, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(VEILED_SLAB, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(VEILED_FENCE_GATE, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(VEILED_FENCE, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(VEILED_STAIRS, 5, 5);

        FlammableBlockRegistry.getDefaultInstance().add(VEILED_SAPLING, 15, 60);
        FlammableBlockRegistry.getDefaultInstance().add(VEILED_LEAVES, 30, 60);
        FlammableBlockRegistry.getDefaultInstance().add(VEILED_LEAF_PILE, 60, 20);

        FlammableBlockRegistry.getDefaultInstance().add(CELESTIAL_STEM, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(CELESTIAL_HYPHAE, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_CELESTIAL_STEM, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_CELESTIAL_HYPHAE, 5, 5);

        FlammableBlockRegistry.getDefaultInstance().add(CELESTIAL_PLANKS, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(CELESTIAL_SLAB, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(CELESTIAL_FENCE_GATE, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(CELESTIAL_FENCE, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(CELESTIAL_STAIRS, 5, 5);

        FlammableBlockRegistry.getDefaultInstance().add(CELESTIAL_CAP, 30, 60);

        FlammableBlockRegistry.getDefaultInstance().add(MURUBLIGHT_STEM, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(MURUBLIGHT_HYPHAE, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_MURUBLIGHT_STEM, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_MURUBLIGHT_HYPHAE, 5, 5);

        FlammableBlockRegistry.getDefaultInstance().add(MURUBLIGHT_PLANKS, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(MURUBLIGHT_SLAB, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(MURUBLIGHT_FENCE_GATE, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(MURUBLIGHT_FENCE, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(MURUBLIGHT_STAIRS, 5, 5);

        FlammableBlockRegistry.getDefaultInstance().add(MURUBLIGHT_CAP, 30, 60);

        FlammableBlockRegistry.getDefaultInstance().add(CELESTIAL_GROWTH, 60, 60);
        FlammableBlockRegistry.getDefaultInstance().add(BULB_FLOWER, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(FLANGER_BERRY_VINE, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(FLANGER_BERRY_FLOWER, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(UNRIPE_FLANGER_BERRY_BLOCK, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(RIPE_FLANGER_BERRY_BLOCK, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(CELESTIAL_CHANTERELLE, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(MURUBLIGHT_CHANTERELLE, 60, 100);

        FlammableBlockRegistry.getDefaultInstance().add(CORRUPT_GROWTH, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(BLINKLIGHT_VINES_BODY, 15, 60);
        FlammableBlockRegistry.getDefaultInstance().add(BLINKLIGHT_VINES_HEAD, 15, 60);
        FlammableBlockRegistry.getDefaultInstance().add(MURUBLIGHT_SHELF, 60, 100);

        StrippableBlockRegistry.register(VEILED_LOG, STRIPPED_VEILED_LOG);
        StrippableBlockRegistry.register(VEILED_WOOD, STRIPPED_VEILED_WOOD);
        StrippableBlockRegistry.register(CELESTIAL_STEM, STRIPPED_CELESTIAL_STEM);
        StrippableBlockRegistry.register(CELESTIAL_HYPHAE, STRIPPED_CELESTIAL_HYPHAE);
        StrippableBlockRegistry.register(MURUBLIGHT_STEM, STRIPPED_MURUBLIGHT_STEM);
        StrippableBlockRegistry.register(MURUBLIGHT_HYPHAE, STRIPPED_MURUBLIGHT_HYPHAE);

        DispenserBlock.registerBehavior(RUSTLE_BUCKET, new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultBehavior = new DefaultDispenseItemBehavior();

            @Override
            public ItemStack execute(BlockSource source, ItemStack stack) {
                DispensibleContainerItem item = (DispensibleContainerItem) stack.getItem();
                BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
                Level level = source.level();

                if (item.emptyContents(null, level, pos, null)) {
                    item.checkExtraContent(null, level, stack, pos);
                    return consumeWithRemainder(source, stack, new ItemStack(Items.BUCKET));
                } else {
                    return defaultBehavior.dispense(source, stack);
                }
            }
        });

        DispenserBlock.registerBehavior(MIRROR, new MirrorDispenserBehavior());
    }

    private static void createAmbienceModifications() {
        BiomeModifications.create(Enderscape.id("modify_end_ambience")).add(ModificationPhase.REPLACEMENTS, (context) -> context.getBiomeRegistryEntry().is(EnderscapeBiomeTags.HAS_MODIFIED_END_AMBIENCE), (selection, modification) -> {
            if (CONFIG.ambienceUpdateDefaultMusic) modification.getEffects().setMusic(new Music(EnderscapeBiomeSounds.DEFAULT_END.music(), 12000, 24000, false));

            if (CONFIG.ambienceUpdateDefaultLoop) modification.getEffects().setAmbientSound(EnderscapeBiomeSounds.DEFAULT_END.loop());
            if (CONFIG.ambienceUpdateDefaultAdditions) modification.getEffects().setAdditionsSound(new AmbientAdditionsSettings(EnderscapeBiomeSounds.DEFAULT_END.additions(), 0.00075));
            if (CONFIG.ambienceUpdateDefaultMood) modification.getEffects().setMoodSound(new AmbientMoodSettings(EnderscapeBiomeSounds.DEFAULT_END.mood(), 6000, 8, 2));
            if (CONFIG.ambienceUpdateDefaultParticles) modification.getEffects().setParticleConfig(new AmbientParticleSettings(EnderscapeParticles.VOID_STARS, 0.003F));

            if (CONFIG.ambienceUpdateDefaultSkyColor) modification.getEffects().setSkyColor(EnderscapeBiomes.DEFAULT_SKY_COLOR);
            if (CONFIG.ambienceUpdateDefaultFogColor) modification.getEffects().setFogColor(EnderscapeBiomes.DEFAULT_FOG_COLOR);

            if (CONFIG.ambienceUpdateDefaultGrassColor) modification.getEffects().setGrassColor(EnderscapeBiomes.DEFAULT_GRASS_COLOR);
            if (CONFIG.ambienceUpdateDefaultFoliageColor) modification.getEffects().setFoliageColor(EnderscapeBiomes.DEFAULT_FOLIAGE_COLOR);

            if (CONFIG.ambienceUpdateDefaultWaterColor) modification.getEffects().setWaterColor(EnderscapeBiomes.DEFAULT_WATER_COLOR);
            if (CONFIG.ambienceUpdateDefaultWaterFogColor) modification.getEffects().setWaterFogColor(EnderscapeBiomes.DEFAULT_WATER_FOG_COLOR);
        });
    }

    private static void createFeatureModifications() {
        BiomeModifications.create(Enderscape.id("add_global_features")).add(ModificationPhase.ADDITIONS, (context) -> context.getBiomeRegistryEntry().is(BiomeTags.IS_END) && !context.getBiomeRegistryEntry().is(EnderscapeBiomeTags.EXCLUDED_FROM_GLOBAL_FEATURE_ADDITIONS), (selection, modification) -> {
            modification.getGenerationSettings().addFeature(Decoration.UNDERGROUND_ORES, EnderscapePlacedFeatures.VOID_SHALE);
            modification.getGenerationSettings().addFeature(Decoration.UNDERGROUND_ORES, EnderscapePlacedFeatures.VOID_SHALE_BLOBS);

            modification.getGenerationSettings().addFeature(Decoration.UNDERGROUND_ORES, EnderscapePlacedFeatures.VERADITE);
            modification.getGenerationSettings().addFeature(Decoration.UNDERGROUND_ORES, EnderscapePlacedFeatures.MIRESTONE_BLOBS);

            modification.getGenerationSettings().addFeature(Decoration.UNDERGROUND_ORES, EnderscapePlacedFeatures.SHADOLINE_ORE);
            modification.getGenerationSettings().addFeature(Decoration.UNDERGROUND_ORES, EnderscapePlacedFeatures.SCATTERED_SHADOLINE_ORE);

            modification.getGenerationSettings().addFeature(Decoration.UNDERGROUND_ORES, EnderscapePlacedFeatures.NEBULITE_ORE);
            modification.getGenerationSettings().addFeature(Decoration.UNDERGROUND_ORES, EnderscapePlacedFeatures.CEILING_NEBULITE_ORE);
        });

        BiomeModifications.create(Enderscape.id("add_new_barrens_content")).add(ModificationPhase.ADDITIONS, context -> context.getBiomeRegistryEntry().is(EnderscapeBiomeTags.INCLUDES_NEW_BARRENS_CONTENT), (selection, modification) -> {
            modification.getSpawnSettings().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EnderscapeEntities.RUBBLEMITE, 4, 2, 3));

            modification.getGenerationSettings().addFeature(Decoration.VEGETAL_DECORATION, EnderscapePlacedFeatures.DRY_END_GROWTH);
            modification.getGenerationSettings().addFeature(Decoration.VEGETAL_DECORATION, EnderscapePlacedFeatures.MURUBLIGHT_SHELF);
        });

        BiomeModifications.create(Enderscape.id("add_enderscape_chorus_sprouts")).add(ModificationPhase.ADDITIONS, context -> context.hasPlacedFeature(EndPlacements.CHORUS_PLANT) || context.getBiomeKey() == Biomes.END_MIDLANDS, (selection, modification) -> modification.getGenerationSettings().addFeature(Decoration.VEGETAL_DECORATION, EnderscapePlacedFeatures.CHORUS_SPROUTS));
        BiomeModifications.create(Enderscape.id("add_enderscape_chorus_plants")).add(ModificationPhase.ADDITIONS, context -> context.hasPlacedFeature(EndPlacements.CHORUS_PLANT) || context.getBiomeKey() == Biomes.END_MIDLANDS, (selection, modification) -> modification.getGenerationSettings().addFeature(Decoration.VEGETAL_DECORATION, EnderscapePlacedFeatures.CHORUS_PLANTS));
        BiomeModifications.create(Enderscape.id("remove_minecraft_chorus_plants")).add(ModificationPhase.REMOVALS, context -> context.hasPlacedFeature(EndPlacements.CHORUS_PLANT), (selection, modification) -> modification.getGenerationSettings().removeFeature(EndPlacements.CHORUS_PLANT));

        BiomeModifications.create(Enderscape.id("add_enderscape_islands")).add(ModificationPhase.ADDITIONS, context -> context.hasPlacedFeature(EndPlacements.END_ISLAND_DECORATED), (selection, modification) -> modification.getGenerationSettings().addFeature(Decoration.RAW_GENERATION, EnderscapePlacedFeatures.SMALL_ISLANDS));
        BiomeModifications.create(Enderscape.id("remove_small_end_islands")).add(ModificationPhase.REMOVALS, context -> context.hasPlacedFeature(EndPlacements.END_ISLAND_DECORATED), (selection, modification) -> modification.getGenerationSettings().removeFeature(EndPlacements.END_ISLAND_DECORATED));

        BiomeModifications.create(Enderscape.id("remove_minecraft_end_gateways")).add(ModificationPhase.REMOVALS, context -> context.hasPlacedFeature(EndPlacements.END_GATEWAY_RETURN), (selection, modification) -> modification.getGenerationSettings().removeFeature(EndPlacements.END_GATEWAY_RETURN));
    }

    static {
        registerVanillaCompat();
        createFeatureModifications();
        createAmbienceModifications();
    }
}