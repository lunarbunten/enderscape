package net.bunten.enderscape.datagen;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.registry.*;
import net.bunten.enderscape.structure.EnderscapeProcessorLists;
import net.bunten.enderscape.structure.EnderscapeStructures;
import net.bunten.enderscape.structure.EnderscapeTemplatePools;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Function;

@EventBusSubscriber(modid = Enderscape.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class EnderscapeDataGen {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		var registrySetBuilder = new RegistrySetBuilder();
		buildRegistry(registrySetBuilder);
		event.createDatapackRegistryObjects(registrySetBuilder);

		addProvider(event, EnderscapeAdvancementProvider::new);
		addProvider(event, EnderscapeBannerPatternTagProvider::new);
		addProvider(event, EnderscapeBiomeTagProvider::new);
		addProvider(event, EnderscapeLootProvider::new);
		var blockTagProvider = new EnderscapeBlockTagProvider(event);
		event.addProvider(blockTagProvider);
		addProvider(event, EnderscapeDamageTypeTagProvider::new);
		addProvider(event, EnderscapeEnchantmentTagProvider::new);
		addProvider(event, EnderscapeEntityTagProvider::new);
		event.addProvider(new EnderscapeItemTagProvider(event, blockTagProvider));

		addProvider(event, EnderscapePaintingVariantTagProvider::new);
		addProvider(event, EnderscapePoiTagProvider::new);
		addProvider(event, EnderscapeRecipeProvider::new);
		addProvider(event, EnderscapeDataMapProvider::new);
	}

	private static void addProvider(GatherDataEvent event, Function<GatherDataEvent, DataProvider> provider) {
		event.addProvider(provider.apply(event));
	}

	private static void buildRegistry(RegistrySetBuilder builder) {
		builder.add(Registries.BANNER_PATTERN, new EnderscapeBannerPatterns()::bootstrap);
		builder.add(Registries.BIOME, new EnderscapeBiomes()::bootstrap);
		builder.add(Registries.CONFIGURED_FEATURE,new  EnderscapeConfiguredFeatures()::bootstrap);
		builder.add(Registries.DAMAGE_TYPE, new EnderscapeDamageTypes()::bootstrap);
		builder.add(Registries.ENCHANTMENT, new EnderscapeEnchantments()::bootstrap);
		builder.add(Registries.JUKEBOX_SONG, new EnderscapeJukeboxSongs()::bootstrap);
		builder.add(Registries.NOISE, new EnderscapeNoiseParameters()::bootstrap);
		builder.add(Registries.PAINTING_VARIANT, new EnderscapePaintingVariants()::bootstrap);
		builder.add(Registries.PLACED_FEATURE, new EnderscapePlacedFeatures()::bootstrap);
		builder.add(Registries.PROCESSOR_LIST, new EnderscapeProcessorLists()::bootstrap);
		builder.add(Registries.STRUCTURE, new EnderscapeStructures()::bootstrap);
		builder.add(Registries.TEMPLATE_POOL, new EnderscapeTemplatePools()::bootstrap);
		builder.add(Registries.TRIM_MATERIAL, new EnderscapeTrimMaterials()::bootstrap);
		builder.add(Registries.TRIM_PATTERN, new EnderscapeTrimPatterns()::bootstrap);
		builder.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, new EnderscapeBiomeModifiers()::bootstrap);

		builder.add(EnderscapeRegistries.MAGNIA_RADIO_SONG, new EnderscapeMagniaRadioSongs()::bootstrap);
		builder.add(EnderscapeRegistries.BIOME_PARAMETERS_KEY, new EnderscapeBiomeParameters()::bootstrap);
		builder.add(EnderscapeRegistries.STRUCTURE_MUSIC, new EnderscapeStructureMusic()::bootstrap);
	}
}