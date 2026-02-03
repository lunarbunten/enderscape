package net.bunten.enderscape.client.registry;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;
import static net.minecraft.world.level.block.Blocks.DRAGON_EGG;

public class EnderscapeBlockRenderLayerMap {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        for (var blockSupplier : List.of(
                ALLURING_MAGNIA_SPROUT,
                BLINKLIGHT_VINES_BODY,
                BLINKLIGHT_VINES_HEAD,
                BULB_FLOWER,
                BULB_LANTERN,
                CELESTIAL_CHANTERELLE,
                CELESTIAL_DOOR,
                CELESTIAL_GROWTH,
                CELESTIAL_TRAPDOOR,
                CHORUS_CAKE_ROLL,
                CHORUS_SPROUTS,
                CORRUPT_GROWTH,
                () -> DRAGON_EGG,
                DRY_END_GROWTH,
                END_TRIAL_SPAWNER,
                END_VAULT,
                FLANGER_BERRY_FLOWER,
                FLANGER_BERRY_VINE,
                MAGNIA_RADIO,
                MIRESTONE_NEBULITE_ORE,
                MURUBLIGHT_CHANTERELLE,
                MURUBLIGHT_DOOR,
                MURUBLIGHT_TRAPDOOR,
                NEBULITE_ORE,
                POTTED_ALLURING_MAGNIA_SPROUT,
                POTTED_BLINKLIGHT,
                POTTED_BULB_FLOWER,
                POTTED_CELESTIAL_CHANTERELLE,
                POTTED_CELESTIAL_GROWTH,
                POTTED_CHORUS_SPROUTS,
                POTTED_CORRUPT_GROWTH,
                POTTED_DRY_END_GROWTH,
                POTTED_MURUBLIGHT_CHANTERELLE,
                POTTED_REPULSIVE_MAGNIA_SPROUT,
                POTTED_VEILED_SAPLING,
                POTTED_WISP_GROWTH,
                REPULSIVE_MAGNIA_SPROUT,
                SHADOLINE_BARS,
                SHADOLINE_CHAIN,
                VOID_TORCH,
                VOID_WALL_TORCH,
                VOID_CAMPFIRE,
                VOID_LANTERN,
                VEILED_DOOR,
                VEILED_SAPLING,
                VOID_TORCH,
                VOID_WALL_TORCH,
                VEILED_TRAPDOOR,
                VEILED_VINES,
                VOID_SHALE,
                WISP_FLOWER,
                WISP_GROWTH,
                WISP_SPROUTS
        )) {
            ItemBlockRenderTypes.setRenderLayer(blockSupplier.get(), RenderType.cutout());
        }
        ItemBlockRenderTypes.setRenderLayer(VEILED_LEAVES.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(VEILED_LEAF_PILE.get(), RenderType.cutoutMipped());

        ItemBlockRenderTypes.setRenderLayer(DRIFT_JELLY_BLOCK.get(), RenderType.translucent());
    }
}