package net.penumbra.enderscape.manager;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.penumbra.enderscape.EnderscapeClient;
import net.penumbra.enderscape.gui.value.HeartOverlay;
import net.penumbra.enderscape.registry.entity.EnderscapeMobEffects;
import net.penumbra.enderscape.registry.sound.EnderscapeUiSounds;
import net.penumbra.enderscape.registry.tag.EnderscapeFluidTags;
import org.joml.Vector2i;

import java.util.Arrays;
import java.util.List;

public class ClientsideVoidManager {

    public static final ClientsideVariables variables = EnderscapeClient.clientsideVariables();

    public static void extractVoidedHealthOverlay(GuiGraphicsExtractor graphics, LivingEntity entity, List<Vector2i> positions, boolean reversed, boolean hardcore, boolean blink) {
        if (VoidManager.hasVoidedHealth(entity)) {
            float voidedHealth = VoidManager.getVoidedHealth(entity);

            if (reversed) {
                positions = positions.reversed();
            }

            for (Vector2i position : positions) {
                if (voidedHealth > 0.0F) {
                    extractHeartOverlay(graphics, HeartOverlay.VOIDED, position, voidedHealth == 1, hardcore, reversed);

                    if (!entity.isDeadOrDying() && entity.hasEffect(EnderscapeMobEffects.VOID_PURIFICATION)) {
                        if (!blink) {
                            extractHeartOverlay(graphics, HeartOverlay.VOID_PURIFICATION_OUTLINE, position, voidedHealth == 1, false, reversed);
                        }

                        if (voidedHealth < 3) {
                            extractHeartOverlay(graphics, HeartOverlay.VOID_PURIFICATION, position, voidedHealth % 2 != 0, false, reversed);
                        }
                    }

                    voidedHealth = Math.max(0.0F, voidedHealth - 2.0F);
                } else break;
            }
        }
    }

    public static void extractOuterVoidWarningOverlay(GuiGraphicsExtractor graphics, LivingEntity entity, List<Vector2i> positions, float maxHealth, boolean reversed, ClientsideVariables.OuterVoidWarning warning) {
        if (VoidManager.hasOuterVoidTicks(entity)) {
            float toBeVoidedHealth = maxHealth;

            float outerVoidTicks = VoidManager.getOuterVoidTicks(entity);
            float maxOuterVoidTicks = VoidManager.maxOuterVoidTicks(entity);

            float progress = outerVoidTicks / maxOuterVoidTicks;

            int maxIntervals = 4;
            int interval = (int) (progress * maxIntervals);

            if (interval > warning.lastInterval && interval < maxIntervals) {
                if (warning.player) {
                    entity.playSound(EnderscapeUiSounds.HEALTH_OUTER_VOID_WARNING, (float) interval / (float) maxIntervals + 0.5F, 1.0F);
                    variables.outerVoidIntensity = (float) interval / (float) maxIntervals;
                }

                warning.lastInterval = interval;
                warning.warningTimestamp = entity.tickCount;
            }

            if (reversed) {
                positions = positions.reversed();
            }

            float intervalProgress = 1.0F / maxIntervals;
            float nextIntervalProgress = Math.min((interval + 1) * intervalProgress, 1.0F);
            float flashDuration = ((nextIntervalProgress - progress) * maxOuterVoidTicks) / 2.0F;

            float remainder = entity.tickCount - warning.warningTimestamp;

            if (remainder <= flashDuration && remainder >= 0) {
                for (Vector2i position : positions) {
                    if (toBeVoidedHealth > 0.0F) {
                        extractHeartOverlay(graphics, HeartOverlay.OUTER_VOID_WARNING, position, toBeVoidedHealth == 1, false, reversed);
                        toBeVoidedHealth = Math.max(0.0F, toBeVoidedHealth - 2.0F);
                    } else break;
                }
            }
        } else {
            warning.lastInterval = 0;
        }
    }

    private static void extractHeartOverlay(GuiGraphicsExtractor graphics, HeartOverlay overlay, Vector2i position, boolean half, boolean hardcore, boolean reversed) {
        graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                overlay.getSprite(half, hardcore, reversed),
                position.x(),
                position.y(),
                9,
                9,
                ARGB.white(1.0F)
        );
    }

    public static boolean cameraInVoidLachryma() {
        final Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level == null) {
            return false;
        } else {
            Camera camera = minecraft.gameRenderer.mainCamera();
            Camera.NearPlane plane = camera.getNearPlane(minecraft.options.fov().get());

            for (Vec3 point : Arrays.asList(plane.forward, plane.getTopLeft(), plane.getTopRight(), plane.getBottomLeft(), plane.getBottomRight())) {
                Vec3 offsetPos = camera.position().add(point);
                BlockPos checkPos = BlockPos.containing(offsetPos);
                FluidState fluid = minecraft.level.getFluidState(checkPos);

                if (fluid.is(EnderscapeFluidTags.VOID_LACHRYMA)) {
                    if (offsetPos.y <= fluid.getHeight(minecraft.level, checkPos) + checkPos.getY()) {
                        return true;
                    }
                }
            }

            return false;
        }
    }
}