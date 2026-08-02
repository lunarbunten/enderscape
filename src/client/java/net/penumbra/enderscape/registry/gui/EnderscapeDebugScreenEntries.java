package net.penumbra.enderscape.registry.gui;

import net.minecraft.client.gui.components.debug.DebugEntryNoop;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.resources.Identifier;
import net.penumbra.enderscape.Enderscape;
import net.penumbra.enderscape.gui.debug.MusicDebugEntry;
import net.penumbra.enderscape.gui.debug.PlayerAttachmentsDebugEntry;
import net.penumbra.enderscape.gui.debug.PlayerSpeedDebugEntry;
import net.penumbra.enderscape.gui.debug.ServerStructureMusicDebugEntry;

public class EnderscapeDebugScreenEntries {
    public static final Identifier MUSIC = DebugScreenEntries.register(Enderscape.id("music"), new MusicDebugEntry());
    public static final Identifier PLAYER_SPEED = DebugScreenEntries.register(Enderscape.id("player_speed"), new PlayerSpeedDebugEntry());
    public static final Identifier SERVER_STRUCTURE_MUSIC = DebugScreenEntries.register(Enderscape.id("server_structure_music"), new ServerStructureMusicDebugEntry());
    public static final Identifier PLAYER_ATTACHMENTS = DebugScreenEntries.register(Enderscape.id("player_attachments"), new PlayerAttachmentsDebugEntry());

    public static final Identifier MAGNIA_SPROUT_RANGE = DebugScreenEntries.register(Enderscape.id("magnia_sprout_range"), new DebugEntryNoop());
}
