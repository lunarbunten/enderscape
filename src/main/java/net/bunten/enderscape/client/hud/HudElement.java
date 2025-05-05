package net.bunten.enderscape.client.hud;

import net.bunten.enderscape.EnderscapeConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public abstract class HudElement {

    public final RenderPhase phase;

    public HudElement(RenderPhase phase) {
        this.phase = phase;
        
        if (phase == RenderPhase.AFTER_HUD) HudRenderCallback.EVENT.register(this::render);
        ClientTickEvents.START_CLIENT_TICK.register(client -> tick());
    }

    protected final Minecraft client = Minecraft.getInstance();
    protected final EnderscapeConfig config = EnderscapeConfig.getInstance();

    public abstract void render(GuiGraphics graphics, DeltaTracker delta);

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