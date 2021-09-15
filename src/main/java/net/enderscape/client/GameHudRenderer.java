package net.enderscape.client;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.enderscape.Enderscape;
import net.enderscape.interfaces.SendsMessage;
import net.enderscape.util.EndMath;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class GameHudRenderer extends DrawableHelper {

    private final MinecraftClient client = MinecraftClient.getInstance();
    private final DecimalFormat df1 = new DecimalFormat("0.0");

    private int messageAlpha;
    private TranslatableText crosshairMessage;

    private int width;
    private int height;
    private int crosshairColor;

    public static void init() {
        GameHudRenderer renderer = new GameHudRenderer();

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (!client.isPaused()) {
                renderer.tick();
            }
        });

        HudRenderCallback.EVENT.register(renderer::render);
    }

    private List<String> getLeftText() {
        List<String> list = Lists.newArrayList();
        if (Enderscape.isDevelopment()) {
            list.addAll(getClientInfo());
            list.addAll(getPlayerInfo());
        }
        return list;
    }

    private List<String> getClientInfo() {
        ClientPlayerEntity player = client.player;
        List<String> list = Lists.newArrayList();

        int fps = MinecraftClient.currentFps;

        list.add("- Client Info -");

        list.add("FPS: " + fps);
        if (client.world != null) {
            assert player != null;
            BlockPos pos = player.getBlockPos();
            list.add("XYZ: " + pos.getX() + " / " + pos.getY() + " / " + pos.getZ());
        }

        list.add("Mouse X: " + (int) (client.mouse.getX() / client.getWindow().getScaleFactor()));
        list.add("Mouse Y: " + (int) (client.mouse.getY() / client.getWindow().getScaleFactor()));

        assert player != null;
        float f = (float) (player.getX() - 0);
        float h = (float) (player.getZ() - 0);

        int distance = (int) EndMath.sqrt(f * f + h * h);

        list.add("Distance from center: " + distance);

        return list;
    }

    private List<String> getPlayerInfo() {
        ClientPlayerEntity player = client.player;
        List<String> list = Lists.newArrayList();

        if (client.world != null && client.player != null) {
            Vec3d vel;
            if (player.hasVehicle()) {
                vel = Objects.requireNonNull(player.getVehicle()).getVelocity();
            } else {
                vel = player.getVelocity();
            }

            double x = vel.getX();
            double y = vel.getY();
            double z = vel.getZ();

            list.add("- Player Info -");

            list.add("X Velocity: " + df1.format(x));
            list.add("Y Velocity: " + df1.format(y));
            list.add("Z Velocity: " + df1.format(z));

            list.add("Fall Distance: " + (int) player.fallDistance);

            list.add("Pitch: " + (int) EndMath.wrapDegrees(player.getPitch()));
            list.add("Yaw: " + (int) EndMath.wrapDegrees(player.getYaw()));
        }

        return list;
    }

    private int fixedMessageAlpha() {
        int l = messageAlpha * 256 / 10;
        l = Math.min(l, 255);
        return l;
    }

    private void drawCrosshairMessage(MatrixStack matrix, int l) {
        TextRenderer render = client.textRenderer;
        int x = width / 2 - (render.getWidth(getCrosshairMessage()) / 2);
        int y = height / 2 - 20;
        render.drawWithShadow(matrix, getCrosshairMessage(), x, y, crosshairColor + (l << 24));
    }

    @Nullable
    private TranslatableText getCrosshairMessage() {
        return messageAlpha > 0 ? crosshairMessage : null;
    }

    private void setCrosshairMessage(TranslatableText text, int color) {
        crosshairColor = color;
        crosshairMessage = text;
    }

    private boolean shouldDisplay(ClientPlayerEntity player) {
        ItemStack stack = player.getInventory().getMainHandStack();
        Item item = stack.getItem();
        if (item instanceof SendsMessage mes && mes.canDisplay(stack, player)) {
            TranslatableText message = mes.getMessage(stack, player);
            if (message != null) {
                setCrosshairMessage(mes.getMessage(stack, player), mes.getColor(stack, player));
                return true;
            }
        }
        return false;
    }

    private void renderDebugText(MatrixStack matrix) {
        List<String> list = getLeftText();

        for (int i = 0; i < list.size(); ++i) {
            String string = list.get(i);
            if (!Strings.isNullOrEmpty(string)) {
                int y = 2 + 9 * i;

                int w = client.textRenderer.getWidth(string);
                fill(matrix, 1, y - 1, w + 2, y + 8, -1873784752);

                client.textRenderer.draw(matrix, string, 2, y, 14737632);
            }
        }
    }

    private void renderCrosshairText(MatrixStack matrix) {
        client.getProfiler().push("enderscapeCrosshair");
        GameOptions options = client.options;
        matrix.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        if (options.getPerspective().isFirstPerson()) {
            if (getCrosshairMessage() != null && messageAlpha > 0) {
                assert client.player != null;
                if (!client.player.isSpectator()) {
                    drawCrosshairMessage(matrix, fixedMessageAlpha());
                }
            }
        }
        RenderSystem.disableBlend();
        matrix.pop();
        client.getProfiler().pop();
    }

    public void tick() {
        width = client.getWindow().getScaledWidth();
        height = client.getWindow().getScaledHeight();

        if (client.player != null) {
            ClientPlayerEntity player = client.player;
            HitResult result = client.crosshairTarget;
            if (shouldDisplay(player)) {
                boolean bl = result instanceof EntityHitResult;
                if (bl && ((EntityHitResult) result).getEntity() != null) {
                    messageAlpha = (int) EndMath.lerp(0.5F, messageAlpha, 2);
                } else {
                    messageAlpha = (int) EndMath.lerp(0.12F, messageAlpha, 40);
                }
            } else {
                messageAlpha = (int) EndMath.lerp(0.5F, messageAlpha, 0);
            }
        }
    }

    public void render(MatrixStack matrix, float delta) {
        if (client.player != null) {
            boolean f3 = client.options.debugEnabled;
            boolean hidden = client.options.hudHidden;
            if (!hidden) {
                renderCrosshairText(matrix);
                if (!f3) {
                    renderDebugText(matrix);
                }
            }
        }
    }
}