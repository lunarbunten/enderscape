package net.penumbra.enderscape.registry.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.penumbra.enderscape.Enderscape;

public class EnderscapeEntityLootTables {

    public static final ResourceKey<LootTable> RUBBLEMITE_END_STONE = register("rubblemite/end_stone");
    public static final ResourceKey<LootTable> RUBBLEMITE_MIRESTONE = register("rubblemite/mirestone");
    public static final ResourceKey<LootTable> RUBBLEMITE_VERADITE = register("rubblemite/veradite");
    public static final ResourceKey<LootTable> RUBBLEMITE_KURODITE = register("rubblemite/kurodite");

    public static final ResourceKey<LootTable> SHEARING_RUSTLE = register("shearing/rustle");

    public static ResourceKey<LootTable> register(String name) {
        return ResourceKey.create(Registries.LOOT_TABLE, Enderscape.id(name));
    }
}