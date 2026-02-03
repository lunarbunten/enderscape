package net.bunten.enderscape.structure;

import net.bunten.enderscape.Enderscape;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.ArrayList;
import java.util.List;

public class EnderscapeProcessorLists {

    public static final List<ResourceKey<StructureProcessorList>> PROCESSOR_LISTS = new ArrayList<>();

    public static ResourceKey<StructureProcessorList> registerKey(String string) {
        ResourceKey<StructureProcessorList> key = ResourceKey.create(Registries.PROCESSOR_LIST, Enderscape.id(string));
        PROCESSOR_LISTS.add(key);
        return key;
    }

    public static void register(BootstrapContext<StructureProcessorList> context, ResourceKey<StructureProcessorList> key, List<StructureProcessor> list) {
        context.register(key, new StructureProcessorList(list));
    }

    public void bootstrap(BootstrapContext<StructureProcessorList> context) {
        StrongholdProcessorLists.bootstrap(context);
    }

    public static void addSlabReplacements(List<ProcessorRule> rules, Block original, Block replacement) {
        for (SlabType type : SlabBlock.TYPE.getPossibleValues()) {
            for (boolean waterlogged : SlabBlock.WATERLOGGED.getPossibleValues()) {
                rules.add(
                        new ProcessorRule(
                                new BlockStateMatchTest(original.defaultBlockState().setValue(SlabBlock.TYPE, type).setValue(SlabBlock.WATERLOGGED, waterlogged)),
                                AlwaysTrueTest.INSTANCE,
                                replacement.defaultBlockState().setValue(SlabBlock.TYPE, type).setValue(SlabBlock.WATERLOGGED, waterlogged)
                        )
                );
            }
        }
    }

    public static void addStairsReplacements(List<ProcessorRule> rules, Block original, Block replacement) {
        for (Direction facing : StairBlock.FACING.getPossibleValues()) {
            for (Half half : StairBlock.HALF.getPossibleValues()) {
                for (StairsShape shape : StairBlock.SHAPE.getPossibleValues()) {
                    for (boolean waterlogged : StairBlock.WATERLOGGED.getPossibleValues()) {
                        rules.add(
                                new ProcessorRule(
                                        new BlockStateMatchTest(original.defaultBlockState().setValue(StairBlock.FACING, facing).setValue(StairBlock.HALF, half).setValue(StairBlock.SHAPE, shape).setValue(StairBlock.WATERLOGGED, waterlogged)),
                                        AlwaysTrueTest.INSTANCE,
                                        replacement.defaultBlockState().setValue(StairBlock.FACING, facing).setValue(StairBlock.HALF, half).setValue(StairBlock.SHAPE, shape).setValue(StairBlock.WATERLOGGED, waterlogged)
                                )
                        );
                    }
                }
            }
        }
    }
}