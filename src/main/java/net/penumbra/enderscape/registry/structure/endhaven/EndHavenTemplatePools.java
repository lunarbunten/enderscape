package net.penumbra.enderscape.registry.structure.endhaven;

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

public class EndHavenTemplatePools extends EnderscapeTemplatePools {

    public static final ResourceKey<StructureTemplatePool> START = localized("start");

    public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
        HolderGetter<StructureProcessorList> processors = context.lookup(Registries.PROCESSOR_LIST);
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);

        Holder<StructureTemplatePool> emptyFallback = pools.getOrThrow(Pools.EMPTY);

        Holder.Reference<StructureProcessorList> degradation = processors.getOrThrow(EndHavenProcessorLists.DEGRADATION);

        register(
                context,
                START,
                new StructureTemplatePool(
                        emptyFallback,
                        ImmutableList.of(Pair.of(StructurePoolElement.single("enderscape:end_haven/start", degradation).apply(StructureTemplatePool.Projection.RIGID), 1))
                )
        );
    }

    private static ResourceKey<StructureTemplatePool> localized(String name) {
        return registerKey("end_haven/" + name);
    }

    private static void register(BootstrapContext<StructureTemplatePool> context, String string, StructureTemplatePool pool) {
        register(context, localized(string), pool);
    }
}