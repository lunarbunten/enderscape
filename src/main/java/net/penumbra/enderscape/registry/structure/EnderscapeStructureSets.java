package net.penumbra.enderscape.registry.structure;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.penumbra.enderscape.Enderscape;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

import static net.penumbra.enderscape.registry.structure.EnderscapeStructures.*;

public class EnderscapeStructureSets {

    public static final List<ResourceKey<StructureSet>> STRUCTURE_SETS = new ArrayList<>();

    public static final ResourceKey<StructureSet> GATEWAYS = register("gateways");
    public static final ResourceKey<StructureSet> RUINS = register("ruins");

    public static void bootstrap(BootstrapContext<StructureSet> context) {
        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);

        context.register(GATEWAYS, new StructureSet(
                List.of(
                        getEntry(structures, LARGE_CENTER_GATEWAY, 1),
                        getEntry(structures, CENTER_GATEWAY, 4),
                        getEntry(structures, END_HAVEN, 2)
                ),
                new RandomSpreadStructurePlacement(
                        24,
                        12,
                        RandomSpreadType.TRIANGULAR,
                        69210808
                )
        ));

        context.register(RUINS, new StructureSet(
                List.of(
                        getEntry(structures, MIRESTONE_RUINS, 1)
                ),
                new RandomSpreadStructurePlacement(
                        24,
                        8,
                        RandomSpreadType.LINEAR,
                        95286963
                )
        ));
    }

    private static StructureSet.@NonNull StructureSelectionEntry getEntry(HolderGetter<Structure> structures, ResourceKey<Structure> structure, int weight) {
        return StructureSet.entry(structures.getOrThrow(structure), weight);
    }

    private static ResourceKey<StructureSet> register(String string) {
        ResourceKey<StructureSet> key = ResourceKey.create(Registries.STRUCTURE_SET, Enderscape.id(string));
        STRUCTURE_SETS.add(key);
        return key;
    }
}