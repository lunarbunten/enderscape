package net.bunten.enderscape.client.registry;

import net.bunten.enderscape.client.block.BlinklightColorProvider;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;

public class EnderscapeBlockColorProviders {

    static {
        ColorProviderRegistry.BLOCK.register(new BlinklightColorProvider(), BLINKLAMP, BLINKLIGHT_VINES_BODY, BLINKLIGHT_VINES_HEAD);
    }
}