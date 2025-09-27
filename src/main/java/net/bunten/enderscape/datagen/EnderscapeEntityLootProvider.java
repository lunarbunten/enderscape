package net.bunten.enderscape.datagen;

import net.bunten.enderscape.entity.rubblemite.RubblemiteVariant;
import net.bunten.enderscape.registry.EnderscapeBlocks;
import net.bunten.enderscape.registry.EnderscapeEntityLootTables;
import net.bunten.enderscape.registry.EnderscapeRegistries;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

import static net.bunten.enderscape.registry.EnderscapeEntities.*;
import static net.bunten.enderscape.registry.EnderscapeEntityLootTables.*;
import static net.bunten.enderscape.registry.EnderscapeItems.RUBBLE_CHITIN;

public class EnderscapeEntityLootProvider extends SimpleFabricLootTableProvider {

    private final CompletableFuture<HolderLookup.Provider> lookup;

    public EnderscapeEntityLootProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup, LootContextParamSets.ENTITY);
        this.lookup = lookup;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
        try {
            HolderGetter<RubblemiteVariant> variants = lookup.get().lookupOrThrow(EnderscapeRegistries.RUBBLEMITE_VARIANT);

            consumer.accept(
                    getLootTable(DRIFTER),
                    LootTable.lootTable()
            );

            consumer.accept(
                    getLootTable(DRIFTLET),
                    LootTable.lootTable()
            );

            consumer.accept(
                    getLootTable(RUBBLEMITE),
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1))
                                            .add(
                                                    LootItem.lootTableItem(RUBBLE_CHITIN)
                                                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                                            .apply(EnchantedCountIncreaseFunction.lootingMultiplier(lookup.get(), UniformGenerator.between(0, 1)))
                                                            .when(LootItemRandomChanceCondition.randomChance(0.25F))
                                            )
                                            .when(LootItemKilledByPlayerCondition.killedByPlayer())
                            )
            );

            createRubblemiteExtraDropItems(consumer, RUBBLEMITE_END_STONE, Blocks.END_STONE);
            createRubblemiteExtraDropItems(consumer, RUBBLEMITE_MIRESTONE, EnderscapeBlocks.MIRESTONE);
            createRubblemiteExtraDropItems(consumer, RUBBLEMITE_VERADITE, EnderscapeBlocks.VERADITE);
            createRubblemiteExtraDropItems(consumer, RUBBLEMITE_KURODITE, EnderscapeBlocks.KURODITE);

            consumer.accept(
                    getLootTable(RUSTLE),
                    LootTable.lootTable()
            );

            consumer.accept(
                    EnderscapeEntityLootTables.SHEARING_RUSTLE,
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1))
                                            .add(LootItem.lootTableItem(Items.STRING).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                            )
            );

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void createRubblemiteExtraDropItems(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer, ResourceKey<LootTable> table, ItemLike item) throws InterruptedException, ExecutionException {
        consumer.accept(
                table,
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1))
                                        .add(
                                                LootItem.lootTableItem(item)
                                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1)))
                                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(lookup.get(), UniformGenerator.between(0, 1)))
                                        )
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                        )
        );
    }

    private ResourceKey<LootTable> getLootTable(EntityType<?> type) {
        return type.getDefaultLootTable().orElseThrow(() -> new IllegalStateException("Entity " + type + " has no loot table"));
    }
}