package net.penumbra.enderscape.registry.structure;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.registry.structure.endcity.EndCityProcessorLists;
import net.penumbra.enderscape.registry.structure.endhaven.EndHavenProcessorLists;
import net.penumbra.enderscape.registry.structure.mirestoneruins.MirestoneRuinsProcessorLists;
import net.penumbra.enderscape.registry.structure.stronghold.StrongholdProcessorLists;

import java.util.ArrayList;
import java.util.List;

public abstract class EnderscapeProcessorLists {

    public static final List<ResourceKey<StructureProcessorList>> PROCESSOR_LISTS = new ArrayList<>();

    public static void bootstrap(BootstrapContext<StructureProcessorList> context) {
        StrongholdProcessorLists.bootstrap(context);
        EndCityProcessorLists.bootstrap(context);
        EndHavenProcessorLists.bootstrap(context);
        MirestoneRuinsProcessorLists.bootstrap(context);
    }

    protected static ResourceKey<StructureProcessorList> registerKey(String string) {
        ResourceKey<StructureProcessorList> key = ResourceKey.create(Registries.PROCESSOR_LIST, Enderscape.id(string));
        PROCESSOR_LISTS.add(key);
        return key;
    }

    protected static void register(BootstrapContext<StructureProcessorList> context, ResourceKey<StructureProcessorList> key, List<StructureProcessor> list) {
        context.register(key, new StructureProcessorList(list));
    }

    @SafeVarargs
    protected static ImmutableList<ProcessorRule> multiple(ImmutableList<ProcessorRule>... lists) {
        ImmutableList.Builder<ProcessorRule> builder = ImmutableList.builder();
        for (ImmutableList<ProcessorRule> list : lists) builder.addAll(list);
        return builder.build();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected static ImmutableList<ProcessorRule> copyProperties(Block replaced, Block replacement, float chance) {
        BlockState replacedState = replaced.defaultBlockState();
        BlockState replacementState = replacement.defaultBlockState();

        ImmutableList.Builder<ProcessorRule> rules = ImmutableList.builder();

        for (Property<?> property : replacedState.getProperties()) {
            if (!replacementState.hasProperty(property)) continue;

            for (Comparable value : property.getPossibleValues()) {
                rules.add(new ProcessorRule(
                        new RandomBlockStateMatchTest(replacedState.setValue((Property) property, value), chance),
                        AlwaysTrueTest.INSTANCE,
                        replacementState.setValue((Property) property, value)
                ));
            }
        }

        return rules.build();
    }
}