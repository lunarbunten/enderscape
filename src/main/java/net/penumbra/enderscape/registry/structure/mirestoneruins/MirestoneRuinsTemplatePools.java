package net.penumbra.enderscape.registry.structure.mirestoneruins;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
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

public class MirestoneRuinsTemplatePools extends EnderscapeTemplatePools {

    public static final ResourceKey<StructureTemplatePool> START = localized("start");
    public static final ResourceKey<StructureTemplatePool> STRUCTURE_FALLBACK = localized("structure_fallback");
    public static final ResourceKey<StructureTemplatePool> WALKWAY_FALLBACK = localized("walkway_fallback");

    public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
        HolderGetter<StructureProcessorList> processors = context.lookup(Registries.PROCESSOR_LIST);
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);

        Holder<StructureTemplatePool> emptyFallback = pools.getOrThrow(Pools.EMPTY);
        Holder.Reference<StructureTemplatePool> walkwayFallback = pools.getOrThrow(WALKWAY_FALLBACK);
        Holder.Reference<StructureTemplatePool> structureFallback = pools.getOrThrow(STRUCTURE_FALLBACK);

        Holder.Reference<StructureProcessorList> degradation = processors.getOrThrow(MirestoneRuinsProcessorLists.DEGRADATION);
        
        register(
                context,
                "walkway",
                new StructureTemplatePool(
                        walkwayFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/bunker/entrance", degradation).apply(StructureTemplatePool.Projection.RIGID), 2),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/walkway/end1", degradation).apply(StructureTemplatePool.Projection.TERRAIN_MATCHING), 2),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/walkway/end2", degradation).apply(StructureTemplatePool.Projection.TERRAIN_MATCHING), 2),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/walkway/straight", degradation).apply(StructureTemplatePool.Projection.TERRAIN_MATCHING), 6),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/walkway/straight_crumbled1", degradation).apply(StructureTemplatePool.Projection.TERRAIN_MATCHING), 2),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/walkway/straight_crumbled2", degradation).apply(StructureTemplatePool.Projection.TERRAIN_MATCHING), 2),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/walkway/structure_on_left", degradation).apply(StructureTemplatePool.Projection.TERRAIN_MATCHING), 9),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/walkway/structure_on_right", degradation).apply(StructureTemplatePool.Projection.TERRAIN_MATCHING), 9)
                        )
                )
        );

        register(
                context,
                WALKWAY_FALLBACK,
                new StructureTemplatePool(
                        emptyFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/walkway/end1", degradation).apply(StructureTemplatePool.Projection.TERRAIN_MATCHING), 5),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/walkway/end2", degradation).apply(StructureTemplatePool.Projection.TERRAIN_MATCHING), 5),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/walkway/fallback", degradation).apply(StructureTemplatePool.Projection.RIGID), 1)
                        )
                )
        );

        register(
                context,
                "walkway_or_gate",
                new StructureTemplatePool(
                        walkwayFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/bunker/entrance", degradation).apply(StructureTemplatePool.Projection.RIGID), 2),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/walkway/end1", degradation).apply(StructureTemplatePool.Projection.TERRAIN_MATCHING), 2),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/walkway/end2", degradation).apply(StructureTemplatePool.Projection.TERRAIN_MATCHING), 2),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/walkway/split", degradation).apply(StructureTemplatePool.Projection.RIGID), 2),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/walkway/straight", degradation).apply(StructureTemplatePool.Projection.TERRAIN_MATCHING), 6),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/walkway/straight_crumbled1", degradation).apply(StructureTemplatePool.Projection.TERRAIN_MATCHING), 2),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/walkway/straight_crumbled2", degradation).apply(StructureTemplatePool.Projection.TERRAIN_MATCHING), 2),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/walkway/structure_on_left", degradation).apply(StructureTemplatePool.Projection.TERRAIN_MATCHING), 9),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/walkway/structure_on_right", degradation).apply(StructureTemplatePool.Projection.TERRAIN_MATCHING), 9),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/walkway/turn_left", degradation).apply(StructureTemplatePool.Projection.RIGID), 3),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/walkway/turn_right", degradation).apply(StructureTemplatePool.Projection.RIGID), 3)
                        )
                )
        );

        register(
                context,
                "bunker_interior",
                new StructureTemplatePool(
                        emptyFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/bunker/blinklamps").apply(StructureTemplatePool.Projection.RIGID), 3),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/bunker/endermite").apply(StructureTemplatePool.Projection.RIGID), 2)
                        )
                )
        );

        register(
                context,
                START,
                new StructureTemplatePool(
                        emptyFallback,
                        ImmutableList.of(Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/start", degradation).apply(StructureTemplatePool.Projection.RIGID), 1))
                )
        );

        register(
                context,
                "structure",
                new StructureTemplatePool(
                        structureFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/structure/one_story_corner_house", degradation).apply(StructureTemplatePool.Projection.RIGID), 3),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/structure/one_story_house", degradation).apply(StructureTemplatePool.Projection.RIGID), 3),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/structure/one_story_house_tower", degradation).apply(StructureTemplatePool.Projection.RIGID), 3),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/structure/shrine", degradation).apply(StructureTemplatePool.Projection.RIGID), 3),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/structure/two_story_house", degradation).apply(StructureTemplatePool.Projection.RIGID), 3),
                                Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/structure/two_story_house_ruined", degradation).apply(StructureTemplatePool.Projection.RIGID), 3)
                        )
                )
        );

        register(
                context,
                STRUCTURE_FALLBACK,
                new StructureTemplatePool(
                        emptyFallback,
                        ImmutableList.of(Pair.of(StructurePoolElement.single("enderscape:mirestone_ruins/structure/fallback", degradation).apply(StructureTemplatePool.Projection.RIGID), 1))
                )
        );
    }

    private static ResourceKey<StructureTemplatePool> localized(String name) {
        return registerKey("mirestone_ruins/" + name);
    }

    private static void register(BootstrapContext<StructureTemplatePool> context, String string, StructureTemplatePool pool) {
        register(context, localized(string), pool);
    }
}