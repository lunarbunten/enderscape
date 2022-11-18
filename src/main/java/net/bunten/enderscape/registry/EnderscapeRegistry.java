package net.bunten.enderscape.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.blocks.NebuliteDispenserBehavior;
import net.bunten.enderscape.items.HealingItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.DispenserBlock;

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
        FabricItemSettings settings = new FabricItemSettings();
        if (addToGroup) settings.tab(Enderscape.TAB);
        register(name, new BlockItem(block, settings));
    }

    public static void registerCompostableItem(float chance, ItemLike item) {
        ComposterBlock.COMPOSTABLES.put(item.asItem(), chance);
    }

    private static void registerMiscItems() {
        register("drifter_spawn_egg", EnderscapeItems.DRIFTER_SPAWN_EGG);
        register("driftlet_spawn_egg", EnderscapeItems.DRIFTLET_SPAWN_EGG);
        register("rubblemite_spawn_egg", EnderscapeItems.RUBBLEMITE_SPAWN_EGG);
        register("music_disc_bliss", EnderscapeItems.MUSIC_DISC_BLISS);
        register("music_disc_glare", EnderscapeItems.MUSIC_DISC_GLARE);
        register("mirror", EnderscapeItems.MIRROR);
        register("healing", new HealingItem(new FabricItemSettings()));
    }
    
    private static void registerDriftItems() {
        register("drift_jelly_bottle", EnderscapeItems.DRIFT_JELLY_BOTTLE);
        register("drift_jelly_block", EnderscapeBlocks.DRIFT_JELLY_BLOCK, true, true);
        register("drift_leggings", EnderscapeItems.DRIFT_LEGGINGS);
    }

    private static void registerVegetation() {
        register("celestial_mycelium_block", EnderscapeBlocks.CELESTIAL_MYCELIUM_BLOCK, true, true);
        register("celestial_path_block", EnderscapeBlocks.CELESTIAL_PATH_BLOCK, true, true);
        register("celestial_growth", EnderscapeBlocks.CELESTIAL_GROWTH, true, true);

        register("bulb_flower", EnderscapeBlocks.BULB_FLOWER, true, true);
        register("bulb_lantern", EnderscapeBlocks.BULB_LANTERN, true, true);

        register("flanger_berry", EnderscapeItems.FLANGER_BERRY);
        register("flanger_berry_vine", EnderscapeBlocks.FLANGER_BERRY_VINE, false, false);
        register("flanger_berry_block", EnderscapeBlocks.FLANGER_BERRY_BLOCK, true, true);

        register("corrupt_mycelium_block", EnderscapeBlocks.CORRUPT_MYCELIUM_BLOCK, true, true);
        register("corrupt_path_block", EnderscapeBlocks.CORRUPT_PATH_BLOCK, true, true);

        register("corrupt_growth", EnderscapeBlocks.CORRUPT_GROWTH, true, true);

        register("blinklight_vines_body", EnderscapeBlocks.BLINKLIGHT_VINES_BODY, false, false);
        register("blinklight_vines_head", EnderscapeBlocks.BLINKLIGHT_VINES_HEAD, false, false);

        register("blinklight", EnderscapeItems.BLINKLIGHT);
        register("blinklamp", EnderscapeBlocks.BLINKLAMP, true, true);

        register("potted_bulb_flower", EnderscapeBlocks.POTTED_BULB_FLOWER, false, false);
        register("potted_celestial_fungus", EnderscapeBlocks.POTTED_CELESTIAL_FUNGUS, false, false);
        register("potted_celestial_growth", EnderscapeBlocks.POTTED_CELESTIAL_GROWTH, false, false);
        register("potted_corrupt_growth", EnderscapeBlocks.POTTED_CORRUPT_GROWTH, false, false);
        register("potted_blinklight", EnderscapeBlocks.POTTED_BLINKLIGHT, false, false);

    }

    private static void registerVeraditeBlocks() {
        register("veradite", EnderscapeBlocks.VERADITE, true, true);
        register("veradite_stairs", EnderscapeBlocks.VERADITE_STAIRS, true, true);
        register("veradite_slab", EnderscapeBlocks.VERADITE_SLAB, true, true);
        register("veradite_wall", EnderscapeBlocks.VERADITE_WALL, true, true);

        register("polished_veradite", EnderscapeBlocks.POLISHED_VERADITE, true, true);
        register("polished_veradite_stairs", EnderscapeBlocks.POLISHED_VERADITE_STAIRS, true, true);
        register("polished_veradite_slab", EnderscapeBlocks.POLISHED_VERADITE_SLAB, true, true);
        register("polished_veradite_wall", EnderscapeBlocks.POLISHED_VERADITE_WALL, true, true);

        register("polished_veradite_button", EnderscapeBlocks.POLISHED_VERADITE_BUTTON, true, true);
        register("polished_veradite_pressure_plate", EnderscapeBlocks.POLISHED_VERADITE_PRESSURE_PLATE, true, true);

        register("veradite_bricks", EnderscapeBlocks.VERADITE_BRICKS, true, true);
        register("veradite_brick_stairs", EnderscapeBlocks.VERADITE_BRICK_STAIRS, true, true);
        register("veradite_brick_slab", EnderscapeBlocks.VERADITE_BRICK_SLAB, true, true);
        register("veradite_brick_wall", EnderscapeBlocks.VERADITE_BRICK_WALL, true, true);

        register("chiseled_veradite", EnderscapeBlocks.CHISELED_VERADITE, true, true);
        register("veradite_pillar", EnderscapeBlocks.VERADITE_PILLAR, true, true);
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
        register("celestial_fungus", EnderscapeBlocks.CELESTIAL_FUNGUS, true, true);

        register("celestial_cap", EnderscapeBlocks.CELESTIAL_CAP, true, true);
        register("celestial_bricks", EnderscapeBlocks.CELESTIAL_BRICKS, true, true);
        register("celestial_brick_stairs", EnderscapeBlocks.CELESTIAL_BRICK_STAIRS, true, true);
        register("celestial_brick_slab", EnderscapeBlocks.CELESTIAL_BRICK_SLAB, true, true);
        register("celestial_brick_wall", EnderscapeBlocks.CELESTIAL_BRICK_WALL, true, true);

        register("celestial_stem", EnderscapeBlocks.CELESTIAL_STEM, true, true);
        register("stripped_celestial_stem", EnderscapeBlocks.STRIPPED_CELESTIAL_STEM, true, true);
        register("celestial_hyphae", EnderscapeBlocks.CELESTIAL_HYPHAE, true, true);
        register("stripped_celestial_hyphae", EnderscapeBlocks.STRIPPED_CELESTIAL_HYPHAE, true, true);
        register("celestial_planks", EnderscapeBlocks.CELESTIAL_PLANKS, true, true);
        register("celestial_stairs", EnderscapeBlocks.CELESTIAL_STAIRS, true, true);
        register("celestial_slab", EnderscapeBlocks.CELESTIAL_SLAB, true, true);
        register("celestial_button", EnderscapeBlocks.CELESTIAL_BUTTON, true, true);
        register("celestial_pressure_plate", EnderscapeBlocks.CELESTIAL_PRESSURE_PLATE, true, true);
        register("celestial_door", EnderscapeBlocks.CELESTIAL_DOOR, true, true);
        register("celestial_trapdoor", EnderscapeBlocks.CELESTIAL_TRAPDOOR, true, true);
        register("celestial_fence", EnderscapeBlocks.CELESTIAL_FENCE, true, true);
        register("celestial_fence_gate", EnderscapeBlocks.CELESTIAL_FENCE_GATE, true, true);

        register("celestial_sign", EnderscapeBlocks.CELESTIAL_SIGN, false, false);
        register("celestial_wall_sign", EnderscapeBlocks.CELESTIAL_WALL_SIGN, false, false);

        register("celestial_sign", new SignItem(new FabricItemSettings().tab(Enderscape.TAB), EnderscapeBlocks.CELESTIAL_SIGN, EnderscapeBlocks.CELESTIAL_WALL_SIGN));
    }

    private static void registerMurushroomWood() {
        register("murushrooms", EnderscapeBlocks.MURUSHROOMS, false, false);
        register("murushrooms", EnderscapeItems.MURUSHROOMS);

        register("murushroom_cap", EnderscapeBlocks.MURUSHROOM_CAP, true, true);
        register("murushroom_bricks", EnderscapeBlocks.MURUSHROOM_BRICKS, true, true);
        register("murushroom_brick_stairs", EnderscapeBlocks.MURUSHROOM_BRICK_STAIRS, true, true);
        register("murushroom_brick_slab", EnderscapeBlocks.MURUSHROOM_BRICK_SLAB, true, true);
        register("murushroom_brick_wall", EnderscapeBlocks.MURUSHROOM_BRICK_WALL, true, true);  

        register("murushroom_stem", EnderscapeBlocks.MURUSHROOM_STEM, true, true);
        register("stripped_murushroom_stem", EnderscapeBlocks.STRIPPED_MURUSHROOM_STEM, true, true);
        register("murushroom_hyphae", EnderscapeBlocks.MURUSHROOM_HYPHAE, true, true);
        register("stripped_murushroom_hyphae", EnderscapeBlocks.STRIPPED_MURUSHROOM_HYPHAE, true, true);
        register("murushroom_planks", EnderscapeBlocks.MURUSHROOM_PLANKS, true, true);
        register("murushroom_stairs", EnderscapeBlocks.MURUSHROOM_STAIRS, true, true);
        register("murushroom_slab", EnderscapeBlocks.MURUSHROOM_SLAB, true, true);
        register("murushroom_button", EnderscapeBlocks.MURUSHROOM_BUTTON, true, true);
        register("murushroom_pressure_plate", EnderscapeBlocks.MURUSHROOM_PRESSURE_PLATE, true, true);
        register("murushroom_door", EnderscapeBlocks.MURUSHROOM_DOOR, true, true);
        register("murushroom_trapdoor", EnderscapeBlocks.MURUSHROOM_TRAPDOOR, true, true);
        register("murushroom_fence", EnderscapeBlocks.MURUSHROOM_FENCE, true, true);
        register("murushroom_fence_gate", EnderscapeBlocks.MURUSHROOM_FENCE_GATE, true, true);
        
        register("murushroom_sign", EnderscapeBlocks.MURUSHROOM_SIGN, false, false);
        register("murushroom_wall_sign", EnderscapeBlocks.MURUSHROOM_WALL_SIGN, false, false);
        
        register("murushroom_sign", new SignItem(new FabricItemSettings().tab(Enderscape.TAB), EnderscapeBlocks.MURUSHROOM_SIGN, EnderscapeBlocks.MURUSHROOM_WALL_SIGN));
    }

    private static void registerVanillaCompat() {
        registerCompostableItem(0.3F, EnderscapeBlocks.CELESTIAL_GROWTH);
        registerCompostableItem(0.3F, EnderscapeBlocks.CORRUPT_GROWTH);
        registerCompostableItem(0.3F, Items.CHORUS_FRUIT);

        registerCompostableItem(0.5F, EnderscapeItems.BLINKLIGHT);
        registerCompostableItem(0.5F, EnderscapeBlocks.MURUSHROOMS);
        registerCompostableItem(0.5F, EnderscapeItems.FLANGER_BERRY);

        registerCompostableItem(0.65F, EnderscapeBlocks.BULB_FLOWER);
        registerCompostableItem(0.65F, EnderscapeBlocks.CELESTIAL_FUNGUS);
        registerCompostableItem(0.65F, EnderscapeBlocks.FLANGER_BERRY_BLOCK);
        
        registerCompostableItem(0.85F, EnderscapeBlocks.CELESTIAL_CAP);
        registerCompostableItem(0.85F, EnderscapeBlocks.MURUSHROOM_CAP);

        DispenserBlock.registerBehavior(EnderscapeItems.NEBULITE, new NebuliteDispenserBehavior());
    }

    static {
        registerMiscItems();
        registerDriftItems();
        registerVegetation();
        registerCelestialWood();
        registerMurushroomWood();
        registerNebuliteItems();
        registerShadowQuartzItems();
        registerVeraditeBlocks();

        registerVanillaCompat();
    }
}