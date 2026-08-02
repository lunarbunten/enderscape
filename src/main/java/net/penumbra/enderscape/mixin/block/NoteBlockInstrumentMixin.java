package net.penumbra.enderscape.mixin.block;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static net.penumbra.enderscape.Enderscape.registerSoundEventHolder;

@Mixin(NoteBlockInstrument.class)
public enum NoteBlockInstrumentMixin {
    ENDERSCAPE_SYNTH_BASS("enderscape_synth_bass", registerSoundEventHolder("block.note_block.enderscape_synth_bass"), NoteBlockInstrument.Type.BASE_BLOCK),
    ENDERSCAPE_SYNTH_BELL("enderscape_synth_bell", registerSoundEventHolder("block.note_block.enderscape_synth_bell"), NoteBlockInstrument.Type.BASE_BLOCK);

    @Shadow
    NoteBlockInstrumentMixin(final String name, final Holder<SoundEvent> soundEvent, final NoteBlockInstrument.Type type) {
    }
}