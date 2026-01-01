package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.registry.tag.EnderscapeItemTags;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.item.enchantment.effects.SetValue;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnderscapeEnchantments {

    public static final List<ResourceKey<Enchantment>> ENCHANTMENTS = new ArrayList<>();

    public static final ResourceKey<Enchantment> REBOUND = register("rebound");
    public static final ResourceKey<Enchantment> RESONANCE = register("resonance");
    public static final ResourceKey<Enchantment> TRANSDIMENSIONAL = register("transdimensional");

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);

        register(context, REBOUND,
                Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(EnderscapeItemTags.ELYTRA_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.constantCost(30),
                                Enchantment.constantCost(80),
                                8,
                                EquipmentSlotGroup.CHEST
                        )
                )
        );

        register(context, TRANSDIMENSIONAL,
                Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(EnderscapeItemTags.MIRROR_ENCHANTABLE),
                                1,
                                1,
                                Enchantment.constantCost(30),
                                Enchantment.constantCost(80),
                                8,
                                EquipmentSlotGroup.HAND
                        )
                ).withSpecialEffect(
                        EnderscapeEnchantmentEffectComponents.LODESTONE_TELEPORTATION_ENABLE_TRANSDIMENSIONAL,
                        new SetValue(LevelBasedValue.constant(1))
                )
        );

        register(context, RESONANCE,
                Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(EnderscapeItemTags.NEBULITE_TOOL_ENCHANTABLE),
                                10,
                                3,
                                Enchantment.dynamicCost(1, 15),
                                Enchantment.dynamicCost(20, 40),
                                2,
                                EquipmentSlotGroup.ANY
                        )
                ).withSpecialEffect(
                        EnderscapeEnchantmentEffectComponents.LODESTONE_TELEPORTATION_DISTANCE_TO_INCREASE_COST,
                        new AddValue(LevelBasedValue.perLevel(250))
                ).withEffect(
                        EnderscapeEnchantmentEffectComponents.INTEGER_COUNTER_THRESHOLD,
                        new AddValue(LevelBasedValue.perLevel(100)),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().of(EnderscapeItems.MAGNIA_ATTRACTOR))
                ).withEffect(
                        EnderscapeEnchantmentEffectComponents.INTEGER_COUNTER_THRESHOLD,
                        new AddValue(LevelBasedValue.perLevel(2)),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().of(EnderscapeItems.DAGGER))
                )
        );
    }

    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.location()));
    }

    private static ResourceKey<Enchantment> register(String name) {
        ResourceKey<Enchantment> key = ResourceKey.create(Registries.ENCHANTMENT, Enderscape.id(name));
        ENCHANTMENTS.add(key);
        return key;
    }

    public static boolean hasRebound(Level level, ItemStack stack) {
        try {
            HolderLookup.RegistryLookup<Enchantment> registry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            Optional<Holder.Reference<Enchantment>> enchantment = registry.get(EnderscapeEnchantments.REBOUND);
            return EnchantmentHelper.getItemEnchantmentLevel(enchantment.get(), stack) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}