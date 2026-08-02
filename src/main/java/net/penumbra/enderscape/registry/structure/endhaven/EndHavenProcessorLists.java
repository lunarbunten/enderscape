package net.penumbra.enderscape.registry.structure.endhaven;

import com.google.common.collect.ImmutableList;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.penumbra.enderscape.registry.structure.EnderscapeProcessorLists;
import org.jspecify.annotations.NonNull;

import static net.minecraft.world.level.block.Blocks.*;
import static net.penumbra.enderscape.registry.block.EnderscapeBlocks.*;

public class EndHavenProcessorLists extends EnderscapeProcessorLists {

    public static final ResourceKey<StructureProcessorList> DEGRADATION = localized("degradation");

    public static void bootstrap(BootstrapContext<StructureProcessorList> context) {
        register(
                context,
                DEGRADATION,
                ImmutableList.of(
                        getLightDegradation(),
                        new RuleProcessor(
                                multiple(
                                        copyProperties(POLISHED_END_STONE_SLAB, END_STONE_SLAB, 0.1F),
                                        copyProperties(POLISHED_END_STONE_STAIRS, END_STONE_STAIRS, 0.1F),
                                        copyProperties(POLISHED_END_STONE_WALL, END_STONE_WALL, 0.1F),

                                        copyProperties(END_STONE_BRICK_SLAB, END_STONE_SLAB, 0.1F),
                                        copyProperties(END_STONE_BRICK_STAIRS, END_STONE_STAIRS, 0.1F),
                                        copyProperties(END_STONE_BRICK_WALL, END_STONE_WALL, 0.1F),

                                        copyProperties(END_STONE_BRICK_SLAB, OVERGROWN_END_STONE_BRICK_SLAB, 0.2F),
                                        copyProperties(END_STONE_BRICK_STAIRS, OVERGROWN_END_STONE_BRICK_STAIRS, 0.2F),
                                        copyProperties(END_STONE_BRICK_WALL, OVERGROWN_END_STONE_BRICK_WALL, 0.2F),

                                        ImmutableList.of(
                                                new ProcessorRule(new RandomBlockMatchTest(END_STONE_BRICKS, 0.2F), AlwaysTrueTest.INSTANCE, CRACKED_END_STONE_BRICKS.defaultBlockState()),
                                                new ProcessorRule(new RandomBlockMatchTest(END_STONE_BRICKS, 0.2F), AlwaysTrueTest.INSTANCE, OVERGROWN_END_STONE_BRICKS.defaultBlockState()),

                                                endStoneRubble(CHISELED_END_STONE, 0.1F),
                                                endStoneRubble(END_STONE_BRICKS, 0.1F),
                                                endStoneRubble(POLISHED_END_STONE, 0.1F)
                                        )
                                )
                        )
                )
        );
    }

    private static RuleProcessor getLightDegradation() {
        return new RuleProcessor(
                ImmutableList.of(
                        new ProcessorRule(
                                new RandomBlockStateMatchTest(magentaCandleState(true, 4), 0.4F),
                                AlwaysTrueTest.INSTANCE,
                                magentaCandleState(false, 4)
                        ),
                        new ProcessorRule(
                                new RandomBlockStateMatchTest(magentaCandleState(true, 3), 0.4F),
                                AlwaysTrueTest.INSTANCE,
                                magentaCandleState(false, 3)
                        ),
                        new ProcessorRule(
                                new RandomBlockStateMatchTest(magentaCandleState(true, 2), 0.4F),
                                AlwaysTrueTest.INSTANCE,
                                magentaCandleState(false, 2)
                        ),
                        new ProcessorRule(
                                new RandomBlockStateMatchTest(magentaCandleState(true, 1), 0.4F),
                                AlwaysTrueTest.INSTANCE,
                                magentaCandleState(false, 1)
                        )
                )
        );
    }

    private static BlockState magentaCandleState(boolean lit, int candles) {
        return Blocks.MAGENTA_CANDLE.defaultBlockState().setValue(CandleBlock.LIT, lit).setValue(CandleBlock.CANDLES, candles);
    }

    private static @NonNull ProcessorRule endStoneRubble(Block block, float probability) {
        return new ProcessorRule(new RandomBlockMatchTest(block, probability), AlwaysTrueTest.INSTANCE, END_STONE.defaultBlockState());
    }

    private static ResourceKey<StructureProcessorList> localized(String name) {
        return registerKey("end_haven/" + name);
    }
}