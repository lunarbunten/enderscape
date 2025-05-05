package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.registry.tag.EnderscapeItemTags;
import net.minecraft.core.HolderGetter;
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
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class EnderscapeEnchantments {

    public static final List<ResourceKey<Enchantment>> ENCHANTMENTS = new ArrayList<>();

    public static final ResourceKey<Enchantment> BUNDLING = register("bundling");
    public static final ResourceKey<Enchantment> LIGHTSPEED = register("lightspeed");
    public static final ResourceKey<Enchantment> REBOUND = register("rebound");
    public static final ResourceKey<Enchantment> TRANSDIMENSIONAL = register("transdimensional");

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);

        register(context, BUNDLING,
                Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(EnderscapeItemTags.MAGNIA_ATTRACTOR_ENCHANTABLE), 1, 1, Enchantment.constantCost(20), Enchantment.constantCost(50), 8, EquipmentSlotGroup.ANY
                        )
                )
        );

        register(context, LIGHTSPEED,
                Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(EnderscapeItemTags.MIRROR_ENCHANTABLE), 1, 3, Enchantment.constantCost(20), Enchantment.constantCost(50), 8, EquipmentSlotGroup.HAND
                        )
                ).withSpecialEffect(EnderscapeEnchantmentEffectComponents.MIRROR_DISTANCE_FOR_COST_INCREASE, new AddValue(LevelBasedValue.perLevel(250)))
        );

        register(context, REBOUND,
                Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(EnderscapeItemTags.ELYTRA_ENCHANTABLE), 1, 1, Enchantment.constantCost(20), Enchantment.constantCost(50), 8, EquipmentSlotGroup.CHEST
                        )
                )
        );

        register(context, TRANSDIMENSIONAL,
                Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(EnderscapeItemTags.MIRROR_ENCHANTABLE), 1, 1, Enchantment.constantCost(20), Enchantment.constantCost(50), 8, EquipmentSlotGroup.HAND
                        )
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
            var registry = level.registryAccess().lookup(Registries.ENCHANTMENT).orElse(null);
            if (registry == null) return false;

            var enchantment = registry.getValue(EnderscapeEnchantments.REBOUND);
            if (enchantment == null) return false;

            var holder = registry.wrapAsHolder(enchantment);
            return EnchantmentHelper.getItemEnchantmentLevel(holder, stack) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}