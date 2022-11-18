package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.items.BlinklightItem;
import net.bunten.enderscape.items.DriftJellyBottleItem;
import net.bunten.enderscape.items.DriftLeggingsItem;
import net.bunten.enderscape.items.FlangerBerryItem;
import net.bunten.enderscape.items.MirrorItem;
import net.bunten.enderscape.items.MurushroomsItem;
import net.bunten.enderscape.items.NebuliteItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.SpawnEggItem;

public class EnderscapeItems {

    public static final TagKey<Item> CELESTIAL_STEMS = register("celestial_stems");
    public static final TagKey<Item> DRIFTER_FOOD = register("drifter_food");
    public static final TagKey<Item> MURUSHROOM_STEMS = register("murushroom_stems");
    public static final TagKey<Item> SHADOW_QUARTZ_BLOCKS = register("shadow_quartz_blocks");
    public static final TagKey<Item> VERADITE_BLOCKS = register("veradite_blocks");
    
    public static final Item BLINKLIGHT = new BlinklightItem(new FabricItemSettings().tab(Enderscape.TAB));
    public static final Item FLANGER_BERRY = new FlangerBerryItem(new FabricItemSettings().tab(Enderscape.TAB));
    public static final Item MURUSHROOMS = new MurushroomsItem(new FabricItemSettings().tab(Enderscape.TAB));

    public static final Item DRIFT_JELLY_BOTTLE = new DriftJellyBottleItem(new FabricItemSettings().tab(Enderscape.TAB).craftRemainder(Items.GLASS_BOTTLE));
    public static final Item DRIFT_LEGGINGS = new DriftLeggingsItem(new FabricItemSettings().tab(Enderscape.TAB));
    public static final Item MIRROR = new MirrorItem(new FabricItemSettings().tab(Enderscape.TAB));
    public static final Item NEBULITE = new NebuliteItem(new FabricItemSettings().tab(Enderscape.TAB));
    public static final Item NEBULITE_SHARDS = new Item(new FabricItemSettings().tab(Enderscape.TAB));
    public static final Item SHADOW_QUARTZ = new Item(new FabricItemSettings().tab(Enderscape.TAB));
    public static final Item MUSIC_DISC_BLISS = musicDisc("bliss", 150, new FabricItemSettings().tab(Enderscape.TAB).stacksTo(1).rarity(Rarity.RARE));
    public static final Item MUSIC_DISC_GLARE = musicDisc("glare", 84, new FabricItemSettings().tab(Enderscape.TAB).stacksTo(1).rarity(Rarity.RARE));
    public static final Item DRIFTER_SPAWN_EGG = new SpawnEggItem(EnderscapeEntities.DRIFTER, 0xE97FFF, 0x7554A8, (new FabricItemSettings().tab(Enderscape.TAB)));
    public static final Item DRIFTLET_SPAWN_EGG = new SpawnEggItem(EnderscapeEntities.DRIFTLET, 0xFFAAF9, 0xE97FFF, (new FabricItemSettings().tab(Enderscape.TAB)));
    public static final Item RUBBLEMITE_SPAWN_EGG = new SpawnEggItem(EnderscapeEntities.RUBBLEMITE, 0xF2FDBE, 0xC1B687, (new FabricItemSettings().tab(Enderscape.TAB)));

    private static Item musicDisc(String name, int lengthInSeconds, Properties settings) {
        return new RecordItem(0, EnderscapeSounds.discMusic(name), settings, lengthInSeconds);
    }

    public static TagKey<Item> register(String name) {
        return TagKey.create(Registry.ITEM_REGISTRY, Enderscape.id(name));
    }
}