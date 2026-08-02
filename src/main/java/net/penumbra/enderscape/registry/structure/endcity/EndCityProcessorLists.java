package net.penumbra.enderscape.registry.structure.endcity;

import com.google.common.collect.ImmutableList;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.penumbra.enderscape.registry.structure.EnderscapeProcessorLists;
import org.jspecify.annotations.NonNull;

import static net.penumbra.enderscape.registry.block.EnderscapeBlocks.*;

public class EndCityProcessorLists extends EnderscapeProcessorLists {

    public static final ResourceKey<StructureProcessorList> POTTED_PLANTS = localized("potted_plants");
    public static final ResourceKey<StructureProcessorList> START_PLATFORM_MODIFICATION = localized("start_platform_modification");

    public static void bootstrap(BootstrapContext<StructureProcessorList> context) {
        register(
                context,
                POTTED_PLANTS,
                ImmutableList.of(
                        new RuleProcessor(
                                multiple(
                                        ImmutableList.of(
                                                flowerPot(0.025F, POTTED_BLINKLIGHT),
                                                flowerPot(0.1F, POTTED_BULB_FLOWER),
                                                flowerPot(0.2F, POTTED_CELESTIAL_GROWTH),
                                                flowerPot(0.15F, POTTED_REPULSIVE_MAGNIA_SPROUT),
                                                flowerPot(0.3F, POTTED_CORRUPT_GROWTH),
                                                flowerPot(0.5F, POTTED_ALLURING_MAGNIA_SPROUT),
                                                flowerPot(1.0F, POTTED_CHORUS_SPROUTS)
                                        )
                                )
                        )
                )
        );

        register(
                context,
                START_PLATFORM_MODIFICATION,
                ImmutableList.of(
                        new RuleProcessor(
                                ImmutableList.of(
                                        startPlatformReplacement(MIRESTONE),
                                        startPlatformReplacement(VEILED_END_STONE),
                                        startPlatformReplacement(CELESTIAL_OVERGROWTH),
                                        startPlatformReplacement(VERADITE),
                                        startPlatformReplacement(KURODITE),
                                        startPlatformReplacement(ALLURING_MAGNIA),
                                        startPlatformReplacement(REPULSIVE_MAGNIA)
                                )
                        )
                )
        );
    }

    private static @NonNull ProcessorRule startPlatformReplacement(Block block) {
        return new ProcessorRule(
                new BlockMatchTest(Blocks.END_STONE),
                new BlockMatchTest(block),
                block.defaultBlockState()
        );
    }

    private static @NonNull ProcessorRule flowerPot(float probability, Block replacement) {
        return new ProcessorRule(new RandomBlockMatchTest(Blocks.FLOWER_POT, probability), AlwaysTrueTest.INSTANCE, replacement.defaultBlockState());
    }

    private static ResourceKey<StructureProcessorList> localized(String name) {
        return registerKey("end_city/" + name);
    }
}