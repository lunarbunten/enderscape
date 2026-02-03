package net.bunten.enderscape.datagen;

import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;

public class EnderscapeLootProvider extends LootTableProvider {

    protected EnderscapeLootProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(EnderscapeBlockLootProvider::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(EnderscapeChestLootProvider::new, LootContextParamSets.CHEST),
                new LootTableProvider.SubProviderEntry(EnderscapeVaultLootProvider::new, LootContextParamSets.VAULT),
                new LootTableProvider.SubProviderEntry(EnderscapeEntityLootProvider::new, LootContextParamSets.ENTITY)
        ), event.getLookupProvider());
    }
}
