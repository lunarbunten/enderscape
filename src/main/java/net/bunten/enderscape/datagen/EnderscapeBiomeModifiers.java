package net.bunten.enderscape.datagen;

import com.mojang.serialization.MapCodec;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.biome.modifications.ModifyEndAmbianceModification;
import net.bunten.enderscape.registry.EnderscapeEntities;
import net.bunten.enderscape.registry.EnderscapePlacedFeatures;
import net.bunten.enderscape.registry.RegistryHelper;
import net.bunten.enderscape.registry.tag.EnderscapeBiomeTags;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.EndPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.holdersets.AndHolderSet;
import net.neoforged.neoforge.registries.holdersets.NotHolderSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class EnderscapeBiomeModifiers {
    public EnderscapeBiomeModifiers() {
        RegistryHelper.checkAllReady();
    }

    public static final List<ResourceKey<BiomeModifier>> BIOME_MODIFIERS = new ArrayList<>();

    public static final ResourceKey<BiomeModifier> MODIFY_END_AMBIENCE = registerKey("modify_end_ambience");
    public static final ResourceKey<BiomeModifier> ADD_GLOBAL_FEATURES = registerKey("add_global_features");
    public static final ResourceKey<BiomeModifier> ADD_NEW_BARENS_SPAWNS = registerKey("add_new_barens_spawns");
    public static final ResourceKey<BiomeModifier> ADD_NEW_BARENS_FEATURES = registerKey("add_new_barens_features");
    public static final ResourceKey<BiomeModifier> ADD_ENDERSCAPE_CHORUS_SPROUTS = registerKey("add_enderscape_chorus_sprouts");
    public static final ResourceKey<BiomeModifier> ADD_ENDERSCAPE_CHORUS_PLANTS = registerKey("add_enderscape_chorus_plants");
    public static final ResourceKey<BiomeModifier> REMOVE_MINECRAFT_CHORUS_PLANTS = registerKey("remove_minecraft_chorus_plants");
    public static final ResourceKey<BiomeModifier> ADD_ENDERSCAPE_ISLANDS = registerKey("add_enderscape_islands");
    public static final ResourceKey<BiomeModifier> REMOVE_SMALL_END_ISLANDS = registerKey("remove_small_end_islands");
    public static final ResourceKey<BiomeModifier> REMOVE_MINECRAFT_END_GATEWAYS = registerKey("remove_minecraft_end_gateways");

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        ResourceKey<BiomeModifier> key = ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Enderscape.id(name));
        BIOME_MODIFIERS.add(key);
        return key;
    }

    private static <T extends BiomeModifier> Supplier<MapCodec<T>> register(String name, MapCodec<T> entry) {
        return RegistryHelper.register(NeoForgeRegistries.BIOME_MODIFIER_SERIALIZERS, Enderscape.id(name), () -> entry);
    }

    static {
        register("modify_end_ambience", ModifyEndAmbianceModification.CODEC);
    }

    public void bootstrap(BootstrapContext<BiomeModifier> context) {
        var biomeLookup = context.lookup(Registries.BIOME);
        var placedFeatureLookup = context.lookup(Registries.PLACED_FEATURE);

        context.register(MODIFY_END_AMBIENCE, ModifyEndAmbianceModification.INSTANCE);
        context.register(ADD_GLOBAL_FEATURES, new BiomeModifiers.AddFeaturesBiomeModifier(
                new AndHolderSet<>(
                        biomeLookup.getOrThrow(BiomeTags.IS_END),
                        // This is null. Hopefully it will not break stuff
                        new NotHolderSet<>(null, biomeLookup.getOrThrow(EnderscapeBiomeTags.EXCLUDED_FROM_GLOBAL_FEATURE_ADDITIONS))
                ),
                HolderSet.direct(
                        placedFeatureLookup.getOrThrow(EnderscapePlacedFeatures.VOID_SHALE),
                        placedFeatureLookup.getOrThrow(EnderscapePlacedFeatures.VOID_SHALE_BLOBS),

                        placedFeatureLookup.getOrThrow(EnderscapePlacedFeatures.VERADITE),
                        placedFeatureLookup.getOrThrow(EnderscapePlacedFeatures.MIRESTONE_BLOBS),

                        placedFeatureLookup.getOrThrow(EnderscapePlacedFeatures.SHADOLINE_ORE),
                        placedFeatureLookup.getOrThrow(EnderscapePlacedFeatures.SCATTERED_SHADOLINE_ORE),

                        placedFeatureLookup.getOrThrow(EnderscapePlacedFeatures.NEBULITE_ORE),
                        placedFeatureLookup.getOrThrow(EnderscapePlacedFeatures.CEILING_NEBULITE_ORE)
                ),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));
        context.register(ADD_NEW_BARENS_SPAWNS, new BiomeModifiers.AddSpawnsBiomeModifier(
                biomeLookup.getOrThrow(EnderscapeBiomeTags.HAS_BARRENS_ADDITIONS),
                List.of(
                        new MobSpawnSettings.SpawnerData(EnderscapeEntities.RUBBLEMITE.get(), 4, 2, 3)
                )
        ));
        context.register(ADD_NEW_BARENS_FEATURES, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomeLookup.getOrThrow(EnderscapeBiomeTags.HAS_BARRENS_ADDITIONS),
                HolderSet.direct(
                        placedFeatureLookup.getOrThrow(EnderscapePlacedFeatures.DRY_END_GROWTH),
                        placedFeatureLookup.getOrThrow(EnderscapePlacedFeatures.MURUBLIGHT_BRACKET)
                ),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));
        context.register(ADD_ENDERSCAPE_CHORUS_SPROUTS, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomeLookup.getOrThrow(Biomes.END_MIDLANDS), biomeLookup.getOrThrow(Biomes.END_HIGHLANDS)),
                HolderSet.direct(placedFeatureLookup.getOrThrow(EnderscapePlacedFeatures.CHORUS_SPROUTS)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));
        context.register(ADD_ENDERSCAPE_CHORUS_PLANTS, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomeLookup.getOrThrow(Biomes.END_MIDLANDS), biomeLookup.getOrThrow(Biomes.END_HIGHLANDS)),
                HolderSet.direct(placedFeatureLookup.getOrThrow(EnderscapePlacedFeatures.CHORUS_PLANTS)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));
        context.register(REMOVE_MINECRAFT_CHORUS_PLANTS, new BiomeModifiers.RemoveFeaturesBiomeModifier(
                HolderSet.direct(biomeLookup.getOrThrow(Biomes.END_MIDLANDS), biomeLookup.getOrThrow(Biomes.END_HIGHLANDS)),
                HolderSet.direct(
                        placedFeatureLookup.getOrThrow(EndPlacements.CHORUS_PLANT)
                ),
                Arrays.stream(GenerationStep.Decoration.values()).collect(Collectors.toSet())
        ));

        context.register(ADD_ENDERSCAPE_ISLANDS, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomeLookup.getOrThrow(Biomes.SMALL_END_ISLANDS)),
                HolderSet.direct(
                        placedFeatureLookup.getOrThrow(EnderscapePlacedFeatures.SMALL_ISLANDS)
                ),
                GenerationStep.Decoration.RAW_GENERATION
        ));
        context.register(REMOVE_SMALL_END_ISLANDS, new BiomeModifiers.RemoveFeaturesBiomeModifier(
                HolderSet.direct(biomeLookup.getOrThrow(Biomes.SMALL_END_ISLANDS)),
                HolderSet.direct(
                        placedFeatureLookup.getOrThrow(EndPlacements.END_ISLAND_DECORATED)
                ),
                Arrays.stream(GenerationStep.Decoration.values()).collect(Collectors.toSet())
        ));
        context.register(REMOVE_MINECRAFT_END_GATEWAYS, new BiomeModifiers.RemoveFeaturesBiomeModifier(
                biomeLookup.getOrThrow(BiomeTags.IS_END),
                HolderSet.direct(
                        placedFeatureLookup.getOrThrow(EndPlacements.END_GATEWAY_RETURN)
                ),
                Arrays.stream(GenerationStep.Decoration.values()).collect(Collectors.toSet())
        ));
    }
}
