package net.penumbra.enderscape.registry.structure;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.registry.structure.endcity.EndCityTemplatePools;
import net.penumbra.enderscape.registry.structure.endhaven.EndHavenTemplatePools;
import net.penumbra.enderscape.registry.structure.gateway.CenterGatewayTemplatePools;
import net.penumbra.enderscape.registry.structure.gateway.LargeCenterGatewayTemplatePools;
import net.penumbra.enderscape.registry.structure.mirestoneruins.MirestoneRuinsTemplatePools;
import net.penumbra.enderscape.registry.structure.stronghold.StrongholdTemplatePools;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EnderscapeTemplatePools {

    public static final List<ResourceKey<StructureTemplatePool>> TEMPLATE_POOLS = new ArrayList<>();

    public static void bootstrap(BootstrapContext<StructureTemplatePool> context) {
        StrongholdTemplatePools.bootstrap(context);
        EndCityTemplatePools.bootstrap(context);
        EndHavenTemplatePools.bootstrap(context);
        MirestoneRuinsTemplatePools.bootstrap(context);
        LargeCenterGatewayTemplatePools.bootstrap(context);
        CenterGatewayTemplatePools.bootstrap(context);
    }

    protected static ResourceKey<StructureTemplatePool> registerKey(String string) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, Enderscape.id(string));
    }

    protected static void register(BootstrapContext<StructureTemplatePool> context, ResourceKey<StructureTemplatePool> key, StructureTemplatePool pool) {
        TEMPLATE_POOLS.add(key);
        context.register(key, pool);
    }

    protected static ImmutableList<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> multiple(ImmutableList<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>... lists) {
        ImmutableList.Builder<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> builder = ImmutableList.builder();
        for (ImmutableList<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> list : lists) {
            builder.addAll(list);
        }
        return builder.build();
    }
}