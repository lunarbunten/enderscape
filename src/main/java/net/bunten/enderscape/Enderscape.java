package net.bunten.enderscape;

import com.google.common.reflect.Reflection;
import net.bunten.enderscape.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.TheEndBiomes;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Enderscape implements ModInitializer {

    public static final String MOD_ID = "enderscape";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final boolean IS_DEBUG = FabricLoader.getInstance().isDevelopmentEnvironment();

    public static final ResourceKey<Structure> END_CITY_RESOURCE_KEY = ResourceKey.create(Registries.STRUCTURE, Enderscape.id("end_city"));

    public static ResourceLocation id(String path) {
        return ResourceLocation.tryBuild(MOD_ID, path);
    }

    public static SoundEvent registerSoundEvent(String name) {
        ResourceLocation id = id(name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static Holder.Reference<SoundEvent> registerSoundEventHolder(String name) {
        ResourceLocation location = id(name);
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
                EnderscapeItems.class,
                EnderscapeBlocks.class,
                EnderscapeFeatures.class,
                EnderscapeConfiguredFeatures.class,
                EnderscapePlacedFeatures.class,
                EnderscapeBlockEntities.class,
                EnderscapeBiomeSounds.class,
                EnderscapeBlockSounds.class,
                EnderscapeEntitySounds.class,
                EnderscapeItemSounds.class,
                EnderscapeMusic.class,
                EnderscapeSoundTypes.class,
                EnderscapePoi.class,
                EnderscapeParticles.class,
                EnderscapeEnchantmentEffectComponents.class,
                EnderscapePaintingVariants.class,
                EnderscapeMobEffects.class,
                EnderscapeAssetModifications.class,
                EnderscapeEntities.class,
                EnderscapeSubEntityPredicates.class,
                EnderscapeStats.class,
                EnderscapeCriteria.class,
                EnderscapeServerNetworking.class,
                EnderscapeModifications.class,
                EnderscapeSoundTypeOverrides.class,
                EnderscapeCreativeModeTab.class,
                EnderscapeDataComponents.class,
                EnderscapeDensityFunctionTypes.class,
                EnderscapeStructureMusic.class
        );

        addBiomeForVanillaWorldgen(EnderscapeBiomes.MAGNIA_CRAGS, 0.8);
        addBiomeForVanillaWorldgen(EnderscapeBiomes.VEILED_WOODLANDS, 0.7);
        addBiomeForVanillaWorldgen(EnderscapeBiomes.CORRUPT_BARRENS, 0.5);
        addBiomeForVanillaWorldgen(EnderscapeBiomes.CELESTIAL_GROVE, 0.3);

        FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(container -> {
            ResourceManagerHelper.registerBuiltinResourcePack(Enderscape.id("fix_levitation_advancement"), container, Component.translatable("pack.enderscape.fix_levitation_advancement"), ResourcePackActivationType.DEFAULT_ENABLED);
            ResourceManagerHelper.registerBuiltinResourcePack(Enderscape.id("fix_vanilla_recipes"), container, Component.translatable("pack.enderscape.fix_vanilla_recipes"), ResourcePackActivationType.DEFAULT_ENABLED);
            ResourceManagerHelper.registerBuiltinResourcePack(Enderscape.id("new_end_cities"), container, Component.translatable("pack.enderscape.new_end_cities"), ResourcePackActivationType.DEFAULT_ENABLED);
            ResourceManagerHelper.registerBuiltinResourcePack(Enderscape.id("new_terrain"), container, Component.translatable("pack.enderscape.new_terrain"), ResourcePackActivationType.DEFAULT_ENABLED);
        });

        LOGGER.info("Enderscape initialized!");
    }
}