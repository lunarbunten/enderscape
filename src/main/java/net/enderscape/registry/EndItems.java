package net.enderscape.registry;

import net.enderscape.Enderscape;
import net.enderscape.items.DriftBootsItem;
import net.enderscape.items.DriftJellyBottleItem;
import net.enderscape.items.FlangerBerryItem;
import net.enderscape.items.HealingItem;
import net.enderscape.items.MirrorItem;
import net.enderscape.items.NebuliteItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.tag.Tag;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class EndItems {

    public static final Tag<Item> DRIFT_ITEMS = register("drift_items");
    public static final Tag<Item> DRIFTER_FOOD = register("drifter_food");
    public static final Tag<Item> RUBBLEMITE_FOOD = register("rubblemite_food");
    
    public static final Item DRIFT_BOOTS = register("drift_boots", new DriftBootsItem(new FabricItemSettings().group(Enderscape.GROUP)));
    public static final Item DRIFT_JELLY_BOTTLE = register("drift_jelly_bottle", new DriftJellyBottleItem(new FabricItemSettings().group(Enderscape.GROUP).recipeRemainder(Items.GLASS_BOTTLE)));
    public static final Item FLANGER_BERRY = register("flanger_berry", new FlangerBerryItem(new FabricItemSettings().group(Enderscape.GROUP)));
    public static final Item MIRROR = register("mirror", new MirrorItem(new FabricItemSettings().group(Enderscape.GROUP)));
    public static final Item MURUSHROOMS = register("murushrooms", new BlockItem(EndBlocks.MURUSHROOMS, new FabricItemSettings().food(EndFoods.MURUSHROOMS).group(Enderscape.GROUP)));
    public static final Item NEBULITE = register("nebulite", new NebuliteItem(new FabricItemSettings().group(Enderscape.GROUP)));
    public static final Item NEBULITE_SHARDS = register("nebulite_shards", new Item(new FabricItemSettings().group(Enderscape.GROUP)));
    public static final Item SHADOW_QUARTZ = register("shadow_quartz", new Item(new FabricItemSettings().group(Enderscape.GROUP)));
    public static final Item HEAL = registerDebug("healing", new HealingItem(new FabricItemSettings().group(Enderscape.GROUP).maxCount(1)));
    public static final Item MUSIC_DISC_GLARE = register("music_disc_glare", new MusicDiscItem(0, EndSounds.MUSIC_GLARE, new FabricItemSettings().maxCount(1).rarity(Rarity.RARE).group(Enderscape.GROUP)));
    public static final Item DRIFTER_SPAWN_EGG = register("drifter_spawn_egg", new SpawnEggItem(EndEntities.DRIFTER, 0xE97FFF, 0x7755A0, (new FabricItemSettings()).group(Enderscape.GROUP)));
    public static final Item RUBBLEMITE_SPAWN_EGG = register("rubblemite_spawn_egg", new SpawnEggItem(EndEntities.RUBBLEMITE, 0xF2FDBE, 0xC1B687, (new FabricItemSettings()).group(Enderscape.GROUP)));

    private static Tag<Item> register(String name) {
        return TagFactory.ITEM.create(Enderscape.id(name));
    }

    private static <T extends Item> T register(String name, T item) {
        return Registry.register(Registry.ITEM, Enderscape.id(name), item);
    }

    private static <T extends Item> T registerDebug(String name, T item) {
        return FabricLoader.getInstance().isDevelopmentEnvironment() ? Registry.register(Registry.ITEM, Enderscape.id(name), item) : null;
    }

    public static void init() {
        //for entrypoint
    }
}