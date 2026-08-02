package net.penumbra.enderscape.registry.gui;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.resources.Identifier;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.gui.hud.*;
import net.penumbra.enderscape.gui.value.RenderPhase;

import java.util.ArrayList;
import java.util.List;

public class EnderscapeHudElements {

    public static final List<EnderscapeHudElement> HUD_ELEMENTS = new ArrayList<>();

    public static final EnderscapeHudElement LODESTONE_TELEPORTATION_SCREEN_EFFECT = create(
            "lodestone_teleportation_screen_effect",
            VanillaHudElements.MISC_OVERLAYS,
            RenderPhase.BEFORE,
            new LodestoneTeleportationScreenEffect()
    );

    public static final EnderscapeHudElement VOID_NOISE_SCREEN_EFFECT = create(
            "void_noise_screen_effect",
            VanillaHudElements.MISC_OVERLAYS,
            RenderPhase.BEFORE,
            new VoidNoiseScreenEffect()
    );

    public static final EnderscapeHudElement OUTER_VOID_WARNING_SCREEN_EFFECT = create(
            "outer_void_warning_screen_effect",
            VanillaHudElements.MISC_OVERLAYS,
            RenderPhase.BEFORE,
            new OuterVoidWarningScreenEffect()
    );

    public static final EnderscapeHudElement FUELED_TOOL_HUD = create(
            "fueled_tool_hud",
            VanillaHudElements.BOSS_BAR,
            RenderPhase.AFTER,
            new FueledToolHud()
    );

    private static EnderscapeHudElement create(String name, Identifier vanillaElement, RenderPhase phase, EnderscapeHudElement element) {
        HUD_ELEMENTS.add(element);

        switch (phase) {
            case BEFORE -> HudElementRegistry.attachElementBefore(
                    vanillaElement,
                    Enderscape.id(name),
                    element
            );
            case AFTER -> HudElementRegistry.attachElementAfter(
                    vanillaElement,
                    Enderscape.id(name),
                    element
            );
        }

        ClientTickEvents.START_CLIENT_TICK.register(minecraft -> element.baseTick());

        return element;
    }
}