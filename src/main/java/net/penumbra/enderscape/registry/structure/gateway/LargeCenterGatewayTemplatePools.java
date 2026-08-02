package net.penumbra.enderscape.registry.structure.gateway;

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

public class LargeCenterGatewayTemplatePools extends EnderscapeTemplatePools {

    public static final ResourceKey<StructureTemplatePool> START = localized("start");
    public static final ResourceKey<StructureTemplatePool> WALKWAY_FALLBACK = localized("walkway_fallback");

    public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
        HolderGetter<StructureProcessorList> processors = context.lookup(Registries.PROCESSOR_LIST);
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);

        Holder<StructureTemplatePool> emptyFallback = pools.getOrThrow(Pools.EMPTY);
        Holder.Reference<StructureTemplatePool> walkwayFallback = pools.getOrThrow(WALKWAY_FALLBACK);

        register(
                context,
                START,
                new StructureTemplatePool(
                        emptyFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:large_center_gateway/start"), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        register(
                context,
                "walkway_1",
                new StructureTemplatePool(
                        walkwayFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:large_center_gateway/walkway_1/stairs"), 1),
                                Pair.of(StructurePoolElement.single("enderscape:large_center_gateway/walkway_1/none"), 2)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        register(
                context,
                "walkway_2",
                new StructureTemplatePool(
                        walkwayFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:large_center_gateway/walkway_end/ruins"), 2),
                                Pair.of(StructurePoolElement.single("enderscape:large_center_gateway/walkway_2/turn_left"), 1),
                                Pair.of(StructurePoolElement.single("enderscape:large_center_gateway/walkway_2/turn_right"), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        register(
                context,
                "walkway_3",
                new StructureTemplatePool(
                        walkwayFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:large_center_gateway/walkway_3/stairs"), 1),
                                Pair.of(StructurePoolElement.single("enderscape:large_center_gateway/walkway_end/ruins"), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );

        register(
                context,
                "walkway_4",
                new StructureTemplatePool(
                        walkwayFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:large_center_gateway/walkway_end/ruins"), 1)
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
                                Pair.of(StructurePoolElement.single("enderscape:large_center_gateway/walkway_end/ruins"), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );
    }

    private static ResourceKey<StructureTemplatePool> localized(String name) {
        return registerKey("large_center_gateway/" + name);
    }

    private static void register(BootstrapContext<StructureTemplatePool> context, String string, StructureTemplatePool pool) {
        register(context, localized(string), pool);
    }
}