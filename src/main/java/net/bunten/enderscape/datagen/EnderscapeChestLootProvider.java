package net.bunten.enderscape.datagen;

import net.bunten.enderscape.registry.EnderscapeBiomes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

import static net.bunten.enderscape.registry.EnderscapeItems.*;
import static net.bunten.enderscape.registry.EnderscapeLootTables.*;
import static net.minecraft.world.item.Items.*;

public class EnderscapeChestLootProvider extends SimpleFabricLootTableProvider {

    private final CompletableFuture<HolderLookup.Provider> lookup;

    public EnderscapeChestLootProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup, LootContextParamSets.CHEST);
        this.lookup = lookup;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
        try {
            LootItemConditionalFunction.Builder<?> toolDamage = SetItemDamageFunction.setDamage(UniformGenerator.between(0.65F, 0.8F));
            EnchantWithLevelsFunction.Builder toolEnchantments = EnchantWithLevelsFunction.enchantWithLevels(lookup.get(), UniformGenerator.between(20, 30));

            consumer.accept(
                    END_CITY_CHEST,
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(UniformGenerator.between(3, 5))

                                            .add(createItem(CHORUS_FRUIT, 12).apply(countBetween(2, 6)))
                                            .add(addBiomeExclusiveItem(FLANGER_BERRY, EnderscapeBiomes.CELESTIAL_GROVE, 10).apply(countBetween(2, 4)))
                                            .add(createItem(BREAD, 8).apply(countBetween(2, 5)))
                                            .add(createItem(ENDER_PEARL, 8).apply(countBetween(1, 2)))
                                            .add(createItem(SPECTRAL_ARROW, 8).apply(countBetween(4, 8)))

                                            .add(createItem(HONEY_BOTTLE, 6).apply(countBetween(1, 3)))

                                            .add(createItem(DIAMOND, 4).apply(countBetween(1, 2)))
                                            .add(createItem(GOLD_INGOT, 4).apply(countBetween(1, 4)))
                                            .add(createItem(IRON_INGOT, 4).apply(countBetween(2, 8)))

                                            .add(createItem(GLASS_BOTTLE, 4).apply(countBetween(1, 3)))
                                            .add(createItem(GUNPOWDER, 4).apply(countBetween(2, 4)))
                                            .add(createItem(LODESTONE, 4))

                                            .add(createItem(IRON_SWORD, 3).apply(toolEnchantments).apply(toolDamage))
                                            .add(createItem(IRON_PICKAXE, 3).apply(toolEnchantments).apply(toolDamage))
                                            .add(createItem(IRON_AXE, 3).apply(toolEnchantments).apply(toolDamage))
                                            .add(createItem(IRON_HOE, 3).apply(toolEnchantments).apply(toolDamage))

                                            .add(createItem(IRON_HELMET, 3).apply(toolEnchantments).apply(toolDamage))
                                            .add(createItem(IRON_CHESTPLATE, 3).apply(toolEnchantments).apply(toolDamage))
                                            .add(createItem(IRON_LEGGINGS, 3).apply(toolEnchantments).apply(toolDamage))
                                            .add(createItem(IRON_BOOTS, 3).apply(toolEnchantments).apply(toolDamage))

                                            .add(createEnchantedItem(BOW, 5, 15, 3))

                                            .add(createItem(MAGENTA_DYE, 1).apply(countBetween(3, 6)))
                                            .add(createItem(BLACK_DYE, 1).apply(countBetween(3, 6)))

                                            .add(createItem(SPLASH_POTION, 1).apply(potionEffect(Potions.STRONG_HEALING)).apply(countBetween(1, 2)))
                                            .add(createItem(SPLASH_POTION, 1).apply(potionEffect(Potions.STRONG_HARMING)).apply(countBetween(1, 2)))
                                            .add(createItem(SPLASH_POTION, 1).apply(potionEffect(Potions.LONG_SLOW_FALLING)).apply(countBetween(1, 2)))
                            )
            );

            consumer.accept(
                    END_CITY_SPAWNER_BASIC,
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(UniformGenerator.between(1, 3))
                                            .add(createItem(BREAD, 32).apply(countBetween(2, 8)))
                                            .add(createItem(GOLDEN_CARROT, 16).apply(countBetween(2, 8)))
                                            .add(createItem(GOLDEN_APPLE, 4).apply(countBetween(1, 3)))
                                            .add(createItem(SPLASH_POTION, 4).apply(potionEffect(Potions.STRONG_HEALING)))
                                            .add(createItem(LINGERING_POTION, 4).apply(potionEffect(Potions.LONG_SLOWNESS)))
                            )
            );

            consumer.accept(
                    END_CITY_SPAWNER_KEY,
                    LootTable.lootTable().withPool(LootPool.lootPool().add(createItem(END_CITY_KEY, 2)))
            );

            consumer.accept(
                    MIRESTONE_RUINS_CHEST,
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(UniformGenerator.between(2, 4))
                                            .add(createItem(BONE, 12).apply(countBetween(3, 6)))
                                            .add(createItem(CHORUS_FRUIT, 12).apply(countBetween(2, 6)))

                                            .add(createItem(BREAD, 8).apply(countBetween(2, 4)))
                                            .add(createItem(ENDER_PEARL, 8).apply(countBetween(1, 6)))
                                            .add(createItem(MURUBLIGHT_SHELF_ITEM, 8).apply(countBetween(1, 2)))

                                            .add(createItem(GUNPOWDER, 6).apply(countBetween(4, 8)))
                                            .add(createItem(LEATHER, 6).apply(countBetween(4, 8)))
                                            .add(createItem(PAPER, 6).apply(countBetween(4, 8)))
                                            .add(createItem(SUGAR, 6).apply(countBetween(3, 6)))

                                            .add(createItem(FLANGER_BERRY, 3).apply(countBetween(2, 8)))
                                            .add(createItem(BLACK_DYE, 3).apply(countBetween(1, 6)))

                                            .add(createItem(MUSIC_DISC_DECAY, 3))
                            )
            );

        } catch (InterruptedException | ExecutionException exception) {
            throw new RuntimeException("Error processing loot table", exception);
        }
    }

    @NotNull
    private LootItemConditionalFunction.Builder<?> potionEffect(Holder<Potion> potion) throws InterruptedException, ExecutionException {
        return SetComponentsFunction.setComponent(DataComponents.POTION_CONTENTS, new PotionContents(getPotion(potion)));
    }

    @NotNull
    private Holder.Reference<Potion> getPotion(Holder<Potion> potion) throws InterruptedException, ExecutionException {
        return lookup.get().lookupOrThrow(Registries.POTION).getOrThrow(potion.unwrapKey().get());
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
    private LootPoolSingletonContainer.Builder<?> addBiomeExclusiveItem(Item item, ResourceKey<Biome> biome, int weight) throws InterruptedException, ExecutionException {
        return createItem(item, weight).when(LocationCheck.checkLocation(LocationPredicate.Builder.inBiome(lookup.get().lookupOrThrow(Registries.BIOME).getOrThrow(biome))));
    }

    @NotNull
    private LootPoolSingletonContainer.Builder<?> createEnchantedItem(Item item, int minEnchant, int maxEnchant, int weight) throws InterruptedException, ExecutionException {
        return createItem(item, weight).apply(EnchantWithLevelsFunction.enchantWithLevels(lookup.get(), UniformGenerator.between(minEnchant, maxEnchant)));
    }

    @NotNull
    private LootPoolSingletonContainer.Builder<?> createEnchantedItem(Item item, ResourceKey<Enchantment> enchantment, UniformGenerator uniform, int weight) throws InterruptedException, ExecutionException {
        return createItem(item, weight).apply(new SetEnchantmentsFunction.Builder().withEnchantment(lookup.get().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantment), uniform));
    }
}