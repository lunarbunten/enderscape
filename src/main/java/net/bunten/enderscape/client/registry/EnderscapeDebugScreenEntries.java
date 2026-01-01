package net.bunten.enderscape.client.registry;

import net.bunten.enderscape.Enderscape;
import net.bunten.enderscape.client.hud.debug.MusicDebugEntry;
import net.bunten.enderscape.client.hud.debug.PlayerSpeedDebugEntry;
import net.minecraft.client.gui.components.debug.DebugEntryNoop;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.resources.ResourceLocation;

public class EnderscapeDebugScreenEntries {
    public static final ResourceLocation MUSIC = DebugScreenEntries.register(Enderscape.id("music"), new MusicDebugEntry());
    public static final ResourceLocation PLAYER_SPEED = DebugScreenEntries.register(Enderscape.id("player_speed"), new PlayerSpeedDebugEntry());

    public static final ResourceLocation MAGNIA_SPROUT_RANGE = DebugScreenEntries.register(Enderscape.id("magnia_sprout_range"), new DebugEntryNoop());
}
