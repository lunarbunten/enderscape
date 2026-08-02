package net.penumbra.enderscape.registry;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityLevelChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.effect.ServerMobEffectEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.DynamicRegistrySetupCallback;
import net.fabricmc.fabric.api.event.registry.DynamicRegistryView;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelValueEvents;
import net.fabricmc.fabric.api.registry.LandPathTypeRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.fabricmc.fabric.api.registry.fluid.EntityFluidInteractionRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.item.equipment.ArmorType;
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
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.block.dispenser.LodestoneTeleportationDispenserBehavior;
import net.penumbra.enderscape.block.fluid.VoidLachrymaBehavior;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.entity.ai.EnderscapePathTypes;
import net.penumbra.enderscape.manager.EndHavenManager;
import net.penumbra.enderscape.manager.RustleItemConversionManager;
import net.penumbra.enderscape.manager.VoidLachrymaItemConversionManager;
import net.penumbra.enderscape.manager.VoidManager;
import net.penumbra.enderscape.registry.enchantment.EnderscapeEnchantments;
import net.penumbra.enderscape.registry.entity.EnderscapeAttachments;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import net.penumbra.enderscape.registry.item.EnderscapeEquipmentAssets;
import net.penumbra.enderscape.registry.level.EnderscapeBiomes;
import net.penumbra.enderscape.registry.server.EnderscapeLootTables;
import net.penumbra.enderscape.registry.sound.EnderscapeItemSounds;
import net.penumbra.enderscape.registry.tag.EnderscapeFluidTags;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.minecraft.world.item.Items.CHORUS_FRUIT;
import static net.minecraft.world.level.block.Blocks.CHORUS_FLOWER;
import static net.minecraft.world.level.block.Blocks.CHORUS_PLANT;
import static net.penumbra.enderscape.registry.block.EnderscapeBlocks.*;
import static net.penumbra.enderscape.registry.item.EnderscapeItems.*;

public class EnderscapeIntegration {

    private static final EnderscapeConfig CONFIG = EnderscapeConfig.getInstance();

    static {
        registerAliases();
        registerCompostableItems();
        registerDispenserBehavior();
        registerFlammableBlocks();
        registerFuelValues();
        registerStrippableBlocks();

        //noinspection UnstableApiUsage
        EntityFluidInteractionRegistry.register(EnderscapeFluidTags.VOID_LACHRYMA, new VoidLachrymaBehavior());

        LandPathTypeRegistry.register(DRIFT_JELLY_BLOCK, (state, neighbor) -> PathType.DAMAGING);
        LandPathTypeRegistry.register(VOID_FIRE, (state, neighbor) -> EnderscapePathTypes.VOID_FIRE);
        LandPathTypeRegistry.register(VOID_LACHRYMA, (state, neighbor) -> EnderscapePathTypes.VOID_LACHRYMA);
        LandPathTypeRegistry.register(VOID_SHALE, (state, neighbor) -> EnderscapePathTypes.VOID_SHALE);

        DefaultItemComponentEvents.MODIFY.register((context) -> {
            context.modify(
                    item -> item == Items.SHULKER_SHELL,
                    (builder, item) -> builder.set(
                            DataComponents.EQUIPPABLE,
                            Equippable.builder(ArmorType.HELMET.getSlot())
                                    .setSwappable(false)
                                    .setEquipSound(EnderscapeItemSounds.SHULKER_SHELL_EQUIP)
                                    .setAsset(EnderscapeEquipmentAssets.SHULKER_SHELL)
                                    .build()
                    )
            );

            context.modify(
                    item -> item == Items.TOTEM_OF_UNDYING,
                    (builder, item) -> {
                        DeathProtection protection = item.components().getOrDefault(DataComponents.DEATH_PROTECTION, DeathProtection.TOTEM_OF_UNDYING);
                        builder.set(DataComponents.DEATH_PROTECTION, addDeathProtectionEffects(
                                protection,
                                new MobEffectInstance(EnderscapeMobEffects.VOID_RESISTANCE, 10 * 20))
                        );
                    }
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

        ServerLivingEntityEvents.ALLOW_DAMAGE.register(VoidManager::allowLivingEntityDamage);
        ServerLivingEntityEvents.AFTER_DAMAGE.register(VoidManager::afterLivingEntityDamage);
        ServerLivingEntityEvents.AFTER_DEATH.register(EnderscapeAttachments::afterLivingEntityDeath);

        ServerLifecycleEvents.SERVER_STARTING.register(RustleItemConversionManager::onServerStarting);
        ServerLifecycleEvents.SERVER_STOPPING.register(RustleItemConversionManager::onServerStopping);

        ServerLifecycleEvents.SERVER_STARTING.register(VoidLachrymaItemConversionManager::onServerStarting);
        ServerLifecycleEvents.SERVER_STOPPING.register(VoidLachrymaItemConversionManager::onServerStopping);

        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> {
            if (success) {
                RustleItemConversionManager.onServerStarting(server);
                VoidLachrymaItemConversionManager.onServerStarting(server);
            }
        });

        ServerEntityLevelChangeEvents.AFTER_PLAYER_CHANGE_LEVEL.register((player, origin, destination) -> EndHavenManager.updateConnection(player));

        ServerMobEffectEvents.BEFORE_ADD.register((instance, entity, context) -> {
            if (instance.is(EnderscapeMobEffects.STUNNED)) {
                entity.setAttached(EnderscapeAttachments.STUN_DURATION, instance.getDuration());
                entity.setAttached(EnderscapeAttachments.STUN_TICKS, instance.getDuration());
            }
        });

        ServerMobEffectEvents.BEFORE_REMOVE.register((instance, entity, context) -> {
            if (instance.is(EnderscapeMobEffects.STUNNED)) {
                entity.removeAttached(EnderscapeAttachments.STUN_DURATION);
                entity.removeAttached(EnderscapeAttachments.STUN_TICKS);
            }
        });
    }

    private static DeathProtection addDeathProtectionEffects(DeathProtection protection, MobEffectInstance ... effects) {
        List<ConsumeEffect> original = new ArrayList<>(protection.deathEffects());

        for (MobEffectInstance instance : effects) {
            original.add(new ApplyStatusEffectsConsumeEffect(instance));
        }

        return new DeathProtection(original);
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

        BuiltInRegistries.BLOCK.addAlias(Enderscape.id("flanger_berry_vine"), Enderscape.id("puruberry_vine"));
        BuiltInRegistries.BLOCK.addAlias(Enderscape.id("flanger_berry_flower"), Enderscape.id("puruberry_flower"));
        BuiltInRegistries.BLOCK.addAlias(Enderscape.id("unripe_flanger_berry_block"), Enderscape.id("unripe_puruberry_block"));
        BuiltInRegistries.BLOCK.addAlias(Enderscape.id("ripe_flanger_berry_block"), Enderscape.id("ripe_puruberry_block"));
        BuiltInRegistries.ITEM.addAlias(Enderscape.id("flanger_berry"), Enderscape.id("puruberry"));

        BuiltInRegistries.ENTITY_TYPE.addAlias(Enderscape.id("driftlet"), Enderscape.id("drifter"));

        BuiltInRegistries.ITEM.addAlias(Enderscape.id("end_stone_rubble_shield"), Enderscape.id("rubble_shield"));
        BuiltInRegistries.ITEM.addAlias(Enderscape.id("mirestone_rubble_shield"), Enderscape.id("rubble_shield"));
        BuiltInRegistries.ITEM.addAlias(Enderscape.id("veradite_rubble_shield"), Enderscape.id("rubble_shield"));
        BuiltInRegistries.ITEM.addAlias(Enderscape.id("kurodite_rubble_shield"), Enderscape.id("rubble_shield"));

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

    private static void registerCompostableItems() {
        registerCompostableItems(0.1F, VEILED_LEAF_PILE);
        registerCompostableItems(0.3F, WISP_SPROUTS);
        registerCompostableItems(0.3F, CELESTIAL_CAP);
        registerCompostableItems(0.3F, CELESTIAL_GROWTH);
        registerCompostableItems(0.3F, CHORUS_FRUIT);
        registerCompostableItems(0.3F, CORRUPT_GROWTH);
        registerCompostableItems(0.3F, MURUBLIGHT_CAP);
        registerCompostableItems(0.3F, VEILED_LEAVES);
        registerCompostableItems(0.5F, BLINKLIGHT);
        registerCompostableItems(0.5F, CHORUS_SPROUTS);
        registerCompostableItems(0.5F, DRY_END_GROWTH);
        registerCompostableItems(0.5F, WISP_GROWTH);
        registerCompostableItems(0.5F, PURUBERRY);
        registerCompostableItems(0.5F, MURUBLIGHT_BRACKET);
        registerCompostableItems(0.5F, VEILED_SAPLING);
        registerCompostableItems(0.65F, BULB_FLOWER);
        registerCompostableItems(0.65F, CELESTIAL_CHANTERELLE);
        registerCompostableItems(0.65F, MURUBLIGHT_CHANTERELLE);
        registerCompostableItems(0.65F, RIPE_PURUBERRY_BLOCK);
        registerCompostableItems(0.65F, WISP_FLOWER);
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

        DispenserBlock.registerBehavior(VOID_LACHRYMA_BUCKET, new DefaultDispenseItemBehavior() {
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
        FlammableBlockRegistry.getDefaultInstance().add(PURUBERRY_VINE, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(PURUBERRY_FLOWER, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(UNRIPE_PURUBERRY_BLOCK, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(RIPE_PURUBERRY_BLOCK, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(CELESTIAL_CHANTERELLE, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(MURUBLIGHT_CHANTERELLE, 60, 100);

        FlammableBlockRegistry.getDefaultInstance().add(CORRUPT_GROWTH, 60, 100);
        FlammableBlockRegistry.getDefaultInstance().add(BLINKLIGHT_VINES_BODY, 15, 60);
        FlammableBlockRegistry.getDefaultInstance().add(BLINKLIGHT_VINES_HEAD, 15, 60);
        FlammableBlockRegistry.getDefaultInstance().add(MURUBLIGHT_BRACKET, 60, 100);

        FlammableBlockRegistry.getDefaultInstance().add(VOID_SHALE, 5, 5);
    }

    private static void registerFuelValues() {
        FuelValueEvents.BUILD.register(Enderscape.id("add_fuels"), (builder, context) -> {
            builder.add(VOID_SHALE, context.baseSmeltTime());
        });
    }

    private static void registerStrippableBlocks() {
        StrippableBlockRegistry.register(VEILED_LOG, STRIPPED_VEILED_LOG);
        StrippableBlockRegistry.register(VEILED_WOOD, STRIPPED_VEILED_WOOD);
        StrippableBlockRegistry.register(CELESTIAL_STEM, STRIPPED_CELESTIAL_STEM);
        StrippableBlockRegistry.register(CELESTIAL_HYPHAE, STRIPPED_CELESTIAL_HYPHAE);
        StrippableBlockRegistry.register(MURUBLIGHT_STEM, STRIPPED_MURUBLIGHT_STEM);
        StrippableBlockRegistry.register(MURUBLIGHT_HYPHAE, STRIPPED_MURUBLIGHT_HYPHAE);
    }

    public static void registerCompostableItems(float chance, ItemLike item) {
        ComposterBlock.COMPOSTABLES.put(item.asItem(), chance);
    }
}