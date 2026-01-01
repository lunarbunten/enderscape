package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.EnderscapeConfig;
import net.bunten.enderscape.block.dispenser.LodestoneTeleportationDispenserBehavior;
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.fabricmc.fabric.api.event.registry.DynamicRegistryView;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.fabricmc.fabric.api.registry.LandPathNodeTypesRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Optional;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;
import static net.bunten.enderscape.registry.EnderscapeItems.*;
import static net.minecraft.world.item.Items.CHORUS_FRUIT;
import static net.minecraft.world.item.equipment.EquipmentAssets.ROOT_ID;
import static net.minecraft.world.level.block.Blocks.CHORUS_FLOWER;
import static net.minecraft.world.level.block.Blocks.CHORUS_PLANT;

public class EnderscapeCompatibility {

    private static final EnderscapeConfig CONFIG = EnderscapeConfig.getInstance();

    public static final ResourceKey<EquipmentAsset> SHULKER_SHELL_ASSET = ResourceKey.create(ROOT_ID, Enderscape.id("shulker_shell"));

    static {
        registerAliases();
        registerCompostables();
        registerDispenserBehavior();
        registerFlammableBlocks();
        registerFuelItems();
        registerStrippables();

        LandPathNodeTypesRegistry.register(DRIFT_JELLY_BLOCK, (state, neighbor) -> PathType.DAMAGE_OTHER);

        DefaultItemComponentEvents.MODIFY.register((context) -> {
            context.modify(
                    item -> item == Items.SHULKER_SHELL,
                    (builder, item) -> builder.set(
                            DataComponents.EQUIPPABLE,
                            Equippable.builder(ArmorType.HELMET.getSlot())
                                    .setSwappable(false)
                                    .setEquipSound(EnderscapeItemSounds.SHULKER_SHELL_EQUIP)
                                    .setAsset(SHULKER_SHELL_ASSET)
                                    .build()
                    )
            );
        });

        LootTableEvents.MODIFY.register(Enderscape.id("inject_supplemental_loot_tables"), (key, builder, source, registries) -> {
            addLootTableInjection(
                    CONFIG.supplementVanillaStrongholdLibraryLoot,
                    BuiltInLootTables.STRONGHOLD_LIBRARY,
                    EnderscapeLootTables.STRONGHOLD_LIBRARY_CHEST_SUPPLEMENTS,
                    key,
                    builder
            );
        });
    }

    private static void addLootTableInjection(boolean allowed, ResourceKey<LootTable> original, ResourceKey<LootTable> injection, ResourceKey<LootTable> key, LootTable.Builder builder) {
        if (allowed && key.equals(original)) {
            builder.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(NestedLootTable.lootTableReference(injection).setWeight(1)));
        }
    }

    private static void registerAliases() {
        DynamicRegistrySetupCallback.EVENT.register((registryView) -> {
            dynamicRegistryAlias(registryView, Registries.BIOME, "magnia_crags", EnderscapeBiomes.MAGNIA_FIELDS.identifier().getPath());
            dynamicRegistryAlias(registryView, Registries.DATA_COMPONENT_TYPE, "current_nebulite_fuel", "current_fuel");
            dynamicRegistryAlias(registryView, Registries.ENCHANTMENT, "lightspeed", EnderscapeEnchantments.RESONANCE.identifier().getPath());
        });

        blockAndItemAlias("celestial_path_block", "celestial_path");
        blockAndItemAlias("corrupt_path_block", "corrupt_path");
    }

    private static <T> void dynamicRegistryAlias(DynamicRegistryView view, ResourceKey<Registry<T>> registry, String old_name, String new_name) {
        Optional<Registry<T>> optional = view.getOptional(registry);
        optional.ifPresent(value -> value.addAlias(Enderscape.id(old_name), Enderscape.id(new_name)));
    }

    private static void blockAndItemAlias(String previous, String current) {
        BuiltInRegistries.BLOCK.addAlias(Enderscape.id(previous), Enderscape.id(current));
        BuiltInRegistries.ITEM.addAlias(Enderscape.id(previous), Enderscape.id(current));
    }

    private static void registerCompostables() {
        registerCompostableItem(0.1F, VEILED_LEAF_PILE);
        registerCompostableItem(0.3F, WISP_SPROUTS);
        registerCompostableItem(0.3F, CELESTIAL_CAP);
        registerCompostableItem(0.3F, CELESTIAL_GROWTH);
        registerCompostableItem(0.3F, CHORUS_FRUIT);
        registerCompostableItem(0.3F, CORRUPT_GROWTH);
        registerCompostableItem(0.3F, MURUBLIGHT_CAP);
        registerCompostableItem(0.3F, VEILED_LEAVES);
        registerCompostableItem(0.5F, BLINKLIGHT);
        registerCompostableItem(0.5F, CHORUS_SPROUTS);
        registerCompostableItem(0.5F, DRY_END_GROWTH);
        registerCompostableItem(0.5F, WISP_GROWTH);
        registerCompostableItem(0.5F, FLANGER_BERRY);
        registerCompostableItem(0.5F, MURUBLIGHT_BRACKET);
        registerCompostableItem(0.5F, VEILED_SAPLING);
        registerCompostableItem(0.65F, BULB_FLOWER);
        registerCompostableItem(0.65F, CELESTIAL_CHANTERELLE);
        registerCompostableItem(0.65F, MURUBLIGHT_CHANTERELLE);
        registerCompostableItem(0.65F, RIPE_FLANGER_BERRY_BLOCK);
        registerCompostableItem(0.65F, WISP_FLOWER);
    }

    private static void registerDispenserBehavior() {
        DispenserBlock.registerBehavior(RUSTLE_BUCKET, new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultBehavior = new DefaultDispenseItemBehavior();

            @Override
            public ItemStack execute(BlockSource source, ItemStack stack) {
                DispensibleContainerItem item = (DispensibleContainerItem) stack.getItem();
                BlockPos pos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
                Level level = source.level();

                if (item.emptyContents(null, level, pos, null)) {
                    item.checkExtraContent(null, level, stack, pos);
                    return consumeWithRemainder(source, stack, new ItemStack(Items.BUCKET));
                } else {
                    return defaultBehavior.dispense(source, stack);
                }
            }
        });

        DispenserBlock.registerBehavior(MIRROR, new LodestoneTeleportationDispenserBehavior());
    }

    private static void registerFlammableBlocks() {
        FlammableBlockRegistry.getDefaultInstance().add(DRY_END_GROWTH, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(CHORUS_SPROUTS, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(WISP_GROWTH, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(WISP_SPROUTS, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(WISP_FLOWER, 60, 100);

        FlammableBlockRegistry.getDefaultInstance().add(CHORUS_PLANT, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(CHORUS_FLOWER, 5, 5);

        FlammableBlockRegistry.getDefaultInstance().add(VEILED_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(VEILED_WOOD, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_VEILED_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_VEILED_WOOD, 5, 5);

        FlammableBlockRegistry.getDefaultInstance().add(VEILED_PLANKS, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(VEILED_SLAB, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(VEILED_FENCE_GATE, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(VEILED_FENCE, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(VEILED_STAIRS, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(VEILED_SHELF, 30, 20);

        FlammableBlockRegistry.getDefaultInstance().add(VEILED_SAPLING, 15, 60);
        FlammableBlockRegistry.getDefaultInstance().add(VEILED_LEAVES, 30, 60);
        FlammableBlockRegistry.getDefaultInstance().add(VEILED_LEAF_PILE, 60, 20);

        FlammableBlockRegistry.getDefaultInstance().add(CELESTIAL_STEM, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(CELESTIAL_HYPHAE, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_CELESTIAL_STEM, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_CELESTIAL_HYPHAE, 5, 5);

        FlammableBlockRegistry.getDefaultInstance().add(CELESTIAL_PLANKS, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(CELESTIAL_SLAB, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(CELESTIAL_FENCE_GATE, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(CELESTIAL_FENCE, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(CELESTIAL_STAIRS, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(CELESTIAL_SHELF, 30, 20);

        FlammableBlockRegistry.getDefaultInstance().add(CELESTIAL_CAP, 30, 60);

        FlammableBlockRegistry.getDefaultInstance().add(MURUBLIGHT_STEM, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(MURUBLIGHT_HYPHAE, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_MURUBLIGHT_STEM, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(STRIPPED_MURUBLIGHT_HYPHAE, 5, 5);

        FlammableBlockRegistry.getDefaultInstance().add(MURUBLIGHT_PLANKS, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(MURUBLIGHT_SLAB, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(MURUBLIGHT_FENCE_GATE, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(MURUBLIGHT_FENCE, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(MURUBLIGHT_STAIRS, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(MURUBLIGHT_SHELF, 30, 20);

        FlammableBlockRegistry.getDefaultInstance().add(MURUBLIGHT_CAP, 30, 60);
        
        FlammableBlockRegistry.getDefaultInstance().add(CELESTIAL_GROWTH, 60, 60);
        FlammableBlockRegistry.getDefaultInstance().add(BULB_FLOWER, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(FLANGER_BERRY_VINE, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(FLANGER_BERRY_FLOWER, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(UNRIPE_FLANGER_BERRY_BLOCK, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(RIPE_FLANGER_BERRY_BLOCK, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(CELESTIAL_CHANTERELLE, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(MURUBLIGHT_CHANTERELLE, 60, 100);

        FlammableBlockRegistry.getDefaultInstance().add(CORRUPT_GROWTH, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(BLINKLIGHT_VINES_BODY, 15, 60);
        FlammableBlockRegistry.getDefaultInstance().add(BLINKLIGHT_VINES_HEAD, 15, 60);
        FlammableBlockRegistry.getDefaultInstance().add(MURUBLIGHT_BRACKET, 60, 100);

        FlammableBlockRegistry.getDefaultInstance().add(VOID_SHALE, 5, 5);
    }

    private static void registerFuelItems() {
        FuelRegistryEvents.BUILD.register(Enderscape.id("add_fuels"), (builder, context) -> {
            builder.add(VOID_SHALE, context.baseSmeltTime());
        });
    }

    private static void registerStrippables() {
        StrippableBlockRegistry.register(VEILED_LOG, STRIPPED_VEILED_LOG);
        StrippableBlockRegistry.register(VEILED_WOOD, STRIPPED_VEILED_WOOD);
        StrippableBlockRegistry.register(CELESTIAL_STEM, STRIPPED_CELESTIAL_STEM);
        StrippableBlockRegistry.register(CELESTIAL_HYPHAE, STRIPPED_CELESTIAL_HYPHAE);
        StrippableBlockRegistry.register(MURUBLIGHT_STEM, STRIPPED_MURUBLIGHT_STEM);
        StrippableBlockRegistry.register(MURUBLIGHT_HYPHAE, STRIPPED_MURUBLIGHT_HYPHAE);
    }

    public static void registerCompostableItem(float chance, ItemLike item) {
        ComposterBlock.COMPOSTABLES.put(item.asItem(), chance);
    }
}