package net.bunten.enderscape.client.hud;

import net.bunten.enderscape.EnderscapeConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public abstract class HudElement {

    public final RenderPhase phase;
    public final ResourceLocation id;

    public HudElement(ResourceLocation id, RenderPhase phase) {
        this.phase = phase;
        this.id = id;
    }

    protected final Minecraft client = Minecraft.getInstance();
    protected final EnderscapeConfig config = EnderscapeConfig.getInstance();

    public abstract void render(GuiGraphics graphics, DeltaTracker delta);

    public void tick() {}

    protected final int white(float alpha) {
        return (Mth.floor(alpha * 255.0) << 24) | (Mth.floor(255.0) << 16) | (Mth.floor(255.0) << 8) | Mth.floor(255.0);
    }

    public enum RenderPhase {
        BEFORE_HUD,
        AFTER_HUD
    }
}