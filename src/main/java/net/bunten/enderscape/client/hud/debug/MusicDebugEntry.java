package net.bunten.enderscape.client.hud.debug;

import com.google.common.collect.Lists;
import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.mixin.MusicManagerAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenDisplayer;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class MusicDebugEntry implements DebugScreenEntry {

    private static final Minecraft CLIENT = Minecraft.getInstance();
    private static final ResourceLocation GROUP = Enderscape.id("music");

    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level level, @Nullable LevelChunk clientChunk, @Nullable LevelChunk chunk) {
        List<String> list = Lists.newArrayList();

        String key = CLIENT.getMusicManager().getCurrentMusicTranslationKey();
        list.add(ChatFormatting.UNDERLINE + "Music: " + (key != null ? Component.translatable(key.replace("/", ".")).getString() : "None"));

        Optional.of(CLIENT.getSituationalMusic()).ifPresent(info -> {
            MusicManagerAccess manager = (MusicManagerAccess) (CLIENT.getMusicManager());

            String nextEvent = shorten(info.music().event().value().location().getPath(), ".");
            boolean sameAsNow = CLIENT.getMusicManager().isPlayingMusic(info.music());
            String delayRange = info.music().minDelay() + " | " + info.music().maxDelay();
            String delayTimer = manager.getNextSongDelay() + " (" + String.format("%02d", manager.getNextSongDelay() / 20 / 60) + ":" + String.format("%02d", (manager.getNextSongDelay() / 20) % 60) + ")";

            list.add("Next event: " + nextEvent + " | same as now: " + sameAsNow);
            list.add("Delay range: " + delayRange);
            list.add("Active delay: " + delayTimer);
        });

        displayer.addToGroup(GROUP, list);
    }

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
}
