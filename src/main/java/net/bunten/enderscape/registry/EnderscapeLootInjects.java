package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.util.Identifier;

/**
 * Would credit a mod here but I don't remember where I got this from. :( sorry
 */
public class EnderscapeLootInjects {
    public static void init() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            Identifier inject = Enderscape.id("inject/" + id.getPath());
            if (LootTables.END_CITY_TREASURE_CHEST.equals(id)) {
                tableBuilder.pool(LootPool.builder().with(LootTableEntry.builder(inject).weight(1).quality(0)).build());
            } else if (LootTables.NETHER_BRIDGE_CHEST.equals(id)) {
                tableBuilder.pool(LootPool.builder().with(LootTableEntry.builder(inject).weight(1).quality(0)).build());
            }
        });
    }
}