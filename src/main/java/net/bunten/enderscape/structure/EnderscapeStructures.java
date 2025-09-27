package net.bunten.enderscape.structure;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.ArrayList;
import java.util.List;

public class EnderscapeStructures {

    public static final List<ResourceKey<Structure>> STRUCTURES = new ArrayList<ResourceKey<Structure>>();

    //public static final ResourceKey<Structure> END_CITY = register("end_city");
    //public static final ResourceKey<Structure> MIRESTONE_RUINS = register("mirestone_ruins");

    //public static final ResourceKey<Structure> CENTER_GATEWAY = register("center_gateway");
    //public static final ResourceKey<Structure> LARGE_CENTER_GATEWAY = register("large_center_gateway");

    public static void bootstrap(BootstrapContext<Structure> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
    }

    private static ResourceKey<Structure> register(String string) {
        ResourceKey<Structure> key = ResourceKey.create(Registries.STRUCTURE, Enderscape.id(string));
        STRUCTURES.add(key);
        return key;
    }
}
