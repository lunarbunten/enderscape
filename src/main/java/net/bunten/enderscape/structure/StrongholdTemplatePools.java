package net.bunten.enderscape.structure;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import dev.worldgen.lithostitched.worldgen.poolelement.legacy.GuaranteedPoolElement;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.Optional;
import java.util.function.Function;

public class StrongholdTemplatePools {

    public static final ResourceKey<StructureTemplatePool> START = EnderscapeTemplatePools.createKey("stronghold/start");
    public static final ResourceKey<StructureTemplatePool> SMALL_HALL_OR_UNIQUE_FALLBACK = EnderscapeTemplatePools.createKey("stronghold/small_hall_or_unique_fallback");
    public static final ResourceKey<StructureTemplatePool> LARGE_HALL_FALLBACK = EnderscapeTemplatePools.createKey("stronghold/large_hall_fallback");

    public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
        HolderGetter<StructureProcessorList> processors = context.lookup(Registries.PROCESSOR_LIST);
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);

        Holder<StructureTemplatePool> emptyFallback = pools.getOrThrow(Pools.EMPTY);

        EnderscapeTemplatePools.register(
                context,
                START,
                new StructureTemplatePool(
                        emptyFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:stronghold/starting_staircase", getDegradation(processors)), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        EnderscapeTemplatePools.register(
                context,
                "stronghold/silverfish_spawner_start",
                new StructureTemplatePool(
                        emptyFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:stronghold/monster_room/silverfish_spawner_start", getDegradation(processors)), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        EnderscapeTemplatePools.register(
                context,
                "stronghold/large_four_way_fountain_only",
                new StructureTemplatePool(
                        emptyFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:stronghold/unique/large_four_way_fountain", getDegradation(processors)), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        EnderscapeTemplatePools.register(
                context,
                "stronghold/small_hall_into_large_hall",
                new StructureTemplatePool(
                        pools.getOrThrow(SMALL_HALL_OR_UNIQUE_FALLBACK),
                        multiple(
                                getSmallHallways(processors),
                                getStaircases(processors)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        EnderscapeTemplatePools.register(
                context,
                SMALL_HALL_OR_UNIQUE_FALLBACK,
                new StructureTemplatePool(
                        emptyFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:stronghold/fallback/small_hall_or_unique/altar", getDegradation(processors)), 20),
                                Pair.of(StructurePoolElement.single("enderscape:stronghold/fallback/small_hall_or_unique/basic", getDegradation(processors)), 20),
                                Pair.of(StructurePoolElement.single("enderscape:stronghold/fallback/small_hall_or_unique/chest", getDegradation(processors)), 10),
                                Pair.of(StructurePoolElement.single("enderscape:stronghold/fallback/small_hall_or_unique/iron_bars", getDegradation(processors)), 20),
                                Pair.of(StructurePoolElement.single("enderscape:stronghold/fallback/small_hall_or_unique/zombie", getDegradation(processors)), 20)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        EnderscapeTemplatePools.register(
                context,
                "stronghold/large_hall_into_unique",
                new StructureTemplatePool(
                        pools.getOrThrow(LARGE_HALL_FALLBACK),
                        getLargeHallways(processors),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        EnderscapeTemplatePools.register(
                context,
                "stronghold/large_hall_into_unique_without_chests",
                new StructureTemplatePool(
                        pools.getOrThrow(LARGE_HALL_FALLBACK),
                        getLargeHallwaysWithoutChests(processors),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        EnderscapeTemplatePools.register(
                context,
                LARGE_HALL_FALLBACK,
                new StructureTemplatePool(
                        emptyFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:stronghold/fallback/large_hall/altar", getDegradation(processors)), 20),
                                Pair.of(StructurePoolElement.single("enderscape:stronghold/fallback/large_hall/basic", getDegradation(processors)), 20),
                                Pair.of(StructurePoolElement.single("enderscape:stronghold/fallback/large_hall/chest", getDegradation(processors)), 10),
                                Pair.of(StructurePoolElement.single("enderscape:stronghold/fallback/large_hall/iron_bars", getDegradation(processors)), 20),
                                Pair.of(StructurePoolElement.single("enderscape:stronghold/fallback/large_hall/spiral", getDegradation(processors)), 20)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        EnderscapeTemplatePools.register(
                context,
                "stronghold/unique_rooms",
                new StructureTemplatePool(
                        emptyFallback,
                        getUniqueRooms(processors),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        EnderscapeTemplatePools.register(
                context,
                "stronghold/spawner/melee",
                new StructureTemplatePool(
                        emptyFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:stronghold/spawner/zombie", getDegradation(processors)), 20)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        EnderscapeTemplatePools.register(
                context,
                "stronghold/spawner/ranged",
                new StructureTemplatePool(
                        emptyFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:stronghold/spawner/skeleton", getDegradation(processors)), 20)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        EnderscapeTemplatePools.register(
                context,
                "stronghold/spawner/silverfish",
                new StructureTemplatePool(
                        emptyFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:stronghold/spawner/silverfish", getDegradation(processors)), 20)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );
    }

    private static ImmutableList<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> getSmallHallways(HolderGetter<StructureProcessorList> processors) {
        Holder.Reference<StructureProcessorList> degradation = getDegradation(processors);

        return ImmutableList.of(
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/straight", degradation), 90),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/straight_chest", degradation), 60),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/straight_chest_trapped", degradation), 20),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/straight_door", degradation), 90),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/straight_door_chest", degradation), 60),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/straight_door_chest_trapped", degradation), 20),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/straight_door_step", degradation), 90),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/straight_door_step_dispenser", degradation), 45),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/straight_iron_door", degradation), 60),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/straight_iron_door_chest", degradation), 30),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/straight_iron_door_chest_trapped", degradation), 15),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/straight_step", degradation), 90),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/straight_step_dispenser", degradation), 45),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/turn", degradation), 40),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/turn_door", degradation), 40),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/turn_iron_door", degradation), 20),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/t_door", degradation), 40),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/t_iron_door", degradation), 20),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/t_section", degradation), 20)
        );
    }

    private static ImmutableList<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> getStaircases(HolderGetter<StructureProcessorList> processors) {
        Holder.Reference<StructureProcessorList> degradation = getDegradation(processors);

        return ImmutableList.of(
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/stairs/down_left", degradation), 50),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/stairs/down_left_chest", degradation), 20),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/stairs/down_right", degradation), 50),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/stairs/down_right_chest", degradation), 20),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/stairs/down_spiral", degradation), 50),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/stairs/down_spiral_chest_bottom", degradation), 10),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/stairs/down_spiral_chest_top", degradation), 10),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/small_hall/stairs/down_spiral_trapped", degradation), 10)
        );
    }

    private static ImmutableList<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> getLargeHallways(HolderGetter<StructureProcessorList> processors) {
        Holder.Reference<StructureProcessorList> degradation = getDegradation(processors);

        return ImmutableList.of(
                Pair.of((projection) -> new GuaranteedPoolElement(StructurePoolElement.single("enderscape:stronghold/end_portal", processors.getOrThrow(StrongholdProcessorLists.PORTAL_ROOM_DEGRADATION)).apply(projection), Optional.empty(), 1), 1),

                Pair.of(StructurePoolElement.single("enderscape:stronghold/monster_room/fabric", degradation), 90),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/monster_room/forge", degradation), 90),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/monster_room/mansion", processors.getOrThrow(StrongholdProcessorLists.LIBRARY_DEGRADATION)), 120),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/monster_room/silverfish_spawner", degradation), 90),

                Pair.of(StructurePoolElement.single("enderscape:stronghold/large_hall/broken_floor", degradation), 40),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/large_hall/split_left", degradation), 90),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/large_hall/split_left_chests1", degradation), 20),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/large_hall/split_left_chests2", degradation), 20),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/large_hall/split_left_chest", degradation), 60),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/large_hall/split_right", degradation), 90),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/large_hall/split_right_chests1", degradation), 20),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/large_hall/split_right_chests2", degradation), 20),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/large_hall/split_right_chest", degradation), 60),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/large_hall/thin", degradation), 90),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/large_hall/thin_chest", degradation), 60)
        );
    }

    private static ImmutableList<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> getLargeHallwaysWithoutChests(HolderGetter<StructureProcessorList> processors) {
        Holder.Reference<StructureProcessorList> degradation = getDegradation(processors);

        return ImmutableList.of(
                Pair.of(StructurePoolElement.single("enderscape:stronghold/large_hall/broken_floor", degradation), 40),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/large_hall/split_left", degradation), 90),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/large_hall/split_right", degradation), 90),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/large_hall/thin", degradation), 90)
        );
    }

    private static ImmutableList<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> getUniqueRooms(HolderGetter<StructureProcessorList> processors) {
        Holder.Reference<StructureProcessorList> degradation = getDegradation(processors);

        return ImmutableList.of(
                Pair.of(StructurePoolElement.single("enderscape:stronghold/monster_room/garden", degradation), 90),

                Pair.of(StructurePoolElement.single("enderscape:stronghold/unique/armory", degradation), 90),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/unique/aquarium", degradation), 90),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/unique/bedrooms", degradation), 45),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/unique/bedrooms_two_story", degradation), 45),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/unique/bridge_intersection_bottom", degradation), 30),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/unique/bridge_intersection_middle", degradation), 30),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/unique/bridge_intersection_top", degradation), 30),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/unique/lab", degradation), 90),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/unique/large_four_way", degradation), 45),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/unique/large_four_way_fountain", degradation), 45),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/unique/large_spiral", degradation), 45),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/unique/large_spiral_bridge", degradation), 45),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/unique/large_three_way", degradation), 60),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/unique/library", processors.getOrThrow(StrongholdProcessorLists.LIBRARY_DEGRADATION)), 90),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/unique/library_ruin", processors.getOrThrow(StrongholdProcessorLists.LIBRARY_DEGRADATION)), 25),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/unique/prison", degradation), 90),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/unique/storage_room", degradation), 90),
                Pair.of(StructurePoolElement.single("enderscape:stronghold/unique/tall_hall_chest", degradation), 90)
        );
    }

    private static Holder.Reference<StructureProcessorList> getDegradation(HolderGetter<StructureProcessorList> processors) {
        return processors.getOrThrow(StrongholdProcessorLists.DEGRADATION);
    }

    private static ImmutableList<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> multiple(ImmutableList<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>... lists) {
        ImmutableList.Builder<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> builder = ImmutableList.builder();
        for (ImmutableList<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> list : lists) {
            builder.addAll(list);
        }
        return builder.build();
    }
}