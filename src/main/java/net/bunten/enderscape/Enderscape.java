package net.bunten.enderscape;

import com.google.common.reflect.Reflection;
import net.bunten.enderscape.compat.EnderscapeTerrablender;
import net.bunten.enderscape.datagen.EnderscapeBiomeModifiers;
import net.bunten.enderscape.registry.*;
import net.bunten.enderscape.sound.StructureMusicHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Enderscape.MOD_ID)
public class Enderscape {

    public static final String MOD_ID = "enderscape";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final boolean IS_DEBUG = !FMLLoader.isProduction();

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static SoundEvent registerSoundEvent(String name) {
        ResourceLocation id = id(name);
        var soundEvent = SoundEvent.createVariableRangeEvent(id);
        RegistryHelper.register(BuiltInRegistries.SOUND_EVENT, id, () -> soundEvent);
        return soundEvent;
    }

    public static Holder<SoundEvent> registerSoundEventHolder(String name) {
        ResourceLocation location = id(name);
        return RegistryHelper.registerForHolder(BuiltInRegistries.SOUND_EVENT, location, () -> SoundEvent.createVariableRangeEvent(location));
    }

    EnderscapeConfig config = EnderscapeConfig.getInstance();

    public Enderscape(IEventBus modBus, ModContainer modContainer) {
        Reflection.initialize(
                EnderscapeConfig.class,
                EnderscapeItems.class,
                EnderscapeBlocks.class,
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
                EnderscapeEventSounds.class,
                EnderscapeItemSounds.class,
                EnderscapeGameEvents.class,
                EnderscapeMusic.class,
                EnderscapeSoundTypes.class,
                EnderscapePoi.class,
                EnderscapeParticles.class,
                EnderscapeEnchantmentEffectComponents.class,
                EnderscapePaintingVariants.class,
                EnderscapeMobEffects.class,
                EnderscapeModifications.class,
                EnderscapeEntities.class,
                EnderscapeSubEntityPredicates.class,
                EnderscapeStats.class,
                EnderscapeCriteria.class,
                EnderscapeServerNetworking.class,
                EnderscapeCompatibility.class,
                EnderscapeSoundTypeOverrides.class,
                EnderscapeCreativeModeTab.class,
                EnderscapeDataComponents.class,
                EnderscapeDensityFunctionTypes.class,
                EnderscapeRecipeSerializers.class,
                EnderscapeBiomeModifiers.class,
                EnderscapeDataAttachments.class,
                EnderscapeEntityDataSerializers.class,
                StructureMusicHandler.class
        );
        
        if (ModList.get().isLoaded("terrablender")) {
            EnderscapeTerrablender.initialize();
        }

        modBus.addListener(AddPackFindersEvent.class, event -> {
            registerResourcePack(event, Enderscape.id("fix_levitation_advancement"), Component.translatable("pack.enderscape.fix_levitation_advancement"), config.defaultDataPackFixLevitationAdvancement);
            registerResourcePack(event, Enderscape.id("fix_vanilla_recipes"), Component.translatable("pack.enderscape.fix_vanilla_recipes"), config.defaultDataPackFixVanillaRecipes);
            registerResourcePack(event, Enderscape.id("new_end_cities"), Component.translatable("pack.enderscape.new_end_cities"), config.defaultDataPackNewEndCities);
            registerResourcePack(event, Enderscape.id("new_strongholds"), Component.translatable("pack.enderscape.new_strongholds"), config.defaultDataPackNewStrongholds);
            registerResourcePack(event, Enderscape.id("new_terrain"), Component.translatable("pack.enderscape.new_terrain"), config.defaultDataPackNewTerrain);
        });

        LOGGER.info("Enderscape initialized!");
    }

    private void registerResourcePack(AddPackFindersEvent event, ResourceLocation id, Component name, boolean enabled) {
        event.addPackFinders(id.withPrefix("resourcepacks/"), PackType.SERVER_DATA, name, PackSource.DEFAULT, enabled, Pack.Position.TOP);
    }
}