package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class EnderscapeEntityLootTables {

    public static final ResourceKey<LootTable> SHEARING_RUSTLE = ResourceKey.create(Registries.LOOT_TABLE, Enderscape.id("shearing/rustle"));

}