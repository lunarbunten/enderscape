package net.bunten.enderscape.datagen;

import net.bunten.enderscape.block.VeiledLeafPileBlock;
import net.bunten.enderscape.block.VoidShaleBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;
import static net.bunten.enderscape.registry.EnderscapeItems.*;
import static net.minecraft.world.item.Items.END_STONE;

public class EnderscapeBlockLootProvider extends FabricBlockLootTableProvider {

    protected EnderscapeBlockLootProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup);
    }

    @Override
    public void generate() {
        HolderLookup.RegistryLookup<Enchantment> enchantment = registries.lookupOrThrow(Registries.ENCHANTMENT);

        dropSelf(DRIFT_JELLY_BLOCK);

        add(VEILED_END_STONE, block -> createSingleItemTableWithSilkTouch(block, END_STONE));
        add(CELESTIAL_OVERGROWTH, block -> createSingleItemTableWithSilkTouch(block, END_STONE));
        add(CORRUPT_OVERGROWTH, block -> createSingleItemTableWithSilkTouch(block, MIRESTONE));

        dropOther(CELESTIAL_PATH, END_STONE);
        dropOther(CORRUPT_PATH, END_STONE);

        add(DRY_END_GROWTH, this::createShearsOnlyDrop);
        dropPottedContents(POTTED_DRY_END_GROWTH);

        add(WISP_SPROUTS, this::createShearsOnlyDrop);
        add(WISP_GROWTH, this::createShearsOnlyDrop);
        dropPottedContents(POTTED_WISP_GROWTH);

        add(WISP_FLOWER, block -> createSinglePropConditionTable(block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));

        dropSelf(VEILED_SAPLING);
        dropPottedContents(POTTED_VEILED_SAPLING);

        add(CHORUS_SPROUTS, this::createShearsOnlyDrop);
        dropPottedContents(POTTED_CHORUS_SPROUTS);

        add(CELESTIAL_GROWTH, this::createShearsOnlyDrop);
        dropPottedContents(POTTED_CELESTIAL_GROWTH);

        add(CORRUPT_GROWTH, this::createShearsOnlyDrop);
        dropPottedContents(POTTED_CORRUPT_GROWTH);

        dropSelf(BULB_FLOWER);
        dropPottedContents(POTTED_BULB_FLOWER);

        dropSelf(END_LAMP);
        dropSelf(BULB_LANTERN);
        dropSelf(BLINKLAMP);

        add(FLANGER_BERRY_FLOWER, this::createShearsOnlyDrop);
        add(UNRIPE_FLANGER_BERRY_BLOCK, this::createShearsOnlyDrop);
        add(RIPE_FLANGER_BERRY_BLOCK, block -> createSilkTouchOrShearsDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(FLANGER_BERRY)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                        .apply(ApplyBonusCount.addUniformBonusCount(getFortune()))
                        .apply(LimitCount.limitCount(IntRange.upperBound(6)))
                )
        ));

        dropOther(BLINKLIGHT_VINES_BODY, BLINKLIGHT);
        dropOther(BLINKLIGHT_VINES_HEAD, BLINKLIGHT);
        dropPottedContents(POTTED_BLINKLIGHT);

        dropSelf(VEILED_LOG);
        dropSelf(STRIPPED_VEILED_LOG);
        dropSelf(VEILED_WOOD);
        dropSelf(STRIPPED_VEILED_WOOD);

        add(VEILED_LEAVES, block -> createLeavesDrops(block, VEILED_SAPLING));
        add(VEILED_LEAF_PILE, createVeiledLeafPile());
        add(VEILED_VINES, block -> createSilkTouchOrShearsDispatchTable(block, LootItem.lootTableItem(block).when(BonusLevelTableCondition.bonusLevelFlatChance(enchantment.getOrThrow(Enchantments.FORTUNE), 0.33F, 0.55F, 0.77F, 1.0F))));

        dropSelf(VEILED_PLANKS);
        dropSelf(VEILED_STAIRS);
        dropSelf(VEILED_SLAB);
        dropSelf(VEILED_FENCE);
        dropSelf(VEILED_FENCE_GATE);
        add(VEILED_DOOR, this::createDoorTable);
        dropSelf(VEILED_TRAPDOOR);
        dropSelf(VEILED_PRESSURE_PLATE);
        dropSelf(VEILED_BUTTON);
        dropOther(VEILED_SIGN, VEILED_SIGN_ITEM);
        dropOther(VEILED_WALL_SIGN, VEILED_SIGN_ITEM);
        dropOther(VEILED_HANGING_SIGN, VEILED_HANGING_SIGN_ITEM);
        dropOther(VEILED_WALL_HANGING_SIGN, VEILED_HANGING_SIGN_ITEM);

        dropSelf(CELESTIAL_CHANTERELLE);
        dropPottedContents(POTTED_CELESTIAL_CHANTERELLE);

        dropSelf(CELESTIAL_CAP);
        dropSelf(CELESTIAL_BRICKS);
        dropSelf(CELESTIAL_BRICK_STAIRS);
        dropSelf(CELESTIAL_BRICK_SLAB);
        dropSelf(CELESTIAL_BRICK_WALL);

        dropSelf(DUSK_PURPUR_BLOCK);
        dropSelf(DUSK_PURPUR_PILLAR);
        dropSelf(DUSK_PURPUR_STAIRS);
        dropSelf(DUSK_PURPUR_SLAB);
        dropSelf(DUSK_PURPUR_WALL);
        dropSelf(CHISELED_DUSK_PURPUR);

        dropSelf(PURPUR_TILES);
        dropSelf(PURPUR_TILE_STAIRS);
        dropSelf(PURPUR_TILE_SLAB);

        dropSelf(CELESTIAL_STEM);
        dropSelf(STRIPPED_CELESTIAL_STEM);
        dropSelf(CELESTIAL_HYPHAE);
        dropSelf(STRIPPED_CELESTIAL_HYPHAE);
        dropSelf(CELESTIAL_PLANKS);
        dropSelf(CELESTIAL_STAIRS);
        dropSelf(CELESTIAL_SLAB);
        dropSelf(CELESTIAL_FENCE);
        dropSelf(CELESTIAL_FENCE_GATE);
        add(CELESTIAL_DOOR, this::createDoorTable);
        dropSelf(CELESTIAL_TRAPDOOR);
        dropSelf(CELESTIAL_PRESSURE_PLATE);
        dropSelf(CELESTIAL_BUTTON);
        dropOther(CELESTIAL_SIGN, CELESTIAL_SIGN_ITEM);
        dropOther(CELESTIAL_WALL_SIGN, CELESTIAL_SIGN_ITEM);
        dropOther(CELESTIAL_HANGING_SIGN, CELESTIAL_HANGING_SIGN_ITEM);
        dropOther(CELESTIAL_WALL_HANGING_SIGN, CELESTIAL_HANGING_SIGN_ITEM);

        dropOther(MURUBLIGHT_BRACKET, MURUBLIGHT_BRACKET_ITEM);

        dropSelf(MURUBLIGHT_CHANTERELLE);
        dropPottedContents(POTTED_MURUBLIGHT_CHANTERELLE);

        dropSelf(MURUBLIGHT_CAP);
        dropSelf(MURUBLIGHT_BRICKS);
        dropSelf(MURUBLIGHT_BRICK_STAIRS);
        dropSelf(MURUBLIGHT_BRICK_SLAB);
        dropSelf(MURUBLIGHT_BRICK_WALL);

        dropSelf(MURUBLIGHT_STEM);
        dropSelf(STRIPPED_MURUBLIGHT_STEM);
        dropSelf(MURUBLIGHT_HYPHAE);
        dropSelf(STRIPPED_MURUBLIGHT_HYPHAE);
        dropSelf(MURUBLIGHT_PLANKS);
        dropSelf(MURUBLIGHT_STAIRS);
        dropSelf(MURUBLIGHT_SLAB);
        dropSelf(MURUBLIGHT_FENCE);
        dropSelf(MURUBLIGHT_FENCE_GATE);
        add(MURUBLIGHT_DOOR, this::createDoorTable);
        dropSelf(MURUBLIGHT_TRAPDOOR);
        dropSelf(MURUBLIGHT_PRESSURE_PLATE);
        dropSelf(MURUBLIGHT_BUTTON);
        dropOther(MURUBLIGHT_SIGN, MURUBLIGHT_SIGN_ITEM);
        dropOther(MURUBLIGHT_WALL_SIGN, MURUBLIGHT_SIGN_ITEM);
        dropOther(MURUBLIGHT_HANGING_SIGN, MURUBLIGHT_HANGING_SIGN_ITEM);
        dropOther(MURUBLIGHT_WALL_HANGING_SIGN, MURUBLIGHT_HANGING_SIGN_ITEM);

        dropSelf(ALLURING_MAGNIA);
        dropSelf(ALLURING_MAGNIA_SPROUT);
        dropSelf(REPULSIVE_MAGNIA);
        dropSelf(REPULSIVE_MAGNIA_SPROUT);

        dropSelf(BLISTERED_MAGNIA);
        dropSelf(POLARIZED_MAGNIA);

        dropSelf(ETCHED_ALLURING_MAGNIA);
        dropSelf(ETCHED_ALLURING_MAGNIA_STAIRS);
        dropSelf(ETCHED_ALLURING_MAGNIA_SLAB);
        dropSelf(ETCHED_ALLURING_MAGNIA_WALL);

        dropSelf(ETCHED_REPULSIVE_MAGNIA);
        dropSelf(ETCHED_REPULSIVE_MAGNIA_STAIRS);
        dropSelf(ETCHED_REPULSIVE_MAGNIA_SLAB);
        dropSelf(ETCHED_REPULSIVE_MAGNIA_WALL);

        dropSelf(MAGNIA_RADIO);

        dropPottedContents(POTTED_ALLURING_MAGNIA_SPROUT);
        dropPottedContents(POTTED_REPULSIVE_MAGNIA_SPROUT);

        add(VOID_SHALE, block -> {
            LootItemBlockStatePropertyCondition.Builder isNatural = LootItemBlockStatePropertyCondition.hasBlockStateProperties(VOID_SHALE).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(VoidShaleBlock.NATURAL, true));

            return applyExplosionDecay(block, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .when(entityPresent().invert())
                            .when(isNatural)
                            .when(LootItemRandomChanceCondition.randomChance(0.2F))
                            .add(LootItem.lootTableItem(block)))
                    .withPool(LootPool.lootPool()
                            .when(entityPresent().invert())
                            .when(isNatural.invert())
                            .add(LootItem.lootTableItem(block)))
                    .withPool(LootPool.lootPool()
                            .when(entityPresent())
                            .add(LootItem.lootTableItem(block)))
            );
        });

        Function<Block, LootTable.Builder> nebuliteOreDrops = block -> createSilkTouchDispatchTable(
                block,
                applyExplosionDecay(
                        block,
                        LootItem.lootTableItem(NEBULITE_SHARDS)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                .apply(ApplyBonusCount.addOreBonusCount(enchantment.getOrThrow(Enchantments.FORTUNE)))
                                .apply(LimitCount.limitCount(IntRange.upperBound(8)))
                )
        );

        add(NEBULITE_ORE, nebuliteOreDrops);
        add(MIRESTONE_NEBULITE_ORE, nebuliteOreDrops);

        dropSelf(NEBULITE_BLOCK);

        Function<Block, LootTable.Builder> shadolineOreDrops = block -> createSilkTouchDispatchTable(
                block,
                applyExplosionDecay(
                        block,
                        LootItem.lootTableItem(RAW_SHADOLINE)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                .apply(ApplyBonusCount.addOreBonusCount(enchantment.getOrThrow(Enchantments.FORTUNE)))
                )
        );

        add(SHADOLINE_ORE, shadolineOreDrops);
        add(MIRESTONE_SHADOLINE_ORE, shadolineOreDrops);

        dropSelf(RAW_SHADOLINE_BLOCK);
        dropSelf(SHADOLINE_BLOCK);
        dropSelf(SHADOLINE_BLOCK_STAIRS);
        dropSelf(SHADOLINE_BLOCK_SLAB);
        dropSelf(SHADOLINE_BLOCK_WALL);
        dropSelf(CUT_SHADOLINE);
        dropSelf(CUT_SHADOLINE_STAIRS);
        dropSelf(CUT_SHADOLINE_SLAB);
        dropSelf(CUT_SHADOLINE_WALL);
        dropSelf(CHISELED_SHADOLINE);
        dropSelf(SHADOLINE_PILLAR);

        dropSelf(END_STONE_STAIRS);
        dropSelf(END_STONE_SLAB);
        dropSelf(END_STONE_WALL);
        dropSelf(POLISHED_END_STONE);
        dropSelf(POLISHED_END_STONE_STAIRS);
        dropSelf(POLISHED_END_STONE_SLAB);
        dropSelf(POLISHED_END_STONE_WALL);
        dropSelf(POLISHED_END_STONE_BUTTON);
        dropSelf(POLISHED_END_STONE_PRESSURE_PLATE);
        dropSelf(CHISELED_END_STONE);

        dropSelf(CHISELED_PURPUR);
        dropSelf(PURPUR_WALL);

        dropSelf(MIRESTONE);
        dropSelf(MIRESTONE_STAIRS);
        dropSelf(MIRESTONE_SLAB);
        dropSelf(MIRESTONE_WALL);
        dropSelf(POLISHED_MIRESTONE);
        dropSelf(POLISHED_MIRESTONE_STAIRS);
        dropSelf(POLISHED_MIRESTONE_SLAB);
        dropSelf(POLISHED_MIRESTONE_WALL);
        dropSelf(POLISHED_MIRESTONE_BUTTON);
        dropSelf(POLISHED_MIRESTONE_PRESSURE_PLATE);
        dropSelf(MIRESTONE_BRICKS);
        dropSelf(MIRESTONE_BRICK_STAIRS);
        dropSelf(MIRESTONE_BRICK_SLAB);
        dropSelf(MIRESTONE_BRICK_WALL);
        dropSelf(CHISELED_MIRESTONE);

        dropSelf(VERADITE);
        dropSelf(VERADITE_STAIRS);
        dropSelf(VERADITE_SLAB);
        dropSelf(VERADITE_WALL);
        dropSelf(POLISHED_VERADITE);
        dropSelf(POLISHED_VERADITE_STAIRS);
        dropSelf(POLISHED_VERADITE_SLAB);
        dropSelf(POLISHED_VERADITE_WALL);
        dropSelf(POLISHED_VERADITE_BUTTON);
        dropSelf(POLISHED_VERADITE_PRESSURE_PLATE);
        dropSelf(VERADITE_BRICKS);
        dropSelf(VERADITE_BRICK_STAIRS);
        dropSelf(VERADITE_BRICK_SLAB);
        dropSelf(VERADITE_BRICK_WALL);
        dropSelf(CHISELED_VERADITE);

        dropSelf(KURODITE);
        dropSelf(KURODITE_STAIRS);
        dropSelf(KURODITE_SLAB);
        dropSelf(KURODITE_WALL);
        dropSelf(POLISHED_KURODITE);
        dropSelf(POLISHED_KURODITE_STAIRS);
        dropSelf(POLISHED_KURODITE_SLAB);
        dropSelf(POLISHED_KURODITE_WALL);
        dropSelf(POLISHED_KURODITE_BUTTON);
        dropSelf(POLISHED_KURODITE_PRESSURE_PLATE);
        dropSelf(KURODITE_BRICKS);
        dropSelf(KURODITE_BRICK_STAIRS);
        dropSelf(KURODITE_BRICK_SLAB);
        dropSelf(KURODITE_BRICK_WALL);
        dropSelf(CHISELED_KURODITE);
    }

    private Function<Block, LootTable.Builder> createVeiledLeafPile() {
        return block -> LootTable.lootTable().withPool(LootPool.lootPool().when(LootItemEntityPropertyCondition.entityPresent(LootContext.EntityTarget.THIS)).add(AlternativesEntry.alternatives(VeiledLeafPileBlock.LAYERS.getPossibleValues(), integer -> integer == 8 ? LootItem.lootTableItem(VEILED_LEAVES) : LootItem.lootTableItem(VEILED_LEAF_PILE).apply(SetItemCountFunction.setCount(ConstantValue.exactly(integer))).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SnowLayerBlock.LAYERS, integer))))));
    }

    public LootTable.Builder createLeavesDrops(Block leaves, ItemLike plant) {
        HolderLookup.RegistryLookup<Enchantment> enchantment = registries.lookupOrThrow(Registries.ENCHANTMENT);
        return createSilkTouchOrShearsDispatchTable(
                leaves,
                applyExplosionCondition(leaves, LootItem.lootTableItem(plant)).when(BonusLevelTableCondition.bonusLevelFlatChance(enchantment.getOrThrow(Enchantments.FORTUNE), 0.05F, 0.0625F, 0.083333336F, 0.1F))
        )
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .when(doesNotHaveShearsOrSilkTouch())
                                .add(applyExplosionDecay(
                                        leaves, LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                                        .when(BonusLevelTableCondition.bonusLevelFlatChance(enchantment.getOrThrow(Enchantments.FORTUNE), 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F))
                                )
                );
    }


    @NotNull
    private Holder.Reference<Enchantment> getFortune() {
        return registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE);
    }

    public LootItemCondition.Builder entityPresent() {
        return LootItemEntityPropertyCondition.entityPresent(LootContext.EntityTarget.THIS);
    }
}