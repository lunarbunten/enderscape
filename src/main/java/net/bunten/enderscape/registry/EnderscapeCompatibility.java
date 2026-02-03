package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.EnderscapeConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class EnderscapeCompatibility {

    private static final EnderscapeConfig CONFIG = EnderscapeConfig.getInstance();

    static {
        registerAliases();
    }

    private static void addLootTableInjection(boolean allowed, ResourceKey<LootTable> original, ResourceKey<LootTable> injection, ResourceKey<LootTable> key, LootTable.Builder builder) {
        if (allowed && key.equals(original)) {
            builder.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(NestedLootTable.lootTableReference(injection).setWeight(1)));
        }
    }

    private static void registerAliases() {
        //DynamicRegistrySetupCallback.EVENT.register((registryView) -> {
        //    dynamicRegistryAlias(registryView, Registries.BIOME, "magnia_crags", EnderscapeBiomes.MAGNIA_FIELDS.location().getPath());
        //    dynamicRegistryAlias(registryView, Registries.DATA_COMPONENT_TYPE, "current_nebulite_fuel", "current_fuel");
        //    dynamicRegistryAlias(registryView, Registries.ENCHANTMENT, "lightspeed", EnderscapeEnchantments.RESONANCE.location().getPath());
        //});

        BuiltInRegistries.ENTITY_TYPE.addAlias(Enderscape.id("driftlet"), Enderscape.id("drifter"));

        blockAndItemAlias("celestial_path_block", "celestial_path");
        blockAndItemAlias("corrupt_path_block", "corrupt_path");

        blockAndItemAlias("murublight_shelf", "murublight_bracket");
    }

    //private static <T> void dynamicRegistryAlias(DynamicRegistryView view, ResourceKey<Registry<T>> registry, String old_name, String new_name) {
    //    Optional<Registry<T>> optional = view.getOptional(registry);
    //    optional.ifPresent(value -> value.addAlias(Enderscape.id(old_name), Enderscape.id(new_name)));
    //}

    private static void blockAndItemAlias(String previous, String current) {
        BuiltInRegistries.BLOCK.addAlias(Enderscape.id(previous), Enderscape.id(current));
        BuiltInRegistries.ITEM.addAlias(Enderscape.id(previous), Enderscape.id(current));
    }
}