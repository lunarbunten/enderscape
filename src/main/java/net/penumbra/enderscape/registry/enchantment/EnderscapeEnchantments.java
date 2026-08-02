package net.penumbra.enderscape.registry.enchantment;

import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
import net.minecraft.world.item.enchantment.effects.MultiplyValue;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.phys.Vec2;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.registry.entity.EnderscapeAttributes;
import net.penumbra.enderscape.registry.item.EnderscapeItems;
import net.penumbra.enderscape.registry.sound.EnderscapeItemSounds;
import net.penumbra.enderscape.registry.tag.EnderscapeItemTags;

import java.util.ArrayList;
import java.util.List;

public class EnderscapeEnchantments {

    public static final List<ResourceKey<Enchantment>> ENCHANTMENTS = new ArrayList<>();

    public static final ResourceKey<Enchantment> BUNDLING = register("bundling");
    public static final ResourceKey<Enchantment> STUN_BURST = register("stun_burst");
    public static final ResourceKey<Enchantment> REBOUND = register("rebound");
    public static final ResourceKey<Enchantment> RESONANCE = register("resonance");
    public static final ResourceKey<Enchantment> TRANSDIMENSIONAL = register("transdimensional");

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);

        register(context, BUNDLING,
                Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(EnderscapeItemTags.MAGNIA_ATTRACTOR_ENCHANTABLE),
                                4,
                                1,
                                Enchantment.constantCost(30),
                                Enchantment.constantCost(50),
                                8,
                                EquipmentSlotGroup.ANY
                        )
                ).withEffect(EnderscapeEnchantmentEffectComponents.MAGNET_ENABLE_DEPOSIT_INTO_BUNDLES)
        );

        register(context, STUN_BURST,
                Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(EnderscapeItemTags.DAGGER_ENCHANTABLE),
                                2,
                                2,
                                Enchantment.dynamicCost(1, 15),
                                Enchantment.dynamicCost(20, 40),
                                4,
                                EquipmentSlotGroup.HAND
                        )
                ).withSpecialEffect(
                        EnderscapeEnchantmentEffectComponents.STUN_ATTACK_EFFECT_DURATION,
                        new MultiplyValue(LevelBasedValue.perLevel(0.5F))
                ).withSpecialEffect(
                        EnderscapeEnchantmentEffectComponents.STUN_ATTACK_COOLDOWN_TIME,
                        new MultiplyValue(LevelBasedValue.perLevel(1.5F))
                ).withSpecialEffect(
                        EnderscapeEnchantmentEffectComponents.AREA_OF_EFFECT_STUN_ATTACK_RADIUS,
                        List.of(
                                new Vec2(4.0F, 2.0F),
                                new Vec2(8.0F, 4.0F)
                        )
                ).withSpecialEffect(
                        EnderscapeEnchantmentEffectComponents.STUN_ATTACK_SOUND,
                        List.of(EnderscapeItemSounds.DAGGER_STUN_BURST)
                )
        );

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
                ).withEffect(
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                Enderscape.id("enchantment.rebound"),
                                EnderscapeAttributes.BOUNCE_STRENGTH,
                                LevelBasedValue.perLevel(0.45F),
                                AttributeModifier.Operation.ADD_VALUE
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
                ).withEffect(EnderscapeEnchantmentEffectComponents.LODESTONE_TELEPORTATION_ENABLE_TRANSDIMENSIONAL)
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
                        EnderscapeEnchantmentEffectComponents.LODESTONE_TELEPORTATION_MAXIMUM_RANGE,
                        new AddValue(LevelBasedValue.perLevel(1250))
                ).withEffect(
                        EnderscapeEnchantmentEffectComponents.INTEGER_COUNTER_THRESHOLD,
                        new AddValue(LevelBasedValue.perLevel(100)),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().of(items, EnderscapeItems.MAGNIA_ATTRACTOR))
                ).withEffect(
                        EnderscapeEnchantmentEffectComponents.INTEGER_COUNTER_THRESHOLD,
                        new AddValue(LevelBasedValue.perLevel(2)),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().of(items, EnderscapeItems.DAGGER))
                )
        );
    }

    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.identifier()));
    }

    private static ResourceKey<Enchantment> register(String name) {
        ResourceKey<Enchantment> key = ResourceKey.create(Registries.ENCHANTMENT, Enderscape.id(name));
        ENCHANTMENTS.add(key);
        return key;
    }
}