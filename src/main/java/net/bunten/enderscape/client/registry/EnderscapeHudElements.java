package net.bunten.enderscape.client.registry;

import net.bunten.enderscape.client.EnderscapeClient;
import net.bunten.enderscape.client.hud.*;

import java.util.Objects;

public class EnderscapeHudElements {

    public static void register(HudElement element) {
        Objects.requireNonNull(element);
        EnderscapeClient.HUD_ELEMENTS.add(element);
    }

    public static void register(HudElement... elements) {
        for (HudElement element : elements) register(element);
    }

    static {
        EnderscapeHudElements.register(new DebugHud());
        EnderscapeHudElements.register(new MirrorScreenEffect());
        EnderscapeHudElements.register(new NebuliteToolHud());
        EnderscapeHudElements.register(new StareScreenEffect());
    }
}