package net.bunten.enderscape.client.registry;

import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;
import static net.minecraft.world.level.block.Blocks.DRAGON_EGG;

public class EnderscapeBlockRenderLayerMap {

    static {

        BlockRenderLayerMap.putBlocks(ChunkSectionLayer.CUTOUT,
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
                DRAGON_EGG,
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
                VEILED_DOOR,
                VEILED_SAPLING,
                VEILED_TRAPDOOR,
                VEILED_VINES,
                VOID_SHALE,
                WISP_FLOWER,
                WISP_GROWTH,
                WISP_SPROUTS
        );

        BlockRenderLayerMap.putBlocks(ChunkSectionLayer.CUTOUT_MIPPED,
                VEILED_LEAVES,
                VEILED_LEAF_PILE
        );

        BlockRenderLayerMap.putBlocks(ChunkSectionLayer.TRANSLUCENT,
                DRIFT_JELLY_BLOCK
        );
    }
}