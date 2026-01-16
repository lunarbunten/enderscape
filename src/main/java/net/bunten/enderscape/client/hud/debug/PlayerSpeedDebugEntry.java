package net.bunten.enderscape.client.hud.debug;

import com.google.common.collect.Lists;
import net.bunten.enderscape.Enderscape;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class PlayerSpeedDebugEntry implements DebugScreenEntry {

    private static final Minecraft CLIENT = Minecraft.getInstance();
    private static final Identifier GROUP = Enderscape.id("player_speed");

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0");

    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level level, @Nullable LevelChunk clientChunk, @Nullable LevelChunk chunk) {
        LocalPlayer player = CLIENT.player;
        List<String> list = Lists.newArrayList();

        if (CLIENT.level != null && player != null) {
            Vec3 delta = player.isPassenger() ? Objects.requireNonNull(player.getVehicle()).getDeltaMovement() : player.getDeltaMovement();

            String velX = DECIMAL_FORMAT.format(delta.x);
            String velY = DECIMAL_FORMAT.format(delta.y);
            String velZ = DECIMAL_FORMAT.format(delta.z);

            list.add(ChatFormatting.UNDERLINE + "Speed: " + velX + " / " + velY + " / " + velZ);
            list.add("Length squared: " + DECIMAL_FORMAT.format(delta.lengthSqr()));
            list.add("Horizontal distance squared: " + DECIMAL_FORMAT.format(delta.horizontalDistanceSqr()));
        }

        displayer.addToGroup(GROUP, list);
    }
}
