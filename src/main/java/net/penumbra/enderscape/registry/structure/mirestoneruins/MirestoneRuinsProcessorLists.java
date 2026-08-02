package net.penumbra.enderscape.registry.structure.mirestoneruins;

import com.google.common.collect.ImmutableList;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.penumbra.enderscape.registry.structure.EnderscapeProcessorLists;
import org.jspecify.annotations.NonNull;

import static net.penumbra.enderscape.registry.block.EnderscapeBlocks.*;

public class MirestoneRuinsProcessorLists extends EnderscapeProcessorLists {

    public static final ResourceKey<StructureProcessorList> DEGRADATION = localized("degradation");

    public static void bootstrap(BootstrapContext<StructureProcessorList> context) {
        register(
                context,
                DEGRADATION,
                ImmutableList.of(
                        new RuleProcessor(
                                multiple(
                                        copyProperties(POLISHED_MIRESTONE_SLAB, MIRESTONE_SLAB, 0.2F),
                                        copyProperties(POLISHED_MIRESTONE_STAIRS, MIRESTONE_STAIRS, 0.2F),
                                        copyProperties(POLISHED_MIRESTONE_WALL, MIRESTONE_WALL, 0.2F),

                                        copyProperties(MIRESTONE_BRICK_SLAB, MIRESTONE_SLAB, 0.2F),
                                        copyProperties(MIRESTONE_BRICK_STAIRS, MIRESTONE_STAIRS, 0.2F),
                                        copyProperties(MIRESTONE_BRICK_WALL, MIRESTONE_WALL, 0.2F),

                                        copyProperties(MIRESTONE_BRICK_SLAB, OVERGROWN_MIRESTONE_BRICK_SLAB, 0.4F),
                                        copyProperties(MIRESTONE_BRICK_STAIRS, OVERGROWN_MIRESTONE_BRICK_STAIRS, 0.4F),
                                        copyProperties(MIRESTONE_BRICK_WALL, OVERGROWN_MIRESTONE_BRICK_WALL, 0.4F),

                                        ImmutableList.of(
                                                new ProcessorRule(new RandomBlockMatchTest(MIRESTONE_BRICKS, 0.4F), AlwaysTrueTest.INSTANCE, CRACKED_MIRESTONE_BRICKS.defaultBlockState()),
                                                new ProcessorRule(new RandomBlockMatchTest(MIRESTONE_BRICKS, 0.4F), AlwaysTrueTest.INSTANCE, OVERGROWN_MIRESTONE_BRICKS.defaultBlockState()),

                                                mirestoneRubble(CHISELED_MIRESTONE, 0.2F),
                                                mirestoneRubble(CORRUPT_OVERGROWTH, 0.2F),
                                                mirestoneRubble(MIRESTONE_BRICKS, 0.2F),
                                                mirestoneRubble(POLISHED_MIRESTONE, 0.2F)
                                        )
                                )
                        )
                )
        );
    }

    private static @NonNull ProcessorRule mirestoneRubble(Block block, float probability) {
        return new ProcessorRule(new RandomBlockMatchTest(block, probability), AlwaysTrueTest.INSTANCE, MIRESTONE.defaultBlockState());
    }

    private static ResourceKey<StructureProcessorList> localized(String name) {
        return registerKey("mirestone_ruins/" + name);
    }
}