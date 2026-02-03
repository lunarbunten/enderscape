package net.bunten.enderscape.structure;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.ArrayList;
import java.util.List;

public class EnderscapeTemplatePools {

    public static final List<ResourceKey<StructureTemplatePool>> TEMPLATE_POOLS = new ArrayList<>();

    public static ResourceKey<StructureTemplatePool> createKey(String string) {
        return ResourceKey.create(Registries.TEMPLATE_POOL, Enderscape.id(string));
    }

    public static void register(BootstrapContext<StructureTemplatePool> context, ResourceKey<StructureTemplatePool> key, StructureTemplatePool pool) {
        TEMPLATE_POOLS.add(key);
        context.register(key, pool);
    }

    public static void register(BootstrapContext<StructureTemplatePool> context, String string, StructureTemplatePool pool) {
        register(context, createKey(string), pool);
    }

    public void bootstrap(BootstrapContext<StructureTemplatePool> context) {
        StrongholdTemplatePools.bootstrap(context);
    }
}