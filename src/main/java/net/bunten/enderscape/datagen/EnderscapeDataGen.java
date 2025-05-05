package net.bunten.enderscape.datagen;

import net.bunten.enderscape.registry.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class EnderscapeDataGen implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();

		pack.addProvider(EnderscapeAdvancementProvider::new);
		pack.addProvider(EnderscapeBannerPatternProvider::new);
		pack.addProvider(EnderscapeBannerPatternTagProvider::new);
		pack.addProvider(EnderscapeBiomeProvider::new);
		pack.addProvider(EnderscapeBiomeTagProvider::new);
		pack.addProvider(EnderscapeBlockLootProvider::new);
		pack.addProvider(EnderscapeBlockTagProvider::new);
		pack.addProvider(EnderscapeChestLootProvider::new);
		pack.addProvider(EnderscapeConfiguredFeatureProvider::new);
		pack.addProvider(EnderscapeDamageTypeProvider::new);
		pack.addProvider(EnderscapeDamageTypeTagProvider::new);
		pack.addProvider(EnderscapeEnchantmentProvider::new);
		pack.addProvider(EnderscapeEnchantmentTagProvider::new);
		pack.addProvider(EnderscapeEntityLootProvider::new);
		pack.addProvider(EnderscapeEntityTagProvider::new);
		pack.addProvider(EnderscapeItemTagProvider::new);
		pack.addProvider(EnderscapeJukeboxSongProvider::new);
		pack.addProvider(EnderscapeNoiseProvider::new);
		pack.addProvider(EnderscapePaintingVariantProvider::new);
		pack.addProvider(EnderscapePaintingVariantTagProvider::new);
		pack.addProvider(EnderscapePlacedFeatureProvider::new);
		pack.addProvider(EnderscapePoiTagProvider::new);
		pack.addProvider(EnderscapeRecipeProvider::new);
		pack.addProvider(EnderscapeSkyParametersProvider::new);
		pack.addProvider(EnderscapeTrialSpawnerProvider::new);
		pack.addProvider(EnderscapeTrimPatternProvider::new);
		pack.addProvider(EnderscapeTrimMaterialProvider::new);
		pack.addProvider(EnderscapeVaultLootProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder builder) {
		builder.add(Registries.BANNER_PATTERN, EnderscapeBannerPatterns::bootstrap);
		builder.add(Registries.BIOME, EnderscapeBiomes::bootstrap);
		builder.add(Registries.CONFIGURED_FEATURE, EnderscapeConfiguredFeatures::bootstrap);
		builder.add(Registries.DAMAGE_TYPE, EnderscapeDamageTypes::bootstrap);
		builder.add(Registries.ENCHANTMENT, EnderscapeEnchantments::bootstrap);
		builder.add(Registries.JUKEBOX_SONG, EnderscapeJukeboxSongs::bootstrap);
		builder.add(Registries.NOISE, EnderscapeNoiseParameters::bootstrap);
		builder.add(Registries.PAINTING_VARIANT, EnderscapePaintingVariants::bootstrap);
		builder.add(Registries.PLACED_FEATURE, EnderscapePlacedFeatures::bootstrap);
		builder.add(Registries.TRIAL_SPAWNER_CONFIG, EnderscapeTrialSpawnerConfigs::bootstrap);
		builder.add(Registries.TRIM_PATTERN, EnderscapeTrimPatterns::bootstrap);
		builder.add(Registries.TRIM_MATERIAL, EnderscapeTrimMaterials::bootstrap);

		builder.add(EnderscapeRegistries.SKY_PARAMETERS_KEY, EnderscapeSkyParameters::bootstrap);
	}
}