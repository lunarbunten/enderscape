package net.bunten.enderscape.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.bunten.enderscape.Enderscape;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.world.entity.ai.village.poi.PoiType;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;
import static net.minecraft.world.level.block.Blocks.*;

public class EnderscapePoi {

    public static final PoiType DRIFTER_HOME = PointOfInterestHelper.register(
            Enderscape.id("drifter_home"),
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

    public static final PoiType RUSTLE_SLEEPING_SPOT = PointOfInterestHelper.register(
            Enderscape.id("rustle_sleeping_spot"),
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