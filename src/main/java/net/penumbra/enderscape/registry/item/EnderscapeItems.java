package net.penumbra.enderscape.registry.item;

import com.google.common.collect.Maps;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.*;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.item.HealingItem;
import net.penumbra.enderscape.item.RustleBucketItem;
import net.penumbra.enderscape.item.RustleSilkItem;
import net.penumbra.enderscape.item.component.*;
import net.penumbra.enderscape.item.component.value.FuelDisplay;
import net.penumbra.enderscape.item.component.value.FuelHud;
import net.penumbra.enderscape.item.component.value.FuelSounds;
import net.penumbra.enderscape.references.EnderscapeBlockItemIds;
import net.penumbra.enderscape.references.EnderscapeItemIds;
import net.penumbra.enderscape.registry.block.EnderscapeBannerPatterns;
import net.penumbra.enderscape.registry.block.EnderscapeBlocks;
import net.penumbra.enderscape.registry.block.EnderscapeFluids;
import net.penumbra.enderscape.registry.component.EnderscapeDataComponents;
import net.penumbra.enderscape.registry.entity.EnderscapeAttributes;
import net.penumbra.enderscape.registry.entity.EnderscapeEntities;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import net.penumbra.enderscape.registry.sound.EnderscapeItemSounds;
import net.penumbra.enderscape.registry.sound.EnderscapeJukeboxSongs;
import net.penumbra.enderscape.registry.tag.EnderscapeBannerPatternTags;
import net.penumbra.enderscape.registry.tag.EnderscapeItemTags;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static net.minecraft.world.item.Items.BUCKET;
import static net.minecraft.world.item.Items.BANNER;
import static net.penumbra.enderscape.registry.block.EnderscapeBlocks.END_VAULT;

public class EnderscapeItems {

    private static final ItemAttributeModifiers DRIFT_LEGGINGS_ATTRIBUTES = ItemAttributeModifiers.builder()
            .add(Attributes.ARMOR, new AttributeModifier(Enderscape.id("drift_leggings_armor"), 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.LEGS)
            .add(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(Enderscape.id("drift_leggings_armor_toughness"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.LEGS)
            .add(Attributes.MOVEMENT_SPEED, new AttributeModifier(Enderscape.id("drift_leggings_movement_speed"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.LEGS)
            .add(Attributes.SAFE_FALL_DISTANCE, new AttributeModifier(Enderscape.id("drift_leggings_safe_fall_distance"), 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.LEGS)
            .add(Attributes.GRAVITY, new AttributeModifier(Enderscape.id("drift_leggings_gravity"), -0.3, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.LEGS)
            .build();

    private static final Map<ArmorType, Integer> SHADOLINE_ARMOR_DEFENSE = Maps.newEnumMap(Map.of(ArmorType.BOOTS, 2, ArmorType.LEGGINGS, 5, ArmorType.CHESTPLATE, 6, ArmorType.HELMET, 2, ArmorType.BODY, 6));
    private static final Map<ArmorType, Double> SHADOLINE_STEALTH_BONUS = Maps.newEnumMap(Map.of(ArmorType.BOOTS, 0.15, ArmorType.LEGGINGS, 0.2, ArmorType.CHESTPLATE, 0.2, ArmorType.HELMET, 0.15, ArmorType.BODY, 0.4));

    private static final ArmorMaterial SHADOLINE_ARMOR_MATERIAL = new ArmorMaterial(25, SHADOLINE_ARMOR_DEFENSE, 15, EnderscapeItemSounds.SHADOLINE_ARMOR_EQUIP, 0, 0, EnderscapeItemTags.REPAIRS_SHADOLINE_ARMOR, EnderscapeEquipmentAssets.SHADOLINE);

    @NotNull
    private static Properties shadolineArmorProperties(ArmorType type) {
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        EquipmentSlotGroup group = EquipmentSlotGroup.bySlot(type.getSlot());
        Identifier location = Enderscape.id("armor." + type.getName());

        builder.add(Attributes.ARMOR, new AttributeModifier(location, SHADOLINE_ARMOR_MATERIAL.defense().getOrDefault(type, 0), AttributeModifier.Operation.ADD_VALUE), group);

        double stealth = SHADOLINE_STEALTH_BONUS.getOrDefault(type, 0.0);

        builder.add(EnderscapeAttributes.STEALTH, new AttributeModifier(location, stealth, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), group);

        // I don't think this works so I commented it out, lol
        // builder.add(Attributes.WAYPOINT_TRANSMIT_RANGE, new AttributeModifier(location, -stealth, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), group);

        ItemAttributeModifiers attributes = builder.build();

        return new Properties().humanoidArmor(SHADOLINE_ARMOR_MATERIAL, type).attributes(attributes);
    }

    public static final Item DRIFTER_SPAWN_EGG = registerSpawnEgg(EnderscapeItemIds.DRIFTER_SPAWN_EGG, EnderscapeEntities.DRIFTER);
    public static final Item RUBBLEMITE_SPAWN_EGG = registerSpawnEgg(EnderscapeItemIds.RUBBLEMITE_SPAWN_EGG, EnderscapeEntities.RUBBLEMITE);
    public static final Item RUSTLE_SPAWN_EGG = registerSpawnEgg(EnderscapeItemIds.RUSTLE_SPAWN_EGG, EnderscapeEntities.RUSTLE);

    public static final Item RUSTLE_BUCKET = registerItem(EnderscapeItemIds.RUSTLE_BUCKET, RustleBucketItem::new, new Item.Properties().stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY));
    public static final Item VOID_LACHRYMA_BUCKET = registerItem(EnderscapeItemIds.VOID_LACHRYMA_BUCKET, properties -> new BucketItem(EnderscapeFluids.VOID_LACHRYMA, properties), new Item.Properties().craftRemainder(BUCKET).stacksTo(1));

    public static final Item VEILED_HANGING_SIGN_ITEM = registerItem(EnderscapeBlockItemIds.VEILED_HANGING_SIGN, properties -> new HangingSignItem(EnderscapeBlocks.VEILED_HANGING_SIGN, EnderscapeBlocks.VEILED_WALL_HANGING_SIGN, properties), new Properties().stacksTo(16).useBlockDescriptionPrefix());
    public static final Item VEILED_SIGN_ITEM = registerItem(EnderscapeBlockItemIds.VEILED_SIGN, properties -> new SignItem(EnderscapeBlocks.VEILED_SIGN, EnderscapeBlocks.VEILED_WALL_SIGN, properties), new Properties().stacksTo(16).useBlockDescriptionPrefix());
    public static final Item CELESTIAL_HANGING_SIGN_ITEM = registerItem(EnderscapeBlockItemIds.CELESTIAL_HANGING_SIGN, properties -> new HangingSignItem(EnderscapeBlocks.CELESTIAL_HANGING_SIGN, EnderscapeBlocks.CELESTIAL_WALL_HANGING_SIGN, properties), new Properties().stacksTo(16).useBlockDescriptionPrefix());
    public static final Item CELESTIAL_SIGN_ITEM = registerItem(EnderscapeBlockItemIds.CELESTIAL_SIGN, properties -> new SignItem(EnderscapeBlocks.CELESTIAL_SIGN, EnderscapeBlocks.CELESTIAL_WALL_SIGN, properties), new Properties().stacksTo(16).useBlockDescriptionPrefix());
    public static final Item MURUBLIGHT_HANGING_SIGN_ITEM = registerItem(EnderscapeBlockItemIds.MURUBLIGHT_HANGING_SIGN, properties -> new HangingSignItem(EnderscapeBlocks.MURUBLIGHT_HANGING_SIGN, EnderscapeBlocks.MURUBLIGHT_WALL_HANGING_SIGN, properties), new Properties().stacksTo(16).useBlockDescriptionPrefix());
    public static final Item MURUBLIGHT_SIGN_ITEM = registerItem(EnderscapeBlockItemIds.MURUBLIGHT_SIGN, properties -> new SignItem(EnderscapeBlocks.MURUBLIGHT_SIGN, EnderscapeBlocks.MURUBLIGHT_WALL_SIGN, properties), new Properties().stacksTo(16).useBlockDescriptionPrefix());

    public static final Item VEILED_SHELF_ITEM = registerBlock(EnderscapeBlockItemIds.VEILED_SHELF, EnderscapeBlocks.VEILED_SHELF, new Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
    public static final Item CELESTIAL_SHELF_ITEM = registerBlock(EnderscapeBlockItemIds.CELESTIAL_SHELF, EnderscapeBlocks.CELESTIAL_SHELF, new Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
    public static final Item MURUBLIGHT_SHELF_ITEM = registerBlock(EnderscapeBlockItemIds.MURUBLIGHT_SHELF, EnderscapeBlocks.MURUBLIGHT_SHELF, new Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));

    public static final Item CHORUS_CAKE_ROLL_ITEM = registerItem(EnderscapeItemIds.CHORUS_CAKE_ROLL, properties -> new BlockItem(EnderscapeBlocks.CHORUS_CAKE_ROLL, properties), new Properties().stacksTo(1).useBlockDescriptionPrefix());

    public static final Item BLINKLIGHT = registerItem(EnderscapeItemIds.BLINKLIGHT, properties -> new BlockItem(EnderscapeBlocks.BLINKLIGHT_VINES_HEAD, properties), new Properties().useItemDescriptionPrefix());

    public static final Item DRIFT_JELLY_BOTTLE = registerItem(
            EnderscapeItemIds.DRIFT_JELLY_BOTTLE,
            new Properties()
                    .craftRemainder(Items.GLASS_BOTTLE)
                    .food(
                            new FoodProperties.Builder()
                                    .alwaysEdible()
                                    .nutrition(6)
                                    .saturationModifier(0.2F)
                                    .build(),
                            Consumables.defaultDrink()
                                    .sound(EnderscapeItemSounds.DRIFT_JELLY_BOTTLE_DRINK)
                                    .onConsume(
                                            new ApplyStatusEffectsConsumeEffect(
                                                    new MobEffectInstance(
                                                            EnderscapeMobEffects.LOW_GRAVITY,
                                                            20 * 20
                                                    )
                                            )
                                    ).build()
                    )
                    .stacksTo(16)
                    .usingConvertsTo(Items.GLASS_BOTTLE)
    );

    public static final Item PURUBERRY = registerItem(
            EnderscapeItemIds.PURUBERRY,
            properties -> new BlockItem(EnderscapeBlocks.PURUBERRY_VINE, properties),
            new Properties()
                    .useCooldown(3.0F)
                    .useItemDescriptionPrefix()
                    .food(
                            new FoodProperties.Builder()
                                    .nutrition(6)
                                    .saturationModifier(1.0F)
                                    .alwaysEdible()
                                    .build(),
                            Consumable.builder()
                                    .onConsume(
                                            new ApplyStatusEffectsConsumeEffect(
                                                    new MobEffectInstance(
                                                            EnderscapeMobEffects.VOID_PURIFICATION,
                                                            15 * 20
                                                    )
                                            )
                                    ).build()
                )
    );

    public static final Item MURUBLIGHT_BRACKET_ITEM = registerItem(
            EnderscapeItemIds.MURUBLIGHT_BRACKET,
            properties -> new BlockItem(EnderscapeBlocks.MURUBLIGHT_BRACKET, properties),
            new Properties()
                    .useBlockDescriptionPrefix()
                    .food(
                            new FoodProperties.Builder()
                                    .alwaysEdible()
                                    .nutrition(4)
                                    .saturationModifier(0.3F)
                                    .build(),
                            Consumable.builder()
                                    .onConsume(
                                            new ApplyStatusEffectsConsumeEffect(
                                                    new MobEffectInstance(
                                                            EnderscapeMobEffects.VOID_CORRUPTION,
                                                            12 * 20
                                                    )
                                            )
                                    ).build()
                    )
    );

    public static final Item VOID_TORCH_ITEM = registerItem(EnderscapeItemIds.VOID_TORCH, properties -> new StandingAndWallBlockItem(EnderscapeBlocks.VOID_TORCH, EnderscapeBlocks.VOID_WALL_TORCH, Direction.DOWN, properties), new Properties().useBlockDescriptionPrefix());

    public static final Item END_CITY_KEY = registerItem(EnderscapeItemIds.END_CITY_KEY);
    public static final Item RUBBLE_CHITIN = registerItem(EnderscapeItemIds.RUBBLE_CHITIN);
    public static final Item RUSTLE_SILK = registerItem(EnderscapeItemIds.RUSTLE_SILK, RustleSilkItem::new, new Properties());
    public static final Item NEBULITE = registerItem(EnderscapeItemIds.NEBULITE, new Properties().trimMaterial(EnderscapeTrimMaterials.NEBULITE));
    public static final Item NEBULITE_SHARDS = registerItem(EnderscapeItemIds.NEBULITE_SHARDS);
    public static final Item RAW_SHADOLINE = registerItem(EnderscapeItemIds.RAW_SHADOLINE);
    public static final Item SHADOLINE_INGOT = registerItem(EnderscapeItemIds.SHADOLINE_INGOT, new Properties().trimMaterial(EnderscapeTrimMaterials.SHADOLINE));
    public static final Item SHADOLINE_NUGGET = registerItem(EnderscapeItemIds.SHADOLINE_NUGGET);

    public static final Item RUBBLE_SHIELD = registerItem(EnderscapeItemIds.RUBBLE_SHIELD, ShieldItem::new, new Properties()
            .delayedComponent(DataComponents.BLOCKS_ATTACKS, context -> new BlocksAttacks(
                    0.25F,
                    1.0F,
                    List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                    new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
                    Optional.of(context.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
                    Optional.of(EnderscapeItemSounds.RUBBLE_SHIELD_BLOCK),
                    Optional.of(SoundEvents.SHIELD_BREAK))
            )
            .component(EnderscapeDataComponents.DASH_JUMP, DashJump.DEFAULT)
            .component(EnderscapeDataComponents.RUBBLE_SHIELD_VARIANT, RubbleShieldVariant.END_STONE)
            .durability(336)
            .equippableUnswappable(EquipmentSlot.OFFHAND)
            .repairable(EnderscapeItemTags.REPAIRS_RUBBLE_SHIELDS)
            .useCooldown(3)
    );

    public static ItemStack rubbleShieldVariant(Identifier identifier) {
        return new ItemStackTemplate(RUBBLE_SHIELD, DataComponentPatch.builder().set(EnderscapeDataComponents.RUBBLE_SHIELD_VARIANT, identifier).build()).create();
    }

    public static final Item DRIFT_LEGGINGS = registerItem(EnderscapeItemIds.DRIFT_LEGGINGS, new Properties()
            .attributes(DRIFT_LEGGINGS_ATTRIBUTES)
            .component(DataComponents.EQUIPPABLE, Equippable.builder(ArmorType.LEGGINGS.getSlot()).setEquipSound(EnderscapeItemSounds.DRIFT_LEGGINGS_EQUIP).setAsset(EnderscapeEquipmentAssets.DRIFT_LEGGINGS).build())
            .durability(495)
            .enchantable(20)
            .repairable(EnderscapeItemTags.REPAIRS_DRIFT_LEGGINGS));

    public static final Item SHADOLINE_HELMET = registerItem(EnderscapeItemIds.SHADOLINE_HELMET, shadolineArmorProperties(ArmorType.HELMET));
    public static final Item SHADOLINE_CHESTPLATE = registerItem(EnderscapeItemIds.SHADOLINE_CHESTPLATE, shadolineArmorProperties(ArmorType.CHESTPLATE));
    public static final Item SHADOLINE_LEGGINGS = registerItem(EnderscapeItemIds.SHADOLINE_LEGGINGS, shadolineArmorProperties(ArmorType.LEGGINGS));
    public static final Item SHADOLINE_BOOTS = registerItem(EnderscapeItemIds.SHADOLINE_BOOTS, shadolineArmorProperties(ArmorType.BOOTS));

    public static final Item DAGGER = registerItem(EnderscapeItemIds.DAGGER, new Properties()
            .stacksTo(1)
            .enchantable(15)
            .attributes(ItemAttributeModifiers.builder()
                    .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 3.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(EnderscapeAttributes.BACKSTAB_DAMAGE, new AttributeModifier(EnderscapeAttributes.BASE_BACKSTAB_DAMAGE_ID, 3.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -1.5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
            .component(DataComponents.MINIMUM_ATTACK_CHARGE, 1.0F)
            .component(DataComponents.WEAPON, new Weapon(1))
            .component(EnderscapeDataComponents.ATTACK_SOUNDS, AttackSounds.Builder.create()
                    .strong(EnderscapeItemSounds.DAGGER_ATTACK_STRONG)
                    .crit(EnderscapeItemSounds.DAGGER_ATTACK_CRIT)
                    .knockback(EnderscapeItemSounds.DAGGER_ATTACK_KNOCKBACK)
                    .backstab(EnderscapeItemSounds.DAGGER_BACKSTAB)
                    .build()
            )
            .component(
                    DataComponents.TOOL,
                    new Tool(
                            List.of(
                                    Tool.Rule.minesAndDrops(HolderSet.direct(Blocks.COBWEB.builtInRegistryHolder()), 7.5F)
                            ),
                            1,
                            0,
                            false
                    )
            )
            .component(EnderscapeDataComponents.THRESHOLD_COUNTER, ThresholdCounter.of(4))
            .component(EnderscapeDataComponents.BACKSTAB_ANGLE, EnderscapeAttributes.DEFAULT_BACKSTAB_ANGLE)
            .component(
                    EnderscapeDataComponents.FUELED_TOOL,
                    FueledTool.Builder.create(5).fuelDisplay(
                            FuelDisplay.Builder.create().hud(FuelHud.HIDDEN)
                    ).build()
            )
            .component(EnderscapeDataComponents.STUN_ATTACK, StunAttack.DEFAULT)
    );

    public static final Item MAGNIA_ATTRACTOR = registerItem(EnderscapeItemIds.MAGNIA_ATTRACTOR, new Properties()
            .stacksTo(1)
            .component(EnderscapeDataComponents.ENABLED, true)
            .component(EnderscapeDataComponents.ENTITY_MAGNET, EntityMagnet.DEFAULT)
            .component(
                    EnderscapeDataComponents.FUELED_TOOL,
                    FueledTool.Builder.create(6)
                            .fuelDisplay(
                                    FuelDisplay.Builder.create().hud(FuelHud.HIDDEN)
                            ).fuelSounds(
                                    FuelSounds.Builder.create().useFuel(EnderscapeItemSounds.MAGNIA_ATTRACTOR_USE_FUEL)
                            ).build()
            )
            .component(EnderscapeDataComponents.THRESHOLD_COUNTER, ThresholdCounter.of(200))
            .component(EnderscapeDataComponents.TOGGLABLE, Togglable.MAGNIA_ATTRACTOR)
            .enchantable(1)
    );

    public static final Item MIRROR = registerItem(EnderscapeItemIds.MIRROR, new Properties()
            .stacksTo(1)
            .component(EnderscapeDataComponents.LODESTONE_TELEPORTATION, LodestoneTeleportation.DEFAULT)
            .component(EnderscapeDataComponents.FUELED_TOOL, FueledTool.simple(5))
            .enchantable(1)
            .rarity(Rarity.RARE)
    );

    public static final Item CRESCENT_BANNER_PATTERN = registerItem(EnderscapeItemIds.CRESCENT_BANNER_PATTERN, new Item.Properties().stacksTo(1).rarity(Rarity.RARE).delayedComponent(DataComponents.PROVIDES_BANNER_PATTERNS, context -> context.getOrThrow(EnderscapeBannerPatternTags.PATTERN_ITEM_CRESCENT)));

    public static final Item STASIS_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem(EnderscapeItemIds.STASIS_ARMOR_TRIM_SMITHING_TEMPLATE, SmithingTemplateItem::createArmorTrimTemplate, new Properties().rarity(Rarity.EPIC));

    public static final Item MUSIC_DISC_GLARE = registerMusicDisc(EnderscapeItemIds.MUSIC_DISC_GLARE, EnderscapeJukeboxSongs.GLARE, Rarity.RARE);
    public static final Item MUSIC_DISC_BLISS = registerMusicDisc(EnderscapeItemIds.MUSIC_DISC_BLISS, EnderscapeJukeboxSongs.BLISS, Rarity.RARE);
    public static final Item MUSIC_DISC_DECAY = registerMusicDisc(EnderscapeItemIds.MUSIC_DISC_DECAY, EnderscapeJukeboxSongs.DECAY, Rarity.UNCOMMON);

    public static final Item HEALING = registerItem(EnderscapeItemIds.HEALING, HealingItem::new, new Properties().useCooldown(0.25F));

    private static <T extends Mob> Item registerSpawnEgg(ResourceKey<Item> key, EntityType<T> type) {
        return registerItem(key, SpawnEggItem::new, new Item.Properties().spawnEgg(type));
    }

    private static Item registerMusicDisc(ResourceKey<Item> id, ResourceKey<JukeboxSong> song, Rarity rarity) {
        return registerItem(id, new Properties().stacksTo(1).rarity(rarity).jukeboxPlayable(song));
    }

    private static ResourceKey<Item> createResourceKey(String name) {
        return ResourceKey.create(Registries.ITEM, Enderscape.id(name));
    }

    private static ResourceKey<Item> blockIdToItemId(ResourceKey<Block> resourceKey) {
        return ResourceKey.create(Registries.ITEM, resourceKey.identifier());
    }

    public static Item registerBlock(BlockItemId key, Block block) {
        return registerBlock(key.item(), block, BlockItem::new, new Item.Properties());
    }

    public static Item registerBlock(BlockItemId key, Block block, Item.Properties properties) {
        return registerBlock(key, block, BlockItem::new, properties);
    }

    public static Item registerBlock(BlockItemId key, Block block, BiFunction<Block, Item.Properties, Item> biFunction, Item.Properties properties) {
        return registerItem(key, propertiesx -> biFunction.apply(block, propertiesx), properties.useBlockDescriptionPrefix());
    }

    public static Item registerItem(BlockItemId resourceKey, Item.Properties properties) {
        return registerItem(resourceKey, Item::new, properties);
    }

    public static Item registerItem(BlockItemId resourceKey) {
        return registerItem(resourceKey, Item::new, new Item.Properties());
    }

    public static Item registerItem(BlockItemId resourceKey, Function<Item.Properties, Item> function, Item.Properties properties) {
        return registerItem(resourceKey.item(), function, properties);
    }

    public static Item registerBlock(ResourceKey<Item> key, Block block) {
        return registerBlock(key, block, BlockItem::new, new Item.Properties());
    }

    public static Item registerBlock(ResourceKey<Item> key, Block block, Item.Properties properties) {
        return registerBlock(key, block, BlockItem::new, properties);
    }

    public static Item registerBlock(ResourceKey<Item> key, Block block, BiFunction<Block, Item.Properties, Item> biFunction, Item.Properties properties) {
        return registerItem(key, propertiesx -> biFunction.apply(block, propertiesx), properties.useBlockDescriptionPrefix());
    }

    public static Item registerItem(ResourceKey<Item> resourceKey, Item.Properties properties) {
        return registerItem(resourceKey, Item::new, properties);
    }

    public static Item registerItem(ResourceKey<Item> resourceKey) {
        return registerItem(resourceKey, Item::new, new Item.Properties());
    }

    public static Item registerItem(ResourceKey<Item> resourceKey, Function<Item.Properties, Item> function, Item.Properties properties) {
        Item item = function.apply(properties.setId(resourceKey));
        if (item instanceof BlockItem blockItem) {
            blockItem.registerBlocks(Item.BY_BLOCK, item);
        }

        return Registry.register(BuiltInRegistries.ITEM, resourceKey, item);
    }

    public static ItemStack getEndVaultInstance() {
        ItemStack stack = new ItemStack(END_VAULT);

        CompoundTag keyItem = new CompoundTag();

        keyItem.putInt("count", 1);
        keyItem.putString("id", "enderscape:end_city_key");

        CompoundTag configTag = new CompoundTag();

        configTag.put("key_item", keyItem);
        configTag.putString("loot_table", "enderscape:end_city/vault");

        CompoundTag blockEntityData = new CompoundTag();

        blockEntityData.putString("id", "minecraft:vault");
        blockEntityData.put("config", configTag);

        stack.set(DataComponents.BLOCK_ENTITY_DATA, TypedEntityData.of(BlockEntityTypes.VAULT, blockEntityData));

        return stack;
    }

    public static ItemStack getEndCityBannerInstance(final HolderGetter<BannerPattern> getter) {
        return getEndCityBannerTemplate(getter).create();
    }

    public static ItemStackTemplate getEndCityBannerTemplate(final HolderGetter<BannerPattern> getter) {
        return new ItemStackTemplate(BANNER.magenta(), getEndCityBannerComponentPatch(getter));
    }

    public static DataComponentPatch getEndCityBannerComponentPatch(final HolderGetter<BannerPattern> getter) {
        DataComponentPatch.Builder builder = DataComponentPatch.builder();

        BannerPatternLayers patterns = new BannerPatternLayers.Builder()
                .addIfRegistered(getter, BannerPatterns.STRIPE_SMALL, DyeColor.BLACK)
                .addIfRegistered(getter, BannerPatterns.STRIPE_TOP, DyeColor.MAGENTA)
                .addIfRegistered(getter, BannerPatterns.STRIPE_BOTTOM, DyeColor.MAGENTA)
                .addIfRegistered(getter, BannerPatterns.STRIPE_MIDDLE, DyeColor.MAGENTA)
                .addIfRegistered(getter, BannerPatterns.FLOWER, DyeColor.MAGENTA)
                .addIfRegistered(getter, BannerPatterns.RHOMBUS_MIDDLE, DyeColor.MAGENTA)
                .addIfRegistered(getter, BannerPatterns.TRIANGLE_BOTTOM, DyeColor.BLACK)
                .addIfRegistered(getter, BannerPatterns.TRIANGLE_TOP, DyeColor.BLACK)
                .addIfRegistered(getter, BannerPatterns.CIRCLE_MIDDLE, DyeColor.BLACK)
                .addIfRegistered(getter, EnderscapeBannerPatterns.CRESCENT, DyeColor.MAGENTA)
                .build();

        builder.set(DataComponents.BANNER_PATTERNS, patterns);
        builder.set(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT.withHidden(DataComponents.BANNER_PATTERNS, true));
        builder.set(DataComponents.ITEM_NAME, Component.translatable("block.enderscape.end_city_banner"));
        builder.set(DataComponents.RARITY, Rarity.UNCOMMON);

        return builder.build();
    }
}