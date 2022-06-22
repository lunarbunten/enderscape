package net.bunten.enderscape.registry;


import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.blocks.NebuliteDispenserBehavior;
import net.bunten.enderscape.items.HealingItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.SignItem;
import net.minecraft.util.registry.Registry;

public class EnderscapeRegistry {

    private static void register(String name, Block block) {
        Registry.register(Registry.BLOCK, Enderscape.id(name), block);
    }

    private static <T extends Item> T register(String name, T item) {
        return Registry.register(Registry.ITEM, Enderscape.id(name), item);
    }

    private static void register(String name, Block block, boolean hasItem, boolean addToGroup) {
        register(name, block);
        if (hasItem) {
            register(name, block, addToGroup);
        }
    }

    private static void register(String name, Block block, boolean addToGroup) {
        var settings = new FabricItemSettings();
        if (addToGroup) {
            settings.group(Enderscape.ITEM_GROUP);
        }
        register(name, new BlockItem(block, settings));
    }

    public static void registerCompostableItem(float chance, ItemConvertible item) {
        ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(item.asItem(), chance);
    }
    
    private static void registerDriftItems() {
        register("drift_jelly_bottle", EnderscapeItems.DRIFT_JELLY_BOTTLE);
        register("drift_jelly_block", EnderscapeBlocks.DRIFT_JELLY_BLOCK, true, true);
        register("drift_leggings", EnderscapeItems.DRIFT_LEGGINGS);
    }

    private static void registerCelestialVegetation() {
        register("celestial_mycelium_block", EnderscapeBlocks.CELESTIAL_MYCELIUM_BLOCK, true, true);
        register("celestial_path_block", EnderscapeBlocks.CELESTIAL_PATH_BLOCK, true, true);

        register("celestial_growth", EnderscapeBlocks.CELESTIAL_GROWTH, true, true);
        register("bulb_flower", EnderscapeBlocks.BULB_FLOWER, true, true);
        register("bulb_lantern", EnderscapeBlocks.BULB_LANTERN, true, true);
        
        register("celestial_fungus", EnderscapeBlocks.CELESTIAL_FUNGUS, true, true);
        register("celestial_cap", EnderscapeBlocks.CELESTIAL_CAP, true, true);

        register("murushrooms", EnderscapeBlocks.MURUSHROOMS, false, false);
        register("murushrooms", EnderscapeItems.MURUSHROOMS);
        register("murushroom_cap", EnderscapeBlocks.MURUSHROOM_CAP, true, true);

        register("flanger_berry", EnderscapeItems.FLANGER_BERRY);
        register("flanger_berry_vine", EnderscapeBlocks.FLANGER_BERRY_VINE, false, false);
        register("flanger_berry_block", EnderscapeBlocks.FLANGER_BERRY_BLOCK, true, true);

        register("potted_bulb_flower", EnderscapeBlocks.POTTED_BULB_FLOWER, false, false);
        register("potted_celestial_fungus", EnderscapeBlocks.POTTED_CELESTIAL_FUNGUS, false, false);
        register("potted_celestial_growth", EnderscapeBlocks.POTTED_CELESTIAL_GROWTH, false, false);
    }

    private static void registerNebuliteItems() {
        register("nebulite", EnderscapeItems.NEBULITE);
        register("nebulite_shards", EnderscapeItems.NEBULITE_SHARDS);
        
        register("nebulite_ore", EnderscapeBlocks.NEBULITE_ORE, true, true);
        register("nebulite_block", EnderscapeBlocks.NEBULITE_BLOCK, true, true);
        register("nebulite_cauldron", EnderscapeBlocks.NEBULITE_CAULDRON, false, false);
    }

    private static void registerShadowQuartzItems() {
        register("shadow_quartz", EnderscapeItems.SHADOW_QUARTZ);
        register("shadow_quartz_ore", EnderscapeBlocks.SHADOW_QUARTZ_ORE, true, true);

        register("shadow_quartz_block", EnderscapeBlocks.SHADOW_QUARTZ_BLOCK, true, true);
        register("shadow_quartz_block_stairs", EnderscapeBlocks.SHADOW_QUARTZ_BLOCK_STAIRS, true, true);
        register("shadow_quartz_block_slab", EnderscapeBlocks.SHADOW_QUARTZ_BLOCK_SLAB, true, true);
        register("shadow_quartz_block_wall", EnderscapeBlocks.SHADOW_QUARTZ_BLOCK_WALL, true, true);

        register("smooth_shadow_quartz", EnderscapeBlocks.SMOOTH_SHADOW_QUARTZ, true, true);
        register("smooth_shadow_quartz_stairs", EnderscapeBlocks.SMOOTH_SHADOW_QUARTZ_STAIRS, true, true);
        register("smooth_shadow_quartz_slab", EnderscapeBlocks.SMOOTH_SHADOW_QUARTZ_SLAB, true, true);
        register("smooth_shadow_quartz_wall", EnderscapeBlocks.SMOOTH_SHADOW_QUARTZ_WALL, true, true);

        register("shadow_quartz_bricks", EnderscapeBlocks.SHADOW_QUARTZ_BRICKS, true, true);
        register("shadow_quartz_brick_stairs", EnderscapeBlocks.SHADOW_QUARTZ_BRICK_STAIRS, true, true);
        register("shadow_quartz_brick_slab", EnderscapeBlocks.SHADOW_QUARTZ_BRICK_SLAB, true, true);
        register("shadow_quartz_brick_wall", EnderscapeBlocks.SHADOW_QUARTZ_BRICK_WALL, true, true);

        register("chiseled_shadow_quartz", EnderscapeBlocks.CHISELED_SHADOW_QUARTZ, true, true);
        register("shadow_quartz_pillar", EnderscapeBlocks.SHADOW_QUARTZ_PILLAR, true, true);
    }

    private static void registerCelestialWood() {
        register("celestial_stem", EnderscapeBlocks.CELESTIAL_STEM, true, true);
        register("stripped_celestial_stem", EnderscapeBlocks.STRIPPED_CELESTIAL_STEM, true, true);
        register("celestial_hyphae", EnderscapeBlocks.CELESTIAL_HYPHAE, true, true);
        register("stripped_celestial_hyphae", EnderscapeBlocks.STRIPPED_CELESTIAL_HYPHAE, true, true);
        register("celestial_planks", EnderscapeBlocks.CELESTIAL_PLANKS, true, true);
        register("celestial_stairs", EnderscapeBlocks.CELESTIAL_STAIRS, true, true);
        register("celestial_slab", EnderscapeBlocks.CELESTIAL_SLAB, true, true);
        register("celestial_pressure_plate", EnderscapeBlocks.CELESTIAL_PRESSURE_PLATE, true, true);
        register("celestial_fence", EnderscapeBlocks.CELESTIAL_FENCE, true, true);
        register("celestial_door", EnderscapeBlocks.CELESTIAL_DOOR, true, true);
        register("celestial_trapdoor", EnderscapeBlocks.CELESTIAL_TRAPDOOR, true, true);
        register("celestial_fence_gate", EnderscapeBlocks.CELESTIAL_FENCE_GATE, true, true);
        register("celestial_button", EnderscapeBlocks.CELESTIAL_BUTTON, true, true);

        register("celestial_sign", EnderscapeBlocks.CELESTIAL_SIGN, false, false);
        register("celestial_wall_sign", EnderscapeBlocks.CELESTIAL_WALL_SIGN, false, false);

        register("celestial_sign", new SignItem(new FabricItemSettings().group(Enderscape.ITEM_GROUP), EnderscapeBlocks.CELESTIAL_SIGN, EnderscapeBlocks.CELESTIAL_WALL_SIGN));
    }

    private static void registerShadowSteelItems() {
        register("shadow_steel_block", EnderscapeBlocks.SHADOW_STEEL_BLOCK, true, false);
        register("chiseled_shadow_steel", EnderscapeBlocks.CHISELED_SHADOW_STEEL, true, false);
        register("shadow_steel_pillar", EnderscapeBlocks.SHADOW_STEEL_PILLAR, true, false);
        register("small_shadow_steel_pillar", EnderscapeBlocks.SMALL_SHADOW_STEEL_PILLAR, true, false);
    }

    private static void registerFunny() {
        Registry.register(Registry.PAINTING_VARIANT, Enderscape.id("big_guy"), new PaintingVariant(64, 64));
        Registry.register(Registry.PAINTING_VARIANT, Enderscape.id("cat"), new PaintingVariant(32, 32));
        Registry.register(Registry.PAINTING_VARIANT, Enderscape.id("thirsty_guy"), new PaintingVariant(64, 64));
    }

    private static void registerCompostables() {
        registerCompostableItem(0.3F, EnderscapeBlocks.CELESTIAL_GROWTH);

        registerCompostableItem(0.5F, EnderscapeItems.FLANGER_BERRY);
        registerCompostableItem(0.5F, EnderscapeBlocks.MURUSHROOMS);

        registerCompostableItem(0.65F, EnderscapeBlocks.BULB_FLOWER);
        registerCompostableItem(0.65F, EnderscapeBlocks.CELESTIAL_FUNGUS);

        registerCompostableItem(0.65F, EnderscapeBlocks.FLANGER_BERRY_BLOCK);

        registerCompostableItem(0.85F, EnderscapeBlocks.MURUSHROOM_CAP);
        registerCompostableItem(0.85F, EnderscapeBlocks.CELESTIAL_CAP);
    }


    private static void registerDispenserBehavior() {
        DispenserBlock.registerBehavior(EnderscapeItems.NEBULITE, new NebuliteDispenserBehavior());
    }

    public static void init() {

        register("drifter_spawn_egg", EnderscapeItems.DRIFTER_SPAWN_EGG);
        register("driftlet_spawn_egg", EnderscapeItems.DRIFTLET_SPAWN_EGG);
        register("rubblemite_spawn_egg", EnderscapeItems.RUBBLEMITE_SPAWN_EGG);

        register("music_disc_bliss", EnderscapeItems.MUSIC_DISC_BLISS);
        register("music_disc_glare", EnderscapeItems.MUSIC_DISC_GLARE);

        register("mirror", EnderscapeItems.MIRROR);

        register("healing", new HealingItem(new FabricItemSettings()));

        registerDriftItems();
        registerCelestialVegetation();
        registerCelestialWood();
        registerNebuliteItems();
        registerShadowQuartzItems();
        registerShadowSteelItems();

        registerCompostables();
        registerFunny();

        registerDispenserBehavior();
    }
}