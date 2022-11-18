package net.bunten.enderscape.client.hud;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.bunten.enderscape.client.EnderscapeClient;
import net.bunten.enderscape.config.Config;
import net.bunten.enderscape.mixin.client.MusicManagerAccess;
import net.bunten.enderscape.util.MathUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class DebugHud extends HudElement {

    public DebugHud() {
        super(RenderPhase.AFTER_HUD);
    }

    private final DecimalFormat df1 = new DecimalFormat("0.0");

    private String shorten(String value, String r) {
        value = value.replace(".ogg", "");
        char[] array = value.toCharArray();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == r.charAt(0)) {
                String first = String.valueOf(value.subSequence(0, i));
                value = value.replaceFirst(first, "");
                array = value.toCharArray();
                i = 0;
            }
        }
        return value.replace(r, "");
    }

    private LinkedHashMap<List<String>, Boolean> getDebugText() {
        var map = new LinkedHashMap<List<String>, Boolean>();
        map.put(getClientInfo(), Config.DEBUG.clientInfo());
        map.put(getMusicInfo(), Config.DEBUG.musicInfo());
        map.put(getPlayerInfo(), Config.DEBUG.playerInfo());
        return map;
    }

    private List<String> getClientInfo() {
        List<String> list = Lists.newArrayList();

        int fps = Minecraft.fps;

        list.add("Client");
        list.add("  fps: " + fps);
        list.add("  guiScale: " + client.options.guiScale().get());
        list.add("  renderDistance: " + client.options.renderDistance().get());
        list.add("  postMirrorUseTicks: " + EnderscapeClient.postMirrorUseTicks);

        return list;
    }

    private List<String> getMusicInfo() {
        List<String> list = Lists.newArrayList();

        MusicManagerAccess access = (MusicManagerAccess) (client.getMusicManager());
        var current = access.getCurrentMusic();

        list.add("Music");
        list.add("  Volume: " + (int) ((client.options.getSoundSourceVolume(SoundSource.MUSIC)) * 100) + "%");
        list.add("  Playing: " + client.getSoundManager().isActive(current));
        list.add("  Current event: " + (current != null ? shorten(current.getLocation().getPath(), ".") : "null"));
        list.add("  Current track: " + (current != null ? shorten(current.getSound().getPath().getPath(), "/") : "null"));
        list.add("  Next event: " + shorten(client.getSituationalMusic().getEvent().getLocation().getPath(), "."));
        list.add("    getMinDelay: " + client.getSituationalMusic().getMinDelay());
        list.add("    getMaxDelay: " + client.getSituationalMusic().getMaxDelay());
        list.add("    replaceCurrentMusic: " + client.getSituationalMusic().replaceCurrentMusic());
        list.add("  Playing next event: " + client.getMusicManager().isPlayingMusic(client.getSituationalMusic()));
        list.add("  Delay: " + access.getNextSongDelay() + " (" + + access.getNextSongDelay() / 20 / 60 + ":" + access.getNextSongDelay() / 20 % 60 + ")");

        return list;
    }

    private List<String> getPlayerInfo() {
        LocalPlayer player = client.player;
        List<String> list = Lists.newArrayList();

        if (client.level != null && player != null) {
            Vec3 vel;
            if (player.isPassenger()) {
                vel = Objects.requireNonNull(player.getVehicle()).getDeltaMovement();
            } else {
                vel = player.getDeltaMovement();
            }

            list.add("Player");

            list.add("  BlockPos: " + player.blockPosition().toShortString());
            list.add("  Dimension: " + player.getLevel().dimension().location().toString());

            float f = (float) (player.getX() - 0);
            float h = (float) (player.getZ() - 0);
    
            list.add("  Center Distance: " + (int) MathUtil.sqrt(f * f + h * h));

            list.add("  Velocity");

            list.add("    x: " + df1.format(vel.x));
            list.add("    y: " + df1.format(vel.y));
            list.add("    z: " + df1.format(vel.z));
            list.add("    lengthSqr: " + df1.format(vel.lengthSqr()));
            list.add("    horizontalDistanceSqr: " + df1.format(vel.horizontalDistanceSqr()));

            list.add("  fallDistance: " + (int) player.fallDistance);

            list.add("  XY Rotation: " + (int) MathUtil.wrapDegrees(player.getXRot()) + " / " + (int) MathUtil.wrapDegrees(player.getYRot()));
        }

        return list;
    }

    public String getLongestString(List<String> list) {
        String longest = "";
        for (String string : list) {
            if (string.length() > longest.length()) {
                longest = string;
            }
        }
        return longest;
    }

    public void render(PoseStack matrix, float delta) {
        if (client.player == null || client.options.renderDebug || client.options.hideGui || !Config.DEBUG.displayDebugHud()) {
            return;
        }

        matrix.pushPose();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        float total = switch(client.options.guiScale().get()){
            case 3 -> 0.6666F;
            case 4 -> 0.5F;
            default -> 1;
        };

        matrix.scale(total, total, total);

        int x = 2;
        int y = 2;

        for (List<String> list : getDebugText().keySet()) {
            if (!getDebugText().get(list)) continue;
            
            fill(matrix, x, y, x + client.font.width(getLongestString(list)) + 10, y + (list.size() * 12) + 4, 0x4F052E60);
            
            for (String string : list) {
                if (!string.isEmpty()) {
                    client.font.draw(matrix, string, x + 4, y + 4, 0xEFFFFFFF);
                }
                y += 12;
            }
            y += 6;
        }
        
        matrix.popPose();
    }
}