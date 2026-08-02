package net.penumbra.enderscape.manager;

import net.minecraft.resources.Identifier;
import net.minecraft.sounds.Music;
import net.penumbra.enderscape.gui.hud.EnderscapeHudElement;
import net.penumbra.enderscape.registry.gui.EnderscapeHudElements;
import net.penumbra.enderscape.sound.EndermanStareSoundInstance;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientsideVariables {

    public final List<Vector2i> playerHearts = new ArrayList<>();
    public final List<Vector2i> vehicleHearts = new ArrayList<>();

    public Optional<Music> structureMusic = Optional.empty();

    public Optional<Identifier> lodestoneTeleportationOverlayTexture = Optional.empty();
    public Optional<Identifier> lodestoneTeleportationVignetteTexture = Optional.empty();
    public int lodestoneTeleportationTicks = 0;

    @Nullable public EndermanStareSoundInstance stareSoundInstance = null;

    public int endermanAngerTicks = 0;

    public boolean displayVoidedTotemEffect = false;
    public float outerVoidIntensity = 0.0F;

    public final OuterVoidWarning playerOuterVoidWarning = new OuterVoidWarning(true);
    public final OuterVoidWarning vehicleOuterVoidWarning = new OuterVoidWarning(false);

    public void reset() {
        playerHearts.clear();
        vehicleHearts.clear();

        lodestoneTeleportationOverlayTexture = Optional.empty();
        lodestoneTeleportationVignetteTexture = Optional.empty();
        structureMusic = Optional.empty();
        lodestoneTeleportationTicks = 0;
        stareSoundInstance = null;
        endermanAngerTicks = 0;
        displayVoidedTotemEffect = false;
        outerVoidIntensity = 0;

        playerOuterVoidWarning.reset();
        vehicleOuterVoidWarning.reset();

        EnderscapeHudElements.HUD_ELEMENTS.forEach(EnderscapeHudElement::reset);
    }

    public static class OuterVoidWarning {

        public final boolean player;

        public float lastInterval = 0.0F;
        public float warningTimestamp = 0.0F;

        public OuterVoidWarning(boolean alertsPlayer) {
            this.player = alertsPlayer;
        }

        public void reset() {
            lastInterval = 0;
            warningTimestamp = 0;
        }
    }
}