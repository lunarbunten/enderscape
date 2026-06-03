package net.bunten.enderscape.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.bunten.enderscape.Enderscape;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PoiHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

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

        var rustleSleepingSpot = ImmutableList.<Block>builder();

        rustleSleepingSpot.add(VEILED_LEAF_PILE,
                MOSS_CARPET,
                PALE_MOSS_CARPET);

        CARPET.forEach(rustleSleepingSpot::add);


        PoiHelper.register(
                RUSTLE_SLEEPING_SPOT.identifier(),
                1,
                8,
                rustleSleepingSpot.build()
                        .stream()
                        .flatMap(block -> block.getStateDefinition().getPossibleStates().stream())
                        .collect(ImmutableSet.toImmutableSet())
        );
    }
}