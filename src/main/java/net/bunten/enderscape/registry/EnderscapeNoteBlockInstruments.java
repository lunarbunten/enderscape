package net.bunten.enderscape.registry;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

import static net.bunten.enderscape.Enderscape.registerSoundEventHolder;

public enum EnderscapeNoteBlockInstruments {
    SYNTH_BASS("synth_bass", NoteBlockInstrument.Type.BASE_BLOCK),
    SYNTH_BELL("synth_bell", NoteBlockInstrument.Type.BASE_BLOCK);

    private final String string;
    private final Holder<SoundEvent> soundEvent;
    private final NoteBlockInstrument.Type type;

    EnderscapeNoteBlockInstruments(String name, NoteBlockInstrument.Type type) {
        this.string = "enderscape_" + name;
        this.soundEvent = registerSoundEventHolder("block.note_block.enderscape_" + name);
        this.type = type;
    }

    public NoteBlockInstrument.Type getType() {
        return type;
    }

    public Holder<SoundEvent> getSoundEvent() {
        return soundEvent;
    }

    public String getString() {
        return string;
    }

    public NoteBlockInstrument get() {
        return NoteBlockInstrument.valueOf(this.name());
    }
}