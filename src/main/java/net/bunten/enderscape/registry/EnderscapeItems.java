package net.bunten.enderscape.registry;

import com.google.common.collect.Maps;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.item.CrackedMirrorItem;
import net.bunten.enderscape.item.HealingItem;
import net.bunten.enderscape.item.RustleBucketItem;
import net.bunten.enderscape.item.component.*;
import net.bunten.enderscape.item.component.value.FuelDisplay;
import net.bunten.enderscape.item.component.value.FuelHud;
import net.bunten.enderscape.item.component.value.FuelSounds;
import net.bunten.enderscape.registry.tag.EnderscapeBannerPatternTags;
import net.bunten.enderscape.registry.tag.EnderscapeItemTags;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static net.bunten.enderscape.registry.EnderscapeBlocks.END_VAULT;
import static net.minecraft.world.item.Items.MAGENTA_BANNER;
import static net.minecraft.world.item.equipment.EquipmentAssets.ROOT_ID;

public class EnderscapeItems {

    private static final ItemAttributeModifiers DRIFT_LEGGINGS_ATTRIBUTES = ItemAttributeModifiers.builder()
            .add(Attributes.ARMOR, new AttributeModifier(Enderscape.id("drift_leggings_armor"), 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.LEGS)
            .add(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(Enderscape.id("drift_leggings_armor_toughness"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.LEGS)
            .add(Attributes.MOVEMENT_SPEED, new AttributeModifier(Enderscape.id("drift_leggings_movement_speed"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.LEGS)
            .add(Attributes.SAFE_FALL_DISTANCE, new AttributeModifier(Enderscape.id("drift_leggings_safe_fall_distance"), 0.3, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.LEGS)
            .add(Attributes.GRAVITY, new AttributeModifier(Enderscape.id("drift_leggings_gravity"), -0.3, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.LEGS)
            .build();

    public static final ResourceKey<EquipmentAsset> SHADOLINE_ARMOR_ASSET = ResourceKey.create(ROOT_ID, Enderscape.id("shadoline"));
    public static final ResourceKey<EquipmentAsset> DRIFT_LEGGINGS_ASSET = ResourceKey.create(ROOT_ID, Enderscape.id("drift_leggings"));

    private static final Map<ArmorType, Integer> SHADOLINE_ARMOR_DEFENSE = Maps.newEnumMap(Map.of(ArmorType.BOOTS, 2, ArmorType.LEGGINGS, 5, ArmorType.CHESTPLATE, 6, ArmorType.HELMET, 2, ArmorType.BODY, 6));
    private static final Map<ArmorType, Double> SHADOLINE_STEALTH_BONUS = Maps.newEnumMap(Map.of(ArmorType.BOOTS, 0.15, ArmorType.LEGGINGS, 0.2, ArmorType.CHESTPLATE, 0.2, ArmorType.HELMET, 0.15, ArmorType.BODY, 0.4));

    private static final ArmorMaterial SHADOLINE_ARMOR_MATERIAL = new ArmorMaterial(25, SHADOLINE_ARMOR_DEFENSE, 15, EnderscapeItemSounds.SHADOLINE_ARMOR_EQUIP, 0, 0, EnderscapeItemTags.REPAIRS_SHADOLINE_ARMOR, SHADOLINE_ARMOR_ASSET);

    @NotNull
    private static Properties shadolineArmorProperties(ArmorType type) {
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        EquipmentSlotGroup group = EquipmentSlotGroup.bySlot(type.getSlot());
        ResourceLocation location = Enderscape.id("armor." + type.getName());

        builder.add(Attributes.ARMOR, new AttributeModifier(location, SHADOLINE_ARMOR_MATERIAL.defense().getOrDefault(type, 0), AttributeModifier.Operation.ADD_VALUE), group);

        double stealth = SHADOLINE_STEALTH_BONUS.getOrDefault(type, 0.0);

        builder.add(EnderscapeAttributes.STEALTH, new AttributeModifier(location, stealth, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), group);

        // I don't think this works so I commented it out, lol
        // builder.add(Attributes.WAYPOINT_TRANSMIT_RANGE, new AttributeModifier(location, -stealth, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), group);

        ItemAttributeModifiers attributes = builder.build();

        return new Properties().humanoidArmor(SHADOLINE_ARMOR_MATERIAL, type).attributes(attributes);
    }

    public static final Item DRIFTER_SPAWN_EGG = registerSpawnEgg(EnderscapeEntities.DRIFTER);
    public static final Item RUBBLEMITE_SPAWN_EGG = registerSpawnEgg(EnderscapeEntities.RUBBLEMITE);
    public static final Item RUSTLE_SPAWN_EGG = registerSpawnEgg(EnderscapeEntities.RUSTLE);

    public static final Item RUSTLE_BUCKET = registerItem("rustle_bucket", RustleBucketItem::new, new Item.Properties().stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY));

    public static final Item VEILED_HANGING_SIGN_ITEM = registerItem("veiled_hanging_sign", properties -> new HangingSignItem(EnderscapeBlocks.VEILED_HANGING_SIGN, EnderscapeBlocks.VEILED_WALL_HANGING_SIGN, properties), new Properties().stacksTo(16).useBlockDescriptionPrefix());
    public static final Item VEILED_SIGN_ITEM = registerItem("veiled_sign", properties -> new SignItem(EnderscapeBlocks.VEILED_SIGN, EnderscapeBlocks.VEILED_WALL_SIGN, properties), new Properties().stacksTo(16).useBlockDescriptionPrefix());
    public static final Item CELESTIAL_HANGING_SIGN_ITEM = registerItem("celestial_hanging_sign", properties -> new HangingSignItem(EnderscapeBlocks.CELESTIAL_HANGING_SIGN, EnderscapeBlocks.CELESTIAL_WALL_HANGING_SIGN, properties), new Properties().stacksTo(16).useBlockDescriptionPrefix());
    public static final Item CELESTIAL_SIGN_ITEM = registerItem("celestial_sign", properties -> new SignItem(EnderscapeBlocks.CELESTIAL_SIGN, EnderscapeBlocks.CELESTIAL_WALL_SIGN, properties), new Properties().stacksTo(16).useBlockDescriptionPrefix());
    public static final Item MURUBLIGHT_HANGING_SIGN_ITEM = registerItem("murublight_hanging_sign", properties -> new HangingSignItem(EnderscapeBlocks.MURUBLIGHT_HANGING_SIGN, EnderscapeBlocks.MURUBLIGHT_WALL_HANGING_SIGN, properties), new Properties().stacksTo(16).useBlockDescriptionPrefix());
    public static final Item MURUBLIGHT_SIGN_ITEM = registerItem("murublight_sign", properties -> new SignItem(EnderscapeBlocks.MURUBLIGHT_SIGN, EnderscapeBlocks.MURUBLIGHT_WALL_SIGN, properties), new Properties().stacksTo(16).useBlockDescriptionPrefix());

    public static final Item VEILED_SHELF_ITEM = registerBlock(EnderscapeBlocks.VEILED_SHELF, new Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
    public static final Item CELESTIAL_SHELF_ITEM = registerBlock(EnderscapeBlocks.CELESTIAL_SHELF, new Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));
    public static final Item MURUBLIGHT_SHELF_ITEM = registerBlock(EnderscapeBlocks.MURUBLIGHT_SHELF, new Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));

    public static final Item CHORUS_CAKE_ROLL_ITEM = registerItem("chorus_cake_roll", properties -> new BlockItem(EnderscapeBlocks.CHORUS_CAKE_ROLL, properties), new Properties().stacksTo(1).useBlockDescriptionPrefix());

    public static final Item BLINKLIGHT = registerItem("blinklight", properties -> new BlockItem(EnderscapeBlocks.BLINKLIGHT_VINES_HEAD, properties), new Properties().useItemDescriptionPrefix());
    public static final Item DRIFT_JELLY_BOTTLE = registerItem("drift_jelly_bottle", new Properties()
            .craftRemainder(Items.GLASS_BOTTLE)
            .food(
                    new FoodProperties.Builder().alwaysEdible().nutrition(6).saturationModifier(0.2F).build(),
                    Consumables.defaultDrink()
                            .sound(EnderscapeItemSounds.DRIFT_JELLY_BOTTLE_DRINK)
                            .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(EnderscapeMobEffects.LOW_GRAVITY, 20 * 20), 1.0F)).build()
            )
            .stacksTo(16)
            .usingConvertsTo(Items.GLASS_BOTTLE)
    );
    public static final Item FLANGER_BERRY = registerItem("flanger_berry", properties -> new BlockItem(EnderscapeBlocks.FLANGER_BERRY_VINE, properties), new Properties().useItemDescriptionPrefix().food(new FoodProperties.Builder().nutrition(5).saturationModifier(1.2F).build()));
    public static final Item MURUBLIGHT_BRACKET_ITEM = registerItem("murublight_bracket", properties -> new BlockItem(EnderscapeBlocks.MURUBLIGHT_BRACKET, properties), new Properties().useBlockDescriptionPrefix().food(new FoodProperties.Builder().alwaysEdible().nutrition(4).saturationModifier(0.3F).build(), Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.POISON, 200), 1.0F)).build()));

    public static final Item VOID_TORCH_ITEM = registerItem("void_torch", properties -> new StandingAndWallBlockItem(EnderscapeBlocks.VOID_TORCH, EnderscapeBlocks.VOID_WALL_TORCH, Direction.DOWN, properties), new Properties().useBlockDescriptionPrefix());

    public static final Item END_CITY_KEY = registerItem("end_city_key");
    public static final Item RUBBLE_CHITIN = registerItem("rubble_chitin");
    public static final Item NEBULITE = registerItem("nebulite", new Properties().trimMaterial(EnderscapeTrimMaterials.NEBULITE));
    public static final Item NEBULITE_SHARDS = registerItem("nebulite_shards");
    public static final Item RAW_SHADOLINE = registerItem("raw_shadoline");
    public static final Item SHADOLINE_INGOT = registerItem("shadoline_ingot", new Properties().trimMaterial(EnderscapeTrimMaterials.SHADOLINE));
    public static final Item SHADOLINE_NUGGET = registerItem("shadoline_nugget");

    public static final Properties RUBBLE_SHIELD_PROPERTIES = new Properties()
            .component(DataComponents.BLOCKS_ATTACKS, new BlocksAttacks(
                    0.25F,
                    1.0F,
                    List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                    new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
                    Optional.of(DamageTypeTags.BYPASSES_SHIELD),
                    Optional.of(EnderscapeItemSounds.RUBBLE_SHIELD_BLOCK),
                    Optional.of(SoundEvents.SHIELD_BREAK)
            ))
            .component(DataComponents.USE_COOLDOWN, new UseCooldown(
                    3,
                    Optional.of(Enderscape.id("rubble_shield")))
            )
            .component(EnderscapeDataComponents.DASH_JUMP, DashJump.DEFAULT)
            .durability(336)
            .equippableUnswappable(EquipmentSlot.OFFHAND)
            .repairable(EnderscapeItemTags.REPAIRS_RUBBLE_SHIELDS);

    public static final Item END_STONE_RUBBLE_SHIELD = registerItem("end_stone_rubble_shield", ShieldItem::new, RUBBLE_SHIELD_PROPERTIES);
    public static final Item VERADITE_RUBBLE_SHIELD = registerItem("veradite_rubble_shield", ShieldItem::new, RUBBLE_SHIELD_PROPERTIES);
    public static final Item MIRESTONE_RUBBLE_SHIELD = registerItem("mirestone_rubble_shield", ShieldItem::new, RUBBLE_SHIELD_PROPERTIES);
    public static final Item KURODITE_RUBBLE_SHIELD = registerItem("kurodite_rubble_shield", ShieldItem::new, RUBBLE_SHIELD_PROPERTIES);

    public static final Item DRIFT_LEGGINGS = registerItem("drift_leggings", new Properties()
            .attributes(DRIFT_LEGGINGS_ATTRIBUTES)
            .component(DataComponents.EQUIPPABLE, Equippable.builder(ArmorType.LEGGINGS.getSlot()).setEquipSound(EnderscapeItemSounds.DRIFT_LEGGINGS_EQUIP).setAsset(DRIFT_LEGGINGS_ASSET).build())
            .durability(495)
            .enchantable(20)
            .repairable(EnderscapeItemTags.REPAIRS_DRIFT_LEGGINGS));

    public static final Item SHADOLINE_HELMET = registerItem("shadoline_helmet", shadolineArmorProperties(ArmorType.HELMET));
    public static final Item SHADOLINE_CHESTPLATE = registerItem("shadoline_chestplate", shadolineArmorProperties(ArmorType.CHESTPLATE));
    public static final Item SHADOLINE_LEGGINGS = registerItem("shadoline_leggings", shadolineArmorProperties(ArmorType.LEGGINGS));
    public static final Item SHADOLINE_BOOTS = registerItem("shadoline_boots", shadolineArmorProperties(ArmorType.BOOTS));

    public static final Item DAGGER = registerItem("dagger", new Properties()
            .stacksTo(1)
            .enchantable(1)
            .attributes(ItemAttributeModifiers.builder()
                    .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 3.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(EnderscapeAttributes.BACKSTAB_DAMAGE, new AttributeModifier(EnderscapeAttributes.BASE_BACKSTAB_DAMAGE_ID, 3.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -1.5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
            .component(DataComponents.USE_COOLDOWN, new UseCooldown(7))
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
                            2,
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

    public static final Item MAGNIA_ATTRACTOR = registerItem("magnia_attractor", new Properties()
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

    public static final Item CRACKED_MIRROR = registerItem("cracked_mirror", CrackedMirrorItem::new, new Properties()
            .component(DataComponents.USE_COOLDOWN, new UseCooldown(1))
            .rarity(Rarity.RARE)
            .stacksTo(1)
    );

    public static final Item MIRROR = registerItem("mirror", new Properties()
            .stacksTo(1)
            .component(EnderscapeDataComponents.LODESTONE_TELEPORTATION, LodestoneTeleportation.DEFAULT)
            .component(EnderscapeDataComponents.FUELED_TOOL, FueledTool.simple(5))
            .enchantable(1)
            .rarity(Rarity.RARE)
    );

    public static final Item CRESCENT_BANNER_PATTERN = registerItem("crescent_banner_pattern", new Item.Properties().stacksTo(1).rarity(Rarity.RARE).component(DataComponents.PROVIDES_BANNER_PATTERNS, EnderscapeBannerPatternTags.PATTERN_ITEM_CRESCENT));

    public static final Item STASIS_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("stasis_armor_trim_smithing_template", SmithingTemplateItem::createArmorTrimTemplate, new Properties().rarity(Rarity.EPIC));

    public static final Item MUSIC_DISC_GLARE = registerMusicDisc(EnderscapeJukeboxSongs.GLARE, Rarity.RARE);
    public static final Item MUSIC_DISC_BLISS = registerMusicDisc(EnderscapeJukeboxSongs.BLISS, Rarity.RARE);
    public static final Item MUSIC_DISC_DECAY = registerMusicDisc(EnderscapeJukeboxSongs.DECAY, Rarity.UNCOMMON);

    public static final Item HEALING = registerItem("healing", HealingItem::new, new Properties().component(DataComponents.USE_COOLDOWN, new UseCooldown(0.25F)));

    private static <T extends Mob> Item registerSpawnEgg(EntityType<T> type) {
        String name = type + "_spawn_egg";
        name = name.replace("entity.enderscape.", "");
        return registerItem(name, SpawnEggItem::new, new Item.Properties().spawnEgg(type));
    }

    private static Item registerMusicDisc(ResourceKey<JukeboxSong> song, Rarity rarity) {
        return registerItem("music_disc_" + song.location().getPath(), new Properties().stacksTo(1).rarity(rarity).jukeboxPlayable(song));
    }

    private static ResourceKey<Item> createResourceKey(String name) {
        return ResourceKey.create(Registries.ITEM, Enderscape.id(name));
    }

    private static ResourceKey<Item> blockIdToItemId(ResourceKey<Block> resourceKey) {
        return ResourceKey.create(Registries.ITEM, resourceKey.location());
    }

    public static Item registerBlock(Block block) {
        return registerBlock(block, BlockItem::new, new Item.Properties());
    }

    public static Item registerBlock(Block block, Item.Properties properties) {
        return registerBlock(block, BlockItem::new, properties);
    }

    public static Item registerBlock(Block block, BiFunction<Block, Item.Properties, Item> biFunction, Item.Properties properties) {
        return registerItem(blockIdToItemId(block.builtInRegistryHolder().key()), propertiesx -> biFunction.apply(block, propertiesx), properties.useBlockDescriptionPrefix());
    }

    public static Item registerItem(String string, Function<Item.Properties, Item> function, Item.Properties properties) {
        return registerItem(createResourceKey(string), function, properties);
    }

    public static Item registerItem(String string, Item.Properties properties) {
        return registerItem(createResourceKey(string), Item::new, properties);
    }

    public static Item registerItem(String string) {
        return registerItem(createResourceKey(string), Item::new, new Item.Properties());
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

        stack.set(DataComponents.BLOCK_ENTITY_DATA, TypedEntityData.of(BlockEntityType.VAULT, blockEntityData));

        return stack;
    }

    public static ItemStack getEndCityBannerInstance(HolderGetter<BannerPattern> getter) {
        ItemStack stack = new ItemStack(MAGENTA_BANNER);
        stack.set(DataComponents.BANNER_PATTERNS, new BannerPatternLayers.Builder()
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
                .build());
        stack.set(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT.withHidden(DataComponents.BANNER_PATTERNS, true));
        stack.set(DataComponents.ITEM_NAME, Component.translatable("block.enderscape.end_city_banner"));
        stack.set(DataComponents.RARITY, Rarity.UNCOMMON);
        return stack;
    }
}