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
import net.penumbra.enderscape.registry.structure.EnderscapeTemplatePools;

public class CenterGatewayTemplatePools extends EnderscapeTemplatePools {

    public static final ResourceKey<StructureTemplatePool> START = localized("start");

    public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
        Holder<StructureTemplatePool> emptyFallback = pools.getOrThrow(Pools.EMPTY);

        register(
                context,
                START,
                new StructureTemplatePool(
                        emptyFallback,
                        ImmutableList.of(
                                Pair.of(StructurePoolElement.single("enderscape:center_gateway/start"), 1)
                        ),
                        StructureTemplatePool.Projection.RIGID
                )
        );
    }

    private static ResourceKey<StructureTemplatePool> localized(String name) {
        return registerKey("center_gateway/" + name);
    }

    private static void register(BootstrapContext<StructureTemplatePool> context, String string, StructureTemplatePool pool) {
        register(context, localized(string), pool);
    }
}