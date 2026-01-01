package net.bunten.enderscape.structure;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EnderscapeStructures {

    public static final List<ResourceKey<Structure>> STRUCTURES = new ArrayList<>();

    public static final ResourceKey<Structure> STRONGHOLD = register("stronghold");
    //public static final ResourceKey<Structure> END_CITY = register("end_city");
    //public static final ResourceKey<Structure> MIRESTONE_RUINS = register("mirestone_ruins");

    //public static final ResourceKey<Structure> CENTER_GATEWAY = register("center_gateway");
    //public static final ResourceKey<Structure> LARGE_CENTER_GATEWAY = register("large_center_gateway");

    public static final StructureSpawnOverride BLANK_SPAWN_OVERRIDE = new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create());

    public static final StructureSpawnOverride STRONGHOLD_MONSTER_SPAWN_OVERRIDE = new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create(
            new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 120, 4, 6),
            new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 100, 4, 4),
            new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE_VILLAGER, 25, 1, 1),
            new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 100, 4, 4),
            new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 40, 1, 4),
            new MobSpawnSettings.SpawnerData(EntityType.SILVERFISH, 80, 4, 6)
    ));

    public static void bootstrap(BootstrapContext<Structure> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);

        context.register(
                STRONGHOLD,
                new JigsawStructure(
                        new Structure.StructureSettings.Builder(biomes.getOrThrow(BiomeTags.HAS_STRONGHOLD))
                                .generationStep(GenerationStep.Decoration.UNDERGROUND_STRUCTURES)
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
                        90,
                        List.of(),
                        new DimensionPadding(20),
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
