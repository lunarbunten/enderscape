package net.bunten.enderscape.client.registry;

import net.bunten.enderscape.client.EnderscapeClient;
import net.bunten.enderscape.client.hud.HudElement;
import net.bunten.enderscape.client.hud.LodestoneTeleportationScreenEffect;
import net.bunten.enderscape.client.hud.FueledToolHud;
import net.bunten.enderscape.client.hud.StareScreenEffect;

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
        EnderscapeHudElements.register(new LodestoneTeleportationScreenEffect());
        EnderscapeHudElements.register(new FueledToolHud());
        EnderscapeHudElements.register(new StareScreenEffect());
    }
}