package net.bunten.enderscape.registry;

import com.google.common.collect.Maps;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.item.*;
import net.bunten.enderscape.item.component.*;
import net.bunten.enderscape.item.component.value.FuelDisplay;
import net.bunten.enderscape.item.component.value.FuelHud;
import net.bunten.enderscape.item.component.value.FuelSounds;
import net.bunten.enderscape.registry.tag.EnderscapeBannerPatternTags;
import net.bunten.enderscape.registry.tag.EnderscapeItemTags;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.bunten.enderscape.registry.EnderscapeBlocks.END_VAULT;
import static net.minecraft.world.item.Items.MAGENTA_BANNER;

public class EnderscapeItems {

    private static final ItemAttributeModifiers DRIFT_LEGGINGS_ATTRIBUTES = ItemAttributeModifiers.builder()
            .add(Attributes.ARMOR, new AttributeModifier(Enderscape.id("drift_leggings_armor"), 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.LEGS)
            .add(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(Enderscape.id("drift_leggings_armor_toughness"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.LEGS)
            .add(Attributes.MOVEMENT_SPEED, new AttributeModifier(Enderscape.id("drift_leggings_movement_speed"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.LEGS)
            .add(Attributes.SAFE_FALL_DISTANCE, new AttributeModifier(Enderscape.id("drift_leggings_safe_fall_distance"), 0.3, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.LEGS)
            .add(Attributes.GRAVITY, new AttributeModifier(Enderscape.id("drift_leggings_gravity"), -0.3, AttributeModifier.Operation.ADD_MULTIPLIED_BASE), EquipmentSlotGroup.LEGS)
            .build();

    private static final EnumMap<ArmorItem.Type, Integer> SHADOLINE_ARMOR_DEFENSE = Maps.newEnumMap(Map.of(ArmorItem.Type.BOOTS, 2, ArmorItem.Type.LEGGINGS, 5, ArmorItem.Type.CHESTPLATE, 6, ArmorItem.Type.HELMET, 2, ArmorItem.Type.BODY, 6));
    private static final EnumMap<ArmorItem.Type, Double> SHADOLINE_STEALTH_BONUS = Maps.newEnumMap(Map.of(ArmorItem.Type.BOOTS, 0.15, ArmorItem.Type.LEGGINGS, 0.2, ArmorItem.Type.CHESTPLATE, 0.2, ArmorItem.Type.HELMET, 0.15, ArmorItem.Type.BODY, 0.4));

    private static final Holder<ArmorMaterial> SHADOLINE_ARMOR_MATERIAL = registerArmorMaterial(
            Enderscape.id("shadoline"),
            SHADOLINE_ARMOR_DEFENSE,
            15,
            EnderscapeItemSounds.SHADOLINE_ARMOR_EQUIP,
            0.0F,
            0.0F,
            () -> Ingredient.of(EnderscapeItemTags.REPAIRS_SHADOLINE_ARMOR)
    );

    public static final Holder<ArmorMaterial> DRIFT_LEGGINGS_MATERIAL = registerArmorMaterial(
            Enderscape.id("drift_leggings"),
            Util.make(new EnumMap(ArmorItem.Type.class), enumMap -> {
                enumMap.put(ArmorItem.Type.BOOTS, 0);
                enumMap.put(ArmorItem.Type.LEGGINGS, 0);
                enumMap.put(ArmorItem.Type.CHESTPLATE, 0);
                enumMap.put(ArmorItem.Type.HELMET, 0);
                enumMap.put(ArmorItem.Type.BODY, 0);
            }),
            20,
            EnderscapeItemSounds.DRIFT_LEGGINGS_EQUIP,
            0.0F,
            0.0F,
            () -> Ingredient.of(EnderscapeItemTags.REPAIRS_DRIFT_LEGGINGS)
    );

    @NotNull
    private static Properties shadolineArmorProperties(ArmorItem.Type type) {
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        EquipmentSlotGroup group = EquipmentSlotGroup.bySlot(type.getSlot());
        ResourceLocation location = Enderscape.id("armor." + type.getName());

        builder.add(Attributes.ARMOR, new AttributeModifier(location, SHADOLINE_ARMOR_DEFENSE.getOrDefault(type, 0), AttributeModifier.Operation.ADD_VALUE), group);
        builder.add(EnderscapeAttributes.STEALTH, new AttributeModifier(location, SHADOLINE_STEALTH_BONUS.getOrDefault(type, 0.0), AttributeModifier.Operation.ADD_MULTIPLIED_BASE), group);

        ItemAttributeModifiers attributes = builder.build();

        return new Properties().attributes(attributes).durability(type.getDurability(25));
    }

    @NotNull
    private static Supplier<Item> shadolineArmor(String name, ArmorItem.Type type) {
        return registerItem(name, properties -> new ArmorItem(SHADOLINE_ARMOR_MATERIAL, type, properties), shadolineArmorProperties(type));
    }

    private static Holder<ArmorMaterial> registerArmorMaterial(ResourceLocation location, EnumMap<ArmorItem.Type, Integer> enumMap, int i, Holder<SoundEvent> holder, float f, float g, Supplier<Ingredient> supplier) {
        List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(location));
        return RegistryHelper.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL, location, () -> new ArmorMaterial(enumMap, i, holder, supplier, list, f, g));
    }

    //public static final Properties SHULKER_SHELL_PROPERTIES = new Item.Properties().component(DataComponents.EQUIPPABLE, Equippable.builder(ArmorItem.Type.HELMET.getSlot()).setEquipSound(EnderscapeItemSounds.SHULKER_SHELL_EQUIP).setAsset(ResourceKey.create(ROOT_ID, Enderscape.id("shulker_shell"))).build());

    public static final Supplier<Item> DRIFTER_SPAWN_EGG = registerSpawnEgg(EnderscapeEntities.DRIFTER, 0xE97FFF, 0x7554A8);
    public static final Supplier<Item> RUBBLEMITE_SPAWN_EGG = registerSpawnEgg(EnderscapeEntities.RUBBLEMITE, 0xF2FDBE, 0xC1B687);
    public static final Supplier<Item> RUSTLE_SPAWN_EGG = registerSpawnEgg(EnderscapeEntities.RUSTLE, 15131119, 11905990);

    public static final Supplier<Item> RUSTLE_BUCKET = registerItem("rustle_bucket", RustleBucketItem::new, new Item.Properties().stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY));

    public static final Supplier<Item> VEILED_HANGING_SIGN_ITEM = registerItem("veiled_hanging_sign", properties -> new HangingSignItem(EnderscapeBlocks.VEILED_HANGING_SIGN.get(), EnderscapeBlocks.VEILED_WALL_HANGING_SIGN.get(), properties), new Properties().stacksTo(16));
    public static final Supplier<Item> VEILED_SIGN_ITEM = registerItem("veiled_sign", properties -> new SignItem(properties, EnderscapeBlocks.VEILED_SIGN.get(), EnderscapeBlocks.VEILED_WALL_SIGN.get()), new Properties().stacksTo(16));
    public static final Supplier<Item> CELESTIAL_HANGING_SIGN_ITEM = registerItem("celestial_hanging_sign", properties -> new HangingSignItem(EnderscapeBlocks.CELESTIAL_HANGING_SIGN.get(), EnderscapeBlocks.CELESTIAL_WALL_HANGING_SIGN.get(), properties), new Properties().stacksTo(16));
    public static final Supplier<Item> CELESTIAL_SIGN_ITEM = registerItem("celestial_sign", properties -> new SignItem(properties, EnderscapeBlocks.CELESTIAL_SIGN.get(), EnderscapeBlocks.CELESTIAL_WALL_SIGN.get()), new Properties().stacksTo(16));
    public static final Supplier<Item> MURUBLIGHT_HANGING_SIGN_ITEM = registerItem("murublight_hanging_sign", properties -> new HangingSignItem(EnderscapeBlocks.MURUBLIGHT_HANGING_SIGN.get(), EnderscapeBlocks.MURUBLIGHT_WALL_HANGING_SIGN.get(), properties), new Properties().stacksTo(16));
    public static final Supplier<Item> MURUBLIGHT_SIGN_ITEM = registerItem("murublight_sign", properties -> new SignItem(properties, EnderscapeBlocks.MURUBLIGHT_SIGN.get(), EnderscapeBlocks.MURUBLIGHT_WALL_SIGN.get()), new Properties().stacksTo(16));

    public static final Supplier<Item> CHORUS_CAKE_ROLL_ITEM = registerItem("chorus_cake_roll", properties -> new BlockItem(EnderscapeBlocks.CHORUS_CAKE_ROLL.get(), properties), new Properties().stacksTo(1));

    public static final Supplier<Item> BLINKLIGHT = registerItem("blinklight", properties -> new ItemNameBlockItem(EnderscapeBlocks.BLINKLIGHT_VINES_HEAD.get(), properties), new Properties());
    public static final Supplier<Item> DRIFT_JELLY_BOTTLE = registerItem("drift_jelly_bottle", DriftJellyBottleItem::new, new Properties()
            .craftRemainder(Items.GLASS_BOTTLE)
            .food(new FoodProperties.Builder().alwaysEdible().nutrition(6).saturationModifier(0.2F).effect(() -> new MobEffectInstance(EnderscapeMobEffects.LOW_GRAVITY, 400), 1.0F).build())
            .stacksTo(16)
    );

    public static final Supplier<Item> FLANGER_BERRY = registerItem("flanger_berry", properties -> new ItemNameBlockItem(EnderscapeBlocks.FLANGER_BERRY_VINE.get(), properties), new Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(1.2F).build()));
    public static final Supplier<Item> MURUBLIGHT_BRACKET_ITEM = registerItem("murublight_bracket", properties -> new BlockItem(EnderscapeBlocks.MURUBLIGHT_BRACKET.get(), properties), new Properties().food(new FoodProperties.Builder().alwaysEdible().nutrition(4).saturationModifier(0.3F).effect(new MobEffectInstance(MobEffects.POISON, 200), 1.0F).build()));

    public static final Supplier<Item> VOID_TORCH_ITEM = registerItem("void_torch", properties -> new StandingAndWallBlockItem(EnderscapeBlocks.VOID_TORCH.get(), EnderscapeBlocks.VOID_WALL_TORCH.get(), properties, Direction.DOWN), new Properties());

    public static final Supplier<Item> END_CITY_KEY = registerItem("end_city_key");
    public static final Supplier<Item> RUBBLE_CHITIN = registerItem("rubble_chitin");
    public static final Supplier<Item> NEBULITE = registerItem("nebulite");
    public static final Supplier<Item> NEBULITE_SHARDS = registerItem("nebulite_shards");
    public static final Supplier<Item> RAW_SHADOLINE = registerItem("raw_shadoline");
    public static final Supplier<Item> SHADOLINE_INGOT = registerItem("shadoline_ingot");
    public static final Supplier<Item> SHADOLINE_NUGGET = registerItem("shadoline_nugget");

    public static final Properties RUBBLE_SHIELD_PROPERTIES = new Properties()
            .component(EnderscapeDataComponents.DASH_JUMP, DashJump.DEFAULT)
            .durability(336);

    public static final Supplier<Item> END_STONE_RUBBLE_SHIELD = registerItem("end_stone_rubble_shield", RubbleShieldItem::new, RUBBLE_SHIELD_PROPERTIES);
    public static final Supplier<Item> VERADITE_RUBBLE_SHIELD = registerItem("veradite_rubble_shield", RubbleShieldItem::new, RUBBLE_SHIELD_PROPERTIES);
    public static final Supplier<Item> MIRESTONE_RUBBLE_SHIELD = registerItem("mirestone_rubble_shield", RubbleShieldItem::new, RUBBLE_SHIELD_PROPERTIES);
    public static final Supplier<Item> KURODITE_RUBBLE_SHIELD = registerItem("kurodite_rubble_shield", RubbleShieldItem::new, RUBBLE_SHIELD_PROPERTIES);

    public static final Supplier<Item> DRIFT_LEGGINGS = registerItem("drift_leggings", DriftLeggingsItem::new, new Properties().attributes(DRIFT_LEGGINGS_ATTRIBUTES).durability(495));

    public static final Supplier<Item> SHADOLINE_HELMET = shadolineArmor("shadoline_helmet", ArmorItem.Type.HELMET);
    public static final Supplier<Item> SHADOLINE_CHESTPLATE = shadolineArmor("shadoline_chestplate", ArmorItem.Type.CHESTPLATE);
    public static final Supplier<Item> SHADOLINE_LEGGINGS = shadolineArmor("shadoline_leggings", ArmorItem.Type.LEGGINGS);
    public static final Supplier<Item> SHADOLINE_BOOTS = shadolineArmor("shadoline_boots", ArmorItem.Type.BOOTS);

    public static final Supplier<Item> DAGGER = registerItem("dagger", DaggerItem::new, new Properties()
            .stacksTo(1)
            .attributes(ItemAttributeModifiers.builder()
                    .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 3.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(EnderscapeAttributes.BACKSTAB_DAMAGE, new AttributeModifier(EnderscapeAttributes.BASE_BACKSTAB_DAMAGE_ID, 3.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -1.5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
            )
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
                                    Tool.Rule.minesAndDrops(List.of(Blocks.COBWEB), 7.5F)
                            ),
                            1,
                            2
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
    );

    public static final Supplier<Item> MAGNIA_ATTRACTOR = registerItem("magnia_attractor", EnchantableItem::new, new Properties()
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
    );

    public static final Supplier<Item> CRACKED_MIRROR = registerItem("cracked_mirror", CrackedMirrorItem::new, new Properties().rarity(Rarity.RARE).stacksTo(1));

    public static final Supplier<Item> MIRROR = registerItem("mirror", MirrorItem::new, new Properties()
            .stacksTo(1)
            .component(EnderscapeDataComponents.FUELED_TOOL, FueledTool.Builder.create(5).fuelDisplay(FuelDisplay.Builder.create().hud(FuelHud.DEFAULT)).build())
            .component(EnderscapeDataComponents.INCREASE_WITH_DISTANCE, true)
            .component(EnderscapeDataComponents.DISTANCE_TO_INCREASE, 500)
            .rarity(Rarity.RARE)
    );

    public static final Supplier<Item> CRESCENT_BANNER_PATTERN = registerItem("crescent_banner_pattern", properties -> new BannerPatternItem(EnderscapeBannerPatternTags.PATTERN_ITEM_CRESCENT, properties), new Properties().stacksTo(1).rarity(Rarity.RARE));

    public static final Supplier<Item> STASIS_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem("stasis_armor_trim_smithing_template", properties -> SmithingTemplateItem.createArmorTrimTemplate(EnderscapeTrimPatterns.STASIS), new Properties());

    public static final Supplier<Item> MUSIC_DISC_GLARE = registerMusicDisc(EnderscapeJukeboxSongs.GLARE, Rarity.RARE);
    public static final Supplier<Item> MUSIC_DISC_BLISS = registerMusicDisc(EnderscapeJukeboxSongs.BLISS, Rarity.RARE);
    public static final Supplier<Item> MUSIC_DISC_DECAY = registerMusicDisc(EnderscapeJukeboxSongs.DECAY, Rarity.UNCOMMON);

    public static final Supplier<Item> HEALING = registerItem("healing", HealingItem::new, new Properties());

    private static <T extends Mob> Supplier<Item> registerSpawnEgg(DeferredHolder<EntityType<?>, EntityType<T>> type, int color1, int color2) {
        String name = type.getId().getPath() + "_spawn_egg";
        name = name.replace("entity.enderscape.", "");
        return registerItem(name, properties -> new DeferredSpawnEggItem(type, color1, color2, properties), new Item.Properties());
    }

    private static Supplier<Item> registerMusicDisc(ResourceKey<JukeboxSong> song, Rarity rarity) {
        return registerItem("music_disc_" + song.location().getPath(), Item::new, new Properties().stacksTo(1).rarity(rarity).jukeboxPlayable(song));
    }

    private static ResourceKey<Item> createResourceKey(String name) {
        return ResourceKey.create(Registries.ITEM, Enderscape.id(name));
    }

    private static ResourceKey<Item> blockIdToItemId(ResourceKey<Block> resourceKey) {
        return ResourceKey.create(Registries.ITEM, resourceKey.location());
    }

    public static Supplier<Item> registerBlock(Supplier<Block> block, ResourceKey<Block> key) {
        return registerBlock(block, key, BlockItem::new, new Item.Properties());
    }

    public static Supplier<Item> registerBlock(Supplier<Block> block, ResourceKey<Block> key, BiFunction<Block, Item.Properties, Item> biFunction, Item.Properties properties) {
        return registerItem(blockIdToItemId(key), propertiesx -> biFunction.apply(block.get(), propertiesx), properties);
    }

    public static Supplier<Item> registerItem(String string, Function<Item.Properties, Item> function, Item.Properties properties) {
        return registerItem(createResourceKey(string), function, properties);
    }

    public static Supplier<Item> registerItem(String string) {
        return registerItem(createResourceKey(string), Item::new, new Item.Properties());
    }

    public static Supplier<Item> registerItem(ResourceKey<Item> resourceKey, Function<Item.Properties, Item> function, Item.Properties properties) {
        return RegistryHelper.register(BuiltInRegistries.ITEM, resourceKey, () -> {
            Item item = function.apply(properties);
            if (item instanceof BlockItem blockItem) {
                blockItem.registerBlocks(Item.BY_BLOCK, item);
            }
            return item;
        });
    }

    public static ItemStack getEndVaultInstance() {
        ItemStack stack = new ItemStack(END_VAULT.get());

        CompoundTag keyItem = new CompoundTag();

        keyItem.putInt("count", 1);
        keyItem.putString("id", "enderscape:end_city_key");

        CompoundTag configTag = new CompoundTag();

        configTag.put("key_item", keyItem);
        configTag.putString("loot_table", "enderscape:end_city/vault");

        CompoundTag blockEntityData = new CompoundTag();

        blockEntityData.putString("id", "minecraft:vault");
        blockEntityData.put("config", configTag);

        stack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(blockEntityData));

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
        stack.set(DataComponents.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        stack.set(DataComponents.ITEM_NAME, Component.translatable("block.enderscape.end_city_banner"));
        stack.set(DataComponents.RARITY, Rarity.UNCOMMON);
        return stack;
    }
}