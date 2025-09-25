package net.bunten.enderscape.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

import static net.bunten.enderscape.registry.EnderscapeEnchantments.*;
import static net.bunten.enderscape.registry.EnderscapeItems.*;
import static net.bunten.enderscape.registry.EnderscapeLootTables.END_CITY_ELYTRA_VAULT;
import static net.bunten.enderscape.registry.EnderscapeLootTables.END_CITY_VAULT;
import static net.minecraft.world.item.Items.*;
import static net.minecraft.world.item.enchantment.Enchantments.*;

public class EnderscapeVaultLootProvider extends SimpleFabricLootTableProvider {

    private final CompletableFuture<HolderLookup.Provider> lookup;

    public EnderscapeVaultLootProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup, LootContextParamSets.VAULT);
        this.lookup = lookup;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
        consumer.accept(END_CITY_VAULT, endCityVaultRewards());
        consumer.accept(END_CITY_ELYTRA_VAULT, endCityVaultRewards().withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(createItem(ELYTRA, 1))
                )
        );
    }

    public LootTable.Builder endCityVaultRewards() {
        try {
            LootItemConditionalFunction.Builder<?> toolDamage = SetItemDamageFunction.setDamage(UniformGenerator.between(0.4F, 1.0F));
            EnchantWithLevelsFunction.Builder toolEnchantments = EnchantWithLevelsFunction.enchantWithLevels(lookup.get(), UniformGenerator.between(20, 40));

            return LootTable.lootTable()
                    .withPool(
                            LootPool.lootPool()
                                    .setRolls(UniformGenerator.between(1, 3))

                                    .add(createItem(DIAMOND_SWORD, 20).apply(toolEnchantments).apply(toolDamage))
                                    .add(createItem(DIAMOND_PICKAXE, 20).apply(toolEnchantments).apply(toolDamage))
                                    .add(createItem(DIAMOND_AXE, 20).apply(toolEnchantments).apply(toolDamage))
                                    .add(createItem(DIAMOND_SHOVEL, 20).apply(toolEnchantments).apply(toolDamage))
                                    .add(createItem(DIAMOND_HOE, 20).apply(toolEnchantments).apply(toolDamage))

                                    .add(createItem(DIAMOND_HELMET, 25).apply(toolEnchantments).apply(toolDamage))
                                    .add(createItem(DIAMOND_CHESTPLATE, 25).apply(toolEnchantments).apply(toolDamage))
                                    .add(createItem(DIAMOND_LEGGINGS, 25).apply(toolEnchantments).apply(toolDamage))
                                    .add(createItem(DIAMOND_BOOTS, 25).apply(toolEnchantments).apply(toolDamage))
                    )
                    .withPool(
                            LootPool.lootPool()
                                    .setRolls(UniformGenerator.between(2, 3))

                                    .add(createItem(IRON_INGOT, 30).apply(countBetween(4, 10)))
                                    .add(createItem(GOLD_INGOT, 30).apply(countBetween(2, 6)))
                                    .add(createItem(DIAMOND, 20).apply(countBetween(2, 8)))
                                    .add(createItem(EMERALD, 20).apply(countBetween(2, 6)))
                                    .add(createItem(BLAZE_ROD, 6).apply(countBetween(2, 4)))
                                    .add(createItem(NEBULITE_SHARDS, 3).apply(countBetween(2, 12)))
                    )
                    .withPool(
                            LootPool.lootPool()
                                    .setRolls(UniformGenerator.between(1, 2))

                                    .add(EmptyLootItem.emptyItem().setWeight(60))

                                    .add(createItem(GOLDEN_CARROT, 40).apply(countBetween(1, 8)))
                                    .add(createItem(GOLDEN_APPLE, 10).apply(countBetween(1, 2)))
                                    .add(createItem(ENCHANTED_GOLDEN_APPLE, 1))
                    )
                    .withPool(
                            LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1))

                                    .add(EmptyLootItem.emptyItem().setWeight(60))

                                    .add(createItem(MUSIC_DISC_GLARE, 6))
                                    .add(createItem(MIRROR, 6))
                                    .add(createItem(SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, 6))

                                    .add(createItem(CRESCENT_BANNER_PATTERN, 4))
                                    .add(createItem(STASIS_ARMOR_TRIM_SMITHING_TEMPLATE, 4))
                    )
                    .withPool(
                            LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(2))
                                    .add(EmptyLootItem.emptyItem().setWeight(100))

                                    .add(createItem(BOOK, 50).apply(
                                                    new EnchantRandomlyFunction.Builder()
                                                            .withOneOf(
                                                                    HolderSet.direct(
                                                                            getEnchantmentReference(PROTECTION),
                                                                            getEnchantmentReference(FEATHER_FALLING),
                                                                            getEnchantmentReference(SHARPNESS),
                                                                            getEnchantmentReference(BANE_OF_ARTHROPODS),
                                                                            getEnchantmentReference(KNOCKBACK),
                                                                            getEnchantmentReference(LOOTING),
                                                                            getEnchantmentReference(EFFICIENCY),
                                                                            getEnchantmentReference(FORTUNE),
                                                                            getEnchantmentReference(SILK_TOUCH),
                                                                            getEnchantmentReference(UNBREAKING),
                                                                            getEnchantmentReference(POWER),
                                                                            getEnchantmentReference(PUNCH)
                                                                    )
                                                            )
                                            )
                                    )

                                    .add(createEnchantedBook(LIGHTSPEED, 10))

                                    .add(createItem(BOOK, 10).apply(
                                                    new EnchantRandomlyFunction.Builder()
                                                            .withOneOf(
                                                                    HolderSet.direct(
                                                                            getEnchantmentReference(REBOUND)
                                                                    )
                                                            )
                                            )
                                    )

                                    .add(createItem(BOOK, 4).apply(
                                                    new EnchantRandomlyFunction.Builder()
                                                            .withOneOf(
                                                                    HolderSet.direct(
                                                                            getEnchantmentReference(TRANSDIMENSIONAL),
                                                                            getEnchantmentReference(MENDING)
                                                                    )
                                                            )
                                            )
                                    )
                    );
        } catch (InterruptedException ignored) {

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @NotNull
    private LootItemConditionalFunction.Builder<?> countBetween(int f, int g) {
        return SetItemCountFunction.setCount(UniformGenerator.between(f, g));
    }

    @NotNull
    private LootPoolSingletonContainer.Builder<?> createItem(Item item, int weight) {
        return LootItem.lootTableItem(item).setWeight(weight);
    }

    @NotNull
    private LootPoolSingletonContainer.Builder<?> createBiomeExclusiveItem(Item item, ResourceKey<Biome> biome, int weight) throws InterruptedException, ExecutionException {
        return createItem(item, weight).when(LocationCheck.checkLocation(LocationPredicate.Builder.inBiome(lookup.get().lookupOrThrow(Registries.BIOME).getOrThrow(biome))));
    }

    @NotNull
    private LootPoolSingletonContainer.Builder<?> createEnchantedItem(Item item, int minEnchant, int maxEnchant, int weight) throws InterruptedException, ExecutionException {
        return createItem(item, weight).apply(EnchantWithLevelsFunction.enchantWithLevels(lookup.get(), UniformGenerator.between(minEnchant, maxEnchant)));
    }

    private LootPoolSingletonContainer.Builder<?> createEnchantedTrimmed(Item item, int minEnchant, int maxEnchant, ResourceKey<TrimMaterial> material, ResourceKey<TrimPattern> pattern, int weight) throws InterruptedException, ExecutionException {
        Holder.Reference<TrimMaterial> materialReference = lookup.get().lookupOrThrow(Registries.TRIM_MATERIAL).getOrThrow(material);
        Holder.Reference<TrimPattern> patternReference = lookup.get().lookupOrThrow(Registries.TRIM_PATTERN).getOrThrow(pattern);
        return createEnchantedItem(item, minEnchant, maxEnchant, weight).apply(SetComponentsFunction.setComponent(DataComponents.TRIM, new ArmorTrim(materialReference, patternReference)));
    }

    @NotNull
    private LootPoolSingletonContainer.Builder<?> createEnchantedBook(ResourceKey<Enchantment> enchantment, int weight) throws InterruptedException, ExecutionException {
        return createItem(BOOK, weight).apply(new SetEnchantmentsFunction.Builder().withEnchantment(getEnchantmentReference(enchantment), ConstantValue.exactly(1)));
    }

    @NotNull
    private Holder.Reference<Enchantment> getEnchantmentReference(ResourceKey<Enchantment> enchantment) throws InterruptedException, ExecutionException {
        return lookup.get().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantment);
    }
}