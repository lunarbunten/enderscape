package net.penumbra.enderscape.gui.debug;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.manager.StructureMusicManager;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static net.penumbra.enderscape.manager.StructureMusicManager.playerStructures;
import static net.penumbra.enderscape.manager.StructureMusicManager.playerTimers;

@Environment(EnvType.CLIENT)
public class ServerStructureMusicDebugEntry implements DebugScreenEntry {

    private static final Identifier GROUP = Enderscape.id("structure_music");

    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level level, @Nullable LevelChunk clientChunk, @Nullable LevelChunk chunk) {
        if (level == null || level.getServer() == null) return;

        List<String> list = Lists.newArrayList();

        list.add("Global structure update timer: " + (StructureMusicManager.structureChangeTimer / 20));
        list.add(ChatFormatting.UNDERLINE + "All Players");

        level.getServer().getAllLevels().forEach(server -> server.players().forEach(player -> {
            String value = "";
            boolean addedTimers = false;

            if (playerTimers.containsKey(player)) {
                value += ": " + (playerTimers.get(player) / 20);
                addedTimers = true;
            }

            if (playerStructures.containsKey(player)) {
                String prefix = "";
                if (addedTimers) {
                    prefix += " | ";
                }
                value += prefix + playerStructures.get(player);
            }

            if (!value.isEmpty()) {
                list.add(player.getPlainTextName() + value);
            }
        }));

        displayer.addToGroup(GROUP, list);
    }
}
