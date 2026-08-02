package net.penumbra.enderscape.datagen;

import dev.worldgen.lithostitched.api.registry.LithostitchedRegistries;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.penumbra.enderscape.datagen.advancement.EnderscapeAdvancementProvider;
import net.penumbra.enderscape.datagen.block.EnderscapeBannerPatternProvider;
import net.penumbra.enderscape.datagen.block.EnderscapeTrialSpawnerProvider;
import net.penumbra.enderscape.datagen.enchantment.EnderscapeEnchantmentProvider;
import net.penumbra.enderscape.datagen.entity.EnderscapeDamageTypeProvider;
import net.penumbra.enderscape.datagen.entity.EnderscapePaintingVariantProvider;
import net.penumbra.enderscape.datagen.entity.EnderscapeRubblemiteVariantProvider;
import net.penumbra.enderscape.datagen.item.EnderscapeTrimMaterialProvider;
import net.penumbra.enderscape.datagen.item.EnderscapeTrimPatternProvider;
import net.penumbra.enderscape.datagen.level.EnderscapeBiomeProvider;
import net.penumbra.enderscape.datagen.level.EnderscapeConfiguredFeatureProvider;
import net.penumbra.enderscape.datagen.level.EnderscapeNoiseProvider;
import net.penumbra.enderscape.datagen.level.EnderscapePlacedFeatureProvider;
import net.penumbra.enderscape.datagen.lithostitched.EnderscapeWorldgenModifierProvider;
import net.penumbra.enderscape.datagen.loot.EnderscapeBlockLootProvider;
import net.penumbra.enderscape.datagen.loot.EnderscapeChestLootProvider;
import net.penumbra.enderscape.datagen.loot.EnderscapeEntityLootProvider;
import net.penumbra.enderscape.datagen.loot.EnderscapeVaultLootProvider;
import net.penumbra.enderscape.datagen.model.EnderscapeModelProvider;
import net.penumbra.enderscape.datagen.recipe.EnderscapeRecipeProvider;
import net.penumbra.enderscape.datagen.sound.EnderscapeJukeboxSongProvider;
import net.penumbra.enderscape.datagen.sound.EnderscapeMagniaRadioSongProvider;
import net.penumbra.enderscape.datagen.sound.EnderscapeStructureMusicProvider;
import net.penumbra.enderscape.datagen.structure.EnderscapeProcessorListProvider;
import net.penumbra.enderscape.datagen.structure.EnderscapeStructureProvider;
import net.penumbra.enderscape.datagen.structure.EnderscapeStructureSetProvider;
import net.penumbra.enderscape.datagen.structure.EnderscapeTemplatePoolProvider;
import net.penumbra.enderscape.datagen.tag.*;
import net.penumbra.enderscape.registry.EnderscapeRegistries;
import net.penumbra.enderscape.registry.block.EnderscapeBannerPatterns;
import net.penumbra.enderscape.registry.block.EnderscapeTrialSpawnerConfigs;
import net.penumbra.enderscape.registry.enchantment.EnderscapeEnchantments;
import net.penumbra.enderscape.registry.entity.EnderscapeDamageTypes;
import net.penumbra.enderscape.registry.entity.EnderscapePaintingVariants;
import net.penumbra.enderscape.registry.entity.EnderscapeRubblemiteVariants;
import net.penumbra.enderscape.registry.item.EnderscapeTrimMaterials;
import net.penumbra.enderscape.registry.item.EnderscapeTrimPatterns;
import net.penumbra.enderscape.registry.level.EnderscapeBiomes;
import net.penumbra.enderscape.registry.level.EnderscapeConfiguredFeatures;
import net.penumbra.enderscape.registry.level.EnderscapeNoiseParameters;
import net.penumbra.enderscape.registry.level.EnderscapePlacedFeatures;
import net.penumbra.enderscape.registry.lithostitched.EnderscapeWorldgenModifiers;
import net.penumbra.enderscape.registry.sound.EnderscapeJukeboxSongs;
import net.penumbra.enderscape.registry.sound.EnderscapeMagniaRadioSongs;
import net.penumbra.enderscape.registry.sound.EnderscapeStructureMusic;
import net.penumbra.enderscape.registry.structure.EnderscapeProcessorLists;
import net.penumbra.enderscape.registry.structure.EnderscapeStructureSets;
import net.penumbra.enderscape.registry.structure.EnderscapeStructures;
import net.penumbra.enderscape.registry.structure.EnderscapeTemplatePools;

public class EnderscapeDataGenerator implements DataGeneratorEntrypoint {

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
		pack.addProvider(EnderscapeFluidTagProvider::new);
		pack.addProvider(EnderscapeItemTagProvider::new);
		pack.addProvider(EnderscapeJukeboxSongProvider::new);
		pack.addProvider(EnderscapeMobEffectTagProvider::new);
		pack.addProvider(EnderscapeModelProvider::new);
		pack.addProvider(EnderscapeNoiseProvider::new);
		pack.addProvider(EnderscapePaintingVariantProvider::new);
		pack.addProvider(EnderscapePaintingVariantTagProvider::new);
		pack.addProvider(EnderscapePlacedFeatureProvider::new);
		pack.addProvider(EnderscapePoiTagProvider::new);
		pack.addProvider(EnderscapeProcessorListProvider::new);
		pack.addProvider(EnderscapeRecipeProvider::new);
		pack.addProvider(EnderscapeSoundEventTagProvider::new);
		pack.addProvider(EnderscapeStructureProvider::new);
		pack.addProvider(EnderscapeStructureSetProvider::new);
		pack.addProvider(EnderscapeStructureTagProvider::new);
		pack.addProvider(EnderscapeTemplatePoolProvider::new);
		pack.addProvider(EnderscapeTrialSpawnerProvider::new);
		pack.addProvider(EnderscapeTrimMaterialProvider::new);
		pack.addProvider(EnderscapeTrimPatternProvider::new);
		pack.addProvider(EnderscapeVaultLootProvider::new);

		pack.addProvider(EnderscapeMagniaRadioSongProvider::new);
		pack.addProvider(EnderscapeRubblemiteVariantProvider::new);
		pack.addProvider(EnderscapeStructureMusicProvider::new);

		pack.addProvider(EnderscapeWorldgenModifierProvider::new);

		EnderscapePacksProvider.initialize(generator);
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
		builder.add(Registries.PROCESSOR_LIST, EnderscapeProcessorLists::bootstrap);
		builder.add(Registries.STRUCTURE, EnderscapeStructures::bootstrap);
		builder.add(Registries.STRUCTURE_SET, EnderscapeStructureSets::bootstrap);
		builder.add(Registries.TEMPLATE_POOL, EnderscapeTemplatePools::bootstrap);
		builder.add(Registries.TRIAL_SPAWNER_CONFIG, EnderscapeTrialSpawnerConfigs::bootstrap);
		builder.add(Registries.TRIM_MATERIAL, EnderscapeTrimMaterials::bootstrap);
		builder.add(Registries.TRIM_PATTERN, EnderscapeTrimPatterns::bootstrap);

		builder.add(EnderscapeRegistries.MAGNIA_RADIO_SONG, EnderscapeMagniaRadioSongs::bootstrap);
		builder.add(EnderscapeRegistries.RUBBLEMITE_VARIANT, EnderscapeRubblemiteVariants::bootstrap);
		builder.add(EnderscapeRegistries.STRUCTURE_MUSIC, EnderscapeStructureMusic::bootstrap);

		builder.add(LithostitchedRegistries.WORLDGEN_MODIFIER, EnderscapeWorldgenModifiers::bootstrap);
	}
}