package net.penumbra.enderscape.registry.structure;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.registry.structure.endcity.EndCityTemplatePools;
import net.penumbra.enderscape.registry.structure.endhaven.EndHavenTemplatePools;
import net.penumbra.enderscape.registry.structure.gateway.CenterGatewayTemplatePools;
import net.penumbra.enderscape.registry.structure.gateway.LargeCenterGatewayTemplatePools;
import net.penumbra.enderscape.registry.structure.mirestoneruins.MirestoneRuinsTemplatePools;
import net.penumbra.enderscape.registry.structure.stronghold.StrongholdTemplatePools;
import net.penumbra.enderscape.registry.tag.EnderscapeBiomeTags;

import java.util.*;
import java.util.stream.Collectors;

public class EnderscapeStructures {

    public static final List<ResourceKey<Structure>> STRUCTURES = new ArrayList<>();

    public static final ResourceKey<Structure> STRONGHOLD = register("stronghold");
    public static final ResourceKey<Structure> END_CITY = register("end_city");
    public static final ResourceKey<Structure> END_HAVEN = register("end_haven");
    public static final ResourceKey<Structure> MIRESTONE_RUINS = register("mirestone_ruins");

    public static final ResourceKey<Structure> CENTER_GATEWAY = register("center_gateway");
    public static final ResourceKey<Structure> LARGE_CENTER_GATEWAY = register("large_center_gateway");

    public static final StructureSpawnOverride BLANK_SPAWN_OVERRIDE = new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedList.of());

    public static final Map<MobCategory, StructureSpawnOverride> ALL_BLANK_SPAWN_OVERRIDES = Arrays.stream(MobCategory.values()).collect(Collectors.toMap(category -> category, _ -> BLANK_SPAWN_OVERRIDE));

    public static final StructureSpawnOverride STRONGHOLD_MONSTER_SPAWN_OVERRIDE = new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedList.<MobSpawnSettings.SpawnerData>builder()
            .add(new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 4, 6), 120)
            .add(new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 4, 4), 100)
            .add(new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE_VILLAGER, 1, 1), 25)
            .add(new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 4, 4), 100)
            .add(new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 4), 40)
            .add(new MobSpawnSettings.SpawnerData(EntityType.SILVERFISH, 4, 6), 80)
            .build());

    public static void bootstrap(BootstrapContext<Structure> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);

        context.register(
                STRONGHOLD,
                new JigsawStructure(
                        new Structure.StructureSettings.Builder(biomes.getOrThrow(BiomeTags.HAS_STRONGHOLD))
                                .generationStep(GenerationStep.Decoration.STRONGHOLDS)
                                .terrainAdapation(TerrainAdjustment.ENCAPSULATE)
                                .spawnOverrides(Arrays.stream(MobCategory.values()).collect(Collectors.toMap(
                                        category -> category,
                                        category -> category == MobCategory.MONSTER ? STRONGHOLD_MONSTER_SPAWN_OVERRIDE : BLANK_SPAWN_OVERRIDE
                                        ))
                                )
                                .build(),
                        pools.getOrThrow(StrongholdTemplatePools.START),
                        Optional.empty(),
                        18,
                        UniformHeight.of(VerticalAnchor.absolute(-20), VerticalAnchor.absolute(20)),
                        false,
                        Optional.empty(),
                        new JigsawStructure.MaxDistance(90),
                        List.of(),
                        new DimensionPadding(20),
                        LiquidSettings.APPLY_WATERLOGGING
                )
        );

        context.register(
                END_CITY,
                new JigsawStructure(
                        new Structure.StructureSettings.Builder(biomes.getOrThrow(BiomeTags.HAS_END_CITY))
                                .generationStep(GenerationStep.Decoration.SURFACE_STRUCTURES)
                                .terrainAdapation(TerrainAdjustment.NONE)
                                .spawnOverrides(ALL_BLANK_SPAWN_OVERRIDES)
                                .build(),
                        pools.getOrThrow(EndCityTemplatePools.START),
                        Optional.empty(),
                        14,
                        ConstantHeight.of(VerticalAnchor.absolute(1)),
                        false,
                        Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
                        new JigsawStructure.MaxDistance(128),
                        List.of(),
                        new DimensionPadding(56, 0),
                        LiquidSettings.IGNORE_WATERLOGGING
                )
        );

        context.register(
                END_HAVEN,
                new JigsawStructure(
                        new Structure.StructureSettings.Builder(biomes.getOrThrow(BiomeTags.HAS_END_CITY))
                                .generationStep(GenerationStep.Decoration.SURFACE_STRUCTURES)
                                .terrainAdapation(TerrainAdjustment.NONE)
                                .build(),
                        pools.getOrThrow(EndHavenTemplatePools.START),
                        Optional.empty(),
                        4,
                        UniformHeight.of(VerticalAnchor.absolute(40), VerticalAnchor.absolute(140)),
                        false,
                        Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
                        new JigsawStructure.MaxDistance(24),
                        List.of(),
                        new DimensionPadding(96, 96),
                        LiquidSettings.IGNORE_WATERLOGGING
                )
        );

        context.register(
                MIRESTONE_RUINS,
                new JigsawStructure(
                        new Structure.StructureSettings.Builder(biomes.getOrThrow(EnderscapeBiomeTags.HAS_MIRESTONE_RUINS))
                                .generationStep(GenerationStep.Decoration.SURFACE_STRUCTURES)
                                .terrainAdapation(TerrainAdjustment.BEARD_BOX)
                                .build(),
                        pools.getOrThrow(MirestoneRuinsTemplatePools.START),
                        Optional.empty(),
                        10,
                        ConstantHeight.of(VerticalAnchor.absolute(0)),
                        false,
                        Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
                        new JigsawStructure.MaxDistance(96),
                        List.of(),
                        new DimensionPadding(56, 0),
                        LiquidSettings.IGNORE_WATERLOGGING
                )
        );

        context.register(
                CENTER_GATEWAY,
                new JigsawStructure(
                        new Structure.StructureSettings.Builder(biomes.getOrThrow(EnderscapeBiomeTags.HAS_GATEWAYS))
                                .generationStep(GenerationStep.Decoration.SURFACE_STRUCTURES)
                                .terrainAdapation(TerrainAdjustment.NONE)
                                .build(),
                        pools.getOrThrow(CenterGatewayTemplatePools.START),
                        Optional.empty(),
                        1,
                        ConstantHeight.of(VerticalAnchor.absolute(6)),
                        false,
                        Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
                        new JigsawStructure.MaxDistance(8),
                        List.of(),
                        new DimensionPadding(56, 60),
                        LiquidSettings.IGNORE_WATERLOGGING
                )
        );

        context.register(
                LARGE_CENTER_GATEWAY,
                new JigsawStructure(
                        new Structure.StructureSettings.Builder(biomes.getOrThrow(EnderscapeBiomeTags.HAS_GATEWAYS))
                                .generationStep(GenerationStep.Decoration.SURFACE_STRUCTURES)
                                .terrainAdapation(TerrainAdjustment.NONE)
                                .build(),
                        pools.getOrThrow(LargeCenterGatewayTemplatePools.START),
                        Optional.empty(),
                        4,
                        ConstantHeight.of(VerticalAnchor.absolute(24)),
                        false,
                        Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
                        new JigsawStructure.MaxDistance(64),
                        List.of(),
                        new DimensionPadding(56, 80),
                        LiquidSettings.IGNORE_WATERLOGGING
                )
        );
    }

    private static ResourceKey<Structure> register(String string) {
        ResourceKey<Structure> key = ResourceKey.create(Registries.STRUCTURE, Enderscape.id(string));
        STRUCTURES.add(key);
        return key;
    }
}