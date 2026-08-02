package net.penumbra.enderscape.gui.debug;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.penumbra.enderscape.Enderscape;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static net.penumbra.enderscape.registry.entity.EnderscapeAttachments.ATTACHMENT_TYPES;

@Environment(EnvType.CLIENT)
public class PlayerAttachmentsDebugEntry implements DebugScreenEntry {

    private static final Minecraft CLIENT = Minecraft.getInstance();
    private static final Identifier GROUP = Enderscape.id("player_attachments");

    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level level, @Nullable LevelChunk clientChunk, @Nullable LevelChunk chunk) {
        LocalPlayer player = CLIENT.player;
        List<String> list = Lists.newArrayList();

        if (CLIENT.level != null && player != null) {
            ATTACHMENT_TYPES.forEach(type -> {
                if (player.hasAttached(type)) {
                    list.add(type.identifier() + ": " + player.getAttached(type));
                }
            });
        }

        displayer.addToGroup(GROUP, list);
    }
}
