package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.items.DriftJellyBottleItem;
import net.bunten.enderscape.items.DriftLeggingsItem;
import net.bunten.enderscape.items.FlangerBerryItem;
import net.bunten.enderscape.items.MirrorItem;
import net.bunten.enderscape.items.MurushroomsItem;
import net.bunten.enderscape.items.NebuliteItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class EnderscapeItems {

    public static final TagKey<Item> DRIFTER_FOOD = register("drifter_food");
    
    public static final FoodComponent FLANGER_BERRY_COMPONENT = new FoodComponent.Builder().hunger(3).saturationModifier(0.8F).build();
    public static final FoodComponent DRIFT_JELLY_COMPONENT = new FoodComponent.Builder().hunger(6).saturationModifier(0.2F).statusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 100), 1).build();
    public static final FoodComponent MURUSHROOMS_COMPONENT = new FoodComponent.Builder().alwaysEdible().hunger(4).saturationModifier(0.3F).statusEffect(new StatusEffectInstance(StatusEffects.POISON, 200, 1), 1).build();
    
    public static final Item FLANGER_BERRY = new FlangerBerryItem(new FabricItemSettings().group(Enderscape.ITEM_GROUP));
    public static final Item MURUSHROOMS = new MurushroomsItem(new FabricItemSettings().group(Enderscape.ITEM_GROUP));

    public static final Item DRIFT_JELLY_BOTTLE = new DriftJellyBottleItem(new FabricItemSettings().group(Enderscape.ITEM_GROUP).recipeRemainder(Items.GLASS_BOTTLE));
    public static final Item DRIFT_LEGGINGS = new DriftLeggingsItem(new FabricItemSettings().group(Enderscape.ITEM_GROUP));
    public static final Item MIRROR = new MirrorItem(new FabricItemSettings().group(Enderscape.ITEM_GROUP));
    public static final Item NEBULITE = new NebuliteItem(new FabricItemSettings().group(Enderscape.ITEM_GROUP));
    public static final Item NEBULITE_SHARDS = new Item(new FabricItemSettings().group(Enderscape.ITEM_GROUP));
    public static final Item SHADOW_QUARTZ = new Item(new FabricItemSettings().group(Enderscape.ITEM_GROUP));
    public static final Item MUSIC_DISC_BLISS = new MusicDiscItem(0, EnderscapeSounds.MUSIC_DISC_BLISS, new FabricItemSettings().maxCount(1).rarity(Rarity.RARE).group(Enderscape.ITEM_GROUP));
    public static final Item MUSIC_DISC_GLARE = new MusicDiscItem(0, EnderscapeSounds.MUSIC_DISC_GLARE, new FabricItemSettings().maxCount(1).rarity(Rarity.RARE).group(Enderscape.ITEM_GROUP));
    public static final Item DRIFTER_SPAWN_EGG = new SpawnEggItem(EnderscapeEntities.DRIFTER, 0xE97FFF, 0x7554A8, (new FabricItemSettings()).group(Enderscape.ITEM_GROUP));
    public static final Item DRIFTLET_SPAWN_EGG = new SpawnEggItem(EnderscapeEntities.DRIFTLET, 0xFFAAF9, 0xE97FFF, (new FabricItemSettings()).group(Enderscape.ITEM_GROUP));
    public static final Item RUBBLEMITE_SPAWN_EGG = new SpawnEggItem(EnderscapeEntities.RUBBLEMITE, 0xF2FDBE, 0xC1B687, (new FabricItemSettings()).group(Enderscape.ITEM_GROUP));

    private static TagKey<Item> register(String name) {
        return TagKey.of(Registry.ITEM_KEY, Enderscape.id(name));
    }
}