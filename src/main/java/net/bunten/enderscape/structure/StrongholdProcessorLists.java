package net.bunten.enderscape.structure;

import com.google.common.collect.ImmutableList;
import net.bunten.enderscape.block.state.StateProperties;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import org.jetbrains.annotations.NotNull;

public class StrongholdProcessorLists {

    public static final ResourceKey<StructureProcessorList> DEGRADATION = EnderscapeProcessorLists.registerKey("stronghold/degradation");
    public static final ResourceKey<StructureProcessorList> PORTAL_ROOM_DEGRADATION = EnderscapeProcessorLists.registerKey("stronghold/portal_room_degradation");
    public static final ResourceKey<StructureProcessorList> LIBRARY_DEGRADATION = EnderscapeProcessorLists.registerKey("stronghold/library_degradation");

    public static void bootstrap(BootstrapContext<StructureProcessorList> context) {
        EnderscapeProcessorLists.register(
                context,
                DEGRADATION,
                ImmutableList.of(
                        getStoneBrickDegradation(),
                        getLightDegradation()
                )
        );

        EnderscapeProcessorLists.register(
                context,
                PORTAL_ROOM_DEGRADATION,
                ImmutableList.of(
                        getStoneBrickDegradation(),
                        new RuleProcessor(
                                ImmutableList.of(
                                        new ProcessorRule(
                                                new RandomBlockStateMatchTest(portalFrameState(false, Direction.NORTH), 0.1F),
                                                AlwaysTrueTest.INSTANCE,
                                                portalFrameState(true, Direction.NORTH)
                                        ),
                                        new ProcessorRule(
                                                new RandomBlockStateMatchTest(portalFrameState(false, Direction.WEST), 0.1F),
                                                AlwaysTrueTest.INSTANCE,
                                                portalFrameState(true, Direction.WEST)
                                        ),
                                        new ProcessorRule(
                                                new RandomBlockStateMatchTest(portalFrameState(false, Direction.SOUTH), 0.1F),
                                                AlwaysTrueTest.INSTANCE,
                                                portalFrameState(true, Direction.SOUTH)
                                        ),
                                        new ProcessorRule(
                                                new RandomBlockStateMatchTest(portalFrameState(false, Direction.EAST), 0.1F),
                                                AlwaysTrueTest.INSTANCE,
                                                portalFrameState(true, Direction.EAST)
                                        )
                                )
                        )
                )
        );

        EnderscapeProcessorLists.register(
                context,
                LIBRARY_DEGRADATION,
                ImmutableList.of(
                        getStoneBrickDegradation(),
                        new RuleProcessor(
                                ImmutableList.of(
                                        new ProcessorRule(new RandomBlockMatchTest(Blocks.COBWEB, 0.1F), AlwaysTrueTest.INSTANCE, Blocks.AIR.defaultBlockState())
                                )
                        )
                )
        );
    }

    @NotNull
    private static RuleProcessor getStoneBrickDegradation() {
        return new RuleProcessor(
                multiple(
                        withAllProperties(Blocks.STONE_BRICK_SLAB, Blocks.MOSSY_STONE_BRICK_SLAB, 0.2F),
                        withAllProperties(Blocks.STONE_BRICK_STAIRS, Blocks.MOSSY_STONE_BRICK_STAIRS, 0.2F),
                        withAllProperties(Blocks.STONE_BRICK_WALL, Blocks.MOSSY_STONE_BRICK_WALL, 0.2F),

                        ImmutableList.of(
                                new ProcessorRule(new RandomBlockMatchTest(Blocks.STONE_BRICKS, 0.2F), AlwaysTrueTest.INSTANCE, Blocks.MOSSY_STONE_BRICKS.defaultBlockState()),
                                new ProcessorRule(new RandomBlockMatchTest(Blocks.STONE_BRICKS, 0.1F), AlwaysTrueTest.INSTANCE, Blocks.CRACKED_STONE_BRICKS.defaultBlockState()),

                                new ProcessorRule(new RandomBlockMatchTest(Blocks.STONE_BRICKS, 0.025F), AlwaysTrueTest.INSTANCE, Blocks.INFESTED_STONE_BRICKS.defaultBlockState()),
                                new ProcessorRule(new RandomBlockMatchTest(Blocks.STONE_BRICKS, 0.005F), AlwaysTrueTest.INSTANCE, Blocks.INFESTED_MOSSY_STONE_BRICKS.defaultBlockState()),
                                new ProcessorRule(new RandomBlockMatchTest(Blocks.STONE_BRICKS, 0.0025F), AlwaysTrueTest.INSTANCE, Blocks.INFESTED_CRACKED_STONE_BRICKS.defaultBlockState()),

                                new ProcessorRule(new RandomBlockMatchTest(Blocks.CHISELED_STONE_BRICKS, 0.005F), AlwaysTrueTest.INSTANCE, Blocks.INFESTED_CHISELED_STONE_BRICKS.defaultBlockState())
                        )
                )
        );
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static ImmutableList<ProcessorRule> withAllProperties(Block replaced, Block replacement, float chance) {
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

    @NotNull
    private static RuleProcessor getLightDegradation() {
        return new RuleProcessor(
                ImmutableList.of(
                        new ProcessorRule(new RandomBlockStateMatchTest(lanternState(false, false), 0.4F), AlwaysTrueTest.INSTANCE, Blocks.AIR.defaultBlockState()),
                        new ProcessorRule(new RandomBlockStateMatchTest(lanternState(true, false), 0.4F), AlwaysTrueTest.INSTANCE, Blocks.AIR.defaultBlockState()),
                        new ProcessorRule(new RandomBlockStateMatchTest(lanternState(false, true), 0.4F), AlwaysTrueTest.INSTANCE, Blocks.WATER.defaultBlockState()),
                        new ProcessorRule(new RandomBlockStateMatchTest(lanternState(true, true), 0.4F), AlwaysTrueTest.INSTANCE, Blocks.WATER.defaultBlockState()),
                        new ProcessorRule(
                                new RandomBlockStateMatchTest(blackCandleState(true, 4), 0.4F),
                                AlwaysTrueTest.INSTANCE,
                                blackCandleState(false, 4)
                        ),
                        new ProcessorRule(
                                new RandomBlockStateMatchTest(blackCandleState(true, 3), 0.4F),
                                AlwaysTrueTest.INSTANCE,
                                blackCandleState(false, 3)
                        ),
                        new ProcessorRule(
                                new RandomBlockStateMatchTest(blackCandleState(true, 2), 0.4F),
                                AlwaysTrueTest.INSTANCE,
                                blackCandleState(false, 2)
                        ),
                        new ProcessorRule(
                                new RandomBlockStateMatchTest(blackCandleState(true, 1), 0.4F),
                                AlwaysTrueTest.INSTANCE,
                                blackCandleState(false, 1)
                        )
                )
        );
    }

    @NotNull
    private static BlockState portalFrameState(boolean hasEye, Direction facing) {
        return Blocks.END_PORTAL_FRAME.defaultBlockState().setValue(EndPortalFrameBlock.HAS_EYE, hasEye).setValue(EndPortalFrameBlock.FACING, facing);
    }

    @NotNull
    private static BlockState lanternState(boolean hanging, boolean waterlogged) {
        return Blocks.LANTERN.defaultBlockState().setValue(StateProperties.HANGING, hanging).setValue(StateProperties.WATERLOGGED, waterlogged);
    }

    @NotNull
    private static BlockState blackCandleState(boolean lit, int candles) {
        return Blocks.DYED_CANDLE.black().defaultBlockState().setValue(CandleBlock.LIT, lit).setValue(CandleBlock.CANDLES, candles);
    }

    private static ImmutableList<ProcessorRule> multiple(ImmutableList<ProcessorRule>... lists) {
        ImmutableList.Builder<ProcessorRule> builder = ImmutableList.builder();
        for (ImmutableList<ProcessorRule> list : lists) builder.addAll(list);
        return builder.build();
    }
}