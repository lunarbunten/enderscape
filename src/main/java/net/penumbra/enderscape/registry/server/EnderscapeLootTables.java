package net.penumbra.enderscape.registry.server;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.penumbra.enderscape.Enderscape;

import java.util.ArrayList;
import java.util.List;

public class EnderscapeLootTables {

    public static final List<ResourceKey<LootTable>> LOOT_TABLES = new ArrayList<>();

    public static final ResourceKey<LootTable> STRONGHOLD_CHEST_ALTAR = register("stronghold/chest/altar");
    public static final ResourceKey<LootTable> STRONGHOLD_CHEST_ARMORY = register("stronghold/chest/armory");
    public static final ResourceKey<LootTable> STRONGHOLD_CHEST_BEDROOM = register("stronghold/chest/bedroom");
    public static final ResourceKey<LootTable> STRONGHOLD_CHEST_GARDEN = register("stronghold/chest/garden");
    public static final ResourceKey<LootTable> STRONGHOLD_CHEST_LIBRARY = register("stronghold/chest/library");
    public static final ResourceKey<LootTable> STRONGHOLD_CHEST_MANSION = register("stronghold/chest/mansion");
    public static final ResourceKey<LootTable> STRONGHOLD_CHEST_SECRET = register("stronghold/chest/secret");
    public static final ResourceKey<LootTable> STRONGHOLD_INFESTATION_DISPENSER = register("stronghold/dispenser/infestation");

    public static final ResourceKey<LootTable> STRONGHOLD_SPAWNER_BASIC = register("stronghold/spawner/basic");

    public static final ResourceKey<LootTable> END_CITY_CHEST = register("end_city/chest");
    public static final ResourceKey<LootTable> END_CITY_ELYTRA_VAULT = register("end_city/elytra_vault");
    public static final ResourceKey<LootTable> END_CITY_SPAWNER_BASIC = register("end_city/spawner/basic");
    public static final ResourceKey<LootTable> END_CITY_SPAWNER_KEY = register("end_city/spawner/key");
    public static final ResourceKey<LootTable> END_CITY_VAULT = register("end_city/vault");

    public static final ResourceKey<LootTable> END_HAVEN_CHEST = register("end_haven/chest");

    public static final ResourceKey<LootTable> MIRESTONE_RUINS_CHEST = register("mirestone_ruins/chest");

    // Vanilla Compatibility

    public static final ResourceKey<LootTable> END_CITY_TREASURE_SUPPLEMENTS = register("supplements/end_city_treasure");
    public static final ResourceKey<LootTable> STRONGHOLD_LIBRARY_CHEST_SUPPLEMENTS = register("supplements/stronghold_library");

    public static final ResourceKey<LootTable> END_CITY_TREASURE_POST_SUPPLEMENTS = register("post_supplements/end_city_treasure");

    private static ResourceKey<LootTable> register(String name) {
        ResourceKey<LootTable> key = ResourceKey.create(Registries.LOOT_TABLE, Enderscape.id(name));
        LOOT_TABLES.add(key);
        return key;
    }
}