package net.bunten.enderscape.datagen;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.entity.rubblemite.RubblemiteVariant;
import net.bunten.enderscape.entity.rubblemite.RubblemiteVariantPredicate;
import net.bunten.enderscape.registry.EnderscapeEntityLootTables;
import net.bunten.enderscape.registry.EnderscapeItems;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static net.bunten.enderscape.registry.EnderscapeEntities.*;

public class EnderscapeEntityLootProvider implements LootTableSubProvider {
    private final HolderLookup.Provider lookup;

    public EnderscapeEntityLootProvider(HolderLookup.Provider lookup) {
        this.lookup = lookup;
    }


    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
        consumer.accept(
                getLootTable(DRIFTER.get()),
                LootTable.lootTable()
        );

        consumer.accept(
                getLootTable(RUBBLEMITE.get()),
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1))
                                        .add(
                                                LootItem.lootTableItem(EnderscapeItems.RUBBLE_CHITIN.get())
                                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(lookup, UniformGenerator.between(0, 1)))
                                                        .when(LootItemRandomChanceCondition.randomChance(0.65F))
                                        )
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                        )
                        .withPool(createRubblemiteVariantPool())
        );

        RUBBLEMITE_BY_VARIANT.forEach((variant, key) -> {
            consumer.accept(
                    key,
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1))
                                            .add(
                                                    LootItem.lootTableItem(variant.getDropItem())
                                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1)))
                                                            .apply(EnchantedCountIncreaseFunction.lootingMultiplier(lookup, UniformGenerator.between(0, 1)))
                                            )
                            )
            );
        });

        consumer.accept(
                getLootTable(RUSTLE.get()),
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

    }

    public static final Map<RubblemiteVariant, ResourceKey<LootTable>> RUBBLEMITE_BY_VARIANT = Arrays.stream(RubblemiteVariant.values()).collect(Collectors.toMap(
            variant -> variant, variant -> ResourceKey.create(Registries.LOOT_TABLE, Enderscape.id("entities/rubblemite/" + variant.getSerializedName())),
                    (a, b) -> b, () -> new EnumMap<>(RubblemiteVariant.class)
            )
    );

    public static LootPool.Builder createRubblemiteVariantPool() {
        AlternativesEntry.Builder builder = AlternativesEntry.alternatives();

        RUBBLEMITE_BY_VARIANT.forEach((variant, lootTableKey) ->
                builder.otherwise(
                        NestedLootTable.lootTableReference(lootTableKey)
                                .when(LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS,
                                                EntityPredicate.Builder.entity().subPredicate(new RubblemiteVariantPredicate(Optional.ofNullable(variant)))
                                        )
                                )
                )
        );

        return LootPool.lootPool().add(builder);
    }


    private ResourceKey<LootTable> getLootTable(EntityType<?> type) {
        return type.getDefaultLootTable();
    }
}