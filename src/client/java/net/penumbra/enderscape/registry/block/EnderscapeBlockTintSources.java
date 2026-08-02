package net.penumbra.enderscape.registry.block;

import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.penumbra.enderscape.renderer.block.NoiseBasedTintSource;
import net.penumbra.enderscape.util.EnderscapeColors;

import java.util.List;

import static net.penumbra.enderscape.registry.block.EnderscapeBlocks.*;

public class EnderscapeBlockTintSources {

    static {
        BlockColorRegistry.register(List.of(blinklight()), BLINKLAMP, BLINKLIGHT_VINES_BODY, BLINKLIGHT_VINES_HEAD);
    }

    private static NoiseBasedTintSource blinklight() {
        return new NoiseBasedTintSource(2022, 6.0, 0.2, 4, EnderscapeColors.BLINKLIGHT_LIGHT_BLUE, EnderscapeColors.BLINKLIGHT_LIGHT_PURPLE);
    }

    public static NoiseBasedTintSource voidLachryma() {
        return new NoiseBasedTintSource(2026, 6.0, 0.2, 4, EnderscapeColors.VOID_LACHRYMA_LIGHT_BLUE, EnderscapeColors.WHITE);
    }
}