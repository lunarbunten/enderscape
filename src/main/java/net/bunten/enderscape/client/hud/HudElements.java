package net.bunten.enderscape.client.hud;

import net.bunten.enderscape.Enderscape;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;

@Environment(EnvType.CLIENT)
public class HudElements {

    public static final MappedRegistry<HudElement> ELEMENTS = FabricRegistryBuilder.createSimple(HudElement.class, Enderscape.id("hud_element")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
    
    public static final HudElement DEBUG = register("debug", new DebugHud());
    public static final HudElement MIRROR_SCREEN_EFFECT = register("mirror_screen_effect", new MirrorScreenEffect());
    public static final HudElement NEBULITE_CHARGED_ITEM = register("nebulite_charged_item", new NebuliteChargedItemHud());

    private static HudElement register(String name, HudElement element) {
        return Registry.register(ELEMENTS, Enderscape.id(name), element);
    }

    public static void init() {
        ELEMENTS.forEach(element -> {
            if (element.phase == RenderPhase.AFTER_HUD) HudRenderCallback.EVENT.register(element::render);
            ClientTickEvents.START_CLIENT_TICK.register(client -> {
                if (!client.isPaused()) element.tick();
            });
        });
    }
}