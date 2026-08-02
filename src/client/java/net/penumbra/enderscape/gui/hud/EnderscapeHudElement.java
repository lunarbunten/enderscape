package net.penumbra.enderscape.gui.hud;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.penumbra.enderscape.EnderscapeClient;
import net.penumbra.enderscape.config.EnderscapeConfig;
import net.penumbra.enderscape.manager.ClientsideVariables;

@Environment(EnvType.CLIENT)
public abstract class EnderscapeHudElement implements HudElement {

    protected final Minecraft minecraft = Minecraft.getInstance();
    protected final EnderscapeConfig config = EnderscapeConfig.getInstance();
    protected final ClientsideVariables variables = EnderscapeClient.clientsideVariables();

    private boolean shouldReset;

    public void baseTick() {
        if (shouldTick()) {
            tick();
        }
    }

    public void tick() {
        LocalPlayer player = minecraft.player;

        boolean playerMissing = player == null || player.isDeadOrDying();

        if (shouldReset && !playerMissing) {
            reset();
        }

        shouldReset = playerMissing;
    }

    public boolean shouldTick() {
        return !minecraft.isPaused() && minecraft.level != null && minecraft.player != null;
    }

    public abstract void reset();
}