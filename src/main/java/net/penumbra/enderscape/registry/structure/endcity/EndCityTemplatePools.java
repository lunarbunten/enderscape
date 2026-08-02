package net.penumbra.enderscape.registry.structure.endcity;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import dev.worldgen.lithostitched.worldgen.poolelement.legacy.GuaranteedPoolElement;
import dev.worldgen.lithostitched.worldgen.poolelement.legacy.LimitedPoolElement;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.penumbra.enderscape.registry.structure.EnderscapeTemplatePools;

import java.util.Optional;

public class EndCityTemplatePools extends EnderscapeTemplatePools {

    public static final ResourceKey<StructureTemplatePool> START = localized("start");

    public static final ResourceKey<StructureTemplatePool> WALKWAY_FALLBACK = localized("walkway/fallback");
    public static final ResourceKey<StructureTemplatePool> TOWER_FALLBACK = localized("tower/fallback");
    public static final ResourceKey<StructureTemplatePool> TOWER_WIDE_FALLBACK = localized("tower/wide_fallback");

    public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
        HolderGetter<StructureProcessorList> processors = context.lookup(Registries.PROCESSOR_LIST);
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);

        registerBasicPools(context, pools, processors);
        registerWalkwayPools(context, pools, processors);
        registerTowerPools(context, pools, processors);
    }

    private static void registerBasicPools(BootstrapContext<StructureTemplatePool> context, HolderGetter<StructureTemplatePool> pools, HolderGetter<StructureProcessorList> processors) {
        Holder<StructureTemplatePool> emptyFallback = pools.getOrThrow(Pools.EMPTY);
        Holder.Reference<StructureProcessorList> pottedPlants = processors.getOrThrow(EndCityProcessorLists.POTTED_PLANTS);

        register(
                context,
                START,
                new StructureTemplatePool(
                        emptyFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:end_city/start", pottedPlants), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        register(
                context,
                "start_platform",
                new StructureTemplatePool(
                        emptyFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:end_city/start_platform", processors.getOrThrow(EndCityProcessorLists.START_PLATFORM_MODIFICATION)), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        register(
                context,
                "ship",
                new StructureTemplatePool(
                        emptyFallback,
                        ImmutableList.of(Pair.of(
                                (projection) -> new GuaranteedPoolElement(
                                        StructurePoolElement.single("enderscape:end_city/ship", pottedPlants).apply(projection),
                                        Optional.empty(),
                                        1
                                ), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        register(
                context,
                "furniture",
                new StructureTemplatePool(
                        emptyFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/furniture/furniture_1", pottedPlants), 50),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/furniture/furniture_2", pottedPlants), 50),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/furniture/furniture_3", pottedPlants), 50),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/furniture/furniture_4", pottedPlants), 50),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/furniture/furniture_5", pottedPlants), 50),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/furniture/furniture_6", pottedPlants), 50),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/furniture/furniture_7", pottedPlants), 50),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/furniture/furniture_8", pottedPlants), 50),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/furniture/furniture_9", pottedPlants), 50),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/furniture/furniture_10", pottedPlants), 50),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/furniture/furniture_11", pottedPlants), 50),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/furniture/furniture_12", pottedPlants), 50),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/furniture/furniture_13", pottedPlants), 50),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/furniture/furniture_14", pottedPlants), 50),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/furniture/furniture_15", pottedPlants), 50),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/furniture/furniture_16", pottedPlants), 50),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/furniture/furniture_17", pottedPlants), 50),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/furniture/furniture_suspicious", pottedPlants), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );
    }

    private static void registerWalkwayPools(BootstrapContext<StructureTemplatePool> context, HolderGetter<StructureTemplatePool> pools, HolderGetter<StructureProcessorList> processors) {
        Holder<StructureTemplatePool> emptyFallback = pools.getOrThrow(Pools.EMPTY);
        Holder.Reference<StructureTemplatePool> walkwayFallback = pools.getOrThrow(WALKWAY_FALLBACK);
        Holder.Reference<StructureProcessorList> pottedPlants = processors.getOrThrow(EndCityProcessorLists.POTTED_PLANTS);

        register(
                context,
                "walkway/end_options",
                new StructureTemplatePool(
                        walkwayFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/one_floor_furniture", pottedPlants), 2),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/one_floor_furniture_stairs", pottedPlants), 2),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/one_floor_vault", pottedPlants), 2),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/one_floor_vault_2", pottedPlants), 2),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/three_floor_into_tower", pottedPlants), 3),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/three_floor_into_tower_2", pottedPlants), 3),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/three_floor_vault_and_spawners", pottedPlants), 3),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/three_floor_vault_and_spawners_2", pottedPlants), 3),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/two_floor_furniture", pottedPlants), 2),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/two_floor_into_tower", pottedPlants), 4),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/two_floor_vault_and_spawner", pottedPlants), 2),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/two_floor_vault_into_tower", pottedPlants), 4),
                                Pair.of(
                                        (projection) -> new LimitedPoolElement(
                                                StructurePoolElement.single("enderscape:end_city/walkway/ship_archway", pottedPlants).apply(projection),
                                                Optional.of(10), 1
                                        ), 8
                                ),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/walkway/unique/arena_corrupt_garden_end", pottedPlants), 3)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        register(
                context,
                WALKWAY_FALLBACK,
                new StructureTemplatePool(
                        emptyFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:end_city/house/one_floor", pottedPlants), 100),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/walkway/fallback", pottedPlants), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        register(
                context,
                "walkway/middle_diagonal",
                new StructureTemplatePool(
                        walkwayFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:end_city/walkway/gentle_middle", pottedPlants), 6),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/walkway/steep_middle", pottedPlants), 6)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        register(
                context,
                "walkway/middle_diagonal_or_unique",
                new StructureTemplatePool(
                        walkwayFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:end_city/walkway/gentle_middle", pottedPlants), 6),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/walkway/steep_middle", pottedPlants), 6),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/walkway/unique/arena_archway", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/walkway/unique/arena_corrupt_garden", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/walkway/unique/arena_shrine", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/walkway/unique/arena_simple", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/walkway/unique/drift_jelly_bridge", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/walkway/unique/veiled_tree_trap", pottedPlants), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        register(
                context,
                "walkway/middle_into_end",
                new StructureTemplatePool(
                        walkwayFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:end_city/walkway/gentle_into_end", pottedPlants), 6),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/walkway/steep_into_end", pottedPlants), 6)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        register(
                context,
                "walkway/start",
                new StructureTemplatePool(
                        walkwayFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:end_city/walkway/flat_start", pottedPlants), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );
    }

    private static void registerTowerPools(BootstrapContext<StructureTemplatePool> context, HolderGetter<StructureTemplatePool> pools, HolderGetter<StructureProcessorList> processors) {
        Holder<StructureTemplatePool> emptyFallback = pools.getOrThrow(Pools.EMPTY);
        Holder.Reference<StructureTemplatePool> towerFallback = pools.getOrThrow(TOWER_FALLBACK);
        Holder.Reference<StructureTemplatePool> wideFallback = pools.getOrThrow(TOWER_WIDE_FALLBACK);
        Holder.Reference<StructureProcessorList> pottedPlants = processors.getOrThrow(EndCityProcessorLists.POTTED_PLANTS);

        register(
                context,
                "tower/wide",
                new StructureTemplatePool(
                        wideFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_1_a", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_1_b", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_1_c", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_1_door", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_2_a", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_2_b", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_2_c", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_2_a_shulker", pottedPlants), 2),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_2_b_shulker", pottedPlants), 2),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_2_c_shulker", pottedPlants), 2),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_bottom_1_a", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_bottom_1_b", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_bottom_2_a", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_bottom_2_b", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/top", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/decoration_1", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/decoration_2", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/decoration_3", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/decoration_4", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/decoration_5", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/decoration_6", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/decoration_7", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_top_1", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_top_2", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_top_3", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_top_4", pottedPlants), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        register(
                context,
                TOWER_WIDE_FALLBACK,
                new StructureTemplatePool(
                        wideFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_1_a", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_1_b", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_1_c", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_2_a", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_2_b", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_2_c", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_bottom_1_a", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_bottom_1_b", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_bottom_2_a", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_bottom_2_b", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/top", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/decoration_1", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/decoration_2", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/decoration_3", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/decoration_4", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/decoration_5", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/decoration_6", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/decoration_7", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_top_1", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_top_2", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_top_3", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/sides_top_4", pottedPlants), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        register(
                context,
                "tower/wide_tower_thin_base",
                new StructureTemplatePool(
                        towerFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/core_1", pottedPlants), 3),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/core_2", pottedPlants), 7),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide/core_3", pottedPlants), 5)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        register(
                context,
                TOWER_FALLBACK,
                new StructureTemplatePool(
                        emptyFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/top", pottedPlants), 100),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/lid", pottedPlants), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        register(
                context,
                "tower/middle",
                new StructureTemplatePool(
                        towerFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/middle_1", pottedPlants), 4),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/middle_2", pottedPlants), 4),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/wide_tower", pottedPlants), 10)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        register(
                context,
                "tower/post_terminal",
                new StructureTemplatePool(
                        towerFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/middle_upper", pottedPlants), 2),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/middle_upper_wide", pottedPlants), 2)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        register(
                context,
                "tower/start",
                new StructureTemplatePool(
                        towerFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/middle_1", pottedPlants), 2),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/middle_2", pottedPlants), 2),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/middle_3", pottedPlants), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        register(
                context,
                "tower/terminal",
                new StructureTemplatePool(
                        towerFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/terminal/two_walkways_diagonal_1", pottedPlants), 4),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/terminal/two_walkways_diagonal_2", pottedPlants), 4),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/terminal/three_walkways", pottedPlants), 1),
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/terminal/three_walkways_flipped", pottedPlants), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        register(
                context,
                "tower/top",
                new StructureTemplatePool(
                        towerFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:end_city/tower/top", pottedPlants), 10)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );
    }

    private static ResourceKey<StructureTemplatePool> localized(String name) {
        return registerKey("end_city/" + name);
    }

    private static void register(BootstrapContext<StructureTemplatePool> context, String string, StructureTemplatePool pool) {
        register(context, localized(string), pool);
    }
}