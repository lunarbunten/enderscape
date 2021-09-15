package net.enderscape.registry;

import net.enderscape.Enderscape;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.util.Identifier;

/**
 * Would credit a mod here but I don't remember where I got this from. :( sorry
 */
public class EndLootTables {
    public static void init() {
        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
            Identifier inject = Enderscape.id("inject/" + id.getPath());
            if (LootTables.END_CITY_TREASURE_CHEST.equals(id)) {
                supplier.withPool(LootPool.builder().with(LootTableEntry.builder(inject).weight(1).quality(0)).build());
            } else if (LootTables.NETHER_BRIDGE_CHEST.equals(id)) {
                supplier.withPool(LootPool.builder().with(LootTableEntry.builder(inject).weight(1).quality(0)).build());
            }
        });
    }
}