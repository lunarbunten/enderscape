package net.bunten.enderscape.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.bunten.enderscape.Enderscape;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PoiHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;
import static net.minecraft.world.level.block.Blocks.*;

public class EnderscapePoi {

    public static final ResourceKey<PoiType> DRIFTER_HOME = create("drifter_home");
    public static final ResourceKey<PoiType> RUSTLE_SLEEPING_SPOT = create("rustle_sleeping_spot");

    private static ResourceKey<PoiType> create(String name) {
        return ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, Enderscape.id(name));
    }

    static {
        PoiHelper.register(
                DRIFTER_HOME.identifier(),
                4,
                8,
                ImmutableList.of(
                                RIPE_FLANGER_BERRY_BLOCK,
                                UNRIPE_FLANGER_BERRY_BLOCK,
                                FLANGER_BERRY_FLOWER
                        )
                        .stream()
                        .flatMap(block -> block.getStateDefinition().getPossibleStates().stream())
                        .collect(ImmutableSet.toImmutableSet())
        );

        PoiHelper.register(
                RUSTLE_SLEEPING_SPOT.identifier(),
                1,
                8,
                ImmutableList.of(
                                VEILED_LEAF_PILE,
                                BLACK_CARPET,
                                BLUE_CARPET,
                                BROWN_CARPET,
                                CYAN_CARPET,
                                GRAY_CARPET,
                                GREEN_CARPET,
                                LIGHT_BLUE_CARPET,
                                LIGHT_GRAY_CARPET,
                                LIME_CARPET,
                                MAGENTA_CARPET,
                                ORANGE_CARPET,
                                PINK_CARPET,
                                PURPLE_CARPET,
                                RED_CARPET,
                                WHITE_CARPET,
                                YELLOW_CARPET,
                                MOSS_CARPET,
                                PALE_MOSS_CARPET
                        )
                        .stream()
                        .flatMap(block -> block.getStateDefinition().getPossibleStates().stream())
                        .collect(ImmutableSet.toImmutableSet())
        );
    }
}