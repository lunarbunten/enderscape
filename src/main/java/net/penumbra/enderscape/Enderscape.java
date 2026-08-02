package net.penumbra.enderscape;

import com.google.common.reflect.Reflection;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.TheEndBiomes;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.Biome;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.manager.StructureMusicManager;
import net.penumbra.enderscape.registry.EnderscapeIntegration;
import net.penumbra.enderscape.registry.EnderscapePacks;
import net.penumbra.enderscape.registry.EnderscapeRegistries;
import net.penumbra.enderscape.registry.block.EnderscapeBlockEntities;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import net.penumbra.enderscape.registry.block.EnderscapeFluids;
import net.penumbra.enderscape.registry.component.EnderscapeDataComponents;
import net.penumbra.enderscape.registry.enchantment.EnderscapeEnchantmentEffectComponents;
import net.penumbra.enderscape.registry.entity.*;
import net.penumbra.enderscape.registry.item.EnderscapeCreativeModeTab;
import net.penumbra.enderscape.registry.item.EnderscapeItems;
import net.penumbra.enderscape.registry.item.EnderscapePotions;
import net.penumbra.enderscape.registry.item.EnderscapeRecipeSerializers;
import net.penumbra.enderscape.registry.level.*;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;
import net.penumbra.enderscape.registry.server.EnderscapeCommands;
import net.penumbra.enderscape.registry.server.EnderscapeCriteria;
import net.penumbra.enderscape.registry.server.EnderscapeGameRules;
import net.penumbra.enderscape.registry.server.EnderscapeServerNetworking;
import net.penumbra.enderscape.registry.sound.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class Enderscape implements ModInitializer {

    public static final String MOD_ID = "enderscape";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final boolean IS_DEBUG = FabricLoader.getInstance().isDevelopmentEnvironment();

    public static Identifier id(String path) {
        return Identifier.tryBuild(MOD_ID, path);
    }

    public static SoundEvent registerSoundEvent(String name) {
        Identifier id = id(name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static Holder.Reference<SoundEvent> registerSoundEventHolder(String name) {
        Identifier location = id(name);
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, location, SoundEvent.createVariableRangeEvent(location));
    }

    private static void addBiomeForVanillaWorldgen(ResourceKey<Biome> key, double weight) {
        TheEndBiomes.addHighlandsBiome(key, weight);
        TheEndBiomes.addMidlandsBiome(key, key, weight);
        TheEndBiomes.addBarrensBiome(key, key, weight);
    }

    @Override
    public void onInitialize() {

        Reflection.initialize(
                EnderscapeConfig.class,
                EnderscapeRegistries.class,
                EnderscapeEnvironmentAttributes.class,
                EnderscapeItems.class,
                EnderscapeBlocks.class,
                EnderscapeFluids.class,
                EnderscapePotions.class,
                EnderscapeAttributes.class,
                EnderscapeStructureMusic.class,
                EnderscapeFeatures.class,
                EnderscapeConfiguredFeatures.class,
                EnderscapePlacedFeatures.class,
                EnderscapeBlockEntities.class,
                EnderscapeBiomeSounds.class,
                EnderscapeBlockSounds.class,
                EnderscapeEntitySounds.class,
                EnderscapeMobEffectSounds.class,
                EnderscapeItemSounds.class,
                EnderscapeUiSounds.class,
                EnderscapeGameEvents.class,
                EnderscapeGameRules.class,
                EnderscapeMusic.class,
                EnderscapeSoundTypes.class,
                EnderscapePoi.class,
                EnderscapeParticles.class,
                EnderscapeEnchantmentEffectComponents.class,
                EnderscapePaintingVariants.class,
                EnderscapeMobEffects.class,
                EnderscapeEntities.class,
                EnderscapeEntityDataSerializers.class,
                EnderscapeSubEntityPredicates.class,
                EnderscapeMagniaInteractionBehaviors.class,
                EnderscapeStats.class,
                EnderscapeCriteria.class,
                EnderscapeCommands.class,
                EnderscapeServerNetworking.class,
                EnderscapeIntegration.class,
                EnderscapeBiomeModifications.class,
                EnderscapeSoundTypeOverrides.class,
                EnderscapeCreativeModeTab.class,
                EnderscapeDataComponents.class,
                EnderscapeDensityFunctionTypes.class,
                EnderscapeSpawnConditionTypes.class,
                EnderscapeRecipeSerializers.class,
                EnderscapeAttachments.class,
                StructureMusicManager.class
        );

        addBiomeForVanillaWorldgen(EnderscapeBiomes.MAGNIA_FIELDS, 0.8);
        addBiomeForVanillaWorldgen(EnderscapeBiomes.VEILED_WOODLANDS, 0.7);
        addBiomeForVanillaWorldgen(EnderscapeBiomes.CORRUPT_BARRENS, 0.5);
        addBiomeForVanillaWorldgen(EnderscapeBiomes.CELESTIAL_GROVE, 0.3);

        FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container -> {
            EnderscapeConfig config = EnderscapeConfig.getInstance();

            registerBuiltInPack(EnderscapePacks.IMPROVED_VISUALS, container, config.defaultResourcePackImprovedVisuals);

            registerBuiltInPack(EnderscapePacks.FIX_LEVITATION_ADVANCEMENT, container, config.defaultDataPackFixLevitationAdvancement);
            registerBuiltInPack(EnderscapePacks.FIX_VANILLA_RECIPES, container, config.defaultDataPackFixVanillaRecipes);
            registerBuiltInPack(EnderscapePacks.NEW_END_CITIES, container, config.defaultDataPackNewEndCities);
            registerBuiltInPack(EnderscapePacks.NEW_STRONGHOLDS, container, config.defaultDataPackNewStrongholds);
            registerBuiltInPack(EnderscapePacks.NEW_TERRAIN, container, config.defaultDataPackNewTerrain);
        });

        LOGGER.info("Enderscape initialized!");
    }

    private static void registerBuiltInPack(Identifier name, ModContainer container, boolean config) {
        ResourceLoader.registerBuiltinPack(name, container, Component.translatable("pack.enderscape." + name.getPath()), getActivationType(config));
    }

    @NotNull
    private static PackActivationType getActivationType(boolean value) {
        return value ? PackActivationType.DEFAULT_ENABLED : PackActivationType.NORMAL;
    }
}
