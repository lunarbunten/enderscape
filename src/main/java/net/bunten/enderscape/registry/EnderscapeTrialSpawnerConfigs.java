package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnderscapeTrialSpawnerConfigs {

    public static final List<ResourceKey<TrialSpawnerConfig>> TRIAL_SPAWNER_CONFIGS = new ArrayList<>();

    public static final ResourceKey<TrialSpawnerConfig> END_CITY_NORMAL = register("end_city/normal");
    public static final ResourceKey<TrialSpawnerConfig> END_CITY_OMINOUS = register("end_city/ominous");
    public static final ResourceKey<TrialSpawnerConfig> END_CITY_SHIP_NORMAL = register("end_city/ship_normal");
    public static final ResourceKey<TrialSpawnerConfig> END_CITY_SHIP_OMINOUS = register("end_city/ship_ominous");

    private static <T extends Entity> SpawnData spawnData(EntityType<T> type) {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(type).toString());
        return new SpawnData(tag, Optional.empty(), Optional.empty());
    }

    public static void bootstrap(BootstrapContext<TrialSpawnerConfig> context) {
        context.register(END_CITY_NORMAL,
                TrialSpawnerConfig.builder()
                        .lootTablesToEject(
                                WeightedList.<ResourceKey<LootTable>>builder()
                                        .add(EnderscapeLootTables.END_CITY_SPAWNER_KEY, 2)
                                        .add(EnderscapeLootTables.END_CITY_SPAWNER_BASIC, 1)
                                        .build()
                        )
                        .spawnPotentialsDefinition(
                                WeightedList.<SpawnData>builder()
                                        .add(spawnData(EntityType.ENDERMITE), 4)
                                        .add(spawnData(EnderscapeEntities.RUBBLEMITE), 1)
                                        .build()
                        )
                        .simultaneousMobsAddedPerPlayer(1.5F)
                        .simultaneousMobs(4)
                        .ticksBetweenSpawn(20)
                        .totalMobs(18)
                        .build()
        );

        context.register(END_CITY_SHIP_NORMAL,
                TrialSpawnerConfig.builder()
                        .lootTablesToEject(WeightedList.of(EnderscapeLootTables.END_CITY_SPAWNER_KEY))
                        .spawnPotentialsDefinition(
                                WeightedList.<SpawnData>builder()
                                        .add(spawnData(EntityType.ENDERMITE), 2)
                                        .add(spawnData(EnderscapeEntities.RUBBLEMITE), 1)
                                        .build()
                        )
                        .simultaneousMobsAddedPerPlayer(1.5F)
                        .simultaneousMobs(4)
                        .ticksBetweenSpawn(10)
                        .totalMobs(18)
                        .build()
        );

        context.register(END_CITY_OMINOUS, TrialSpawnerConfig.builder().build());
        context.register(END_CITY_SHIP_OMINOUS, TrialSpawnerConfig.builder().build());
    }

    private static ResourceKey<TrialSpawnerConfig> register(String name) {
        ResourceKey<TrialSpawnerConfig> key = ResourceKey.create(Registries.TRIAL_SPAWNER_CONFIG, Enderscape.id(name));
        TRIAL_SPAWNER_CONFIGS.add(key);
        return key;
    }
}