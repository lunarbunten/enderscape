package net.bunten.enderscape.client.registry;

import net.bunten.enderscape.client.block.BlinklightColorProvider;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;

import java.util.List;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;

public class EnderscapeBlockColorProviders {

    static {
        BlockColorRegistry.register(List.of(new BlinklightColorProvider()), BLINKLAMP, BLINKLIGHT_VINES_BODY, BLINKLIGHT_VINES_HEAD);
    }
}