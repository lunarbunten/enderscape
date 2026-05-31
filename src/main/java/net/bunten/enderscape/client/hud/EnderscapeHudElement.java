package net.bunten.enderscape.client.hud;

import net.bunten.enderscape.EnderscapeConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public abstract class EnderscapeHudElement implements HudElement {

    public final RenderPhase phase;

    public EnderscapeHudElement(RenderPhase phase, Identifier identifier) {
        this.phase = phase;
        
        if (phase == RenderPhase.AFTER_HUD) HudElementRegistry.attachElementAfter(VanillaHudElements.HOTBAR, identifier, this);
        ClientTickEvents.START_CLIENT_TICK.register(client -> tick());
    }

    protected final Minecraft client = Minecraft.getInstance();
    protected final EnderscapeConfig config = EnderscapeConfig.getInstance();

    public abstract void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker delta);

    public void tick() {}

    protected final int white(float alpha) {
        return (Mth.floor(alpha * 255.0) << 24) | (Mth.floor(255.0) << 16) | (Mth.floor(255.0) << 8) | Mth.floor(255.0);
    }

    @Environment(EnvType.CLIENT)
    public enum RenderPhase {
        BEFORE_HUD,
        AFTER_HUD
    }
}