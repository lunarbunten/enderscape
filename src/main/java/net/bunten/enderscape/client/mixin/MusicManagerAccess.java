package net.bunten.enderscape.client.mixin;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@OnlyIn(Dist.CLIENT)
@Mixin(MusicManager.class)
public interface MusicManagerAccess {

    @Accessor
    SoundInstance getCurrentMusic();

    @Accessor
    int getNextSongDelay();
}