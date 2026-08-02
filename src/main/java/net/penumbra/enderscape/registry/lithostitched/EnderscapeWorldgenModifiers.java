package net.penumbra.enderscape.registry.lithostitched;

import dev.worldgen.lithostitched.api.registry.LithostitchedRegistries;
import dev.worldgen.lithostitched.api.util.InjectionType;
import dev.worldgen.lithostitched.api.worldgen.modifier.WorldgenModifier;
import dev.worldgen.lithostitched.worldgen.modifier.attribute.SetDimensionAttributesModifier;
import dev.worldgen.lithostitched.worldgen.modifier.attribute.SetTimelineTracksModifier;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.KeyframeTrack;
import net.minecraft.world.attribute.*;
import net.minecraft.world.attribute.modifier.BooleanModifier;
import net.minecraft.world.attribute.modifier.FloatModifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.timeline.AttributeTrack;
import net.minecraft.world.timeline.Timelines;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.registry.level.EnderscapeBiomes;
import net.penumbra.enderscape.registry.level.EnderscapeEnvironmentAttributes;
import net.penumbra.enderscape.registry.level.EnderscapePlacedFeatures;
import net.penumbra.enderscape.registry.level.EnderscapeSurfaceRules;
import net.penumbra.enderscape.registry.particle.EnderscapeParticles;
import net.penumbra.enderscape.registry.sound.EnderscapeBiomeSounds;
import net.penumbra.enderscape.registry.tag.EnderscapeBiomeTags;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EnderscapeWorldgenModifiers {

    public static final List<ResourceKey<WorldgenModifier>> WORLDGEN_MODIFIERS = new ArrayList<>();

    public static final ResourceKey<WorldgenModifier> DIMENSION_ATTRIBUTES_END = register("dimension_attributes/end");
    public static final ResourceKey<WorldgenModifier> DIMENSION_ATTRIBUTES_OVERWORLD = register("dimension_attributes/overworld");

    public static final ResourceKey<WorldgenModifier> FEATURES_BARRENS_VOID_LAKES = register("features/barrens_void_lakes");
    public static final ResourceKey<WorldgenModifier> FEATURES_BARRENS_VEGETATION = register("features/barrens_vegetation");

    public static final ResourceKey<WorldgenModifier> SURFACE_RULES_END = register("surface_rules/end");

    public static final ResourceKey<WorldgenModifier> TIMELINES_OVERWORLD_DAY = register("timelines/overworld_day");

    public static void bootstrap(BootstrapContext<WorldgenModifier> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        context.register(FEATURES_BARRENS_VOID_LAKES, WorldgenModifier.builder().addFeatures(
                biomes.getOrThrow(EnderscapeBiomeTags.HAS_BARRENS_ADDITIONS),
                HolderSet.direct(
                        placedFeatures.getOrThrow(EnderscapePlacedFeatures.VOID_LAKES)
                ), GenerationStep.Decoration.LAKES
        ));

        context.register(FEATURES_BARRENS_VEGETATION, WorldgenModifier.builder().addFeatures(
                biomes.getOrThrow(EnderscapeBiomeTags.HAS_BARRENS_ADDITIONS),
                HolderSet.direct(
                        placedFeatures.getOrThrow(EnderscapePlacedFeatures.CHORUS_SPROUTS),
                        placedFeatures.getOrThrow(EnderscapePlacedFeatures.DRY_END_GROWTH),
                        placedFeatures.getOrThrow(EnderscapePlacedFeatures.MURUBLIGHT_BRACKET),
                        placedFeatures.getOrThrow(EnderscapePlacedFeatures.VOID_FIRE_ON_VOID_SHALE)
                ), GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(DIMENSION_ATTRIBUTES_END, new SetDimensionAttributesModifier(
                Optional.empty(),
                WorldgenModifier.DEFAULT_PRIORITY,
                HolderSet.direct(context.lookup(Registries.DIMENSION_TYPE).getOrThrow(BuiltinDimensionTypes.END)),
                EnvironmentAttributeMap.builder()
                        .set(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, 0x1A1E1A)
                        .set(EnvironmentAttributes.AMBIENT_PARTICLES, AmbientParticle.of(EnderscapeParticles.VOID_STARS, 0.003F))
                        .set(EnvironmentAttributes.AMBIENT_SOUNDS, new AmbientSounds(
                                Optional.of(EnderscapeBiomeSounds.DEFAULT_END.loop()),
                                Optional.of(new AmbientMoodSettings(EnderscapeBiomeSounds.DEFAULT_END.mood(), 6000, 8, 2)),
                                List.of(new AmbientAdditionsSettings(EnderscapeBiomeSounds.DEFAULT_END.additions(), 0.00075))
                        ))
                        .set(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(EnderscapeBiomeSounds.DEFAULT_END.music()))
                        .set(EnvironmentAttributes.BLOCK_LIGHT_TINT, 0xFFA69E)
                        .set(EnvironmentAttributes.CLOUD_COLOR, EnderscapeBiomes.DEFAULT_CLOUD_COLOR)
                        .set(EnvironmentAttributes.SKY_COLOR, EnderscapeBiomes.DEFAULT_SKY_COLOR)
                        .set(EnvironmentAttributes.SKY_LIGHT_COLOR, 0x654B82)
                        .set(EnvironmentAttributes.FOG_COLOR, EnderscapeBiomes.DEFAULT_FOG_COLOR)
                        .set(EnvironmentAttributes.FOG_START_DISTANCE, -20.0F)
                        .set(EnvironmentAttributes.STAR_BRIGHTNESS, EnderscapeEnvironmentAttributes.DEFAULT_STAR_BRIGHTNESS)
                        .modify(EnderscapeEnvironmentAttributes.FOG_END_DENSITY, FloatModifier.MULTIPLY, 0.9F)
                        .set(EnderscapeEnvironmentAttributes.END_HAVEN_CORE_WORKS, true)
                        .set(EnderscapeEnvironmentAttributes.MONSTER_SPAWN_CAP, 200)
                        .build(),
                true
        ));

        context.register(DIMENSION_ATTRIBUTES_OVERWORLD, new SetDimensionAttributesModifier(
                Optional.empty(),
                WorldgenModifier.DEFAULT_PRIORITY,
                HolderSet.direct(context.lookup(Registries.DIMENSION_TYPE).getOrThrow(BuiltinDimensionTypes.OVERWORLD)),
                EnvironmentAttributeMap.builder().set(EnderscapeEnvironmentAttributes.PURIFIES_VOIDED_HEALTH, true).build(),
                true
        ));

        context.register(TIMELINES_OVERWORLD_DAY, new SetTimelineTracksModifier(
                Optional.empty(),
                WorldgenModifier.DEFAULT_PRIORITY,
                HolderSet.direct(context.lookup(Registries.TIMELINE).getOrThrow(Timelines.OVERWORLD_DAY)),
                Map.of(
                        EnderscapeEnvironmentAttributes.PURIFIES_VOIDED_HEALTH, new AttributeTrack<>(
                                BooleanModifier.AND,
                                new KeyframeTrack.Builder().addKeyframe(12542, false).addKeyframe(23460, true).build()
                        )
                ),
                true
        ));

        context.register(SURFACE_RULES_END, WorldgenModifier.builder().addSurfaceRule(Level.END, InjectionType.PREPEND, EnderscapeSurfaceRules.create()));
    }

    private static ResourceKey<WorldgenModifier> register(String name) {
        ResourceKey<WorldgenModifier> key = ResourceKey.create(LithostitchedRegistries.WORLDGEN_MODIFIER, Enderscape.id(name));
        WORLDGEN_MODIFIERS.add(key);
        return key;
    }
}