package net.bunten.enderscape.client.registry;

import net.bunten.enderscape.client.block.BlinklightColorProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import static net.bunten.enderscape.registry.EnderscapeBlocks.*;

public class EnderscapeBlockColorProviders {

    @SubscribeEvent
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register(new BlinklightColorProvider(), BLINKLAMP.get(), BLINKLIGHT_VINES_BODY.get(), BLINKLIGHT_VINES_HEAD.get());
    }
}