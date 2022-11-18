package net.bunten.enderscape.client.hud;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;

@Environment(EnvType.CLIENT)
public abstract class HudElement extends GuiComponent {

    public final RenderPhase phase;

    public HudElement(RenderPhase phase) {
        this.phase = phase;
    }

    protected final Minecraft client = Minecraft.getInstance();
    protected int width;
    protected int height;

    public abstract void render(PoseStack matrix, float delta);

    public void tick() {
        width = client.getWindow().getGuiScaledWidth();
        height = client.getWindow().getGuiScaledHeight();
    }
}